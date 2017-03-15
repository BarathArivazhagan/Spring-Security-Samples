package com.barath.app;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barath.app.Application.TokenAuthenticationService.JWTLoginFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SpringBootApplication(exclude=SecurityAutoConfiguration.class)
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
		
		private JWTLoginFilter filter=null;
		
		public AppSecurity() throws Exception {
			this.filter=new JWTLoginFilter("/login", authenticationManager());
			Assert.notNull(filter, "JWT Login filter cannot be null");
		}
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			
			http.antMatcher("/insecured").authorizeRequests().anyRequest().permitAll()
				.and()
				.authorizeRequests().antMatchers("/secured").authenticated()	
				.and()
					.authorizeRequests()
						.antMatchers("/").permitAll()
							.antMatchers(HttpMethod.POST, "/login").permitAll()
							.anyRequest().authenticated()
					.and()
            // We filter the api/login requests
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
           
           
		}
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication().withUser("barath").password("barath").roles("ADMIN")
			.and()
			.withUser("a").password("a").roles("USER");
		}
		
	}
	
	@Component
	protected static class TokenAuthenticationService{
		 private long EXPIRATIONTIME = 1000 * 60 * 60 * 24 * 10; // 10 days
	     private String secret = "ThisIsASecret";
	     private String tokenPrefix = "Bearer";
	     private String headerString = "Authorization";
	     public void addAuthentication(HttpServletResponse response, String username) {
	         // We generate a token now.
	         String JWT = Jwts.builder()
	             .setSubject(username)
	             .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
	             .signWith(SignatureAlgorithm.HS512, secret)
	             .compact();
	         response.addHeader(headerString, tokenPrefix + " " + JWT);
	     }

	     public Authentication getAuthentication(HttpServletRequest request) {
	         String token = request.getHeader(headerString);
	         if (token != null) {
	             // parse the token.
	             String username = Jwts.parser()
	                 .setSigningKey(secret)
	                 .parseClaimsJws(token)
	                 .getBody()
	                 .getSubject();
	           
	             if (username != null) // we managed to retrieve a user
	             {
	                 return new UsernamePasswordAuthenticationToken(username,null);
	             }
	         }
	         return null;
	     }
	     
	   
	     protected static class JWTLoginFilter extends AbstractAuthenticationProcessingFilter{
	    	 
	    	
	    	 private TokenAuthenticationService tokenAuthenticationService;
	    	 
	    	 public JWTLoginFilter(String url, AuthenticationManager authenticationManager) {
	             super(new AntPathRequestMatcher(url));
	             setAuthenticationManager(authenticationManager);	 
	             tokenAuthenticationService=new TokenAuthenticationService();
	        }
			@Override
			public Authentication attemptAuthentication(HttpServletRequest paramHttpServletRequest,
					HttpServletResponse paramHttpServletResponse)
					throws AuthenticationException, IOException, ServletException {
				 AccountCredentials credentials = new ObjectMapper().readValue(paramHttpServletRequest.getInputStream(), AccountCredentials.class);
			     UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
			     return getAuthenticationManager().authenticate(token);
				
			}
			
			@Override
			 protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
			 throws IOException, ServletException {
			     String name = authentication.getName();
			     tokenAuthenticationService.addAuthentication(response, name);
			 }
	    	 
	    }
	     
	     protected static class AccountCredentials{
	    	  private String username;
	    	  private String password;
			public String getUsername() {
				return username;
			}
			public void setUsername(String username) {
				this.username = username;
			}
			public String getPassword() {
				return password;
			}
			public void setPassword(String password) {
				this.password = password;
			}
			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + ((password == null) ? 0 : password.hashCode());
				result = prime * result + ((username == null) ? 0 : username.hashCode());
				return result;
			}
			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				AccountCredentials other = (AccountCredentials) obj;
				if (password == null) {
					if (other.password != null)
						return false;
				} else if (!password.equals(other.password))
					return false;
				if (username == null) {
					if (other.username != null)
						return false;
				} else if (!username.equals(other.username))
					return false;
				return true;
			}
			@Override
			public String toString() {
				return "AccountCredentials [username=" + username + ", password=" + password + "]";
			}
			public AccountCredentials(String username, String password) {
				super();
				this.username = username;
				this.password = password;
			}
			public AccountCredentials() {
				super();
				
			}
	    	  
	    	  
	     }
	}
	
	
}
