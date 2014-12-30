package com.esaych.io.touch;

import com.badlogic.gdx.Gdx;

public class TouchData {
	
	public static void setSize(int gameWidth, int gameHeight) {
		width = gameWidth;
		height = gameHeight;
	}
	
	private static Touch[] touches = new Touch[3];
	private static int width = 0;
	private static int height = 0;
	
	public static Touch[] getTouches() {
		return touches;
	}
	
	public static void clear() {
		touches[0] = null;
		touches[1] = null;
		touches[2] = null;
	}
	
    public static boolean touchDAt(int x, int y, int pointer) {
    	if (pointer < 2) {
			float[] xy = convertToGameSize(x, y);
			x = (int) xy[0];
			y = (int) xy[1];
    		touches[pointer] = new Touch(x, y);
    	}
        return true;
    }

    public static boolean touchUAt(int x, int y, int pointer) {
    	if (pointer < 2) {
    		touches[pointer] = null;
    	}
        return true;
    }
	
	public static void dragAt(int x, int y, int pointer) {
		if (pointer < 2) {
			float[] xy = convertToGameSize(x, y);
			x = (int) xy[0];
			y = (int) xy[1];
			if (touches[pointer] == null)
				touches[pointer] =  new Touch(x, y);
			else
				touches[pointer].redefine(x, y);
		}
	}
	
	public static float[] convertToGameSize(float x, float y) {
		x = (width * x) / Gdx.graphics.getWidth();
		y = (height * y) / Gdx.graphics.getHeight();
		float[] data = {x, y};
		return data;
	}
}
