package Utils;

import java.io.File;
import java.util.Map;

import knoodrake.knoocraft.R;

import org.bukkit.util.config.Configuration;

public class KcConfiguration {
	public String name = null;
	public Configuration config = null;
	public String path = null; 
	
	public KcConfiguration(String name, File config_file) {
		this.name = name;
		this.config = new Configuration(config_file);
		this.path = config_file.getPath();
	}

	// Valeur automatique par rapport a la valeur par defaut dans les ressources
	public void setProperty(String string) {
		config.setProperty(string, getDefaultValue(string));
	}
	
	public void save() {
		config.save();
	}
	
	// Avec valeur 
	public void setProperty(String string, Object defaultValue) {
		config.setProperty(string, defaultValue);
	}
	
	private Object getDefaultValue(String string) {
		return R.getObject(string, "configuration_default_values");
	}

	// Avec valeurs 
	public void setProperties(Map<String, String> confs) {
		for(String key: confs.keySet()) {
			config.setProperty(key, confs.get(key));
		}
	}
	
	// Valeurs par defaut automatiques
	public void setProperties( String[] confs) {
		for(String key: confs) {
			config.setProperty(key, getDefaultValue(key));
		}		
	}

	public void load() {
		config.load();
	}

	public String getString(String string) {
		return config.getString(string);
	}

	public String getString(String string, String string2) {
		return config.getString(string, string2);
	}

	public Boolean getBoolean(String string, Boolean defaultValue) {
		return config.getBoolean(string, defaultValue);
	}

	public int getInt(String string, int int1) {
		return config.getInt(string, int1);
	}	
}
