package Utils;

import java.io.IOException;
import java.util.logging.Level;

import knoodrake.knoocraft.knoocraft;

import org.bukkit.entity.Player;

import com.avaje.ebeaninternal.server.lib.util.MailMessage;
import com.avaje.ebeaninternal.server.lib.util.MailSender;
 
public class KcMail {
	private MailMessage mail = new MailMessage();
	private final knoocraft plugin;

	private String mailTemplate = "[_##WORLD_NAME##:##PLAYERNAME##:##POSITION##:##INGAMETIME##] ##PLAYERTEXT##\nVoir sur la map: http://5.75.27.167/static_map/#/##PLAYER_X##/64/##PLAYER_Z##/-2/mcmapNormal";
	private String debugInfosTemplate = "[DEBUG INFOS: Main:##ITEM_IN_HAND##, Chunk:##CHUNK_X##.##CHUNK_Z##, IP joueur: ##PLAYER_IP##, ##PLAYER_ISOP##, vie joueur:##PLAYER_HEALTH##]";
	private String subjectTemplate = "[Minecraft] Message de ##PLAYER_DISPLAY_NAME##";
	private Boolean useTemplateFIle;
	private String templateFilePath;
	private String smtpServer;

	private Player player = null;
	private String playerText = "";
	
	public KcMail(knoocraft plugin, String from, String to, String text, Player senderplayer, Boolean includesDebugInfos) throws IOException {
		player = senderplayer;
		playerText = text;
		this.plugin = plugin;
		
		smtpServer 			= plugin.getConfig("main").getString("mail.smtp_server");
		subjectTemplate 	= plugin.getConfig("main").getString("mail.template.subject");
		debugInfosTemplate 	= plugin.getConfig("main").getString("mail.template.debug_infos");
		useTemplateFIle 	= plugin.getConfig("main").getBoolean("mail.template.useAFileForBody");
		templateFilePath 	= plugin.getConfig("main").getString("mail.template.file_path");
		mailTemplate		= plugin.getConfig("main").getString("mail.template.body");

		String mailText = useTemplateFIle ? KcFileUtils.getTextFileContent(templateFilePath) : replacer(mailTemplate);
		
		if(includesDebugInfos) {
			String debugInfos = replacer(debugInfosTemplate);	
			mailText += ("\n" + debugInfos);   
		}
		
		String subject = replacer(subjectTemplate);
		
		for(String line: mailText.split("/\n/g")) {
			mail.addBodyLine(line);
		}
		
		mail.addRecipient(to, to);
		mail.setSender(player.getDisplayName(), from);
		mail.setSubject(subject);
		
		try {
			MailSender ms = new MailSender(smtpServer);
			ms.send(mail);
		}
		catch (Exception e) {
			knoocraft.log.log(Level.WARNING, "Impossible d envoyer le mail: " + e.getMessage());
		}
	}

	private String replacer(String string) 
	{
		String l_token = "##";
		String r_token = "##";
		//String regexp_modifiers = "g";
		
		string = string
				.replaceAll(l_token+ "POSITION"			+r_token, player.getLocation().toString())
				.replaceAll(l_token+ "PLAYERNAME"		+r_token, player.getName())
				.replaceAll(l_token+ "PLAYERTEXT"		+r_token, playerText)
				.replaceAll(l_token+ "INGAMETIME"		+r_token, String.valueOf(player.getWorld().getTime()))
				.replaceAll(l_token+ "ITEM_IN_HAND"		+r_token, player.getItemInHand().getType().name())		
				.replaceAll(l_token+ "PLAYER_ISOP"		+r_token, "IsOP():" + (player.isOp() ? "Oui" : "Non"))
				.replaceAll(l_token+ "PLAYER_IP"		+r_token, player.getAddress().toString())
				.replaceAll(l_token+ "WORLD_NAME"		+r_token, player.getWorld().getName())
				.replaceAll(l_token+ "CHUNK_X"			+r_token, String.valueOf(player.getWorld().getChunkAt(player.getLocation()).getX()))
				.replaceAll(l_token+ "CHUNK_Z"			+r_token, String.valueOf(player.getWorld().getChunkAt(player.getLocation()).getZ()))
				.replaceAll(l_token+ "PLAYER_HEALTH"	+r_token, String.valueOf(player.getHealth()))
				.replaceAll(l_token+ "PLAYER_DISPLAY_NAME"+r_token, player.getDisplayName())
				.replaceAll(l_token+ "PLAYER_X"			+r_token, String.valueOf(player.getLocation().getBlockX()))
				.replaceAll(l_token+ "PLAYER_Y"			+r_token, String.valueOf(player.getLocation().getBlockY()))
				.replaceAll(l_token+ "PLAYER_Z"			+r_token, String.valueOf(player.getLocation().getBlockZ()));

		return string;
	}
}
