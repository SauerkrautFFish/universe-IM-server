package edu.yjzxc.universeimserver.controller;

import edu.yjzxc.universeimserver.constants.CommonConstant;
import edu.yjzxc.universeimserver.enums.ResponseEnum;
import edu.yjzxc.universeimserver.request.UserRequest;
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
    public CommonResponse registerNewUser(@RequestBody UserRequest userRequest) {

        if(Objects.isNull(userRequest) || !StringUtils.hasLength(userRequest.getVerifyCode()) ||
                userRequest.getVerifyCode().length() != CommonConstant.REGISTER_VERIFY_CODE_NUMBER ||
                !StringUtils.hasLength(userRequest.getPassword()) || !StringUtils.hasLength(userRequest.getNickname()) ||
                !StringUtils.hasLength(userRequest.getAccount())) {
            return CommonResponse.status(ResponseEnum.MISSING_PARAMS);
        }
        try {
            ResponseEnum responseEnum = userService.createAccount(userRequest);
            return CommonResponse.status(responseEnum);
        } catch (Exception e) {
            // 日志
            e.printStackTrace();
            return CommonResponse.status(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/forgetPassword")
    public CommonResponse forgetPassword(@RequestBody UserRequest userRequest) {

        if(Objects.isNull(userRequest) || !StringUtils.hasLength(userRequest.getVerifyCode()) ||
                userRequest.getVerifyCode().length() != CommonConstant.FOEGET_PWD_VERIFY_CODE_NUMBER ||
                !StringUtils.hasLength(userRequest.getPassword()) || !StringUtils.hasLength(userRequest.getAccount())) {
            return CommonResponse.status(ResponseEnum.MISSING_PARAMS);
        }
        try {
            ResponseEnum responseEnum = userService.resetPassword(userRequest);
            return CommonResponse.status(responseEnum);
        } catch (Exception e) {
            // 日志
            e.printStackTrace();
            return CommonResponse.status(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public CommonResponse login(@RequestBody UserRequest userRequest) {

        if(Objects.isNull(userRequest) || !StringUtils.hasLength(userRequest.getPassword()) ||
                !StringUtils.hasLength(userRequest.getAccount())) {
            return CommonResponse.status(ResponseEnum.MISSING_PARAMS);
        }
        try {
            return userService.loginIndex(userRequest);
        } catch (Exception e) {
            // 日志
            e.printStackTrace();
            return CommonResponse.status(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/sendForgetPasswordCode")
    public CommonResponse sendForgetPasswordCode(@RequestBody UserRequest userRequest) {

        if(Objects.isNull(userRequest) || StringUtils.hasLength(userRequest.getAccount())) {
            return CommonResponse.status(ResponseEnum.EMAIL_NULL_INCORRECT);
        }

        try {
            ResponseEnum responseEnum = userService.verifyAndSendForgetPwdEmailCode(userRequest.getAccount());
            return CommonResponse.status(responseEnum);
        } catch (Exception e) {
            // 日志
            e.printStackTrace();
            return CommonResponse.status(ResponseEnum.SYSTEM_ERROR);
        }
    }
}
