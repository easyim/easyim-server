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
 * 平台的APP管理SDK
 * </p>
 *
 * @author sun
 * @since 2019-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SdkApp implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * UUID格式的ID
     */
    @TableField("uid")
    private String uid;

    /**
     * app_key:识别是那个APP
     */
    @TableField("app_key")
    private String appKey;

    /**
     * APP名字
     */
    @TableField("name")
    private String name;

    /**
     * app_secret
     */
    @TableField("app_secret")
    private String appSecret;

    /**
     * 限制的连接数
     */
    @TableField("limit_connection")
    private Integer limitConnection;

    /**
     * app 允许使用时间(天)
     */
    @TableField("allow_use_day")
    private Integer allowUseDay;

    /**
     * app 过期时间
     */
    @TableField("expire_date")
    private Date expireDate;

}
