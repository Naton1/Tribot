package scripts.miner.data;

public class Vars {
	
	// Instance Manipulation
	private static Vars vars;
	
	public static Vars get() {
		return vars == null ? vars = new Vars() : vars;
	}
	
	public void reset() {
		vars = new Vars();
	}
	
	// Script Variables
	public boolean conditionMet = false;
	public String status = "Initializing...";

	// Paint Variables
	public long runTime, expGained;
	public int levelsGained;

	// Action Variables
	public boolean needToChangeWorld;

}
