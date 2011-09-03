package knoodrake.knoocraft;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
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

import Utils.KcMail;
import Utils.KcMessaging;
import Utils.KcStrings;

import knoodrake.knoocraft.Orientation.CardinalPoints;

public class KcCommand implements CommandExecutor {
	private final knoocraft plugin;
	private KcMessaging msg = new KcMessaging();
	private String[] args = {};
	private Player player = null;
	private HashMap<String, ArrayList<String>> aliases = new HashMap<String, ArrayList<String>>();
	private boolean SuperSpeeded = false;

	public KcCommand(knoocraft plugin) {
		this.plugin = plugin;
	}

	/**
	 * Vérifie si le joueur courrant (tappant la commande) à le droit de le faire.<br/>
	 * Exemple à metre en 1<sup>ère</sup> ligne d'une méthode d'une commande:<br/>
	 * <code>if(!playerHasRightsForCommand("getwool")) return false;</code><br/>
	 * Tout simplement !
	 * @param cmdname : nom de la commande (ex: <i>eyetp</i>) 
	 * @return true ou false selon que le joueur à le droit ou non
	 */
	private boolean playerHasRightsForCommand(String cmdname) {
		if(!player.hasPermission("knoocraft.commands." + cmdname.toLowerCase())) {
			say(KcStrings.getString("player_doesnt_have_permission"));
			return false;
		}
		return true;
	}

	/**
	 * Retourne un sous-paramètre de la commande. Exemple pour <i>/kc getwool
	 * white 64</i> getArg(0) donnera <i>white</i>, et getArg(1) <i>64</i>
	 * 
	 * @param num
	 *            paramètre demandé (sa position)
	 * @return la valeur du parametre (le parametre quoi)
	 * @throws ArrayIndexOutOfBoundsException
	 *             si on demande un param inexistant
	 */
	private String getArg(int num) throws ArrayIndexOutOfBoundsException {
		int index = num + 1;
		if (args.length >= index)
			return args[index];
		else
			throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * Donne des blocs de laine dans la couleur et la quantité souhaitée.
	 * Remarque: les blocs arrivent directement dans l'inventaire. Excpetion non
	 * gérée si plus de place..
	 * 
	 * @return vrai si Ok.
	 */
	public boolean cmd_getwool() {
		if(!playerHasRightsForCommand("getwool")) return false;
		
		HashMap<String, Short> colors = new HashMap<String, Short>();
		colors.put("white", (short) 0);
		colors.put("orange", (short) 1);
		colors.put("magenta", (short) 2);
		colors.put("lightblue", (short) 3);
		colors.put("yellow", (short) 4);
		colors.put("lightgreen", (short) 5);
		colors.put("pink", (short) 6);
		colors.put("gray", (short) 7);
		colors.put("lightgray", (short) 8);
		colors.put("cyan", (short) 9);
		colors.put("purple", (short) 10);
		colors.put("blue", (short) 11);
		colors.put("brown", (short) 12);
		colors.put("darkgreen", (short) 13);
		colors.put("red", (short) 14);
		colors.put("black", (short) 15);

		if (getArg(0).equalsIgnoreCase("list")) {
			say(KcStrings.getString("getwool.list.title"));
			say(KcStrings.getString("getwool.list.white"));
			say(KcStrings.getString("getwool.list.orange"));
			say(KcStrings.getString("getwool.list.magenta"));
			say(KcStrings.getString("getwool.list.lightblue"));
			say(KcStrings.getString("getwool.list.yellow"));
			say(KcStrings.getString("getwool.list.lightgreen"));
			say(KcStrings.getString("getwool.list.pink"));
			say(KcStrings.getString("getwool.list.gray"));
			say(KcStrings.getString("getwool.list.lightgray"));
			say(KcStrings.getString("getwool.list.cyan"));
			say(KcStrings.getString("getwool.list.purple"));
			say(KcStrings.getString("getwool.list.blue"));
			say(KcStrings.getString("getwool.list.brown"));
			say(KcStrings.getString("getwool.list.darkgreen"));
			say(KcStrings.getString("getwool.list.red"));
			say(KcStrings.getString("getwool.list.black"));
		} else {
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
					args = new String[] {};
					return cmd_help();
				}
			}

			if (colors.containsKey(color)) {
				Inventory inventory = player.getInventory();
				ItemStack coloredWool = new ItemStack(Material.WOOL, amount,
						colors.get(color.toLowerCase()));
				inventory.addItem(coloredWool);
			} else {
				say(KcStrings.getString("getwool.unknown.title") + color);
				say(KcStrings.getString("getwool.unknown.details"));
				return false;
			}
		}
		return true;
	}

	/**
	 * Dessine une bite de la taille souhaitée
	 * La taille (paramètre optionel) correspond au
	 * diamètre d'une couille carrée
	 */
	public boolean cmd_penis() {
		if(!playerHasRightsForCommand("penis")) return false;
		
		int size = plugin.getConfig("main").getInt("penis.default_size");
		if (countCmdArgs() >= 1) size = Integer.parseInt(getArg(0));
		int sizeMax = R.getInt("penis.max_size");
		if (size > sizeMax) {
			say(KcStrings.getString("penis.msg.too_big.1") + size
					+ KcStrings.getString("penis.msg.too_big.2") + sizeMax
					+ KcStrings.getString("penis.msg.too_big.3"));
			size = sizeMax;
		}
		say(KcStrings.getString("penis.msg.size") + size);

		Penis penis = new Penis(plugin, player, size);
		penis.build();

		return true;
	}
	
	public boolean cmd_mail()
	{
		try {
			new KcMail(plugin, "knoodrake@gmail.com","knoodrake@gmail.com", getArg(0), player, true);
		} catch (IOException e) {
			e.printStackTrace();
		}  
		say("message envoye a knoodrake@gmail.com: " + getArg(0));
		return true;
	}

	/**
	 * Remplace certains types de blocs par d'autre, choisis dans les fichiers
	 * de conf.
	 * 
	 * @return un booléen
	 */
	public boolean cmd_sanitizehell() 
	{
		if(!playerHasRightsForCommand("sanitizehell")) return false;
		
		int range = plugin.getConfig("main").getInt("sanitizeahell.default_range");
		if (countCmdArgs() >= 1)
			range = Integer.parseInt(getArg(0));

		say(R.get("sanitizehell.msg.title"));
		say(R.get("sanitizehell.msg.range") + range);
		Location ploc = player.getLocation();
		World world = ploc.getWorld();
		int minX = ploc.getBlockX() - range;
		int maxX = ploc.getBlockX() + range;
		int minZ = ploc.getBlockZ() - range;
		int maxZ = ploc.getBlockZ() + range;
		int minY = ploc.getBlockY() - range;
		int maxY = ploc.getBlockY() + range;

		Material replacedType = Material.getMaterial(plugin.getConfig("main").getString("sanitizehell.replacedType"));
		Material replaceByType = Material.getMaterial(plugin.getConfig("main").getString("sanitizehell.replaceByType"));

		int count_changes = 0;
		for (int y = minY; y < maxY; y++) {
			for (int x = minX; x < maxX; x++) {
				for (int z = minZ; z < maxZ; z++) {
					Block b = world.getBlockAt(x, y, z);
					if (b.getType() == replacedType) {
						b.setType(replaceByType);
						count_changes++;
					}
				}
			}
		}
		say(KcStrings.getString("sanitizehell.msg.success.1") + count_changes
				+ KcStrings.getString("sanitizehell.msg.success.2")
				+ replacedType.name().toLowerCase()
				+ KcStrings.getString("sanitizehell.msg.success.3")
				+ replaceByType.name().toLowerCase() + " !");
		return true;
	}

	/**
	 * Commande d'aide. Liste donc les commandes dispo, et l'aide.
	 */
	public boolean cmd_help() {
		say(R.get("help.title"));
		if (countCmdArgs() == 0) {
			say(KcStrings.getString("help.cmd.title"));
			say(KcStrings.getString("help.cmd.help"));
			say(KcStrings.getString("help.cmd.help_cmd"));
			say(KcStrings.getString("help.cmd.greenwooler"));
			say(KcStrings.getString("help.cmd.sanitizehell"));
			say(KcStrings.getString("help.cmd.getwool"));
			say(KcStrings.getString("help.cmd.listalias"));
			say(KcStrings.getString("help.cmd.give"));
			say(KcStrings.getString("help.cmd.eyetp"));
			say(KcStrings.getString("help.cmd.penis"));
		} else {
			say(KcStrings.getString("help.cmd.unknown"));
		}
		return true;
	}

	/**
	 * Commande executée lors d'une commande inconnue.
	 * 
	 * @return
	 */
	public boolean cmd_unknowncmd() {
		say(KcStrings.getString("unknown_cmd.title"));
		say(KcStrings.getString("unknown_cmd.details.1") + this.args[0]
				+ KcStrings.getString("unknown_cmd.details.2"));
		return true;
	}
	
	public void cmd_superspeed()
	{
		SuperSpeeded = !SuperSpeeded;
		if(SuperSpeeded)
			player.setVelocity(player.getVelocity().multiply(3.0));
	}

	/**
	 * kc est une commande qui recoit des <i>sous-commandes</i>, par exemple
	 * pour <i>kc help</i> la commande pour bukkit est <i>kc</i>, mais pour
	 * KnooCraft, c'est <i>help</i>. Cette fonction retourne le nombre de
	 * parametre de la commandSe KnooCraft. Exemple: dans <i>kc help test
	 * test2</i>, il y à 3 params pour bukkit, mais cette fonction renverra 2
	 * (test et test2) que sont les params de help.
	 * 
	 * @return nombre de parametres passés à la sous-commande
	 */
	private int countCmdArgs() {
		return args.length - 1;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
		this.args = split;
		this.player = (Player) sender;

		if (split.length < 1)
			return false;

		String mainCmd = split[0].toLowerCase();
		if (aliases.isEmpty()) {
			this.addCommands(new String[][]{
					/*
					 * Ajouter les commandes suivi des alias ici
					 */
					{ "sanitizehell", "sh" },
					{ "greenwooler", "gw", "gwr" }, 
					{ "getwool", "wool" },
					{ "penis", "bite" },
					{ "help", "h", "/h", "/?", "?", "-h", "--help" },
					{ "listalias", "aliases" }, 
					{ "give", "g" },
					{ "eyetp", "etp" }, 
					{ "orient", "orientation", "compass" },
					{ "superspeed", "speed", "s"}, ///@<FIXME a faire ou effacer
					{ "mail", "sendmail" }
				});
		}
		
		//on tente de trouver la méthode correspondant à 
		//la commande ou l'alias tappé (mainCmd).
		//FIXME S'occuper de ces exceptions catchés a blanc
		for (String cmdName : aliases.keySet()) {
			if(isCommandOrAlias(mainCmd, cmdName)) {
				java.lang.reflect.Method method;
				try {
				  method = this.getClass().getMethod("cmd_" + cmdName);
				  return (Boolean) method.invoke(this);
				} 
				catch (SecurityException e) {} 
				catch (NoSuchMethodException e) {
					return cmd_unknowncmd();
				}
				catch (IllegalArgumentException e) {}
				catch (IllegalAccessException e) {}
				catch (InvocationTargetException e) {}
			}
		}
		return cmd_unknowncmd();
	}

	/**
	 * Téléporte si possible le joueur à l'emplacement visée sous le curseur
	 * 
	 * @return
	 */
	public boolean cmd_eyetp() {
		if(!playerHasRightsForCommand("eyetp")) return false;
		
		Location loc = player.getTargetBlock(null, 1024).getLocation();
		loc.setY(loc.getBlockY() + 1);
		player.teleport(loc);
		return true;
	}

	/**
	 * Affiche le point cardinal vers lequel est orienté le regard du joueur
	 */
	public boolean cmd_orientation() {
		if(!playerHasRightsForCommand("orientation")) return false;
		
		Orientation orientation = new Orientation(player);
		CardinalPoints cardinalPoint = orientation.dirEye();
		if (cardinalPoint == Orientation.CardinalPoints.NORTH) {
			say(KcStrings.getString("orientation.north"));
		} else {
			if (cardinalPoint == Orientation.CardinalPoints.SOUTH) {
				say(KcStrings.getString("orientation.south"));
			} else {
				if (cardinalPoint == Orientation.CardinalPoints.EAST) {
					say(KcStrings.getString("orientation.east"));
				} else {
					say(KcStrings.getString("orientation.west"));
				}
			}
		}
		return true;
	}

	
	/**
	 * donne des objets (remplace la commande /give par défaut) On peu donner
	 * l'ID ou le nom du matériau. Usage: /kc give cobblestone 900 inchest
	 * De plus, la qtt est facultative. on peu donc tapper: /kc g wood pour avoir 1 bloc de bois
	 * @return
	 */
	public boolean cmd_give() {
		if(!playerHasRightsForCommand("give")) return false;
		
		/* ----------------------------------------
		 *
		 * Traitement des params 
		 *
		 * ---------------------------------------- */
		if (countCmdArgs() >= 1) {
			String tmp = getArg(0);
			Material material = null;
			try {
				int id_blocktype = Integer.parseInt(tmp);
				material = Material.getMaterial(id_blocktype);
			} catch (Exception e) {
				material = Material
						.getMaterial(tmp.toUpperCase(Locale.ENGLISH));
			}
			int qtt = (countCmdArgs() > 1) ? Integer.parseInt(getArg(1)) : 1;
			boolean in_chest = false;
			if (countCmdArgs() > 2)
				if (getArg(2).equalsIgnoreCase("inchest"))
					in_chest = true;
			/* ----------------------------------------
			 *
			 * Code 
			 * 
			 * -------------------------------------- */

			Object destination;
			ItemStack given = null;
			destination = in_chest ? 
					  (Object) player.getLocation().getBlock()
					: (Object) player.getInventory();

			given = new ItemStack(material, qtt);

			say(in_chest ? KcStrings.getString("give.msg.chest") : KcStrings.getString("give.msg.inventory"));

			if (in_chest) {
				((Block) destination).setType(Material.CHEST);
				((Chest) ((Block) destination).getState()).getInventory()
						.addItem(given);
			} else
				((PlayerInventory) destination).addItem(given);

		} else {
			say(KcStrings.getString("give.msg.unknown"));
		}
		return true;
	}

	/**
	 * Formate le msg & l'envoie au joueur courrant.
	 * 
	 * @param text
	 */
	public void say(String text) {
		player.sendMessage(msg.format(text));
	}

	/**
	 * Liste les alias utilisables des commandes
	 * 
	 * @return
	 */
	public boolean cmd_listaliases() {
		if(!playerHasRightsForCommand("listaliases")) return false;

		Set<String> k = aliases.keySet();
		for (String cmdName : k) {
			say(KcStrings.getString("list_aliases.cmd.1") + cmdName
					+ KcStrings.getString("list_aliases.cmd.2")
					+ aliases.get(cmdName).toString());
		}
		return true;
	}

	/**
	 * Ajoute une commande à /kc
	 * 
	 * @param commands
	 *            tableau de tableau. Exemple:<br/>
	 *            <code>String[][] commands = {<br/>
	 * {"MaCommande", "alias1", "autrealias"},<br/>
	 * {"AutrCommande", "aliasAutrCommande"}<br/>
	 * };</code>
	 */
	private void addCommands(String[][] commands) {
		for (String[] cmd : commands) {
			if (!aliases.containsKey(cmd[0])) {
				aliases.put(cmd[0], new ArrayList<String>());
			}
			for (String alias : cmd) {
				ArrayList<String> c = aliases.get(cmd[0]);
				c.add(alias);
			}
		}
	}

	/**
	 * indique si la chaine passé correspond à la commande passée ou à un de ses
	 * alias.
	 * 
	 * @param mainCmd
	 *            chaine testée
	 * @param string
	 *            nom de la commande attendue
	 * @return vrai si c'est bien la commande, faux sinon.
	 */
	private boolean isCommandOrAlias(String mainCmd, String string) {
		return (aliases.get(string).contains(mainCmd));
	}

	/**
	 * Démarre le tapissage !
	 * 
	 * @return
	 */
	public boolean cmd_greenwooler() {
		if(!playerHasRightsForCommand("greenwooler")) return false;
		
		if (countCmdArgs() >= 1) {
			boolean OnOff = getArg(0).equalsIgnoreCase("on") ? true : false;
			if (OnOff) {
				KnoocraftPlayerListener.greenwooling = true;
				say(KcStrings.getString("greenwooler.msg.start"));
			} else if (getArg(0).equalsIgnoreCase("off")) {
				KnoocraftPlayerListener.greenwooling = false;
				say(KcStrings.getString("greenwooler.msg.stop"));
			} else {
				say(KcStrings.getString("greenwooler.msg.unknown"));
			}
		} else {
			say(KcStrings.getString("greenwooler.msg.unknown"));
			return false;
		}
		return true;
	}
}
