package com.jacobaukeman.android2dgametools.opengl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;

public class Shaders {

	public static final String ourVertexShader = 
		"attribute vec4 aPosition; " +
		"attribute vec2 aTexCoordinate;" +
	    "uniform mat4 uMVPMatrix; " +
		"varying vec2 vTexCoordinate;" +
	    "void main() { " +
	    "    vTexCoordinate = aTexCoordinate;" +
	    "    gl_Position = uMVPMatrix * aPosition; " +
        "}";
	
	public static final String ourFragmentShader = 
		"precision mediump float;" +
		"varying vec2 vTexCoordinate;" +
	    "uniform sampler2D uTexture; " +
		"void main() {" +
//	    " gl_FragColor = uColor;" +
	    "  vec4 textureColor = texture2D(uTexture, vTexCoordinate);" +
		"  bvec4 is_equal = equal( textureColor, vec4( 1.0, 0.0, 1.0, 1.0 ) );" +
		"  if ( all(is_equal) ){" +
		"    discard;" +
		"  }" +
		"  gl_FragColor = textureColor;" +
		"}";
	
	private static int ourVertexShaderId;
	
	private static int ourFragmentShaderId;
	
	private static int ourProgramId;
	
	private static int ourPositionHandle;
	
	private static int ourMVPMatrixHandle;
	
	private static int ourTexCoordinateHandle;

	private static int ourTextureHandle;
	
	private static int ourLastTextureId;
	
	static {
		clearIdsAndHandles();
	}
	
	public static void initialize(){

		checkGlError("before initialize");

		deleteProgramAndShaders();

		getVertexShaderId();
		checkGlError("load vertex shader");
		
		Log.d(Shaders.class.getName(), GLES20.glGetShaderInfoLog(getVertexShaderId()));

		getFragmentShaderId();
		checkGlError("load fragment shader");
		
		Log.d(Shaders.class.getName(), GLES20.glGetShaderInfoLog(getFragmentShaderId()));

		getProgramId();
		checkGlError("create program");
		
		Log.d(Shaders.class.getName(), GLES20.glGetProgramInfoLog(getProgramId()));
		
		getPositionHandle();
		checkGlError("get position handle");
		
		getMVPMatrixHandle();
		checkGlError("get mvp matrix handle");
		
		getTextureCoordinatesHandle();
		checkGlError("get texture coordinates handle");
		
		getTextureHandle();
		checkGlError("get texture handle");
		
		GLES20.glUseProgram(getProgramId());
		
		GLES20.glEnableVertexAttribArray(getPositionHandle());
		GLES20.glEnableVertexAttribArray(getTextureCoordinatesHandle());
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glUniform1i(getTextureHandle(), 0);
		
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);


	}
	
	public static int getVertexShaderId(){

		if ( ourVertexShaderId < 0 ){
			ourVertexShaderId = loadShader(GLES20.GL_VERTEX_SHADER, ourVertexShader);
		}
		
		return ourVertexShaderId;
	}
	
	public static int getFragmentShaderId(){
		
		if ( ourFragmentShaderId < 0 ){
			ourFragmentShaderId = loadShader(GLES20.GL_FRAGMENT_SHADER, ourFragmentShader);
		}
		
		return ourFragmentShaderId;
	}
	
	public static int getProgramId(){
		if ( ourProgramId < 0 ){
			ourProgramId = GLES20.glCreateProgram();
			
			GLES20.glAttachShader(ourProgramId, getVertexShaderId());   // add the vertex shader to program
		    GLES20.glAttachShader(ourProgramId, getFragmentShaderId()); // add the fragment shader to program
		    GLES20.glLinkProgram(ourProgramId);
		}
		
		return ourProgramId;
	}
	
	public static int getPositionHandle(){
		if ( ourPositionHandle < 0 ){
			ourPositionHandle = GLES20.glGetAttribLocation(getProgramId(), "aPosition");
		}
		
		return ourPositionHandle;
	}
	
	public static int getMVPMatrixHandle(){
		if ( ourMVPMatrixHandle < 0 ){
			ourMVPMatrixHandle = GLES20.glGetUniformLocation(getProgramId(), "uMVPMatrix");
		}
		
		return ourMVPMatrixHandle;
	}
	
	public static int getTextureCoordinatesHandle(){
		if ( ourTexCoordinateHandle < 0 ){
			ourTexCoordinateHandle = GLES20.glGetAttribLocation(getProgramId(), "aTexCoordinate");
		}
		
		return ourTexCoordinateHandle;
	}

	public static int getTextureHandle(){
		if ( ourTextureHandle < 0 ){
			ourTextureHandle = GLES20.glGetUniformLocation(getProgramId(), "uTexture");
		}
		
		return ourTextureHandle;
	}

	public static void render(int numberOfVertices, FloatBuffer vertices, FloatBuffer textureCoords, ShortBuffer drawOrder, int textureId, float[] mvpMatrix){
		
		if (textureId != ourLastTextureId )
		{
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
			ourLastTextureId = textureId;
		}
		
		GLES20.glVertexAttribPointer(getPositionHandle(), 3, GLES20.GL_FLOAT, false, 0, vertices);
		GLES20.glVertexAttribPointer(getTextureCoordinatesHandle(), 2, GLES20.GL_FLOAT, false, 0, textureCoords);
		
		GLES20.glUniformMatrix4fv(getMVPMatrixHandle(), 1, false, mvpMatrix, 0);
		
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, numberOfVertices, GLES20.GL_UNSIGNED_SHORT, drawOrder);
			
	}
	
	public static int loadTexture(Context context, int resourceId){
		  
		final int[] textureHandle = new int[1];
		   
	    GLES20.glGenTextures(1, textureHandle, 0);
	 
	    if (textureHandle[0] != 0)
	    {
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inScaled = false;   // No pre-scaling
	 
	        // Read in the resource
	        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
	 
	        // Bind to the texture in OpenGL
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
	 
	        // Set filtering
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
	 
	        // Load the bitmap into the bound texture.
	        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
	 
	        // Recycle the bitmap, since its data has been loaded into OpenGL.
	        bitmap.recycle();
	    }
	 
	    if (textureHandle[0] == 0)
	    {
	    	Log.e(Shaders.class.getName(), "Unable to load texture: " + resourceId );
	    }
	 
	    return textureHandle[0];
	}
	
	private static int loadShader(int type, String shaderCode){
	    // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
	    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
	    int shader = GLES20.glCreateShader(type);

	    // add the source code to the shader and compile it
	    GLES20.glShaderSource(shader, shaderCode);
	    GLES20.glCompileShader(shader);

	    return shader;
	} 

	private static void deleteProgramAndShaders(){

		if ( 0 < ourPositionHandle ){
			GLES20.glDisableVertexAttribArray(ourPositionHandle);
		}
		
		if ( 0 < ourTexCoordinateHandle ){
			GLES20.glDisableVertexAttribArray(ourTexCoordinateHandle);
		}
		
		if ( 0 < ourVertexShaderId ){
			GLES20.glDeleteShader(ourVertexShaderId);
			GLES20.glDetachShader(ourProgramId, ourVertexShaderId);
		}
		
		if ( 0 < ourFragmentShaderId ){
			GLES20.glDeleteShader(ourFragmentShaderId);
			GLES20.glDetachShader(ourProgramId, ourFragmentShaderId);
		}
		
		if ( 0 < ourProgramId ){
			GLES20.glDeleteProgram(ourProgramId);
		}
		
		clearIdsAndHandles();
	}
	
	
	private static void clearIdsAndHandles(){
		
		ourVertexShaderId = -1;
		ourFragmentShaderId = -1;
		ourProgramId = -1;
		ourPositionHandle = -1;
		ourMVPMatrixHandle = -1;
		ourTexCoordinateHandle = -1;
		ourTextureHandle = -1;
		
		ourLastTextureId = -1;
	}
	
    private static void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("opengl", op + ": glError " + GLU.gluErrorString(error) + " (" + error +")" );
        }
    }
	
}
