package me.ryan_clark.logic.entities;

import java.util.Random;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import me.ryan_clark.app.Prefs;

public class Powerup extends Entity {

	private final Rectangle2D[] cellClips;
	private int timer = 2;
	private int currentFrame = 0;
	private int speed = 6;
	private boolean homing;
	private int points = 1;

	public Powerup() {
		setPos(200, 400);
		setType(EntityType.POWER_UP);
		adjustImage(-5, -5);
		setImage(Prefs.SPRITE_POWERUP);
		setVisible(false);

		cellClips = new Rectangle2D[24];
		for (int i = 0; i < 24; i++) {
			cellClips[i] = new Rectangle2D(i * 20, 0, 20, 20);
		}
		for (int i = 0; i < 12; i++) {
			cellClips[i + 12] = new Rectangle2D(i * 20, 20, 20, 20);
		}
		((ImageView) getShape()).setViewport(cellClips[23]);

	}

	public void update() {
		if (timer-- <= 0) {
			timer = 2;
			nextFrame();
		}

		if (getY() < -10) {
			kill();
		}

		checkPlayer();
		if (!homing) {
			setY(getY() - 1);
		}
	}

	private void nextFrame() {
		if (currentFrame++ >= 23) {
			currentFrame = 0;
		}
		((ImageView) getShape()).setViewport(cellClips[currentFrame]);
	}

	private void checkPlayer() {
		double pX = EntityFactory.getPlayer().getX();
		double pY = EntityFactory.getPlayer().getY();
		double dist = Math.pow(getX() - pX, 2) + Math.pow(getY() - pY, 2);
		if (dist < 16) {
			EntityFactory.getPlayer().addPoints(points);
			kill();
		} else if (dist < Math.pow(Prefs.SUCC_RADIUS, 2) || pY < Prefs.SUCC_ZONE_Y || homing == true) {
			homing = true;
			double angle = Math.atan2(pX - getX(), pY - getY());
			setPos(getX() + Math.sin(angle) * speed, getY() + Math.cos(angle) * speed);
		}
	}

	private void kill() {
		isActive = false;
		setVisible(false);
	}

	public void reset(int p) {
		points = p;
		homing = false;
		isActive = true;
		Random r = new Random();
		setPos(r.nextInt(Prefs.BOARD_X), 400);
		setVisible(true);
	}

}
