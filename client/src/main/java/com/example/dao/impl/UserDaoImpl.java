package com.example.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.domain.User;

@Service("userDao")
public class UserDaoImpl {

	
	@Autowired
	JdbcTemplate jdbcTemplate;
	public int delete(User user){
		String sql = "delete from user where username = ?";
		return jdbcTemplate.update(sql, user.getUsername());
	}
}
