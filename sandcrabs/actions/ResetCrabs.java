package scripts.crabs.actions;

import org.tribot.api2007.Player;

import scripts.acamera.ACamera;
import scripts.crabs.data.Variables;

/**
 * 
 * @author Nate
 *
 */
public class ResetCrabs extends Action {

	public ResetCrabs(ACamera aCamera) {
		super(aCamera);
	}

	@Override
	public boolean validate() {
		return Variables.get().shouldReset;
	}

	@Override
	public void execute() {
		Variables.get().positionHandler.moveToResetTile();
		
		// If we are within 5 tiles from the reset tile then we have successfully reset aggro.
		if (Player.getPosition().distanceTo(Variables.get().positionHandler.getResetTile()) < 5)
			Variables.get().shouldReset = false;
	}

	@Override
	public String info() {
		return "Resetting";
	}

}
