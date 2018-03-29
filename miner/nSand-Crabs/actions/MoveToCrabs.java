package scripts.crabs.actions;

import org.tribot.api.General;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;

import scripts.acamera.ACamera;
import scripts.crabs.data.Variables;
import scripts.crabs.utils.SandCrabUtility;

/**
 * 
 * @author Nate
 *
 */
public class MoveToCrabs extends Action {

	public MoveToCrabs(ACamera aCamera) {
		super(aCamera);
	}

	// This got sort of messy due adding in functionality for people only drinking potions who want to bank for more potions when they run out
	@Override
	public boolean validate() {
		
		// We are set to move to crabs if we are not at the crab tile and we do not need to reset aggro AND 
		if (!Variables.get().positionHandler.atCrabTile() && !Variables.get().shouldReset) {
			
			// We are using food and we have food
			if (Variables.get().useFood && Variables.get().foodHandler.haveFood())
				return true;
			// Or if we are only using potions and we still have potions remaining and we do not have low HP
			else if (!Variables.get().useFood && Variables.get().drinkPotions
					&& (Variables.get().potionHandler.havePotions() || !Variables.get().potionHandler.shouldDrinkPotion())
					&& Combat.getHP() > Variables.get().eatAtHP)
				return true;
			// Or if we are not using potions or food and we still have HP
			else if (!Variables.get().useFood && !Variables.get().drinkPotions && Combat.getHP() > Variables.get().eatAtHP)
				return true;
			
			// Otherwise, we are not ready to move to crabs
			return false;
		}
		return false;

	}

	@Override
	public void execute() {
		switch (getSubTask()) {
		case MOVE_NEAR_CRAB_TILE:
			Variables.get().positionHandler.moveNearCrabTile();
			break;
		case MOVE_TO_BOAT:
			SandCrabUtility.moveToBoatZeah();
			break;
		case MOVE_TO_CRAB_TILE:
			// Generate a new action time so we don't instantly perform an action when we get back to the tile
			if (Variables.get().afkMode)
				Variables.get().shouldGenerateActionTime = true;
			Variables.get().positionHandler.moveToCrabTile();
			break;
		case TAKE_BOAT:
			// If have less than 10k here then something went wrong.
			if (Inventory.getCount(995) < 10000) { // 995 is the coins id
				General.println("Error: Tried to take the boat when we don't have enough coins.");
				Variables.get().conditionMet = true;
			}
			SandCrabUtility.takeBoat(false, aCamera);
		
		}
	}

	@Override
	public String info() {
		return "Moving to crabs";
	}
	
	enum SubTask {
		MOVE_NEAR_CRAB_TILE,
		MOVE_TO_BOAT,
		MOVE_TO_CRAB_TILE,
		TAKE_BOAT;
	}
	
	public SubTask getSubTask() {
		// If we are using Crabclaw Island and we are not already there and we are not near Sandicrahb then we must move to Sandicrahb
		if (Variables.get().crabclawIsland && !SandCrabUtility.SANDCRAB_ISLAND.contains(Player.getPosition()) && !SandCrabUtility.nearSandicrahbZeah())
			return SubTask.MOVE_TO_BOAT;
		// If we are using Crabclaw Island and we are not already there and we are near Sandicrahb then we must take his boat
		else if (Variables.get().crabclawIsland && !SandCrabUtility.SANDCRAB_ISLAND.contains(Player.getPosition()) && SandCrabUtility.nearSandicrahbZeah())
			return SubTask.TAKE_BOAT;
		// If we are greater then 8 tiles away then move to the tile in a general area
		else if (Player.getPosition().distanceTo(Variables.get().positionHandler.getCrabTile()) > 8)
			return SubTask.MOVE_NEAR_CRAB_TILE;
		// Otherwise, move directly to the tile.
		else
			return SubTask.MOVE_TO_CRAB_TILE;
	}

}
