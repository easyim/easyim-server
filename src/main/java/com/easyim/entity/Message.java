package com.easyim.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 普通消息表
 * </p>
 *
 * @author sun
 * @since 2019-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 内部ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 消息ID,UUID格式的ID
     */
    @TableField("uid")
    private String uid;

    /**
     * SDK APP ID
     */
    @TableField("app_key")
    private String appKey;

    /**
     * 消息发送方式(P2P:点对点;P2G:点对多)
     */
    @TableField("way")
    private String way;

    /**
     * 来源用户auid
     */
    @TableField("from_user")
    private String fromUser;

    /**
     * 目标用户auid/目标群uid
     */
    @TableField("to_target")
    private String toTarget;

    /**
     * TEXT,IMAGE,AUDIO,VIDEO,POSITON,FILE,NOTIFY,CUSTOM
     */
    @TableField("type")
    private String type;

    /**
     * 消息体,json格式
     */
    @TableField("body")
    private String body;

    /**
     * 消息选项设置
     */
    @TableField("msg_option")
    private String msgOption;

    /**
     * 消息要AT的用户,json 数组格式
     */
    @TableField("at_users")
    private String atUsers;

    /**
     * 是否离线消息(1:是;0:否)
     */
    @TableField("offline")
    private Integer offline;

    /**
     * 离线消失超时时间(仅offline为1时有效)
     */
    @TableField("offline_expire_date")
    private Date offlineExpireDate;

    /**
     * 消息是否已撤回
     */
    @TableField("cancel")
    private Integer cancel;

    /**
     * 消息撤回时间
     */
    @TableField("cancel_time")
    private Date cancelTime;

    /**
     * 消息状态(NORMAL:正常;DEL: 已删除;CANCEL:已撤回)
     */
    @TableField("status")
    private String status;

    /**
     * 系统备注
     */
    @TableField("system_meno")
    private String systemMeno;

}
