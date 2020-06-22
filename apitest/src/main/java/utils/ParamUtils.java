package utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONPath;

public class ParamUtils {

	// 全局map支持多列数据提取
	// static Map<String, Object> paramMps = new LinkedHashMap<String, Object>();
	static ThreadLocal<Map<String, Object>> paramMps = new ThreadLocal<Map<String, Object>>() {
		@Override
		protected Map<String, Object> initialValue() {
			return new LinkedHashMap<String, Object>();
		}
	};

	// 重置map
	public static void clear() {
		paramMps.get().clear();
	}

	// 将Excel中的参数map加入到全局的map中
	public static void addFromMap(Map<String, Object> map) {
		if (MapUtils.isNotEmpty(map)) {
			Set<String> keySet = map.keySet();
			for (String key : keySet) {
				paramMps.get().put(key, map.get(key));
			}
		}
	}

	public static void addFromJson(String json, String regx) {
		if (StringUtils.isNoneEmpty(json)) {
			Map<String, Object> map = StringToMapUtils.strToMpConvert2(regx);
			if (MapUtils.isNotEmpty(map)) {
				Set<String> keys = map.keySet();
				for (String key : keys) {
					String value = String.valueOf(map.get(key));
					if (StringUtils.isNotBlank(value)) {
						Object newValue = JSONPath.read(json, value);
						if (newValue == null) {
							newValue = JSONPath.read(json, ".." + value);
						}
						if (newValue instanceof List) {
							List<Object> list = (List<Object>) newValue;
							int count = 1;
							for (Object o : list) {
								paramMps.get().put(key + "_g" + count++, o);
							}
						} else {
							paramMps.get().put(key, newValue);
						}
					}
				}
				map.clear();
			}
		}
	}

	/**
	 * 自定义替换
	 * 
	 * @param str
	 * @param map
	 * @return
	 */
	// 匹配规则
	static String regex = "\\$\\{(.+?)\\}";
	static Pattern pattern = Pattern.compile(regex);

	public static String match(String str) {
		if (StringUtils.isNotEmpty(str)) {
			Matcher matcher = pattern.matcher(str);

			while (matcher.find()) {
				str = str.replace(matcher.group(), MapUtils.getString(paramMps.get(), matcher.group(1), ""));
			}
		}
		return str;
	}
}
