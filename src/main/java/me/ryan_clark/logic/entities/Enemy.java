package me.ryan_clark.logic.entities;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import me.ryan_clark.app.Prefs;
import me.ryan_clark.logic.ai.EnemyPath;
import me.ryan_clark.logic.ai.PathDriver;

public class Enemy extends Entity {

	private int enemyId;

	private int shootCounter = 0;
	private int shootTimer;
	private int burstTimer;
	private boolean burstOn = true;

	private int maxHp = 10;
	private int currentHp = 1;
	private Group healthBar = new Group();

	private int timer = 0;
	private double speed = 3f;

	// Path stuff
	private EnemyPath path = EnemyPath.ENEMY_01;
	private double queueX = 0;
	private double queueY = 0;
	private int queueNum = 0;
	private int queueTimer = 0;

	private BulletPattern bulletPattern = BulletPattern.DOUBLE_SPIRAL;

	private double shootAngle = 270;
	
	private StringProperty name = new SimpleStringProperty();
	private DoubleProperty hpPercent = new SimpleDoubleProperty();

	public Enemy(int i) {
		name.set("");
		enemyId = i;
		setPos(-200, -200);
		setType(EntityType.ENEMY);
		setVisible(false);

		initHealthBar();

	}

	void initHealthBar() {
		Rectangle healthBack = new Rectangle(20, 4);
		healthBack.setFill(Color.RED);
		healthBack.setX(1);
		healthBack.setY(1);
		Rectangle healthCurrent = new Rectangle(20, 4);
		healthCurrent.setFill(Color.web("#00ff00"));
		healthCurrent.setX(1);
		healthCurrent.setY(1);

		healthBar.getChildren().addAll(new Rectangle(22, 6), healthBack, healthCurrent);
		healthBar.layoutXProperty().bind((getShape().layoutXProperty().subtract(11 - 5)));
		healthBar.layoutYProperty().bind((getShape().layoutYProperty().subtract(10)));
		healthBar.setVisible(true);
	}

	@Override
	public void update() {

		// Handle moving
		PathDriver.updatePosition(this);

		// Handle shooting
		shoot();

		// Handle out of bounds
		if (getX() < -20 || getX() > Prefs.BOARD_X + 20 || getY() < -20 || getY() > Prefs.BOARD_Y + 20) {
			System.out.println("Enemy " + enemyId + " went out of bounds.");
			kill();
		}

		timer++;
	}

	private void shoot() {
		switch (bulletPattern) {
		case HOMING:
			if (shootTimer > 0) {
				shootTimer--;
			} else {
				incrementShootCounter();
				EntityFactory.getEnemyBullets(enemyId)[shootCounter].shoot(5,
						Math.toDegrees(Math.atan2(EntityFactory.getPlayer().getX() - getX(),
								EntityFactory.getPlayer().getY() - getY())) - 90);
				shootTimer = 15;
			}
			break;
		case HOMING_BURST:
			if (burstTimer > 0) {
				burstTimer--;
			} else {
				burstTimer = 15;
				burstOn = !burstOn;
				shootAngle = Math.toDegrees(Math.atan2(EntityFactory.getPlayer().getX() - getX(),
						EntityFactory.getPlayer().getY() - getY())) - 90;
			}
			if (burstOn) {
				if (shootTimer > 0) {
					shootTimer--;
				} else {
					incrementShootCounter();
					EntityFactory.getEnemyBullets(enemyId)[shootCounter].shoot(10, shootAngle);
					shootTimer = 1;
				}
			}
			break;
		case HOMING_FAN:
			if (shootTimer > 0) {
				shootTimer--;
			} else {
				for (int i = 0; i < 20; i++) {
					incrementShootCounter();
					EntityFactory.getEnemyBullets(enemyId)[shootCounter].shoot(4,
							Math.toDegrees(Math.atan2(EntityFactory.getPlayer().getX() - getX(),
									EntityFactory.getPlayer().getY() - getY())) - 80 - i);
				}
				shootTimer = 25;
			}
			break;
		case SPIRAL:
			incrementShootCounter();
			shootAngle -= 10;
			EntityFactory.getEnemyBullets(enemyId)[shootCounter].shoot(2, shootAngle);
			break;
		case SPIRAL_REVERSE:
			incrementShootCounter();
			shootAngle = 10;
			EntityFactory.getEnemyBullets(enemyId)[shootCounter].shoot(2, shootAngle);
			break;
		case DOUBLE_SPIRAL:
			incrementShootCounter();
			shootAngle -= 10.25;
			EntityFactory.getEnemyBullets(enemyId)[shootCounter].shoot(2, shootAngle);
			incrementShootCounter();
			shootAngle += 180;
			EntityFactory.getEnemyBullets(enemyId)[shootCounter].shoot(2, shootAngle);
			break;
		case STRAIGHT:
			if (shootTimer > 0) {
				shootTimer--;
			} else {
				incrementShootCounter();
				EntityFactory.getEnemyBullets(enemyId)[shootCounter].shoot();
				shootTimer = 15;
			}
			break;
		default:
			break;
		}
	}

	public void reset(EnemyPath ep) {
		name.set("");
		path = ep;
		timer = 0;
		currentHp = maxHp;
		hpPercent.set(1.0);
		((Rectangle) healthBar.getChildren().get(2)).setWidth(20);
		setPos(-10, -10);
		isActive = true;
		setVisible(true);
		if (maxHp == 1) {
			healthBar.setVisible(false);
		} else {
			healthBar.setVisible(true);
		}
		queueX = 0;
		queueY = 0;
		queueNum = 0;
		queueTimer = 0;
	}
	
	public void spawnBoss(int i) {
		maxHp = 1000;
		reset(EnemyPath.ENEMY_03);
		healthBar.setVisible(false);
		name.set("boss boi");
		EntityFactory.getBossBar().setBoss(this);
	}

	public void kill() {
		setVisible(false);
		isActive = false;
		setPos(-200, -200);
		healthBar.setVisible(false);
	}

	private void incrementShootCounter() {
		if (shootCounter++ >= Prefs.MAX_ENEMY_BULLETS - 1) {
			shootCounter = 0;
		}
	}

	public final int getId() {
		return enemyId;
	}

	public final Node getHealthBar() {
		return healthBar;
	}

	public void damage(int i) {
		currentHp -= i;
		hpPercent.set((float) currentHp / (float) maxHp);
		((Rectangle) healthBar.getChildren().get(2)).setWidth((int) (((float) currentHp / (float) maxHp) * 20));
		if (currentHp <= 0) {
			kill();
		}
	}

	public final int getTime() {
		return timer;
	}

	public final EnemyPath getPath() {
		return path;
	}

	public final double getSpeed() {
		return speed;
	}

	public final int getQueue() {
		return queueNum;
	}

	public final double[] getQueuePos() {
		return new double[] { queueX, queueY };
	}

	public final int getQueueTimer() {
		return queueTimer;
	}

	public void endQueue(double x, double y) {
		setPos(x, y);
		queueX = x;
		queueY = y;
		endQueue();
	}

	public void endQueue() {
		queueNum++;
		queueTimer = timer;
	}

	public void setBulletPattern(BulletPattern bp) {
		if (bulletPattern != bp) {
			bulletPattern = bp;
			shootTimer = 0;
		}
	}
	
	public StringProperty getName() {
		return name;
	}
	
	public DoubleProperty getHealthProperty() {
		return hpPercent;
	}
}
