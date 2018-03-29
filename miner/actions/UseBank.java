package scripts.miner.actions;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;

import scripts.acamera.ACamera;
import scripts.miner.api.PriorityAction;
import scripts.miner.data.MiningConstants;
import scripts.miner.data.Priority;
import scripts.miner.utils.MiningUtil;
import scripts.napi.NTiming;

public class UseBank extends PriorityAction {

	public UseBank(ACamera aCamera) {
		super(Priority.USE_BANK, aCamera);
	}

	@Override
	public boolean validate() {
		return Inventory.isFull() && Banking.isInBank();
	}

	@Override
	public void execute() {
		switch (getSubTask()) {
		case BANK_ITEMS:
			Banking.depositAllExcept(MiningConstants.PICKAXES);
			NTiming.waitCondition(() -> MiningUtil.inventoryEmptyBesidesPickaxe(), General.randomSD(750, 75));
			break;
		case OPEN_BANK:
			Banking.openBank();
			NTiming.waitCondition(() -> Banking.isBankScreenOpen(), General.randomSD(750, 75));
			break;
		}
	}

	@Override
	public String toString() {
		return "Banking";
	}
	
	private enum SubTask {
		OPEN_BANK,
		BANK_ITEMS;
	}
	
	private SubTask getSubTask() {
		if (!Banking.isBankScreenOpen())
			return SubTask.OPEN_BANK;
		else
			return SubTask.BANK_ITEMS;
	}

}
