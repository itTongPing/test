package com.light.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.light.springboot.domain.ClazzEntity;
import com.light.springboot.domain.StudentEntity;

@Mapper
public interface InfoManagerMapper {
	
	public List<ClazzEntity> getClassByID(Integer id);
	
	public List<StudentEntity> getStudentByClassID(Integer id);

}
