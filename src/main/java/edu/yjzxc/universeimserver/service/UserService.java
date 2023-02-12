package edu.yjzxc.universeimserver.service;

import edu.yjzxc.universeimserver.enums.ResponseEnum;
import edu.yjzxc.universeimserver.request.UserRequest;
import edu.yjzxc.universeimserver.response.CommonResponse;

public interface UserService {
    ResponseEnum verifyAndSendRegisterEmailCode(String account);

    ResponseEnum createAccount(UserRequest userRequest);

    ResponseEnum verifyAndSendForgetPwdEmailCode(String account);

    ResponseEnum resetPassword(UserRequest userRequest);

    CommonResponse loginIndex(UserRequest userRequest);

    CommonResponse queryUserInfoById(Long id);


}
