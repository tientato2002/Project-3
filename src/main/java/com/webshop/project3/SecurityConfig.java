package com.webshop.project3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true,
		prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtTokenFilter jwtTokenFilter;
	
	//xac thuc
	@Autowired
	public void config(AuthenticationManagerBuilder auth) 
			throws Exception{
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration) 
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public SecurityFilterChain config(HttpSecurity http) throws Exception{
		http.authorizeRequests()
		.antMatchers("/admin/**") // nhung duong dan admin
//		.hasAnyRole("ROLE_ADMIN");
		.hasAnyAuthority("ROLE_ADMIN")
		
		.antMatchers("/customer/**")	//nguoi dung con lai truy cap theo member
		.authenticated()
		
		.anyRequest().permitAll()	//nhung duong dan con lai
		
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)	//tat session
		.and()
		.httpBasic()
		.and()
		.csrf().disable(); 	//csrf tat bao mat

		//apply filter
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	
}
