package test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;

import com.github.crab2died.ExcelUtils;
import com.github.crab2died.annotation.ExcelField;
import com.github.crab2died.exceptions.Excel4JException;

import convert.FileConvert;
import convert.isRunConvert;
import utils.StringToMapUtils;

public class TestCase implements Comparable<TestCase> {

	@ExcelField(title = "顺序")
	private int order;

	@ExcelField(title = "是否开启", readConverter = isRunConvert.class)
	private boolean run;

	@ExcelField(title = "用例名称")
	private String caseName;

	@ExcelField(title = "类型")
	private String type;

	@ExcelField(title = "地址")
	private String url;

	@ExcelField(title = "参数", readConverter = FileConvert.class)
	private String params;

	@ExcelField(title = "头部")
	private String headers;

	@ExcelField(title = "返回结果从json提取")
	private String resultJson;

	@ExcelField(title = "返回结果校验")
	private String checkPoint;

	@ExcelField(title = "返回结果")
	private String checkResult;

	@ExcelField(title = "数据库检查")
	private String checkDbPoint;

	@ExcelField(title = "数据库检查结果")
	private String checkDbResult;

	@ExcelField(title = "返回结果从xml提取")
	private String resultXml;

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}

	public String getResultJson() {
		return resultJson;
	}

	public void setResultJson(String resultJson) {
		this.resultJson = resultJson;
	}

	public String getCheckPoint() {
		return checkPoint;
	}

	public void setCheckPoint(String checkPoint) {
		this.checkPoint = checkPoint;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getCheckDbPoint() {
		return checkDbPoint;
	}

	public void setCheckDbPoint(String checkDbPoint) {
		this.checkDbPoint = checkDbPoint;
	}

	public String getCheckDbResult() {
		return checkDbResult;
	}

	public void setCheckDbResult(String checkDbResult) {
		this.checkDbResult = checkDbResult;
	}

	public String getResultXml() {
		return resultXml;
	}

	public void setResultXml(String resultXml) {
		this.resultXml = resultXml;
	}

	public int compareTo(TestCase t) {

		return this.getOrder() - t.getOrder();
	}

	@Override
	public String toString() {
		return "TestCase [order=" + order + ", run=" + run + ", caseName=" + caseName + ", type=" + type + ", url="
				+ url + ", params=" + params + ", headers=" + headers + ", resultJson=" + resultJson + ", checkPoint="
				+ checkPoint + ", checkResult=" + checkResult + ", checkDbPoint=" + checkDbPoint + ", checkDbResult="
				+ checkDbResult + ", resultXml=" + resultXml + "]";
	}

	@Test
	//测试代码
	public void Test() {
		// 文件路径
		String dir = System.getProperty("user.dir");
		String path = dir + File.separator + "data" + File.separator + "apitest6.xlsx";

		try {
			List<TestCase> lines = ExcelUtils.getInstance().readExcel2Objects(path, TestCase.class);
			Collections.sort(lines);
			for (TestCase testCase : lines) {
				// System.out.println(testCase);
				if (StringUtils.isNoneEmpty(testCase.getResultJson())) {
					Map<String, Object> map = StringToMapUtils.strToMpConvert2(testCase.getResultJson());
					System.out.println(map);
				}
			}
		} catch (InvalidFormatException | Excel4JException | IOException e) {
			e.printStackTrace();
		}
	}
}
