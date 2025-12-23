package com.url_Shortner.demo.Service;

import com.url_Shortner.demo.Models.User;
import com.url_Shortner.demo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDeatilsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByUserName(username).orElseThrow(()-> new UsernameNotFoundException("usernotfound"));
        return UserDetailImpl.build(user);
    }

}
