package scripts.crabs.actions;

import scripts.acamera.ACamera;

/**
 * Credits to Worthy for his task/node framework and Final Caliber for the aCamera
 */

public abstract class Action {
	
	protected ACamera aCamera;

	public Action(ACamera aCamera) {
		this.aCamera = aCamera;
	}
	
	public abstract boolean validate();
	
	public abstract void execute();
	
	public abstract String info();

}
