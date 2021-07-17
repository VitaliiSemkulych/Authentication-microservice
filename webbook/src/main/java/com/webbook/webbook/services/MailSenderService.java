package com.webbook.webbook.services;

import com.webbook.webbook.models.User;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface MailSenderService {
    public void sendMime(User user)
            throws MessagingException, UnsupportedEncodingException;
    }
