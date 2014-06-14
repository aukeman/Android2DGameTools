package com.jacobaukeman.android2dgametools;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class DeviceRotation implements SensorEventListener {

	private float mXAccel = 0.0f;
	private float mYAccel = 0.0f;
	private float mZAccel = 0.0f;
	
	private float mXAccelValues[] = { 0.0f, 0.0f, 0.0f, 0.0f };
	private float mYAccelValues[] = { 0.0f, 0.0f, 0.0f, 0.0f };
	private float mZAccelValues[] = { 0.0f, 0.0f, 0.0f, 0.0f };
	
	private int mAccelIdx = 0;
	
	private float mXRotationDegrees = 0.0f;
	private float mXRotationRadians = 0.0f;
	
	private float mXRotationAngleSin = 0.0f;
	private float mXRotationAngleCos = 0.0f;
	
	private float mXDeadZoneDegrees = 0.0f;
	
	private float mXCenterRadians = 0.0f;
	
	
	private float mYRotationDegrees = 0.0f;
	private float mYRotationRadians = 0.0f;
	
	private float mYRotationAngleSin = 0.0f;
	private float mYRotationAngleCos = 0.0f;
	
	private float mYDeadZoneDegrees = 0.0f;
	
	private float mYCenterRadians = 0.0f;

	
	private float mZRotationDegrees = 0.0f;
	private float mZRotationRadians = 0.0f;
	
	private float mZRotationAngleSin = 0.0f;
	private float mZRotationAngleCos = 0.0f;
	
	private float mZDeadZoneDegrees = 0.0f;
	
	private float mZCenterRadians = 0.0f;

	public float getXRotationDegrees(){
		return (mXRotationDegrees);
	}
	
	public float getXRotationRadians(){
		return (mXRotationRadians);
	}
	
	public float getXRotationAngleSin(){
		return (mXRotationAngleSin);
	}
	
	public float getXRotationAngleCos(){
		return (mXRotationAngleCos);
	}
	
	public void setXDeadZoneDegrees( float deadZoneDegrees ){
		mXDeadZoneDegrees = deadZoneDegrees;
	}
	
	public void setXCenterDegrees( float centerDegrees ){
		mXCenterRadians = (float)(centerDegrees*Math.PI/180.0);
	}

	public float getYRotationDegrees(){
		return (mYRotationDegrees);
	}
	
	public float getYRotationRadians(){
		return (mYRotationRadians);
	}
	
	public float getYRotationAngleSin(){
		return (mYRotationAngleSin);
	}
	
	public float getYRotationAngleCos(){
		return (mYRotationAngleCos);
	}
	
	public void setYDeadZoneDegrees( float deadZoneDegrees ){
		mYDeadZoneDegrees = deadZoneDegrees;
	}
	
	public void setYCenterDegrees( float centerDegrees ){
		mYCenterRadians = (float)(centerDegrees*Math.PI/180.0);
	}

	public float getZRotationDegrees(){
		return (mZRotationDegrees);
	}
	
	public float getZRotationRadians(){
		return (mZRotationRadians);
	}
	
	public float getZRotationAngleSin(){
		return (mZRotationAngleSin);
	}
	
	public float getZRotationAngleCos(){
		return (mZRotationAngleCos);
	}
	
	public void setZDeadZoneDegrees( float deadZoneDegrees ){
		mZDeadZoneDegrees = deadZoneDegrees;
	}
	
	public void setZCenterDegrees( float centerDegrees ){
		mZCenterRadians = (float)(centerDegrees*Math.PI/180.0);
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// no op
	}

	@Override
	public void onSensorChanged(SensorEvent e) {
    	mXAccelValues[mAccelIdx] = e.values[0];
    	mYAccelValues[mAccelIdx] = e.values[1];
    	mZAccelValues[mAccelIdx] = e.values[2];
    	
    	mAccelIdx += 1;
    	 	
    	if ( 3 < mAccelIdx ){
    		mAccelIdx = 0;
    	}
    	
    	mXAccel = 0.25f*(mXAccelValues[0] + mXAccelValues[1] + mXAccelValues[2] + mXAccelValues[3]);
    	mYAccel = 0.25f*(mYAccelValues[0] + mYAccelValues[1] + mYAccelValues[2] + mYAccelValues[3]);
    	mZAccel = 0.25f*(mZAccelValues[0] + mZAccelValues[1] + mZAccelValues[2] + mZAccelValues[3]);
	}

	public void calculateAngles(){

		mXRotationRadians = (float)Math.atan2(mZAccel, mYAccel) - mXCenterRadians;
		mXRotationDegrees = (float)(mXRotationRadians*180.0f / Math.PI);

		if ( Math.abs(mXRotationDegrees) < mXDeadZoneDegrees ){
			mXRotationRadians = 0.0f;
			mXRotationDegrees = 0.0f;
		}
		
		mXRotationAngleSin = (float)Math.sin(mXRotationRadians);
		mXRotationAngleCos = (float)Math.cos(mXRotationRadians);

		
		mYRotationRadians = -(float)Math.atan2(mXAccel, mZAccel) - mYCenterRadians;
		mYRotationDegrees = (float)(mYRotationRadians*180.0f / Math.PI);
		
		if ( Math.abs(mYRotationDegrees) < mYDeadZoneDegrees ){
			mYRotationRadians = 0.0f;
			mYRotationDegrees = 0.0f;
		}
		
		mYRotationAngleSin = (float)Math.sin(mYRotationRadians);
		mYRotationAngleCos = (float)Math.cos(mYRotationRadians);

		
		mZRotationRadians = (float)Math.atan2(mXAccel, mZAccel) - mZCenterRadians;
		mZRotationDegrees = (float)(mZRotationRadians*180.0f / Math.PI);
		
		if ( Math.abs(mZRotationDegrees) < mZDeadZoneDegrees ){
			mZRotationRadians = 0.0f;
			mZRotationDegrees = 0.0f;
		}
		
		mZRotationAngleSin = (float)Math.sin(mZRotationRadians);
		mZRotationAngleCos = (float)Math.cos(mZRotationRadians);

	}

}
