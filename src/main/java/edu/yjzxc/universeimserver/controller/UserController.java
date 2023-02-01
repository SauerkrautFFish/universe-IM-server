package edu.yjzxc.universeimserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping(method = RequestMethod.GET, value = "/sendRegisterCode")
    public void sendEmailCode(String emailAddress) {

    }

    @PostMapping("/register")
    public void registerNewUser(String emailAddress) {

    }

}
