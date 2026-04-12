package com.url_Shortner.demo.Controller;

import com.url_Shortner.demo.Dtos.LoginUser;
import com.url_Shortner.demo.Dtos.RegisterRequest;
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
   /* public ResponseEntity<? >Register(@RequestBody RegisterRequest registerRequest)
    {
        System.out.println("hiting at the controller"+registerRequest);
        User user=new User();
        user.setUserName(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setRoleUser("ROLE_USER");
       userService.Registeruser(user);
        return ResponseEntity.ok("User Register to Our DataBase");
    }*/
    @PostMapping("/public/login")
    /*
    // this is actual
    public ResponseEntity<?> makeloginuser(@RequestBody LoginUser loginRequest)
    {
        // responce entity request ka ststus code send kara ka kam ayataa ha
        System.out.println("Request is being hit at login request "+loginRequest);
        System.out.println("Server Time: " + ZonedDateTime.now());

        return  ResponseEntity.ok( userService.loginuser(loginRequest));


    }*/
    public ResponseEntity<?> makeloginuser(@RequestBody LoginUser loginRequest)
    {
        // responce entity request ka ststus code send kara ka kam ayataa ha
        System.out.println("Request is being hit at login request "+loginRequest);
        System.out.println("Server Time: " + ZonedDateTime.now());

        return   userService.loginuser(loginRequest);


    }


}
