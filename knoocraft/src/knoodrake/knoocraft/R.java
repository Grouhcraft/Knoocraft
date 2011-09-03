package knoodrake.knoocraft;

import java.io.IOException;
import java.util.HashMap;
import java.util.ListResourceBundle;
import java.util.Properties;
/**
 * @author melendil, knoodrake
 * 
 * @TODO Y'a un pb de taille avec cette classe, c'est que quand, gentillement on demande un truc à la con genre
 * une chaine de caractère mais qu'elle existe pas, ca fait tout plantouyer, exception non catché et tout.
 * Faudrait qu'il renvoi rien, ou un message, ou la clef, ou je sais pas, mais bon, une exception non gérée pour une pauvre
 * chaine manquante c'est un peu Hard je trouve.
 */
public class R {
	private static HashMap<String, Properties> properties = new HashMap<String, Properties>();
	
	
	private static Properties getProperties(String name) {
		if(!properties.containsKey(name)) {
			properties.put(name, new Properties());
				try {
					properties.get(name).load(R.class.getResourceAsStream("/res/" + name + ".properties"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return properties.get(name);
	}
	
	public static String get(String key, String bundleName) {
		String val = getProperties(bundleName).getProperty(key);
		return val;
	}

	public static int getInt(String key, String bundleName) throws NumberFormatException {
		String stringVal = getProperties(bundleName).getProperty(key);
		return Integer.parseInt(stringVal);
	}

	public static String getString(String key, String bundleName) {
		String stringVal = getProperties(bundleName).getProperty(key);
		return stringVal;
	}

	public static Object getObject(String key, String bundleName) {
		return (Object)getProperties(bundleName).getProperty(key);
	}
	
	public static boolean getBoolean(String key, String bundleName) {		
		String stringVal = getProperties(bundleName).getProperty(key);
		return Boolean.parseBoolean(stringVal);
	}
	
	/* ------------------------------------------------------------- */

	public static String get(String key) {
		String val = ListResourceBundle.getBundle("knoocraft").getString(key);
		return val;
	}

	public static int getInt(String key) throws NumberFormatException {
		String stringVal = ListResourceBundle.getBundle("knoocraft").getString(key);
		return Integer.parseInt(stringVal);
	}

	public static String getString(String key) {
		String stringVal = ListResourceBundle.getBundle("knoocraft").getString(key).toString();
		return stringVal;
	}

	public static boolean getBoolean(String key) {
		String stringVal = ListResourceBundle.getBundle("knoocraft").getString(key).toString();
		return Boolean.parseBoolean(stringVal);
	}
}
