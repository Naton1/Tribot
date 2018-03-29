package scripts.crabs.actions;

import org.tribot.api.General;
import org.tribot.api2007.Combat;
import scripts.acamera.ACamera;
import scripts.crabs.data.Variables;
import scripts.crabs.utils.SandCrabUtility;

/**
 * This is used as a stop condition to check if the player has low HP and is not using food.
 * Only added into the list of tasks if food is not being used so we do not have to check for it again.
 * @author Nate
 *
 */

public class CheckForStop extends Action {

	public CheckForStop(ACamera aCamera) {
		super(aCamera);
	}

	@Override
	public boolean validate() {
		return (Variables.get().eatAtHP >= Combat.getHP());
	}

	@Override
	public void execute() {
		if (Combat.isUnderAttack()) {
			if (Variables.get().crabclawIsland)
				SandCrabUtility.moveToBoatIsland(); // Potential error if we end up attacking sand crabs while near the boat.
			else
				SandCrabUtility.moveToBankChest();
		}
		else {
			Variables.get().conditionMet = true;
			General.println("Ending script, we are low HP and not using food.");
		}
	}

	@Override
	public String info() {
		return "Ending script: Low HP and not using food";
	}

}
