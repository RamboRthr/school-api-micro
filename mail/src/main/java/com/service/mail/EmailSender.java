package com.service.mail;

import Classes.ForgotPasswordRequest;
import Classes.SignupRequest;
import com.service.mail.Data.EmailData;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
public class EmailSender {
    @Autowired
    EmailService emailService;

    @RabbitListener(queues = {"${queue.signup}"})
    public void receiveSignup(@Payload SignupRequest request) throws Exception {

        emailService.sendEmail("school@eb-isco.com", EmailData.from(request));
    }

    @RabbitListener(queues = {"${queue.reset}"})
    public void receiveReset(@Payload ForgotPasswordRequest request) throws Exception {

            emailService.sendEmail("school@eb-isco.com", EmailData.from(request));
    }
}
