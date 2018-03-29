package scripts.miner.graphics;

import org.tribot.api2007.types.RSTile;

public class NMinerSettings {
	
	private final int[] rocks;
	private final RSTile miningTile;
	private final boolean banking;
	private final double reactionTimeScale;
	private final boolean hopWorlds;
	
	public NMinerSettings(int[] rocks, RSTile miningTile, boolean banking, double reactionTimeScale, boolean hopWorlds) {
		this.rocks = rocks;
		this.miningTile = miningTile;
		this.banking = banking;
		this.reactionTimeScale = reactionTimeScale;
		this.hopWorlds = hopWorlds;
	}

	public int[] getRocks() {
		return rocks;
	}

	public RSTile getMiningTile() {
		return miningTile;
	}

	public boolean isBanking() {
		return banking;
	}

	public double getReactionTimeScale() {
		return reactionTimeScale;
	}

	public boolean isHopWorlds() {
		return hopWorlds;
	}
	
}
