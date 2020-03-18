package com.easyim.response.dto;


import com.easyim.request.form.SocketHideProtocolMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class ClientListDTO extends SocketHideProtocolMessage {
    private String clientId = ""; // 用户登录的客户端id
    private String auid = ""; // 用户auid
    private String name = ""; // 账号昵称
}
