package me.ryan_clark.logic.entities;

import me.ryan_clark.app.Prefs;

public class EntityFactory {
	private static Player player = new Player();
	private static PlayerBullet[] playerBullets = new PlayerBullet[Prefs.MAX_PLAYER_BULLETS];
	private static Enemy[] enemies = new Enemy[Prefs.MAX_ENEMIES];
	private static EnemyBullet[][] enemyBullets = new EnemyBullet[Prefs.MAX_ENEMIES][Prefs.MAX_ENEMY_BULLETS];
	private static Powerup[] powerups = new Powerup[Prefs.MAX_POWERUPS];
	private static BossBar bossBar = new BossBar();

	static {
		for (int i = 0; i < Prefs.MAX_PLAYER_BULLETS; i++) {
			playerBullets[i] = new PlayerBullet();
		}
		for (int i = 0; i < Prefs.MAX_ENEMIES; i++) {
			enemies[i] = new Enemy(i);
			for (int j = 0; j < Prefs.MAX_ENEMY_BULLETS; j++) {
				enemyBullets[i][j] = new EnemyBullet(i);
			}
		}
		for (int i = 0; i < Prefs.MAX_POWERUPS; i++) {
			powerups[i] = new Powerup();
		}
		bossBar.getBar().setVisible(false);
	}

	public final static Player getPlayer() {
		return player;
	}

	public final static PlayerBullet[] getPlayerBullets() {
		return playerBullets;
	}

	public final static Enemy[] getEnemies() {
		return enemies;
	}

	public final static EnemyBullet[] getEnemyBullets(int enemy_id) {
		return enemyBullets[enemy_id];
	}

	public final static Powerup[] getPowerups() {
		return powerups;
	}

	public final static BossBar getBossBar() {
		return bossBar;
	}
}
