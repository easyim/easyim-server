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
public class MessageSendForm extends SocketHideProtocolMessage {
    @NotBlank(message = "from不能为空")
    private String formUser = ""; // 用户auid
    @NotBlank(message = "to不能为空")
    private String toTarget = ""; // 用户auid/ 群uid
    @NotBlank(message = "way不能为空")
    private String way = "";// 消息发送方式(P2P:点对点;P2G:点对多)
    @NotBlank(message = "type不能为空")
    private String type = "";//TEXT,IMAGE,AUDIO,VIDEO,POSITION,FILE,NOTIFY,CUSTOM
    @NotBlank(message = "body不能为空")
    private String body = "";//消息体,json格式
    private String msgOption = "";//消息选项设置
    private String at_users = "";//消息要AT的用户,json 数组格式
}
