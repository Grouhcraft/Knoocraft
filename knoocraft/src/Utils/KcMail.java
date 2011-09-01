package Utils;

import java.util.logging.Level;

import knoodrake.knoocraft.knoocraft;

import org.bukkit.entity.Player;

import com.avaje.ebeaninternal.server.lib.util.MailMessage;
import com.avaje.ebeaninternal.server.lib.util.MailSender;


public class KcMail {
	private MailMessage mail = new MailMessage();
	private String mailTemplate = "[##PLAYERNAME##:##POSITION##:##INGAMETIME##] ##PLAYERTEXT##";
	private String debugInfosTemplate = "[DEBUG INFOS: ]";
	private String subjectTemplate = "[Minecraft] Message de ##PLAYER_DISPLAY_NAME##";
	private String smtpServer = "smtp.neuf.fr";
	
	public KcMail(String from, String to, String text, Player player, Boolean includesDebugInfos) {
		String mailText = mailTemplate;
		mailText.replaceAll("##POSITION##", player.getLocation().toString());
		mailText.replaceAll("##PLAYERNAME##", player.getName());
		mailText.replaceAll("##PLAYERTEXT##", text);
		mailText.replaceAll("##INGAMETIME##", String.valueOf(player.getWorld().getTime()));
		
		if(includesDebugInfos) {
			String debugInfos = debugInfosTemplate;
			debugInfos.replaceAll("##ITEM_IN_HAND##", player.getItemInHand().getType().name());
			debugInfos.replaceAll("##PLAYER_HEALTH##", String.valueOf(player.getHealth()));
			
			mailText += ("\n" + debugInfos);   
		}
		
		String subject = subjectTemplate;
		subject.replaceAll("##PLAYER_DISPLAY_NAME##", player.getDisplayName());
		
		mail.addBodyLine(mailText);
		mail.addRecipient(to, to);
		mail.setSender(player.getDisplayName(), from);
		mail.setSubject(subject);
		
		try {
			MailSender ms = new MailSender(smtpServer);
			ms.send(mail);
		}
		catch (Exception e) {
			knoocraft.log.log(Level.INFO, "Impossible d envoyer le mail: " + e.getMessage());
		}
	}
}
