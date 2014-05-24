package com.jacobaukeman.android2dgametools;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.MethodNotSupportedException;

import com.jacobaukeman.android2dgametools.interfaces.IPoolable;

public class Pool<T extends IPoolable> implements Iterable<T> {

	private ArrayList<T> mPooledObjects;
	
	private ObjectsInUse mObjectsInUseIterable;

	public Pool(){
		mPooledObjects = new ArrayList<T>();
		mObjectsInUseIterable = new ObjectsInUse();
	}
	
	public void add(T pooledObject){
		mPooledObjects.add(pooledObject);
	}
	
	public void clear(){
		mPooledObjects.clear();
	}
	
	public T getNextAvailable(){
		for ( int i = 0; i < mPooledObjects.size(); ++i ){
			if ( mPooledObjects.get(i).available() ){
				return mPooledObjects.get(i);
			}
		}
		
		return null;
	}
	
	@Override
	public Iterator<T> iterator() {
		return mPooledObjects.iterator();
	}
	
	public Iterable<T> getObjectsInUse(){
		mObjectsInUseIterable.reset();
		return mObjectsInUseIterable;
	}
	
	private class ObjectsInUse implements Iterable<T>, Iterator<T>{

		private int mNextIndex;

		@Override
		public Iterator<T> iterator() {
			return this;
		}
			
		public void reset(){
			mNextIndex = -1;
			
			advanceNextIndex();
		}
			
		@Override
		public boolean hasNext() {
			return mNextIndex < mPooledObjects.size()-1;
		}

		@Override
		public T next() {
			T result = mPooledObjects.get(mNextIndex);
			advanceNextIndex();
		
			return result;
		}

		@Override
		public void remove() {
			throw new RuntimeException( new MethodNotSupportedException("Iterator.remove not supported by ObjectsInUseIterator") );
		}
		
		private void advanceNextIndex(){
			for ( mNextIndex = mNextIndex+1; 
				  (mNextIndex < mPooledObjects.size() && 
				   mPooledObjects.get(mNextIndex).available()); 
				  ++mNextIndex )
			{ }
		}
	}
}
