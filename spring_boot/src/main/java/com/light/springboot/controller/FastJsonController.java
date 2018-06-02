package com.light.springboot.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.light.springboot.domain.User;

@Controller
@RequestMapping("fastjson")
public class FastJsonController {

	
	@RequestMapping("/test")
    @ResponseBody
    public User test() {
        User user = new User();
        user.setId(1);
        user.setUsername("jack成");
        user.setPassword("jack123");
        user.setBirthday(new Date());
        
        // 模拟异常
       // int i = 1/0;
        return user;
    }
}

