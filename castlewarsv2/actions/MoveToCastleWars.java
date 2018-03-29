package scripts.castlewarsv2.actions;

import org.tribot.api2007.Player;

import scripts.acamera.ACamera;
import scripts.castlewarsv2.api.CastleWars;
import scripts.castlewarsv2.api.CastleWars.Teams;
import scripts.webwalker_logic.WebWalker;

public class MoveToCastleWars extends Task {

	private final Teams team;
	
	public MoveToCastleWars(ACamera aCamera, Teams team) {
		super(aCamera);
		this.team = team;
	}

	@Override
	public boolean validate() {
		return !CastleWars.wearingCwarsCape() && !CastleWars.CASTLE_WARS_LOBBY.contains(Player.getPosition()) && !CastleWars.canFindPortal(this.team);
	}

	@Override
	public void execute() {
		WebWalker.walkTo(CastleWars.CASTLE_WARS_LOBBY_TILE);
	}

	@Override
	public String status() {
		return "Moving to castle wars";
	}

}
