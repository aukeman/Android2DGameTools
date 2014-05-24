package com.jacobaukeman.android2dgametools;

import com.jacobaukeman.android2dgametools.interfaces.IFrameInfo;

public class FrameInfo implements IFrameInfo {
	
	private long mStartTime;
	
	private long mTopOfFrame;
	
	private long mTopOfLastFrame;
	
	private long mTimeLastFrameRateCalculated;
	
	private long mFrameCountLastFrameRateCalculated;
	
	private int mLengthOfLastFrameInMilliseconds;
	
	private float mLengthOfLastFrameInSeconds;
	
	private float mFrameRate;
	
	private long mFrameCount;

	private String mFrameRateString;
	
	private boolean mCalculateFrameRate;
	
	public long getTopOfFrame(){
		return mTopOfFrame;
	}
	
	public long getTopOfLastFrame(){
		return mTopOfLastFrame;
	}
	
	public long getLengthOfLastFrameInMilliseconds(){
		return mLengthOfLastFrameInMilliseconds;
	}
	
	public float getLengthOfLastFrameInSeconds(){
		return mLengthOfLastFrameInSeconds;
	}

	public long getFrameCount(){
		return mFrameCount;
	}
	
	public float getFrameRate(){
		return mFrameRate;
	}
	
	public String getFrameRateString(){
		return mFrameRateString;
	}
	
	public FrameInfo(boolean calculateFrameRate){
		mStartTime = 0;
		mTopOfFrame = 0;
		mTopOfLastFrame = 0;
		mTimeLastFrameRateCalculated = 0;
		mFrameCountLastFrameRateCalculated = 0;
		mLengthOfLastFrameInMilliseconds = 0;
		mLengthOfLastFrameInSeconds = 0.0f;
		mFrameRate = 0.0f;
		mFrameCount = 0;
		mFrameRateString = "FPS: 0.0";
		mCalculateFrameRate = calculateFrameRate;
		
	}
	
	public void topOfFrame(){
		
		if ( mStartTime == 0 ){
			mStartTime = System.currentTimeMillis();
		}
		
		mTopOfLastFrame = mTopOfFrame;
		mTopOfFrame = System.currentTimeMillis() - mStartTime;

		if ( mCalculateFrameRate && mTopOfFrame % 250 < mTopOfLastFrame % 1000 ){
			mFrameRate = (float)(mFrameCount - mFrameCountLastFrameRateCalculated ) / (mTopOfFrame - mTimeLastFrameRateCalculated ) * 1000 ;
			mFrameRateString = String.format("FPS: %.1f", mFrameRate);
			mTimeLastFrameRateCalculated = mTopOfFrame;
			mFrameCountLastFrameRateCalculated = mFrameCount;
		}
		
		mLengthOfLastFrameInMilliseconds = 0 < mTopOfLastFrame ? (int)(mTopOfFrame - mTopOfLastFrame) : 17; 
		mLengthOfLastFrameInSeconds = mLengthOfLastFrameInMilliseconds / 1000.0f;

		mFrameCount++;
	}


}
