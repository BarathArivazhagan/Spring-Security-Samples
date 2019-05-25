package com.barath.app;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	
	
	private final DataSource dataSource;

    private final UserDetailsService userDetailsService;


    public SecurityConfiguration(DataSource dataSource,UserDetailsService userDetailsService){
        this.userDetailsService=userDetailsService;
        this.dataSource = dataSource;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    	auth.jdbcAuthentication()
    	.dataSource(dataSource)
    		.passwordEncoder(new BCryptPasswordEncoder())
    		.withUser("barath").password("barath")
    		 .authorities("USER");
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

//    @Bean
//    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter(){
//        UsernamePasswordAuthenticationFilter filter=new UsernamePasswordAuthenticationFilter();
//        filter.set
//
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**").authorizeRequests().anyRequest().permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/login_form.html").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated()
             
                .and().formLogin().loginPage("/login_form.html").loginProcessingUrl("/login").defaultSuccessUrl("/home.html").permitAll()
                .and().csrf().disable()
                ;
    }
    
    
//    @Bean
//    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//    	return new JdbcTemplate(dataSource);
//    }
    
   
}
