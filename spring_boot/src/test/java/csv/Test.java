package csv;

import java.net.URLEncoder;

public class Test {
	
	public static void main(String[] args) throws Exception {
		
		
		Student s = new Student();
		s.setName("小帅");
		s.setAge(10);
		s.setScore("100");
		s.setSex("男");
		
		//CSVUtils.exportCSV(s);
		
		System.out.println(URLEncoder.encode("55赫尔3215.csv", "gbk"));
		
	}
	
	@org.junit.Test
	public void test01(){
		StringBuffer sb = new StringBuffer();
		int index  = 0;
		while(true){
			index++;
			sb.append(index);
			if(index %100 == 0){
				System.out.println(index);
			}
		}
	}

	@org.junit.Test
	public void show(){
		String str = ":::RKB18052300013,IQC18052300014,CG18052300005,144426_4,（中性）美国日全食眼镜 腿弯曲款 黑橙绿蓝变参 颜色->蓝色,卢玉巧,卢玉巧,小包中转仓,100,0,0.0408,153.0,HK-4PX澳洲仓-001-自发货,深圳市傲基电子商务股份有限公司,海运,1,2018-05-23 14:18:57.0,chensi,王丹丹,华杰小包系统销售,RKB18052300013,三只松老虎,1.53,1.31,USD";
		System.out.println(str.length());
	
	}
	
	
}
