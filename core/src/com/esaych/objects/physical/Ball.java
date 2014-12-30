package com.esaych.objects.physical;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.esaych.io.AssetLoader;
import com.esaych.objects.Physical;
import com.esaych.world.World;

public class Ball extends Physical {
	
	public Ball(World world, int x, int y) { 
		super(world, x, y);
		setBounds(new Circle(pos.x + 7, pos.y + 7, 14));
		setGravity(890);
	}
	
	public void update(float delta) {
		super.update(delta);
		
		rotate(vel.x * 4, delta);
		
		setBounds(new Circle(pos.x + 7, pos.y + 7, 14));
	}
	
	public void render(SpriteBatch batcher, float runTime) {
		batcher.draw(AssetLoader.ballAnim.getKeyFrame(runTime), pos.x, pos.y, 7, 7, 14, 14, 2, 2, getRot());
	}
	
}
