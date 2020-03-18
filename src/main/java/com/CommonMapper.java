package com;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.lang.NonNull;

/**
 * 演示 mapper 父类，注意这个类不要让 mp 扫描到！！
 */
public interface CommonMapper<T> extends BaseMapper<T> {
    // 查询id 是否在数据库表存在, 否则抛出异常
    default void assertIdExist(@NonNull Integer id){
        T result = this.selectById(id);
        if(result == null){
            throw new RuntimeException("id:" + id + "不存在.");
        }
    }

    // 查询id 是否在数据库表存在, 否则抛出异常
    default void assertIdExist(String msgIfNotExist, @NonNull Integer id){
        T result = this.selectById(id);
        if(result == null){
            throw new RuntimeException(msgIfNotExist);
        }
    }
}
