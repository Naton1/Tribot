package scripts.crabs.antiban;

import org.tribot.api.General;
import org.tribot.api.util.abc.ABCUtil;

/**
 * This mainly just utilizes the ABCUtil.
 * @author Nate
 *
 */

public class Antiban {
	
	public ABCUtil ab;

	public Antiban() {
		ab = new ABCUtil();
	}
	
	public void performTimedActions() {
		if (ab.shouldCheckXP()) {
			ab.checkXP();
			General.sleep(850,1400);
		}
		if (ab.shouldRotateCamera()) {
			ab.rotateCamera();
		}
		if (ab.shouldRightClick()) {
			ab.rightClick();
		}
		if (ab.shouldLeaveGame()) {
			ab.leaveGame();
		}
		if (ab.shouldPickupMouse()) {
			ab.pickupMouse();
		}
		if (ab.shouldMoveMouse()) {
			ab.moveMouse();
		}
		if (ab.shouldExamineEntity()) {
			ab.examineEntity();
		}
	}
	
	public long generateReactionTime() {
		return ab.generateReactionTime();
	}
	
	public void close() {
		ab.close();
	}
	
	/**
	 * Suspends Tribot's antiban threads. Credits to someone of the forums for this method I didn't write it myself (forgot his name).
	 */
	@SuppressWarnings("deprecation")
	public static void suspendAntiban() {
		for (Thread thread : Thread.getAllStackTraces().keySet()) {
			if (thread.getName().contains("Antiban") || thread.getName().contains("Fatigue")) {
				thread.suspend();
			}
		}
	}
	
	public ABCUtil getABCUtil() {
		return ab;
	}

}
