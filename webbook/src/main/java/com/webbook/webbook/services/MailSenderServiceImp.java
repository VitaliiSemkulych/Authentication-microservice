package com.webbook.webbook.services;

import com.webbook.webbook.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Component
public class MailSenderServiceImp implements MailSenderService{

    private final JavaMailSender mailSender;

    @Autowired
    public MailSenderServiceImp(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String applicationEmail;

    @Value("${spring.mail.sender.subject}")
    private String subject;

    @Value("${spring.mail.sender.name}")
    private String senderName ;
    @Value("${spring.mail.sender.url}")
    private String siteUrL;






    @Override
    public void sendMime(User user) throws MessagingException, UnsupportedEncodingException {

        final String message =String.format("Dear %s,<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"%s/activate/%s\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br> %s",user.getUserName(),siteUrL,user.getActivationCode(),senderName);


        //                "Dear "+user.getUserName()+",<br>"
//                + "Please click the link below to verify your registration:<br>"
//                + "<h3><a href=\""+siteUrL+"/activate/"+user.getActivationCode()+"\" target=\"_self\">VERIFY</a></h3>"
//                + "Thank you,<br>"
//                + "Your company name.";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setFrom(applicationEmail,senderName);
        mimeMessageHelper.setTo(user.getEmail());
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(message,true);

        mailSender.send(mimeMessage);

    }




}
