package com.barath.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("barath").password("barath").roles("USER").authorities("ROLE_USER")
		.and()
		.withUser("a").password("a").roles("USER").authorities("ROLE_USER");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		http.authorizeRequests()
		.antMatchers("/insecured","/html/**","/login**","/lib/**","/css/**").permitAll()
		.antMatchers("/secured").hasRole("USER").anyRequest().authenticated()
		.and().formLogin().loginPage("/html/login.html").loginProcessingUrl("/login").usernameParameter("username").passwordParameter("password")
		//.and().headers().contentTypeOptions().disable()
		.and().csrf().disable()
		;
	}

}
