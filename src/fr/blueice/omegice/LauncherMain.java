package fr.blueice.omegice;

import fr.trxyy.alternative.alternative_apiv2.base.AlternativeBase;
import fr.trxyy.alternative.alternative_apiv2.base.GameConnect;
import fr.trxyy.alternative.alternative_apiv2.base.GameEngine;
import fr.trxyy.alternative.alternative_apiv2.base.GameFolder;
import fr.trxyy.alternative.alternative_apiv2.base.GameLinks;
import fr.trxyy.alternative.alternative_apiv2.base.LauncherBackground;
import fr.trxyy.alternative.alternative_apiv2.base.LauncherBase;
import fr.trxyy.alternative.alternative_apiv2.base.LauncherPane;
import fr.trxyy.alternative.alternative_apiv2.base.LauncherPreferences;
import fr.trxyy.alternative.alternative_apiv2.base.WindowStyle;
import fr.trxyy.alternative.alternative_apiv2.utils.Mover;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LauncherMain extends AlternativeBase {
	public static GameFolder gameFolder = new GameFolder("OmegiceLauncher");
	private GameLinks gameLinks = new GameLinks("*****", "1.19.2-forge-43.1.1.json");
	private LauncherPreferences launcherPref = new LauncherPreferences("Omegice Launcher V1.0A", 880, 520, Mover.MOVE);
	private GameEngine gameEngine = new GameEngine(gameFolder, gameLinks, launcherPref);
	private GameConnect gameConnect = new GameConnect("*****", "25565");

	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(this.createContent());
		LauncherBase launcher = new LauncherBase(primaryStage, scene, StageStyle.TRANSPARENT, this.gameEngine);
		launcher.setIconImage(primaryStage, "favicon.png");
	}

	private Parent createContent() {
		LauncherPane contentPane = new LauncherPane(this.gameEngine, 5, WindowStyle.TRANSPARENT);
		/** Direct Connect **/
		this.gameEngine.reg(gameConnect);
		new LauncherBackground(this.gameEngine, "background.mp4", contentPane);
		new LauncherPanel(contentPane, this.gameEngine);
		return contentPane;
	}
	
	public static void main(String[] args) {
		Application.launch(LauncherMain.class, args);
	}
}
