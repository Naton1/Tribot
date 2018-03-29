package scripts.miner.graphics;

import org.tribot.api.Timing;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.script.Script;

import scripts.miner.data.Vars;

public class PaintInfoThread extends Thread {
	
	private long startTime, startExp;
	
	private int startLevel;
	
	private Script script;
	
	private boolean running;
	
	public PaintInfoThread(Script script) {
		this.script = script;
		this.startTime = Timing.currentTimeMillis();
		this.startExp = Skills.getXP(SKILLS.MINING);
		this.startLevel = Skills.getActualLevel(SKILLS.MINING);
		running = true;
	}
	
	@Override
	public void run() {
		
		while (running) {
			
			Vars.get().expGained = Skills.getXP(SKILLS.MINING) - this.startExp;
			Vars.get().runTime = Timing.currentTimeMillis() - this.startTime;
			Vars.get().levelsGained = Skills.getActualLevel(SKILLS.MINING) - this.startLevel;
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			running = this.script.isActive();
			
		}
	}
	
	

}
