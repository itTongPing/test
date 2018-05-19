package com.light.springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.light.springboot.service.JavaMailComponent;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MailTest {

	  @Autowired
	    private JavaMailComponent javaMailComponent;
	    
	    @Test
	    public void test() {
	        this.javaMailComponent.sendMail("56389951@qq.com");
	    }
	
}
