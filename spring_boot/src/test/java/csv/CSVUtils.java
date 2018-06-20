package csv;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import com.csvreader.CsvWriter;

public class CSVUtils {

	public static <T> void exportCSV(List<T> list,String[] headers) throws Exception {
		//写入临时文件
         File tempFile = File.createTempFile("vehicle", ".csv");
         CsvWriter csvWriter = new CsvWriter(tempFile.getCanonicalPath(),',', Charset.forName("UTF-8"));
         csvWriter.writeRecord(headers);
		    for(T  target : list){
		    	 Class<? extends Object> clazz = target.getClass();
				 Field[] fields = target.getClass().getDeclaredFields();//获得属性  	
				 //获得Object对象中的所有方法  
				    for(Field field:fields){  
				        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz); 
				        System.out.println(field.getName());
				        Method getMethod = pd.getReadMethod();//获得get方法  
				        Object object = getMethod.invoke(target);//此处为执行该Object对象的get方法 
				        csvWriter.write(object==null?"-":object.toString());
				    } 
				    csvWriter.endRecord();
		    }
		    csvWriter.close();
		    
		      File fileLoad = new File(tempFile.getCanonicalPath());    
		      OutputStream out = new FileOutputStream("D:\\aa.csv");
		      OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
		      InputStream  in = new FileInputStream(fileLoad);
		      InputStreamReader isr = new InputStreamReader(in);
		      int n;
		     char[] b = new char[10240];
		  	while ((n = isr.read(b)) != -1) {  
		    	 osw.write(b, 0, n); //每次写入out1024字节 
		  	}  
		  	     osw.flush();
		    	 in.close();  
		    	 out.close();
		    
		
	}
	
	
	private <T> void getFiledValue(T target){
		 Class clazz = target.getClass();
		 Field[] fields = target.getClass().getDeclaredFields();//获得属性  
	}

	@Test
	public void test01() throws Exception {
		List<Student> ls = new ArrayList<Student>();
		for (int i = 0; i < 10; i++) {
			Student s = new Student();
			s.setName("小帅");
			s.setAge(10);
			s.setScore("10\\\r0".replace("\r", "\\r").replace("\t", "\\t").replace("\n", "\\n"));
			s.setSex("男");
			ls.add(s);
		}
		 String[] headers = {"姓名","年龄","编号","性别"};
		exportCSV(ls,headers);	
		
	}
	
	
	
	/**
	 * 添加加了注解的属性名并排序
	 * 
	 * @param fields
	 * @return
	 */
/*	private static List<Field> getOrderedField(Field[] fields) {
		// 用来存放所有的属性域
		List<Field> fieldList = new ArrayList<>();
		// 过滤带有注解的Field
		for (Field f : fields) {
			if (f.getAnnotation(BeanFieldAnnotation.class) != null) {
				fieldList.add(f);
			}
		}
		// 这个比较排序的语法依赖于java 1.8
		fieldList.sort(Comparator.comparingInt(m -> m.getAnnotation(
				BeanFieldAnnotation.class).order()));
		return fieldList;
	}*/
}
