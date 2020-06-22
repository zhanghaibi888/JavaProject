package convert;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.crab2died.converter.ReadConvertible;

public class FileConvert implements ReadConvertible {
	// 文件支持
	@Override
	public Object execRead(String object) {
		if (StringUtils.endsWithIgnoreCase(object, "csv") || StringUtils.endsWithIgnoreCase(object, "txt")) {
			String dir = System.getProperty("user.dir");
			String path = dir + File.separator + "data" + File.separator + object;
			try {
				return FileUtils.readFileToString(new File(path), "utf-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return object;
	}
}
