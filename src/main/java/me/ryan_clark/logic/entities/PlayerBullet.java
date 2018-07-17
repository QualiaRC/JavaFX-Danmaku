package me.ryan_clark.logic.entities;

public class PlayerBullet extends Entity {

	public PlayerBullet() {
		setPos(-100, -100);
		setType(EntityType.P_BULLET);
		setVisible(false);
	}

	public void reset() {
		setPos(-100, -100);
		isActive = false;
		setVisible(false);
	}

	public void shoot() {
		setX(EntityFactory.getPlayer().getX());
		setY(EntityFactory.getPlayer().getY());
		isActive = true;
		setVisible(true);
	}

	@Override
	public void update() {
		// Check if OOB
		if (getY() < -10) {
			reset();
		}

		// Shooting behavior
		if (isActive) {
			setY(getY() - 15);

			checkCollision();
		}
	}

	private void checkCollision() {
		for (Enemy e : EntityFactory.getEnemies()) {
			if (e.isActive()) {
				if (Math.pow(e.getX() - getX(), 2) + Math.pow(e.getY() - getY(), 2) < Math.pow(10, 2)) {
					e.damage(1);
					reset();
				}
			}
		}
	}
}
