package scripts.crabs.utils;

import java.util.ArrayList;
import java.util.LinkedList;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.Combat;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;

import scripts.acamera.ACamera;
import scripts.webwalker_logic.WebWalker;

/**
 * Designed for nSand Crabs. Contains information and actions revolving around killing sand crabs.
 * @author Nate
 *
 */

public class SandCrabUtility {
	
	// Thanks to trilez's area creator script
	public static final RSArea SANDCRAB_ISLAND = new RSArea(new RSTile[] { new RSTile(1746, 3411, 0), new RSTile(1749, 3424, 0), new RSTile(1756, 3439, 0),
			new RSTile(1766, 3449, 0), new RSTile(1780, 3441, 0), new RSTile(1782, 3428, 0), new RSTile(1796, 3411, 0), new RSTile(1789, 3403, 0),
			new RSTile(1786, 3402, 0), new RSTile(1752, 3407, 0) });
	
	public static final RSTile BOAT_TILE_ISLAND = new RSTile(1778, 3418, 0);
	
	public static final RSTile BOAT_TILE_ZEAH = new RSTile(1783, 3458, 0);
	
	public final static int[] SAND_CRAB_IDS = {7206, 5935};
	
	public final static int[] SAND_ROCKS_IDS = {7207, 5936};
	
	public static final int SANDICRAHB_ZEAH_ID = 7483;
	
	public static final int SANDICRAHB_ISLAND_ID = 7484;
	
	public static final RSTile CHEST_TILE = new RSTile(1719, 3465);
	
	public static final int CHEST_ID = 10562;
	
	public static boolean talkingToNPC() {
		return !(NPCChat.getName() == null && NPCChat.getClickContinueInterface() == null
				&& NPCChat.getSelectOptionInterface() == null);
	}
	
	public static RSNPC[] getAllCrabs() {
		return NPCs.find(SAND_CRAB_IDS);
	}
	
	public static RSNPC[] getAllCrabs(Filter<RSNPC> filter) {
		return NPCs.find(filter.combine(Filters.NPCs.idEquals(SAND_CRAB_IDS), true));
	}
	
	public static RSNPC[] getAllRocks() {
		return NPCs.find(SAND_ROCKS_IDS);
	}
	
	public static RSNPC[] getAllRocks(Filter<RSNPC> filter) {
		return NPCs.find(filter.combine(Filters.NPCs.idEquals(SAND_ROCKS_IDS), true));
	}
	
	/**
	 * @return A 3x3 box with coordinates top right (x+1, y+1), and bottom left (x-1, y-1).
	 */
	public static RSArea getAggroArea() {
		return new RSArea(Player.getPosition().translate(1, 1), Player.getPosition().translate(-1, -1));
	}
	
	/** 
	 * Checks to see if there are any sandy rocks next to the player. Checks one tile away in each direction, if the player has not lost aggression then the rocks should turn into crabs.
	 * @return Whether or not there are sandy rocks next to the player.
	 */
	public static boolean sandyRocksNearby() {
		RSArea aggroArea = getAggroArea();
		for (RSNPC npc : getAllRocks())
			if (aggroArea.contains(npc))
				return true;
		return false;
	}
	
	/** 
	 * Checks to see if there are any sand crabs next to the player. Checks one tile away in each direction.
	 * @return Whether or not there are sand crabs next to the player.
	 */
	public static boolean sandCrabsNearby() {
		RSArea aggroArea = getAggroArea();
		for (RSNPC npc : getAllCrabs())
			if (aggroArea.contains(npc))
				return true;
		return false;
	}
	
	/**
	 * Finds all the tiles of all sandy rocks nearby.
	 * @return All of the tiles of sandy rocks in a 3x3 box around the player.
	 */
	public static RSTile[] getSandyRockTilesNearby() {
		LinkedList<RSTile> tiles = new LinkedList<RSTile>();
		RSArea aggroArea = getAggroArea();
		for (RSNPC npc : getAllRocks())
			if (aggroArea.contains(npc))
				tiles.add(npc.getPosition());
		return tiles.toArray(new RSTile[tiles.size()]);
	}
	
	/**
	 * Checks to see if the player has lost sand crab aggression.
	 * @return Whether or not aggression has been lost.
	 */
	public static boolean checkAggro() {
		if (sandyRocksNearby()) {
			// If there are sandy rocks nearby, wait 1200-1800 ms and check again to make sure the crab wasn't just respawning.
			RSTile[] rockTilesFirstCheck = getSandyRockTilesNearby();
			if (!Timing.waitCondition(new Condition() {
				@Override
				public boolean active() {
					General.sleep(100);
					return !sandyRocksNearby();
				}
			}, General.random(1200, 1800))) {
				/* Make sure the sandy rock has the same tile as before, just in case a crab respawned just as the second check took place.
				 * May not be necessary as the wait condition checks constantly if there are any sandy rocks nearby
				 */
				RSTile[] rockTilesSecondCheck = getSandyRockTilesNearby();
				for (RSTile tile1 : rockTilesFirstCheck)
					for (RSTile tile2 : rockTilesSecondCheck) 
						if (tile1.getPosition().equals(tile2))
							return true;
			}
		}
		return false;
		
	}
	
	/**
	 * @return A sand crab RSNPC that is located in a 3x3 box around the player. Returns null if there are none.
	 */
	public static RSNPC getNearbySandCrab() {
		for (RSNPC npc : getAllCrabs(Filters.NPCs.inArea(getAggroArea())))
			if (npc.getHealthPercent() != 0)
				return npc;
		return null;
	}

	/**
	 * Checks if we should attack a sand crab
	 * @return Whether or not we should attack a sand crab
	 */
	public static boolean shouldAttackSandCrab() {
		return Player.getRSPlayer().getInteractingCharacter() == null && sandCrabsNearby();
	}
	
	/**
	 * Attacks a nearby sand crab in a 3x3 box around the player. Waits until the player is under attack or for around 1200-1800 ms, whichever comes first.
	 */
	public static boolean attackSandCrab() {
		RSNPC crab = SandCrabUtility.getNearbySandCrab();
		if (crab != null)
			if (crab.click("Attack")) {
				if (Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(100);
						return Combat.isUnderAttack();
					}
				}, General.random(1200, 1800))) 
					return true;
			}
		return false;
	}
	
	/**
	 * @return Sandicrahb - The NPC who can take you to Crabclaw Island.
	 */
	public static RSNPC getSandicrahbZeah() {
		RSNPC[] sandi = NPCs.find(SANDICRAHB_ZEAH_ID);
		if (sandi.length > 0 && sandi[0] != null)
			return sandi[0];
		return null;
	}
	
	/**
	 * @return Sandicrahb - The NPC who can take you back from Crabclaw Island.
	 */
	public static RSNPC getSandicrahbIsland() {
		RSNPC[] sandi = NPCs.find(SANDICRAHB_ISLAND_ID);
		if (sandi.length > 0 && sandi[0] != null)
			return sandi[0];
		return null;
	}
	
	/**
	 * @return Whether or not Sandicrahb(Zeah) is within 15 tiles of the player.
	 */
	public static boolean nearSandicrahbZeah() {
		RSNPC sandi = getSandicrahbZeah();
		if (sandi != null && sandi.getPosition().distanceTo(Player.getPosition()) < 15)
			return true;
		return false;
	}
	
	/**
	 * @return Whether or not Sandicrahb(Island) is within 10 tiles of the player.
	 */
	public static boolean nearSandicrahbIsland() {
		RSNPC sandi = getSandicrahbIsland();
		if (sandi != null && sandi.getPosition().distanceTo(Player.getPosition()) < 10)
			return true;
		return false;
	}
	
	/**
	 * Takes Sandicrahb's boat to or from Zeah and Crabclaw Island
	 * @param alreadyOnIsland whether or not we are already on Crabclaw Island
	 * @param aCamera Camera to adjust to the tile if not on screen
	 * @return True if we successfully take the boat, false if we don't.
	 */
	public static boolean takeBoat(boolean alreadyOnIsland, ACamera aCamera) {
		RSNPC sandi = alreadyOnIsland ? getSandicrahbIsland() : getSandicrahbZeah();
		if (sandi != null) {
			if (!sandi.isOnScreen() && !sandi.isClickable())
				aCamera.adjustAngleToTile(sandi.getPosition());
			else if (sandi.isOnScreen() && sandi.isClickable() && DynamicClicking.clickRSNPC(sandi, "Quick-travel")) {
				if (Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(100);
						return alreadyOnIsland ? nearSandicrahbZeah() : nearSandicrahbIsland();
					}
				}, General.random(10000, 15000))) 
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Searches for the bank chest by sand crabs on Zeah.
	 * @return The bank chest by sand crabs on Zeah.
	 */
	public static RSObject getBankChest() {
		RSObject[] chest = Objects.find(10, CHEST_ID);
		if (chest.length > 0 && chest[0] != null)
			return chest[0];
		return null;
	}
	
	/**
	 * Checks if we are near the bank chest by sand crabs on Zeah.
	 * @return True if we are near the bank chest, false if we are not.
	 */
	public static boolean nearBankChest() {
		RSObject chest = getBankChest();
		if (chest != null)
			return true;
		return false;
	}
	
	public static void moveToBankChest() {
		moveTo(CHEST_TILE);
	}
	
	public static void moveToBoatZeah() {
		moveTo(BOAT_TILE_ZEAH);
	}
	
	public static void moveToBoatIsland() {
		moveTo(BOAT_TILE_ISLAND);
	}
	
	/**
	 * Moves to the specified tile using Dax's webwalker.
	 * @param tile The RSTile to move to.
	 */
	public static void moveTo(RSTile tile) {
		WebWalker.walkTo(tile);
		Timing.waitCondition(new Condition() {
			@Override
			public boolean active() {
				General.sleep(100);
				return Player.getPosition().distanceTo(tile) < 5 || !Player.isMoving();
			}
		}, General.random(2000, 2800));
	}
	
	/**
	 * Checks if there is a player standing in the same location as us.
	 * @return True if there is a player under us, false if there isn't.
	 */
	public static boolean isPlayerUnderUs() {
		for (RSPlayer p : Players.getAll()) 
			if (p.getPosition().equals(Player.getPosition()))
				return true;
		
		return false;
	}
	
	/**
	 * Finds all players standing under us.
	 * @return An RSPlayer array of all players standing under us.
	 */
	public static RSPlayer[] getPlayersUnderUs() {
		ArrayList<RSPlayer> players = new ArrayList<RSPlayer>();
		for (RSPlayer p : Players.getAll()) 
			if (p.getPosition().equals(Player.getPosition()))
				players.add(p);
		
		return players.toArray(new RSPlayer[players.size()]);
	}

}
