package exer;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DbToTxt {

	private static final String filePath = System.getProperty("user.dir") + File.separator + "data" + File.separator + "test2.txt";
	private static final String sql = "select * from test_fan_zhb";
	
	public static void exportToTxt() {
		ComboPooledDataSource ds = new ComboPooledDataSource();
		QueryRunner runner = new QueryRunner(ds);
		
		try {
			List<Map<String, Object>> list = runner.query(sql,new MapListHandler());
			File file = new File(filePath);
			for (Map<String, Object> map : list) {
				System.out.println(map.toString());
				FileUtils.write(file, JSON.toJSONString(map) + "\n", "utf-8",true);
			}
			System.out.println("导出成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		exportToTxt();
	}
}
