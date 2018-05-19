package collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;


public class ListTest {
	@Test
	public void test(){
		
		
		Object[] arr = {};
		System.out.println(arr.length);
		arr[0] = 1;
		System.out.println(arr.length);
		
		
	}
	
	
	@Test
	public void test2(){
		List x = Arrays.asList("a", "b", "c");
		Object[] array = x.toArray();
		
		
		
		List<Object> l = new ArrayList<Object>(Arrays.asList("foo", "bar"));
		l.set(0, new Object());
		
		
		  String[] s=new String[]{"hello","world"};
		  List<String> list=Arrays.asList(s);
		  Object[] a=list.toArray();
		  System.out.println(a.getClass().getName());
		
	}
		


}
