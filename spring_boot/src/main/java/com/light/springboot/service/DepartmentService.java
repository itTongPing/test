package com.light.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.light.springboot.dao.DepartmentMapper;
import com.light.springboot.domain.Department;
@CacheConfig(cacheNames = "department")
@Service("departmentService")
public class DepartmentService {
	
	
	 @Autowired
	  private DepartmentMapper departmentMapper;
	  @CachePut(key = "#department.id")    //这个注释可以确保方法被执行，同时方法的返回值也被记录到缓存中。    
	  public Department save(Department department) {
	    System.out.println("保存 id=" + department.getId() + " 的数据");
	    this.departmentMapper.insert(department);
	    return department;
	  }
	  @CachePut(key = "#department.id")
	  public Department update(Department department) {
	    System.out.println("修改 id=" + department.getId() + " 的数据");
	    this.departmentMapper.update(department);
	    return department;
	  }
	  @Cacheable(key = "#id")//当重复使用相同参数调用方法的时候，方法本身不会被调用执行，即方法本身被略过了，取而代之的是方法的结果直接从缓存中找到并返回了。
	  public Department getDepartmentById(Integer id) {
	    System.out.println("获取 id=" + id + " 的数据");
	    Department department = this.departmentMapper.getById(id);
	    return department;
	  }
	  @CacheEvict(key = "#id")
	  public void delete(Integer id) {
	    System.out.println("删除 id=" + id + " 的数据");
	    this.departmentMapper.deleteById(id);
	  }
	

}
