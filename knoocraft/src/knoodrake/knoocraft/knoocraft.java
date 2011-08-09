package knoodrake.knoocraft;

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import org.bukkit.plugin.Plugin;
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
    
    public static PermissionHandler permissionHandler;
    public static Logger log = Logger.getLogger("Minecraft");

    public void onDisable() {
        //
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


