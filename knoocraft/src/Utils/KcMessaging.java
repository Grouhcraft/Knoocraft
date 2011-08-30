package Utils; 

import java.util.HashMap;

public class KcMessaging {
	protected HashMap<String, String> colors = new HashMap<String, String>();

	public KcMessaging (){
		this.colors.put("black", "&0");
		this.colors.put("darkblue", "&1");
		this.colors.put("darkgreen", "&2");
		this.colors.put("darkteal", "&3");
		this.colors.put("darkred", "&4");
		this.colors.put("purple", "&5");
		this.colors.put("gold", "&6");
		this.colors.put("gray", "&7");
		this.colors.put("blue", "&8");
		this.colors.put("brightgreen","&a");
		this.colors.put("teal", "&b");
		this.colors.put("red", "&c");
		this.colors.put("pink", "&d");
		this.colors.put("yellow", "&e");
		this.colors.put("white", "&f");
	}
	
	public String format( String str ){
		for (String key : this.colors.keySet()) {
			str = str.replace("<" + key + "/>", this.colors.get(key));
		}
	    str = str.replaceAll("(&([a-z0-9]))", "\u00A7$2").replace("&&", "&");
		return str;
	}
}
