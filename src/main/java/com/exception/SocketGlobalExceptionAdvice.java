package com.exception;

import com.broker.base.protocol.response.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class SocketGlobalExceptionAdvice {
    public Resp<Object> onHandle(Exception e){
        logs(e);
        return Resp.failed(e.toString());
    }

    private void logs(Exception e){
        log.error(" error: ", e);
    }
}
