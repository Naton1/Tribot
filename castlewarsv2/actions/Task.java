package scripts.castlewarsv2.actions;

import scripts.acamera.ACamera;

public abstract class Task {
	
	protected ACamera aCamera;

	public Task(ACamera aCamera) {
		this.aCamera = aCamera;
	}

	public abstract boolean validate();
	
	public abstract void execute();
	
	public abstract String status();
}
