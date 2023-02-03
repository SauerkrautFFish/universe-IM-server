package edu.yjzxc.universeimserver.service;

import edu.yjzxc.universeimserver.enums.ResponseEnum;

public interface UserService {

    ResponseEnum verifyAndSendRegisterEmailCode(String account);
}
