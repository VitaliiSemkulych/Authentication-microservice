package com.webbook.webbook.unitTest.email;

import com.webbook.webbook.models.User;
import com.webbook.webbook.services.MailSenderService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@SpringBootTest
public class EmailTest {

   private final MailSenderService mailSenderService;


   @Autowired
    public EmailTest(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }


    @Test
    public void mimeMailTest() throws UnsupportedEncodingException, MessagingException {
        User user = User.builder()
                .userName("Vitalii Semkulych")
                .email("vitaliy089@gmail.com")
                .activationCode(UUID.randomUUID().toString())
                .build();
        mailSenderService.sendMime(user);

    }

}
