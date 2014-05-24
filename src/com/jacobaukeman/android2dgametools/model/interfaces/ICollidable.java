package com.jacobaukeman.android2dgametools.model.interfaces;

import java.util.List;

import com.jacobaukeman.android2dgametools.model.BoundingBox;

public interface ICollidable {

	public boolean testCollision( ICollidable other );
	
	public BoundingBox getBoundingBox();
	
	public List<BoundingBox> getCollisionAreas();
}
