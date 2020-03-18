package com.easyim.mapper;


import com.CommonMapper;
import com.easyim.entity.Message;
import com.easyim.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageMapper extends CommonMapper<Message> {
}
