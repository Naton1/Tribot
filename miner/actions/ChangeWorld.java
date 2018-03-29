package scripts.miner.actions;

import org.tribot.api2007.WorldHopper;

import scripts.acamera.ACamera;
import scripts.miner.api.PriorityAction;
import scripts.miner.data.Priority;
import scripts.miner.data.Vars;
import scripts.worldhopping.IngameHopper;

public class ChangeWorld extends PriorityAction {
	
	private final boolean members;

	public ChangeWorld(boolean members, ACamera aCamera) {
		super(Priority.CHANGE_WORLD, aCamera);
		this.members = members;
	}

	@Override
	public boolean validate() {
		return Vars.get().needToChangeWorld;
	}

	@Override
	public void execute() {
		if (IngameHopper.hop(WorldHopper.getRandomWorld(members)))
			Vars.get().needToChangeWorld = false;
	}

	@Override
	public String toString() {
		return "Changing world";
	}

}
