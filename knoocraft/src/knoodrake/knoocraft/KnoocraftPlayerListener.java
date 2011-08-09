package knoodrake.knoocraft;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.config.Configuration;

/**
 * Handle events for all Player related events
 * @author knoodrake 
 */
public class KnoocraftPlayerListener extends PlayerListener {
	private final knoocraft plugin;
	public static boolean greenwhooling = false; 
	public static int moins_Y = 1;

	private int firstColor;
	private int secondColor;
	
    public KnoocraftPlayerListener(knoocraft instance) {
        plugin = instance;
        Math.random();    
    }
    
    @SuppressWarnings("unused")
	@Override
    public void onPlayerMove(PlayerMoveEvent event) {
    	if(KnoocraftPlayerListener.greenwhooling) {
            
    		firstColor  = plugin.getConfig().getInt("greenwhooler.firstColor", 13);
            secondColor = plugin.getConfig().getInt("greenwhooler.secondColor", 5);
            
    		Location playerFrom = event.getFrom().clone();
    		Location playerTo = event.getTo();
			playerFrom.setY( playerFrom.getBlockY() -moins_Y);
			
			int underlaying_block = playerFrom.getBlock().getTypeId(); 
			if(underlaying_block == Material.NETHERRACK.getId() || underlaying_block == Material.SOUL_SAND.getId())
			{
    			double r = Math.random(); 
    			byte whool_color = 0;
    			if(r < 0.33) 	whool_color = (byte)firstColor;
    			else			whool_color = (byte)secondColor;
    			
    			playerFrom.getBlock().setTypeIdAndData(Material.WOOL.getId(), (byte)whool_color, true);
			}
    	}
    }
}
