package edu.yjzxc.universeimserver.service.impl;

import edu.yjzxc.universeimserver.enums.ResponseEnum;
import edu.yjzxc.universeimserver.service.RelationService;
import org.springframework.stereotype.Service;

@Service
public class RelationServiceImpl implements RelationService {
    @Override
    public ResponseEnum addFriendRequest(Long sender, Long receiver) {
        // 不能添加自己
        if(sender.longValue() == receiver.longValue()) {
            return ResponseEnum.ADD_FRIEND_YOURSELF;
        }

        // 是否已经是好友
        if(true) {
            return ResponseEnum.ALREADY_FRIEND;
        }
        // 发送添加请求

        return ResponseEnum.SUCCESS;
    }
}
