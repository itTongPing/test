package csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.csvreader.CsvWriter;

public  class  CSVTest {
    @Test
    public void write(){
    try {
             // 创建CSV写对象
            List<Student> ls=new ArrayList<Student>();
            for (int i = 0; i <10; i++) {
            Student s=new Student();
            s.setName("小---帅"+i);
            s.setAge(i);
            s.setScore("1    00"+i);
            s.setSex("男   "+i);
            ls.add(s);
}
             //写入临时文件
             File tempFile = File.createTempFile("vehicle", ".csv");
             CsvWriter csvWriter = new CsvWriter(tempFile.getCanonicalPath(),',', Charset.forName("UTF-8"));
             // 写表头
             long s= System.currentTimeMillis();
             System.err.println();
             String[] headers = {"姓名","年龄","编号","性别"};
             csvWriter.writeRecord(headers);
             for (Student stu : ls) {
		csvWriter.write(stu.getName());
		csvWriter.write(stu.getAge()+"");
		csvWriter.write(stu.getScore());
		csvWriter.write(stu.getSex());
		csvWriter.endRecord();
		}
             csvWriter.close();
             long e=System.currentTimeMillis();

             System.err.println(e-s);
     
            
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
          
             
             
             

   /**
    * 写入csv结束，写出流
    */
  /* java.io.OutputStream out = getResponse.getOutputStream();  
   byte[] b = new byte[10240];
   java.io.File fileLoad = new java.io.File(tempFile.getCanonicalPath());  
   getResponse.reset();
   getResponse.setContentType("application/csv");  
   getResponse.setHeader("content-disposition", "attachment; filename=vehicleModel.csv");  
	long fileLength = fileLoad.length();  
	String length1 = String.valueOf(fileLength);  
	getResponse.setHeader("Content_Length", length1);  
	java.io.FileInputStream in = new java.io.FileInputStream(fileLoad);  
	int n;  
	while ((n = in.read(b)) != -1) {  
  	 out.write(b, 0, n); //每次写入out1024字节 
	}  
  	 in.close();  
  	 out.close(); */

         } catch (Exception e) {
             e.printStackTrace();
         }
    }

}
