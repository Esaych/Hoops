package com.esaych.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.esaych.hoops.screen.GameScreen.GameState;
import com.esaych.io.AssetLoader;
import com.esaych.world.World;

public class Hoop {
	
	protected Vector2 pos;
	public World world;
	protected float runTime;
	
	private Rectangle bounds;
	
	public Hoop(World world, int x, int y) { 
		this.world = world;
		this.runTime = 0;
		this.runTime = 1;
		pos = new Vector2(x, y);
		bounds = new Rectangle(x+2, y+4, 18, 3);
	}
	
	public Hoop(World world) {
		this.world = world;
		this.runTime = 0;
		int x = (int) (Math.random() * world.getWidth()*.8) + world.getWidth()/10 - 12;
		int y = (int) (Math.random() * world.getHeight() * .8) + world.getHeight()/10 - 5;
		pos = new Vector2(x, y);
		while (pos.dst(world.getBall().getPos()) < 30) {
			x = (int) (Math.random() * world.getWidth()*.8) + world.getWidth()/10 - 12;
			y = (int) (Math.random() * world.getHeight() * .8) + world.getHeight()/10 - 5;
			pos = new Vector2(x, y);
		}
			
		bounds = new Rectangle(x+2, y+4, 18, 3);
	}
	
	public void move() {
		double randNumber = Math.random();
		runTime = 0;
		pos.y = (int) (randNumber * (world.getHeight()-50)) + 25;
		randNumber = Math.random();
		pos.x = (int) (randNumber * (world.getWidth()-50)) + 25;
		while (Intersector.overlaps(new Circle(pos.x+4, pos.y+4, 60), world.getBomb().getBounds()) || Intersector.overlaps(new Circle(pos.x+4, pos.y+4, 60), world.getBall().getBounds())) {
			randNumber = Math.random();
			pos.y = (int) (randNumber * (world.getHeight()-50)) + 25;
			randNumber = Math.random();
			pos.x = (int) (randNumber * (world.getWidth()-50)) + 25;
		}
		bounds = new Rectangle(pos.x+4, pos.y+4, 20, 6);
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void update(float delta) {
		runTime += delta;
	}
	
	public Vector2 getPos() {
		return pos;
	}
	
	public float getRunTime() {
		return runTime;
	}
	
	public void setPos(float x, float y) {
		pos.x = x;
		pos.y = y;
		bounds = new Rectangle(x+2, y+4, 18, 3);
	}
	
	public void setLife(float time) {
		runTime = time;
	}
	
	public void render(SpriteBatch batcher) {
		if (runTime > .2f)
		batcher.draw(AssetLoader.hoopAnim.getKeyFrame(runTime), pos.x, pos.y, 12, 5, 24, 10, 2, 2, 0);
		if (runTime < .4f && world.getGameScreen().gameState != GameState.TUTORIAL)
			batcher.draw(AssetLoader.hoopGenerator.getKeyFrame(runTime), pos.x-2, pos.y-1, 12, 5, 26, 11, 2, 2, 0);
	}

}
