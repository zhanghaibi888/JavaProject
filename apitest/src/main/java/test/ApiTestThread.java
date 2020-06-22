package test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.checkpoint.CheckPointUtils;
import com.github.crab2died.ExcelUtils;

import utils.DbCheckUtils;
import utils.EmailUtils;
import utils.ExcelToMapUtils;
import utils.HttpClientUtils;
import utils.ParamUtils;
import utils.StringToMapUtils;

class RunTask extends Thread {

	private Map<String, Object> map;
	private XmlMapper xmlMapper = new XmlMapper();

	public RunTask(Map<String, Object> map) {
		this.map = map;
	}

	@Override
	public void run() {
		ParamUtils.addFromMap(map);
		try {
			// 读取用例文件
			List<TestCase> list = ExcelUtils.getInstance().readExcel2Objects(ApiTestThread.path, TestCase.class);
			// 排序
			Collections.sort(list);
			for (TestCase testCase : list) {
				// 是否开启测试
				if (testCase.isRun()) {
					String result = null;
					// 参数替换
					ApiTestThread.replace(testCase);
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
					} else if ("getxml".equals(testCase.getType())) {
						result = HttpClientUtils.doGet(testCase.getUrl(),
								StringToMapUtils.strToMpConvert1(testCase.getHeaders()));
						Map map = xmlMapper.readValue(result, Map.class);
						result = JSON.toJSONString(map);
						ParamUtils.addFromJson(result, testCase.getResultXml());

					}
					// 参数替换
					ParamUtils.addFromJson(result, testCase.getResultJson());
					// 将检查结果保存在resultList
					testCase.setCheckResult(CheckPointUtils.check(result, testCase.getCheckPoint()).getMsg());
					// 数据库检查点
					String dbCheckResult = DbCheckUtils.check(testCase);
					testCase.setCheckDbResult(dbCheckResult);
					ApiTestThread.resultList.add(testCase);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ParamUtils.clear();// 重置map
	}
}

public class ApiTestThread {

	// 文件路径
	static String path = System.getProperty("user.dir") + File.separator + "data" + File.separator + "apitest9.xlsx";
	// 保存结果的List
	static List<TestCase> resultList = new ArrayList<TestCase>();
	// 日志
	private static final Logger logger = LoggerFactory.getLogger(ApiTestThread.class);

	public static void main(String[] args) {

		try {
			boolean openProxy = false;
			if (args.length > 0) {
				openProxy = Boolean.valueOf(args[0]);
			}
			// 是否开启代理
			HttpClientUtils.openProxy = openProxy;

			// 读取要覆盖的参数
			List<Map<String, Object>> listMaps = ExcelToMapUtils.importExcel(path, 1);
			// 多线程执行
			for (Map<String, Object> map : listMaps) {
				new RunTask(map).start();
			}
			// 测试结果写入excel
			long time = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
			String date = sdf.format(time);
			String monthDate = sdf2.format(time);
			String path = System.getProperty("user.dir") + File.separator + "result" + File.separator + monthDate ;
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			String resultPath = path + File.separator+ "result_" + date + ".xlsx";
			ExcelUtils.getInstance().exportObjects2Excel(resultList, TestCase.class, resultPath);
			// 发送邮件
			try {
				EmailUtils.sendEmailsWithAttachments("请查收", "接口测试", resultPath);
			} catch (EmailException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
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
