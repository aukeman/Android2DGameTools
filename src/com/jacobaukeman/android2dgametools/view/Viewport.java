package com.jacobaukeman.android2dgametools.view;

import com.jacobaukeman.android2dgametools.interfaces.IBoundingBox;
import com.jacobaukeman.android2dgametools.view.interfaces.IViewport;

public class Viewport implements IViewport{
	private float top = 0.0f;
	private float bottom = 0.0f;
	private float left = 0.0f;
	private float right = 0.0f;
	
	private float screenWidth = 0.0f;
	private float screenHeight = 0.0f;

	public void setTop(float top) {
		this.top = top;
	}

	public void setBottom(float bottom) {
		this.bottom = bottom;
	}

	public void setLeft(float left) {
		this.left = left;
	}

	public void setRight(float right) {
		this.right = right;
	}

	public void setScreenWidth(float screenWidth) {
		this.screenWidth = screenWidth;
	}

	public void setScreenHeight(float screenHeight) {
		this.screenHeight = screenHeight;
	}

	/* (non-Javadoc)
	 * @see com.aukeman.f35game.view.IViewport#getTop()
	 */
	@Override
	public float getTop() {
		return top;
	}

	/* (non-Javadoc)
	 * @see com.aukeman.f35game.view.IViewport#getBottom()
	 */
	@Override
	public float getBottom() {
		return bottom;
	}

	/* (non-Javadoc)
	 * @see com.aukeman.f35game.view.IViewport#getLeft()
	 */
	@Override
	public float getLeft() {
		return left;
	}

	/* (non-Javadoc)
	 * @see com.aukeman.f35game.view.IViewport#getRight()
	 */
	@Override
	public float getRight() {
		return right;
	}
	
	@Override
	public boolean overlaps( IBoundingBox other) {
		return false;
	}

	public float getScreenWidth() {
		return screenWidth;
	}

	public float getScreenHeight() {
		return screenHeight;
	}

	public float getXFromScreenX(float screenX){
		return screenX * (this.right - this.left) / this.screenWidth + this.left;
	}

	public float getYFromScreenY(float screenY){
		return screenY * (this.bottom - this.top) / this.screenHeight + this.top;
	}
}