package com.easyim.service.app;

import com.annotation.MethodFor;
import com.annotation.TopicFor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broker.base.protocol.response.Resp;
import com.broker.base.utils.ObjectUtils;
import com.commom.Topic;
import com.easyim.response.dto.UserListDTO;
import com.easyim.response.dto.UserRefreshTokenDTO;
import com.easyim.entity.User;
import com.easyim.request.form.UserAddForm;
import com.easyim.request.form.UserRefreshTokenForm;
import com.easyim.request.form.UserUpdateForm;
import com.easyim.request.form.UserUpdateTokenForm;
import com.easyim.mapper.UserMapper;

import com.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/2/21 13:21
 * Description: easyim
 **/


@Slf4j
@TopicFor(value = Topic.APP_USER.name)
@Service
//public class UsersService  {
public class UserService extends ServiceImpl<UserMapper, User>  {
    @MethodFor(value = Topic.APP_USER.METHOD.ADD, consumer = UserAddForm.class)
    public Resp<String> add(@Valid UserAddForm userAddForm){
        log.info(" add: " + ObjectUtils.json(userAddForm));
//        log.info(" get userName: " + userAddForm.getUserJwt().getName());
        this.save(ObjectUtils.copy(userAddForm, User.class).setUid(UUIDUtils.gen()));
        return Resp.ok("添加成功");
    }



    public Resp<String> updateUser(UserUpdateForm updateForm) {
        this.lambdaUpdate()
                .set(ObjectUtils.strNotEmpty(updateForm.getName()), User::getName, updateForm.getName())
                .set(ObjectUtils.strNotEmpty(updateForm.getAvatar()), User::getAvatar, updateForm.getAvatar())
                .set(ObjectUtils.strNotEmpty(updateForm.getSign()), User::getSign, updateForm.getSign())
                .set(ObjectUtils.strNotEmpty(updateForm.getEmail()), User::getEmail, updateForm.getEmail())
                .set(ObjectUtils.strNotEmpty(updateForm.getBirth()), User::getBirth, updateForm.getBirth())
                .set(ObjectUtils.strNotEmpty(updateForm.getMobile()), User::getMobile, updateForm.getMobile())
                .set(User::getGender, updateForm.getGender())
                .set(ObjectUtils.strNotEmpty(updateForm.getEx()), User::getEx, updateForm.getEx())
                // where
                .eq(User::getAuid, updateForm.getAuid());

        return Resp.ok("更新成功");
    }

    public Resp<String> updateToken(UserUpdateTokenForm updateTokenForm) {
        this.lambdaUpdate()
                .set(User::getToken, updateTokenForm.getToken())
                // where
                .eq(User::getAuid, updateTokenForm.getAuid());

        return Resp.ok("更新成功");
    }

    public Resp<UserRefreshTokenDTO> refreshToken(UserRefreshTokenForm refreshTokenForm) {
        String token = UUIDUtils.gen();
        this.lambdaUpdate()
                .set(User::getToken, token)
                // where
                .eq(User::getAuid, refreshTokenForm.getAuid());

        return Resp.ok(new UserRefreshTokenDTO()
                .setAuid(refreshTokenForm.getAuid())
                .setToken(token));
    }

    public Resp<List<UserListDTO>> listUser(List<String> auids) {
        List<User> users = this.lambdaQuery().in(User::getAuid, auids).list();
        List<UserListDTO> dtos = users.stream().map(v->ObjectUtils.copy(v, UserListDTO.class)).collect(Collectors.toList());
        return Resp.ok(dtos);
    }
}
