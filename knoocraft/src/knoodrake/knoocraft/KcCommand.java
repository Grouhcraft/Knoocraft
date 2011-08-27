package knoodrake.knoocraft;

import java.lang.reflect.InvocationTargetException;
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
import knoodrake.knoocraft.Orientation.CardinalPoints;

public class KcCommand implements CommandExecutor {
	private final knoocraft plugin;
	private KcMessaging msg = new KcMessaging();
	private String[] args = {};
	private Player player = null;
	private CommandSender sender = null;
	private HashMap<String, ArrayList<String>> aliases = new HashMap<String, ArrayList<String>>();

	// toto
	public KcCommand(knoocraft plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("unused")
	private boolean checkPerm(String node) {
		boolean x = (knoocraft.permissionHandler.has(player, node));
		if (!x)
			knoocraft.log.log(Level.WARNING, msg
					.format("<red/> checkPerm: fails"));
		return x;
	}

	@SuppressWarnings("unused")
	private boolean checkIsPlayer() {
		boolean x = sender.getClass().getName().equals("Player");
		if (!x)
			knoocraft.log.log(Level.WARNING, msg
					.format("<red/> checkIsPlayer: fails"));
		return x;
	}

	@SuppressWarnings("unused")
	private boolean checkOp() {
		boolean x = player.isOp();
		if (!x)
			knoocraft.log.log(Level.WARNING, msg
					.format("<red/> checkOp: fails"));
		return x;
	}

	/**
	 * Retourne un sous-paramètre de la commande. Exemple pour <i>/kc getwool
	 * white 64</i> getArg(1) donnera <i>white</i>, et getArg(2) <i>64</i>
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
	@SuppressWarnings("unused")
	private boolean cmd_getwool() {
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
			say(R.get("getwool.list.title"));
			say(R.get("getwool.list.white"));
			say(R.get("getwool.list.orange"));
			say(R.get("getwool.list.magenta"));
			say(R.get("getwool.list.lightblue"));
			say(R.get("getwool.list.yellow"));
			say(R.get("getwool.list.lightgreen"));
			say(R.get("getwool.list.pink"));
			say(R.get("getwool.list.gray"));
			say(R.get("getwool.list.lightgray"));
			say(R.get("getwool.list.cyan"));
			say(R.get("getwool.list.purple"));
			say(R.get("getwool.list.blue"));
			say(R.get("getwool.list.brown"));
			say(R.get("getwool.list.darkgreen"));
			say(R.get("getwool.list.red"));
			say(R.get("getwool.list.black"));
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
				say(R.get("getwool.unknown.title") + color);
				say(R.get("getwool.unknown.details"));
				return false;
			}
		}
		return true;
	}

	/**
	 * Dessine une bite de la taille souhaitée
	 * 
	 * @return vrai si Ok
	 */
	public boolean cmd_penis() {

		// size correspond au diamètre d'une couille (carrée, la couille)
		int size = plugin.getConfig().getInt("penis.default_size",
				R.getInt("penis.default_size"));
		if (countCmdArgs() >= 1)
			size = Integer.parseInt(getArg(0));
		int sizeMax = R.getInt("penis.max_size");
		if (size > sizeMax) {
			say(R.get("penis.msg.too_big.1") + size
					+ R.get("penis.msg.too_big.2") + sizeMax
					+ R.get("penis.msg.too_big.3"));
			size = sizeMax;
		}
		say(R.get("penis.msg.size") + size);

		Penis penis = new Penis(plugin, player, size);
		penis.build();

		return true;
	}

	/**
	 * Remplace certains types de blocs par d'autre, choisis dans les fichiers
	 * de conf.
	 * 
	 * @return un booléen
	 */
	public boolean cmd_sanitizehell() {
		int range = plugin.getConfig().getInt("sanitizehell.default_range",
				R.getInt("sanitizehell.default_range"));
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

		Material replacedType = Material.getMaterial(plugin.getConfig()
				.getString("sanitizehell.replacedType", Material.FIRE.name())
				.toUpperCase());
		Material replaceByType = Material.getMaterial(plugin.getConfig()
				.getString("sanitizehell.replaceByType",
						Material.GLOWSTONE.name()).toUpperCase());

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
		say(R.get("sanitizehell.msg.success.1") + count_changes
				+ R.get("sanitizehell.msg.success.2")
				+ replacedType.name().toLowerCase()
				+ R.get("sanitizehell.msg.success.3")
				+ replaceByType.name().toLowerCase() + " !");
		return true;
	}

	/**
	 * Commande d'aide. Liste donc les commandes dispo, et l'aide.
	 */
	public boolean cmd_help() {
		say(R.get("help.title"));
		if (countCmdArgs() == 0) {
			say(R.get("help.cmd.title"));
			say(R.get("help.cmd.help"));
			say(R.get("help.cmd.help_cmd"));
			say(R.get("help.cmd.greenwooler"));
			say(R.get("help.cmd.sanitizehell"));
			say(R.get("help.cmd.getwool"));
			say(R.get("help.cmd.listalias"));
			say(R.get("help.cmd.give"));
			say(R.get("help.cmd.eyetp"));
			say(R.get("help.cmd.penis"));
		} else {
			say(R.get("help.cmd.unknown"));
		}
		return true;
	}

	/**
	 * Commande executée lors d'une commande inconnue.
	 * 
	 * @return
	 */
	public boolean cmd_unknowncmd() {
		say(R.get("unknown_cmd.title"));
		say(R.get("unknown_cmd.details.1") + this.args[0]
				+ R.get("unknown_cmd.details.2"));
		return true;
	}

	/**
	 * kc est une commande qui recoit des <i>sous-commandes</i>, par exemple
	 * pour <i>kc help</i> la commande pour bukkit est <i>kc</i>, mais pour
	 * KnooCraft, c'est <i>help</i>. Cette fonction retourne le nombre de
	 * parametre de la commande KnooCraft. Exemple: dans <i>kc help test
	 * test2</i>, il y à 3 params pour bukkit, mais cette fonction renverra 2
	 * (test et test2) que sont les params de help.
	 * 
	 * @return nombre de parametres passés à la sous-commande
	 */
	private int countCmdArgs() {
		return args.length - 1;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] split) {
		this.args = split;
		this.sender = sender;
		//TODO faire marcher..
		// if(!checkIsPlayer()) return false;
		// if(!checkPerm("knoocraft.getwool")) return false;
		// if(!checkOp()) return false;
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
					{ "orient", "orientation", "compass" }
				});
		}
		
		//on tente de trouver la méthode correspondant à 
		//la commande ou l'alias tappé (mainCmd).
		//TODO S'occuper de ces exceptions là..
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
	@SuppressWarnings("unused")
	private boolean cmd_eyetp() {
		Location loc = player.getTargetBlock(null, 1024).getLocation();
		loc.setY(loc.getBlockY() + 1);
		player.teleport(loc);
		return true;
	}

	/**
	 * Affiche le point cardinal vers lequel est orienté le regard du joueur
	 */
	@SuppressWarnings("unused")
	private boolean cmd_orientation() {
		Orientation orientation = new Orientation(player);
		CardinalPoints cardinalPoint = orientation.dirEye();
		if (cardinalPoint == Orientation.CardinalPoints.NORTH) {
			say(R.get("orientation.north"));
		} else {
			if (cardinalPoint == Orientation.CardinalPoints.SOUTH) {
				say(R.get("orientation.south"));
			} else {
				if (cardinalPoint == Orientation.CardinalPoints.EAST) {
					say(R.get("orientation.east"));
				} else {
					say(R.get("orientation.west"));
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
	@SuppressWarnings("unused")
	private boolean cmd_give() {
		/* ---------------------------------------- */
		/*
		 * Traitement des params /* ----------------------------------------
		 */
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
			/* ---------------------------------------- */
			/*
			 * Code /* ----------------------------------------
			 */

			Object destination;
			ItemStack given = null;
			destination = in_chest ? (Object) player.getLocation().getBlock()
					: (Object) player.getInventory();

			given = new ItemStack(material, qtt);

			say(in_chest ? R.get("give.msg.chest") : R
					.get("give.msg.inventory"));

			if (in_chest) {
				((Block) destination).setType(Material.CHEST);
				((Chest) ((Block) destination).getState()).getInventory()
						.addItem(given);
			} else
				((PlayerInventory) destination).addItem(given);

		} else {
			say(R.get("give.msg.unknown"));
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
	@SuppressWarnings("unused")
	private boolean cmd_listaliases() {
		Set<String> k = aliases.keySet();
		for (String cmdName : k) {
			say(R.get("list_aliases.cmd.1") + cmdName
					+ R.get("list_aliases.cmd.2")
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
	@SuppressWarnings("unused")
	private boolean cmd_greenwooler() {
		if (countCmdArgs() >= 1) {
			boolean OnOff = getArg(0).equalsIgnoreCase("on") ? true : false;
			if (OnOff) {
				KnoocraftPlayerListener.greenwooling = true;
				say(R.get("greenwooler.msg.start"));
			} else if (getArg(0).equalsIgnoreCase("off")) {
				KnoocraftPlayerListener.greenwooling = false;
				say(R.get("greenwooler.msg.stop"));
			} else {
				say(R.get("greenwooler.msg.unknown"));
			}
		} else {
			say(R.get("greenwooler.msg.unknown"));
			return false;
		}
		return true;
	}
}
