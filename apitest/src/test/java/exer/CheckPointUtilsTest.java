package exer;

import com.github.checkpoint.CheckPointUtils;

public class CheckPointUtilsTest {
	   public static void main(String[] args) {
		    //jsonobject 测试
		    String testjson ="{\"uid\":\"test\"}";
		    CheckPointUtils.openLog=false;
			System.out.println("-----json校验");
			System.out.println(CheckPointUtils.check(testjson,"uid=test").getMsg());
			
		    System.out.println("-----多值组合测试 ");
		    testjson ="{\"msg\":\"登录成功\",\"uid\":\"DAD3483647A94DBDB174C4C036CA8A80\",\"code\":\"1\",\"code2\":\"2\",\"code3\":\"3\"}";
		    System.out.println(CheckPointUtils.check(testjson,"code=1&&code2>=1||code3>=5").getMsg());
			
			System.out.println("-----不存在值判断");
			System.out.println(CheckPointUtils.check(testjson,"uidtest=12").getMsg());
			
			//json数组测试
			System.out.println("-----json数组测试");
			testjson="{\"code\":\"1\",\"data\":[{\"name\":\"testfan0\",\"pwd\":\"pwd0\"},{\"name\":\"testfan1\",\"pwd\":\"pwd1\"},{\"name\":\"testfan2\",\"pwd\":\"pwd2\"}]}";
			System.out.println(CheckPointUtils.check(testjson,"name[1]=testfan1").getMsg());
		    System.out.println("匹配第一个 -----");
			System.out.println(CheckPointUtils.check(testjson,"name=testfan1").getMsg());			
//			
//			//函数测试 
			testjson="{\"code\":\"1\",\"data\":[{\"name\":\"testfan0\",\"pwd\":\"pwd0\"},{\"name\":\"testfan1\",\"pwd\":\"pwd1\"},{\"name\":\"testfan2\",\"pwd\":\"pwd2\"}]}";
			System.out.println("-----函数测试 ");
		    System.out.println(CheckPointUtils.check(testjson,"data.size()>4").getMsg());
		    System.out.println("length ----");
		    System.out.println(CheckPointUtils.check(testjson,"data.length()>2").getMsg());
	   }

}
