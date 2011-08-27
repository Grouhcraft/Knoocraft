package knoodrake.knoocraft;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Penis {
	private Player player;
	private Location ploc;
	private final knoocraft plugin;
	int size;
	private World world;

	public Penis(knoocraft plugin, Player player) {
		this.plugin = plugin;
		this.size = R.getInt("penis.default_size");
		this.player = player;
		this.ploc = player.getLocation();
		this.world = player.getWorld();
	}

	public Penis(knoocraft plugin, Player player, int size) {
		this.plugin = plugin;
		this.size = size;
		this.player = player;
		this.ploc = player.getLocation();
		this.world = player.getWorld();
	}

	/**
	 * construire une bite parallèle à l'axe des x
	 * 
	 * @param minX
	 *            : début en x de la 1ère couille
	 * @param cou1X
	 *            : fin en x de la 1ère couille
	 * @param cou2X
	 *            : début en x de la 2ème couille
	 * @param maxX
	 *            : fin en x de la 2ème couille
	 * @param minY
	 *            : bas en y des couilles
	 * @param maxcouY
	 *            : haut en y des couilles
	 * @param minglaY
	 *            : bas en y du gland
	 * @param maxY
	 *            : haut en y de la bite
	 * @param zCst
	 *            : coordonnée z constante
	 */
	private void biteX(int minX, int cou1X, int cou2X, int maxX, int minY,
			int maxcouY, int minglaY, int maxY, int zCst) {
		// On construit la bite
		for (int y = minY; y <= maxcouY; y++) {
			for (int x = minX; x <= cou1X; x++) {
				couleurCouille(world.getBlockAt(x, y, zCst));
			}
			for (int x = cou2X; x <= maxX; x++) {
				couleurCouille(world.getBlockAt(x, y, zCst));
			}
		}
		for (int x = cou1X + 1; x < cou2X; x++) {
			for (int y = maxcouY + 1; y < minglaY; y++) {
				couleurBite(world.getBlockAt(x, y, zCst));
			}
			for (int y = minglaY; y <= maxY; y++) {
				couleurGland(world.getBlockAt(x, y, zCst));
			}
		}
		// On construit le socle
		for (int y = ploc.getBlockY(); y < minY; y++) {
			for (int x = minX - 1; x <= maxX + 1; x++) {
				if (world.getBlockAt(x, y, zCst).getType() == Material.AIR
						|| world.getBlockAt(x, y, zCst).getType() == Material.SNOW) {
					world.getBlockAt(x, y, zCst).setType(Material.GLASS);
				}
			}
		}
	}

	/**
	 * construire une bite parallèle à l'axe des z
	 * 
	 * @param minZ
	 *            : début en z de la 1ère couille
	 * @param cou1Z
	 *            : fin en z de la 1ère couille
	 * @param cou2Z
	 *            : début en z de la 2ème couille
	 * @param maxZ
	 *            : fin en z de la 2ème couille
	 * @param minY
	 *            : bas en y des couilles
	 * @param maxcouY
	 *            : haut en y des couilles
	 * @param minglaY
	 *            : bas en y du gland
	 * @param maxY
	 *            : haut en y de la bite
	 * @param xCst
	 *            : coordonnée x constante
	 */
	private void biteZ(int minZ, int cou1Z, int cou2Z, int maxZ, int minY,
			int maxcouY, int minglaY, int maxY, int xCst) {
		for (int y = minY; y <= maxcouY; y++) {
			for (int z = minZ; z <= cou1Z; z++) {
				couleurCouille(world.getBlockAt(xCst, y, z));
			}
			for (int z = cou2Z; z <= maxZ; z++) {
				couleurCouille(world.getBlockAt(xCst, y, z));
			}
		}
		for (int z = cou1Z + 1; z < cou2Z; z++) {
			for (int y = maxcouY + 1; y < minglaY; y++) {
				couleurBite(world.getBlockAt(xCst, y, z));
			}
			for (int y = minglaY; y <= maxY; y++) {
				couleurGland(world.getBlockAt(xCst, y, z));
			}
		}
		// On construit le socle
		for (int y = ploc.getBlockY(); y < minY; y++) {
			for (int z = minZ - 1; z <= maxZ + 1; z++) {
				if (world.getBlockAt(xCst, y, z).getType() == Material.AIR
						|| world.getBlockAt(xCst, y, z).getType() == Material.SNOW) {
					world.getBlockAt(xCst, y, z).setType(Material.GLASS);
				}
			}
		}
	}

	/**
	 * Construit la bite
	 * 
	 * @return true
	 */
	public boolean build() {
		// Min et max vertical
		int minY = ploc.getBlockY() + 2;
		int maxY = ploc.getBlockY() + 4 * size + 1;

		// Autres points de repère verticaux
		int maxcouY = minY + size - 1;
		int minglaY = maxcouY + 2 * size + 1;

		// Orientation du regard du joueur
		Orientation orientation = new Orientation(player);
		Orientation.CardinalPoints cardinalPoint = orientation.dirEye();
		if (cardinalPoint == Orientation.CardinalPoints.NORTH
				|| cardinalPoint == Orientation.CardinalPoints.SOUTH) {
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
			if (cardinalPoint == Orientation.CardinalPoints.NORTH) {
				xCst = ploc.getBlockX() - 1 - R.getInt("penis.dist_user");
			} else {
				xCst = ploc.getBlockX() + 1 + R.getInt("penis.dist_user");
			}
			// On teste si la place est libre
			if (!isLibreZ(minZ, cou1Z, cou2Z, maxZ, minY, maxcouY, maxY, xCst)) {
				plugin.getKcCommand().say(R.get("penis.msg.no_space"));
			} else {
				// On construit la bite
				biteZ(minZ, cou1Z, cou2Z, maxZ, minY, maxcouY, minglaY, maxY,
						xCst);
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
			if (cardinalPoint == Orientation.CardinalPoints.EAST) {
				zCst = ploc.getBlockZ() - 1 - R.getInt("penis.dist_user");
			} else {
				zCst = ploc.getBlockZ() + 1 + R.getInt("penis.dist_user");
			}
			// On teste si la place est libre
			if (!isLibreX(minX, cou1X, cou2X, maxX, minY, maxcouY, maxY, zCst)) {
				this.plugin.getKcCommand().say(R.get("penis.msg.no_space"));
			} else {
				// On construit la bite
				biteX(minX, cou1X, cou2X, maxX, minY, maxcouY, minglaY, maxY,
						zCst);
			}
		}
		return true;
	}

	/**
	 * Transformer un bloc en un morceau de bite
	 * 
	 * @param b
	 */
	private void couleurBite(Block b) {
		b.setType(Material.WOOL);
		b.setData((byte) 6);
	}

	/**
	 * Transformer un bloc en un morceau de couille
	 * 
	 * @param b
	 */
	private void couleurCouille(Block b) {
		b.setType(Material.DIRT);
	}

	/**
	 * Transformer un bloc en un morceau de gland
	 * 
	 * @param b
	 */
	private void couleurGland(Block b) {
		b.setType(Material.NETHERRACK);
	}

	/**
	 * Y a-t-il la place de construire une bite parallèle à l'axe des x ?
	 * 
	 * @param minX
	 *            : début en x de la 1ère couille
	 * @param cou1X
	 *            : fin en x de la 1ère couille
	 * @param cou2X
	 *            : début en x de la 2ème couille
	 * @param maxX
	 *            : fin en x de la 2ème couille
	 * @param minY
	 *            : bas en y des couilles
	 * @param maxcouY
	 *            : haut en y des couilles
	 * @param maxY
	 *            : haut en y de la bite
	 * @param zCst
	 *            : coordonnée z constante
	 * @return
	 */
	private boolean isLibreX(int minX, int cou1X, int cou2X, int maxX,
			int minY, int maxcouY, int maxY, int zCst) {
		boolean libre = true;
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
		return libre;
	}

	/**
	 * Y a-t-il la place de construire une bite parallèle à l'axe des z ?
	 * 
	 * @param minZ
	 *            : début en z de la 1ère couille
	 * @param cou1Z
	 *            : fin en z de la 1ère couille
	 * @param cou2Z
	 *            : début en z de la 2ème couille
	 * @param maxZ
	 *            : fin en z de la 2ème couille
	 * @param minY
	 *            : bas en y des couilles
	 * @param maxcouY
	 *            : haut en y des couilles
	 * @param maxY
	 *            : haut en y de la bite
	 * @param xCst
	 *            : coordonnée x constante
	 * @return
	 */
	private boolean isLibreZ(int minZ, int cou1Z, int cou2Z, int maxZ,
			int minY, int maxcouY, int maxY, int xCst) {
		boolean libre = true;
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
		return libre;
	}
}
