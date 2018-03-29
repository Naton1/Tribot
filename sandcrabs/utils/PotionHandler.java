package scripts.crabs.utils;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSItem;

import scripts.crabs.data.Variables;

/**
 * Handles actions and data about potions. Used for nSand Crabs
 * @author Nate
 *
 */

public class PotionHandler {
	
	private PotionType potion;
	
	// The threshold between current level and actual level that when reached, a potion should be drank. Could be changed in the future
	private int levelDifference = 0;
	
	public PotionHandler() {
		generateLevelDifference();
	}
	
	public PotionHandler(PotionType potion) {
		this();
		this.potion = potion;
	}
	
	public void setPotion(PotionType potion) {
		this.potion = potion;
	}

	public PotionType getPotion() {
		return this.potion;
	}
	
	public void setLevelDifference(int levelDifference) {
		this.levelDifference = levelDifference;
	}
	
	public int getLevelDifference() {
		return this.levelDifference;
	}
	
	
	/**
	 * Gets the potion IDs based off of the set potion.
	 * @return The potion IDs of the set potion.
	 */
	public int[] getPotionIDs() {
		return this.potion.getIDs();
	}
	
	/**
	 * Gets the full potion ID of the set potion
	 * @return The full 4 dose potion ID of the set potion.
	 */
	public int getFullPotionID() {
		return this.potion.getIDs()[0];
	}
	
	/**
	 * Checks if we have any non full potions in our inventory, of the set type of potion.
	 * @return Whether or not we have any non full potions in our inventory.
	 */
	public boolean haveNonFullPotions() {
		for (int i : getPotionIDs())
			if (i != getFullPotionID())
				if (Inventory.getCount(i) > 0)
					return true;
		return false;
	}
	
	public void generateLevelDifference() {
		levelDifference = General.random(0, 4);
	}

	
	/**
	 * Determines whether or not it is time to drink the set potion.
	 * 
	 * @return If the set potion should be drank.
	 */
	public boolean shouldDrinkPotion() {
		return Skills.getCurrentLevel(this.potion.getCorrespondingSkill()) - Skills.getActualLevel(this.potion.getCorrespondingSkill()) <= levelDifference;
	}
	
	/**
	 * Checks to see if there is any potions of the set potion type in our inventory.
	 * @return If we have any potions of the set potion type.
	 */
	public boolean havePotions() {
		for (RSItem item : Inventory.getAll())
			for (int i : getPotionIDs())
				if (i == item.getID())
					return true;
		return false;

	}
	
	/**
	 * Drinks the set potion.
	 */
	public boolean drinkPotion() {
		
		Variables.get().drankPotion = false;
		
		for (RSItem item : Inventory.getAll())
			for (int i : getPotionIDs())
				if (i == item.getID())
					if (item.click("Drink")) {
						if (Timing.waitCondition(new Condition() {
							@Override
							public boolean active() {
								General.sleep(100);
								return Variables.get().drankPotion; // This will return true when a server message is received saying the potion was drank
							}
						}, General.random(1200, 1800))) {
							General.sleep(150, 300);
						}
						return true;
					}
		return false;

	}
	
	/**
	 * Gets the next level to drink a potion at.
	 * @return The level at which another potion should be drank.
	 */
	public int getNextDrinkLevel() {
		return Skills.getActualLevel(this.potion.getCorrespondingSkill()) + levelDifference;
	}
	
	
	public enum PotionType {
		COMBAT_POTION {
			@Override
			public int[] getIDs() {
				return new int[] {9739, 9741, 9743, 9745};
			}

			@Override
			public SKILLS getCorrespondingSkill() {
				return Skills.SKILLS.STRENGTH;
			}
		},
		SUPER_COMBAT_POTION {
			@Override
			public int[] getIDs() {
				return new int[] {12695, 12697, 12699, 12701};
			}

			@Override
			public SKILLS getCorrespondingSkill() {
				return Skills.SKILLS.STRENGTH;
			}
		},
		RANGING_POTION {
			@Override
			public int[] getIDs() {
				return new int[] {2444, 169, 171, 173};
			}

			@Override
			public SKILLS getCorrespondingSkill() {
				return Skills.SKILLS.RANGED;
			}
		},
		SUPER_STRENGTH_POTION {
			@Override
			public int[] getIDs() {
				return new int[] {2440, 157, 159, 161};
			}

			@Override
			public SKILLS getCorrespondingSkill() {
				return Skills.SKILLS.STRENGTH;
			}
		};
		
		/**
		 * Gets the potion ID's of the set potion.
		 * @return the potion ID's in the form of {4 dose, 3 dose, 2 dose, 1 dose}
		 */
		public abstract int[] getIDs();
		
		/**
		 * Gets the corresponding skill of the potion.
		 * @return the correspoding skill to the potion.
		 */
		public abstract Skills.SKILLS getCorrespondingSkill();
	}

}
