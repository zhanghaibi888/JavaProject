package convert;

import com.github.crab2died.converter.ReadConvertible;

public class isRunConvert implements ReadConvertible {

	@Override
	public Object execRead(String object) {
		if ("是".equals(object)) {
			return true;
		}
		return false;
	}
}
