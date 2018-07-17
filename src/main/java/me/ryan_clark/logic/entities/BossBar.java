package me.ryan_clark.logic.entities;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import me.ryan_clark.app.Prefs;

public class BossBar {
	private Group bossBar = new Group();
	private StringProperty name = new SimpleStringProperty();
	private DoubleProperty hpPercent = new SimpleDoubleProperty();
	
	public BossBar() {
		name.set("null");
		hpPercent.set(0);
		Label nameLabel = new Label();
		nameLabel.textProperty().bind(name);
		nameLabel.setPrefSize(Prefs.BOARD_X, 20);
		nameLabel.setLayoutX(10);
		
		Effect glow = new Glow(1.0);
		nameLabel.setEffect(glow);
		
		Rectangle barOutline = new Rectangle();
		barOutline.setWidth(Prefs.BOARD_X - 20);
		barOutline.setHeight(20);
		barOutline.setLayoutX(10);
		barOutline.setLayoutY(20);
		
		Rectangle barBack = new Rectangle();
		barBack.setWidth(barOutline.getWidth() - 2);
		barBack.setHeight(barOutline.getHeight() - 2);
		barBack.setLayoutX(barOutline.getLayoutX() + 1);
		barBack.setLayoutY(barOutline.getLayoutY() + 1);
		barBack.setFill(Color.RED);
		
		Rectangle barFront = new Rectangle();
		barFront.widthProperty().bind(hpPercent.multiply(barBack.getWidth()));
		barFront.setHeight(barOutline.getHeight() - 2);
		barFront.setLayoutX(barOutline.getLayoutX() + 1);
		barFront.setLayoutY(barOutline.getLayoutY() + 1);
		barFront.setFill(Color.web("#00ff00"));
		
		bossBar.getChildren().addAll(nameLabel, barOutline, barBack, barFront);
		
		hpPercent.addListener((arg, oldVal, newVal) -> {
			if(newVal.floatValue() < 0.00001) {
				bossBar.setVisible(false);
			}
		});
	}
	
	public void setBoss(Enemy e) {
		bossBar.setVisible(true);
		name.bind(e.getName());
		hpPercent.bind(e.getHealthProperty());
	}
	
	public Node getBar() {
		return bossBar;
	}
}
