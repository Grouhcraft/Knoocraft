package knoodrake.knoocraft;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Handle events for all Player related events
 * @author knoodrake 
 */
public class KnoocraftPlayerListener extends PlayerListener {
	@SuppressWarnings("unused")
    private final knoocraft plugin;
	public static boolean greenwhooling = false; 
	public static int moins_Y = 1;

    public KnoocraftPlayerListener(knoocraft instance) {
        plugin = instance;
        Math.random();
    }
    
    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
    	if(KnoocraftPlayerListener.greenwhooling) {
    		Location playerFrom = event.getFrom().clone();
    		Location playerTo = event.getTo();
			playerFrom.setY( playerFrom.getBlockY() -moins_Y);
			
			int underlaying_block = playerFrom.getBlock().getTypeId(); 
			if(underlaying_block == Material.NETHERRACK.getId() || underlaying_block == Material.SOUL_SAND.getId())
			{
    			double r = Math.random(); 
    			byte whool_color = 0;
    			if(r < 0.33) 	whool_color = (byte)13;
    			else			whool_color = (byte)5;
    			
    			playerFrom.getBlock().setTypeIdAndData(Material.WOOL.getId(), (byte)whool_color, true);
			}
    	}
    }
}
