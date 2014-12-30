package com.esaych.io.touch;

import com.badlogic.gdx.math.Vector2;

public class Touch {

	public int x;
	public int y;
	public Vector2 direction = new Vector2(0, 0);
	
	public Touch(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void redefine(int x, int y) {
		direction = new Vector2(direction.x + (x - this.x), direction.y + (y - this.y));
		this.x = x;
		this.y = y;
	}
	
	public Vector2 clearDirection() {
		Vector2 other = direction.cpy();
		direction = new Vector2(0, 0);
		return other;
	}
}
