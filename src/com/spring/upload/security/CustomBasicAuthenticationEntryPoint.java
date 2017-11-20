package com.spring.upload.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;


import com.spring.upload.model.LoginInfo;

public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
	
	private static final Logger logger=LogManager.getLogger(CustomBasicAuthenticationEntryPoint.class);
	public static ResponseEntity<String> badReuqest(){
		return new ResponseEntity<String>("Bad username or password", HttpStatus.BAD_REQUEST);
	}

    @Override
    public void commence(final HttpServletRequest request, 
    		final HttpServletResponse response, 
    		final AuthenticationException authException) throws IOException, ServletException {
    	
    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    	response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
    	/*PrintWriter writer = response.getWriter();
        writer.println("HTTP Status 401 : " + authException.getMessage());*/
    	badReuqest();
        logger.info("Login error: " +authException.getMessage());
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("MY_TEST_REALM");
        super.afterPropertiesSet();
    }
}