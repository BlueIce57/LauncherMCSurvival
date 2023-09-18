package fr.blueice.omegice;

import java.io.File;
import java.util.UUID;

import javax.swing.JDialog;
import javax.swing.JFrame;

import fr.trxyy.alternative.alternative_api_uiv2.components.LauncherAlert;
import fr.trxyy.alternative.alternative_api_uiv2.components.LauncherButton;
import fr.trxyy.alternative.alternative_api_uiv2.components.LauncherImage;
import fr.trxyy.alternative.alternative_api_uiv2.components.LauncherLabel;
import fr.trxyy.alternative.alternative_api_uiv2.components.LauncherPasswordField;
import fr.trxyy.alternative.alternative_api_uiv2.components.LauncherProgressBar;
import fr.trxyy.alternative.alternative_api_uiv2.components.LauncherTextField;
import fr.trxyy.alternative.alternative_apiv2.base.GameEngine;
import fr.trxyy.alternative.alternative_apiv2.base.IScreen;
import fr.trxyy.alternative.alternative_apiv2.base.LauncherPane;
import fr.trxyy.alternative.alternative_apiv2.updater.GameUpdater;
import fr.trxyy.alternative.alternative_apiv2.utils.FontLoader;
import fr.trxyy.alternative.alternative_auth.account.AccountType;
import fr.trxyy.alternative.alternative_auth.base.GameAuth;
import fr.trxyy.alternative.alternative_authv2.base.Session;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LauncherPanel extends IScreen {
	
	private LauncherLabel titleLabel;
	private LauncherImage titleImage;
	
	private LauncherTextField usernameField;
	private LauncherPasswordField passwordField;
	
	private LauncherButton loginButton;
	private LauncherButton settingsButton;
	private LauncherButton discButton;
	private LauncherButton microsoftButton;
	
	private LauncherButton closeButton;
	private LauncherButton reduceButton;
	
	private LauncherProgressBar progressBar;
	private LauncherLabel updateLabel;
	private Rectangle updateRectangle;
	private Thread updateThread;
	private GameUpdater updater;
	
	private Rectangle loggedRectangle;
	private LauncherImage headImage;
	private LauncherLabel accountLabel;
	
	private GameEngine engine;
	private fr.trxyy.alternative.alternative_api.GameEngine engine2;
	private GameAuth auth2;
	private fr.trxyy.alternative.alternative_authv2.base.GameAuth auth;
	private Session gameSession;
	
	public LauncherPanel(Pane root, GameEngine gameEngine) {
		
		/** ===================== BOUTON FERMER ===================== */
		this.closeButton = new LauncherButton(root);
		this.closeButton.setInvisible();
		this.closeButton.setBounds(gameEngine.getWidth() - 50, -3, 40, 20);
		LauncherImage closeImage = new LauncherImage(root, loadImage(gameEngine, "close.png"));
		closeImage.setSize(40, 20);
		this.closeButton.setGraphic(closeImage);
		this.closeButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
		/** ===================== BOUTON REDUIRE ===================== */
		this.reduceButton = new LauncherButton(root);
		this.reduceButton.setInvisible();
		this.reduceButton.setBounds(gameEngine.getWidth() - 91, -3, 40, 20);
		LauncherImage reduceImage = new LauncherImage(root, loadImage(gameEngine, "reduce.png"));
		reduceImage.setSize(40, 20);
		this.reduceButton.setGraphic(reduceImage);
		this.reduceButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Stage stage = (Stage) ((LauncherButton) event.getSource()).getScene().getWindow();
				stage.setIconified(true);
			}
		});
		
		/** ===================== Titre + Logo ================*/
		              
		this.titleLabel = new LauncherLabel(root);
		this.titleLabel.setText("Omegice Launcher");
		this.titleLabel.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 18F));
		this.titleLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
		this.titleLabel.setOpacity(0.7);
		this.titleLabel.setSize(500, 40);
		this.titleLabel.setPosition(gameEngine.getWidth()/2-80, -4);
		
		this.titleImage = new LauncherImage(root);
		this.titleImage.setImage(getResourceLocation().loadImage(gameEngine, "favicon.png"));
		this.titleImage.setSize(25, 25);
		this.titleImage.setY(3);
		this.titleImage.setX(gameEngine.getWidth()/3+40);
		
		/**====================== Boutton discord ================*/
		
		this.discButton = new LauncherButton(root);
		this.discButton.setInvisible();
		LauncherImage discImg = new LauncherImage(root, getResourceLocation().loadImage(gameEngine, "discord.png"));
		discImg.setSize(40, 40);
		this.discButton.setGraphic(discImg);
		this.discButton.setSize((int)discImg.getFitWidth(), (int)discImg.getFitHeight());
		this.discButton.setPosition(gameEngine.getWidth()/2-430,  gameEngine.getHeight()/2+200);
		this.discButton.setOnAction(event -> {
			openLink("https://discord.gg/adYs325aQJ");
		});
		
		/**======================= Pseudo + Mot de Passe =========================*/
		
		this.usernameField = new LauncherTextField(root);
		this.usernameField.setPosition(gameEngine.getWidth()/2-135, gameEngine.getHeight()/2 -57);
		this.usernameField.setSize(270, 50);
		this.usernameField.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 14F));
		this.usernameField.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-text-fill: white;");
		this.usernameField.setVoidText("Pseudo");
		
		this.passwordField = new LauncherPasswordField(root);
		this.passwordField.setPosition(gameEngine.getWidth()/2-135, gameEngine.getHeight()/2);
		this.passwordField.setSize(270, 50);
		this.passwordField.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 14F));
		this.passwordField.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-text-fill: white;");
		this.passwordField.setVoidText("Mot de passe");
		
		/**======================= Boutton Login =================================*/
		
		this.loginButton = new LauncherButton(root);
		this.loginButton.setText("Se connecter");
		this.loginButton.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 22F));
		this.loginButton.setPosition(gameEngine.getWidth()/2-100, gameEngine.getHeight()/2+60);
		this.loginButton.setSize(200, 45);
		this.loginButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-text-fill: white;");
		this.loginButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				/**
				 * ===================== AUTHENTIFICATION OFFLINE (CRACK) =====================
				 */
				if (usernameField.getText().length() < 1) {
					new LauncherAlert("Le pseudonyme n'est pas assez long (3 caracteres minimum.)",
							"Il y a un probleme lors de la tentative de connexion: Le pseudonyme doit comprendre au moins 3 caracteres.");
				} else if (usernameField.getText().length() > 1 && passwordField.getText().isEmpty()) {
					gameSession =  new  fr.trxyy.alternative.alternative_authv2.base.Session(usernameField.getText(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
					File jsonFile = downloadVersion(gameEngine.getGameLinks().getJsonUrl(), gameEngine);
					update(gameSession, jsonFile);
				}
			}
		});
		
		/** ===================== BOUTON DE CONNEXION MICROSOFT ===================== */
		this.microsoftButton = new LauncherButton("Connexion avec Microsoft", root);
		this.microsoftButton.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 12.5F));
		this.microsoftButton.setBounds(this.engine.getWidth() - 345, this.engine.getHeight() - 33, 190, 20);
		this.microsoftButton.addStyle("-fx-background-color: rgb(230, 230, 230);");
		this.microsoftButton.addStyle("-fx-text-fill: black;");
		this.microsoftButton.addStyle("-fx-border-radius: 0 0 0 0;");
		this.microsoftButton.addStyle("-fx-background-radius: 0 0 0 0;");
		this.microsoftButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				auth2 = new GameAuth(AccountType.MICROSOFT);
				showMicrosoftAuth(gameEngine, auth);
				if (auth.isLogged()) {
					gameSession = auth.getSession();
					File jsonFile = downloadVersion(gameEngine.getGameLinks().getJsonUrl(), gameEngine);
					update(gameSession, jsonFile);
				}
			} 
		});
		
		/**======================= Settings Button ====================================*/
		
		this.settingsButton = new LauncherButton(root);
		this.settingsButton.setBackground(null);
		this.settingsButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-text-fill: white;");
		LauncherImage settingsImg = new LauncherImage(root, getResourceLocation().loadImage(gameEngine, "settings.png"));
		settingsImg.setSize(40, 40);
		this.settingsButton.setGraphic(settingsImg);
		this.settingsButton.setPosition(gameEngine.getWidth()/2+370, gameEngine.getHeight()/2+200);
		this.settingsButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				final JDialog frame = new JDialog();
				frame.setTitle("Modification des parametres");
				frame.setContentPane(new SettingsPanel(gameEngine));
				frame.setResizable(false);
				frame.setModal(true);
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				frame.setSize(630, 210);
				frame.setVisible(true);
			}
			
			private void showMicrosoftAuth(GameAuth auth) {
				 Scene scene = new Scene(createMicrosoftPanel(auth));
				 Stage stage = new Stage();
				 scene.setFill(Color.TRANSPARENT);
				 stage.setResizable(false);
				 stage.setTitle("Microsoft Authentication");
				 stage.setWidth(500);
				 stage.setHeight(600);
				 stage.setScene(scene);
				 stage.initModality(Modality.APPLICATION_MODAL);
				 stage.showAndWait();
				 }
				 
				 private Parent createMicrosoftPanel(GameAuth auth) {
				 LauncherPane contentPane = new LauncherPane(engine);
				 auth.connectMicrosoft(engine2, contentPane);
				 return contentPane;
				 }
			
		});
		
	}

private void update(Session gameSession2, File jsonFile) {		
	this.accountLabel.setText(gameSession.getUsername());
	this.fadeOut(this.loginButton, 300).setOnFinished(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
			loginButton.setVisible(false);
			updateLabel.setVisible(true);
			fadeIn(updateLabel, 300);
			
			progressBar.setVisible(true);
			fadeIn(progressBar, 300);
			
			loggedRectangle.setVisible(true);
			fadeIn(loggedRectangle, 300);
			
			headImage.setImage(new Image("https://minotar.net/helm/" + gameSession.getUsername() + "/120.png"));
			headImage.setVisible(true);
			fadeIn(headImage, 300);
			
			updateRectangle.setVisible(true);
			fadeIn(updateRectangle, 300);
			
			loggedRectangle.setVisible(true);
			fadeIn(loggedRectangle, 300);
			
			accountLabel.setVisible(true);
			fadeIn(accountLabel, 300);
	    }
	});
	this.fadeOut(this.usernameField, 300).setOnFinished(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
	    	usernameField.setVisible(false);
	    }
	});
	this.fadeOut(this.passwordField, 300).setOnFinished(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
	    	passwordField.setVisible(false);
	    }
	});
	this.fadeOut(this.settingsButton, 300).setOnFinished(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
	    	settingsButton.setVisible(false);
	    }
	});
	
		this.updateThread = new Thread() {
		public void run() {
			updater = new GameUpdater(prepareGameUpdate(updater, engine, gameSession, jsonFile), engine);
			engine.reg(updater);
			Timeline t = new Timeline( new KeyFrame[] { new KeyFrame(Duration.seconds(0.0D), new EventHandler<ActionEvent>() {
						public void handle(ActionEvent event) {
							double percent = (engine.getGameUpdater().downloadedFiles * 100.0D / engine.getGameUpdater().filesToDownload / 100.0D);
							progressBar.setProgress(percent);
							updateLabel.setText(engine.getGameUpdater().getUpdateText());
						}
					}, new KeyValue[0]), new KeyFrame(Duration.seconds(0.0D))});
			t.setCycleCount(Animation.INDEFINITE);
			t.play();
			downloadGameAndRun(updater, gameSession);
		}
	};
	this.updateThread.start();
}}