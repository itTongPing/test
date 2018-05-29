package com.example.servicefeign;

import org.springframework.stereotype.Component;

/**
 * fallback指定的类，
 * @author a
 *
 */
@Component
public class SchedualServiceHiHystric implements SchedualServiceHi {

	@Override
	public String sayHiFromClientOne(String name) {
		 return "sorry "+name;
	}

}
