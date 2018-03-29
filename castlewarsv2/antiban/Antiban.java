package scripts.castlewarsv2.antiban;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.util.abc.ABCUtil;

import scripts.castlewarsv2.api.CastleWars;

public class Antiban {
	
	private static Antiban antiban;
	
	public static Antiban get() {
		return antiban == null ? antiban = new Antiban() : antiban;
	}
	
	public static void reset() {
		antiban = new Antiban();
	}
	
	public boolean inGame, sleepBeforeNextAction;
	
	public ABCUtil ab;
	
	public long generateReactionTime() {
		return ab.generateReactionTime();
	}
	
	public void close() {
		ab.close();
	}
	
	@SuppressWarnings("deprecation")
	public static void suspendAntiban() {
		for(Thread thread:Thread.getAllStackTraces().keySet())
			if(thread.getName().contains("Antiban")||thread.getName().contains("Fatigue"))
				thread.suspend();
	}
	
	public ABCUtil getABCUtil() {
		return ab;
	}
	
	// this "antiban" is not the best (randomly performing actions) but I've been able to run the script for upwards of 60 hours straight with no ban
	public void performRandomTimedAction() {
		int random = General.random(1, 100);
		if (random > 60) 
			ab.rightClick();
		else if (random > 20)
			ab.rotateCamera();
		else
			ab.examineEntity();
	}
	
	public void sleepBeforeClimbingLadder() {
		
		int sleepPeriod = (int) (General.random(30000, 70000) + Timing.currentTimeMillis());
		
		// so that we don't sleep extra long if the game ends
		while (sleepPeriod > Timing.currentTimeMillis() && CastleWars.inGame())
			General.sleep(150, 500);
	}
}
