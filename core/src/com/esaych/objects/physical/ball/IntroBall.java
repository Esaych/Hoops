package com.esaych.objects.physical.ball;

import com.badlogic.gdx.math.Vector2;
import com.esaych.objects.physical.Ball;
import com.esaych.world.World;

public class IntroBall extends Ball {
	
	public IntroBall(World world, int x, int y) {
		super(world, x, y);
		addVelocity(new Vector2(600, 500));
		setFrictionScale(1f);
	}
	
	public void update(float delta, float runTime) {
		if (runTime < 1)
			return;
		super.update(delta);
	}
}
