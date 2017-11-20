package com.spring.upload.dataconfig;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.spring.upload.FileUploadController.UploadController;

@Configuration
public class DataConfig extends WebMvcConfigurerAdapter {
	
	public static final Logger logger=LogManager.getLogger(DataConfig.class);
	@Bean(name = "dataSource")
    DataSource dataSource() {
        DataSource dataSource = null;
        JndiTemplate jndi = new JndiTemplate();
        try {
            dataSource = jndi.lookup("java:comp/env/jdbc/database", DataSource.class);
        } catch (NamingException e) {
            logger.error("NamingException for java:comp/env/jdbc/database", e);
        }
        return dataSource;
    }
  
}
