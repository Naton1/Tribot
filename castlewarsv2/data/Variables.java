package scripts.castlewarsv2.data;

public class Variables {
	
	private static Variables vars;
	
	public static Variables get() {
		return vars == null ? vars = new Variables() : vars;
	}
	
	public static void reset() {
		vars = new Variables();
	}
	
	public boolean conditionMet = false, paintEnabled = true;
	
	public int numTickets;
	
	public double ticketsPerHour;
	
	public long runTime;
	
	public String status = "Initializing...", runTimeString;
}
