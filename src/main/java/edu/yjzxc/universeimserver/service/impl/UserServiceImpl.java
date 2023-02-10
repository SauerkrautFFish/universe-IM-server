package edu.yjzxc.universeimserver.service.impl;

import edu.yjzxc.universeimserver.constants.CommonConstant;
import edu.yjzxc.universeimserver.dto.UserInfoDTO;
import edu.yjzxc.universeimserver.entity.UserCenter;
import edu.yjzxc.universeimserver.entity.UserCenterExample;
import edu.yjzxc.universeimserver.mapper.UserCenterMapper;
import edu.yjzxc.universeimserver.enums.ResponseEnum;
import edu.yjzxc.universeimserver.request.UserRequest;
import edu.yjzxc.universeimserver.response.CommonResponse;
import edu.yjzxc.universeimserver.service.UserService;
import edu.yjzxc.universeimserver.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
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
        criteria.andAccountEqualTo(account);

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
            EmailUtil.sendEmail(account, "[与君酌星辰]宇宙级即时通讯系统", "注册验证码为: " + digitalVerificationCode);
            return ResponseEnum.SUCCESS;
        } catch (MessagingException e) {
            // 日志
            e.printStackTrace();
            return ResponseEnum.SYSTEM_ERROR;
        }

    }

    @Override
    public ResponseEnum createAccount(UserRequest userRequest) {
        String account = userRequest.getAccount();

        // 是否存在验证码
        String registerCode = (String)redisTemplate.opsForValue().get(CommonConstant.REDIS_REGISTER_CODE + account);
        if(!StringUtils.hasLength(registerCode)) {
            return ResponseEnum.RESEND_CODE;
        }

        // 验证码是否一致
        if(!userRequest.getVerifyCode().equals(registerCode)) {
            return ResponseEnum.VERIFY_CODE_ERROR;
        }

        // 用户id
        long userId = SnowFlake.getNextId();
        // 个人二维码
        String filePath = QRCodeUtil.createQRCode(String.valueOf(userId));
        // todo 把filepath的内容上传到文件服务器(或者直接不要filepath用url)

        // cid(暂不确定会不会出现获取不到的情况)
        String cid = userRequest.getCid() == null ? userRequest.getCid() : "";

        // 构建user对象
        UserCenter userCenter = new UserCenter();
        userCenter.setAccount(account);
        userCenter.setPassword(userRequest.getPassword());
        userCenter.setNickname(userRequest.getNickname());
        userCenter.setQrcode(filePath);
        userCenter.setCid(cid);
        userCenter.setFaceImage("default.jpg");
        userCenter.setId(userId);

        // 创建账号
        int count = userCenterMapper.insertIfNotExist(userCenter);

        // 判断是否创建成功
        if(count == CommonConstant.INSERT_ONE) {
            return ResponseEnum.SUCCESS;
        } else {
            // count=2代表执行了insert 和 update
            // count=0插入失败会报错,直接系统异常
            return ResponseEnum.ACCOUNT_EXISTS;
        }
    }

    @Override
    public ResponseEnum verifyAndSendForgetPwdEmailCode(String account) {
        // 邮箱地址格式是否正确
        if(!pattern.matcher(account).find()) {
            return ResponseEnum.EMAIL_FORMAT_INCORRECT;
        }

        UserCenterExample userCenterExample = new UserCenterExample();
        UserCenterExample.Criteria criteria = userCenterExample.createCriteria();
        criteria.andAccountEqualTo(account);

        // 查询下该账号是否存在
        int count = userCenterMapper.countByExample(userCenterExample);
        if(count == CommonConstant.COUNT_ZERO) {
            return ResponseEnum.ACCOUNT_NOT_EXISTS;
        }

        // 生成验证码
        String digitalVerificationCode = VerifyCode.buildDigitalVerificationCode(CommonConstant.FOEGET_PWD_VERIFY_CODE_NUMBER);

        try {
            // 缓存验证码
            redisTemplate.opsForValue().set(CommonConstant.REDIS_FORGET_PWD_CODE + account, digitalVerificationCode, 5, TimeUnit.MINUTES);
            // 发送邮件
            EmailUtil.sendEmail(account, "[与君酌星辰]宇宙级即时通讯系统", "(重置密码)验证码为: " + digitalVerificationCode);
            return ResponseEnum.SUCCESS;
        } catch (MessagingException e) {
            // 日志
            e.printStackTrace();
            return ResponseEnum.SYSTEM_ERROR;
        }

    }

    @Override
    public ResponseEnum resetPassword(UserRequest userRequest) {
        String account = userRequest.getAccount();

        // 是否存在验证码
        String resetCode = (String)redisTemplate.opsForValue().get(CommonConstant.REDIS_FORGET_PWD_CODE + account);
        if(!StringUtils.hasLength(resetCode)) {
            return ResponseEnum.RESEND_CODE;
        }

        // 验证码是否一致
        if(!userRequest.getVerifyCode().equals(resetCode)) {
            return ResponseEnum.VERIFY_CODE_ERROR;
        }

        UserCenterExample userCenterExample = new UserCenterExample();
        UserCenterExample.Criteria criteria = userCenterExample.createCriteria();
        criteria.andAccountEqualTo(account);

        // 获取用户
        List<UserCenter> userCenters = userCenterMapper.selectByExample(userCenterExample);
        if(CollectionUtils.isEmpty(userCenters)) {
            return ResponseEnum.ACCOUNT_NOT_EXISTS;
        }

        UserCenter userCenter = userCenters.get(0);
        userCenter.setPassword(userRequest.getPassword());

        int count = userCenterMapper.updateByPrimaryKey(userCenter);

        // 判断是否创建成功
        if(count == CommonConstant.UPDATE_ONE) {
            return ResponseEnum.SUCCESS;
        } else {
            // count=0插入失败会报错,直接系统异常（这句代码应该不会执行）
            return ResponseEnum.ACCOUNT_EXISTS;
        }
    }

    @Override
    public CommonResponse loginIndex(UserRequest userRequest) {

        UserCenterExample example = new UserCenterExample();
        UserCenterExample.Criteria criteria = example.createCriteria();
        criteria.andAccountEqualTo(userRequest.getAccount());

        List<UserCenter> userCenters = userCenterMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(userCenters)) {
            return CommonResponse.status(ResponseEnum.ACCOUNT_NOT_EXISTS);
        }

        UserCenter userCenter = userCenters.get(0);

        if(!userRequest.getPassword().equals(userCenter.getPassword())) {
            return CommonResponse.status(ResponseEnum.PASSWORD_INCORRECT);
        }

        String token = TokenUtil.buildToken(userCenter.getId().toString(), userCenter.getAccount());
        return CommonResponse.successData(token);
    }

    @Override
    public CommonResponse queryUserInfoById(Long id) {

        UserCenter userCenter = userCenterMapper.selectByPrimaryKey(id);
        if(Objects.isNull(userCenter)) {
            return CommonResponse.status(ResponseEnum.ACCOUNT_NOT_EXISTS);
        }

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(userCenter, userInfoDTO);

        return CommonResponse.successData(userInfoDTO);
    }

}
