package scripts.castlewarsv2.actions;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;

import scripts.acamera.ACamera;
import scripts.castlewarsv2.antiban.Antiban;
import scripts.castlewarsv2.api.CastleWars;
import scripts.napi.NTiming;

public class InWaitingRoom extends Task {
	
	private final boolean sleepBeforeActions;
	
	private long nextActionTime;

	public InWaitingRoom(ACamera aCamera, boolean sleepBeforeActions) {
		super(aCamera);
		this.sleepBeforeActions = sleepBeforeActions;
	}

	@Override
	public boolean validate() {
		return CastleWars.inWaitingRoom();
	}

	@Override
	public void execute() {
		// we need to sleep before entering the ladder when we arrive in game
		Antiban.get().sleepBeforeNextAction = true;
		
		// check if we can enter a game that is already in progress that will still provide tickets
		if (CastleWars.canEnterStartedGame()) {
			if (this.sleepBeforeActions)
				General.sleep(Antiban.get().generateReactionTime());
			Antiban.get().sleepBeforeNextAction = false;
			if (CastleWars.enterStartedGame()) 
				NTiming.waitCondition(() -> !CastleWars.inWaitingRoom(), General.random(2500, 3500));
		}
		
		// otherwise perform random actions occasionally so we don't log out
		else if (nextActionTime == 0) {
			nextActionTime = (Timing.currentTimeMillis() + General.random(45000, 135000));
		}
		else if (Timing.currentTimeMillis() > nextActionTime) {
			Antiban.get().performRandomTimedAction();
			nextActionTime = (Timing.currentTimeMillis() + General.random(45000, 135000));
		}
		else if (Mouse.isInBounds())
			Mouse.leaveGame();
		else
			General.sleep(100,150);
	}

	@Override
	public String status() {
		return "Waiting for game to start";
	}

}
