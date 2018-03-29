package scripts.crabs.actions;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;

import scripts.acamera.ACamera;
import scripts.crabs.data.Variables;
import scripts.crabs.utils.SandCrabUtility;

/**
 * 
 * @author Nate
 *
 */
public class UseBank extends Action {
	
	public static final int VIAL_ID = 229;
	public static final int COINS_ID = 995;

	public UseBank(ACamera aCamera) {
		super(aCamera);
	}

	@Override
	public boolean validate() {
		// We need to bank if we are currently at the bank AND
		if (SandCrabUtility.nearBankChest()) {
			
			// We are using food and have no food,
			if (Variables.get().useFood && !Variables.get().foodHandler.haveFood())
				return true;
			// Or if we are only using potions and have no potions left and need to drink one
			else if (!Variables.get().useFood && Variables.get().drinkPotions && !Variables.get().potionHandler.havePotions() && Variables.get().potionHandler.shouldDrinkPotion())
				return true;
			
			// Otherwise, we do not need to bank.
			return false;
		}
		return false;

	}

	@Override
	public void execute() {
		switch (getSubTask()) {
		case CHECK_FOR_ITEMS:
			// Check if we have enough potions
			if (Variables.get().drinkPotions) {
				RSItem potions = Banking.find(Variables.get().potionHandler.getFullPotionID())[0];
				if (potions == null || potions.getStack() < Variables.get().potionQuantity) {
					General.println("We have ran out of potions");
					Variables.get().conditionMet = true;
					return;
				}
			}
			
			// Check if we have enough food
			if (Variables.get().useFood) {
				RSItem food = Banking.find(Variables.get().foodID)[0];
				if (food == null || food.getStack() < Variables.get().foodQuantity) {
					General.println("We have ran out of food");
					Variables.get().conditionMet = true;
					return;
				}
			}
			
			// Check if we have enough gold for Crabclaw Island
			if (Variables.get().crabclawIsland) {
				RSItem coins = Banking.find(COINS_ID)[0];
				if (coins == null || coins.getStack() < 10000) {
					General.println("We have ran out of coins");
					Variables.get().conditionMet = true;
					return;
				}
			}
			
			Variables.get().checkBank = false;
			break;
		case DEPOSIT_POTIONS:
			for (int i : Variables.get().potionHandler.getPotionIDs())
				if (i != Variables.get().potionHandler.getFullPotionID())
					if (Inventory.getCount(i) > 0)
						if (Banking.deposit(0, i))
							Timing.waitCondition(new Condition() {
								@Override
								public boolean active() {
									General.sleep(100);
									return Inventory.getCount(i) == 0;
								}
							}, General.random(1200, 2000));
			break;
		case DEPOSIT_VIALS:
			Banking.deposit(0, VIAL_ID);
			Timing.waitCondition(new Condition() {
				@Override
				public boolean active() {
					General.sleep(100);
					return Inventory.getCount(VIAL_ID) == 0;
				}
			}, General.random(1200, 2000));
			break;
		case WITHDRAW_ITEMS:
			// Withdraw correct amount of potions
			if (Variables.get().drinkPotions) {
				Banking.withdraw(Variables.get().potionQuantity - Inventory.getCount(Variables.get().potionHandler.getFullPotionID()), Variables.get().potionHandler.getFullPotionID());
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(100);
						return Inventory.getCount(Variables.get().potionHandler.getFullPotionID()) == Variables.get().potionQuantity;
					}
				}, General.random(1200, 2000));
			}
			
			// Withdraw correct amount of food
			if (Variables.get().useFood) {
				Banking.withdraw(Variables.get().foodQuantity - Inventory.getCount(Variables.get().foodID), Variables.get().foodID);
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(100);
						return Inventory.getCount(Variables.get().foodID) == Variables.get().foodQuantity;
					}
				}, General.random(1200, 2000));
			}
			
			// If we are using Crabclaw Island, then we need to have gp in our inventory.
			
			if (Variables.get().crabclawIsland) {
				if (Inventory.getCount(COINS_ID) < 10000)
					Banking.withdraw(10000, COINS_ID);
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100);
							return Inventory.getCount(COINS_ID) >= 10000;
						}
					}, General.random(1200, 2000));
			}
			
			break;
		case OPEN_BANK:
			RSObject o = SandCrabUtility.getBankChest();
			if (o != null) {
				o.click("Use");
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(100);
						return Banking.isBankScreenOpen();
					}
				}, General.random(3000, 4000));
			}
			break;
		}
	}

	@Override
	public String info() {
		return "Banking";
	}
	
	enum SubTask {
		OPEN_BANK,
		DEPOSIT_POTIONS,
		DEPOSIT_VIALS,
		CHECK_FOR_ITEMS,
		WITHDRAW_ITEMS;
	}
	
	public SubTask getSubTask() {
		if (!Banking.isBankScreenOpen())
			return SubTask.OPEN_BANK;
		else if (Inventory.getCount(VIAL_ID) > 0)
			return SubTask.DEPOSIT_VIALS;
		else if (Variables.get().potionHandler.haveNonFullPotions())
			return SubTask.DEPOSIT_POTIONS;
		else if (Variables.get().checkBank)
			return SubTask.CHECK_FOR_ITEMS;
		else
			return SubTask.WITHDRAW_ITEMS;
	}

}
