package com.fr.project.entity;

public class Ball {

	public final int size = 100;
	private int positionX;
	private int positionY;
	private double velocityX;
	private double velocityY;
	public final double deltaT=0.05;
	private final double velocity = 300;

	public Ball() {
		this.positionX = 750;
		this.positionY = 500;

		int signX = -1;
		int signY = -1;

		if (Math.random() > 0.5)
			signX = 1;
		if (Math.random() > 0.5)
			signY = 1;

		this.velocityY =  (velocity * Math.random())*signY;
		this.velocityX =  (Math.sqrt(velocity * velocity - velocityY * velocityY))*signX;
	}

	public void move() {
		setPositionX((int)Math.round((double)this.positionX + deltaT*this.velocityX));
		setPositionY((int)Math.round((double)this.positionY + deltaT*this.velocityY));
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
	public double getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	public double getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}
}
