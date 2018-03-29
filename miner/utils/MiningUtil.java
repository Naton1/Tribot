package scripts.miner.utils;

import org.tribot.api.General;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.miner.data.MiningConstants;
import scripts.napi.NTiming;

public class MiningUtil {
	
	public static boolean havePickaxe() {
		return Inventory.getCount(MiningConstants.PICKAXES) > 0 || wearingPickaxe();
	}
	
	public static boolean wearingPickaxe() {
		
		String currentWeapon = Interfaces.get(593,1).getText();
		
		for (String pickaxe : MiningConstants.PICKAXES) 
			if (pickaxe.equals(currentWeapon))
				return true;
		
		return false;
		
	}
	
	public static boolean nearTile(RSTile tile) {
		return Player.getPosition().distanceTo(tile) <= 15;
	}
	
	public static boolean inventoryEmptyBesidesPickaxe() {
		return Inventory.getAll().length - Inventory.getCount(MiningConstants.PICKAXES) == 0;	
	}
	
	public static int[] getRockIDs(String rockName) {
		return MiningConstants.ROCKS.valueOf(rockName.toUpperCase()).getIds();
	}
	
	public static boolean isRockValid(RSObject rock) {
		
		if (rock == null)
			return false;
		
		RSTile rockTile = rock.getPosition();
		
		Filter<RSObject> filter = Filters.Objects.tileEquals(rockTile);
		
		RSObject[] rocks = Objects.find(15, filter);
		
		for (RSObject object : rocks) 
			for (int id : MiningConstants.ROCKS.EMPTY.getIds()) 
				if (id == object.getID())
					return false;
		
		return true;
	}
	
	// TY Einstein
	public static void waitToStartAnimating(Positionable target) {
		
		if (Player.getPosition().distanceTo(target) > 1) {
			// Wait to stop walking
			NTiming.waitCondition(() -> !Player.isMoving() || !isRockValid((RSObject)target), General.random(4000, 6000));
		}
		
		//Wait to start animating
		NTiming.waitCondition(() -> isMining() || !isRockValid((RSObject)target), General.random(2000, 3000));
		
	}
	
	public static boolean rocksAvailable(int[] rockIds) {
		RSObject[] rocks = getAvailableRocks(rockIds);
		return rocks != null && rocks.length > 0;
	}
	
	public static RSObject[] getAvailableRocks(int[] rocks) {
		return org.tribot.api2007.Objects.findNearest(15, rocks);
	}
	
	public static boolean hasRockBeenStolen() {
		
		RSObject interactingRock = getInteractingRock();
		
		if (interactingRock == null)
			return false;
		
		for (int i : MiningConstants.ROCKS.EMPTY.getIds())
			if (i == interactingRock.getID())
				return true;
		
		return false;
		
	}
	
    private static final int NORTH = 1023;
    private static final int EAST = 1537;
    private static final int SOUTH = 0;
    private static final int WEST = 511;
	
	public static RSObject getInteractingRock() {
		
		if (!isMining())
			return null;
		
		RSTile objectTile = Player.getPosition();
		
		switch (Player.getRSPlayer().getOrientation()) {
		case EAST:
			objectTile = objectTile.translate(1, 0);
			break;
		case NORTH:
			objectTile = objectTile.translate(0, 1);
			break;
		case SOUTH:
			objectTile = objectTile.translate(0, -1);
			break;
		case WEST:
			objectTile = objectTile.translate(-1, 0);
			break;
		default:
			return null;
		}
		
		Filter<RSObject> filter = Filters.Objects.tileEquals(objectTile);
		
		RSObject[] foundObjects = Objects.findNearest(5, filter);
		
		RSObject rock = getObjectWithName(foundObjects, "Rocks");

		return rock;
	}
	
	public static RSObject getObjectWithName(RSObject[] objects, String name) {
		try {
			for (RSObject object : objects) 
				if (object.getDefinition().getName().equals(name))
					return object;
		}
		catch (NullPointerException e) {
		}
		return null;
	}
	
	public static boolean areRocksEqual(RSObject rock1, RSObject rock2) {
		if (rock1 == null || rock2 == null)
			return false;
		
		return rock1.getPosition().equals(rock2.getPosition());
	}
	
	public static boolean isMining() {
		return Player.getAnimation() == MiningConstants.MINING_ANIMATION;
	}
	

}
