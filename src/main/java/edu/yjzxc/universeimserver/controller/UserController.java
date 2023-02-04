package edu.yjzxc.universeimserver.controller;

import edu.yjzxc.universeimserver.constants.CommonConstant;
import edu.yjzxc.universeimserver.entity.UserCenter;
import edu.yjzxc.universeimserver.enums.ResponseEnum;
import edu.yjzxc.universeimserver.request.RegisterRequest;
import edu.yjzxc.universeimserver.response.CommonResponse;
import edu.yjzxc.universeimserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/sendRegisterCode")
    public CommonResponse sendEmailCode(String account) {
        // 日志
        System.out.println("进入 sendEmailCode");
        if(!StringUtils.hasLength(account)) {
            return CommonResponse.status(ResponseEnum.EMAIL_NULL_INCORRECT);
        }

        try {
            ResponseEnum responseEnum = userService.verifyAndSendRegisterEmailCode(account);
            return CommonResponse.status(responseEnum);
        } catch (Exception e) {
            // 日志
            e.printStackTrace();
            return CommonResponse.status(ResponseEnum.SYSTEM_ERROR);
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public CommonResponse registerNewUser(@RequestBody RegisterRequest registerRequest) {

        if(Objects.isNull(registerRequest) || !StringUtils.hasLength(registerRequest.getVerifyCode()) ||
                registerRequest.getVerifyCode().length() != CommonConstant.REGISTER_VERIFY_CODE_NUMBER ||
                !StringUtils.hasLength(registerRequest.getPassword()) || !StringUtils.hasLength(registerRequest.getNickname()) ||
                !StringUtils.hasLength(registerRequest.getAccount())) {
            return CommonResponse.status(ResponseEnum.MISSING_PARAMS);
        }
        try {
            ResponseEnum responseEnum = userService.createAccount(registerRequest);
            return CommonResponse.status(responseEnum);
        } catch (Exception e) {
            // 日志
            e.printStackTrace();
            return CommonResponse.status(ResponseEnum.SYSTEM_ERROR);
        }
    }

}
