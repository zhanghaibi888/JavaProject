package exer;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class IdCardTest {

	private static String filePath = System.getProperty("user.dir") + File.separator + "data" + File.separator
			+ "test.txt";

	/**
	 * JsonObject
	 */
	public static void countByJsonObject() {
		// 文件目录
		File file = new File(filePath);
		int count = 0;
		try {
			List<String> lines = FileUtils.readLines(file, "utf-8");
			for (String line : lines) {
				JSONObject jsonObject = (JSONObject) JSON.parse(line);
				// System.out.println(jsonObject);
				String idCard = jsonObject.getString("idcard");
				if (StringUtils.isNoneBlank(idCard)) {
					count++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("countByJsonObject统计的idcart非空结果：" + count);
	}

	/**
	 * JsonPath
	 */
	public static void countByJsonPath() {
		// 文件目录
		File file = new File(filePath);
		int count = 0;
		try {
			List<String> lines = FileUtils.readLines(file, "utf-8");
			for (String line : lines) {
				String str = String.valueOf(JSONPath.read(line, "idcard"));
				if (StringUtils.isNoneBlank(str)) {
					count++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("countByJsonPath统计的idcard非空结果：" + count);
	}

	/**
	 * 正则
	 */
	public static void countByPatten() {
		// 文件目录
		File file = new File(filePath);
		int count = 0;
		try {
			String regex = "\"idcard\":\"(.+?)\",";
			Pattern pattern = Pattern.compile(regex);
			String str = FileUtils.readFileToString(file, "utf-8");
			Matcher matcher = pattern.matcher(str);
			while (matcher.find()) {
				if (StringUtils.isNotBlank(matcher.group(1))) {
					count++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("countByPatten统计的idcard非空结果：" + count);
	}
	
	public static void CountByDB() {
		
		//数据库连接池
		ComboPooledDataSource ds = new ComboPooledDataSource();
		QueryRunner runner = new QueryRunner(ds);
		String sql = "select * from fan_test where idcard != ''";
		
		try {
			List<Map> list = (List)runner.query(sql, new MapListHandler());
			System.out.println(list.size());
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			ds.close();
		}
	}

	public static void main(String[] args) {
		CountByDB();
	}
}
