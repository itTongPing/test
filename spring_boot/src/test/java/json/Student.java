package json;

import com.alibaba.fastjson.annotation.JSONField;

public class Student {
       @JSONField(ordinal = 1)
	   private String name;
       @JSONField(ordinal = 3)
	   private String  sex;
	   @JSONField(ordinal = 2)
	   private String  password;
	   @JSONField(ordinal = 4)
	   private Integer age ;
	   @JSONField(ordinal = 5)
	   private Course course;
	   
	   
	   
		public Student() {
		} 
	   
	public Student(String name, String sex) {
		super();
		this.name = name;
		this.sex = sex;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public Student(String name, String sex, String password, Integer age) {
		this.name = name;
		this.sex = sex;
		this.password = password;
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	

}
