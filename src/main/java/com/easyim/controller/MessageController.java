package com.easyim.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.broker.base.protocol.response.Resp;
import com.commom.Topic;
import com.easyim.request.form.MessageSendForm;
import com.easyim.service.app.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * IM用户相关业务
 */
@RestController
@RequestMapping(Topic.APP_MESSAGE.base_uri)
public class MessageController extends ApiController {
    @Autowired
    private MessageService messageService;

    /**
     * APP跟IM用户发送消息
     */
    @PostMapping("/"+Topic.APP_MESSAGE.METHOD.SEND)
    public Resp<String> sendMessage(@RequestBody @Valid MessageSendForm sendForm) {
        return messageService.send(sendForm);
    }
}
