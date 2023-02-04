package edu.yjzxc.universeimserver.service.impl;

import edu.yjzxc.universeimserver.constants.CommonConstant;
import edu.yjzxc.universeimserver.entity.UserCenter;
import edu.yjzxc.universeimserver.entity.UserCenterExample;
import edu.yjzxc.universeimserver.mapper.UserCenterMapper;
import edu.yjzxc.universeimserver.enums.ResponseEnum;
import edu.yjzxc.universeimserver.request.RegisterRequest;
import edu.yjzxc.universeimserver.service.UserService;
import edu.yjzxc.universeimserver.utils.EmailUtil;
import edu.yjzxc.universeimserver.utils.SnowFlake;
import edu.yjzxc.universeimserver.utils.VerifyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        if(count != CommonConstant.COUNT_ZERO) {
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

    @Override
    public ResponseEnum createAccount(RegisterRequest registerRequest) {
        String account = registerRequest.getAccount();

        // 是否存在验证码
        String registerCode = (String)redisTemplate.opsForValue().get(CommonConstant.REDIS_REGISTER_CODE + account);
        if(!StringUtils.hasLength(registerCode)) {
            return ResponseEnum.RESEND_REGISTER_CODE;
        }

        // 验证码是否一致
        if(!registerRequest.getVerifyCode().equals(registerCode)) {
            return ResponseEnum.REGISTER_CODE_ERROR;
        }

        // 构建user对象
        UserCenter userCenter = new UserCenter();
        userCenter.setAccount(account);
        userCenter.setPassword(registerRequest.getPassword());
        userCenter.setNickname(registerRequest.getNickname());
        userCenter.setQrcode("");
        userCenter.setCid("");
        userCenter.setFaceImage("");
        userCenter.setIsDel(CommonConstant.NOT_DEL);
        userCenter.setId(SnowFlake.getNextId());

        // todo cid and qrcode

        // 创建账号
        int count = userCenterMapper.insertIfNotExist(userCenter);
        System.out.println("insert count = " + count);

        // 判断是否创建成功
        if(count == CommonConstant.INSERT_ONE) {
            return ResponseEnum.SUCCESS;
        } else {
            // count=2代表执行了insert 和 update
            // count=0插入失败会报错,直接系统异常
            return ResponseEnum.ACCOUNT_EXISTS;
        }

    }

}
