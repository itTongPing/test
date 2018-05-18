package reflect;

import java.lang.reflect.Method;

import org.junit.Test;

public class ReflectTest {
	
	
	
	@Test
	public void test01(){
		Class c1=String.class;
		Class c2=int.class;
		Class c3=void.class;
		System.out.println(c1.getName());
		System.out.println(c1.getSimpleName());
		System.out.println(c2.getSimpleName());
		System.out.println(c3.getName());
	}

	
	
	public void getClassInfo(Object object){
		Class c=object.getClass();
        System.out.println("类的名称："+c.getName());

        /**
         * 一个成员方法就是一个method对象
         * getMethod()所有的 public方法，包括父类继承的 public
         * getDeclaredMethods()获取该类所有的方法，包括private ,但不包括继承的方法。
         */
        Method[] methods=c.getMethods();//获取方法
        //获取所以的方法，包括private ,c.getDeclaredMethods();

        for(int i=0;i<methods.length;i++){
            //得到方法的返回类型
            Class returnType=methods[i].getReturnType();
            System.out.print(returnType.getName());
            //得到方法名：
            System.out.print(methods[i].getName()+"(");

            Class[] parameterTypes=methods[i].getParameterTypes();
            for(Class class1:parameterTypes){
                System.out.print(class1.getName()+",");
            }
            System.out.println(")");
        }
		
		
	}
	@Test
	public void test02(){
		String s = "aa";
		getClassInfo(s);
		
	}
	
	
	
}
