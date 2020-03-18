package com.easyim.filter;

import com.broker.base.auth.TokenProvider;
import com.broker.base.protocol.ProtocolMessage;
import com.broker.base.protocol.request.RequestMessage;
import com.broker.base.protocol.response.Resp;
import com.broker.base.protocol.response.ResponseMessage;
import com.commom.Topic;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


/**
 *  Topic 请求拦截器, 功能类似springMVC 的 HandlerInterceptor
 *  功能:
 *  1. 可排除不拦截的Topic;
 *  2. 对拦截的Topic, 做jwt检测, 返回jwt信息.
 *  3. 请求重复提交(待完成)
 *  4. 请求的权限管理(待完成)
 * */
@Component
public class TopicInterceptor implements ProtocolHandlerInterceptor{
    // 不拦截的Topic
    private static final List<String> excludeTopic = Arrays.asList(Topic.CONNECTION.name);

    // 若返回true, 继续执行后面的流程
    @Override
    public boolean preHandle(RequestMessage request, ResponseMessage response, Object handler) throws Exception {
        response.setRequestId(request.getRequestId());
        if(excludeTopic.contains(request.getTopic())){
            return true;
        }
        // 不在excludeTopic内的, 检测jwt
        if(!validRequestToken(request.getProtocolMessage())){
            response.setResponse(Resp.failed("token 校验错误."));

            return false;
        }

        return true;
    }

    @Override
    public void afterHandler(RequestMessage request, ResponseMessage response, Object handler) throws Exception {

    }


    // 消息是否已鉴权?
    private boolean validRequestToken(ProtocolMessage message){
        if("".equals(message.getJwt())){
            return false;
        }
        return new TokenProvider().validateToken(message.getJwt());
    }
}
