package scripts.miner.actions;

import org.tribot.api2007.Inventory;

import scripts.acamera.ACamera;
import scripts.miner.api.PriorityAction;
import scripts.miner.data.MiningConstants;
import scripts.miner.data.Priority;

public class DropOre extends PriorityAction {

	public DropOre(ACamera aCamera) {
		super(Priority.DROP_ORE, aCamera);
	}

	@Override
	public boolean validate() {
		return Inventory.isFull();
	}

	@Override
	public void execute() {	
		Inventory.dropAllExcept(MiningConstants.PICKAXES);
	
	}

	@Override
	public String toString() {
		return "Dropping ore";
	}

}
