package com.light.springboot.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.light.springboot.Filter.TimeFilter;
import com.light.springboot.interceptor.TimeInterceptor;
import com.light.springboot.listener.ListenerTest;
import com.light.springboot.servlet.ServletTest;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{

	
	    @Autowired
	    private TimeInterceptor timeInterceptor;
	
	    @Bean
	    public HttpMessageConverters fastJsonHttpMessageConverters() {
	        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
	        FastJsonConfig fastJsonConfig = new FastJsonConfig();
	        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
	        
	        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
	        
	        HttpMessageConverter<?> converter = fastJsonHttpMessageConverter;
	        
	        return new HttpMessageConverters(converter);
	    }
	    
	    /**
	     * 添加到过滤器链中，此方式适用于使用第三方的过滤器。将过滤器写到 WebConfig 类中
	     * @return
	     */
	    @Bean
	    public ServletRegistrationBean servletRegistrationBean() {
	        return new ServletRegistrationBean(new ServletTest(),"/servletTest");
	    }
	    
	    @Bean
	    public FilterRegistrationBean timeFilter() {
	        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	        
	        TimeFilter timeFilter = new TimeFilter();
	        registrationBean.setFilter(timeFilter);
	        
	        List<String> urls = new ArrayList<>();
	        urls.add("/*");
	        registrationBean.setUrlPatterns(urls);
	        
	        return registrationBean;
	    }
	    
	    
	    @Bean
	    public ServletListenerRegistrationBean<ListenerTest> servletListenerRegistrationBean() {
	        return new ServletListenerRegistrationBean<ListenerTest>(new ListenerTest());
	    }

	   
	    @Override
	    public void addInterceptors(InterceptorRegistry registry) {
	        registry.addInterceptor(timeInterceptor);
	    }
	    
	    
}
