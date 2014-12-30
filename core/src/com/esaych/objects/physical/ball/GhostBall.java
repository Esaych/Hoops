package com.esaych.objects.physical.ball;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.esaych.io.AssetLoader;
import com.esaych.objects.physical.Ball;
import com.esaych.world.World;

public class GhostBall extends Ball {
	
	private int Xpush;
	private int Ypush;

	public boolean up,down,left,right;
	
	public GhostBall(World world, int x, int y) {
		super(world, x, y);
		setXPush(1200);
		setYPush(1500);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		float Xpush = ((float) this.Xpush)*delta;
		float Ypush = ((float) this.Ypush)*delta;
		if (left)
			vel.add(-Xpush, 0);
		if (right)
			vel.add(Xpush, 0);
		if (up)
			vel.add(0, -Ypush);
		if (down)
			vel.add(0, Ypush);
	}
	
	@Override
	public void setBounds(Circle c) {
		
	}
	
	public void setXPush(int x) {
		Xpush = x;
	}
	
	public void setYPush(int y) {
		Ypush = y;
	}
	
	
	@Override
	public void render(SpriteBatch batcher, float runTime) {
        Color c = batcher.getColor();
        batcher.setColor(c.r, c.g, c.b, .3f); //set alpha to 0.3
		batcher.draw(AssetLoader.ballAnim.getKeyFrame(runTime), pos.x, pos.y, 7, 7, 14, 14, 2, 2, getRot());
		batcher.setColor(c);
	}

}
