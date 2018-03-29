package scripts.miner.api;

import java.util.function.BooleanSupplier;

import org.tribot.api2007.types.RSTile;

import scripts.acamera.ACamera;
import scripts.webwalker_logic.WebWalker;

public class MovingAction extends PriorityAction {
	
	private final BooleanSupplier validateCondition;
	private final RSTile tile;
	
	public MovingAction(int priority, ACamera aCamera, BooleanSupplier validateCondition, RSTile tile) {
		super(priority, aCamera);
		this.validateCondition = validateCondition;
		this.tile = tile;
	}

	@Override
	public boolean validate() {
		return this.validateCondition.getAsBoolean();
	}

	@Override
	public void execute() {
		WebWalker.walkTo(this.tile);
	}

	@Override
	public String toString() {
		return "Travelling";
	}

}
