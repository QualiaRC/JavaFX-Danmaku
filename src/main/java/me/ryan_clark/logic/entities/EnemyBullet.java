package me.ryan_clark.logic.entities;

import me.ryan_clark.app.Prefs;

public class EnemyBullet extends Entity {

	private int enemyId = -1;
	private double degrees = 270;
	private double speed = 5;

	public EnemyBullet(int i) {
		enemyId = i;
		setPos(-100, -100);
		setType(EntityType.E_BULLET);
		setVisible(false);
	}

	public void reset() {
		setPos(-100, -100);
		isActive = false;
		setVisible(false);
	}

	public void shoot() {
		setX(EntityFactory.getEnemies()[enemyId].getX());
		setY(EntityFactory.getEnemies()[enemyId].getY());
		isActive = true;
		setVisible(true);
	}

	public void shoot(double offsetX) {
		setX(EntityFactory.getEnemies()[enemyId].getX() + offsetX);
		setY(EntityFactory.getEnemies()[enemyId].getY());
		isActive = true;
		setVisible(true);
	}

	public void shoot(double speed, double degrees) {
		this.speed = speed;
		this.degrees = degrees;
		shoot();
	}

	@Override
	public void update() {
		// Check if OOB
		if (getY() < -10 || getY() > Prefs.BOARD_Y + 10 || getX() < -10 || getX() > Prefs.BOARD_X + 10) {
			reset();
		}

		// Shooting behavior
		if (isActive) {
			setY(getY() - speed * Math.sin(Math.toRadians(degrees)));
			setX(getX() + speed * Math.cos(Math.toRadians(degrees)));
		}
	}

}
