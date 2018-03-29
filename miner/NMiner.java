package scripts.miner;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Login.STATE;
import org.tribot.api2007.WorldHopper;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;

import scripts.acamera.ACamera;
import scripts.fluffeepaint.FluffeesPaint;
import scripts.fluffeepaint.PaintInfo;
import scripts.miner.actions.ChangeWorld;
import scripts.miner.actions.DropOre;
import scripts.miner.actions.MineRocks;
import scripts.miner.actions.UseBank;
import scripts.miner.antiban.NMinerAntiban;
import scripts.miner.antiban.NMinerAntibanProfile;
import scripts.miner.api.Action;
import scripts.miner.api.MoveToBank;
import scripts.miner.api.MovingAction;
import scripts.miner.api.PriorityAction;
import scripts.miner.data.Priority;
import scripts.miner.data.Vars;
import scripts.miner.graphics.FXMLString;
import scripts.miner.graphics.GUI;
import scripts.miner.graphics.NMinerSettings;
import scripts.miner.graphics.PaintInfoThread;
import scripts.miner.utils.MiningUtil;
import scripts.napi.Account;
import scripts.napi.NScripts;
import scripts.napi.Signature;
import scripts.webwalker_logic.WebWalker;

public class NMiner extends Script implements Starting, Ending, Painting, PaintInfo, MessageListening07 {
	
	private boolean paintThreadStarted = false;
	
	private GUI gui;
	
	private final FluffeesPaint FLUFFEES_PAINT = new FluffeesPaint(this, FluffeesPaint.PaintLocations.BOTTOM_RIGHT_PLAY_SCREEN,
			new Color[]{new Color(255, 251, 255)}, "Tahoma", new Color[]{new Color(255, 218, 185, 127)},
            new Color[]{new Color(139, 119, 101)}, 1, false, 5, 3, 0);
	
	@Override
	public void run() {
		
		// Set private key for dax walker
		WebWalker.setApiKey(NScripts.DAX_API_KEY, NScripts.DAX_SECRET_KEY);
		
		// Run GUI (fxml created with SceneBuilder)
		gui = new GUI(FXMLString.get);
		gui.show();
		while (gui.isOpen())
			General.sleep(300, 500);
		
		NMinerSettings settings = gui.getController().getSettings();
		
		// We need to be logged in for the rest of the steps
		while (Login.getLoginState() != STATE.INGAME)
			General.sleep(300, 500);
		
		// Set up list of actions
		ACamera aCamera = new ACamera(this);
		
		ArrayList<PriorityAction> actions = new ArrayList<PriorityAction>();
		
		Collections.addAll(actions,
				new MovingAction(Priority.GO_TO_ROCKS, aCamera, () -> !Inventory.isFull() && !MiningUtil.nearTile(settings.getMiningTile()), settings.getMiningTile()),
				new MineRocks(aCamera, settings.isHopWorlds(), settings.getMiningTile(), settings.getRocks()));
		
		if (settings.isBanking())
			Collections.addAll(actions, new UseBank(aCamera), new MoveToBank(Priority.GO_TO_BANK, aCamera, () -> Inventory.isFull() && !Banking.isInBank()));
		else
			actions.add(new DropOre(aCamera));
		
		if (settings.isHopWorlds()) 
			actions.add(new ChangeWorld(WorldHopper.isMembers(WorldHopper.getWorld()), aCamera));
		
		Collections.sort(actions);
		
		// Load/Create antiban profile (to persist certain patterns)
		NMinerAntiban.get().antibanProfile = new NMinerAntibanProfile(Account.getUsername());
		NMinerAntiban.get().antibanProfile.loadProfile();
		
		NMinerAntiban.get().reactionTimeScale = settings.getReactionTimeScale();
		
		// Start paint data tracking thread
		new PaintInfoThread(this).start();
		paintThreadStarted = true;
		
		// Check if we have a pickaxe
		if (!MiningUtil.havePickaxe()) {
			Vars.get().conditionMet = true;
			General.println("We do not have a pickaxe.");
		}
		
		// Run main loop
		loop(actions);
	}
	
	private void loop(List<PriorityAction> actions) {
		while (!Vars.get().conditionMet) {
			for (Action action : actions) 
				if (action.validate()) {
					Vars.get().status = action.toString();
					action.execute();
					break;
				}
			General.sleep(20, 40);
		}
	}
	
	@Override
	public void onStart() {
		
		Vars.get().reset();
		
		General.println("Welcome to nMiner.");
		General.println("NOTE: This script implements full ABC2 so the exp rates will be a bit lower but bots will survive longer.");
		General.println("However, the reaction times can be scaled down through the GUI.");
	}
	
	@Override
	public void onEnd() {
		
		// Stop ABC2 thread
		NMinerAntiban.get().close();
		
		// Close GUI if it is open
		if (gui != null && gui.isOpen())
			gui.close();
		
		// Print statistics
		General.println("Thanks for using nMiner!");
		General.println("Total Runtime: " + Timing.msToString(Vars.get().runTime));
		General.println("Mining Exp Gained: " + Vars.get().expGained);
		
		Signature.sendSignatureDataMiner(General.getTRiBotUsername(), Vars.get().runTime, Vars.get().expGained);
	}

	@Override
	public void onPaint(Graphics g) {
		// ty for the paint tool fluffee
		if (paintThreadStarted)
			FLUFFEES_PAINT.paint(g);
	}

	@Override
	public String[] getPaintInfo() {
		return new String[] {"nMiner v1.50", "Runtime: " + Timing.msToString(Vars.get().runTime), "Status: " + Vars.get().status, "Exp gained: " + Vars.get().expGained + " (" + Vars.get().levelsGained  + ")"};
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
	}

	@Override
	public void tradeRequestReceived(String arg0) {}

}
