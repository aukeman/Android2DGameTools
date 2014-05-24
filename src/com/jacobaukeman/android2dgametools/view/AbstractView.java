package com.jacobaukeman.android2dgametools.view;

import com.jacobaukeman.android2dgametools.model.AbstractModel;
import com.jacobaukeman.android2dgametools.view.interfaces.IDrawable;

public class AbstractView implements IDrawable {

	private AbstractModel mModel;
	
	private IDrawable mDrawable;

	public AbstractView(){
		mModel = null;
		mDrawable = null;
	}
	
	public AbstractView(AbstractModel model, IDrawable drawable){
		mModel = model;
		mDrawable = drawable;
	}
	
	protected void setModel(AbstractModel model){
		this.mModel = model;
	}
	
	protected void setDrawable(IDrawable drawable){
		this.mDrawable = drawable;
	}
	
	@Override
	public void draw(float[] mvpMatrix) {
		mDrawable.moveTo(mModel.getTop(), mModel.getLeft());
		mDrawable.draw(mvpMatrix);
	}

	@Override
	public float getY() {
		return mModel.getY();
	}

	@Override
	public float getX() {
		return mModel.getX();
	}

	@Override
	public float getWidth() {
		return mModel.getWidth();
	}

	@Override
	public float getHeight() {
		return mModel.getHeight();
	}

	@Override
	public float getPriority() {
		return mDrawable.getPriority();
	}

	@Override
	public void setPriority(float priority) {
		mDrawable.setPriority(priority);
	}

	@Override
	public void moveTo(float x, float y) {
		mModel.moveTo(x, y);
	}
	
	public AbstractModel getModel(){
		return mModel;
	}
}
