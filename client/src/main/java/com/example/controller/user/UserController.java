package com.example.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.User;
import com.example.service.impl.UserServiceImpl;


@RestController           //使用restController时下面不用指定@responseBody()(不解析视图)
@RequestMapping("user")
@ResponseBody
public class UserController {

	

	@Autowired
	UserServiceImpl userService;
	@RequestMapping("delete")
	public int add() throws Exception{
		User user =new User();
		user.setId(1);
		user.setUsername("aa");
		return userService.delete(user);
	}

	
}
