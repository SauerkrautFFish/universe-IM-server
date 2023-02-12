package edu.yjzxc.universeimserver.service;

import edu.yjzxc.universeimserver.enums.ResponseEnum;

public interface RelationService {
    ResponseEnum addFriendRequest(Long sender, Long receiver);
}
