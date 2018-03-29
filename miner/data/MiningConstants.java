package scripts.miner.data;

public class MiningConstants {
	
	public static final String[] PICKAXES = {"Bronze pickaxe", "Iron pickaxe", "Steel pickaxe", "Black pickaxe", "Mithril pickaxe", "Adamant pickaxe", "Rune pickaxe", "Dragon pickaxe"};
	
	//                            	 Bronze Iron Steel Black Mithril Adamant Rune Dragon 3rd Age Infernal
	public static final int[] PICKAXE_IDS = {1265, 1267, 1269, 12297, 1273, 1271, 1275, 11920, 20014, 13243};
	
	public static final String[] ROCK_NAMES = {"Copper", "Tin", "Iron", "Clay", "Coal", "Gold", "Mithril", "Adamant", "Rune"};
	
	
	public enum ROCKS {
		
		COPPER(new int[] {7484, 7453}),
		TIN(new int[] {7485, 7486}),
		IRON(new int[] {7455, 7488}),
		CLAY(new int[] {7454, 7487}),
		COAL(new int[] {7456, 7489}),
		GOLD(new int[] {7458, 7491}),
		MITHRIL(new int[] {7459, 7492}),
		ADAMANT(new int[] {7460, 7493}),
		RUNE(new int[] {7461}),
		EMPTY(new int[] {7468, 7469});
		
		private final int[] ids;
		
		private ROCKS(int[] ids) {
			this.ids = ids;
		}
		
		public int[] getIds() {
			return this.ids;
		}
	}
	
	public static final int MINING_ANIMATION = 625;

}
