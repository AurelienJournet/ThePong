package com.fr.project.entity;

public abstract class AbstractPad{
	
	public final int sizeX=50;
	public final int sizeY=200;
	public final double masse=0.2;
	public final double fApplied=200;
	private double forceY;

	private int yMoinsDeltaT;
	private int yT;
	private int yPlusDeltaT;
	private int velocityY;
	public final double deltaT=0.05;
	public final double frictionCoef=0.5;
	private int positionCenterY;
	protected int positionCenterX;
	
	private boolean superStrikeAvailable;
	private boolean superStrikeInUse;
	
	public AbstractPad() {
		init();
	}
	
	public void init() {
		positionCenterY=500;
		yMoinsDeltaT=positionCenterY;
		yT=positionCenterY;
		forceY=0;
	}

	public void move() {
		
		yPlusDeltaT=(int) Math.round(((deltaT*deltaT/masse*forceY)+(double)yT*(2+deltaT/masse*frictionCoef)-(double)yMoinsDeltaT)/(1+deltaT/masse*frictionCoef));
		
		if(yPlusDeltaT>1000-sizeY/2) {
			setPositionCenterY(1000-sizeY/2);
			yPlusDeltaT=1000-sizeY/2;
		}
		else if(yPlusDeltaT<sizeY/2) {
			setPositionCenterY(sizeY/2);
			yPlusDeltaT=sizeY/2;
		}
		else
			setPositionCenterY(yPlusDeltaT);
			
		yMoinsDeltaT=yT;
		yT=yPlusDeltaT;
		velocityY=(int)(((double)yPlusDeltaT-(double)yMoinsDeltaT)/((double)deltaT*2));
	}

	public void upKeyPress() {
			forceY=-fApplied;
	}
	
	public void downKeyPress() {
			forceY=fApplied;
	}
	
	public void releaseKeyPress() {
			forceY=0;
	}
	
	public int getPositionCenterY() {
		return positionCenterY;
	}

	public void setPositionCenterY(int positionCenterY) {
		this.positionCenterY = positionCenterY;
	}
	
	public int getPositionCenterX() {
		return positionCenterX;
	}

	public int getVelocityY() {
		return velocityY;
	}

	public boolean isSuperStrikeAvailable() {
		return superStrikeAvailable;
	}

	public void setSuperStrikeAvailable(boolean superStrikeAvailable) {
		this.superStrikeAvailable = superStrikeAvailable;
	}

	public boolean isSuperStrikeInUse() {
		return superStrikeInUse;
	}

	public void setSuperStrikeInUse(boolean superStrikeInUse) {
		this.superStrikeInUse = superStrikeInUse;
	}
}
