package com.url_Shortner.demo.Security;
import com.url_Shortner.demo.JWTSecurity.JwtAuthentaionfilter;
import com.url_Shortner.demo.Service.UserDeatilsServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
@NoArgsConstructor
public class WebSecurity {
    private UserDetails userdetails;
    @Autowired
    private UserDeatilsServiceImpl userDeatilsServiceImpl;
    @Bean
    public JwtAuthentaionfilter jwtAuthentaionfilter()
    {
        return new JwtAuthentaionfilter();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    // this part need to verfiy part need to verify  because there might be some issue
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider( userDeatilsServiceImpl);
        //authProvider.setUserDetailsPasswordService( );
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
    {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception
    {
        httpSecurity
                .cors(Customizer.withDefaults()) // this line add by the claud and to allow cros orign platform to allow
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/urls/**","/api/Testing").authenticated()
                         .requestMatchers("/{shorturl}").permitAll().anyRequest().authenticated());
        httpSecurity.addFilterBefore(jwtAuthentaionfilter(), UsernamePasswordAuthenticationFilter.class);
       httpSecurity.authenticationProvider(authenticationProvider());
        return httpSecurity.build();
    }

}
