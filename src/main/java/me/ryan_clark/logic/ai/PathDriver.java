package me.ryan_clark.logic.ai;

import me.ryan_clark.app.Prefs;
import me.ryan_clark.logic.entities.Enemy;

public class PathDriver {

	public static void updatePosition(Enemy enemy) {
		switch (enemy.getPath()) {
		case ENEMY_01:
			enemy1(enemy);
			break;
		case ENEMY_02:
			enemy2(enemy);
			break;
		case ENEMY_03:
			enemy3(enemy);
			break;
		case ENEMY_04:
			break;
		case ENEMY_05:
			break;
		case ENEMY_06:
			break;
		default:
			break;
		}
	}

	private static void enemy1(Enemy enemy) {
		switch (enemy.getQueue()) {
		case 0:
			start(enemy, -10, -10);
			break;
		case 1:
			// enemy.setBulletPattern(BulletPattern.STRAIGHT);
			moveLine(enemy, 100, 100);
			break;
		case 2:
			// enemy.setBulletPattern(BulletPattern.HOMING_FAN);
			wait(enemy, 300);
			break;
		case 3:
			// enemy.setBulletPattern(BulletPattern.STRAIGHT);
			moveLine(enemy, 500, 500);
			break;
		default:
			break;
		}
	}

	private static void enemy2(Enemy enemy) {
		switch (enemy.getQueue()) {
		case 0:
			start(enemy, Prefs.BOARD_X - 10, -10);
			break;
		case 1:
			// enemy.setBulletPattern(BulletPattern.STRAIGHT);
			moveLine(enemy, Prefs.BOARD_X - 100, 100);
			break;
		case 2:
			// enemy.setBulletPattern(BulletPattern.HOMING_FAN);
			wait(enemy, 300);
			break;
		case 3:
			// enemy.setBulletPattern(BulletPattern.STRAIGHT);
			moveLine(enemy, Prefs.BOARD_X - 500, 500);
			break;
		default:
			break;
		}
	}

	private static void enemy3(Enemy enemy) {
		switch (enemy.getQueue()) {
		case 0:
			start(enemy, 200, 200);
			break;
		}
	}

	private static void start(Enemy enemy, double x, double y) {
		if (enemy.getTime() == 0) {
			enemy.endQueue(x, y);
		}
	}

	private static void moveLine(Enemy enemy, double x, double y) {
		double[] q = enemy.getQueuePos();
		double angle = Math.atan2(x - q[0], y - q[1]);
		enemy.setPos(enemy.getX() + Math.sin(angle) * enemy.getSpeed(),
				enemy.getY() + Math.cos(angle) * enemy.getSpeed());
		if (Math.abs(enemy.getX() - x) < 2 && Math.abs(enemy.getY() - y) < 2) {
			enemy.endQueue(x, y);
		}
	}

	private static void wait(Enemy enemy, int t) {
		if (enemy.getTime() == enemy.getQueueTimer() + t) {
			enemy.endQueue();
		}
	}
}
