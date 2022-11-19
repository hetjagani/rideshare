package com.rideshare.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rideshare.auth.security.UserPrincipal;
import com.rideshare.auth.service.IUserService;
import com.rideshare.auth.service.RideShareUserDetailsService;
import com.rideshare.auth.webentity.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private IUserService userService;

    @MockBean
    private RideShareUserDetailsService rideShareUserDetailsService;

    @Test
    public void signUpUserTest() throws Exception {
        // mock model objects
        com.rideshare.auth.model.User user = new com.rideshare.auth.model.User("test@mail.com","$2a$10$o//zf0M3Akg5gkeCnmf7sOlz8nVNE9TIfA2Q8wG9jS/o2xL7WjwWa", "1234567890", true, Arrays.asList("DRIVER", "RIDER"));
        Collection<? extends GrantedAuthority> authorities = user.getRoles().stream().map((r) -> new SimpleGrantedAuthority(r)).collect(Collectors.toList());

        // mock service methods
        Mockito.when(userService.getUserByEmail(user.getEmail())).thenThrow(new EmptyResultDataAccessException(1));
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(user);
        Mockito.when(rideShareUserDetailsService.loadUserByUsername(user.getEmail())).thenReturn(new UserPrincipal(String.valueOf(user.getId()), user.getEmail(), user.getPassword(), user.getVerified(), authorities));

        // request body
        User bodyUser = new User("test@mail.com","Password@123", "1234567890");

        // building request
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsBytes(bodyUser));

        this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andReturn();
    }
}
