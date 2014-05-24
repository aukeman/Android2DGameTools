package com.jacobaukeman.android2dgametools;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.hardware.SensorEventListener;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.jacobaukeman.android2dgametools.interfaces.IFrameInfo;
import com.jacobaukeman.android2dgametools.opengl.Shaders;
import com.jacobaukeman.android2dgametools.view.Viewport;
import com.jacobaukeman.android2dgametools.view.interfaces.IViewport;


public abstract class Android2DGameView extends GLSurfaceView implements GLSurfaceView.Renderer, SensorEventListener {

	private static final int MATCH_RESOLUTION = -1;
	
	private static final String VIEW_LOG_TAG = Android2DGameView.class.getName();
	
	private final float[] mMVPMatrix = new float[16];
	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];

	private final int mNumberOfLines;
	
	private final float[] mClearColor = new float[4];
	
	private Viewport mViewport;
	
	private FrameInfo mFrameInfo;
	
	public Android2DGameView(Context context){
		this(context, MATCH_RESOLUTION);
	}
	
	
	public Android2DGameView(Context context, int numberOfLines){
		this(context, numberOfLines, 0.0f, 0.0f, 0.0f, 1.0f);
	}
	
	public Android2DGameView(Context context, int numberOfLines, float clearColorRed, float clearColorGreen, float clearColorBlue, float clearColorAlpha) {
		super(context);

		mNumberOfLines = numberOfLines;
		
		mClearColor[0] = clearColorRed;
		mClearColor[1] = clearColorGreen;
		mClearColor[2] = clearColorBlue;
		mClearColor[3] = clearColorAlpha;
		
		mViewport = new Viewport();

		mFrameInfo = new FrameInfo(false);

		setEGLContextClientVersion(2);
		
		setRenderer(this);
		
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		
	}
	
	public IViewport getViewport(){
		return mViewport;
	}
	
	public IFrameInfo getFrameInfo(){
		return mFrameInfo;
	}
	
	public abstract void handleRenderFrame( IViewport viewport, IFrameInfo frameInfo, float[] mvpMatrix );
	
	public void handleSurfaceCreated(Context context){
		
	}
	
	public void handleSurfaceChanged(Context context, int screenWidth, int screenHeight){
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		 
		GLES20.glClearColor(mClearColor[0], mClearColor[1], mClearColor[2], mClearColor[3]);
		Shaders.initialize();
		
		handleSurfaceCreated(getContext());
	}
	
	@Override
	public void onSurfaceChanged(GL10 arg0, int screenWidth, int screenHeight) {
		GLES20.glViewport(0, 0, screenWidth, screenHeight);
		
		int pixelHeightFactor = 
				(0 < mNumberOfLines ? screenHeight/mNumberOfLines : 1);
		
		float ratio = (float)screenWidth / screenHeight;
		
		float top = 0.0f;
		float left = 0.0f;
		
		float bottom = (float)screenHeight/pixelHeightFactor + top;
		float right = (float)ratio*(bottom - top) + left; 

		mViewport.setTop(top);
		mViewport.setBottom(bottom);
		mViewport.setLeft(left);
		mViewport.setRight(right);
		mViewport.setScreenWidth(screenWidth);
		mViewport.setScreenHeight(screenHeight);
		
		Log.i(VIEW_LOG_TAG, String.format("width: %d height: %d", screenWidth, screenHeight));
		Log.i(VIEW_LOG_TAG, String.format("top: %f bottom: %f left: %f right: %f", top, bottom, left, right));
		Log.i(VIEW_LOG_TAG, String.format("pixelHeightFactor: %d ratio: %f", pixelHeightFactor, ratio));
		
		Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, 1, 201);

		Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 101, 0, 0, 0, 0, 1, 0);
		
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
		
		handleSurfaceChanged(getContext(), screenWidth, screenHeight);
	}
	
	@Override
	public void onDrawFrame(GL10 arg0) {

		mFrameInfo.topOfFrame();
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		handleRenderFrame(mViewport, mFrameInfo, mMVPMatrix);
	}
}
