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
public class UserRefreshTokenDTO extends SocketHideProtocolMessage {
    private String auid = ""; // 用户auid
    private String token = ""; // 用户token
}
