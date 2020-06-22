package exer;

import java.io.File;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TxtToDB {

	private static final String filePath = System.getProperty("user.dir") + File.separator + "data" + File.separator
			+ "test.txt";

	public static void addTxtToDb() {
		File file = new File(filePath);
		String sql = "insert into test_fan_zhb(id,phone,idcard,color) values (?,?,?,?)";
		try {
			ComboPooledDataSource ds = new ComboPooledDataSource();
			QueryRunner runner = new QueryRunner(ds);

			int count = 1;
			List<String> lists = FileUtils.readLines(file, "utf-8");
			for (String line : lists) {
				JSONObject jsonObject = (JSONObject) JSON.parse(line);
				Object[] object = new Object[] { count++, jsonObject.get("phone"), jsonObject.get("idcard"),
						jsonObject.get("color") };
				runner.update(sql, object);
			}
			System.out.println("数据插入成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 批量插入
	 */
	public static void addTxtToDbBatch() {
		File file = new File(filePath);
		String sql = "insert into test_fan_zhb(id,phone,idcard,color) values (?,?,?,?)";

		ComboPooledDataSource ds = new ComboPooledDataSource();
		QueryRunner runner = new QueryRunner(ds);
		int count = 0;
		try {
			List<String> lines = FileUtils.readLines(file, "utf-8");
			Object[][] params = new Object[lines.size()][4];
			for (String line : lines) {
				JSONObject jsonObject = JSON.parseObject(line);
				params[count][0] = count;
				params[count][1] = jsonObject.get("phone");
				params[count][2] = jsonObject.get("idcard");
				params[count][3] = jsonObject.get("color");
				count++;
			}
			runner.batch(sql, params);
			System.out.println("执行完成");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		addTxtToDbBatch();
	}
}
