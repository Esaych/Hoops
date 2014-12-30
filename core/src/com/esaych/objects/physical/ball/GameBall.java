package com.esaych.objects.physical.ball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esaych.io.menus.Options;
import com.esaych.io.touch.Touch;
import com.esaych.io.touch.TouchData;
import com.esaych.objects.physical.Ball;
import com.esaych.world.World;

public class GameBall extends Ball {
	
	private int Xpush;
	private int Ypush;
	
	public boolean up,down,left,right;
	
	public GameBall(World world, int x, int y) { 
		super(world, x, y);
		setXPush(1200);
		setYPush(1500);
	}
	
	@Override
	public void update(float delta) {

		up = false;
		down = false;
		left = false;
		right = false;
		
		for (Touch t : TouchData.getTouches()) {
			if (t != null) {
				processTouches(t, delta);
			}
		}
		
		if (Options.getTilt()) {
			if (world.getGameScreen().getScreenRotation() == 1) {
				float LRorientation = Gdx.input.getAccelerometerY();
				float UDorientation = Gdx.input.getAccelerometerX();
				if (UDorientation > 3)
					UDorientation = 3;
				addVelocity(new Vector2(LRorientation*400f*delta, (UDorientation-3)*500*delta));
			} else {
				float LRorientation = Gdx.input.getAccelerometerY();
				float UDorientation = Gdx.input.getAccelerometerX();
				if (UDorientation < -3)
					UDorientation = -3;
				addVelocity(new Vector2(-LRorientation*400f*delta, -(UDorientation+3)*500*delta));
			}
		}
		
		super.update(delta);
	}
	
	public int getXPush() {
		return Xpush;
	}
	
	public void setXPush(int x) {
		Xpush = x;
	}
	
	public int getYPush() {
		return Ypush;
	}
	
	public void setYPush(int y) {
		Ypush = y;
	}
	
	public void processTouches(Touch t, float delta) {
		float Xpush = ((float) this.Xpush)*delta;
		float Ypush = ((float) this.Ypush)*delta;
		
		if (t.x < world.getWidth(.5)) {
			if (t.x < world.getWidth(.25)) {
				vel.add(-Xpush, 0);
				left = true;
			} else {
				vel.add(Xpush, 0);
				right = true;
			}
		} else {
			if (t.y < world.getHeight()/2) {
				vel.add(0, -Ypush);
				up = true;
			} else {
				vel.add(0, Ypush);
				down = true;
			}
		}
	}
	
}
