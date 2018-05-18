package com.aukey.report.configuration;

import java.io.IOException;
import java.util.Map;

import javax.naming.CommunicationException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aukey.auth.DataAuthListener;
import com.aukey.auth.DataAuthTypeListener;
import com.aukey.security.CustomBeforeSecurityContextFilter;
import com.aukey.security.InvocationSecurityMetadataSourceService;
import com.aukey.security.MatchSecurityUserFilter;
import com.aukey.security.MenuInstanceFilter;
import com.aukey.security.SecurityAccessDecisionManager;
import com.aukey.security.SecurityAccessDeniedHandler;
import com.aukey.security.SecurityLoginUrlAuthenticationEntryPoint;
import com.aukey.util.AjaxResponse;
import com.aukey.util.CommonUtil;
import com.aukey.util.HttpUtil;
import com.aukeys.ibatis.sql.SqlParser;

@Component
@EnableOAuth2Sso
public  class LoginConfigurer extends WebSecurityConfigurerAdapter {
	
	  @Value("${security.oauth2.custom.server-logout-url}")
	  private String serverLogoutUrl;
	  @Value("${spring.datasource.url}")
	  private String datasourceUrl;
	  @Value("${security.oauth2.custom.server-logouted-redirect-url}")
	  private String serverLogoutedRedirectUrl;
//	  @Autowired
//	  private ModuleService moduleService; 
	
	  private SecurityAccessDecisionManager securityAccessDecisionManager=new SecurityAccessDecisionManager(); 
	  
	  private SecurityAccessDeniedHandler securityAccessDeniedHandler=new SecurityAccessDeniedHandler() ; 
 
	  private SecurityLoginUrlAuthenticationEntryPoint securityLoginUrlAuthenticationEntryPoint=new SecurityLoginUrlAuthenticationEntryPoint() ; 
//	  @Autowired
//	  private LoginSuccessHandler loginSuccessHandler;
//	  @Autowired
//	  private LoginFailureHandler loginFailureHandler;
	 
	  private InvocationSecurityMetadataSourceService invocationSecurityMetadataSourceService=new InvocationSecurityMetadataSourceService("product");
	  
	  
	
	  
	  

	@Value("${url.cas.server}")
	private String casserverUrl;

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		super.configure(auth);
	}


	
	  @Bean
	  public MenuInstanceFilter menuInstanceFilter() { 
		  MenuInstanceFilter menuInstanceFilter = new MenuInstanceFilter();
		  menuInstanceFilter.setCasServiceUrl(casserverUrl);
		  //menuInstanceFilter.setParentName("Wish产品发布");
	      return menuInstanceFilter; 
	  }
	 @Bean
	 public DataAuthTypeListener dataAuthTypeListener() throws Exception {
		String schema = CommonUtil.subDatasource(datasourceUrl);
		String url = casserverUrl + "/auth/queryAuthType?schema="+schema;
		AjaxResponse result = HttpUtil.doGet(url);
		//.doGet(url);
		Map<Integer,Object > targetMap = null;
		if (result != null&&result.isSuccess()) // 取到接口模块数据
		{
			targetMap = JSON.parseObject(String.valueOf(result.getData()),
					new TypeReference<Map<Integer,Object>>() {
					}.getType());
		}
		else{
			throw new CommunicationException("Unable to get the link to the server,please check the url for the cas server!");
		}
		return new DataAuthTypeListener(targetMap);
	 }
	@Bean
	public DataAuthListener dataAuthListener() throws Exception {
		return new DataAuthListener(datasourceUrl);
	}

	

	   /**
	    * ignored public module - resource;
	    */
	   @Override
	   public void configure(WebSecurity web) throws Exception {
	         web.ignoring().antMatchers("/css/**", "/js/**", "/images/**","/bootstrap/**","/bootstrap-datetimepicker/**","/bootstrap-fileinput/**","/bootstrap-lightbox/**","/bootstrap-modal/**", 
				      "/bootstrap-select/**","/bootstrap-table/**","/category/**","/ckeditor/**","/echarts/**","/flyer/**","/fonts/**","/landing/**","/wishaccount/**","/ztree/**");
	    }
	
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();
		SqlParser.initCas(casserverUrl);
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
		//joinModuleAndRole(registry);
		registry.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            public <O extends FilterSecurityInterceptor> O postProcess(
                    O fsi) {
                fsi.setSecurityMetadataSource(invocationSecurityMetadataSourceService);
                return fsi;
            }
        });
		
		registry.anyRequest()
				.authenticated()
				//权限决策器
				.accessDecisionManager(securityAccessDecisionManager)
				.and()
				.addFilterAfter(new CustomBeforeSecurityContextFilter(casserverUrl), SecurityContextPersistenceFilter.class)
				.addFilterBefore(new MatchSecurityUserFilter(casserverUrl),
					      FilterSecurityInterceptor.class)
				//.addFilterAfter(menuInstanceFilter(), FilterSecurityInterceptor.class)
				.formLogin()
//				.successHandler(loginSuccessHandler)
//				.failureHandler(loginFailureHandler)
				.and()
				.exceptionHandling()
//				验证用户对URL是否有权限类(针对ajax, security配置原本不支持ajax，必须重写)
				.accessDeniedHandler(securityAccessDeniedHandler)
				.authenticationEntryPoint(securityLoginUrlAuthenticationEntryPoint)
				.and().csrf()
				.csrfTokenRepository(csrfTokenRepository())
				.and()
				.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
				.logout()
				.deleteCookies("JSESSIONID","XSRF-TOKEN")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.invalidateHttpSession(true).logoutSuccessUrl(serverLogoutUrl + "?next=" + serverLogoutedRedirectUrl).permitAll()
				.and().headers().cacheControl();
	}

	private Filter csrfHeaderFilter() {
		return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request,
					HttpServletResponse response, FilterChain filterChain)
					throws ServletException, IOException {
				CsrfToken csrf = (CsrfToken) request
						.getAttribute(CsrfToken.class.getName());
				if (csrf != null) {
					Cookie cookie = new Cookie("XSRF-TOKEN",
							csrf.getToken());
					cookie.setPath("/");
					response.addCookie(cookie);
				}
				filterChain.doFilter(request, response);
			}
		};
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}
	
//	private void joinModuleAndRole(
//			ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
//
//		String url = casserverUrl + "/moduleRest/%S";
//		url = String.format(url, "product");
//		try {
//			String result = HttpUtil.doGet(url);
//
//			Map<String, Set<String>> targetMap = null;
//
//			if (result != null) // 取到接口模块数据
//			{
//				targetMap = JSON.parseObject(result,
//						new TypeReference<Map<String, Set<String>>>() {
//						}.getType());
//				for (Map.Entry<String, Set<String>> entry : targetMap
//						.entrySet()) {
//					String urlPermission = entry.getKey();
//					Set<String> RoleList = entry.getValue();
//					if (RoleList != null && RoleList.size() > 0) {
//						String[] roles = RoleList.toArray(new String[0]);
//						registry.antMatchers(urlPermission).hasAnyAuthority(roles);
//					}
//				}
//			}
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		
//	}
}