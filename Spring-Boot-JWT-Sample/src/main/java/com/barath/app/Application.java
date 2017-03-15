package com.barath.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
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
	
	@GetMapping("/secured")
	public String securedResource(){
		return "Secured Resource";
	}
	
	@GetMapping("/insecured")
	public String insecuredResource(){
		System.out.println("Insecured called");
		return "Not a Secured Resource";
	}
	
	
	@Configuration
	protected static class AppSecurity extends WebSecurityConfigurerAdapter{
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			
			http.antMatcher("/insecured").authorizeRequests().anyRequest().permitAll()
				.and()
				.authorizeRequests().antMatchers("/secured").authenticated();

		}
		
	}
	
	
}
