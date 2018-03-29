package scripts.crabs.data;

import java.util.Properties;

import org.tribot.api2007.types.RSTile;

import scripts.crabs.antiban.Antiban;
import scripts.crabs.paint.Paint;
import scripts.crabs.paint.Paint.Tabs;
import scripts.crabs.utils.FoodHandler;
import scripts.crabs.utils.PositionHandler;
import scripts.crabs.utils.PotionHandler;

public class Variables {
	
	// instance stuff
	
	private static Variables vars;
	
	public static Variables get() {
		return vars == null? vars = new Variables() : vars;
	}
	
	public static void reset() {
		vars = new Variables();
	}
	
	// other data
	
	// Properties for loading/saving GUI settings
	public Properties prop = new Properties();
	
	// Script information
	public boolean conditionMet = false;
	public String status = "Initializing";
	public long startTime;
	
	// Antiban
	public Antiban ab = new Antiban();
	public boolean afkMode, shouldGenerateActionTime;
	
	// Position
	public RSTile crabTile;
	public RSTile resetTile;
	public boolean crabclawIsland;
	
	// Food
	public boolean useFood;
	public int foodID;
	public int foodQuantity;
	
	// Potions
	public boolean drinkPotions;
	public String potionName;
	public int potionQuantity;
	
	// Paint
	public Tabs tab = Paint.Tabs.INFO;
	public int attXp, strXp, defXp, mageXp, rangeXp, hpXp;
	public boolean hidePaint = false;
	
	// Combat Variables
	public int eatAtHP;
	public boolean shouldReset, drankPotion, ateFood;
	public PotionHandler potionHandler;
	public FoodHandler foodHandler;
	public PositionHandler positionHandler;
	
	// Banking
	public boolean checkBank = true;

}
