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
		 *  Faut donc quand on instancie la classe (donc ici-même) lire ce-dit fichier
		 *  et en charger toutes les locations dans la porp privé "locations" ci-dessus.
		 *  
		 *  Le format du fichier reste donc à définir. Je pensai que le plus simple serait comme
		 *  je disai d'utiliser soit un .yml avec du coup les fonctions pour fichier de conf déjà 
		 *  toutes prêtes de bukkit (pratique !) ou bien, une base de donnée SQLite, des requêtes, ca peu
		 *  être pas mal pratique.. et puis c'est un peu plus évolutif qu'un fichier. Mais, est-ce chiant à
		 *  utiliser ? comment qu'on fait ça en java ? .. autant de mystère, et si peu de boules de gomme.
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
