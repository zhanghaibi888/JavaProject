package exer;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.alibaba.fastjson.JSON;
import com.github.checkpoint.CheckPointUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBCheckTest {

	public static void main(String[] args) {
		String dbCheck = "select * from t_user_test,uid=4D4FAB150B7451407198111&&size>0,mysql";
		ComboPooledDataSource ds = new ComboPooledDataSource();
		QueryRunner runner = new QueryRunner(ds);
		String sql = dbCheck.split(",")[0];
		String check = dbCheck.split(",")[1];
		
		try {
			List<Map> listMps = (List)runner.query(sql,new MapListHandler());
			String jsonString = JSON.toJSONString(listMps);
			System.out.println(CheckPointUtils.check(jsonString,check).getMsg());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
