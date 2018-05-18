package com.light.springboot.configuration;
//@Component
//@ConfigurationProperties(prefix="ds")
public class DataSourceProperties {
	
	private String userName;
	
	private String password;
	
	
	
	public void show(){
		System.out.println("ds.userName:"+this.userName);
		System.out.println("ds.password:"+this.password);
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
	
	

}
