package test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;
/**
 * 测试读取Excel文件的代码
 * @author zhang
 *
 */
public class MyTest {

	static String dir = System.getProperty("user.dir");
	static String path = dir + File.separator + "data" + File.separator + "apitest4.xlsx";

	public static void main(String[] args) {
		List<TestCase> list;
		try {
			list = ExcelUtils.getInstance().readExcel2Objects(path, TestCase.class);
			Collections.sort(list);
			for (TestCase testCase : list) {
				System.out.println(testCase);
			}
		} catch (InvalidFormatException | Excel4JException | IOException e) {
			e.printStackTrace();
		}
	}
}
