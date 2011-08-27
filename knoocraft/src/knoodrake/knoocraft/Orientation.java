package knoodrake.knoocraft;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Orientation {

	public enum CardinalPoints {
		NORTH, // -1, 0, 0
		SOUTH, // 1, 0, 0
		EAST, // 0, 0, -1
		WEST // 0 , 0, 1
	}

	Player player;
	Location location;
	CardinalPoints cardinalPoint;

	public Orientation(Player player) {
		this.player = player;
		this.location = player.getEyeLocation();
	}

	/**
	 * Renvoie le point cardinal vers lequel est orienté le regard du joueur et
	 * met à jour le champ cardinalPoint
	 * 
	 * @return Orientation
	 */
	public CardinalPoints dirEye() {
		Vector dir = this.location.getDirection();
		double dirx = dir.getX();
		double dirz = dir.getZ();
		if (dirz == dirx) {
			if (dirx >= 0) {
				this.cardinalPoint = CardinalPoints.SOUTH;
			} else {
				this.cardinalPoint = CardinalPoints.NORTH;
			}
		} else {
			if (dirz == -dirx) {
				if (dirz > 0) {
					this.cardinalPoint = CardinalPoints.WEST;
				} else {
					this.cardinalPoint = CardinalPoints.EAST;
				}
			} else {
				if (dirz < dirx) {
					if (dirz > -dirx) {
						this.cardinalPoint = CardinalPoints.SOUTH;
					}
					this.cardinalPoint = CardinalPoints.EAST;
				} else {
					// dirz > dirx
					if (dirz > -dirx) {
						this.cardinalPoint = CardinalPoints.WEST;
					} else {
						this.cardinalPoint = CardinalPoints.NORTH;
					}
				}
			}
		}
		return this.cardinalPoint;
	}

	public CardinalPoints getCardinalPoint() {
		return cardinalPoint;
	}

	public void setCardinalPoint(CardinalPoints cardinalPoint) {
		this.cardinalPoint = cardinalPoint;
	}
}