package me.ryan_clark.logic.entities;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.ryan_clark.app.Prefs;

abstract class Entity {

	protected boolean isActive = false;
	protected DoubleProperty layoutXProperty = new SimpleDoubleProperty();
	protected DoubleProperty layoutYProperty = new SimpleDoubleProperty();

	private double xPos = 0;
	private double yPos = 0;
	private ImageView shape = new ImageView();
	private EntityType type = EntityType.NONE;

	public Entity() {
		shape.setImage(Prefs.SPRITE);
		shape.layoutXProperty().bind(layoutXProperty.subtract(shape.getImage().getWidth() / 2));
		shape.layoutYProperty().bind(layoutYProperty.subtract(shape.getImage().getHeight() / 2));
	}

	public abstract void update();

	public double getX() {
		return xPos;
	}

	public double getY() {
		return yPos;
	}

	public void setX(double x) {
		xPos = x;
		layoutXProperty.set(x);
	}

	public void setY(double y) {
		yPos = y;
		layoutYProperty.set(y);
	}

	public void setPos(double x, double y) {
		setX(x);
		setY(y);
	}

	public Node getShape() {
		return shape;
	}

	public EntityType getType() {
		return type;
	}

	protected void setType(EntityType t) {
		type = t;
	}

	public double getRadius() {
		return shape.getImage().getWidth();
	}

	protected void setVisible(boolean b) {
		shape.setVisible(b);
	}

	public void setImage(Image i) {
		shape.setImage(i);
	}

	public void adjustImage(int dX, int dY) {
		shape.layoutXProperty().bind(layoutXProperty.subtract(shape.getImage().getWidth() / 2).add(dX));
		shape.layoutYProperty().bind(layoutYProperty.subtract(shape.getImage().getHeight() / 2).add(dY));
	}

	public boolean isActive() {
		return isActive;
	}

}
