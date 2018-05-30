package collection.arrayList;

import java.io.Serializable;

public class UserInfo implements Serializable{

	 private static final long serialVersionUID = 996890129747019948L;
	     private String name;
	     private transient String psw;  //transient
	 
	     public UserInfo(String name, String psw) {
	         this.name = name;
	         this.psw = psw;
	     }
	 
	     public String toString() {
	         return "name=" + name + ", psw=" + psw;
	     }
	
	
}
