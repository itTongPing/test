package com.light.springboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

//@Configuration
public class BeanConfiguration {
	    @Bean
	    @Profile("dev")
	    public Runnable test1() {
	        System.out.println("开发环境使用的 Bean");
	        return () -> {};
	    }
	
	
}
