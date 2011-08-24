package knoodrake.knoocraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import knoodrake.knoocraft.KcMessaging;

public class KcCommand implements CommandExecutor {
	private final knoocraft plugin;
	private KcMessaging msg = new KcMessaging();
	private String[] args = {};
	private Player player = null;
	private CommandSender sender = null;
	private HashMap<String, ArrayList<String>> aliases = new HashMap<String, ArrayList<String>>();

	public KcCommand(knoocraft plugin) {
        this.plugin = plugin;
    } 
	
	private boolean checkPerm(String node){
		boolean x = ((this.plugin).permissionHandler.has(player, node));
		if(!x) this.plugin.log.log(Level.WARNING ,msg.format("<red/> checkPerm: fails"));
		return x;
	}
	
	private boolean checkIsPlayer() {
		boolean x = sender.getClass().getName().equals("Player");
		if(!x) this.plugin.log.log(Level.WARNING ,msg.format("<red/> checkIsPlayer: fails"));
		return x;
	}
	
	private boolean checkOp() {
		boolean x = player.isOp();
		if(!x) this.plugin.log.log(Level.WARNING ,msg.format("<red/> checkOp: fails"));
		return x;
	}
	
	/**
	 * Retourne un sous-paramètre de la commande.
	 * Exemple pour <i>/kc getwhool white 64</i>
	 * getArg(1) donnera <i>white</i>, et getArg(2) <i>64</i> 
	 * @param num paramètre demandé (sa position)
	 * @return la valeur du paramètre (le paramètre quoi)
	 * @throws ArrayIndexOutOfBoundsException si on demande un param inexistant
	 */
	private String getArg(int num) throws ArrayIndexOutOfBoundsException
	{
		int index = num + 1;
		if(args.length >= index)
			return args[index];
		else throw new ArrayIndexOutOfBoundsException();
	}
	
	/**
	 * Donne des blocs de laine dans la couleur et la quantité souhaité.
	 * Remarque: les blocs arrivent directement dans l'inventaire. 
	 * Excpetion non gérée si plus de place..
	 * @return vrai si Ok.
	 */
	private boolean cmdGetwhool() {
		HashMap<String, Short> colors = new HashMap<String, Short>();
		colors.put("white", (short)0);
		colors.put("orange", (short)1);
		colors.put("magenta", (short)2);
		colors.put("lightblue", (short)3);
		colors.put("yellow", (short)4);
		colors.put("lightgreen", (short)5);
		colors.put("pink", (short)6);
		colors.put("gray", (short)7);
		colors.put("lightgray", (short)8);
		colors.put("cyan", (short)9);
		colors.put("purple", (short)10);
		colors.put("blue", (short)11);
		colors.put("brown", (short)12);
		colors.put("darkgreen", (short)13);
		colors.put("red", (short)14);
		colors.put("black", (short)15);
		
		if(getArg(0).equalsIgnoreCase("list"))
		{
			say("====== colors: ======");
			say("  *<white/> white ");
			say("  *<gold/> orange ");
			say("  *<red/> magenta ");
			say("  *<blue/> lightblue ");
			say("  *<yellow/> yellow ");
			say("  *<brightgreen/> lightgreen ");
			say("  *<pink/> pink ");
			say("  *<gray/> gray ");
			say("  *<gray/> lightgray ");
			say("  *<teal/> cyan ");
			say("  *<purple/> purple ");
			say("  *<darkblue/> blue ");
			say("  *<red/> brown ");
			say("  *<darkgreen/> darkgreen ");
			say("  *<darkred/> red ");
			say("  *<black/> black ");
		}
		else 
		{	
			int amount = 1;
			String color = "";
			try {
				amount = Integer.parseInt(getArg(0));
				color = getArg(1);
			} catch (NumberFormatException e) {
				try {
					amount = Integer.parseInt(getArg(1));
					color = getArg(0);					
				} catch (NumberFormatException e2) {
					args = new String[]{};
					return cmdHelp();
				}
			}
			
			if(colors.containsKey(color)) {
				Inventory inventory = player.getInventory();
				ItemStack coloredWhool = new ItemStack(Material.WOOL, amount, colors.get(color.toLowerCase()));
				inventory.addItem(coloredWhool);
			} else {
				say("<red/> Couleur inconnue: " + color);
				say("Tappez <gold/>\"/kc getwhool list\"<white/> pour une liste des couleurs possibles.");
				return false;
			}
		}
		return true;
	}
	
	public boolean cmdPenis() 
	{
		int size = plugin.getConfig().getInt("penis.default_size", 3);
		if(countCmdArgs() >= 1) 
			size = Integer.parseInt(getArg(0));
		if (size > 30) {
			say("<pink/>"+ size + "??? Mais quelle idée de vouloir faire une bite aussi grosse !!! Elle ne fera que 30, et c'est déjà pas mal.");
			size = 30;
		}
		say("<pink/>Bite de taille " + size);
		Location ploc = player.getLocation();
		World world = ploc.getWorld();
		int air = Material.AIR.getId();
		
		int ray;
		if (size%2==1) {
			ray = (3 * size - 1) / 2;
		}
		else {
			 ray = 3 * (size/2) - 1;
		}
		
		// Min et max horizontal 1ere coordonnée
		int minX = ploc.getBlockX() - ray;
		int maxX = ploc.getBlockX() + ray;
		// 2nde coordonnée horizontale : constante car plat
		int z = ploc.getBlockZ();
		// Min et max vertical
		int minY = ploc.getBlockY() + 2;
		int maxY = ploc.getBlockY() + 4 * size + 1;
				
		// Autres points de repere horizontaux
		int cou1X = minX + size - 1;
		int cou2X = maxX - size + 1;
		
		// Autres points de repere verticaux
		int maxcouY = minY + size - 1;
		int minglaY = maxcouY + 2 * size + 1;
		
		boolean libre = true;
		
		for (int y=minY; y<=maxcouY; y++) {
			for (int x=minX; x<=cou1X; x++) {
				if (world.getBlockTypeIdAt(x,y,z)!=air) {
					libre = false;
				}	
			}
			for (int x=cou2X; x<=maxY; x++) {
				if (world.getBlockTypeIdAt(x,y,z)!=air) {
					libre = false;
				}	
			}
		}
		for (int y = maxcouY + 1; y<=maxY; y++) {
			for (int x=cou1X + 1; x<cou2X; x++) {
				if (world.getBlockTypeIdAt(x,y,z)!=air) {
					libre = false;
				}	
			}
		}
		
		if (!libre) {
			say("<pink/>Il n'y a pas la place suffisante pour un tel engin ici. Veuillez remonter votre braguette.");
		}
		else {
			for (int y=minY; y<=maxcouY; y++) {
				for (int x=minX; x<=cou1X; x++) {
					world.getBlockAt(x, y, z).setType(Material.DIRT);
				}
				for (int x=cou2X; x<=maxX; x++) {
					world.getBlockAt(x, y, z).setType(Material.DIRT);
				}
			}
			for (int x=cou1X + 1; x<cou2X; x++) {
				for (int y = maxcouY + 1; y<minglaY; y++) {
					world.getBlockAt(x, y, z).setType(Material.WOOL);
					world.getBlockAt(x, y, z).setData((byte)6);
				}
				for (int y = minglaY; y<=maxY; y++) {
					world.getBlockAt(x, y, z).setType(Material.NETHERRACK);
				}
			}
		}
		
		return true;
		
	}
	
	/**
	 * Remplace certains types de blocs par d'autre, choisis
	 * dans les fichiers de conf.
	 * @return un booléen
	 */
	public boolean cmdSanitizeHell() 
	{
		int range = plugin.getConfig().getInt("sanitizehell.default_range", 15);
		if(countCmdArgs() >= 1) 
			range = Integer.parseInt(getArg(0));
		
		say("<gray/>[ === <gold/>KnooCraft <gray/>==== ]");
		say("<pink/>Sanitize fire of Hell in a range of " + range);
		Location ploc = player.getLocation();
		World world = ploc.getWorld();
		int minX = ploc.getBlockX() - range;
		int maxX = ploc.getBlockX() + range;
		int minZ = ploc.getBlockZ() - range;
		int maxZ = ploc.getBlockZ() + range;
		int minY = ploc.getBlockY() - range;
		int maxY = ploc.getBlockY() + range;
		
		Material replacedType  = Material.getMaterial(plugin.getConfig().getString("sanitizehell.replacedType", Material.FIRE.name()).toUpperCase());
		Material replaceByType = Material.getMaterial(plugin.getConfig().getString("sanitizehell.replaceByType", Material.GLOWSTONE.name()).toUpperCase());
		
		int count_changes = 0;
		for(int y=minY; y<maxY; y++){
			for(int x=minX; x<maxX; x++){
				for(int z=minZ; z<maxZ; z++){
					Block b = world.getBlockAt(x, y, z);
					if(b.getType() == replacedType) {
						b.setType(replaceByType);
						count_changes++;
					}
				}
			}
		}
		say("<pink/>" + count_changes + " <gold/>"+ replacedType.name().toLowerCase() +" have been fucked out and now illuminate world peacefully with "+ replaceByType.name().toLowerCase() +" !");
		return true;
	}
	
	/**
	 * Commande d'aide. Liste donc les commandes dispo, et l'aide.
	 */
	public boolean cmdHelp()
	{
		say("<gray/>[ === <gold/>KnooCraft <gray/>==== ]");
		if(countCmdArgs() == 0) {
			say("<gray/>Commandes:");
			say("<gold/>/kc help                             :<gray/> Affiche cet ecran d'aide.");
			say("<gold/>/kc help <commande>                  :<gray/> Afficher l'aide de la commande");
			say("<gold/>/kc greenwhooler <on/off>            :<gray/> Tapissage de sol de Nether");
			say("<gold/>/kc sanitizehell [range]             :<gray/> Assainissement de l'enfer.");
			say("<gold/>/kc getwhool <color> <qty>           :<gray/> Optention de laine coloree.");
			say("<gold/>/kc listalias                        :<gray/> Liste les alias des commandes.");
			say("<gold/>/kc give <item> [qtt] [\"inchest\"]  :<gray/> donne objet.");
			say("<gold/>/kc eyetp                            :<gray/> tp sur curseur.");
		} 
		else {
			say("<gold/>aide des commandes en construction..");
		}
		return true;
	}
    
	/**
	 * Commande executée lors d'une commande inconnue.
	 * @return
	 */
	public boolean cmdUnknownCmd() {
		say("<gray/>[ === <gold/>KnooCraft <gray/>==== ]");
		say("<red/>commande knoocraft inconnue: <gray/>\""+ this.args[0] + "\"");
		return true;
	}
	
	/**
	 * kc est une commande qui recoit des <i>sous-commandes</i>, par exemple pour <i>kc help</i> 
	 * la commande pour bukkit est <i>kc</i>, mais pour KnooCraft, c'est <i>help</i>. 
	 * Cette fonction retourne le nombre de paramètre de la commande KnooCraft.
	 * Exemple: dans <i>kc help test test2</i>, il y à 3 params pour bukkit, mais 
	 * cette fonction renverra 2 (test et test2) que sont les params de help.  
	 * @return nombre de paramètres passés à la sous-commande
	 */
	private int countCmdArgs() {
		return args.length - 1;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
		this.args = split;
		this.sender = sender;	
		//if(!checkIsPlayer()) 				return false;
		//if(!checkPerm("knoocraft.getwool")) return false;
		//if(!checkOp()) 						return false;
		this.player = (Player)sender;
		
		if(split.length < 1) 
			return false; 
		
		String mainCmd = split[0].toLowerCase();
		if(aliases.isEmpty()) {
			String[][] commands = {
					{"sanitizehell", "sh"},
					{"greenwhooler", "gw", "gwr"},
					{"getwhool", "wool"}, 
					{"penis", "bite"},
					{"help", "h", "/h", "/?", "?", "-h", "--help"},
					{"listalias", "aliases"},
					{"give", "g"},
					{"eyetp", "etp"},
					{"musicman", "mm", "music"}
			};
			addCommands(commands);
		}
			 if(isCommandOrAlias(mainCmd, "sanitizehell"))	return cmdSanitizeHell();
		else if(isCommandOrAlias(mainCmd, "greenwhooler"))	return cmdGreenWhooler();
		else if(isCommandOrAlias(mainCmd, "getwhool")) 		return cmdGetwhool();
		else if(isCommandOrAlias(mainCmd, "penis")) 		return cmdPenis();
		else if(isCommandOrAlias(mainCmd, "help"))			return cmdHelp();
		else if(isCommandOrAlias(mainCmd, "listalias"))		return cmdListAliases();
		else if(isCommandOrAlias(mainCmd, "give"))			return cmdGive();
		else if(isCommandOrAlias(mainCmd, "eyetp"))			return cmdEyeTp();
		else if(isCommandOrAlias(mainCmd, "musicman"))		return cmdMusicMan();
		else 												return cmdUnknownCmd();
	}

	private boolean cmdMusicMan() {
		say("EN CONSTRUCTION..");
		if(KnoocraftPlayerListener.isMusicman())
			KnoocraftPlayerListener.setMusicman(false, player.getLocation());
		else 
			KnoocraftPlayerListener.setMusicman(true, player.getLocation());
		return true;
	}

	/**
	 * Téléporte si possible le joueur à l'emplacement visée sous le curseur
	 * @return
	 */
	private boolean cmdEyeTp() {
		Location loc = player.getTargetBlock(null, 1024).getLocation();
		loc.setY(loc.getBlockY() + 1);
		player.teleport(loc);
		return true;
	}

	/**
	 * donne des objets (remplace la commande /give par défaut)
	 * On peu donner l'ID ou le nom du matériau.
	 * Usage: /kc give cobblestone 900 inchest
	 * @return
	 */
	private boolean cmdGive() {
		/* ---------------------------------------- */
		/* 	Traitement des params
		/* ---------------------------------------- */
		if(countCmdArgs() >= 1) {
			String tmp = getArg(0);
			Material material = null;
			try {
				int id_blocktype = Integer.parseInt(tmp);
				material = Material.getMaterial(id_blocktype);
			} catch (Exception e) {
				material = Material.getMaterial(tmp.toUpperCase(Locale.ENGLISH));
			}
			int qtt = (countCmdArgs() > 1) ? Integer.parseInt(getArg(1)) : 1;
			boolean in_chest = false;
			if(countCmdArgs() > 2)
				if(getArg(2).equalsIgnoreCase("inchest"))
					in_chest = true;
			/* ---------------------------------------- */
			/* 	Code
			/* ---------------------------------------- */
			
			
			Object destination;
			ItemStack given = null;
			destination = in_chest ?
					(Object) player.getLocation().getBlock()
					: (Object)player.getInventory();
			
					
			given = new ItemStack(material, qtt);
			
			say(in_chest? "Donne des objets dans un coffre.." : "Donne des objets dans l'inventaire");
			
			if(in_chest) {
				((Block)destination).setType(Material.CHEST);
				((Chest) ((Block)destination).getState()).getInventory().addItem(given);
			}
			else ((PlayerInventory)destination).addItem(given);
			
		} else {
			say("<red/>Usage: /kc give <objet|ID> [quantite]");
		}
		return true;
	}

	/**
	 * Formate le msg & l'envoi au joueur courrant.
	 * @param text
	 */
	private void say(String text) {
		player.sendMessage(msg.format(text));
	}

	/**
	 * Liste les alias utilisables des commandes
	 * @return
	 */
	private boolean cmdListAliases() {
		Set<String> k = aliases.keySet();
		for(String cmdName : k) {
			say("<gold/>" + cmdName + ": <gray/>" + aliases.get(cmdName).toString());
		}
		return true;
	}

	/**
	 * Ajoute une commande à /kc
	 * @param commands tableau de tableau. Exemple:<br/> 
	 * <code>String[][] commands = {<br/>
	 *      {"MaCommande", "alias1", "autrealias"},<br/>
	 * 	    {"AutrCommande", "aliasAutrCommande"}<br/>
	 * };</code>
	 */
	private void addCommands(String[][] commands) {
		for (String[] cmd : commands) {
			//plugin.log.log(Level.INFO, "[KNOOCRAFT] adding aliases for.. cmd[0]:" + cmd[0]);
			if(!aliases.containsKey(cmd[0])) {
				aliases.put(cmd[0], new ArrayList<String>());
			}
			for(String alias : cmd) {
				//plugin.log.log(Level.INFO, "[KNOOCRAFT] trying to add alias..:" + alias);
				ArrayList<String> c = aliases.get(cmd[0]);
				c.add(alias);
			}
		}
	}

	/**
	 * indique si la chaine passé correspond à la commande passée ou à un de ses alias.  
	 * @param mainCmd chaine testée
	 * @param string nom de la commande attendue
	 * @return vrai si c'est bien la commande, faux sinon.
	 */
	private boolean isCommandOrAlias(String mainCmd, String string) {
			return (aliases.get(string).contains(mainCmd));
	}

	/**
	 * Démare le tapissage !
	 * @return
	 */
	private boolean cmdGreenWhooler() 
	{ 
		if(countCmdArgs() >= 1) 
		{
			boolean OnOff = getArg(0).equalsIgnoreCase("on") ? true : false;
			if(OnOff) 
			{
				KnoocraftPlayerListener.greenwhooling = true;
				say("[ <pink/>Greenwhooling began ]");
			} 
			else if (getArg(0).equalsIgnoreCase("off")) 
			{
				KnoocraftPlayerListener.greenwhooling = false;
				say("[ <pink/>Greenwhooling stopped ]");
			}
			else
			{
				say("Usage: <gold/>/greenwhooler <on|off>");
			}
		} 
		else 
		{ 
			say("Usage: <gold/>/greenwhooler <on|off>");
			return false;
		}
		return true;
	}
}