package com.easyim.filter;


import com.broker.base.protocol.request.RequestMessage;
import com.broker.base.protocol.response.ResponseMessage;

public interface ProtocolHandlerInterceptor {
    boolean preHandle(RequestMessage request, ResponseMessage response, Object handler)
            throws Exception;
    void afterHandler(RequestMessage request, ResponseMessage response, Object handler)
            throws Exception;
}
