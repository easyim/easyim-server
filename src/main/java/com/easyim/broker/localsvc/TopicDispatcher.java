package com.easyim.broker.localsvc;

import com.annotation.MethodFor;
import com.annotation.TopicFor;
import com.broker.base.protocol.ProtocolMessage;
import com.broker.base.protocol.request.RequestMessage;
import com.broker.base.protocol.response.Resp;
import com.broker.base.protocol.response.ResponseMessage;
import com.broker.base.utils.ObjectUtils;
import com.easyim.filter.ProtocolHandlerInterceptor;
import com.easyim.filter.TopicInterceptor;
import com.exception.FormValidationException;
import com.exception.SocketGlobalExceptionAdvice;
import com.utils.ProtocolAnnotationUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.remoting.common.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/2/21 13:18
 * Description: 转发从客户端接收到的信息到业务层处理
 **/

@Slf4j
@Component
public class TopicDispatcher {
    @Autowired
    private SocketGlobalExceptionAdvice globalExceptionAdvice;
    @Autowired
    private Validator validator;
    @Autowired
    private TopicInterceptor topicInterceptor;

    @Data
    private static class InvokeInfo{
        private Object bean;
        private Method method;
        private MethodFor  methodAnnotation;
        private List<Pair<Object, Boolean>> methodParameterAndInject;

        public boolean isEmpty(){
            return bean == null ||
                    method == null ||
                    methodAnnotation == null ||
                    (methodParameterAndInject != null && methodParameterAndInject.stream().noneMatch(Pair::getObject2));
        }
    }

    public ResponseMessage consumeMessage(RequestMessage requestMessage){
        log.info(" receive eventBus message:" + ObjectUtils.json(requestMessage.getBody()));
        long t1 = System.currentTimeMillis();

        InvokeInfo invokeInfo = getTargetInvokeInfo(requestMessage);
        if(invokeInfo.isEmpty()){
            log.info("Topic:{}, Method: {} Couldn't invoke target method.", requestMessage.getTopic(), requestMessage.getMethod());
            log.info(" time consume:{} ms", System.currentTimeMillis()-t1);
            return ResponseMessage.failed(requestMessage.getRequestId(), "无对应的处理方法");
        }
        // 校验添加了 @Valid 的参数
        Pair<Object, Boolean> methodAndParameter = invokeInfo.methodParameterAndInject.stream().filter(v-> v.getObject2()).findFirst().orElse(null);
        if(methodAndParameter == null){
            return ResponseMessage.failed(requestMessage.getRequestId(), "无对应的处理方法");
        }
        Object parameterInject = methodAndParameter.getObject1();
        Object parameterInjectAfter = ObjectUtils.beans(ObjectUtils.json(requestMessage.getBody()), parameterInject.getClass());
        if(parameterInjectAfter instanceof ProtocolMessage){
            ((ProtocolMessage)parameterInjectAfter).setClientId(requestMessage.getProtocolMessage().getClientId());
        }
        try {
            validForm(parameterInjectAfter, invokeInfo.method, parameterInject.getClass());
        }
        catch (ValidationException e){
            return new ResponseMessage<>()
                    .setRequestId(requestMessage.getProtocolMessage().getRequestId())
                    .setResponse(globalExceptionAdvice.onHandle(e));
        }
        // 注入参数
        for(int index = 0; index < invokeInfo.methodParameterAndInject.size(); index++){
            Pair<Object, Boolean> pair = invokeInfo.methodParameterAndInject.get(index);
            if(pair.getObject1().getClass() == parameterInject.getClass()){
                invokeInfo.methodParameterAndInject.set(index, new Pair<>(parameterInjectAfter, true));
            }
        }
        try {
            ResponseMessage responseMessage = ResponseMessage.succeed(requestMessage.getRequestId());
            // 应用拦截器
            filterApply(requestMessage, responseMessage, (request, response)->{
                // 反射目标方法
                Object res = ProtocolAnnotationUtils.invokeMethod(invokeInfo.bean,
                        invokeInfo.method,
                        invokeInfo.methodParameterAndInject.stream().map(Pair::getObject1).toArray()
                );
                // 返回参数必须为 R.class
                if(!(res instanceof Resp)){
                    log.error("Topic:{}, Method: {} the return type expect:{} but is:{}",
                            request.getTopic(), request.getMethod(), Resp.class, res.getClass());
                    response.setResponse(Resp.failed("方法返回参数格式不是 Resp.class."));
                    return;
                }
                response.setResponse((Resp) res);
            }, invokeInfo.method);


            log.info(" time consume:{} ms", System.currentTimeMillis()-t1);
            return responseMessage;

        }
        catch (Exception e){
            return new ResponseMessage<>()
                    .setRequestId(requestMessage.getProtocolMessage().getRequestId())
                    .setResponse(globalExceptionAdvice.onHandle(e));
        }
    }





    @NonNull
    private InvokeInfo getTargetInvokeInfo(RequestMessage message){
        AtomicReference<Object> result = new AtomicReference<>();
        InvokeInfo invokeInfo = new InvokeInfo();

        ProtocolAnnotationUtils.getBeansWithAnnotation(TopicFor.class)
                .stream()
                .filter(v->v.getObject2().value().equals(message.getTopic()))
                .forEach(beanAndAnnotation->{
                    ProtocolAnnotationUtils.getMethodWithAnnotation(MethodFor.class, beanAndAnnotation.getObject1().getClass()).forEach(methodAndAnnotation->{
                        Method method = methodAndAnnotation.getObject1();
                        MethodFor methodFor = methodAndAnnotation.getObject2();

                        List<Object> params = ProtocolAnnotationUtils.getMethodInstanceParam(method);
                        if(params == null){
                            return;
                        }
                        invokeInfo.setBean(beanAndAnnotation.getObject1());
                        invokeInfo.setMethod(method);
                        if(methodFor.value().equals(message.getMethod())){
                            invokeInfo.setMethodAnnotation(methodFor);
                        }
                        invokeInfo.setMethodParameterAndInject(
                                params.stream().map(v->new Pair<>(v, false)).collect(Collectors.toList())
                        );
                        for(int index = 0; index < invokeInfo.getMethodParameterAndInject().size(); index++){
                            Pair<Object, Boolean> pair = invokeInfo.methodParameterAndInject.get(index);
                            if(pair.getObject1().getClass() == methodFor.consumer()){
                                invokeInfo.getMethodParameterAndInject().set(index, new Pair<>(pair.getObject1(), true));
                            }
                        }
                    });

                });

        return invokeInfo;
    }

    // 应用拦截器
    private void filterApply(RequestMessage requestMessage, ResponseMessage responseMessage,
                                        BiConsumer<RequestMessage, ResponseMessage> handler,
                             Object invokeMethod) throws Exception {
        // TODO: 使用spring 扫描方式获取拦截器.
        List<ProtocolHandlerInterceptor> protocolHandlerInterceptors = Arrays.asList(topicInterceptor);
        boolean continueNextHandler = true;
        // 触发 preHandle 拦截
        for(ProtocolHandlerInterceptor protocolHandlerInterceptor: protocolHandlerInterceptors){
            continueNextHandler = protocolHandlerInterceptor.preHandle(requestMessage, responseMessage, invokeMethod);
            if(!continueNextHandler){
                if(responseMessage.getResponse().ok()){
                    responseMessage.setResponse(Resp.failed("请求被拦截"));
                }
                return;
            }
        }
        handler.accept(requestMessage, responseMessage);
        // 触发 afterHandler 拦截
        for(ProtocolHandlerInterceptor protocolHandlerInterceptor: protocolHandlerInterceptors){
            protocolHandlerInterceptor.afterHandler(requestMessage, responseMessage, invokeMethod);
        }
    }

    private void validForm(Object requestForm, Method method, Class targetParamClz) throws ValidationException{
        Class paramClzWithValid = ProtocolAnnotationUtils.getParamClzWithAnnotationFor(method, Valid.class);
        if(paramClzWithValid != null && paramClzWithValid == targetParamClz){
            try {
                Set<ConstraintViolation<Object>> violations = validator.validate(requestForm);
                if(violations.size() > 0){
                    throw new FormValidationException(violations);
                }
            }
            catch (ValidationException e){
                throw e;
            }
        }
    }


}
