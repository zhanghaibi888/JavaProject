package exer;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.map.HashedMap;

public class RegexMatches {

/*	public static void main(String[] args) {
		//要查找的字符串
		String line = "This order was placed for QT3000! OK?";
		
		//规则1
		String pattern = "(\\D*)(\\d+)(.*)";
		
		//创建Pattern对象
		Pattern p = Pattern.compile(pattern);
		//创建matcher对象
		Matcher m = p.matcher(line);
		if (m.find()) {
			System.out.println("匹配到所有的数据：" + m.group());
		}else {
			System.out.println("No Match!");
		}
	}*/
	
	// 测试代码
	public static void test() {
		String url = "http://118.24.13.38:8080/goods/UserServlet?method=loginMobile&loginname=${loginname}&loginpass=${loginpass}";
		//String line = "http://118.24.13.38:8080/goods/UserServlet?method=loginMobile&loginname=${loginname}";

		String patternRegex = "\\$\\{(.+?)\\}";
		
		Pattern pattern = Pattern.compile(patternRegex);
		
		Matcher matcher = pattern.matcher(url);
		Map<String, Object> map = new HashedMap<>();
		map.put("loginname", "abc");
		//map.put("loginpass", "abc");
		while (matcher.find()) {
			System.out.println(matcher.group());
			System.out.println(matcher.group(1));
			url = url.replace(matcher.group(), MapUtils.getString(map, matcher.group(1),""));
		}
		System.out.println(url);
	}

	public static void main(String[] args) {
		test();
	}
}
