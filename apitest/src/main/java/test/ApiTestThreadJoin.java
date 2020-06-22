package test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.github.checkpoint.CheckPointUtils;
import com.github.crab2died.ExcelUtils;

import utils.DbCheckUtils;
import utils.ExcelToMapUtils;
import utils.HttpClientUtils;
import utils.ParamUtils;
import utils.StringToMapUtils;

class RunTask2 extends Thread {

	private Map<String, Object> map;

	public RunTask2(Map<String, Object> map) {
		this.map = map;
	}

	@Override
	public void run() {
		ParamUtils.addFromMap(map);
		try {
			// 读取用例文件
			List<TestCase> list = ExcelUtils.getInstance().readExcel2Objects(ApiTestThreadJoin.path, TestCase.class);
			// 排序
			Collections.sort(list);
			for (TestCase testCase : list) {
				// 是否开启测试
				if (testCase.isRun()) {
					String result = null;
					// 参数替换
					ApiTestThreadJoin.replace(testCase);
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
					ApiTestThreadJoin.resultList.add(testCase);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ParamUtils.clear();// 重置map
	}
}

public class ApiTestThreadJoin {

	// 文件路径
	static String path = System.getProperty("user.dir") + File.separator + "data" + File.separator + "apitest6-2.xlsx";
	// 保存结果的List
	static List<TestCase> resultList = new ArrayList<TestCase>();

	public static void main(String[] args) {

		try {
			// 是否开启代理
			HttpClientUtils.openProxy = true;

			// 读取要覆盖的参数
			List<Map<String, Object>> listMaps = ExcelToMapUtils.importExcel(path, 1);
			List<Thread> list = new ArrayList<Thread>();
			for (Map<String, Object> map : listMaps) {
				RunTask2 runTask2 = new RunTask2(map);
				list.add(runTask2);
				runTask2.start();
			}
			try {
				Thread.sleep(10000);

			} catch (Exception e) {
				// TODO: handle exception
			}
			for (Thread thread : list) {
				// 所有执行完成
				thread.join();
			}
			// 测试结果写入excel
			long time = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmm");
			String date = sdf.format(time);
			ExcelUtils.getInstance().exportObjects2Excel(resultList, TestCase.class, "result_" + date + ".xlsx");
		} catch (Exception e) {
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
