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

import java.time.LocalDateTime;
import java.util.Map;

import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository repo;
    private PasswordEncoder password;
    private AuthenticationManager authenticationManager;
    private JwtUtlis jwtUtlis;
    private EmailService emailService;

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
        System.out.println("username and email is correct");
        System.out.print("checking what isvalid return "+isValidPassword(user.getPassword()));

        if(isValidPassword(user.getPassword())){
            try{
                emailService.sendmailuserregister(user.getEmail());
                user.setPassword(password.encode(user.getPassword()));
                repo.save(user);
                System.out.print("user register  to the db and mail send to it");
                return ResponseEntity.ok("User Registered Successfully");
            } catch (RuntimeException e) {
                System.out.println("catched block executed run time exception");
                ResponseEntity.badRequest().body(Map.of("check your enter emial onces agian",e.getMessage()));
            }
            catch (Exception e)
            {
                System.out.println("exception block runs");
                ResponseEntity.badRequest().body(Map.of("emailservices failed ",e.getMessage()));
            }
        }
        else {
            return ResponseEntity.badRequest().body("password is not valid");
        }
        return ResponseEntity.badRequest().body("registerartion failed");

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
    public void GenerateResetToken(String email)
    {
        System.out.print("request hit the generate tokien");
        User user=repo.findByEmail(email).orElseThrow(()->new RuntimeException("user Not found"));
        String token= UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
        repo.save(user);
        System.out.println("generated token"+token);
        emailService.sendresetmail(email,token);
    }
    public void resetpassword(String newpassword,String token)
    {
        System.out.println("request reaching the resetpassword at the service layet");
        System.out.println(newpassword+" "+token);

        User user=repo.findByResetToken(token).orElseThrow(()->new RuntimeException("invalid token or experied"));
        if(user.getResetTokenExpiry().isBefore(LocalDateTime.now()))
        {
            throw new RuntimeException("token is expired request for the new one");
        }
        System.out.println("token which is we are getting from the db"+user.getResetToken());
        System.out.println("hello ihave reached here ..");
        if(!isValidPassword(newpassword)) {
            throw new RuntimeException("password is invalid");

        }
        if (password.matches(newpassword,user.getPassword()))
        {
            throw new RuntimeException("new password must be not same as the old password");
        }
        user.setPassword(password.encode(newpassword));
        System.out.println("encoded=" + user.getPassword());
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        repo.save(user);
    }
    public boolean isValidPassword(String password) {
       String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$";;
        return password.matches(regex);

    }

}
