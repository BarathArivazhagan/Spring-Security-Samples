package com.barath.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@GetMapping("/")
	public String home() {
		return "spring-security-actuator-basic";
	}
	
	@Configuration
	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
			
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			
			http.authorizeRequests()
				.requestMatchers(EndpointRequest.toAnyEndpoint())
				.authenticated().anyRequest().permitAll()
				.and().formLogin();
		}
	}

}
