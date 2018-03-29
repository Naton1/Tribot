package scripts.crabs.fx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.tribot.api.General;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSTile;
import org.tribot.util.Util;

import com.allatori.annotations.DoNotRename;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import scripts.crabs.data.Variables;

/**
 * Credits to laniax for the base JavaFX GUI + Controller in his API located at https://github.com/Laniax/LanAPI/blob/master/core/gui/
 */

@DoNotRename 
public class GUIController implements Initializable {
	
	private GUI gui;
	private File directory = new File(Util.getWorkingDirectory() + "\\nSandCrabs");
	
	// Antiban
	@FXML @DoNotRename 
	private CheckBox afkMode;
	
	// Tiles
	@FXML @DoNotRename 
	private TextField crabTile, resetTile;
	@FXML @DoNotRename 
	private CheckBox crabclawIsland;
	@FXML @DoNotRename 
	private Button crabTileButton, resetTileButton;
	
	// Food
	@FXML @DoNotRename 
	private TextField foodID, foodQuantity;
	@FXML @DoNotRename 
	private CheckBox useFood;
	
	// Potions
	@FXML @DoNotRename 
	private CheckBox drinkPotions;
	@FXML @DoNotRename 
	private ComboBox<String> potionBox;
	@FXML @DoNotRename 
	private TextField potionQuantity;
	private String[] potionTypes = {"Combat Potion", "Super Combat Potion", "Ranging Potion", "Super Strength"};
	
	// Load Settings
	@FXML @DoNotRename 
	private ComboBox<String> settingsBox;
	@FXML @DoNotRename 
	private Button loadSettings;
	
	// Save Settings
	@FXML @DoNotRename 
	private TextField saveSettingsField;
	@FXML @DoNotRename 
	private Button saveSettings;
	
	// Start
	@FXML @DoNotRename 
	private Button startScript;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		potionBox.setItems(FXCollections.observableArrayList(potionTypes));
		potionBox.setValue(potionTypes[0]);
		settingsBox.setItems(FXCollections.observableArrayList(getSaveFiles()));
	}
	
	public void setGUI(GUI gui) {
		this.gui = gui;
	}
	
	public GUI getGUI() {
		return this.gui;
	}

	@DoNotRename 
	public void currentCrabTilePressed() {
		crabTile.setText(Player.getPosition().toString());
	}
	
	@DoNotRename 
	public void currentResetTilePressed() {
		resetTile.setText(Player.getPosition().toString());
	}

	@DoNotRename 
	public void startScriptPressed() {
		
		// check if the GUI has been completed
		if (guiComplete()) {
			try {
				// initialize the variables
				Variables.get().afkMode = afkMode.isSelected();
				Variables.get().crabTile = getRSTile(crabTile.getText());
				Variables.get().resetTile = getRSTile(resetTile.getText());
				Variables.get().crabclawIsland = crabclawIsland.isSelected();
				Variables.get().useFood = useFood.isSelected();
				Variables.get().foodID = Integer.parseInt(foodID.getText());
				Variables.get().foodQuantity = Integer.parseInt(foodQuantity.getText());
				Variables.get().drinkPotions = drinkPotions.isSelected();
				Variables.get().potionName = potionBox.getValue().replaceAll(" ", "_").toUpperCase();
				Variables.get().potionQuantity = Integer.parseInt(potionQuantity.getText());
			}
			catch (NumberFormatException e) {
				General.println("Error: A non integer was entered into a text field where an integer was expected");
			}
			gui.close();
		}
		else
			General.println("GUI is not complete.");
		

	}
	
	@DoNotRename 
	public void saveSettings() {
		if (!directory.exists())
			directory.mkdirs();
		try {
			Variables.get().prop.clear();
			
			// Antiban
			Variables.get().prop.put("afkMode", String.valueOf(afkMode.isSelected()));
			
			// Position
			Variables.get().prop.put("crabTile", crabTile.getText());
			Variables.get().prop.put("resetTile", resetTile.getText());
			Variables.get().prop.put("crabclawIsland", String.valueOf(crabclawIsland.isSelected()));
			
			// Food
			Variables.get().prop.put("useFood", String.valueOf(useFood.isSelected()));
			Variables.get().prop.put("foodID", foodID.getText());
			Variables.get().prop.put("foodQuantity", foodQuantity.getText());
			
			// Potions
			Variables.get().prop.put("drinkPotions", String.valueOf(drinkPotions.isSelected()));
			Variables.get().prop.put("potionType", potionBox.getValue());
			Variables.get().prop.put("potionQuantity", potionQuantity.getText());
			
			String saveFilePath = directory.getAbsolutePath() + "/" + saveSettingsField.getText();
			
			Variables.get().prop.store(new FileOutputStream(saveFilePath), "GUI Settings");

			General.println("Settings saved successfully.");
		} catch (IOException e) {
			General.println("Error attempting to save settings.");
			e.printStackTrace();
		}
	}
	
	@DoNotRename 
	public void loadSettings() {
		String settingsFileName = settingsBox.getValue();
		String settingsFilePath = directory.getAbsolutePath() + "/" + settingsFileName;
		
		if (settingsFileName != "No Saved Settings Found") {
			try {
				Variables.get().prop.load(new FileInputStream(settingsFilePath));
				
				// Antiban
				afkMode.setSelected(Boolean.valueOf(Variables.get().prop.getProperty("afkMode")));
				
				// Position
				crabTile.setText((String) Variables.get().prop.getProperty("crabTile"));
				resetTile.setText((String) Variables.get().prop.getProperty("resetTile"));
				crabclawIsland.setSelected(Boolean.valueOf(Variables.get().prop.getProperty("crabclawIsland")));
				
				// Food
				useFood.setSelected(Boolean.valueOf(Variables.get().prop.getProperty("useFood")));
				foodID.setText((String) Variables.get().prop.getProperty("foodID"));
				foodQuantity.setText((String) Variables.get().prop.getProperty("foodQuantity"));
				
				// Potions
				drinkPotions.setSelected(Boolean.valueOf(Variables.get().prop.getProperty("drinkPotions")));
				potionBox.setValue(Variables.get().prop.getProperty("potionType"));
				potionQuantity.setText((String) Variables.get().prop.getProperty("potionQuantity"));
				
				General.println("Settings loaded sucessfully.");
			} catch (IOException e) {
				General.println("Error attempting to load settings.");
				e.printStackTrace();
			}
		}
		else
			General.println("No saved settings to load.");
	}
	
	/**
	 * This method is called when the save settings button is pressed.
	 * It will search the nSandCrabs folder for all save files.
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
	
	/**
	 * This method creates an RSTile object according to the string argument
	 * 
	 * @param tileString RSTile in string form (x, y, z)
	 * @return RSTile object with coordinates according to the string argument.
	 */
	private RSTile getRSTile(String tileString) {
		int count = 0, x = 0, y = 0, z = 0;
		// remove all parenthesis and spaces from the string
		tileString = tileString.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(" ", "");
		for (String s : tileString.split(",")) {
			switch (count) {
			case 0:
				x = Integer.parseInt(s);
				break;
			case 1:
				y = Integer.parseInt(s);
				break;
			case 2:
				z = Integer.parseInt(s);
				break;
			}
			count++;
		}
		return new RSTile(x, y, z);
	}
	
	/**
	 * Checks if the GUI is complete enough for the script to function.
	 * @return True if the GUI is complete enough for the script to function, false if it is not.
	 */
	private boolean guiComplete() {
		// This could all be done in one line however it would get really messy
		
		if (crabTile.getText().equals("") || resetTile.getText().equals(""))
			return false;
		
		else if (useFood.isSelected() && (foodID.getText().equals("") || foodQuantity.getText().equals("")))
			return false;
		
		else if (drinkPotions.isSelected() && potionQuantity.getText().equals(""))
			return false;
		
		return true;
					
	}

}
