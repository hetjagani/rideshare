package com.rideshare.payment;

import com.rideshare.payment.facade.RideServiceFacade;
import com.rideshare.payment.facade.StripeServiceFacade;
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
public class PaymentApplication {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public RideServiceFacade rideServiceFacade() {
		return new RideServiceFacade();
	}

	@Bean
	public StripeServiceFacade stripeServiceFacade() {
		return new StripeServiceFacade();
	}

	public static void main(String[] args) {
		SpringApplication.run(PaymentApplication.class, args);
	}

}
