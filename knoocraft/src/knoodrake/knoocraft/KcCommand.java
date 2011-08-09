package knoodrake.knoocraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import knoodrake.knoocraft.KcMessaging;

public class KcCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	
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
	
	private String getArg(int num) throws ArrayIndexOutOfBoundsException
	{
		int index = num + 1;
		if(args.length >= index)
			return args[index];
		else throw new ArrayIndexOutOfBoundsException();
	}
	
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
			player.sendMessage(msg.format("====== colors: ======"));
			player.sendMessage(msg.format("  *<white/> white "));
			player.sendMessage(msg.format("  *<gold/> orange "));
			player.sendMessage(msg.format("  *<red/> magenta "));
			player.sendMessage(msg.format("  *<blue/> lightblue "));
			player.sendMessage(msg.format("  *<yellow/> yellow "));
			player.sendMessage(msg.format("  *<brightgreen/> lightgreen "));
			player.sendMessage(msg.format("  *<pink/> pink "));
			player.sendMessage(msg.format("  *<gray/> gray "));
			player.sendMessage(msg.format("  *<gray/> lightgray "));
			player.sendMessage(msg.format("  *<teal/> cyan "));
			player.sendMessage(msg.format("  *<purple/> purple "));
			player.sendMessage(msg.format("  *<darkblue/> blue "));
			player.sendMessage(msg.format("  *<red/> brown "));
			player.sendMessage(msg.format("  *<darkgreen/> darkgreen "));
			player.sendMessage(msg.format("  *<darkred/> red "));
			player.sendMessage(msg.format("  *<black/> black "));
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
				player.sendMessage(msg.format("<red/> Couleur inconnue: " + color));
				player.sendMessage(msg.format("Tappez <gold/>\"/kc getwhool list\"<white/> pour une liste des couleurs possibles."));
				return false;
			}
		}
		return true;
	}
	
	public boolean cmdSanitizeHell() 
	{
		int range = plugin.getConfig().getInt("sanitizehell.default_range", 15);
		if(countCmdArgs() >= 1) 
			range = Integer.parseInt(getArg(0));
		
		player.sendMessage(msg.format("<gray/>[ === <gold/>KnooCraft <gray/>==== ]"));
		player.sendMessage(msg.format("<pink/>Sanitize fire of Hell in a range of " + range));
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
		player.sendMessage(msg.format("<pink/>" + count_changes + " <gold/>"+ replacedType.name().toLowerCase() +" have been fucked out and now illuminate world peacefully with "+ replaceByType.name().toLowerCase() +" !"));
		return true;
	}
	
	public boolean cmdHelp()
	{
		player.sendMessage(msg.format("<gray/>[ === <gold/>KnooCraft <gray/>==== ]"));
		if(countCmdArgs() == 0) {
			player.sendMessage(msg.format("<gray/>Commandes:"));
			player.sendMessage(msg.format("<gold/>/kc help                   :<gray/> Affiche cet ecran d'aide."));
			player.sendMessage(msg.format("<gold/>/kc help <commande>        :<gray/> Afficher l'aide de la commande"));
			player.sendMessage(msg.format("<gold/>/kc greenwhooler <on/off>  :<gray/> Tapissage de sol de Nether"));
			player.sendMessage(msg.format("<gold/>/kc sanitizehell [range]   :<gray/> Assainissement de l'enfer."));
			player.sendMessage(msg.format("<gold/>/kc getwhool <color> <qty> :<gray/> Optention de laine coloree."));
			player.sendMessage(msg.format("<gold/>/kc listalias              :<gray/> Liste les alias des commandes."));
		} 
		else {
			player.sendMessage(msg.format("<gold/>aide des commandes en construction.."));
		}
		return true;
	}
    
	public boolean cmdUnknownCmd() {
		player.sendMessage(msg.format("<gray/>[ === <gold/>KnooCraft <gray/>==== ]"));
		player.sendMessage(msg.format("<red/>commande knoocraft inconnue: <gray/>\""+ this.args[0] + "\""));
		return true;
	}
	
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
					{"help", "h", "/h", "/?", "?", "-h", "--help"},
					{"listalias", "aliases"}
			};
			addCommands(commands);
		}
			 if(isCommandOrAlias(mainCmd, "sanitizehell"))	return cmdSanitizeHell();
		else if(isCommandOrAlias(mainCmd, "greenwhooler"))	return cmdGreenWhooler();
		else if(isCommandOrAlias(mainCmd, "getwhool")) 		return cmdGetwhool();
		else if(isCommandOrAlias(mainCmd, "help"))			return cmdHelp();
		else if(isCommandOrAlias(mainCmd, "listalias"))		return cmdListAliases();
		else 												return cmdUnknownCmd();
	}

	private boolean cmdListAliases() {
		Set<String> k = aliases.keySet();
		for(String cmdName : k) {
			player.sendMessage(msg.format("<gold/>" + cmdName + ": <gray/>" + aliases.get(cmdName).toString()));
		}
		return true;
	}

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

	private boolean isCommandOrAlias(String mainCmd, String string) {
			return (aliases.get(string).contains(mainCmd));
	}

	private boolean cmdGreenWhooler() 
	{ 
		if(countCmdArgs() >= 1) 
		{
			boolean OnOff = getArg(0).equalsIgnoreCase("on") ? true : false;
			if(OnOff) 
			{
				KnoocraftPlayerListener.greenwhooling = true;
				player.sendMessage(msg.format("[ <pink/>Greenwhooling began ]"));
			} 
			else if (getArg(0).equalsIgnoreCase("off")) 
			{
				KnoocraftPlayerListener.greenwhooling = false;
				player.sendMessage(msg.format("[ <pink/>Greenwhooling stopped ]"));
			}
			else
			{
				player.sendMessage(msg.format("Usage: <gold/>/greenwhooler <on|off>"));
			}
		} 
		else 
		{ 
			player.sendMessage(msg.format("Usage: <gold/>/greenwhooler <on|off>"));
			return false;
		}
		return true;
	}
}