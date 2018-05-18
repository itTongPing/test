package com.light.springboot.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.light.springboot.dao.DepartmentMapper;
import com.light.springboot.domain.Department;
import com.light.springboot.service.DepartmentService;

@RestController           //使用restController时下面不用指定@responseBody()(不解析视图)
@RequestMapping("department")
@ResponseBody
public class DepartmentController {

	
	@Autowired
	DepartmentMapper departmentMapper;
	
	@Autowired DepartmentService departmentService;
	
	@RequestMapping("insert")
	public String insert(){
		Department department = new Department();
        department.setId(4);
        department.setName("研发部");
        department.setDescr("开发产品");
        System.out.println(department);
        departmentMapper.insert(department);
		return "insert";
	}
	@RequestMapping("delete")
	public String deleteById(@PathVariable("id") Integer id ){
		departmentMapper.deleteById(id);
		return "delete";
	}
	
	
	  @RequestMapping("save")
	  public Map<String,Object> save() {
		 Department department = new Department();
		 department.setId(4);
	        department.setName("研发部");
	        department.setDescr("开发产品");
	    departmentService.save(department);
	    Map<String,Object> map = new HashMap<String,Object>();
	    map.put("code", "200");
	    map.put("msg", "保存成功");
	    return map;
	  }
	  @RequestMapping("get/{id}")
	  public Map<String,Object> get(@PathVariable("id") Integer id) {
	    Department department = departmentService.getDepartmentById(id);
	    Map<String,Object> map = new HashMap<String,Object>();
	    map.put("code", "200");
	    map.put("msg", "获取成功");
	    map.put("data", department);
	    return map;
	  }
	  @RequestMapping("update")
	  public Map<String,Object> update(Department department) {
	    departmentService.update(department);
	    Map<String,Object> map = new HashMap<String,Object>();
	    map.put("code", "200");
	    map.put("msg", "修改成功");
	    return map;
	  }
	  @RequestMapping("delete/{id}")
	  public Map<String,Object> delete(@PathVariable("id") Integer id) {
	    departmentService.delete(id);
	    Map<String,Object> map = new HashMap<String,Object>();
	    map.put("code", "200");
	    map.put("msg", "删除成功");
	    return map;
	  }
}
