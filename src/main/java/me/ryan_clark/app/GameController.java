package me.ryan_clark.app;

import me.ryan_clark.gui.GuiController;

public class GameController {

	private final GuiController viewGuiController;

	public GameController(GuiController c) {
		viewGuiController = c;
		viewGuiController.initGameView();
	}

}
