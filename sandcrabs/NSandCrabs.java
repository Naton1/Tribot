package scripts.crabs;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Login;
import org.tribot.api2007.Login.STATE;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.EventBlockingOverride;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;

import scripts.acamera.ACamera;
import scripts.crabs.actions.Action;
import scripts.crabs.actions.CheckForStop;
import scripts.crabs.actions.KillCrabs;
import scripts.crabs.actions.MoveToBank;
import scripts.crabs.actions.MoveToCrabs;
import scripts.crabs.actions.ResetCrabs;
import scripts.crabs.actions.UseBank;
import scripts.crabs.antiban.Antiban;
import scripts.crabs.data.Variables;
import scripts.crabs.fx.GUI;
import scripts.crabs.paint.Paint;
import scripts.crabs.paint.Paint.Tabs;
import scripts.crabs.utils.FoodHandler;
import scripts.crabs.utils.MessageUtility;
import scripts.crabs.utils.PositionHandler;
import scripts.crabs.utils.PotionHandler;

@ScriptManifest(authors = {"Naton"}, category = "Combat", name = "nSand Crabs", description = "Simple sand crab script that supports custom crab tiles and reset tiles. Also supports food, potions, and banking. If you are using Crabclaw Island then please start on the Island or start with 10k in your inventory for the boat and the script will handle the rest and will withdraw another 10k when banking (if we are banking).")

public class NSandCrabs extends Script implements Starting, Ending, Painting, EventBlockingOverride, MessageListening07 {
	
	private ACamera aCamera = new ACamera(this);
	private GUI gui;
	private boolean initialized;
	private ArrayList<Action> actions = new ArrayList<Action>();

	@Override
	public void run() {
		
		// Set up dax walker private key
		WebWalker.setApiKey(NScripts.DAX_API_KEY, NScripts.DAX_SECRET_KEY);
		
		// run GUI (fxml created with SceneBuilder)
		try {
			gui = new GUI(new URL("http://nscripts.info/crabs/gui.fxml"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		gui.show();
		while (gui.isOpen())
			sleep(500);

		// we need the account to be logged in for the next step
		while (Login.getLoginState() != STATE.INGAME)
			sleep(500);
		
		// record starting exp for the paint
		recordStartingExp();
		
		// init handlers (food, potion, position)
		initHandlers();
		
		// add actions to the list
		Collections.addAll(actions, new MoveToCrabs(aCamera), new KillCrabs(aCamera), new ResetCrabs(aCamera));
		if (Variables.get().drinkPotions || Variables.get().useFood)
			Collections.addAll(actions, new MoveToBank(aCamera), new UseBank(aCamera));
		if (!Variables.get().useFood)
			actions.add(new CheckForStop(aCamera));
		
		// set some important settings
		startup();
		
		// run main loop
		while (!Variables.get().conditionMet) 
			for (Action a : actions) {
				if (a.validate()) {
					Variables.get().status = a.info();
					a.execute();
				}
				General.sleep(20, 40);
			}
		
	}
	
	private void initHandlers() {
		Variables.get().positionHandler = new PositionHandler(Variables.get().crabTile, Variables.get().resetTile);

		if (Variables.get().drinkPotions)
			Variables.get().potionHandler = new PotionHandler(PotionHandler.PotionType.valueOf(Variables.get().potionName));

		if (Variables.get().useFood)
			Variables.get().foodHandler = new FoodHandler(Variables.get().foodID);
	}
	
	private void recordStartingExp() {
		Variables.get().mageXp = Skills.getXP(SKILLS.MAGIC);
		Variables.get().defXp = Skills.getXP(SKILLS.DEFENCE);
		Variables.get().attXp = Skills.getXP(SKILLS.ATTACK);
		Variables.get().strXp = Skills.getXP(SKILLS.STRENGTH);
		Variables.get().hpXp = Skills.getXP(SKILLS.HITPOINTS);
		Variables.get().rangeXp = Skills.getXP(SKILLS.RANGED);
		initialized = true;
	}
	
	public void startup() {
		if (!Combat.isAutoRetaliateOn())
			Combat.setAutoRetaliate(true);
		if (Variables.get().afkMode)
			Antiban.suspendAntiban();
		ThreadSettings.get().setClickingAPIUseDynamic(true);
		setRandomSolverState(false);
	}
	
	@Override
	public void onStart() {
		println("Welcome to nSand Crabs. Enjoy!");
		Variables.get().startTime = Timing.currentTimeMillis();
	}

	@Override
	public void onEnd() {
		// close the gui if it is open when the script ends (not sure if theres a better way to do this, not familiar with jfx)
		if (gui != null && gui.isOpen())
			gui.close();
		println("Thanks for using nSand Crabs!");
	}

	@Override
	public void onPaint(Graphics g) {
		// run the paint after starting exp has been recorded
		if (initialized)
			Paint.paint(g);
	}

	@Override
	public OVERRIDE_RETURN overrideKeyEvent(KeyEvent arg0) {
		return null;
	}

	@Override
	public OVERRIDE_RETURN overrideMouseEvent(MouseEvent e) {
		if (e.getButton() == 1) {
			if (Paint.hideArea.contains(e.getPoint()))
				Variables.get().hidePaint = !Variables.get().hidePaint;
			else if (Paint.Tabs.INFO.get().contains(e.getPoint()))
				Variables.get().tab = Tabs.INFO;
			else if (Paint.Tabs.SKILLS.get().contains(e.getPoint()))
				Variables.get().tab = Tabs.SKILLS;
		}
		return OVERRIDE_RETURN.PROCESS;
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
	public void serverMessageReceived(String message) {
		MessageUtility.handleServerMessage(message);
	}

	@Override
	public void tradeRequestReceived(String arg0) {}

}
