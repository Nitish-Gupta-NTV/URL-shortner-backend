package com.url_Shortner.demo.JWTSecurity;

import com.url_Shortner.demo.Service.UserDetailImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;
@Component
public class JwtUtlis {
    @Value("${jwt.secret}")
    private String JwtSecret;
    @Value("${Jwt.expirations}")
    private int JwtExpirationsMs;
    // we will pass the user authentcated with JwtToken
    //Bearer<Token>
    public String JwtHeaderValidation(HttpServletRequest request)
    {
        String BearerToken=request.getHeader("Authorization");
        if(BearerToken!=null&&BearerToken.startsWith("Bearer "))
        {
            return BearerToken.substring(7);

        }
        return null;

    }
    public String generatedToken(UserDetailImpl userDetail)
    {
        String username=userDetail.getUsername();
        String roles=userDetail.getAuthorities().stream()
                .map(authority->authority.getAuthority())
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .subject(username)
                .claim("Role",roles)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime()+JwtExpirationsMs))
                .signWith(key())
                .compact();

    }
    public String extractusernamefromJwtToken(String Token)
    {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(Token)
                .getPayload().getSubject();
    }
    private Key key()
    {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JwtSecret));
    }
    public boolean validateToken(String authToken)
    {
        try {
            Jwts.parser().verifyWith((SecretKey) key())
                    .build().parseSignedClaims(authToken);
            return  true;
        } catch (JwtException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
