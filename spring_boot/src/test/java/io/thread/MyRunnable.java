package io.thread;

public class MyRunnable implements Runnable{

	    ShareData shareData;
	    
	    public MyRunnable( ShareData shareData){
	    	this.shareData = shareData;
	    }
	  
	    @Override  
	    public void run() {  
	    	while(shareData.count > 1){
	    		shareData.decrease();
	    	
	    	}
	    }
	    
	    
	   

}
