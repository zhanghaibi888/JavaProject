package utils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.github.checkpoint.CheckPointUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import test.TestCase;

public class DbCheckUtils {

	private static final ComboPooledDataSource defaultDataSource = new ComboPooledDataSource();
	private static final ComboPooledDataSource mysqlDataSource = new ComboPooledDataSource("mysql");
	private static final ComboPooledDataSource mysql2DataSource = new ComboPooledDataSource("mysql2");
	private static final ComboPooledDataSource oracleDataSource = new ComboPooledDataSource("oracle");

	public static String check(TestCase testCase) {
		if (StringUtils.isNotBlank(testCase.getCheckDbPoint())) {
			String dbCheck = ParamUtils.match(testCase.getCheckDbPoint());
			testCase.setCheckDbPoint(dbCheck);
			String sql = dbCheck.split(",")[0];
			String check = dbCheck.split(",")[1];
			String dbType = dbCheck.split(",")[2];
			ComboPooledDataSource ds = getDataSource(dbType);
			QueryRunner runner = new QueryRunner(ds);

			try {
				List<Map> listMps = (List) runner.query(sql, new MapListHandler());
				String jsonString = JSON.toJSONString(listMps);
				return CheckPointUtils.check(jsonString, check).getMsg();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "没有设置数据库检查点";
	}

	/**
	 * 工厂
	 */
	public static ComboPooledDataSource getDataSource(String dbType) {
		if ("mysql".equals(dbType)) {
			return mysqlDataSource;
		} else if ("mysql2".equals(dbType)) {
			return mysql2DataSource;
		} else if ("oracle".equals(dbType)) {
			return oracleDataSource;
		} else {
			return defaultDataSource;
		}
	}
}
