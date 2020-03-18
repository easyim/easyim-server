package com.easyim.request.form;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;


@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class UserUpdateForm extends SocketHideProtocolMessage {
    @NotBlank(message = "用户auid不能为空")
    private String auid = ""; // 要添加的用户uid

    private String name = ""; // 账号昵称
    private String avatar = ""; // 用户头像
    private String sign = ""; // 用户签名
    private String email = ""; // 用户email
    private String birth = ""; // 用户生日
    private String mobile = ""; // 用户mobile
    @Range(min = 0, max = 2)
    private Integer gender = 0; // 用户性别，0表示未知，1表示男，2女表示女
    private String ex = ""; // 用户扩展字段
}
