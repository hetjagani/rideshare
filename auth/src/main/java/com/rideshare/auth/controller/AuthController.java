package com.rideshare.auth.controller;

import com.rideshare.auth.exception.BadRequestException;
import com.rideshare.auth.exception.UserAlreadyExistsException;
import com.rideshare.auth.exception.UserDoesNotExistException;
import com.rideshare.auth.security.TokenProvider;
import com.rideshare.auth.service.IUserService;
import com.rideshare.auth.webentity.AuthResponse;
import com.rideshare.auth.webentity.LoginInfo;
import com.rideshare.auth.webentity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/users/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    private Set<String> validRoles = new HashSet<String>(Arrays.asList("DRIVER", "RIDER", "ADMIN"));

    @PostMapping(path = "/signup")
    public ResponseEntity<AuthResponse> signUpUser(@RequestBody User user) throws BadRequestException {
        try {
            userService.getUserByEmail(user.getEmail());
        } catch (EmptyResultDataAccessException ex) {
            try {
                // check for valid role
                for(String role: user.getRoles()) {
                    if(!validRoles.contains(role)) {
                        throw new BadRequestException("Invalid role in roles list, roles should be: DRIVER, RIDER or ADMIN");
                    }
                }

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

    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginInfo loginInfo) throws Exception {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginInfo.getEmail(), loginInfo.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(auth);

            final String token = tokenProvider.createToken(auth);

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/validate")
    public ResponseEntity<com.rideshare.auth.model.User> validateToken(@RequestParam String token) throws Exception {
        try {
            String userID = tokenProvider.getUserIdFromToken(token);

            com.rideshare.auth.model.User user = userService.getUserById(Integer.parseInt(userID));

            tokenProvider.validateToken(token);

            return ResponseEntity.ok(user);
        } catch (EmptyResultDataAccessException er) {
            er.printStackTrace();
            throw new UserDoesNotExistException("User does not exist");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
