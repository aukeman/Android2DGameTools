package com.jacobaukeman.android2dgametools.model;

import com.jacobaukeman.android2dgametools.interfaces.IBoundingBox;

public class BoundingBox implements IBoundingBox {
	
		private float mTop;
		private float mLeft;

		private float mWidth;
		private float mHeight;
		
		public BoundingBox(float top, float left, float height, float width) {
			this.mTop = top;
			this.mLeft = left;
			this.mHeight = height;
			this.mWidth = width;
		}
		
		/* (non-Javadoc)
		 * @see com.aukeman.f35game.model.IBoundingBox#getTop()
		 */
		@Override
		public float getTop() {
			return mTop;
		}
		
		public void setTop(float top) {
			this.mTop = top;
		}
		
		/* (non-Javadoc)
		 * @see com.aukeman.f35game.model.IBoundingBox#getLeft()
		 */
		@Override
		public float getLeft() {
			return mLeft;
		}
		
		public void setLeft(float left) {
			this.mLeft = left;
		}
		
		public float getWidth() {
			return mWidth;
		}
		
		public void setWidth(float width) {
			this.mWidth = width;
		}
		
		public float getHeight() {
			return mHeight;
		}
		
		public void setHeight(float height) {
			this.mHeight = height;
		}
		
		/* (non-Javadoc)
		 * @see com.aukeman.f35game.model.IBoundingBox#getBottom()
		 */
		@Override
		public float getBottom(){
			return this.mTop + this.mHeight;
		}
		
		/* (non-Javadoc)
		 * @see com.aukeman.f35game.model.IBoundingBox#getRight()
		 */
		@Override
		public float getRight(){
			return this.mLeft + this.mWidth;
		}
		
		/* (non-Javadoc)
		 * @see com.aukeman.f35game.model.IBoundingBox#overlaps(com.aukeman.f35game.model.BoundingBox)
		 */
		@Override
		public boolean overlaps( IBoundingBox other ){
			return Utilities.overlaps(this, other);
		}
}
