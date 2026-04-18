package com.url_Shortner.demo.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${frontend.url}")
    private String frontendurl;
    private JavaMailSender sender;
    public EmailService(JavaMailSender sender)
    {
        this.sender=sender;
    }

    public void sendresetmail(String email,String token)
    {
        System.out.print("emial is being sent sucessfully");
        SimpleMailMessage message=new SimpleMailMessage();
             message.setTo(email);
             message.setSubject("passWord ReSetLink please click this link to verify its you");
        message.setText(
                "Hello,\n\n" +
                        "You requested to reset your password.\n\n" +
                        "Click the link below to reset your password:\n\n" +
                        frontendurl+"/auth/reset-password?token="+token+"\n\n" +
                        "This link will expire in 5 minutes.\n\n" +
                        "If you did not request this, please ignore this email.\n\n" +
                        "Thanks,\nshortly Team"
        );
             sender.send(message);
        System.out.println("emial is being sent sucessfully ="+email);
    }
    public void sendmailuserregister(String email)
    {
        System.out.println("sending mail to the user register with the mail"+email);
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Conguraltion "+
                " \n\n you have registerd sucessfully with linkshortly platform");
        message.setText("for the furhter query you can contacts \n\n"+
                "developed and maintained by Nitish Gupta\n\n"+
                "thanyou \n Team shortly");
        sender.send(message);
        System.out.println("email sent to the user register with us"+email);

    }
}
