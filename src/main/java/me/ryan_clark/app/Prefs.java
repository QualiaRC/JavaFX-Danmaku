package me.ryan_clark.app;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Prefs {
	// GUI settings

	public static final int RES_X = 600;
	public static final int RES_Y = 664;

	public static final int BOARD_X = 400;
	public static final int BOARD_Y = 624;

	// Entity Pool settings
	public static final int MAX_PLAYER_BULLETS = 500;
	public static final int MAX_ENEMIES = 20;
	public static final int MAX_ENEMY_BULLETS = 500;

	// Player Settings
	public static final int PLAYER_SPEED = 4;
	public static final int PLAYER_SPEED_SLOW = 2;
	public static final int PLAYER_RADIUS = 5;
	public static final Color PLAYER_COLOR = Color.RED;

	public static final int[] EXP_CURVE = new int[] { 0, 10, 20, 40, 80, 160, Integer.MAX_VALUE };
	public static final int[] SHOOT_DELAY = new int[] { 0, 15, 4, 1, 1, 1, 1 };
	public static final int LEVEL_CAP = EXP_CURVE.length - 1;

	public static final int PLAYER_BULLET_RADIUS = 2;
	public static final Color PLAYER_BULLET_COLOR = Color.BLUE;

	// Sprite images
	public static final Image SPRITE = new Image("res/images/test.png");
	public static final Image SPRITE_POWERUP = new Image("res/images/powerup.png");

	// Powerup Settings
	public static final int MAX_POWERUPS = 50;
	public static final int SUCC_RADIUS = 30;
	public static final int SUCC_ZONE_Y = 100;

}
