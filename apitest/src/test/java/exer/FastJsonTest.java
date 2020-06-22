package exer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/**
 * FastJson用法练习
 * @author zhang
 *
 */
public class FastJsonTest {

	public static void main(String[] args) {
		//map
		String json1 = "{\"loginname\":\"abc\",\"loginpass\":\"abc\"}";
		JSONObject jsonObject = (JSONObject) JSON.parse(json1);
		System.out.println(jsonObject.get("loginname"));
		
		String json2 = "[{\"loginname\":\"abc0\",\"loginpass\":\"abc0\"},{\"loginname\":\"abc1\",\"loginpass\":\"abc1\"},{\"loginname\":\"abc2\",\"loginpass\":\"abc2\"}]";
		JSONArray jsonArray = JSON.parseArray(json2);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
			System.out.println(jsonObject2.get("loginname"));
		}
		
		String json3 = "{\"code\":\"1\",\"data\":[{\"name\":\"testfan0\",\"pwd\":\"pwd0\"},{\"name\":\"testfan1\",\"pwd\":\"pwd1\"},{\"name\":\"testfan2\",\"pwd\":\"pwd2\"},{\"name\":\"testfan3\",\"pwd\":\"pwd3\"},{\"name\":\"testfan4\",\"pwd\":\"pwd4\"},{\"name\":\"testfan5\",\"pwd\":\"pwd5\"},{\"name\":\"testfan6\",\"pwd\":\"pwd6\"},{\"name\":\"testfan7\",\"pwd\":\"pwd7\"},{\"name\":\"testfan8\",\"pwd\":\"pwd8\"},{\"name\":\"testfan9\",\"pwd\":\"pwd9\"}]}";
		JSONObject jsonObject3 = (JSONObject) JSON.parse(json3);
		JSONArray jsonArray2 = (JSONArray) jsonObject3.getJSONArray("data");
		for (int i = 0; i < jsonArray2.size(); i++) {
			JSONObject object = (JSONObject) jsonArray2.get(i);
			System.out.println(object.getString("name"));
		}
		
		//fastjson 反射处理方式
		
		
		
		//fastjsonpath
	}
}
