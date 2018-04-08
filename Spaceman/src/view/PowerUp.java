package view;

import javafx.scene.shape.*;

public class PowerUp {
	private double graphicalX;
	private double graphicalY;
	private Circle model;
	private int nextRespawnTime;
	
	public PowerUp(double graphicalX, double graphicalY, double radius) {
		this.graphicalX = graphicalX;
		this.graphicalY = graphicalY;
		model = new Circle(graphicalX, graphicalY, radius);
	}
	
	public double getGraphicalX() {
		return graphicalX;
	}
	
	public double getGraphicalY() {
		return graphicalY;
	}
	
	public Circle returnPowerUp() {
		return model;
	}
	
	public void setRespawnTime(int newTime) {
		nextRespawnTime = newTime;
	}
	
	public int getRespawnTime() {
		return nextRespawnTime;
	}
	
}