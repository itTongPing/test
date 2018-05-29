package com.example.serviceribbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    /***
     * 当请求接口不可用时 会执行快速失败，直接返回一组字符串，而不是等待响应超时，这很好的控制了容器的线程阻塞
     * @param name
     * @return
     */
    @HystrixCommand(fallbackMethod = "hiError")  //该注解对该方法创建了熔断器的功能，并指定了fallbackMethod熔断方法
    public String hiService(String name) {
        return restTemplate.getForObject("http://SERVICE-HI/hi?name="+name,String.class);
    }

        //熔断方法
        public String hiError(String name) {
            return "hi,"+name+",sorry,error!";
        }


}
