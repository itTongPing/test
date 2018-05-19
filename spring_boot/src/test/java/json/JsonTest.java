package json;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class JsonTest {
	
	@Test
    public void testSimpleJSON(){
        Student stu = new Student("xuliugen", "nan", "123123", 100);
        Course course = new Course("JAVA", "xiaobin", 100);
        stu.setCourse(course);
        String json = JSON.toJSONString(stu);
        System.out.println(json);
    }
	
	
	@Test
    public void testListJSON(){
		Student jt1 = new Student("xuliugen", "nan");
		Student jt2 = new Student("xieyan", "nv");
        List<Student> li = new ArrayList<Student>();
        li.add(jt1);
        li.add(jt2);
        String jsonstr = JSON.toJSONString(li);
        System.out.println(jsonstr);
    }

		
	   @Test
	    public void testParseSimpleJSON(){
	        String json = "[{\"name\":\"xuliugen\",\"sex\":\"nan\"},{\"name\":\"xieyan\",\"sex\":\"nv\"}]";
	        JSONArray jsonArray = JSON.parseArray(json);
	        String str = jsonArray.getString(0);
	        Student jsonTest = JSON.parseObject(str,Student.class);  //这里会创建一个Student对象，默认构造
	        System.out.println(jsonTest.getSex());
	    }
	   
	   @Test
	    public void testParseSimpleJSON2(){
	        String json = "[{\"name\":\"xuliugen\",\"sex\":\"nan\"},{\"name\":\"xieyan\",\"sex\":\"nv\"}]";
	       // JSONArray jsonArray = JSON.parseArray(json);
	       // String str = jsonArray.getString(0);
	       List jsonTest = JSON.parseObject(json,List.class);  //这里会创建一个Student对象，默认构造
	        System.out.println(jsonTest.get(0).getClass());
	    }
	
	
	 	//学生中包含课程
	    @Test
	    public void testParseStudentIncludeCourseJSON() {
	        String json = "{\"course\":{\"coursename\":\"JAVA\",\"coursescore\":\"100\",\"courseteacher\":\"xiaobin\"},\"password\":\"123123\",\"score\":\"100\",\"sex\":\"nan\",\"name\":\"xuliugen\"}";
	        Student stu = JSON.parseObject(json,Student.class);
	        System.out.println(stu.getPassword());
	    }
	
	
	    
	    @Test
	    public void testParseListStudentIncludeCourseJSON() {
	        String json = "[{\"course\":{\"coursename\":\"JAVA\",\"coursescore\":\"100\",\"courseteacher\":\"xiaobin\"},\"password\":\"123123\",\"score\":\"100\",\"sex\":\"nan\",\"name\":\"xuliugen123\"},{\"course\":{\"coursename\":\"music\",\"coursescore\":\"100\",\"courseteacher\":\"qwe\"},\"password\":\"123123\",\"score\":\"100\",\"sex\":\"nan\",\"name\":\"xieyan\"}]";

	        JSONArray jsonArray = JSON.parseArray(json);
	        String str = jsonArray.getString(0);
	        Student stu = JSON.parseObject(str, Student.class);
	        System.out.println(stu.getName());
	    }
	
	   //a = a|b    将a和b转成二进制，执行或操作赋值给a 
	    @Test
	    public void test(){
	    	
	    	int a = 1;
	    	int b = 2;
	    	System.out.println(a |=b);
	    }
	    
	    
	    
	    
	    
	    
	    
}
