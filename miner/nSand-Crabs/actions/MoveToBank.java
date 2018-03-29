package scripts.crabs.actions;

import org.tribot.api2007.Player;

import scripts.acamera.ACamera;
import scripts.crabs.data.Variables;
import scripts.crabs.utils.SandCrabUtility;

/**
 * 
 * @author Nate
 *
 */
public class MoveToBank extends Action {

	public MoveToBank(ACamera aCamera) {
		super(aCamera);
	}

	@Override
	public boolean validate() {
		
		// We need to move to the bank if we are not currently at the bank AND
		if (!SandCrabUtility.nearBankChest()) {
			
			// We are using food and have no food and low HP,
			if (Variables.get().useFood && !Variables.get().foodHandler.haveFood())
				return true;
			// Or if we are only using potions and have no potions left and need to drink one
			else if (!Variables.get().useFood && Variables.get().drinkPotions && !Variables.get().potionHandler.havePotions() && Variables.get().potionHandler.shouldDrinkPotion())
				return true;
			
			// Otherwise, we do not need to move to the bank.
			return false;
		}
		return false;

	}

	@Override
	public void execute() {
		
		/* To prevent the scenario that we try to reset aggression as soon as we come back because we
		 * ran out of food at the same time that we lost aggression.
		 */
		Variables.get().shouldReset = false;
		
		switch (getSubTask()) {
		case MOVE_TO_BANK:
			SandCrabUtility.moveToBankChest();
			break;
		case MOVE_TO_BOAT:
			SandCrabUtility.moveToBoatIsland();
			break;
		case TAKE_BOAT:
			SandCrabUtility.takeBoat(true, aCamera);
			break;
		}
	}

	@Override
	public String info() {
		return "Moving to bank";
	}
	
	enum SubTask {
		MOVE_TO_BOAT,
		TAKE_BOAT,
		MOVE_TO_BANK;
	}
	
	public SubTask getSubTask() {
		// If we are using Crabclaw Island and we are on Crabclaw Island and we are not near Sandicrahb
		if (Variables.get().crabclawIsland && SandCrabUtility.SANDCRAB_ISLAND.contains(Player.getPosition()) && !SandCrabUtility.nearSandicrahbIsland())
			return SubTask.MOVE_TO_BOAT;
		// If we are using Crabclaw Island and we are on Crabclaw Island and we are near Sandicrahb
		else if (Variables.get().crabclawIsland && SandCrabUtility.SANDCRAB_ISLAND.contains(Player.getPosition()) && SandCrabUtility.nearSandicrahbIsland())
			return SubTask.TAKE_BOAT;
		// Otherwise, we must move directly to the bank.
		else
			return SubTask.MOVE_TO_BANK;
	}

}
