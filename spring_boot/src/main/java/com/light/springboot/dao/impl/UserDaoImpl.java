package com.light.springboot.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.light.springboot.dao.UserDao;
import com.light.springboot.domain.User;

public class UserDaoImpl implements UserDao {

	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public int insert(User user) {
		String sql = "insert into user values(?,?,?,?)";
		return jdbcTemplate.update(sql, user.getId(),user.getUsername(),user.getPassword(),user.getBirthday());
	}

	@Override
	public int deleteById(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public User getById(Integer id) {
		String sql = "select * from user where id = ?";
		
		final BeanPropertyRowMapper<User> rowMapper = new BeanPropertyRowMapper<User>(
	            User.class);
		return null;
	}

}
