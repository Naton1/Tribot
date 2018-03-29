package scripts.castlewarsv2.paint;

import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.script.Script;

import scripts.castlewarsv2.api.CastleWars;
import scripts.castlewarsv2.data.Variables;

public class PaintInfoThread extends Thread {
	
	private final Script script;
	private final int startTickets;
	private final long startTime;
	
	private boolean running;
	
	public PaintInfoThread(Script script) {
		this.script = script;
		this.startTickets = Inventory.getCount(CastleWars.TICKET_ID);
		this.startTime = Timing.currentTimeMillis();
		this.running = true;
	}
	
	@Override
	public void run() {
		while (running) {
			
			Variables.get().numTickets = Inventory.getCount(CastleWars.TICKET_ID) - this.startTickets;
			Variables.get().runTime = Timing.timeFromMark(this.startTime);
			Variables.get().runTimeString = Timing.msToString(Variables.get().runTime);
			Variables.get().ticketsPerHour = Math.round((double) Variables.get().numTickets  * 3600000 / Timing.timeFromMark(this.startTime));
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			running = script.isActive();
			
		}
	}

}
