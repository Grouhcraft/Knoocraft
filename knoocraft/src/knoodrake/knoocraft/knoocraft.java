package knoodrake.knoocraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import org.bukkit.util.FileUtil;
import Utils.KcConfiguration;
import Utils.KcConfigurationManager;

import java.util.logging.Logger;
/**
* KnooCraft plugin
*
* @author knoodrake
*/
public class knoocraft extends JavaPlugin {
    public static Logger log = Logger.getLogger("Minecraft");
    private final KnoocraftBlockListener blockListener = new KnoocraftBlockListener(this);
    public KcConfigurationManager confManager;
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
	private KcCommand kcCommand;
	
    public void onDisable() {
		// Try to auto update, inspired from Narrowtux GPL's code
		// https://github.com/narrowtux/NarrowtuxLib/blob/master/src/com/narrowtux/Main/NarrowtuxLib.java
        try {
            File directory = new File(Bukkit.getServer().getUpdateFolder());
            if (directory.exists()) {
                File plugin = new File(directory.getPath(), R.getString("jar_name"));
                if (plugin.exists()) {
                    FileUtil.copy(plugin, this.getFile());
                    plugin.delete();
                }
            }
        }
        catch (Exception e) {}
    }

    
	protected boolean isUpdateAvailable() {
		// Try to auto update, inspired from Narrowtux GPL's code
		// https://github.com/narrowtux/NarrowtuxLib/blob/master/src/com/narrowtux/Main/NarrowtuxLib.java
		if(getConfig("main").getBoolean("autoupdate.enabled", false))
		{
			log.info( "[KnooCraft] Looking for updates..");
			log.info( "[KnooCraft] Current version: " + getVersion());
			try {
				URL url = new URL(getConfig("main").getString("autoupdate.check_url", ""));
				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
				String str;
				while ((str = in.readLine()) != null) {
					String[] split = str.split("\\.");
					int version = Integer.parseInt(split[0]) * 10
							+ Integer.parseInt(split[1]);
					if (version > getVersion()) {
						in.close();
						log.info( "[KnooCraft] a new version is avaible and will be downloaded: " + String.valueOf(version));
						return true;
					}
				}
				in.close();
			} catch (Exception e) {
				log.info( "[KnooCraft] Error while trying to check new versions..:\"" + e.getMessage() +"\"");
			}
			return false;
		}
		else {
			log.info( "[KnooCraft] automatic updates disabled.");
		}
		return false;
	}
	
	protected void update() {
		// Try to auto update, inspired from Narrowtux GPL's code
		// https://github.com/narrowtux/NarrowtuxLib/blob/master/src/com/narrowtux/Main/NarrowtuxLib.java
        if (!isUpdateAvailable()) {
            return;
        }
        try {
        	log.info( "[KnooCraft] downloading and installing new KnooCraft version");
            File directory = new File(Bukkit.getServer().getUpdateFolder());
            if (!directory.exists()) {
            	log.info( "[KnooCraft] creating update directory \"" + directory.getName() + "\"");
                directory.mkdir();
            }
            File plugin = new File(directory.getPath(), R.getString("jar_name"));
            if (!plugin.exists()) {
            	log.info( "[KnooCraft] downloading from \""+ getConfig("main").getString("autoupdate.download_url") +"\"");
                URL bukkitContrib = new URL(getConfig("main").getString("autoupdate.download_url"));
                HttpURLConnection con = (HttpURLConnection)(bukkitContrib.openConnection());
                System.setProperty("http.agent", ""); //Spoofing the user agent is required to track stats
                con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.100 Safari/534.30");
                ReadableByteChannel rbc = Channels.newChannel(con.getInputStream());
                FileOutputStream fos = new FileOutputStream(plugin);
                fos.getChannel().transferFrom(rbc, 0, 1 << 24);
            }
        }
        catch (Exception e) {
        	log.info( "[KnooCraft] Error while auto-updating: \"" + e.getMessage() + "\"");
        }
    }

    private int getVersion() {
    	String[] split = getDescription().getVersion().split("\\.");
		int version = Integer.parseInt(split[0]) * 10
				+ Integer.parseInt(split[1]);
		return version;
	}


    private final KnoocraftPlayerListener playerListener = new KnoocraftPlayerListener(this);
    public knoocraft() throws IOException {
    	super();
    	if(!confManager.exists("main")) {
    		try {
    	    	String main_conf_path = "plugins" + File.separator + "Knoocraft" + File.separator + "config.yml";
    			confManager.createConfig("main", new File(main_conf_path));
    			createMainDefaultConfig();
    		} catch (Exception e) {
				// TODO: handle exception
			}
    	}
    	try {
    		confManager.loadAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void createMainDefaultConfig() {
    	getConfig("main").setProperties(new String[]{
    			"greenwooler.firstColor",
    			"greenwooler.secondColor",
    			"sanitizehell.default_range",
    			"sanitizehell.replacedType",
    			"sanitizehell.replaceByType",
    			"penis.default_size",
    			"penis.max_size",
    			"penis.dist_user",
    			"autoupdate.enabled",
    			"autoupdate.check_url",
    			"autoupdate.download_url",
    			"mail.smtp_server",
    			"mail.template.subject",
    			"mail.template.debug_infos",
    			"mail.template.useAFileForBody",
    			"mail.template.file_path",
    			"mail.template.body"
    		});
        
        getConfig("main").save();
	}
    
    //TODO: Utiliser un fichier de config.. 
    // @melendil: HEY, je comprend pas.. ca sert à quoi ça ?..
    // comment tu compte trouver "string" dans "messages" alors que 
    // tu créé "messages" 2 lignes plus haut et vide... ?..
	public String get_messages(String string) {
		HashMap<String, String> messages = new HashMap<String, String>();
		messages.put("KcCommandInterpreter.invalid_command_name", "<red/>Commande \"%s\" non reconnue.. !");
	
		return messages.get(string);
	}

    public KcConfiguration getConfig(String confName) {
		try {
			return confManager.getConfig(confName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public KcCommand getKcCommand() {
		return kcCommand;
	}

	public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        
        (new Thread() {
            public void run() {
                update();
            }
        }).start();
        
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Low, this);
        pm.registerEvent(Event.Type.BLOCK_PHYSICS, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_CANBUILD, blockListener, Priority.Normal, this);
        
        this.kcCommand = new KcCommand(this);
        getCommand("kc").setExecutor(this.kcCommand);
        setupPermissions();

        PluginDescriptionFile pdfFile = this.getDescription();
        log.info( "[KnooCraft] " + pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }

    private void setupPermissions() 
    {
    	PluginManager pm = getServer().getPluginManager();
    	pm.addPermission(new Permission("knoocraft.commands.eyetp", 		PermissionDefault.OP));
    	pm.addPermission(new Permission("knoocraft.commands.give", 			PermissionDefault.OP));
    	pm.addPermission(new Permission("knoocraft.commands.sanitizehell", 	PermissionDefault.OP));
    	pm.addPermission(new Permission("knoocraft.commands.greenwooler", 	PermissionDefault.OP));
    	pm.addPermission(new Permission("knoocraft.commands.penis", 		PermissionDefault.OP));
    	pm.addPermission(new Permission("knoocraft.commands.orient", 		PermissionDefault.TRUE));
    	pm.addPermission(new Permission("knoocraft.commands.listalias", 	PermissionDefault.TRUE));
    	
    	// Toutes les commmandes
    	Map<String, Boolean> childrens = new HashMap<String, Boolean>();
    	childrens.put("knoocraft.commands.eyetp", true);
    	childrens.put("knoocraft.commands.give", true);
    	childrens.put("knoocraft.commands.sanitizehell", true);
    	childrens.put("knoocraft.commands.greenwooler", true);
    	childrens.put("knoocraft.commands.penis", true);
    	childrens.put("knoocraft.commands.orient", true);
    	childrens.put("knoocraft.commands.listalias", true);
    	pm.addPermission(new Permission("knoocraft.commands.all"
    			, "all knoocraft commands"
    			, PermissionDefault.FALSE
    			, childrens));
    	
    	// Tout le plugin
    	Map<String, Boolean> child = new HashMap<String, Boolean>();
    	child.put("knoocraft.commands.all", true);
    	pm.addPermission(new Permission("knoocraft.all"
    			, "all knoocraft commands"
    			, PermissionDefault.FALSE
    			, child ));
    }
}


