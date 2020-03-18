package com.commom;

public class DBConst {

    //  通用数据状态(用于逻辑删除)
    public enum EntryStatus{
        NORMAL("正常"),
        DEL("已删除");
        private String mean = "";
        EntryStatus(String mean){
            this.mean = mean;
        }

    }

    //  MessageStableOffline消息状态
    public enum MessageStableOfflineStatus{
        OFFLINE("离线中"),
        SENT("已发送");
        private String mean = "";
        MessageStableOfflineStatus(String mean){
            this.mean = mean;
        }

    }


    //  MessageStableOffline消息状态
    public enum MessageWay{
        P2P("点对点消息"),
        P2R("点对群消息"),
        P2HR("点对超大群消息");
        private String mean = "";
        MessageWay(String mean){
            this.mean = mean;
        }

    }
}
