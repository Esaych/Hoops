package com.esaych.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esaych.objects.physical.Ball;
import com.esaych.world.World;

public class PowerUp {
	
	public String name;
	public World world;
	public TextureRegion ballSprite;
	public float duration;
	public float lifeTime;
	
	public PowerUp(World world, String name, TextureRegion ballColor, float duration) {
		this.name = name;
		this.world = world;
		this.duration = duration;
		ballSprite = ballColor;
		lifeTime = 0;
	}
	
	public boolean shouldTerminate(float delta) {
		lifeTime += delta;
		if (lifeTime > duration)
			return true;
		return false;
	}
	
	public void render(SpriteBatch batcher) {
		if (lifeTime < .25 || ((int) (lifeTime*10/(duration-lifeTime+1.5f))) % 2 == 1) {
			batcher.draw(ballSprite, world.getBall().getPos().x, world.getBall().getPos().y, 7, 7, 14, 14, 2, 2, world.getBall().getRot());
			for (Ball ball : world.getMultiBall()) {
				batcher.draw(ballSprite, ball.getPos().x, ball.getPos().y, 7, 7, 14, 14, 2, 2, ball.getRot());
			}
		}
	}
	
	public boolean equals(PowerUp pwr) {
		return pwr.name.equals(name);
	}
	
}
