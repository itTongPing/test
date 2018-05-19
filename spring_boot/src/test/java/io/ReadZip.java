package io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class ReadZip {
	
	public static void main(String[] args) throws IOException {
       // ReadZip rz=new ReadZip();
        //rz.readZipContext();
		//String pdf = getTextFromPDF("D:\\dd.pdf");
		File file = new File("D:\\dd.pdf");
		file.delete();
		//System.out.println(pdf);
        
    }
    public void readZipContext() throws IOException{
        String zipPath="D:\\aa.zip";
        ZipFile zf=new ZipFile(zipPath);

        InputStream in=new BufferedInputStream(new FileInputStream(zipPath));
        ZipInputStream zin=new ZipInputStream(in);
        //ZipEntry 类用于表示 ZIP 文件条目。
        ZipEntry ze;
        while((ze=zin.getNextEntry())!=null){
            if(ze.isDirectory()){
                //为空的文件夹什么都不做 
            }else{

                System.err.println("file:"+ze.getName()+"\nsize:"+ze.getSize()+"bytes");
                if(ze.getSize()>0){
                    BufferedReader reader;
                    try {
                        reader = new BufferedReader(new InputStreamReader(zf.getInputStream(ze), "utf-8"));
                        String line=null;
                        while((line=reader.readLine())!=null){
                            System.out.println(line);
                        }
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
    
    
    public static String getTextFromPDF(String pdfFilePath)   
    {  
        String result = null;  
        FileInputStream is = null;  
        PDDocument document = null;  
        try {  
            is = new FileInputStream(pdfFilePath);  
            PDFParser parser = new PDFParser(is);  
            parser.parse();  
            document = parser.getPDDocument();  
            PDFTextStripper stripper = new PDFTextStripper();  
            result = stripper.getText(document);  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (is != null) {  
                try {  
                    is.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            if (document != null) {  
                try {  
                    document.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return result;  
    }  

}
