package utils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
/**
 * 将String转换成Map的工具类
 * @author zhang
 *
 */
public class StringToMapUtils {

	public static Map<String, Object> strToMpConvert(String str, String regx) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (StringUtils.isNotEmpty(str)) {
			String[] str_array = str.split(regx);
			for (int i = 0; i < str_array.length; i++) {
				String keys = str_array[i];
				String[] key_value = keys.split("=");
				if (key_value.length < 2) {
					map.put(key_value[0], "");
				} else {
					map.put(key_value[0], key_value[1]);
				}
			}
		}
		return map;
	}

	public static Map<String, Object> strToMpConvert1(String str) {
		return strToMpConvert(str, "&");
	}

	public static Map<String, Object> strToMpConvert2(String str) {
		return strToMpConvert(str, ";");
	}

	public static Map<String, Object> strToMpConvert3(String str) {
		return strToMpConvert(str, "_");
	}
}
