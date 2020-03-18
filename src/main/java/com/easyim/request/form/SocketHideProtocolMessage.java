package com.easyim.request.form;

import com.broker.base.auth.UserJwt;
import com.broker.base.protocol.ProtocolMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/13 16:14
 * Description: easyim
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@Data
public class SocketHideProtocolMessage extends ProtocolMessage {
    @ApiModelProperty(hidden = true)
    private String clientId = "";
    @ApiModelProperty(hidden = true)
    private String requestId = "";
    @ApiModelProperty(hidden = true)
    private Long timestamp = 0L;
    @ApiModelProperty(hidden = true)
    private String method = "";
    @ApiModelProperty(hidden = true)
    private String jwt = "";
    @ApiModelProperty(hidden = true)
    public UserJwt getUserJwt() {
        return super.getUserJwt();
    }
}
