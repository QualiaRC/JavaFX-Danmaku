package me.ryan_clark.gui;

import me.ryan_clark.logic.ai.EnemyPath;
import me.ryan_clark.logic.entities.*;
import me.ryan_clark.app.Prefs;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class GuiController implements Initializable {

	@FXML
	private Pane gamePanel;

	@FXML
	private Pane scorePanel;

	@FXML
	private Pane mainMenuPanel;

	@FXML
	private Group menuButton1;

	@FXML
	private Group menuButton2;

	@FXML
	private Group menuButton3;

	private Group pauseScreen = new Group();

	private AnimationTimer timer = null;
	private boolean paused = false;
	private boolean canPause = true;

	private final BooleanProperty isPaused = new SimpleBooleanProperty();
	private final BooleanProperty isGameOver = new SimpleBooleanProperty();

	private final long[] frameTimes = new long[100];
	private int frameTimeIndex = 0;
	private boolean arrayFilled = false;

	// Debug stuff
	private boolean debugActive = true;

	private Group debugGroup = new Group();

	private int enemyIndex = 0;
	private int powerupIndex = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		gamePanel.setVisible(false);

		mainMenuPanel.setFocusTraversable(true);
		mainMenuPanel.requestFocus();

		gamePanel.setOnKeyPressed((KeyEvent event) -> {
			if (isPaused.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {

				if (event.getCode() == KeyCode.UP) {
					EntityFactory.getPlayer().setUp(true);
				}
				if (event.getCode() == KeyCode.DOWN) {
					EntityFactory.getPlayer().setDown(true);
				}
				if (event.getCode() == KeyCode.LEFT) {
					EntityFactory.getPlayer().setLeft(true);
				}
				if (event.getCode() == KeyCode.RIGHT) {
					EntityFactory.getPlayer().setRight(true);
				}
				if (event.getCode() == KeyCode.SHIFT) {
					EntityFactory.getPlayer().setSlow(true);
				}
				if (event.getCode() == KeyCode.Z) {
					EntityFactory.getPlayer().setShoot(true);
				}
				if (event.getCode() == KeyCode.ESCAPE && canPause) {
					canPause = false;
					System.out.println("paused = " + paused);
					paused = !paused;
					if (paused) {
						timer.stop();
						pauseScreen.setVisible(true);
					} else {
						timer.start();
						pauseScreen.setVisible(false);
					}
				}
			}
		});

		gamePanel.setOnKeyReleased((KeyEvent event) -> {
			if (isPaused.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {

				if (event.getCode() == KeyCode.UP) {
					EntityFactory.getPlayer().setUp(false);
				}
				if (event.getCode() == KeyCode.DOWN) {
					EntityFactory.getPlayer().setDown(false);
				}
				if (event.getCode() == KeyCode.LEFT) {
					EntityFactory.getPlayer().setLeft(false);
				}
				if (event.getCode() == KeyCode.RIGHT) {
					EntityFactory.getPlayer().setRight(false);
				}
				if (event.getCode() == KeyCode.SHIFT) {
					EntityFactory.getPlayer().setSlow(false);
				}
				if (event.getCode() == KeyCode.Z) {
					EntityFactory.getPlayer().setShoot(false);
				}
				if (event.getCode() == KeyCode.TAB) {
					debugActive = !debugActive;
					setDebug(debugActive);
				}
				if (event.getCode() == KeyCode.SPACE && !paused) {
					spawnEnemy();
				}
				if (event.getCode() == KeyCode.ESCAPE) {
					canPause = true;
				}
			}
		});
	}

	public void initGameView() {

		ImageView title = new ImageView("res/images/title.png");
		title.setLayoutX(20);
		title.setLayoutY(20);

		Group startGameButton = new Group();
		Label menuLabel1 = new Label("Start Game");
		menuLabel1.setAlignment(Pos.CENTER);
		menuLabel1.setFont(new Font(null, 60));
		menuLabel1.setStyle("-fx-background-color: white;");
		menuLabel1.setPrefSize(360, 120);

		ImageView menuButton1 = new ImageView("res/images/border3.png");
		startGameButton.getChildren().addAll(menuLabel1, menuButton1);
		startGameButton.setLayoutX(20);
		startGameButton.setLayoutY(Prefs.BOARD_Y - 420);
		startGameButton.setOnMousePressed(event -> {
			startGame();
		});
		startGameButton.setOnMouseEntered(event -> {
			startGameButton.getChildren().get(0).setStyle("-fx-background-color: lightgray;");
		});
		startGameButton.setOnMouseExited(event -> {
			startGameButton.getChildren().get(0).setStyle("-fx-background-color: white;");
		});
		
		Group nullButton = new Group();
		Label menuLabel2 = new Label("???");
		menuLabel2.setAlignment(Pos.CENTER);
		menuLabel2.setFont(new Font(null, 60));
		menuLabel2.setStyle("-fx-background-color: white;");
		menuLabel2.setPrefSize(360, 120);
		ImageView menuButton2 = new ImageView("res/images/border3.png");
		nullButton.getChildren().addAll(menuLabel2, menuButton2);
		nullButton.setLayoutX(20);
		nullButton.setLayoutY(Prefs.BOARD_Y - 280);
		nullButton.setOnMousePressed(event -> {
			System.out.println("This does nothing lol");
		});
		nullButton.setOnMouseEntered(event -> {
			nullButton.getChildren().get(0).setStyle("-fx-background-color: lightgray;");
		});
		nullButton.setOnMouseExited(event -> {
			nullButton.getChildren().get(0).setStyle("-fx-background-color: white;");
		});

		Group exitGameButton = new Group();
		Label menuLabel3 = new Label("Quit Game");
		menuLabel3.setAlignment(Pos.CENTER);
		menuLabel3.setFont(new Font(null, 60));
		menuLabel3.setStyle("-fx-background-color: white;");
		menuLabel3.setPrefSize(360, 120);
		ImageView menuButton3 = new ImageView("res/images/border3.png");
		exitGameButton.getChildren().addAll(menuLabel3, menuButton3);
		exitGameButton.setLayoutX(20);
		exitGameButton.setLayoutY(Prefs.BOARD_Y - 140);
		exitGameButton.setOnMousePressed(event -> {
			System.exit(0);
		});
		exitGameButton.setOnMouseEntered(event -> {
			exitGameButton.getChildren().get(0).setStyle("-fx-background-color: lightgray;");
		});
		exitGameButton.setOnMouseExited(event -> {
			exitGameButton.getChildren().get(0).setStyle("-fx-background-color: white;");
		});

		mainMenuPanel.getChildren().addAll(title, startGameButton, nullButton, exitGameButton);

	}

	public void startGame() {
		EntityFactory.getPlayer().spawn();

		mainMenuPanel.setVisible(false);
		mainMenuPanel.setFocusTraversable(false);

		gamePanel.setVisible(true);
		gamePanel.setFocusTraversable(true);
		gamePanel.requestFocus();

		// Set *transparent* background for game board
		Rectangle background = new Rectangle(Prefs.BOARD_X, Prefs.BOARD_Y);
		gamePanel.setMaxSize(Prefs.BOARD_X, Prefs.BOARD_Y);
		background.setFill(Color.TRANSPARENT);

		// Set clip region as inner rectangle (Nothing renders outside of this region)
		gamePanel.setClip(new Rectangle(Prefs.BOARD_X, Prefs.BOARD_Y));

		// Add all entities to the game board

		for (PlayerBullet b : EntityFactory.getPlayerBullets()) {
			gamePanel.getChildren().addAll(b.getShape());
		}
		for (Enemy e : EntityFactory.getEnemies()) {
			for (EnemyBullet b : EntityFactory.getEnemyBullets(e.getId())) {
				gamePanel.getChildren().addAll(b.getShape());
			}
			gamePanel.getChildren().addAll(e.getShape(), e.getHealthBar());
		}
		for (Powerup p : EntityFactory.getPowerups()) {
			gamePanel.getChildren().addAll(p.getShape());
		}

		gamePanel.getChildren().addAll(background, EntityFactory.getPlayer().getShape(),
				EntityFactory.getPlayer().getHitBox(), EntityFactory.getBossBar().getBar());
		
		// Debug labels
		Rectangle debugBackground = new Rectangle(120, 60);
		debugBackground.setFill(Color.WHITE);
		Label debugFPS = new Label("FPS: --");
		debugFPS.setLayoutX(5);
		Label debugPlayerBullets = new Label("Player Bullets: 0");
		debugPlayerBullets.setLayoutY(10);
		debugPlayerBullets.setLayoutX(5);
		Label debugEnemyBullets = new Label("Enemy Bullets: 0");
		debugEnemyBullets.setLayoutY(20);
		debugEnemyBullets.setLayoutX(5);
		Label debugAllBullets = new Label("Total Bullets: 0");
		debugAllBullets.setLayoutY(30);
		debugAllBullets.setLayoutX(5);
		Label debugNumEnemies = new Label("Enemies: 0");
		debugNumEnemies.setLayoutY(40);
		debugNumEnemies.setLayoutX(5);
		debugGroup.getChildren().addAll(debugBackground, debugFPS, debugPlayerBullets, debugEnemyBullets,
				debugAllBullets, debugNumEnemies);

		// Debug zone areas
		Rectangle powerupLine = new Rectangle(Prefs.BOARD_X, 1);
		powerupLine.setY(Prefs.SUCC_ZONE_Y);
		Label powerupLabel = new Label("POWERUP ZONE");
		powerupLabel.setLayoutY(Prefs.SUCC_ZONE_Y - 15);
		powerupLabel.setPrefWidth(200);
		powerupLabel.setAlignment(Pos.CENTER);
		powerupLabel.setLayoutX(Prefs.BOARD_X / 2 - 100);

		debugGroup.getChildren().addAll(powerupLine, powerupLabel);
		debugGroup.setOpacity(1);

		gamePanel.getChildren().addAll(debugGroup);

		// Set up pause menu
		pauseScreen = new Group();
		Rectangle pauseBackground = new Rectangle(Prefs.BOARD_X, Prefs.BOARD_Y);
		pauseBackground.setOpacity(0.25);
		Rectangle pauseLabelBackground = new Rectangle(60, 20);
		pauseLabelBackground.setFill(Color.WHITE);
		pauseLabelBackground.setOpacity(0.75);
		pauseLabelBackground.setLayoutX(Prefs.BOARD_X / 2 - 30);
		pauseLabelBackground.setLayoutY(Prefs.BOARD_Y / 2 - 10);
		Label pauseLabel = new Label("PAUSED");
		pauseLabel.setPrefSize(Prefs.BOARD_X, Prefs.BOARD_Y);
		pauseLabel.setAlignment(Pos.CENTER);
		pauseScreen.getChildren().addAll(pauseBackground, pauseLabelBackground, pauseLabel);
		pauseScreen.setVisible(false);
		gamePanel.getChildren().addAll(pauseScreen);

		// Cheats
		Label debugButton = new Label("Display Debug (tab)");
		debugButton.setOnMousePressed(event -> {
			debugActive = !debugActive;
			setDebug(debugActive);
		});
		debugButton.setLayoutY(Prefs.BOARD_Y - 25);
		debugButton.setStyle("-fx-background-color: white;");
		debugButton.setPrefSize(130, 25);
		debugButton.setAlignment(Pos.CENTER);

		Label powerupButton1 = new Label("spawn powerup (+1)");
		powerupButton1.setOnMousePressed(event -> {
			spawnPowerup(1);
		});
		powerupButton1.setLayoutY(150);
		powerupButton1.setStyle("-fx-background-color: white;");
		powerupButton1.setPrefSize(130, 25);

		Label powerupButton5 = new Label("spawn powerup (+5)");
		powerupButton5.setOnMousePressed(event -> {
			spawnPowerup(5);
		});
		powerupButton5.setLayoutY(175);
		powerupButton5.setStyle("-fx-background-color: gray;");
		powerupButton5.setPrefSize(130, 25);

		Label powerupButton10 = new Label("spawn powerup (+10)");
		powerupButton10.setOnMousePressed(event -> {
			spawnPowerup(10);
		});
		powerupButton10.setLayoutY(200);
		powerupButton10.setStyle("-fx-background-color: white;");
		powerupButton10.setPrefSize(130, 25);

		Label powerupButton20 = new Label("spawn powerup (+20)");
		powerupButton20.setOnMousePressed(event -> {
			spawnPowerup(20);
		});
		powerupButton20.setLayoutY(225);
		powerupButton20.setStyle("-fx-background-color: gray;");
		powerupButton20.setPrefSize(130, 25);

		Label enemyButton1 = new Label("spawn enemy (space) ");
		enemyButton1.setOnMousePressed(event -> {
			spawnEnemy();
		});
		enemyButton1.setLayoutY(275);
		enemyButton1.setStyle("-fx-background-color: white;");
		enemyButton1.setPrefSize(130, 25);

		EntityFactory.getPlayer().getScoreGui().setLayoutX(15);
		EntityFactory.getPlayer().getScoreGui().setLayoutY(15);

		scorePanel.getChildren().addAll(EntityFactory.getPlayer().getScoreGui(), powerupButton1, powerupButton5,
				powerupButton10, powerupButton20, enemyButton1, debugButton);

		// Game timer
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update(now);
			}
		};
		timer.start();
	}

	private void update(long now) {

		// debug counters
		int bulletCountPlayer = 0;
		int bulletCountEnemy = 0;
		int numEnemies = 0;

		/////////////////////////
		// Update all entities
		/////////////////////////

		// Update Player
		EntityFactory.getPlayer().update();

		// Update Player Bullets
		for (PlayerBullet b : EntityFactory.getPlayerBullets()) {
			if (b.isActive()) {
				b.update();
				if (debugActive) {
					bulletCountPlayer++;
				}
			}
		}

		// Update active Enemies and active Enemy Bullets
		for (Enemy e : EntityFactory.getEnemies()) {
			if (e.isActive()) {
				numEnemies++;
				e.update();
			}
			for (EnemyBullet b : EntityFactory.getEnemyBullets(e.getId())) {
				if (b.isActive()) {
					b.update();
					if (debugActive) {
						bulletCountEnemy++;
					}
				}
			}
		}

		// Update active Powerups
		for (Powerup p : EntityFactory.getPowerups()) {
			if (p.isActive()) {
				p.update();
			}
		}

		/////////////////////////
		// Update debug labels
		/////////////////////////

		if (debugActive) {
			((Label) debugGroup.getChildren().get(2)).setText("Player Bullets: " + bulletCountPlayer);
			((Label) debugGroup.getChildren().get(3)).setText("Enemy Bullets: " + bulletCountEnemy);
			((Label) debugGroup.getChildren().get(4))
					.setText("Total Bullets: " + (bulletCountPlayer + bulletCountEnemy));
			((Label) debugGroup.getChildren().get(5)).setText("Enemies:" + numEnemies);
		}

		// Update frame counter
		long oldFrameTime = frameTimes[frameTimeIndex];
		frameTimes[frameTimeIndex] = now;
		frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
		if (frameTimeIndex == 0) {
			arrayFilled = true;
		}
		if (arrayFilled) {
			long elapsedNanos = now - oldFrameTime;
			long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
			double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
			if (debugActive) {
				((Label) debugGroup.getChildren().get(1)).setText(String.format("FPS: %.3f", frameRate));
			}
		}
	}

	private void setDebug(boolean b) {
		debugGroup.setVisible(b);
	}

	private void spawnEnemy() {
		if (enemyIndex++ >= Prefs.MAX_ENEMIES - 1) {
			enemyIndex = 0;
		}
		if (!EntityFactory.getEnemies()[enemyIndex].isActive()) {
			/*
			 * boolean even = false; if(enemyIndex %2 == 0) { even = true; } if(even) {
			 * EntityFactory.getEnemies()[enemyIndex].reset(EnemyPath.ENEMY_01); } else {
			 * EntityFactory.getEnemies()[enemyIndex].reset(EnemyPath.ENEMY_02); }
			 */

			EntityFactory.getEnemies()[enemyIndex].reset(EnemyPath.ENEMY_03);
			//EntityFactory.getEnemies()[enemyIndex].spawnBoss(1);
			

		}
	}

	private void spawnPowerup(int i) {
		if (powerupIndex++ >= Prefs.MAX_POWERUPS - 1) {
			powerupIndex = 0;
		}
		if (!EntityFactory.getPowerups()[powerupIndex].isActive()) {
			EntityFactory.getPowerups()[powerupIndex].reset(i);
		}
	}
}
