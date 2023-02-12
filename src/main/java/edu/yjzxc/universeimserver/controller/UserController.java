package edu.yjzxc.universeimserver.controller;

import edu.yjzxc.universeimserver.constants.CommonConstant;
import edu.yjzxc.universeimserver.enums.ResponseEnum;
import edu.yjzxc.universeimserver.request.UserRequest;
import edu.yjzxc.universeimserver.response.CommonResponse;
import edu.yjzxc.universeimserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.Objects;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/sendRegisterCode")
    public CommonResponse sendEmailCode(String account) {
        log.info("[calling] sendEmailCode(account={})", account);
        if(!StringUtils.hasLength(account)) {
            return CommonResponse.status(ResponseEnum.EMAIL_NULL_INCORRECT);
        }

        try {
            ResponseEnum responseEnum = userService.verifyAndSendRegisterEmailCode(account);
            log.info("[finish] sendEmailCode success to return. [responseEnum]={}", responseEnum);
            return CommonResponse.status(responseEnum);
        } catch (Exception e) {
            log.error("[error] sendEmailCode occur exception:{}", e.getMessage(), e);
            return CommonResponse.status(ResponseEnum.SYSTEM_ERROR);
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public CommonResponse registerNewUser(@RequestBody UserRequest userRequest) {
        log.info("[calling] registerNewUser(userRequest={})", userRequest);
        if(Objects.isNull(userRequest) || !StringUtils.hasLength(userRequest.getVerifyCode()) ||
                userRequest.getVerifyCode().length() != CommonConstant.REGISTER_VERIFY_CODE_NUMBER ||
                !StringUtils.hasLength(userRequest.getPassword()) || !StringUtils.hasLength(userRequest.getNickname()) ||
                !StringUtils.hasLength(userRequest.getAccount())) {
            return CommonResponse.status(ResponseEnum.MISSING_PARAMS);
        }
        try {
            ResponseEnum responseEnum = userService.createAccount(userRequest);
            log.info("[finish] registerNewUser success to return. [responseEnum]={}", responseEnum);
            return CommonResponse.status(responseEnum);
        } catch (Exception e) {
            log.error("[error] registerNewUser occur exception:{}", e.getMessage(), e);
            return CommonResponse.status(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/forgetPassword")
    public CommonResponse forgetPassword(@RequestBody UserRequest userRequest) {
        log.info("[calling] forgetPassword(userRequest={})", userRequest);
        if(Objects.isNull(userRequest) || !StringUtils.hasLength(userRequest.getVerifyCode()) ||
                userRequest.getVerifyCode().length() != CommonConstant.FOEGET_PWD_VERIFY_CODE_NUMBER ||
                !StringUtils.hasLength(userRequest.getPassword()) || !StringUtils.hasLength(userRequest.getAccount())) {
            return CommonResponse.status(ResponseEnum.MISSING_PARAMS);
        }
        try {
            ResponseEnum responseEnum = userService.resetPassword(userRequest);
            log.info("[finish] forgetPassword success to return. [responseEnum]={}", responseEnum);
            return CommonResponse.status(responseEnum);
        } catch (Exception e) {
            log.error("[error] forgetPassword occur exception:{}", e.getMessage(), e);
            return CommonResponse.status(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public CommonResponse login(@RequestBody UserRequest userRequest) {
        log.info("[calling] login(userRequest={})", userRequest);
        if(Objects.isNull(userRequest) || !StringUtils.hasLength(userRequest.getPassword()) ||
                !StringUtils.hasLength(userRequest.getAccount())) {
            return CommonResponse.status(ResponseEnum.MISSING_PARAMS);
        }
        try {
            CommonResponse commonResponse = userService.loginIndex(userRequest);
            log.info("[finish] login success to return. [commonResponse]={}", commonResponse);
            return commonResponse;
        } catch (Exception e) {
            log.error("[error] login occur exception:{}", e.getMessage(), e);
            return CommonResponse.status(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/sendForgetPasswordCode")
    public CommonResponse sendForgetPasswordCode(@RequestBody UserRequest userRequest) {
        log.info("[calling] sendForgetPasswordCode(userRequest={})", userRequest);
        if(Objects.isNull(userRequest) || StringUtils.hasLength(userRequest.getAccount())) {
            return CommonResponse.status(ResponseEnum.EMAIL_NULL_INCORRECT);
        }

        try {
            ResponseEnum responseEnum = userService.verifyAndSendForgetPwdEmailCode(userRequest.getAccount());
            log.info("[finish] sendForgetPasswordCode success to return. [responseEnum]={}", responseEnum);
            return CommonResponse.status(responseEnum);
        } catch (Exception e) {
            log.error("[error] sendForgetPasswordCode occur exception:{}", e.getMessage(), e);
            return CommonResponse.status(ResponseEnum.SYSTEM_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/getUserInfo")
    public CommonResponse getUserInfo(@RequestAttribute("zxcId") Long zxcId) {
        log.info("[calling] getUserInfo(zxcId={})", zxcId);

        try {
            CommonResponse commonResponse = userService.queryUserInfoById(zxcId);
            log.info("[finish] getUserInfo success to return. [commonResponse]={}", commonResponse);
            return commonResponse;
        } catch (Exception e) {
            log.error("[error] getUserInfo occur exception:{}", e.getMessage(), e);
            return CommonResponse.status(ResponseEnum.SYSTEM_ERROR);
        }
    }
}
