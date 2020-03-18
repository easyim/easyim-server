package com.utils;

import java.util.UUID;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/13 16:40
 * Description: easyim
 **/
public class UUIDUtils {
    public static String gen(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
