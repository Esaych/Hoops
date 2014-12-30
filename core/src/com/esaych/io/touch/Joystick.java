package com.esaych.io.touch;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.esaych.objects.physical.ball.GameBall;
import com.esaych.world.World;

public class Joystick {
	private World world;
	private boolean onRight;
	private boolean onLeft;
	private Vector2 velChange;
	private int Xpush;
	private int Ypush;
	private Touch t;
	private float size = 24;
	
	public Joystick(World world) {
		this.world = world;
		onRight = false;
		onLeft = false;
		velChange = new Vector2(0,0);
		Xpush = (int) (((GameBall) world.getBall()).getXPush()*.8);
		Ypush = (int) (((GameBall) world.getBall()).getYPush());
	}
	
	public void update() {
		t = TouchData.getTouches()[0];
		if (t == null) {
			onRight = false;
			onLeft = false;
			velChange.set(0, 0);
			return;
		}
		if (!(onRight || onLeft)) {
			if (t.x > world.getWidth(.5))
				onRight = true;
			else
				onLeft = true;
		}
		if (onLeft) {
			int x = 60;
			int y = 240;
			float angle = (float) Math.atan2(x-4-t.x, t.y-y+4);
			double dist = Math.sqrt(Math.pow(t.x-x+4,2) + Math.pow(t.y-y+4,2));
			if (dist < size) {
				double factor = dist/(size/2);
				System.out.println(dist);
				velChange.set((float) -(Xpush*Math.sin(angle)*factor), (float) (Ypush*Math.cos(angle)*factor));
			} else {
				velChange.set((float) -(Xpush*Math.sin(angle)), (float) (Ypush*Math.cos(angle)));
			}
		} else if (onRight) {
			int x = world.getWidth()-60;
			int y = 240;
			float angle = (float) Math.atan2(x-4-t.x, t.y-y+4);
			double dist = Math.sqrt(Math.pow(t.x-x+4,2) + Math.pow(t.y-y+4,2));
			if (dist < size) {
				double factor = dist/(size/2);
				velChange.set((float) -(Xpush*Math.sin(angle)*factor), (float) (Ypush*Math.cos(angle)*factor));
			} else {
				velChange.set((float) -(Xpush*Math.sin(angle)), (float) (Ypush*Math.cos(angle)));
			}
		}
	}
	
	public void render(SpriteBatch batcher) {
		int x = 0;
		int y = 0;
		if (onLeft) {
			x = 60;
			y = 240;
		} else if (onRight) {
			x = world.getWidth()-60;
			y = 240;
		} else {
			return;
		}
//		float angle = (float) Math.atan2(x-4-t.x, t.y-y+4);
//		batcher.draw(AssetLoader.jsFrame, x-19, y-19, 15, 15, 30, 30, size/12, size/12, 0);
		if (Math.sqrt(Math.pow(t.x-x+4,2) + Math.pow(t.y-y+4,2)) < size) {
//			batcher.draw(AssetLoader.jsTouch, t.x-4, t.y-4, 4, 4, 8, 8, size/12, size/12, (float) Math.toDegrees(angle));
		} else {
//			float pointX = (float) (x + size * Math.cos(angle+Math.PI/2));
//			float pointY = (float) (y + size * Math.sin(angle+Math.PI/2));
//			batcher.draw(AssetLoader.jsTouch, pointX-8, pointY-8, 4, 4, 8, 8, size/12, size/12, (float) Math.toDegrees(angle));
		}
	}
	
	public Vector2 getVelChange() {
		return velChange;
	}
}
