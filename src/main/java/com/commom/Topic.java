package com.commom;


import java.util.Arrays;
import java.util.List;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/2/21 9:28
 * Description: easyim
 **/
public class Topic {
    public static final String TOPIC_PREFIX = "topic.";
    public static final String HTTP_API_PREFIX = "/v1/";

    // 客户端上传错误信息
    public static class ERROR{
        public static final String name = TOPIC_PREFIX +"error";
        public static final String base_uri = HTTP_API_PREFIX +"error";
        public static class METHOD{

        }
    }

    public static class CONNECTION{
        public static final String name = TOPIC_PREFIX +"connection";
//        public static final String base_uri = "connection";
        public static final String base_uri = HTTP_API_PREFIX +"connection";
        public static class METHOD{
               public static final String AUTHORITY_REQUEST = "authority_request";

        }
    }

    public static class APP_USER{
        public static final String name = TOPIC_PREFIX +"user";
//        public static final String base_uri = "user";
        public static final String base_uri = HTTP_API_PREFIX +"user";
        public static class METHOD{
//            public static final String USER_ADD = "user/add";
            public static final String ADD = "add";
            public static final String UPDATE = "update";
            public static final String LIST = "list";
            public static final String UPDATE_TOKEN = "update_token";
            public static final String REFRESH_TOKEN = "refresh_token";
        }
    }


    public static class APP_MESSAGE{
        public static final String name = TOPIC_PREFIX +"message";
        //        public static final String base_uri = "user";
        public static final String base_uri = HTTP_API_PREFIX +"message";
        public static class METHOD{
            public static final String SEND = "send";
            public static final String BATCH_SEND = "batch_send";
            public static final String SEND_MESSAGE_TO_CLIENT = "sendmessage_toclient";
        }
    }

    public static class APP_CLIENT{
        public static final String name = TOPIC_PREFIX +"client";
        //        public static final String base_uri = "user";
        public static final String base_uri = HTTP_API_PREFIX +"client";
        public static class METHOD{
            public static final String LIST = "list";// 列出已登录的客户端(clientId -> auid)
        }
    }


    public static class APP_ROOM {
        public static final String name = TOPIC_PREFIX +"room";
        //        public static final String base_uri = "user";
        public static final String base_uri = HTTP_API_PREFIX +"room";
        public static class METHOD{
            public static final String ADD = "add"; // 添加群
            public static final String UPDATE = "update"; // 更新群信息
            public static final String LIST = "list"; // 列出群信息
            public static final String DEL = "del"; // 删除群
            public static final String LIST_MEMBER = "list_member"; // 列出群信息/群成员
            public static final String ADD_USER = "add_user"; // 添加群用户
            public static final String DEL_USER = "del_user"; // 删除群用户
        }
    }


    // 所有的topic
    public static List<String> topics = Arrays.asList(
            ERROR.name,
            CONNECTION.name,
            APP_USER.name
    );

}
