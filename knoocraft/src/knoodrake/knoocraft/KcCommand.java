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
import org.bukkit.util.Vector;
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
	private boolean cmdGetwool() {
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
					return cmdHelp();
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
	public boolean cmdPenis() {

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

		Location ploc = player.getLocation();
		World world = ploc.getWorld();

		// Min et max vertical
		int minY = ploc.getBlockY() + 2;
		int maxY = ploc.getBlockY() + 4 * size + 1;

		// Autres points de repère verticaux
		int maxcouY = minY + size - 1;
		int minglaY = maxcouY + 2 * size + 1;

		boolean libre = true;

		// Orientation du regard du joueur
		Orientation ori = dirEye();
		if (ori == Orientation.NORTH || ori == Orientation.SOUTH) {
			// Les couilles s'étendent d'est en ouest face au joueur
			int minZ, maxZ;
			if (size % 2 == 1) {
				int ray = (3 * size - 1) / 2;
				minZ = ploc.getBlockZ() - ray;
				maxZ = ploc.getBlockZ() + ray;
			} else {
				int ray = 3 * (size / 2) - 1;
				minZ = ploc.getBlockZ() - ray;
				maxZ = ploc.getBlockZ() + ray + 1;
			}
			// Autres points de repère horizontaux
			int cou1Z = minZ + size - 1;
			int cou2Z = maxZ - size + 1;
			// x est constant
			int xCst;
			if (ori == Orientation.NORTH) {
				xCst = ploc.getBlockX() - 1 - R.getInt("penis.dist_user");
			} else {
				xCst = ploc.getBlockX() + 1 + R.getInt("penis.dist_user");
			}
			// On teste si la place est libre
			for (int y = minY; y <= maxcouY; y++) {
				for (int z = minZ; z <= cou1Z; z++) {
					if (!world.getBlockAt(xCst, y, z).isEmpty()) {
						libre = false;
					}
				}
				for (int z = cou2Z; z <= maxZ; z++) {
					if (!world.getBlockAt(xCst, y, z).isEmpty()) {
						libre = false;
					}
				}
			}
			for (int y = maxcouY + 1; y <= maxY; y++) {
				for (int z = cou1Z + 1; z < cou2Z; z++) {
					if (!world.getBlockAt(xCst, y, z).isEmpty()) {
						libre = false;
					}
				}
			}
			if (!libre) {
				say(R.get("penis.msg.no_space"));
			} else {
				// On construit la bite
				for (int y = minY; y <= maxcouY; y++) {
					for (int z = minZ; z <= cou1Z; z++) {
						world.getBlockAt(xCst, y, z).setType(Material.DIRT);
					}
					for (int z = cou2Z; z <= maxZ; z++) {
						world.getBlockAt(xCst, y, z).setType(Material.DIRT);
					}
				}
				for (int z = cou1Z + 1; z < cou2Z; z++) {
					for (int y = maxcouY + 1; y < minglaY; y++) {
						world.getBlockAt(xCst, y, z).setType(Material.WOOL);
						world.getBlockAt(xCst, y, z).setData((byte) 6);
					}
					for (int y = minglaY; y <= maxY; y++) {
						world.getBlockAt(xCst, y, z).setType(
								Material.NETHERRACK);
					}
				}
				// On construit le socle
				for (int y = ploc.getBlockY(); y < minY; y++) {
					for (int z = minZ - 1; z <= maxZ + 1; z++) {
						if (world.getBlockAt(xCst, y, z).getType() == Material.AIR
								|| world.getBlockAt(xCst, y, z).getType() == Material.SNOW) {
							world.getBlockAt(xCst, y, z)
									.setType(Material.GLASS);
						}
					}
				}

			}
		} else {
			// Les couilles s'étendent du nord au sud face au joueur
			int minX, maxX;
			if (size % 2 == 1) {
				int ray = (3 * size - 1) / 2;
				minX = ploc.getBlockX() - ray;
				maxX = ploc.getBlockX() + ray;
			} else {
				int ray = 3 * (size / 2) - 1;
				minX = ploc.getBlockX() - ray;
				maxX = ploc.getBlockX() + ray + 1;
			}
			// Autres points de repère horizontaux
			int cou1X = minX + size - 1;
			int cou2X = maxX - size + 1;
			// z est constant
			int zCst;
			if (ori == Orientation.EAST) {
				zCst = ploc.getBlockZ() - 1 - R.getInt("penis.dist_user");
			} else {
				zCst = ploc.getBlockZ() + 1 + R.getInt("penis.dist_user");
			}
			// On teste si la place est libre
			for (int y = minY; y <= maxcouY; y++) {
				for (int x = minX; x <= cou1X; x++) {
					if (!world.getBlockAt(x, y, zCst).isEmpty()) {
						libre = false;
					}
				}
				for (int x = cou2X; x <= maxX; x++) {
					if (!world.getBlockAt(x, y, zCst).isEmpty()) {
						libre = false;
					}
				}
			}
			for (int y = maxcouY + 1; y <= maxY; y++) {
				for (int x = cou1X + 1; x < cou2X; x++) {
					if (!world.getBlockAt(x, y, zCst).isEmpty()) {
						libre = false;
					}
				}
			}
			if (!libre) {
				say(R.get("penis.msg.no_space"));
			} else {
				// On construit la bite
				for (int y = minY; y <= maxcouY; y++) {
					for (int x = minX; x <= cou1X; x++) {
						world.getBlockAt(x, y, zCst).setType(Material.DIRT);
					}
					for (int x = cou2X; x <= maxX; x++) {
						world.getBlockAt(x, y, zCst).setType(Material.DIRT);
					}
				}
				for (int x = cou1X + 1; x < cou2X; x++) {
					for (int y = maxcouY + 1; y < minglaY; y++) {
						world.getBlockAt(x, y, zCst).setType(Material.WOOL);
						world.getBlockAt(x, y, zCst).setData((byte) 6);
					}
					for (int y = minglaY; y <= maxY; y++) {
						world.getBlockAt(x, y, zCst).setType(
								Material.NETHERRACK);
					}
				}
				// On construit le socle
				for (int y = ploc.getBlockY(); y < minY; y++) {
					for (int x = minX - 1; x <= maxX + 1; x++) {
						if (world.getBlockAt(x, y, zCst).getType() == Material.AIR
								|| world.getBlockAt(x, y, zCst).getType() == Material.SNOW) {
							world.getBlockAt(x, y, zCst)
									.setType(Material.GLASS);
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Remplace certains types de blocs par d'autre, choisis dans les fichiers
	 * de conf.
	 * 
	 * @return un booléen
	 */
	public boolean cmdSanitizeHell() {
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
	public boolean cmdHelp() {
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
	public boolean cmdUnknownCmd() {
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
		// if(!checkIsPlayer()) return false;
		// if(!checkPerm("knoocraft.getwool")) return false;
		// if(!checkOp()) return false;
		this.player = (Player) sender;

		if (split.length < 1)
			return false;

		String mainCmd = split[0].toLowerCase();
		if (aliases.isEmpty()) {
			String[][] commands = { { "sanitizehell", "sh" },
					{ "greenwooler", "gw", "gwr" }, { "getwool", "wool" },
					{ "penis", "bite" },
					{ "help", "h", "/h", "/?", "?", "-h", "--help" },
					{ "listalias", "aliases" }, { "give", "g" },
					{ "eyetp", "etp" }, { "orient", "orientation", "compass" }, };
			addCommands(commands);
		}
		if (isCommandOrAlias(mainCmd, "sanitizehell"))
			return cmdSanitizeHell();
		if (isCommandOrAlias(mainCmd, "greenwooler"))
			return cmdGreenWooler();
		if (isCommandOrAlias(mainCmd, "getwool"))
			return cmdGetwool();
		if (isCommandOrAlias(mainCmd, "penis"))
			return cmdPenis();
		if (isCommandOrAlias(mainCmd, "help"))
			return cmdHelp();
		if (isCommandOrAlias(mainCmd, "listalias"))
			return cmdListAliases();
		if (isCommandOrAlias(mainCmd, "give"))
			return cmdGive();
		if (isCommandOrAlias(mainCmd, "eyetp"))
			return cmdEyeTp();
		if (isCommandOrAlias(mainCmd, "orient"))
			return cmdOrientation();
		else
			return cmdUnknownCmd();
	}

	/**
	 * Téléporte si possible le joueur à l'emplacement visée sous le curseur
	 * 
	 * @return
	 */
	private boolean cmdEyeTp() {
		Location loc = player.getTargetBlock(null, 1024).getLocation();
		loc.setY(loc.getBlockY() + 1);
		player.teleport(loc);
		return true;
	}

	/**
	 * Les 4 points cardinaux
	 */
	private enum Orientation {
		NORTH, // -1, 0, 0
		SOUTH, // 1, 0, 0
		EAST, // 0, 0, -1
		WEST
		// 0 , 0, 1
	}

	/**
	 * Renvoie le point cardinal vers lequel est orienté le regard du joueur
	 * 
	 * @return Orientation
	 */
	private Orientation dirEye() {
		Location eyeloc = player.getEyeLocation();
		Vector dir = eyeloc.getDirection();
		double dirx = dir.getX();
		double dirz = dir.getZ();
		if (dirz == dirx) {
			if (dirx >= 0) {
				return Orientation.SOUTH;
			}
			return Orientation.NORTH;
		}
		if (dirz == -dirx) {
			if (dirz > 0) {
				return Orientation.WEST;
			}
			return Orientation.EAST;
		}
		if (dirz < dirx) {
			if (dirz > -dirx) {
				return Orientation.SOUTH;
			}
			return Orientation.EAST;
		}
		// dirz > dirx
		if (dirz > -dirx) {
			return Orientation.WEST;
		}
		return Orientation.NORTH;
	}

	/**
	 * Affiche le point cardinal vers lequel est orienté le regard du joueur
	 */
	private boolean cmdOrientation() {
		Orientation ori = dirEye();
		if (ori == Orientation.NORTH) {
			say(R.get("orientation.north"));
		} else {
			if (ori == Orientation.SOUTH) {
				say(R.get("orientation.south"));
			} else {
				if (ori == Orientation.EAST) {
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
	 * 
	 * @return
	 */
	private boolean cmdGive() {
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
	 * Formate le msg & l'envoi au joueur courrant.
	 * 
	 * @param text
	 */
	private void say(String text) {
		player.sendMessage(msg.format(text));
	}

	/**
	 * Liste les alias utilisables des commandes
	 * 
	 * @return
	 */
	private boolean cmdListAliases() {
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
			// plugin.log.log(Level.INFO,
			// "[KNOOCRAFT] adding aliases for.. cmd[0]:" + cmd[0]);
			if (!aliases.containsKey(cmd[0])) {
				aliases.put(cmd[0], new ArrayList<String>());
			}
			for (String alias : cmd) {
				// plugin.log.log(Level.INFO,
				// "[KNOOCRAFT] trying to add alias..:" + alias);
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
	private boolean cmdGreenWooler() {
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