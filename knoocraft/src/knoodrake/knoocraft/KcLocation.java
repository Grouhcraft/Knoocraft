package knoodrake.knoocraft;

import java.util.Date;

import org.bukkit.Location;



public class KcLocation {

	protected String[] owner = {};
	protected Location location = null;
	protected Date date_added = null;
	protected String description = "";
	protected String name = "";
	protected Boolean isPrivate = false;
	
	// --------------------------------
	
	
	public String[] getOwner() {
		return owner;
	}
	public void setOwner(String[] owner) {
		this.owner = owner;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Date getDate_added() {
		return date_added;
	}
	public void setDate_added(Date date_added) {
		this.date_added = date_added;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean isPrivate() {
		return isPrivate;
	}
	public void setPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	public Boolean isPublic() {
		return !isPrivate;
	}
}
