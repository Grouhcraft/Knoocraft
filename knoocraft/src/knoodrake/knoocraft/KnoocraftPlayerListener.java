package knoodrake.knoocraft;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Handle events for all Player related events
 * 
 * @author knoodrake
 */
public class KnoocraftPlayerListener extends PlayerListener {
	private final knoocraft plugin;
	public static boolean greenwooling = false;
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
		/* ------------------------------------------------------ */
		// Greenwooler
		/* ------------------------------------------------------ */
		if (KnoocraftPlayerListener.greenwooling) {

			firstColor = plugin.getConfig("main").getInt("greenwooler.firstColor", 	R.getInt("greenwooler.firstColor"));
			secondColor = plugin.getConfig("main").getInt("greenwooler.secondColor", R.getInt("greenwooler.secondColor"));

			Location playerFrom = event.getFrom().clone();
			Location playerTo = event.getTo();
			playerFrom.setY(playerFrom.getBlockY() - moins_Y);

			int underlaying_block = playerFrom.getBlock().getTypeId();
			if (underlaying_block == Material.NETHERRACK.getId()
					|| underlaying_block == Material.SOUL_SAND.getId()) {
				double r = Math.random();
				byte wool_color = 0;
				if (r < 0.33)
					wool_color = (byte) firstColor;
				else
					wool_color = (byte) secondColor;

				playerFrom.getBlock().setTypeIdAndData(Material.WOOL.getId(),
						(byte) wool_color, true);
			}
		}
	}
}
