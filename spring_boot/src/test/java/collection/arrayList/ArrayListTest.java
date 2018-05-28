package collection.arrayList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ArrayListTest {
	
	public static void main(String[] args) throws Exception {
		UserInfo userInfo = new UserInfo("hehe","123456");
		System.out.println(userInfo);
		
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("userInfo.out"));
			oos.writeObject(userInfo);
			oos.close();
		}catch(Exception e){
			
		}
		
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("userinfo.out"));
		UserInfo userInfo2 = (UserInfo)ois.readObject();
		System.out.println(userInfo2);
	}

}
