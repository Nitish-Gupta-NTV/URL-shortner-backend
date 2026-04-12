package com.url_Shortner.demo.Service;

import com.url_Shortner.demo.Dtos.LoginUser;
import com.url_Shortner.demo.JWTSecurity.JwtAuthentaionResponce;
import com.url_Shortner.demo.JWTSecurity.JwtUtlis;
import com.url_Shortner.demo.Models.User;
import com.url_Shortner.demo.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;

import java.util.Map;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository repo;
    private PasswordEncoder password;
    private AuthenticationManager authenticationManager;
    private JwtUtlis jwtUtlis;

    public ResponseEntity<?> Registeruser(User user) {
        System.out.println("Checking username: " + user.getUserName());
        System.out.println("Exists: " + repo.existsByUserName(user.getUserName()));
        if (repo.existsByUserName(user.getUserName())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Username already taken"));
        }
        if (repo.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email already registered"));
        }

        user.setPassword(password.encode(user.getPassword()));
        repo.save(user);
        return ResponseEntity.ok("User Registered Successfully");
    }
   /* public User Registeruser(User user)
    {
    // this is actual code

        System.out.println("Request is hiting the USer Service with password "+user.getPassword());
        user.setPassword(password.encode(user.getPassword()));
        return repo.save(user);
    }*/
   /*
   // this is the actual code
   public JwtAuthentaionResponce loginuser(LoginUser loginUser)
    {
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(),
                loginUser.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailImpl userDetail=(UserDetailImpl) authentication.getPrincipal();
        String jwt=jwtUtlis.generatedToken(userDetail);
                return new JwtAuthentaionResponce(jwt);


    }*/
   public ResponseEntity<?> loginuser(LoginUser loginUser)
   {
       try {
           Authentication authentication = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(loginUser.getUsername(),
                           loginUser.getPassword())
           );
           SecurityContextHolder.getContext().setAuthentication(authentication);
           UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
           String jwt = jwtUtlis.generatedToken(userDetail);
           return ResponseEntity.ok(new JwtAuthentaionResponce(jwt));
       }
       catch(BadCredentialsException e)
       {
           return ResponseEntity.status(401).body(Map.of("error","invalid username or password"));
       }
    catch (Exception e) {
        return ResponseEntity.status(500)
                .body(Map.of("error", "Something went wrong, please try again"));
    }


   }
    public  User findByUsername (String username)
    {
        return repo.findByUserName(username).orElseThrow(
                ()-> new UsernameNotFoundException("User Not found with username "+username)
        );
    }

}
