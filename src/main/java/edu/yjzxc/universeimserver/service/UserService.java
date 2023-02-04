package edu.yjzxc.universeimserver.service;

import edu.yjzxc.universeimserver.entity.UserCenter;
import edu.yjzxc.universeimserver.enums.ResponseEnum;
import edu.yjzxc.universeimserver.request.RegisterRequest;

public interface UserService {
    ResponseEnum verifyAndSendRegisterEmailCode(String account);

    ResponseEnum createAccount(RegisterRequest registerRequest);
}
