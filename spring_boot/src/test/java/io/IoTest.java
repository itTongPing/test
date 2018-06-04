package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class IoTest {
	
	public static void main(String[] args) throws IOException {
		copyFile("D:\\aa.txt","E:\\bb.txt");
	}

	
	
	
	
	public static void copyFile(String srcPath,String desPath) throws IOException{
		FileInputStream fis =null;
		FileOutputStream fos =null;
		
		
		try{
			fis = new FileInputStream(srcPath);
			fos = new FileOutputStream(desPath);
			
			byte[] b =new byte[1024];
			int hasRead = 0;
			while((hasRead = fis.read(b))>0){
				fos.write(b, 0, hasRead);
			}
		}catch(Exception e){
			
		}finally{
			fis.close();
			fos.close();
		}
	}
	
	
	
	
	public void testDelete(){
		File file = new File("D://java//aa.xsl");
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
