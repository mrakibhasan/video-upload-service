package com.spring.upload.security;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


	@Autowired
	private DataSource dataSource;
	private static String REALM="MY_TEST_REALM";
	
	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {

		/* auth.inMemoryAuthentication().withUser("rakib").password("rakib123").roles("ADMIN");
	     auth.inMemoryAuthentication().withUser("hasan").password("hasan123").roles("USER");*/
		
		auth.jdbcAuthentication().dataSource(dataSource)
		  .usersByUsernameQuery("select username,password, enabled from user where username=?")		  
		  .authoritiesByUsernameQuery("select username, role from user_roles where username=?");
		
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
 
	  http.csrf().disable()
	  	.authorizeRequests()
	  	 
	  	/*.antMatchers("/upload/**").access("hasRole('ROLE_ADMIN')")
		.and().httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint());*/
	  	.antMatchers("/upload/**").hasRole("ADMIN")
	  	.and().httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint());
	  	 
 	}
	
	@Bean
	public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint(){
		return new CustomBasicAuthenticationEntryPoint();
	}
	
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}
