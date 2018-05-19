package dataType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class DataTest {
	
	@Test
	public void test(){
		
		double d=0.02;
		double d2 = 0.03;
		System.out.println(d2-d);
		
	}
	@Test
	public void test02(){
		String ids = "1";
		String[] split = ids.split(",");
		
		List<String> asList = Arrays.asList(split);
		System.out.println(asList);
		
	}
	
	
	@Test
	public void test03(){
		
		String format = new java.text.DecimalFormat("#.0000").format(3);
		BigDecimal bigDecimal = new BigDecimal(format);
		System.out.println(bigDecimal);
	}
	
	
	
	
}
