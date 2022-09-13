package com.rideshare.userinfo.security;

import com.rideshare.userinfo.exception.AuthenticationFailException;
import com.rideshare.userinfo.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.auth.url}")
    private String authURL;

    private static final Logger logger = LogManager.getLogger(TokenAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = request.getHeader("Authorization");

            if (StringUtils.hasText(jwt)) {
                // validate token by calling validate endpoint on auth service
                // create UserPrincipal object from the details and set user details auth
                String requestURL = authURL + "/auth/validate" + "?token={token}";
                Map<String, String> queryParams = new HashMap<>();
                queryParams.put("token", jwt);

                ResponseEntity<User> responseEntity = restTemplate.getForEntity(requestURL, User.class, queryParams);

                User respUser = responseEntity.getBody();

                List<GrantedAuthority> authorities = respUser.getRoles().stream().map((r)->new SimpleGrantedAuthority(r)).collect(Collectors.toList());

                UserPrincipal userDetails = new UserPrincipal(String.valueOf(respUser.getId()), respUser.getEmail(), null, respUser.getVerified(), authorities);
                userDetails.setPhoneNo(respUser.getPhoneNo());

                logger.debug("Setting Authentication for: " + userDetails);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
            throw new AuthenticationFailException("Could not set user authentication in security context, check authentication token");
        }
    }
}
