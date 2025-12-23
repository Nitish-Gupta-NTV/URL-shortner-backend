package com.url_Shortner.demo.Service;

import com.url_Shortner.demo.Models.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
@Data
@NoArgsConstructor
public class UserDetailImpl implements UserDetails {
    public static final Long Serlizable=1L;
    private Long id;
    private String username;
    private String email;
    private String Password;
    private Collection< ? extends GrantedAuthority> authorised;

    public UserDetailImpl(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorised) {
        this.id = id;
        this.username = username;
        this.email = email;
        Password = password;
        this.authorised = authorised;
    }
    public static UserDetailImpl build(User user)
    {
        GrantedAuthority authority=new SimpleGrantedAuthority(user.getRoleUser());
        return new UserDetailImpl(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(authority)
                );

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorised;
    }

    @Override
    public @Nullable String getPassword() {

        return Password;
    }

    @Override
    public String getUsername() {

        return username;
    }
}
