package knoodrake.knoocraft;

import org.bukkit.Location;

public class KcTeleportation {

	private KcLocation[] locations = null;
	
	public KcTeleportation ()
	{
		loadLocations();
	}
	
	private boolean loadLocations()
	{
		/**
		 * TODO  Il faut un fichier de conf (genre "locations.yml") dans lequels on peu 
		 * tout stocker, ou bien une BDD SQLite ou autre, on s'en fout, tant que c'est un fichier.
		 * 
		 *  Faut donc quand on instancie la classe (donc ici-m�me) lire ce-dit fichier
		 *  et en charger toutes les locations dans la porp priv� "locations" ci-dessus.
		 *  
		 *  Le format du fichier reste donc � d�finir. Je pensai que le plus simple serait comme
		 *  je disai d'utiliser soit un .yml avec du coup les fonctions pour fichier de conf d�j� 
		 *  toutes pr�tes de bukkit (pratique !) ou bien, une base de donn�e SQLite, des requ�tes, ca peu
		 *  �tre pas mal pratique.. et puis c'est un peu plus �volutif qu'un fichier. Mais, est-ce chiant �
		 *  utiliser ? comment qu'on fait �a en java ? .. autant de myst�re, et si peu de boules de gomme.
		 */
		return false;
	}
	
	public KcLocation addLocation(Location bukkitLoc)
	{
		//TODO a faire
		return new KcLocation();
	}
	
	public KcLocation addLocation(KcLocation kcLoc)
	{
		//TODO a faire
		return new KcLocation();		
	}
	
	public KcLocation addLocation(int x, int y, int z)
	{
		//TODO a faire
		return new KcLocation();	
	}
}
