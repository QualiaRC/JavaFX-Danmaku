package me.ryan_clark.logic.entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import me.ryan_clark.app.Prefs;

public class Player extends Entity {

	private int prevPointProgress = 0;
	private int pointTimer = 0;
	private boolean addingPoints = false;

	private int pointProgress = 0;
	private int level = 1;

	private boolean moveUp = false;
	private boolean moveDown = false;
	private boolean moveLeft = false;
	private boolean moveRight = false;

	private boolean isSpawning = true;
	private int spawnTimer = 0;

	private boolean slow = false;
	private boolean isShooting = false;
	private int shootCounter = 0;

	private int shootTimer;

	private Group scoreGui = new Group();
	private Arc arc;

	private ImageView playerHitbox = new ImageView("res/images/player_hitbox.png");

	private StringProperty pointString = new SimpleStringProperty();
	private StringProperty levelString = new SimpleStringProperty();

	public Player() {
		
		playerHitbox.layoutXProperty().bind(layoutXProperty.subtract(2));
		playerHitbox.layoutYProperty().bind(layoutYProperty.subtract(2));

		setPos(200, 500);
		setType(EntityType.PLAYER);

		// TODO: Create separate class for score gui
		initScoreGui();

		spawn();
	}

	public void spawn() {
		setPos(200, Prefs.BOARD_Y + 10);
		isSpawning = true;
		spawnTimer = 32;
	}

	public void setUp(boolean b) {
		moveUp = b;
	}

	public void setDown(boolean b) {
		moveDown = b;
	}

	public void setLeft(boolean b) {
		moveLeft = b;
	}

	public void setRight(boolean b) {
		moveRight = b;
	}

	public void setSlow(boolean b) {
		slow = b;
	}

	public void setShoot(boolean b) {
		isShooting = b;
	}

	@Override
	public void update() {

		if (isSpawning) {
			if (spawnTimer-- <= 0) {
				isSpawning = false;
			} else {
				setY(getY() - Prefs.PLAYER_SPEED);
			}
		} else {

			// point stuff
			if (addingPoints) {
				updateScoreGui();
				pointTimer++;
			}

			int dY = 0;
			int dX = 0;
			double newX = 0;
			double newY = 0;
			int dS = Prefs.PLAYER_SPEED;

			if (slow) {
				dS = Prefs.PLAYER_SPEED_SLOW;
			}

			if (moveUp) {
				dY -= dS;
			}
			if (moveDown) {
				dY += dS;
			}
			if (moveLeft) {
				dX -= dS;
			}
			if (moveRight) {
				dX += dS;
			}

			// Update position accordingly

			newX = getX() + dX;
			newY = getY() + dY;

			// Check for wall collision
			if (newX - Prefs.PLAYER_RADIUS < 0) {
				newX = Prefs.PLAYER_RADIUS;
			}
			if (newY - Prefs.PLAYER_RADIUS < 0) {
				newY = Prefs.PLAYER_RADIUS;
			}
			if (newX + Prefs.PLAYER_RADIUS > Prefs.BOARD_X) {
				newX = (Prefs.BOARD_X - Prefs.PLAYER_RADIUS);
			}
			if (newY + Prefs.PLAYER_RADIUS > Prefs.BOARD_Y) {
				newY = (Prefs.BOARD_Y - Prefs.PLAYER_RADIUS);
			}

			setX(newX);
			setY(newY);

			// Handle shooting
			if (isShooting) {
				if (shootTimer > 0) {
					shootTimer--;
				} else {
					incrementShootCounter();
					EntityFactory.getPlayerBullets()[shootCounter].shoot();
					shootTimer = Prefs.SHOOT_DELAY[level];
				}
			}

		}
	}

	private void incrementShootCounter() {
		if (shootCounter++ >= Prefs.MAX_PLAYER_BULLETS - 1) {
			shootCounter = 0;
		}
	}

	public void addPoints(int i) {
		if (level != Prefs.LEVEL_CAP) {
			prevPointProgress = pointProgress;
			pointProgress += i;

			addingPoints = true;

			while (pointProgress >= Prefs.EXP_CURVE[level]) {
				pointProgress -= Prefs.EXP_CURVE[level];
				levelUp();
			}

			pointTimer = 0;

			System.out.println(String.format("%d / %d", pointProgress, Prefs.EXP_CURVE[level]));
		}
	}

	private int pointProgress() {
		return pointProgress * 100 / Prefs.EXP_CURVE[level];
	}

	private int prevPointProgress() {
		return prevPointProgress * 100 / Prefs.EXP_CURVE[level];
	}

	private void levelUp() {
		if (level != Prefs.LEVEL_CAP) {
			System.out.println(String.format("Player leveled up! %d -> %d", level, level + 1));
			shootTimer = 0;
			level++;
			levelString.set("Level: " + level);
			if (level == Prefs.LEVEL_CAP) {
				levelString.set("Level: MAX");
				pointString.set("100%");
				arc.setLength(360);
				addingPoints = false;
			}
		}
	}

	private void initScoreGui() {
		pointString.set("0%");
		levelString.set("Level: 1");

		arc = new Arc(50, 50, 50, 50, 90, 0);
		arc.setType(ArcType.ROUND);
		RadialGradient gradient1 = new RadialGradient(0, .1, 50, 50, 80, false, CycleMethod.NO_CYCLE,
				new Stop(0, Color.DODGERBLUE), new Stop(1, Color.BLACK));
		RadialGradient gradient2 = new RadialGradient(0, .1, 50, 50, 50, false, CycleMethod.NO_CYCLE,
				new Stop(0, Color.WHITE), new Stop(1, Color.GRAY));
		arc.setFill(gradient1);

		Shape outerCircle = Shape.subtract(new Circle(50, 50, 50), new Circle(50, 50, 49));
		Circle innerCircle = new Circle(50, 50, 30);
		innerCircle.setFill(gradient2);

		Label pointCounter = new Label();
		pointCounter.setPrefWidth(100);
		pointCounter.setPrefHeight(100);
		pointCounter.setAlignment(Pos.CENTER);
		pointCounter.textProperty().bind(pointString);

		Label levelCounter = new Label();
		levelCounter.setPrefWidth(100);
		levelCounter.setLayoutY(100);
		levelCounter.setAlignment(Pos.CENTER);
		levelCounter.textProperty().bind(levelString);

		scoreGui.getChildren().addAll(arc, outerCircle, new Circle(50, 50, 31), innerCircle, pointCounter,
				levelCounter);
	}

	private void updateScoreGui() {
		if (pointTimer > 16) {
			addingPoints = false;
		} else {
			pointString.set(
					(int) (prevPointProgress() + pointTimer / 16f * (pointProgress() - prevPointProgress())) + "%");
			arc.setLength(
					-360 * (prevPointProgress() + pointTimer / 16f * (pointProgress() - prevPointProgress())) / 100);
		}
	}

	public Node getScoreGui() {
		return scoreGui;
	}

	public Node getHitBox() {
		return playerHitbox;
	}

}
