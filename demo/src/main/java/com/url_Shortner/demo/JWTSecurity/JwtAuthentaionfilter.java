package com.url_Shortner.demo.JWTSecurity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthentaionfilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtlis jwtToken;
    @Autowired
    private UserDetailsService userDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

             try {
                 String Jwt= jwtToken.JwtHeaderValidation(request);
                 //if (jwtToken!=null&&jwtToken.validateToken(Jwt))
                 if (Jwt != null && !Jwt.isEmpty() && jwtToken.validateToken(Jwt))
                 {
                     String username=jwtToken.extractusernamefromJwtToken(Jwt);
                     UserDetails userDetails=userDetailsService.loadUserByUsername(username);
                     if (userDetails!=null)
                     {
                         UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                         authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                         SecurityContextHolder.getContext().setAuthentication(authentication);
                     }

                 }


             } catch (Exception e) {
                e.printStackTrace();
             }
           filterChain.doFilter(request, response);


    }
}
