package com.unishare.unishare.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public void sendEmail(String toEmail, String otp){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("UniShare <" + fromAddress + ">");
        msg.setTo(toEmail);
        msg.setSubject("Unishare -- Reset Password Code");
        msg.setText(
                        "Your password reset code is: " + otp + "\n\n" +
                        "This code expires in 15 minutes.\n" +
                        "If you did not request this, ignore this email."
                    );

        mailSender.send(msg);
    }

}
