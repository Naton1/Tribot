package scripts.castlewarsv2;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.tribot.api.General;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.Login;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.EventBlockingOverride;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;

import scripts.acamera.ACamera;
import scripts.castlewarsv2.actions.InGame;
import scripts.castlewarsv2.actions.InLobby;
import scripts.castlewarsv2.actions.InWaitingRoom;
import scripts.castlewarsv2.actions.MoveToCastleWars;
import scripts.castlewarsv2.actions.Task;
import scripts.castlewarsv2.antiban.Antiban;
import scripts.castlewarsv2.api.CastleWarsSettings;
import scripts.castlewarsv2.data.Variables;
import scripts.castlewarsv2.gui.GUI;
import scripts.castlewarsv2.paint.Paint;
import scripts.castlewarsv2.paint.PaintInfoThread;
import scripts.napi.NScripts;
import scripts.napi.Signature;
import scripts.webwalker_logic.WebWalker;

public class NCastleWars extends Script implements Starting, Ending, Painting, MessageListening07, EventBlockingOverride {

	@Override
	public void run() {
		
		// set private key for dax walker
		WebWalker.setApiKey(NScripts.DAX_API_KEY, NScripts.DAX_SECRET_KEY);
		
		// run settings before the initial loop
		startup();
		
		// run gui
		GUI gui = new GUI();
		while (gui.isOpen()) 
			General.sleep(500);
		
		// wait till we are in game for the rest
		while (Login.getLoginState() != Login.STATE.INGAME)
			General.sleep(250,500);
		
		// start paint info tracking thread
		new PaintInfoThread(this).start();
	    
		// add tasks
		CastleWarsSettings settings = gui.getSettings();
		
		List<Task> tasks = new ArrayList<Task>();
		
		ACamera aCamera = new ACamera(this);
		
		Collections.addAll(tasks, new InGame(aCamera, settings.isSleep()), new InLobby(aCamera, settings.isSleep(), settings.getTeam()),
				new InWaitingRoom(aCamera, settings.isSleep()), new MoveToCastleWars(aCamera, settings.getTeam()));
		
		// run main loop
		loop(tasks);
	}
	
	private void startup() {
		Antiban.suspendAntiban();
		this.setAIAntibanState(false);
		this.setRandomSolverState(false);
		General.useAntiBanCompliance(false);
	}
	
	private void loop(List<Task> tasks) {
		while (!Variables.get().conditionMet) {
			for (Task t : tasks) {
				if (t.validate()) {
					Variables.get().status = t.status();
					t.execute();
				}
			}
			General.sleep(20, 40);
		}
		
	}
	
	@Override
	public void onStart() {
		Antiban.get().ab = new ABCUtil();

		General.println("Make sure to start the script in the world you wish to play castle wars in.");
		General.println("Block all user input to prevent interruptions");
		General.println("Please also make sure to start the script on 'default zoom' and on 'fixed mode' or the script will malfunction");

	}

	@Override
	public void onEnd() {
		General.println("nCastle Wars ran for " + Variables.get().runTimeString);
		General.println("You have earned " + Variables.get().numTickets + " tickets!");
		General.println("If you enjoyed using this script please leave a review on the forums!");
		Antiban.get().ab.close();
		
		Signature.sendSignatureDataCwars(General.getTRiBotUsername(), Variables.get().runTime, Variables.get().numTickets);
	}

	@Override
	public void clanMessageReceived(String arg0, String arg1) {}

	@Override
	public void duelRequestReceived(String arg0, String arg1) {}

	@Override
	public void personalMessageReceived(String arg0, String arg1) {}

	@Override
	public void playerMessageReceived(String arg0, String arg1) {}
	
	@Override
	public void serverMessageReceived(String msg) {
		if (msg.contains("You do not have the correct items")) {
			Variables.get().conditionMet = true;
			General.println("Ending script; do not start the script with items you cannot bring into castle wars.");
		}
		else if (msg.contains("you are dead"))
			Antiban.get().sleepBeforeNextAction = true;
	}

	@Override
	public void tradeRequestReceived(String arg0) {}

	@Override
	public void onPaint(Graphics g) {
		Paint.paint(g);
	}

	@Override
	public OVERRIDE_RETURN overrideKeyEvent(KeyEvent arg0) {
		return OVERRIDE_RETURN.PROCESS;
	}

	@Override
	public OVERRIDE_RETURN overrideMouseEvent(MouseEvent e) {
		if (e.getButton() == 1) 
			if (Paint.hideArea.contains(e.getPoint())) 
				Variables.get().paintEnabled = !Variables.get().paintEnabled;
		return OVERRIDE_RETURN.PROCESS;
	}

}
