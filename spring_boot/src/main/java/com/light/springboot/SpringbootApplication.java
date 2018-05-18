package com.light.springboot;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.light.springboot.Filter.TimeFilter;
import com.light.springboot.listener.ListenerTest;
import com.light.springboot.service.JavaMailComponent;
import com.light.springboot.servlet.ServletTest;
@EnableCaching
@Controller
@SpringBootApplication
public class SpringbootApplication implements ServletContextInitializer {
	
	@Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // 配置 Servlet
        servletContext.addServlet("servletTest",new ServletTest())
                      .addMapping("/servletTest");
        // 配置过滤器
        servletContext.addFilter("timeFilter",new TimeFilter())
                      .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST),true,"/*");
        // 配置监听器
        servletContext.addListener(new ListenerTest());
    }
	
	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class,args);
	}
	
	@Autowired
    private JavaMailComponent javaMailComponent;
	
	
	@RequestMapping("/mail")
	public void sendMail(){
		javaMailComponent.sendMail("563895591@qq.com");
	}
	
	
	
}
