package com.aukey.report.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

/**
 * Web cas 登陆Session灯状态切面拦截
 *
 * @version 1.0.0
 */
@Aspect
@Order(5)
@Component
public class WebLoginAspect {

    private Logger logger = Logger.getLogger(getClass());


    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.aukey.report.web..*.*(..))")
    public void casLogin() {
    } //只是一个标记，方法体不会被执行，类似于spring2.x配置文件里面的<aop:pointcut id="beforePointCut" ...> 具体逻辑在befor after,around里写


    @Value("${string.version.number}")
    private String version;
    
    @Value("${url.cas.server}")
    private String cassever;

    @Value("${nc.server.url}")
    private String ncServerUrl;


    @Before("casLogin()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        try {
            request.setAttribute("cassever", cassever);
            request.setAttribute("ncUrl", ncServerUrl);
            Map<String, Object> map = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (map != null) {
                Object obj = map.get("username");
                request.setAttribute("account", obj);
                Map<String, String> mp = (Map<String, String>) map.get("user");
                request.setAttribute("user", mp);
                request.setAttribute("userId", mp.get("userId"));
                request.setAttribute("name", mp.get("name"));
                Object password = map.get("password");
                request.setAttribute("password", password);
            }

        } catch (Exception e) {
            // TODO: handle exception

            logger.info("no login");
        }

        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));

    }

    @AfterReturning(returning = "ret", pointcut = "casLogin()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info("RESPONSE : " + ret);
        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 版本号
        request.setAttribute("version", version);
    }


}

