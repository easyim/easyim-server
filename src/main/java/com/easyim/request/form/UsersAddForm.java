package com.easyim.request.form;


import com.broker.base.protocol.ProtocolMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class UsersAddForm extends ProtocolMessage {
    @NotBlank(message = "用户名字不能为空")
    private String userName = ""; // 要添加的用户名字
}
