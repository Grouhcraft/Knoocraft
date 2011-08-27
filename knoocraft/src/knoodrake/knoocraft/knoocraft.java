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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import org.bukkit.plugin.Plugin;
import org.bukkit.util.FileUtil;
import org.bukkit.util.config.Configuration;

import java.util.logging.Logger;
/**
* KnooCraft plugin
*
* @author knoodrake
*/
public class knoocraft extends JavaPlugin {
    private final KnoocraftPlayerListener playerListener = new KnoocraftPlayerListener(this);
    private final KnoocraftBlockListener blockListener = new KnoocraftBlockListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    
    public Configuration config;
    
    public static PermissionHandler permissionHandler;
    public static Logger log = Logger.getLogger("Minecraft");

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
		if(getConfig().getBoolean("autoupdate.enabled", false)) 
		{
			log.info("Version: " + getVersion());
			try {
				URL url = new URL(getConfig().getString("autoupdate.check_url"));
				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
				String str;
				while ((str = in.readLine()) != null) {
					String[] split = str.split("\\.");
					int version = Integer.parseInt(split[0]) * 10
							+ Integer.parseInt(split[1]);
					if (version > getVersion()) {
						in.close();
						return true;
					}
				}
				in.close();
			} catch (Exception e) {
			}
			return false;
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
            File directory = new File(Bukkit.getServer().getUpdateFolder());
            if (!directory.exists()) {
                directory.mkdir();
            }
            File plugin = new File(directory.getPath(), R.getString("jar_name"));
            if (!plugin.exists()) {
                URL bukkitContrib = new URL(getConfig().getString("autoupdate.download_url"));
                HttpURLConnection con = (HttpURLConnection)(bukkitContrib.openConnection());
                System.setProperty("http.agent", ""); //Spoofing the user agent is required to track stats
                con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.100 Safari/534.30");
                ReadableByteChannel rbc = Channels.newChannel(con.getInputStream());
                FileOutputStream fos = new FileOutputStream(plugin);
                fos.getChannel().transferFrom(rbc, 0, 1 << 24);
            }
        }
        catch (Exception e) {}
    }

    private int getVersion() {
    	return R.getInt("version.major") * 10
    			+ R.getInt("version.minor");
	}

	public knoocraft() throws IOException {
    	super();
    	new File("plugins" + File.separator + "Knoocraft" + File.separator).mkdirs();
    	if(new File("plugins" + File.separator + "Knoocraft" + File.separator).exists()) {
    		boolean newfile = new File("plugins" + File.separator + "Knoocraft" + File.separator + "config.yml").createNewFile();
    		if(new File("plugins" + File.separator + "Knoocraft" + File.separator + "config.yml").exists()) 
    		{
    			this.config = new Configuration(new File("plugins" + File.separator + "Knoocraft" + File.separator + "config.yml"));
    			if(newfile)
    				createDefaultConfig();
    		}
    	}
    }

    public Configuration getConfig() {
		return config;
	}

	private void createDefaultConfig() {
		getConfig().setProperty("greenwhooler.firstColor", R.getInt("greenwhooler.firstColor"));
        getConfig().setProperty("greenwhooler.secondColor", R.getInt("greenwhooler.secondColor"));
        getConfig().setProperty("sanitizehell.default_range", R.getInt("sanitizehell.default_range"));
        getConfig().setProperty("sanitizehell.replacedType", Material.FIRE.name().toLowerCase());
        getConfig().setProperty("sanitizehell.replaceByType", Material.GLOWSTONE.name().toLowerCase());
        getConfig().setProperty("penis.default_size", R.getInt("penis.default_size"));
        getConfig().setProperty("penis.max_size", R.getInt("penis.max_size"));
        getConfig().setProperty("penis.dist_user", R.getInt("penis.dist_user"));
        getConfig().setProperty("autoupdate.enabled", R.getBoolean("autoupdate.enabled"));
        getConfig().setProperty("autoupdate.check_url", R.getString("autoupdate.check_url"));
        getConfig().setProperty("autoupdate.download_url", R.getString("autoupdate.download_url"));
        getConfig().save();
	}

	public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Low, this);
        pm.registerEvent(Event.Type.BLOCK_PHYSICS, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_CANBUILD, blockListener, Priority.Normal, this);
        
        getCommand("kc").setExecutor(new KcCommand(this));
        setupPermissions();

        PluginDescriptionFile pdfFile = this.getDescription();
        log.info( "[KnooCraft] " + pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
    
    private void setupPermissions() {
        if (permissionHandler != null) {
            return;
        }
        
        Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
        
        if (permissionsPlugin == null) {
        	System.out.println("[KnooCraft] Permission system not detected, defaulting to OP");
            return;
        } 
        
        permissionHandler = ((Permissions) permissionsPlugin).getHandler();
        System.out.println("[KnooCraft] Found and will use plugin "+((Permissions)permissionsPlugin).getDescription().getFullName());
    }

    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }

    //TODO: Utiliser un fichier de config.. 
	public String get_messages(String string) {
		HashMap<String, String> messages = new HashMap<String, String>();
		messages.put("KcCommandInterpreter.invalid_command_name", "<red/>Commande \"%s\" non reconnue.. !");
	
		
		return messages.get(string);
	}
}


