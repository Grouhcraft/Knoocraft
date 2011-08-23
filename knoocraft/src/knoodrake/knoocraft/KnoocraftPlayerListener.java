package knoodrake.knoocraft;

import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Handle events for all Player related events
 * @author knoodrake 
 */
public class KnoocraftPlayerListener extends PlayerListener {
	private final knoocraft plugin;
	public static boolean greenwhooling = false; 
	public static int moins_Y = 1;
	
	public static boolean isMusicman() {
		return musicman;
	}

	public static void setMusicman(boolean musicman, Location newLocation) {
		KnoocraftPlayerListener.musicman = musicman;
		KnoocraftPlayerListener.musicman_loc = newLocation;
		KnoocraftPlayerListener.tone_index = 3;
		KnoocraftPlayerListener.instrument_index = 2;
		KnoocraftPlayerListener.octave = 1;
	}

	private static boolean musicman = false;
	private static Location musicman_loc = null;
	private static byte octave = 1;
	private static int tone_index = 3;
	private static int instrument_index = 2;
	
	private static int max_octave = 2;
	private static int min_octave = 0;
	

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
    	//					Greenwhooler
    	/* ------------------------------------------------------ */
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
    	/* ------------------------------------------------------ */
    	//					MusicMan
    	/* ------------------------------------------------------ */
    	else if (KnoocraftPlayerListener.musicman ) {
    		Location from	= event.getFrom();
    		Location to		= event.getTo();
    		int fx = from.getBlockX();
    		int fz = from.getBlockZ();
    		int fy = from.getBlockY();
    		int tx = to.getBlockX();
    		int tz = to.getBlockZ();
    		int ty = to.getBlockY();
    		
    		if(fx == tx && fz == tz) return;
    		
    		Player player = event.getPlayer();
    		Note.Tone tones[] = {
    			Note.Tone.A,
    			Note.Tone.B,
    			Note.Tone.C,
    			Note.Tone.D,
    			Note.Tone.E,
    			Note.Tone.F,
    			Note.Tone.G
    		};
    		Instrument instruments[] = Instrument.values();
    		
    		// Tonalité
    		if(tx > fx) // Avance
    		{
    			if(tone_index < tones.length-1) {
    				tone_index++;
    			} else {
					tone_index = 0;
    				if(octave < max_octave) octave++;
    				else octave = (byte) 	min_octave;
    			}    			
    		}
    		else if( tx < fx ) // Recule
    		{
    			if(tone_index > 0) {
    				tone_index--;
    			} else {
					tone_index = tones.length -1;
    				if(octave > min_octave) octave--;
    				else octave = (byte) 	min_octave;
    			}    			    			
    		}
    		
    		if(fy < ty) 
    			player.getServer().broadcastMessage((new KcMessaging()).format("<gray/>*claps*<pink/> J'ump! <gray/>*claps*"));
    		
    		// Instrument
    		if(tz > fz) 
    		{
    			if(instrument_index < instruments.length-1)
    				instrument_index++;
    			else instrument_index--;
    		} 
    		else if ( tz < fz ) 
    		{	
    			if(instrument_index > 0)
    				instrument_index--;
    			else instrument_index++;
    		}
    		
    		Location loc = player.getLocation();
    		loc.setY(loc.getBlockY() -1);
    		
    		Note note = new Note(octave, tones[tone_index], false);
    		player.playNote(loc, instruments[instrument_index], note);
    	}
    }

	public static Location getMusicman_loc() {
		return musicman_loc;
	}

	public static void setMusicman_loc(Location musicman_loc) {
		KnoocraftPlayerListener.musicman_loc = musicman_loc;
	}
}
