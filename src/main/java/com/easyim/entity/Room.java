package com.easyim.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 组
 * </p>
 *
 * @author sun
 * @since 2019-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * UUID格式的ID
     */
    @TableField("uid")
    private String uid;

    /**
     * SDK APP KEY
     */
    @TableField("app_key")
    private String appKey;

    /**
     * 群名
     */
    @TableField("name")
    private String name;

    /**
     * 群主,用户auid
     */
    @TableField("owner")
    private String owner;

    /**
     * 群公告
     */
    @TableField("announce")
    private String announce;

    /**
     * 群简介
     */
    @TableField("introduce")
    private String introduce;

    /**
     * 邀请文字
     */
    @TableField("invite_text")
    private String inviteText;

    /**
     * 群LOGO,头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 群加入模式
     */
    @TableField("conf_joinmode")
    private String confJoinmode;

    /**
     * 群员被邀请方式
     */
    @TableField("conf_beinvite")
    private String confBeinvite;

    /**
     * 群员邀请权限(OWNER:仅群主;ALL:任何人也可以)
     */
    @TableField("conf_inviteother")
    private String confInviteother;

    /**
     * 群信息更新权限(OWNER:仅群主;ALL:任何人也可以)
     */
    @TableField("conf_update")
    private String confUpdate;

    /**
     * 群最大成员数量
     */
    @TableField("max_member")
    private Integer maxMember;

    /**
     * 文件状态(NORMAL:正常;DEL: 已解散)
     */
    @TableField("status")
    private String status;

}
