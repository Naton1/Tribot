package scripts.miner.api;

import java.util.function.BooleanSupplier;

import scripts.acamera.ACamera;
import scripts.webwalker_logic.WebWalker;

public class MoveToBank extends PriorityAction {

	private final BooleanSupplier validateCondition;
	
	public MoveToBank(int priority, ACamera aCamera, BooleanSupplier validateCondition) {
		super(priority, aCamera);
		this.validateCondition = validateCondition;
	}

	@Override
	public boolean validate() {
		return this.validateCondition.getAsBoolean();
	}

	@Override
	public void execute() {
		WebWalker.walkToBank();
	}

	@Override
	public String toString() {
		return "Moving to bank";
	}



}
