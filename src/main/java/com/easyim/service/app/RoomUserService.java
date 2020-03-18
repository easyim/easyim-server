package com.easyim.service.app;

import com.annotation.TopicFor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.commom.Topic;
import com.easyim.entity.RoomUser;
import com.easyim.mapper.RoomUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/2/21 13:21
 * Description: easyim
 **/


@Slf4j
@TopicFor(value = Topic.APP_MESSAGE.name)
@Service
public class RoomUserService extends ServiceImpl<RoomUserMapper, RoomUser>  {

}
