package edu.yjzxc.universeimserver.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
@Component
public class EmailUtil {

    //创建一个配置文件并保存
    private static Properties properties = new Properties();
    private static String senderEmailAddress;
    private static String authorityCode;

    @Value("${senderEmailAddress}")
    public void setSenderEmailAddress(String emailAddress) {
        senderEmailAddress = emailAddress;
    }

    @Value("${authorityCode}")
    public void setAuthorityCode(String authorityNumber) {
        authorityCode = authorityNumber;
    }

    static {
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); //TLS
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
    }

    public static void sendEmail(String emailAddress, String subject, String message) throws MessagingException {

        //创建一个session对象
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmailAddress, authorityCode);
            }
        });

        //创建邮件对象
        Message mimeMessage = new MimeMessage(session);
        //邮件发送人
        mimeMessage.setFrom(new InternetAddress(senderEmailAddress));
        //邮件接收人
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
        //邮件标题
        mimeMessage.setSubject(subject);
        //邮件内容
        mimeMessage.setText(message);
        //发送邮件
        Transport.send(mimeMessage);
    }
}
