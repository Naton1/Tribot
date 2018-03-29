package scripts.miner.actions;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.acamera.ACamera;
import scripts.miner.antiban.NMinerAntiban;
import scripts.miner.api.PriorityAction;
import scripts.miner.data.Priority;
import scripts.miner.data.Vars;
import scripts.miner.utils.MiningUtil;

public class MineRocks extends PriorityAction {

	private final boolean hopWorlds;
	private final RSTile miningTile;
	private final int[] rockIds;
	
	private RSObject target;
	private boolean changedTarget;

	public MineRocks(ACamera aCamera, boolean hopWorlds, RSTile miningTile, int[] rockIds) {
		super(Priority.MINE_ROCKS, aCamera);
		this.hopWorlds = hopWorlds;
		this.miningTile = miningTile;
		this.rockIds = rockIds;
	}

	@Override
	public boolean validate() {
		return !Inventory.isFull() && MiningUtil.nearTile(this.miningTile);
	}

	@Override
	public void execute() {
		
		switch (getSubTask()) {
		
		case MINE_ROCK:
			
			if (MiningUtil.isRockValid(target))
				if (clickRock(target, changedTarget)) {
					
					// Antiban stuff
					NMinerAntiban.get().setLastObjectHoverAndMenu();
					NMinerAntiban.get().setHoverAndMenuOpenBooleans();
					NMinerAntiban.get().generateSupportingTrackerInfo();
					NMinerAntiban.get().shouldAbcSleep = true;
					
					// Wait till we start mining
					MiningUtil.waitToStartAnimating(target);
				}
			
			
			else {
				target = NMinerAntiban.get().getNewTarget(target, rockIds);
				changedTarget = true;
				
				General.sleep(250, 400);
			}
			
			break;
		case MINING:
			
			NMinerAntiban.get().startMiningTime = Timing.currentTimeMillis();
			
			changedTarget = false;
			target = NMinerAntiban.get().getNewTarget(target, rockIds);
			
			// Only hover if our inventory is not almost full
			if (target != null && Inventory.getAll().length != 27 && Mouse.isInBounds())
				NMinerAntiban.get().executeHoverOrMenuOpen(target);
			
			while (MiningUtil.isMining() && !MiningUtil.hasRockBeenStolen()) {
				General.sleep(250, 400);
				
				// If we are not hovering then we can perform timed actions
				if (!NMinerAntiban.get().isHovering())
					NMinerAntiban.get().performTimedActions();
			}
			
			NMinerAntiban.get().updateMiningStatistics();
			
			NMinerAntiban.get().abcSleepAfterMining();
			
			break;
		case WAIT:
			
			if (this.hopWorlds) {
				Vars.get().needToChangeWorld = true;
				return;
			}
			
			long startIdleTime = Timing.currentTimeMillis();
			
			while (!MiningUtil.rocksAvailable(rockIds)) {
				NMinerAntiban.get().performTimedActions();
				General.sleep(250, 400);
			}
			
			long stopIdleTime = Timing.currentTimeMillis();
			
			NMinerAntiban.get().sleepReactionTime((int)(stopIdleTime - startIdleTime));
			
			break;
		}
	}

	@Override
	public String toString() {
		return "Mining rocks";
	}
	
	enum SubTask {
		MINING,
		MINE_ROCK,
		WAIT;
	}

	private SubTask getSubTask() {
		if (MiningUtil.isMining() && !MiningUtil.hasRockBeenStolen())
			return SubTask.MINING;
		else if (MiningUtil.rocksAvailable(rockIds))
			return SubTask.MINE_ROCK;
		else
			return SubTask.WAIT;
	}
	
	private boolean clickRock(RSObject target, boolean changedTarget) {

		if (target == null)
			return false;

		if (ChooseOption.isOpen() && !changedTarget)
			return ChooseOption.select("Mine");

		if (!target.isOnScreen()) {
			if (Player.getPosition().distanceTo(target) < General.random(8, 12))
				aCamera.turnToTile(target);
			else
				Walking.blindWalkTo(target);
		}

		return target.click("Mine");
	}

}
