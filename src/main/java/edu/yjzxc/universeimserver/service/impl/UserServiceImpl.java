package edu.yjzxc.universeimserver.service.impl;

import edu.yjzxc.universeimserver.service.UserService;
import edu.yjzxc.universeimserver.utils.EmailUtil;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    final String emailPattern = "^([a-z0-9A-Z]+[-|//.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?//.)+[a-zA-Z]{2,}$";
    Pattern pattern = Pattern.compile(emailPattern);
    @Override
    public void verifyAndSendRegisterEmailCode(String account) {


        Matcher matcher = pattern.matcher(account);
        if(!matcher.find()) {
            // 不符合邮件地址格式
        }

        // 查询下该账号是否存在


        String verifyCode = "";

        try {
            EmailUtil.sendEmail(account, "[与君酌星辰]宇宙级即时通讯系统-验证码", verifyCode);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
