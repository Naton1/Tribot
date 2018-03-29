package scripts.miner.graphics;

import java.io.ByteArrayInputStream;

import javax.swing.SwingUtilities;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Credits to laniax for the base JavaFX GUI in his API located at https://github.com/Laniax/LanAPI/blob/master/core/gui/GUI.java
 * Also thanks to laniax for discovering a way to load fxml without having to load it from a website on the tribot repository
 */

public class GUI extends Application {
	
	private GUIController controller;

	private Stage stage;
	private Scene scene;

	private boolean isOpen = false;
	
	private String fxml;
	
	public GUI(String fxml) {
		
		this.fxml = fxml;
		
		// We have to start the JFX thread from the EDT otherwise tribot will end it.
		SwingUtilities.invokeLater(() -> {

			new JFXPanel(); // we have to init the toolkit

			Platform.runLater(() -> {
				try {
					final Stage stage = new Stage();

					start(stage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		});

		waitForInit();
	}


	public Scene getScene() {
		return this.scene;
	}

	public Stage getStage() {
		return this.stage;
	}

	/**
	 * The main entry point for all JavaFX applications. The start method is called
	 * after the init method has returned, and after the system is ready for the
	 * application to begin running.
	 * <p>
	 * <p>
	 * NOTE: This method is called on the JavaFX Application Thread.
	 * </p>
	 *
	 * @param stage
	 *            the primary stage for this application, onto which the application
	 *            scene can be set. The primary stage will be embedded in the
	 *            browser if the application was launched as an applet. Applications
	 *            may create other stages, if needed, but they will not be primary
	 *            stages and will not be embedded in the browser.
	 */
	@Override
	public void start(Stage stage) throws Exception {

		this.stage = stage;
		
		stage.setTitle("nMiner GUI");

		Platform.setImplicitExit(false);

		FXMLLoader loader = new FXMLLoader();

		loader.setClassLoader(this.getClass().getClassLoader());
		
		Parent box = loader.load(new ByteArrayInputStream(fxml.getBytes()));
		
		controller = loader.getController();
		
		controller.setGUI(this);

		scene = new Scene(box);
		
		scene.setFill(Color.TRANSPARENT);

		stage.setScene(scene);
		
		stage.setResizable(false);

	}
	
    @SuppressWarnings("unchecked")
	public <T extends GUIController> T getController() {

        return (T) this.controller;

    }

	public void show() {

		if (stage == null)
			return;

		isOpen = true;

		Platform.runLater(() -> stage.show());
	}

	public void close() {

		if (stage == null)
			return;

		isOpen = false;

		Platform.runLater(() -> stage.close());
	}

	public boolean isOpen() {
		return isOpen;
	}

	private void waitForInit() {

		Timing.waitCondition(new Condition() {
			@Override
			public boolean active() {
				General.sleep(250);
				return stage != null;
			}
		}, 5000);
	}
}
