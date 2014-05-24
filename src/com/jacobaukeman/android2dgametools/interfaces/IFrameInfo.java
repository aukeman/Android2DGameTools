package com.jacobaukeman.android2dgametools.interfaces;

public interface IFrameInfo {

	public long getTopOfFrame();
	
	public long getTopOfLastFrame();
	
	public long getLengthOfLastFrameInMilliseconds();
	
	public float getLengthOfLastFrameInSeconds();
	
	public long getFrameCount();
	
	public float getFrameRate();
}
