package scripts.castlewarsv2.actions;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;

import scripts.acamera.ACamera;
import scripts.castlewarsv2.antiban.Antiban;
import scripts.castlewarsv2.api.CastleWars;

public class InGame extends Task {
	
	private final boolean sleepBeforeActions;
	
	private long nextActionTime;

	public InGame(ACamera aCamera, boolean sleepBeforeActions) {
		super(aCamera);
		this.sleepBeforeActions = sleepBeforeActions;
	}

	@Override
	public boolean validate() {
		return CastleWars.inGame();
	}

	@Override
	public void execute() {
		Antiban.get().sleepBeforeNextAction = true;
		switch (Player.getPosition().getPlane()) {
		case 1:
			floorOne();
			break;
		case 2:
			floorTwo();
			break;
		default:
			break;
		}
	}

	@Override
	public String status() {
		return "In game, afk'ing..";
	}
	
	private void floorOne() {
		
		// sleep before using later to be "afk"
		if (Antiban.get().sleepBeforeNextAction) {
			
			if (this.sleepBeforeActions) 
				Antiban.get().sleepBeforeClimbingLadder();
			
			else
				General.sleep(Antiban.get().generateReactionTime());
			
			Antiban.get().sleepBeforeNextAction = false;
			
		}
		
		// climb up ladder
		RSObject ladder = CastleWars.getLadder();
		if (ladder != null) {
			if (ladder.isClickable() && ladder.click("Climb")) {
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(100);
						return Player.getPosition().getPlane() == 2;
					}
				}, General.random(3500, 6000));
			} else
				aCamera.turnToTile(ladder);
		}
	}
	
	private void floorTwo() {
		// perform random actions so we don't log out from being afk (custom "antiban")
		if (nextActionTime == 0)
			nextActionTime = (Timing.currentTimeMillis() + General.random(45000, 170000));
		else if (Timing.currentTimeMillis() > nextActionTime) {
			nextActionTime = (Timing.currentTimeMillis() + General.random(45000, 170000));
			Antiban.get().performRandomTimedAction();
		}
		else if (Mouse.isInBounds())
			Mouse.leaveGame();
		else
			General.sleep(100,300);

		Antiban.get().sleepBeforeNextAction = true;
	}
	

}
