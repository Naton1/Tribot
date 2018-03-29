package scripts.crabs.utils;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSTile;

import scripts.webwalker_logic.WebWalker;

/**
 * Deals solely with the crab tile and reset tile.
 * @author Nate
 *
 */

public class PositionHandler {
	
	private RSTile crabTile;
	
	private RSTile resetTile;
	
	public PositionHandler(RSTile crabTile, RSTile resetTile) {
		this.crabTile = crabTile;
		this.resetTile = resetTile;
	}

	public RSTile getResetTile() {
		return resetTile;
	}

	public void setResetTile(RSTile resetTile) {
		this.resetTile = resetTile;
	}

	public RSTile getCrabTile() {
		return crabTile;
	}

	public void setCrabTile(RSTile crabTile) {
		this.crabTile = crabTile;
	}
	
	/**
	 * Checks whether the player is at the set crab tile.
	 * @return Whether or not the player is at the crab tile.
	 */
	public boolean atCrabTile() {
		return (Player.getPosition().equals(getCrabTile()));
	}
	
	public void moveNearCrabTile() {
		WebWalker.walkTo(crabTile);
	}
	
	public void moveToCrabTile() {
		if (!getCrabTile().isOnScreen() || !getCrabTile().isClickable())
			Camera.turnToTile(getCrabTile());
		else if (getCrabTile().isOnScreen() && getCrabTile().isClickable()) {
			if (DynamicClicking.clickRSTile(getCrabTile(), "Walk here")) {
				// Wait until we start moving
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(100);
						return Player.isMoving();
					}
				}, General.random(1000, 2000));
				// Then wait until we are no longer moving
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(100);
						return !Player.isMoving();
					}
				}, General.random(4000, 7000));
			}
		}
	}
	
	public void moveToResetTile() {
		WebWalker.walkTo(resetTile);
	}

}
