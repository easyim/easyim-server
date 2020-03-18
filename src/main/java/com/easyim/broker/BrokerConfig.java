package com.easyim.broker;

import com.broker.utils.events.EventFactory;
import com.broker.utils.strorage.StorageFactory;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/18 10:26
 * Description: easyim
 **/

@Accessors(chain = true)
@Data
public class BrokerConfig {
    private String storageUsing = StorageFactory.STORAGE_TYPE_REDIS;
    private String redisHost = "localhost";
    private String mqPoint = "";
    private String mqServerUrl = "localhost:9876";
    private String eventUsing = EventFactory.EVENT_TYPE_ROCKETMQ;
    private String signingKey = "ZWFzeWlt";
    private String tokenValid = "10080000"; // 一周(ms)

    public void initSystemProperty(){
        // 配置鉴权
        System.setProperty("broker.auth.signingkey", signingKey);
        System.setProperty("broker.auth.tokenvalid", tokenValid);
        // 配置 redis storage 组件
        System.setProperty("broker.storage.use", storageUsing);
        System.setProperty("broker.storage.redis.host", redisHost);
        // 配置 rocketMQ event 组件
        System.setProperty("broker.event.use", eventUsing);
        System.setProperty("broker.event.mq.server", mqServerUrl);
        System.setProperty("broker.event.mq.point", mqPoint);
    }
}
