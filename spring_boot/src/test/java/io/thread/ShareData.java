package io.thread;

public class ShareData {

	
	
	public  int count = 100;
	
	 public synchronized void decrease() {  
         count--;  
         System.out.println(Thread.currentThread().getName() + "decrease this count: " + count);  
     }  

     public synchronized void increment() {  
         count++;  
         System.out.println(Thread.currentThread().getName() + "increment this count: " + count);  
     }
}
