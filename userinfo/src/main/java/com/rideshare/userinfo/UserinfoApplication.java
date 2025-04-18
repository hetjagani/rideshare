package com.rideshare.userinfo;

import com.rideshare.userinfo.facade.AuthServiceFacade;
import com.rideshare.userinfo.facade.RideServiceFacade;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@SecurityScheme(scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class UserinfoApplication {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public RideServiceFacade rideServiceFacade() {
		return new RideServiceFacade();
	}

	@Bean
	public AuthServiceFacade authServiceFacade() {
		return new AuthServiceFacade();
	}

	public static void main(String[] args) {
		SpringApplication.run(UserinfoApplication.class, args);
	}

}
