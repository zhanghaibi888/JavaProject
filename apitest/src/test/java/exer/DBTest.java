package exer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBTest {
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://118.24.13.38:3309/testfan_jiguang?characterEncoding=utf8&useSSL=false", "root", "123456");;
		
		String sql = "select * from t_user_test";
		
		Statement statement = conn.createStatement();
		
		ResultSet resultSet = statement.executeQuery(sql);
		
		while(resultSet.next()) {
			String uid = resultSet.getString(1);
			System.out.println(uid);
		}
		
		
		
	}
}
