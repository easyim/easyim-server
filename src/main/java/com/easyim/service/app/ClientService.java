package com.easyim.service.app;

import com.annotation.MethodFor;
import com.annotation.TopicFor;
import com.broker.base.protocol.response.Resp;
import com.broker.base.storage.BrokerStorageEntry;
import com.commom.Topic;
import com.easyim.broker.localsvc.TopicSender;
import com.easyim.response.dto.ClientListDTO;
import com.easyim.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/2/21 13:21
 * Description: easyim
 **/


@Slf4j
@TopicFor(value = Topic.APP_CLIENT.name)
@Service
public class ClientService {
    @Autowired
    private UserService userService;
    @Autowired
    private TopicSender topicSender;
    @Value("${broker.machineId}")
    private String machineId;

    @MethodFor(value = Topic.APP_CLIENT.METHOD.LIST)
    public Resp<List<ClientListDTO>> list(){
        List<String> allClientIds = topicSender.getSocketIOServer().getAllClients()
                .stream()
                .map(v->v.getSessionId().toString())
                .collect(Collectors.toList());
        List<ClientListDTO> dtos = new ArrayList<>();
        allClientIds.forEach(v->{
            dtos.add(new ClientListDTO().setClientId(v).setAuid(topicSender.getStorage().getValue(BrokerStorageEntry.CLIENT_TO_LOGINUSER, v)));
        });

        List<User> users = userService.lambdaQuery()
                .in(User::getAuid, dtos.stream().map(v->v.getAuid()).collect(Collectors.toList()))
                .list();
        Map<String, User> userMayByAuid = new HashMap<>();
        users.forEach(v->{
            userMayByAuid.put(v.getAuid(), v);
        });

        dtos.forEach(v->{
            v.setName(userMayByAuid.getOrDefault(v.getAuid(), new User()).getName());
        });
        return Resp.ok(dtos);
    }

}
