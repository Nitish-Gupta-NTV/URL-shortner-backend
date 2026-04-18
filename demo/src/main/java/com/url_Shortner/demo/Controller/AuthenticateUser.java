package com.url_Shortner.demo.Controller;

import com.url_Shortner.demo.Dtos.ForgotPasswordDto;
import com.url_Shortner.demo.Dtos.LoginUser;
import com.url_Shortner.demo.Dtos.RegisterRequest;
import com.url_Shortner.demo.Dtos.ResetPasswordDto;
import com.url_Shortner.demo.Models.User;
import com.url_Shortner.demo.Service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.ZonedDateTime;
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticateUser {
    private UserService userService;

    @PostMapping("/public/register")
    public ResponseEntity<? >Register(@Valid @RequestBody RegisterRequest registerRequest)
    {
        System.out.println("hiting at the controller"+registerRequest);
        User user=new User();
        user.setUserName(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setRoleUser("ROLE_USER");
         return userService.Registeruser(user);
       // return ResponseEntity.ok("User Register to Our DataBase");
    }
    @PostMapping("/public/login")
    public ResponseEntity<?> makeloginuser(@RequestBody LoginUser loginRequest)
    {
        // responce entity request ka ststus code send kara ka kam ayataa ha
        System.out.println("Request is being hit at login request "+loginRequest);
        System.out.println("Server Time: " + ZonedDateTime.now());

        return   userService.loginuser(loginRequest);


    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?>forgatepassword(@RequestBody ForgotPasswordDto forgotPasswordDto)
    {
        System.out.println("request is reacing to the controller "+forgotPasswordDto.getEmail());
        try{
            userService.GenerateResetToken(forgotPasswordDto.getEmail());
            System.out.println("try block of the forgot password got the trigger");
            return ResponseEntity.ok("Email Sent Sucessfully");
        } catch (Exception e) {
            System.out.println("catch block of the forgot password get trigger");
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetpassword(@RequestBody ResetPasswordDto reset)
    {
        try{
            userService.resetpassword(reset.getPassword(),reset.getToken());
            return ResponseEntity.ok("password reset sucesfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }


}
