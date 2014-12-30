package com.esaych.world.aspects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esaych.io.AssetLoader;

public class Broadcast {
	
	private String info;
	private float duration;
	private float width;
	private int x;
	private float lifeTime;
	
	public Broadcast(int screenWidth) {
		this.x = screenWidth/2;
	}
	
	public void bc(String info, float duration) {
		this.info = info;
		this.duration = duration;
		width = (int) AssetLoader.font.getBounds(info).width/2;
		lifeTime = 0;
	}
	
	public void update(float delta) {
		lifeTime += delta;
	}
	
	public void render(SpriteBatch batcher) {
		if (lifeTime < duration) {
			int y = (int) (lifeTime*60);
			if (y > 40)
				y = 40;
        	AssetLoader.bgfont.draw(batcher, info, x - width - 1, y-20);
        	AssetLoader.font.draw(batcher, info, x - width, y-21);
		}
	}
}
