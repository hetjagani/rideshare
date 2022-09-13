package com.rideshare.auth.service;

import com.rideshare.auth.model.User;
import com.rideshare.auth.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class RideShareUserDetailsService implements UserDetailsService {
    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User modelUser = userService.getUserByEmail(username);

            Collection<? extends GrantedAuthority> authorities = modelUser.getRoles().stream().map((r) -> new SimpleGrantedAuthority(r)).collect(Collectors.toList());

            return new UserPrincipal(String.valueOf(modelUser.getId()), modelUser.getEmail(), modelUser.getPassword(), modelUser.getVerified(), authorities);

        } catch (DataAccessException dae) {
            throw new UsernameNotFoundException("cannot access user with username: " + username);
        }
    }
}
