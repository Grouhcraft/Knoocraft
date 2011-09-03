package Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class KcConfigurationManager {
	private HashMap<String, String> paths = new HashMap<String, String>();
	private HashMap<String, KcConfiguration> configs = new HashMap<String, KcConfiguration>();
	
	public Boolean exists( String config_name )
	{
		return configs.containsKey(config_name); 
	}
	
	public KcConfiguration getConfig(String conf_name) throws Exception {
		if(exists(conf_name))
			return configs.get(conf_name);
		else
			throw new Exception("Configuration " + conf_name + " does not exists");
	}
	
	public void createConfig(String config_name, File file) throws Exception 
	{
		if(exists(config_name)) throw new Exception("La configuration existe déjà");
    	new File(file.getParent()).mkdirs();
    	file.createNewFile();
    	configs.put(config_name, new KcConfiguration(config_name, file));
    	paths.put(config_name, file.getAbsolutePath());
    	loadConfig(config_name);
	}
	
	private void loadConfig(String config_name) throws Exception 
	{
		if(exists(config_name)) {
			configs.get(config_name).load();
		}
		else throw new Exception("La configuration "+config_name+" n'existe pas");
	}

	public void loadAll() throws Exception {
		ArrayList<String> loading_exceptions = new ArrayList<String>();
		for(String confName: configs.keySet()) {
			try {
				loadConfig(confName);
			} catch (Exception e) {
			//	loading_exceptions.add(confName + ": " + e.getLocalizedMessage());
				throw new Exception(e.getLocalizedMessage());
			}
		}
		if(!loading_exceptions.isEmpty())
			throw new Exception(); // TODO message de l'exception => metre la liste loading_exceptions
	}
}
