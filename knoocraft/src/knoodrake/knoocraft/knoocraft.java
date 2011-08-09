package knoodrake.knoocraft;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.sun.org.apache.bcel.internal.generic.NEW;

import org.bukkit.plugin.Plugin;
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
        //
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
		getConfig().setProperty("greenwhooler.firstColor", 13);
        getConfig().setProperty("greenwhooler.secondColor", 5);
        getConfig().setProperty("sanitizehell.default_range", 15);
        getConfig().setProperty("sanitizehell.replacedType", Material.FIRE.name().toLowerCase());
        getConfig().setProperty("sanitizehell.replaceByType", Material.GLOWSTONE.name().toLowerCase());
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


