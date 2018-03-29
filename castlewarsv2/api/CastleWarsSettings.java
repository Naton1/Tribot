package scripts.castlewarsv2.api;

import scripts.castlewarsv2.api.CastleWars.Teams;

public class CastleWarsSettings {
	
	private final boolean sleep;
	private final Teams team;
	
	public CastleWarsSettings(Teams team, boolean sleep) {
		this.team = team;
		this.sleep = sleep;
	}

	public Teams getTeam() {
		return team;
	}

	public boolean isSleep() {
		return sleep;
	}

}
