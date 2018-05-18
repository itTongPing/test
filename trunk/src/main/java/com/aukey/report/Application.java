package com.aukey.report;

import java.io.InputStream;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.aukey.exception.GlobalExceptionHandler;
import com.aukey.security.meata.MeataHandler;

//@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling
@Configuration
@ComponentScan
@EnableAutoConfiguration
@RestController
@EnableMBeanExport(defaultDomain = "aukey-report")
@SessionAttributes("authorizationRequest")
public class Application extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}
	
	@Bean
    public MultipartConfigElement multipartConfigElement()
    {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(1024L * 1024L * 10L);
        return factory.createMultipartConfig();
    }
	@Autowired
	private DiscoveryClient client;
	@Value("${eureka.client.serviceUrl.defaultZone}")
	private String eurkahost;

	// 元数据文件位置,在Resources下
	@Value("${eureka.instance.metadata-map.meatafile}")
	private String meatafile;
 
	@Bean
	public MeataHandler meataHandler() {
		return new MeataHandler();
	}

	/**
	 * 初始化元数据微服务
	 * 
	 * @return
	 * @throws Exception
	 */

	@Bean
	CommandLineRunner init() {
		return (args) -> {
			ServiceInstance instance = client.getLocalServiceInstance();

			ClassPathResource metaResource = new ClassPathResource(meatafile);

			if (metaResource.exists()) {
				InputStream inputstrean = new ClassPathResource(meatafile).getInputStream();

				meataHandler().meataInvoke(instance, inputstrean, eurkahost);
			}
		};
	}

	

	/**
	 * 统一异常捕获
	 * 
	 * @return
	 */
	@Bean
	public GlobalExceptionHandler CustomizerError() {
		return new GlobalExceptionHandler();
	}

}
