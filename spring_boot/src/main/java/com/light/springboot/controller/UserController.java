package com.light.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.light.springboot.domain.User;
import com.light.springboot.service.UserServiceImpl;

@RestController           //使用restController时下面不用指定@responseBody()(不解析视图)
@RequestMapping("user")
@ResponseBody
public class UserController {
	
	
	@Autowired
	UserServiceImpl userService;
	
	
	
	@RequestMapping("add")
	public int add(){
		User user =new User();
		user.setId(1);
		user.setUsername("aa");
		user.setPassword("456");
		return userService.addUser(user);
	}

}
