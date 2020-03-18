package com.easyim.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.broker.base.protocol.response.Resp;
import com.commom.Topic;
import com.easyim.response.dto.UserListDTO;
import com.easyim.response.dto.UserRefreshTokenDTO;
import com.easyim.request.form.UserAddForm;
import com.easyim.request.form.UserRefreshTokenForm;
import com.easyim.request.form.UserUpdateForm;
import com.easyim.request.form.UserUpdateTokenForm;
import com.easyim.service.app.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * IM用户相关业务
 */
@RestController
@RequestMapping(Topic.APP_USER.base_uri)
public class UserController extends ApiController {
    @Autowired
    private UserService userService;


    /**
     * APP添加IM用户
     */
    @PostMapping("/"+Topic.APP_USER.METHOD.ADD)
    public Resp<String> addUser(@RequestBody @Valid UserAddForm addForm) {
        return userService.add(addForm);
    }

    /**
     * APP更新IM用户
     */
    @PostMapping("/"+Topic.APP_USER.METHOD.UPDATE)
    public Resp<String> updateUser(@RequestBody @Valid UserUpdateForm updateForm) {
        return userService.updateUser(updateForm);
    }


    /**
     * APP获取IM用户
     */
    @PostMapping("/"+Topic.APP_USER.METHOD.LIST)
    public Resp<List<UserListDTO>> listUsers(@RequestBody @Valid List<String> auids) {
        return userService.listUser(auids);
    }

    /**
     * APP更新用户token
     */
    @PostMapping("/"+Topic.APP_USER.METHOD.UPDATE_TOKEN)
    public Resp<String> updateUserToken(@RequestBody @Valid UserUpdateTokenForm userUpdateTokenForm) {
        return userService.updateToken(userUpdateTokenForm);
    }

    /**
     * APP刷新用户token
     */
    @PostMapping("/"+Topic.APP_USER.METHOD.REFRESH_TOKEN)
    public Resp<UserRefreshTokenDTO> refreshUserToken(@RequestBody @Valid UserRefreshTokenForm userRefreshTokenForm) {
        return userService.refreshToken(userRefreshTokenForm);
    }
}
