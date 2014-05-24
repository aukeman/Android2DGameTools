package com.jacobaukeman.android2dgametools.view;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.content.Context;
import android.opengl.Matrix;

import com.jacobaukeman.android2dgametools.opengl.Shaders;
import com.jacobaukeman.android2dgametools.view.interfaces.IDrawable;

public class Sprite implements IDrawable{
	
	public static class Data{
		
		public float width = 0.0f;
		public float height = 0.0f;
		
		public float referenceX = 0.0f;
		public float referenceY = 0.0f;
		
		public float rotationAngle = 0.0f;
		
		public float scaleX = 1.0f;
		public float scaleY = 1.0f;
		
		public int textureId = 0;
		public int textureColumns = 1;
		public int textureRows = 1;
		
		public int textureFrameIdx = 0;
	}

	private float mX;
	private float mY;
	
	private float mPriority;

	private float mWidth;
	private float mHeight;
	
	private float mReferenceX;
	private float mReferenceY;
	
	private float mRotationAngle;
	
	private float mScaleX;
	private float mScaleY;
	
	private static float[] mMVPMatrix = new float[16];
	
	private FloatBuffer mVertices;
	
	private FloatBuffer[] mTextureCoordinates;
	
	private ShortBuffer mDrawOrder;
	
	private int mTextureId;
	
	private int mTextureFrameIdx;
	
	public Sprite( Context context, Sprite.Data data ){
		this(context, data.width, data.height, data.referenceX, data.referenceY, data.rotationAngle, data.scaleX, data.scaleY, data.textureId, data.textureColumns, data.textureRows, data.textureFrameIdx);
	}
	
	public Sprite(Context context, float width, float height, float referenceX, float referenceY, float rotationAngle, float scaleX, float scaleY, int textureId, int textureColumns, int textureRows, int textureFrameIdx) {
		super();
		this.mY = 0.0f;
		this.mX = 0.0f;
		
		this.mReferenceX = referenceX;
		this.mReferenceY = referenceY;
		
		this.mRotationAngle = rotationAngle;
		
		this.mScaleX = scaleX;
		this.mScaleY = scaleY;
		
		this.mPriority = 0.0f;
		this.mWidth = width;
		this.mHeight = height;
		this.mTextureId = Shaders.loadTexture(context, textureId);
		
		this.mTextureFrameIdx = textureFrameIdx;
	
		ByteBuffer bb = ByteBuffer.allocateDirect(4*3*4);
		bb.order(ByteOrder.nativeOrder());
		
		mVertices = bb.asFloatBuffer();
		mVertices.put(new float[] { 0.0f - this.mReferenceX,       0.0f - this.mReferenceY,         0.0f,
				                    0.0f - this.mReferenceX,       1.0f*height - this.mReferenceY,  0.0f,
				                    1.0f*width - this.mReferenceX, 1.0f*height - this.mReferenceY,  0.0f,
				                    1.0f*width - this.mReferenceX, 0.0f - this.mRotationAngle,      0.0f });
		mVertices.position(0);
		
		mTextureCoordinates = new FloatBuffer[textureColumns*textureRows];
		
		float frameWidth = 1.0f / textureColumns;
		float frameHeight = 1.0f / textureRows;
		
		int idx = 0;
		
		for ( int rowIdx = 0; rowIdx < textureRows; ++rowIdx){
			for ( int colIdx = 0; colIdx < textureColumns; ++colIdx){

				bb = ByteBuffer.allocateDirect(4*2*4);
				bb.order(ByteOrder.nativeOrder());
				mTextureCoordinates[idx] = bb.asFloatBuffer();
				mTextureCoordinates[idx].put(new float[] { frameWidth*colIdx, frameHeight*(rowIdx),
														   frameWidth*colIdx, frameHeight*(rowIdx+1),
														   frameWidth*(colIdx+1), frameHeight*(rowIdx+1),
														   frameWidth*(colIdx+1), frameHeight*(rowIdx) });
				mTextureCoordinates[idx].position(0);
				
				++idx;
			}
		}
		
		bb = ByteBuffer.allocateDirect(6*2);
		bb.order(ByteOrder.nativeOrder());
		
		mDrawOrder = bb.asShortBuffer();
		mDrawOrder.put(new short[] { 0, 1, 2, 0, 2, 3 });
		mDrawOrder.position(0);
		  
		setTextureFrameIdx(0);
	}
	
	public float getX() { return this.mX; }
	
	public float getY() { return this.mY; }
	
	public float getPriority() { return this.mPriority; }
	
	public float getWidth() { return this.mWidth; }
	
	public float getHeight() { return this.mHeight; }
	
	public float getReferenceX(){ return this.mReferenceX; }
	
	public float getReferenceY(){ return this.mReferenceY; }
	
	public float getRotationAngle(){ return this.mRotationAngle; }
	
	public void draw(float[] mvpMatrix, float x, float y){
		
		this.moveTo(x, y);
		draw(mvpMatrix);
	}
	
	public void draw(float[] mvpMatrix, float x, float y, float angle){
		
		this.moveTo(x, y);
		this.setRotationAngle(angle);
		draw(mvpMatrix);
	}
	
	public void draw(float[] mvpMatrix, float x, float y, float angle, float scale){

		this.moveTo(x, y);
		this.setRotationAngle(angle);
		this.setScale(scale);
		
		draw(mvpMatrix);
	}
	
	public void draw(float[] mvpMatrix, float x, float y, float angle, float scaleX, float scaleY){

		this.moveTo(x, y);
		this.setRotationAngle(angle);
		this.setScaleX(scaleX);
		this.setScaleY(scaleY);
		
		draw(mvpMatrix);
	}
	
	public void draw(float[] mvpMatrix, float x, float y, float angle, float scaleX, float scaleY, float priority){

		this.moveTo(x, y);
		this.setRotationAngle(angle);
		this.setScaleX(scaleX);
		this.setScaleY(scaleY);
		this.setPriority(priority);
		
		draw(mvpMatrix);
	}
	
	public void draw(float[] mvpMatrix, int frameIdx, float x, float y){
		
		this.setTextureFrameIdx(frameIdx);
		this.moveTo(x, y);
		draw(mvpMatrix);
	}
	
	public void draw(float[] mvpMatrix, int frameIdx, float x, float y, float angle){
		
		this.setTextureFrameIdx(frameIdx);
		this.moveTo(x, y);
		this.setRotationAngle(angle);
		draw(mvpMatrix);
	}
	
	public void draw(float[] mvpMatrix, int frameIdx, float x, float y, float angle, float scale){

		this.setTextureFrameIdx(frameIdx);
		this.moveTo(x, y);
		this.setRotationAngle(angle);
		this.setScale(scale);
		
		draw(mvpMatrix);
	}
	
	public void draw(float[] mvpMatrix, int frameIdx, float x, float y, float angle, float scaleX, float scaleY){

		this.setTextureFrameIdx(frameIdx);
		this.moveTo(x, y);
		this.setRotationAngle(angle);
		this.setScaleX(scaleX);
		this.setScaleY(scaleY);
		
		draw(mvpMatrix);
	}
	
	public void draw(float[] mvpMatrix, int frameIdx, float x, float y, float angle, float scaleX, float scaleY, float priority){

		this.setTextureFrameIdx(frameIdx);
		this.moveTo(x, y);
		this.setRotationAngle(angle);
		this.setScaleX(scaleX);
		this.setScaleY(scaleY);
		this.setPriority(priority);
		
		draw(mvpMatrix);
	}
	
	public void draw(float[] mvpMatrix){
		
		Matrix.translateM(mMVPMatrix, 0, mvpMatrix, 0, mX, mY, mPriority);
		
		if ( this.mScaleX != 1.0f || this.mScaleY != 1.0f ){
			Matrix.scaleM(mMVPMatrix, 0, mScaleX, mScaleY, 1.0f);
		}
		
		if (this.mRotationAngle != 0.0f){
			Matrix.rotateM(mMVPMatrix, 0, this.mRotationAngle, 0, 0, 1);
		}
		
		Shaders.render(6, mVertices, mTextureCoordinates[mTextureFrameIdx], mDrawOrder, mTextureId, mMVPMatrix);
	}
	
	public void moveTo(float x, float y){
		this.mX = x;
		this.mY = y;
	}
	
	public void setPriority(float priority){
		this.mPriority = priority;
	}
	
	public void setRotationAngle(float rotationAngle){
		this.mRotationAngle = rotationAngle;
	}
	
	public void setScale(float scale){
		this.mScaleX = scale;
		this.mScaleY = scale;
	}
	
	public void setScaleX(float scaleX){
		this.mScaleX = scaleX;
	}
	
	public void setScaleY(float scaleY){
		this.mScaleY = scaleY;
	}
	
	public void setTextureFrameIdx(int idx){
		if ( 0 <= idx && idx < mTextureCoordinates.length ){
			mTextureFrameIdx = idx;
		}
		else{
			mTextureFrameIdx = 0;
		}
	}
}
