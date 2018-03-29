package scripts.miner.graphics;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

import org.tribot.api.General;
import org.tribot.api2007.Player;
import org.tribot.util.Util;

import com.allatori.annotations.DoNotRename;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import scripts.miner.data.MiningConstants;
import scripts.miner.utils.MiningUtil;
import scripts.napi.Position;


/**
 * Credits to laniax for the base JavaFX GUI + Controller in his API located at https://github.com/Laniax/LanAPI/blob/master/core/gui/
 */

@DoNotRename 
public class GUIController implements Initializable {
	
	private GUI gui;
	
	private File directory = new File(Util.getWorkingDirectory() + "\\nMiner");
	
	@DoNotRename @FXML
	private ComboBox<String> rock, settingsBox;

	private String[] rockTypes = MiningConstants.ROCKS;
	
	@DoNotRename @FXML
	private CheckBox bank, hop;
	
	@DoNotRename @FXML
	private TextField miningTile, settingsName;
	
	@DoNotRename @FXML
	private Slider scale;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		rock.setItems(FXCollections.observableArrayList(rockTypes));
		rock.setValue(rockTypes[0]);
		settingsBox.setItems(FXCollections.observableArrayList(getSaveFiles()));
	}
	
	@DoNotRename @FXML
	public void startScript() {
		
		if (guiComplete()) {
			
			// Attempting to remove dependencies
//			Vars.get().rocks = MiningUtil.getRockIDs(rock.getValue());
//			Vars.get().miningTile = Position.stringToRSTile(miningTile.getText());
//			Vars.get().banking = bank.isSelected();
//			NMinerAntiban.get().reactionTimeScale = scale.getValue();
//			Vars.get().changeWorld = hop.isSelected();
			
			gui.close();
		}
		else {
			General.println("GUI not fully completed");
		}
	}
	
	@DoNotRename @FXML
	public void getCurrentTile() {
		miningTile.setText(Player.getPosition().toString());
	}
	
	@DoNotRename @FXML
	public void saveSettings() {
		
		if (!directory.exists())
			directory.mkdirs();
		
		if (!guiComplete()) {
			General.println("GUI not filled out.");
			return;
		}
		
		try {
			Properties prop = new Properties();
			
			prop.put("Rock", rock.getValue());
			prop.put("Tile", miningTile.getText());
			prop.put("Bank", String.valueOf(bank.isSelected()));
			prop.put("Scale", String.valueOf(scale.getValue()));
			prop.put("Hop", hop.isSelected());
			
			String saveFilePath = directory.getAbsolutePath() + "/" + settingsName.getText();
			prop.store(new FileOutputStream(saveFilePath), "GUI Settings");

			General.println("Settings saved successfully.");
		} catch (IOException e) {
			General.println("Error attempting to save settings.");
			e.printStackTrace();
		}
	}
	
	@DoNotRename @FXML
	public void loadSettings() {
		
		String settingsFileName = settingsBox.getValue();
		String settingsFilePath = directory.getAbsolutePath() + "/" + settingsFileName;
		
		if (settingsFileName != "No Saved Settings Found") {
			try {
				
				Properties prop = new Properties();
				
				prop.load(new FileInputStream(settingsFilePath));
				
				rock.setValue(prop.getProperty("Rock"));
				miningTile.setText(prop.getProperty("Tile"));
				bank.setSelected(Boolean.valueOf(prop.getProperty("Bank")));
				scale.setValue(Double.valueOf(prop.getProperty("Scale")));
				hop.setSelected(Boolean.valueOf(prop.getProperty("Hop")));
				
				General.println("Settings loaded sucessfully.");
			} catch (IOException e) {
				General.println("Error attempting to load settings.");
				e.printStackTrace();
			}
		}
		else
			General.println("No saved settings to load.");
	}
	
	public void setGUI(GUI gui) {
		this.gui = gui;
	}
	
	public GUI getGUI() {
		return this.gui;
	}
	
	public NMinerSettings getSettings() {
		return new NMinerSettings(MiningUtil.getRockIDs(rock.getValue()), Position.stringToRSTile(miningTile.getText()), bank.isSelected(), scale.getValue(), hop.isSelected());
	}
	
	private boolean guiComplete() {
		return !miningTile.getText().trim().equals("");
	}
	
	/**
	 * This method is called when the save settings button is pressed.
	 * It will search the nMiner folder for all save files.
	 * 
	 * @return The names of the save files. If there are none, it will return with "No Saved Settings Found" at index 0 of the array.
	 */
	private String[] getSaveFiles() {
		ArrayList<String> settings = new ArrayList<String>();
		if (!directory.exists())
			return new String[] {"No Saved Settings Found"};
		else {
			for (File f : directory.listFiles()) {
				if (f.isFile()) 
					settings.add(f.getName());
			}
			return settings.toArray(new String[settings.size()]);
		}
	}
	
}
