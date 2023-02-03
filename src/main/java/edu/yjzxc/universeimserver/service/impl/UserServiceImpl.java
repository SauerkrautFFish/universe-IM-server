package edu.yjzxc.universeimserver.service.impl;

import edu.yjzxc.universeimserver.constants.CommonConstant;
import edu.yjzxc.universeimserver.entity.UserCenterExample;
import edu.yjzxc.universeimserver.mapper.UserCenterMapper;
import edu.yjzxc.universeimserver.enums.ResponseEnum;
import edu.yjzxc.universeimserver.service.UserService;
import edu.yjzxc.universeimserver.utils.EmailUtil;
import edu.yjzxc.universeimserver.utils.VerifyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    UserCenterMapper userCenterMapper;

    Pattern pattern = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    @Override
    public ResponseEnum verifyAndSendRegisterEmailCode(String account) {
        // 邮箱地址格式是否正确
        Matcher matcher = pattern.matcher(account);
        if(!matcher.find()) {
            return ResponseEnum.EMAIL_FORMAT_INCORRECT;
        }

        UserCenterExample userCenterExample = new UserCenterExample();
        UserCenterExample.Criteria criteria = userCenterExample.createCriteria();
        criteria.andAccountEqualTo(account).andIsDelEqualTo(CommonConstant.NOT_DEL);

        // 查询下该账号是否存在
        int count = userCenterMapper.countByExample(userCenterExample);
        if(count != CommonConstant.ACCOUNT_NOT_EXIST) {
            return ResponseEnum.ACCOUNT_EXISTS;
        }

        // 生成验证码
        String digitalVerificationCode = VerifyCode.buildDigitalVerificationCode(CommonConstant.REGISTER_VERIFY_CODE_NUMBER);

        try {
            // 缓存验证码
            redisTemplate.opsForValue().set(CommonConstant.REDIS_REGISTER_CODE + account, digitalVerificationCode, 5, TimeUnit.MINUTES);
            // 发送邮件
            EmailUtil.sendEmail(account, "[与君酌星辰]宇宙级即时通讯系统", "验证码为: " + digitalVerificationCode);
            return ResponseEnum.SUCCESS;
        } catch (MessagingException e) {
            // 日志
            e.printStackTrace();
            return ResponseEnum.SYSTEM_ERROR;
        }

    }


}
