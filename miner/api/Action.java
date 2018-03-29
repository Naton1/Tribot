package scripts.miner.api;

import scripts.acamera.ACamera;
import scripts.miner.NMiner;

public abstract class Action {
	
	protected NMiner script;
	
	protected ACamera aCamera;
	
	public Action(ACamera aCamera) {
		this.aCamera = aCamera;
	}
	
	public abstract boolean validate();
	
	public abstract void execute();
	
	@Override
	public abstract String toString();

}
