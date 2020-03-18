package com.easyim.request.form;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;


@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class UserUpdateTokenForm extends SocketHideProtocolMessage {
    @NotBlank(message = "用户auid不能为空")
    private String auid = ""; // 要添加的用户uid
    @NotBlank(message = "用户token不能为空")
    private String token = ""; // 用户登录token

}
