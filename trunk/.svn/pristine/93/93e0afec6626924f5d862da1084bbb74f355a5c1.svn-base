/**
 * 本文件版权由傲基电子商务有限公司所有，侵权必究  CopyRight © 2016-2026
 * cas#com.aukey.cas.configuration#WebMvcConfig.java
 */
package com.aukey.report.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.aukey.security.tag.ThymeLeafDialect;

	
/**
 * @author long.tang 2016年8月9日
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	/**
	 * 
	 */
	public WebMvcConfig() {
	}
	

	@Value("${url.cas.server}")
	private String casserverUrl;
	
//	@Autowired
//	private ServletContextTemplateResolver templateResolver;

	/*
	 * @param registry
	 * 
	 * @see
	 * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
	 * #addViewControllers(org.springframework.web.servlet.config.annotation.
	 * ViewControllerRegistry) overridden by long.tang 2016年8月9日
	 */
//	@Override
//	public void addViewControllers(ViewControllerRegistry registry) {
//		// registry.addViewController("/login").setViewName("login");
//		// registry.addViewController("/oauth/confirm_access").setViewName(
//		// "authorize");
//		registry.addViewController("/login").setViewName("login");
//		registry.addViewController("/hello").setViewName("hello");
//		registry.addViewController("/oauth/confirm_access").setViewName(
//				"authorize");
//		
//		// registry.addViewController("/oauth/authorize").setViewName("authorize");
//	}
	  @Bean
	  public ThymeLeafDialect thymeLeafDialect() { 
		  ThymeLeafDialect thymeLeafDialect = new ThymeLeafDialect();
		  thymeLeafDialect.setCasServiceUrl(casserverUrl);
	    return thymeLeafDialect; 
	  }
    
	 @Bean
     public ClassLoaderTemplateResolver templateResolver() {
         ClassLoaderTemplateResolver classLoaderTemplateResolver = new ClassLoaderTemplateResolver();
         classLoaderTemplateResolver.setCacheable(false);
         classLoaderTemplateResolver.setPrefix("templates/");
         classLoaderTemplateResolver.setSuffix(".html");
         classLoaderTemplateResolver.setTemplateMode("HTML5");
         classLoaderTemplateResolver.setCacheable(false);
         return classLoaderTemplateResolver;
     }

	  @Bean
	    public SpringSecurityDialect securityDialect() {
	        return new SpringSecurityDialect();
	    }

     @Bean
     public SpringTemplateEngine templateEngine() {
         SpringTemplateEngine templateEngine = new SpringTemplateEngine();
         templateEngine.setTemplateResolver(templateResolver());
         templateEngine.addDialect(thymeLeafDialect());
         templateEngine.addDialect(securityDialect());
         return templateEngine;
     }

     @Bean
     public ThymeleafViewResolver viewResolver() {
         ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
         viewResolver.setTemplateEngine(templateEngine());
         

         return viewResolver;
     }

     @Override
     public void addResourceHandlers(ResourceHandlerRegistry registry) {
         registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/");
     }
}
