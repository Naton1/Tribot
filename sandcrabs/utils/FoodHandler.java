package scripts.crabs.utils;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;

import scripts.crabs.data.Variables;

/**
 *
 * @author Nate
 *
 */
public class FoodHandler {
	
	private int foodID;
	
	public FoodHandler(int foodID) {
		this.foodID = foodID;
	}

	public int getFood() {
		return foodID;
	}

	public void setFood(int foodID) {
		this.foodID = foodID;
	}
	
	/**
	 * Checks if we have any of the set food.
	 * @return Whether or not we have any of the set food.
	 */
	public boolean haveFood() {
		for (RSItem item : Inventory.getAll())
			if (item.getID() == foodID)
				return true;
		return false;
	}
	
	/**
	 * Eats the set food.
	 * @return Whether or not we successfully ate the food.
	 */
	public boolean eatFood() {
		
		Variables.get().ateFood = false;

		for (RSItem item : Inventory.getAll())
			if (foodID == item.getID() && item.click("Eat") && Timing.waitCondition(new Condition() {
				@Override
				public boolean active() {
					General.sleep(100);
					return Variables.get().ateFood; // This will return true when a server message is received saying
													// the food was eaten
				}
			}, General.random(1200, 1800))) {
				General.sleep(150, 300);
				return true;
			}

		return false;

	}

}

