package knoodrake.knoocraft;

import org.bukkit.event.block.BlockListener;

/**
 * Knoocraft block listener
 * @author knoodrake 
 */
public class KnoocraftBlockListener extends BlockListener {
	@SuppressWarnings("unused")
    private final knoocraft plugin;

    public KnoocraftBlockListener(final knoocraft plugin) {
        this.plugin = plugin;
    }

    //put all Block related code here
}


