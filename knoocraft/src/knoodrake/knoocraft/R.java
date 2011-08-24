package knoodrake.knoocraft;

import java.util.ListResourceBundle;

public class R {

	public static String get(String key) {
		String val = ListResourceBundle.getBundle("knoocraft").getString(key);
		return val;
	}

	public static int getInt(String key) throws NumberFormatException {
		String stringVal = ListResourceBundle.getBundle("knoocraft").getString(
				key);
		return Integer.parseInt(stringVal);
	}
}
