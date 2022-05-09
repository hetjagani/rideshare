package com.rideshare.auth.controller;

import com.rideshare.auth.exception.UserAlreadyExistsException;
import com.rideshare.auth.security.TokenProvider;
import com.rideshare.auth.service.IUserService;
import com.rideshare.auth.webentity.AuthResponse;
import com.rideshare.auth.webentity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping(path = "/signup")
    public ResponseEntity<AuthResponse> signUpUser(@RequestBody User user) {
        try {
            userService.getUserByEmail(user.getEmail());
        } catch (EmptyResultDataAccessException ex) {
            try {
                com.rideshare.auth.model.User dbUser = new com.rideshare.auth.model.User(user.getEmail(),
                        user.getPassword(),
                        user.getPhoneNo(),
                        true,
                        user.getRoles());
                dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
                com.rideshare.auth.model.User createdUser = userService.createUser(dbUser);

                List<GrantedAuthority> grantedAuthorities = createdUser.getRoles().stream().map((r) -> new SimpleGrantedAuthority(r)).collect(Collectors.toList());

                Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(createdUser.getEmail(), user.getPassword(), grantedAuthorities));

                SecurityContextHolder.getContext().setAuthentication(auth);
                final String token = tokenProvider.createToken(auth);

                return ResponseEntity.ok(new AuthResponse(token));
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new UserAlreadyExistsException();
    }

}
