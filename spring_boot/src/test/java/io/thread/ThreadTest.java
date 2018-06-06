package io.thread;

public class ThreadTest {
	
	 public static void main(String[] args) {  
	       // for (int i = 0; i < 100; i++) {  
	           // System.out.println(Thread.currentThread().getName() + " " + i);  
	           // if (i == 30) { 
		 			ShareData shareData = new ShareData();
	                Runnable myRunnable = new MyRunnable(shareData); // 创建一个Runnable实现类的对象  
	             //myRunnable.run();并不是线程开启，而是简单的方法调用  
	                Thread thread1 = new Thread(myRunnable,"A窗口（线程）"); // 将myRunnable作为Thread target创建新的线程  
	                Thread thread2 = new Thread(myRunnable,"B窗口（线程）");  
	  
	            //thread1.run(); //如果该线程是使用独立的 Runnable 运行对象构造的，则调用该 Runnable 对象的 run 方法；否则，该方法不执行任何操作并返回。  
	  
	                thread1.start(); // 调用start()方法使得线程进入就绪状态  
	                thread2.start();  
	           // }  
	      //  }  
	    }  

}
