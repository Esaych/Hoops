package com.esaych.objects.hoop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esaych.io.AssetLoader;
import com.esaych.objects.Hoop;
import com.esaych.world.World;

public class PowerHoop extends Hoop {
	
	public String name;
	public TextureRegion hoop;
	public TextureRegion ball;
	public float duration;
	
	public PowerHoop(World world, int x, int y, String name, TextureRegion hoopColor, TextureRegion ballColor, float duration) {
		super(world, x, y);
		this.name = name;
		hoop = hoopColor;
		ball = ballColor;
		this.duration = duration;
	}
	
	public PowerHoop(World world, String name, TextureRegion hoopColor, TextureRegion ballColor, float duration) {
		super(world);
		this.name = name;
		hoop = hoopColor;
		ball = ballColor;
		this.duration = duration;
	}
	
	public void render(SpriteBatch batcher) {
		if (runTime > .2f)
			batcher.draw(hoop, pos.x, pos.y, 12, 5, 24, 10, 2, 2, 0);
		if (runTime < .4f)
			batcher.draw(AssetLoader.hoopGenerator.getKeyFrame(runTime), pos.x-2, pos.y-1, 12, 5, 26, 11, 2, 2, 0);
	}
	
	public boolean equals(PowerHoop hoop) {
		return hoop.name.equals(name);
	}
	
}
