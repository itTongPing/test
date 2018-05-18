package com.aukey.report.web.finance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*@author eric
 * @2016年11月17日
 *
 */
public class ZipUtils {
	 public static void zipFile(String fileName, ZipOutputStream out) throws IOException{
		 File file = new File(fileName);
	        if( file.exists() ){
	            byte[] buffer = new byte[1024];
	            FileInputStream fis = new FileInputStream(file);
	            String [] arys=  fileName.split("order");
	            out.putNextEntry(new ZipEntry("order"+arys[1]));
	            int len = 0 ;
	           
	            while ((len = fis.read(buffer)) > 0) {
	                out.write(buffer, 0, len);
	            }
	            out.flush();
	            out.closeEntry();
	            fis.close();
	        }
	    }
}
