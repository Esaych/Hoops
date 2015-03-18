package com.esaych.world.aspects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.esaych.io.AssetLoader;

public class Lives {
	
	int life = 0;
	boolean animating = false;
	Vector2 animLoc, gotoLoc;
	private float cacheDst = 0;
	
	public Lives(int num) {
		life = num;
	}
	
	public void setLife(int num) {
		life = num;
	}
	
	public int incLife(int num) {
		life += num;
		return life;
	}
	
	public int incLife(int num, float x, float y) {
		life += num;
		animLoc = new Vector2(x, y);
		animating = true;
		return life;
	}
	
	public int getLife() {
		return life;
	}
	
	public void update(float delta) {
		if (animating && animLoc != null && gotoLoc != null) {
			cacheDst = animLoc.dst2(gotoLoc);
			if (cacheDst < 1) {
				animLoc = null;
				animating = false;
			} else 
				animLoc.add(gotoLoc.sub(animLoc).scl(3).scl(delta));
		}
	}
	
	public void render(SpriteBatch batcher, int worldWidth) {
		if (animating) {
			float distance;
			if (gotoLoc == null)
				distance = (float) (worldWidth/2.0 - (life-1)*17.0/2);
			else {
				distance = (float) (worldWidth/2.0 - (life-1)*17.0/2 - 8.5/(cacheDst/100.0 + 1));
				System.out.println(17.0/(cacheDst + 1));
			}
			float a = distance;
			while (a < distance+(life-1)*17) {
				batcher.draw(AssetLoader.life, a, 5, 7, 7, 15, 15, 1, 1, 0);
				a += 17;
			}
			gotoLoc = new Vector2(a, 5);
			batcher.draw(AssetLoader.life, animLoc.x, animLoc.y, 7, 7, 15, 15, 1, 1, 0);
		} else {
			float distance = (float) (worldWidth/2.0 - life*17.0/2);
			for (float a = distance; a < distance+life*17; a+=17) {
				batcher.draw(AssetLoader.life, a, 5, 7, 7, 15, 15, 1, 1, 0);
			}
		}
	}
	
}
