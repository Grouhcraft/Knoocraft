package Utils;

import knoodrake.knoocraft.R;

public class KcStrings {

	static boolean return_key_if_fails = true;
	
	public static String getString(String key) {
		try {
			String s = R.getString(key);
			return s;
		} catch (Exception e) {
			return return_key_if_fails ? key : "";
		}
	}
}
