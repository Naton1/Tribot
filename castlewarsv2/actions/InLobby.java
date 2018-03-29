package scripts.castlewarsv2.actions;

import org.tribot.api.General;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSObject;

import scripts.acamera.ACamera;
import scripts.castlewarsv2.antiban.Antiban;
import scripts.castlewarsv2.api.CastleWars;
import scripts.castlewarsv2.api.CastleWars.Teams;
import scripts.napi.NTiming;
import scripts.webwalker_logic.WebWalker;

public class InLobby extends Task {
	
	private final boolean sleepBeforeActions;
	
	private final Teams team;
	
	public InLobby(ACamera aCamera, boolean sleepBeforeActions, Teams team) {
		super(aCamera);
		this.sleepBeforeActions = sleepBeforeActions;
		this.team = team;
	}

	@Override
	public boolean validate() {
		return CastleWars.CASTLE_WARS_LOBBY.contains(Player.getPosition()) || CastleWars.canFindPortal(this.team);
	}

	@Override
	public void execute() {

		// sleep before entering a game to pretend we are afk
		if (Antiban.get().sleepBeforeNextAction) {
			
			if (this.sleepBeforeActions)
				General.sleep(10000, 110000);
			else
				General.sleep(Antiban.get().generateReactionTime());
			
			Antiban.get().sleepBeforeNextAction = false;
		}
		
		// then find the portal and click it
		RSObject portal = CastleWars.getTeamPortal(this.team);
		
		if (portal != null) {
			
			if (portal.isClickable() && portal.click("Enter")) 
				NTiming.waitCondition(() -> CastleWars.inWaitingRoom(), General.random(2500, 4500));
			
			else {
				
				if (Player.getPosition().distanceTo(portal) > 5)
					Walking.walkScreenPath(Walking.randomizePath(Walking.generateStraightScreenPath(CastleWars.getTeamTile(this.team)), 2, 2));
				
				else
					aCamera.turnToTile(portal);
			}
		}
		else
			WebWalker.walkTo(CastleWars.getTeamTile(this.team));
	}

	@Override
	public String status() {
		return "Joining team";
	}

}
