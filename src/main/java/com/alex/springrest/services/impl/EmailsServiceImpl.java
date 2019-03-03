package com.alex.springrest.services.impl;

import com.alex.springrest.services.EmailTemplatesService;
import com.alex.springrest.services.EmailsService;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailsServiceImpl implements EmailsService {

    @Autowired
    private EmailTemplatesService emailTemplatesService;

    @Value("${sendgrid.key}")
    private String sendgridKey;

    @Override
    public void sendVerificationEmail(String email, String emailVerificationToken) {
        Map<String, Object> args = new HashMap<>();
        args.put("domain", "http://localhost:3000");
        args.put("token", emailVerificationToken);
        sendEmail(email,"[Spring rest] Email verification",  emailTemplatesService.renderTemplate("email-verification", args));
    }

    @Override
    public void sendPasswordResetEmail(String email, String resetPasswordToken) {
        Map<String, Object> args = new HashMap<>();
        args.put("domain", "http://localhost:3000");
        args.put("token", resetPasswordToken);
        sendEmail(email,"[Spring rest] Reset password",  emailTemplatesService.renderTemplate("reset-password", args));
    }

    private void sendEmail(String email, String subject, String htmlContent) {
        Email from = new Email(email);
        Email to = new Email(email);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, to, content);
        Request request = new Request();

        SendGrid sendGrid = new SendGrid(sendgridKey);
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
