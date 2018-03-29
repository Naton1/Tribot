package scripts.miner.api;

import scripts.acamera.ACamera;

public abstract class PriorityAction extends Action implements Comparable<PriorityAction> {
	
	protected final int priority;

	public PriorityAction(int priority, ACamera aCamera) {
		super(aCamera);
		this.priority = priority;
	}
	
	@Override
	public int compareTo(PriorityAction action) {
		return this.getPriority() - action.getPriority();
	}
	
	public int getPriority() {
		return this.priority;
	}

}
