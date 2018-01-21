package com.sungung.appcaching;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Bean
	protected UserDetailsService userDetailsService() {
		return new InMemoryUserDetailsManager(Arrays.asList(User.withUsername("user").password("password").roles("ADMIN").build()));
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().and()
			.authorizeRequests()
			.antMatchers("/").hasRole("ADMIN")
			.anyRequest().authenticated()
			.and().csrf().disable();
	}
	
}
