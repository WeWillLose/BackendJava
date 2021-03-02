package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.service.UserService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@NoArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user==null){
            throw new UsernameNotFoundException(String.format("User with username %s not found", username));
        }
        log.info("IN loadByUsername user with username {} successfully loaded",username);
        return user;
    }
}
