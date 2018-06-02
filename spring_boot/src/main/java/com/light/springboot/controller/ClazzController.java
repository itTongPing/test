package com.light.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.light.springboot.domain.ClazzEntity;
import com.light.springboot.mapper.InfoManagerMapper;

@RestController           //使用restController时下面不用指定@responseBody()(不解析视图)
@RequestMapping("clazz")
@ResponseBody
public class ClazzController {
	
	@Autowired
	InfoManagerMapper infoManagerMapper;
	
	
	@RequestMapping("search")
	public List<ClazzEntity> search(){
		List<ClazzEntity> classByID = infoManagerMapper.getClassByID(1);
		
		System.out.println(classByID);
		return classByID;
		
	}

}
