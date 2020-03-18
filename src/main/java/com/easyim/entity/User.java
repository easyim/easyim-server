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
 * 用户表
 * </p>
 *
 * @author sun
 * @since 2019-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 内部ID
     */
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
     * APP用户账号ID(与客户系统对接的用户ID)
     */
    @TableField("auid")
    private String auid;

    /**
     * 账号昵称
     */
    @TableField("name")
    private String name;

    /**
     * 用户头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 用户登录token
     */
    @TableField("token")
    private String token;

    /**
     * 用户登录token 过期时间
     */
    @TableField("token_expire_date")
    private Date tokenExpireDate;

    /**
     * 用户签名
     */
    @TableField("sign")
    private String sign;

    /**
     * 用户email
     */
    @TableField("email")
    private String email;

    /**
     * 用户生日
     */
    @TableField("birth")
    private String birth;

    /**
     * 用户mobile
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 用户性别，0表示未知，1表示男，2女表示女
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 用户扩展字段
     */
    @TableField("ex")
    private String ex;

    /**
     * 用户状态(NORMAL:正常, DISABLE:封禁)
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

}
