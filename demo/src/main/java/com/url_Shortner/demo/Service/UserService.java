package com.url_Shortner.demo.Service;

import com.url_Shortner.demo.Dtos.LoginUser;
import com.url_Shortner.demo.JWTSecurity.JwtAuthentaionResponce;
import com.url_Shortner.demo.JWTSecurity.JwtUtlis;
import com.url_Shortner.demo.Models.User;
import com.url_Shortner.demo.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository repo;
    private PasswordEncoder password;
    private AuthenticationManager authenticationManager;
    private JwtUtlis jwtUtlis;
    public User Registeruser(User user)
    {
        System.out.println("Request is hiting the USer Service with password "+user.getPassword());
        user.setPassword(password.encode(user.getPassword()));
        return repo.save(user);
    }
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


    }
    public  User findByUsername (String username)
    {
        return repo.findByUserName(username).orElseThrow(
                ()-> new UsernameNotFoundException("User Not found with username "+username)
        );
    }

}
