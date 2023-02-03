package edu.yjzxc.universeimserver.controller;

import edu.yjzxc.universeimserver.enums.ResponseEnum;
import edu.yjzxc.universeimserver.response.CommonResponse;
import edu.yjzxc.universeimserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/sendRegisterCode")
    public CommonResponse sendEmailCode(String account) {
        // 日志
        System.out.println("进入系统");
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

    @PostMapping("/register")
    public void registerNewUser(String emailAddress) {

    }

}
