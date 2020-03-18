package com.easyim.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.broker.base.protocol.response.Resp;
import com.commom.Topic;
import com.easyim.response.dto.ClientListDTO;
import com.easyim.service.app.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 客户端相关业务
 */
@RestController
@RequestMapping(Topic.APP_CLIENT.base_uri)
public class ClientController extends ApiController {
    @Autowired
    private ClientService clientService;

    /**
     * APP获取已登录的IM客户端
     */
    @PostMapping("/"+Topic.APP_CLIENT.METHOD.LIST)
    public Resp<List<ClientListDTO>> listClients() {
        return clientService.list();
    }
}
