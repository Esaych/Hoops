package com.esaych.objects.physical.ball;

import com.esaych.world.World;

public class MultiBall extends GameBall {

	public MultiBall(World world, int x, int y) {
		super(world, x, y);
		setFrictionScale((float) (.99 + (Math.random()*.02)-.01));
		setGravity((int) (890 + (Math.random()*10)-5));
	}

}
