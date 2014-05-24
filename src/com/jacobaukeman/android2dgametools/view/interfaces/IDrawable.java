package com.jacobaukeman.android2dgametools.view.interfaces;

public interface IDrawable {

	public void draw(float[] mvpMatrix);
	
	public float getX();
	
	public float getY();
	
	public float getWidth();
	
	public float getHeight();
	
	public float getPriority();
	
	public void moveTo(float x, float y);
	
	public void setPriority(float priority);
}
