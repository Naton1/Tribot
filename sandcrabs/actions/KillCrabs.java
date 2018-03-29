package scripts.crabs.actions;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Combat;

import scripts.acamera.ACamera;
import scripts.crabs.data.Variables;
import scripts.crabs.utils.SandCrabUtility;

/**
 * 
 * @author Nate
 *
 */
public class KillCrabs extends Action {

	// Used to determine when to perform the next action to prevent us from logging out if using AFK mode.
	private long nextActionTime;
	
	public KillCrabs(ACamera aCamera) {
		super(aCamera);
		// Generate initial HP value to eat at
		Variables.get().eatAtHP = Variables.get().ab.getABCUtil().generateEatAtHP() * Combat.getMaxHP() / 100;
	}

	// This got sort of messy due adding in functionality for people only drinking potions who want to bank for more potions when they run out
	@Override
	public boolean validate() {
		
		// We are set to kill crabs if we are at the crab tile and we do not need to reset aggro AND 
		if (Variables.get().positionHandler.atCrabTile() && !Variables.get().shouldReset) {
			
			// We are using food and we have food
			if (Variables.get().useFood && Variables.get().foodHandler.haveFood())
				return true;
			// Or if we are only using potions and we still have potions remaining and we do not have low HP
			else if (!Variables.get().useFood && Variables.get().drinkPotions
					&& (Variables.get().potionHandler.havePotions() || !Variables.get().potionHandler.shouldDrinkPotion())
					&& Combat.getHP() > Variables.get().eatAtHP)
				return true;
			// Or if we are not using potions or food and we still have HP
			else if (!Variables.get().useFood && !Variables.get().drinkPotions && Combat.getHP() > Variables.get().eatAtHP)
				return true;
			
			// Otherwise, we are not ready to kill crabs
			else
				return false;
		}
		return false;

	}

	@Override
	public void execute() {
		switch (getSubTask()) {
		case AFK:
			
			// Check if we should attack a sand crab (Only if not in afk mode)
			if (!Variables.get().afkMode && SandCrabUtility.shouldAttackSandCrab()) {
				General.sleep(Variables.get().ab.generateReactionTime());
				SandCrabUtility.attackSandCrab();
			}

			// Antiban
			Variables.get().ab.performTimedActions();
				
			// Check aggro
			if (SandCrabUtility.checkAggro()) {
				Variables.get().shouldReset = true;
				General.sleep(Variables.get().ab.generateReactionTime()); // Generate reaction time because the next action will be resetting aggro
			}
			// Check afk mode 
			if (Variables.get().afkMode) {
				
				// Perform random action every 45 seconds - 3 minutes so that we don't log out from being afk
				
				// Check if we need to generate a new action time (in case the script was just started or we just came back from the bank)
				if (Variables.get().shouldGenerateActionTime) {
					generateNextActionTime();
					Variables.get().shouldGenerateActionTime = false;
				}
				if (Timing.currentTimeMillis() > nextActionTime) {
					performRandomAction();
					generateNextActionTime();
				}
				
				// Take mouse off screen if we are using afk mode and the mouse in in bounds and we are not about to reset aggro
				if (Mouse.isInBounds() && !Variables.get().shouldReset)
					Mouse.leaveGame();
			}
			
			break;
		case DRINK_POTION:
			General.sleep(Variables.get().ab.generateReactionTime());
			Variables.get().potionHandler.drinkPotion();
			Variables.get().potionHandler.generateLevelDifference();
			break;
		case EAT_FOOD:
			General.sleep(Variables.get().ab.generateReactionTime());
			Variables.get().foodHandler.eatFood();
			Variables.get().eatAtHP = Variables.get().ab.getABCUtil().generateEatAtHP() * Combat.getMaxHP() / 100;
			break;
		
		}

	}
	
	@Override
	public String info() {
		return "Killing Crabs";
	}
	
	enum SubTask {
		AFK,
		DRINK_POTION,
		EAT_FOOD;
	}
	
	private SubTask getSubTask() {
		// If our HP is less then or equal to the HP we should eat at and we have food then we should eat food.
		if (Variables.get().useFood && Combat.getHP() <= Variables.get().eatAtHP && Variables.get().foodHandler.haveFood())
			return SubTask.EAT_FOOD;
		// If we need to drink a potion and we have potions then we should drink a potion.
		else if (Variables.get().drinkPotions && Variables.get().potionHandler.shouldDrinkPotion() && Variables.get().potionHandler.havePotions())
			return SubTask.DRINK_POTION;
		// Otherwise, "afk"
		else
			return SubTask.AFK;
	}
	
	private void generateNextActionTime() {
		nextActionTime = (Timing.currentTimeMillis() + General.random(45000, 180000));
	}
	
	/**
	 * Performs a random action to prevent us from logging out. This method could be changed.
	 * However, this worked perfectly for my Castlewars AFK'ing script where I would only get banned if I ran it 50+ hours straight.
	 */
	private void performRandomAction() {
		int random = General.random(1, 100);
		if (random > 70) 
			Variables.get().ab.getABCUtil().rightClick();
		else if (random > 40) {
			Variables.get().ab.getABCUtil().checkXP();
			General.sleep(1200, 3000); // Pause so you can actaully "see" the exp.
		}
		else if (random > 10)
			Variables.get().ab.getABCUtil().rotateCamera();
		else
			Variables.get().ab.getABCUtil().examineEntity();
	}


}
