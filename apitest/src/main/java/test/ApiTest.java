package test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.mail.EmailException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.github.checkpoint.CheckPointUtils;
import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;

import utils.DbCheckUtils;
import utils.EmailUtils;
import utils.ExcelToMapUtils;
import utils.HttpClientUtils;
import utils.ParamUtils;
import utils.StringToMapUtils;

public class ApiTest {

	// 文件路径
	static String dir = System.getProperty("user.dir");
	static String path = dir + File.separator + "data" + File.separator + "apitest6-2.xlsx";

	public static void main(String[] args) {

		try {
			// 是否开启代理
			HttpClientUtils.openProxy = true;

			// 读取要覆盖的参数
			List<Map<String, Object>> listMaps = ExcelToMapUtils.importExcel(path, 1);

			// 保存结果的List
			List<TestCase> resultList = new ArrayList<TestCase>();

			for (Map<String, Object> map : listMaps) {
				ParamUtils.addFromMap(map);
				// 读取用例文件
				List<TestCase> list = ExcelUtils.getInstance().readExcel2Objects(path, TestCase.class);
				// 排序
				Collections.sort(list);
				for (TestCase testCase : list) {
					// 是否开启测试
					if (testCase.isRun()) {
						String result = null;
						// 参数替换
						replace(testCase);
						if ("get".equals(testCase.getType())) {
							result = HttpClientUtils.doGet(testCase.getUrl(),
									StringToMapUtils.strToMpConvert1(testCase.getHeaders()));
						} else if ("post".equals(testCase.getType())) {
							result = HttpClientUtils.doPost(testCase.getUrl(),
									StringToMapUtils.strToMpConvert1(testCase.getParams()),
									StringToMapUtils.strToMpConvert2(testCase.getHeaders()));
						} else if ("postjson".equals(testCase.getType())) {
							result = HttpClientUtils.doPostJson(testCase.getUrl(), testCase.getParams(),
									StringToMapUtils.strToMpConvert2(testCase.getHeaders()));
						}
						ParamUtils.addFromJson(result, testCase.getResultJson());
						// 将检查结果保存在resultList
						testCase.setCheckResult(CheckPointUtils.check(result, testCase.getCheckPoint()).getMsg());
						// 数据库检查点
						String dbCheckResult = DbCheckUtils.check(testCase);
						testCase.setCheckDbResult(dbCheckResult);
						resultList.add(testCase);
					}
				}
				ParamUtils.clear();// 重置map
			}
			// 测试结果写入excel
			long time = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmm");
			String date = sdf.format(time);
			String resultPath = "result_" + date + ".xlsx";
			ExcelUtils.getInstance().exportObjects2Excel(resultList, TestCase.class, resultPath);
			// 发送邮件
			try {
				EmailUtils.sendEmailsWithAttachments("请查收", "接口测试", resultPath);
			} catch (EmailException e) {
				e.printStackTrace();
			}
		} catch (InvalidFormatException | Excel4JException | IOException e) {
			e.printStackTrace();
		}
	}

	// 统一替换方法
	public static void replace(TestCase testCase) {
		// Url替换
		testCase.setUrl(ParamUtils.match(testCase.getUrl()));
		// 参数替换
		testCase.setParams(ParamUtils.match(testCase.getParams()));
		// 请求头替换
		testCase.setHeaders(ParamUtils.match(testCase.getHeaders()));
		// 检查点替换
		testCase.setCheckPoint(ParamUtils.match(testCase.getCheckPoint()));
	}
}
