package edu.yjzxc.universeimserver.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;
@Component
public class EmailUtil {

    //创建一个配置文件并保存
    private static Properties properties = new Properties();
    private static MailSSLSocketFactory sf;

    @Value("${senderEmailAddress}")
    private static String senderEmailAddress;
    @Value("${authorityCode}")
    private static String authorityCode;

    static {
        properties.setProperty("mail.host", "smtp.qq.com");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");

        try {
            sf = new MailSSLSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        //QQ存在一个特性设置SSL加密
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
    }

    public static void sendEmail(String emailAddress, String subject, String message) throws MessagingException {

        //创建一个session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmailAddress, authorityCode);
            }
        });

        //获取连接对象
        Transport transport = session.getTransport();
        //连接服务器
        transport.connect("smtp.qq.com", senderEmailAddress, authorityCode);
        //创建邮件对象
        MimeMessage mimeMessage = new MimeMessage(session);
        //邮件发送人
        mimeMessage.setFrom(new InternetAddress(senderEmailAddress));
        //邮件标题
        mimeMessage.setSubject(subject);
        //邮件内容
        mimeMessage.setContent(message, "text/html;charset=UTF-8");
        //邮件接收人
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
        //发送邮件
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        //关闭连接
        transport.close();
    }


}
