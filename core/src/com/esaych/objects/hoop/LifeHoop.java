package com.esaych.objects.hoop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esaych.io.AssetLoader;
import com.esaych.objects.Hoop;
import com.esaych.world.World;

public class LifeHoop extends Hoop {
	
	public LifeHoop(World world, int x, int y) {
		super(world, x, y);
	}
	
	public LifeHoop(World world) {
		super(world);
	}
	
	public void render(SpriteBatch batcher) {
		if (runTime > .2f)
			batcher.draw(AssetLoader.hcLife, pos.x, pos.y, 12, 5, 24, 10, 2, 2, 0);
		if (runTime < .4f)
			batcher.draw(AssetLoader.hoopGenerator.getKeyFrame(runTime), pos.x-2, pos.y-1, 12, 5, 26, 11, 2, 2, 0);
	}
	
}
