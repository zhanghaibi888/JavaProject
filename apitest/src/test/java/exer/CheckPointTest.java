package exer;

import com.github.checkpoint.CheckPointUtils;

/**
 * CheckPoint练习
 * @author zhang
 *
 */
public class CheckPointTest {

	public static void main(String[] args) {
		String testjson ="{\"uid\":\"test\"}";
	    CheckPointUtils.openLog=false;
		System.out.println("-----json校验");
		System.out.println(CheckPointUtils.check(testjson,"uid=test").getMsg());
	}
}
