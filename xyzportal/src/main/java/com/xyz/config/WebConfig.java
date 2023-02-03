package com.xyz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		
		UserDetails normalUser = User
				.withUsername("normal")
				.password(passwordEncoder().encode("normal"))
				.roles("NORMAL")
				.build();
		
		UserDetails adminUser = User
				.withUsername("admin")
				.password(passwordEncoder().encode("admin"))
				.roles("ADMIN")
				.build();
		
		return new InMemoryUserDetailsManager(normalUser, adminUser);
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
					.authorizeRequests()
					
					.antMatchers("home/admin")
					.hasRole("ADMIN")
					.antMatchers("home/normal")
					.hasRole("Normal")
					
					.antMatchers("/home/public")
					.permitAll()
					.anyRequest()
					.authenticated()
					.and()
					.formLogin();
		
		return httpSecurity.build();
	}
}
