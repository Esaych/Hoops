package com.esaych.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.esaych.io.AssetLoader;
import com.esaych.objects.physical.Ball;
import com.esaych.world.World;
import com.esaych.world.worldtypes.GameWorld;
import com.esaych.world.worldtypes.IntroWorld;
import com.esaych.world.worldtypes.multiplayer.MultiplayerWorld;

public class Explosion {
	private float runTime;
	private Vector2 pos;
	private World world;
	private float rot;
	
	public Explosion(float x, float y, float rot, World world) {
		pos = new Vector2(x-19, y-20);
		this.rot = rot;
		this.world = world;
	}
	
	public void update(float delta) {
		runTime += delta;
		if (world instanceof IntroWorld) {
			if (runTime > .7)
				world.terminateExplosion();
		} else {
			if (runTime > .9) 
				world.terminateExplosion();
		}
		if (world.getMultiBall().size() == 0) {
			for (Hoop hoop : world.getHoopBlaster().getHoopsList()) {
				int degrees = 0;
				Vector2 vec = new Vector2(pos.x - hoop.getPos().x, pos.y - hoop.getPos().y);
				vec.scl(1/vec.len());
				degrees = (int) vec.angle() + 180;
				hoop.setPos(hoop.getPos().x + (float)(8*Math.cos(Math.toRadians(degrees))), hoop.getPos().y + (float)(8*Math.sin(Math.toRadians(degrees))));
			}
		}
	}
	
	public float getRunTime() {
		return runTime;
	}
	
	public float getRot() {
		return rot;
	}
	
	public Vector2 getPos() {
		return pos;
	}
	
	public void render(SpriteBatch batcher, ShapeRenderer shapeRenderer) {
    	batcher.end();
    	//Enable Transparency
    	Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //Draw background darkness
    	shapeRenderer.begin(ShapeType.Filled);
    	shapeRenderer.setColor(new Color(0, 0, 0, runTime));
    	shapeRenderer.rect(0, 0, world.getWidth(), world.getHeight());
    	shapeRenderer.end();
    	//Draw Explosion then Ball, changing color depending on RunTime of Explosion   (No bomb is drawn)
    	batcher.begin();
    	if (runTime < .7)
    		batcher.draw(AssetLoader.bexplosion.getKeyFrame(runTime), world.getExplosion().getPos().x, world.getExplosion().getPos().y, 30, 30, 60, 60, 3, 3, world.getExplosion().getRot());
    	else
            batcher.draw(AssetLoader.bexplosion.getKeyFrame(runTime), world.getExplosion().getPos().x, world.getExplosion().getPos().y, 30, 30, 60, 60, 3, 3, 0);

    	if (world instanceof MultiplayerWorld && !((MultiplayerWorld)world).isMyFault) {
        	world.getBall().render(batcher, runTime);
        	return;
    	}
    	if (runTime < .2) {
    		if (world instanceof GameWorld) {
    			GameWorld gw = (GameWorld) world;
    			gw.getExplodedBall().render(batcher, runTime);
    		} else {
            	world.getBall().render(batcher, runTime);
    		}
    	} else if (runTime < .5)
    		if (world instanceof GameWorld) {
    			GameWorld gw = (GameWorld) world;
    			Ball b = gw.getExplodedBall();
    			batcher.draw(AssetLoader.ballRed, b.getPos().x, b.getPos().y, 7, 7, 14, 14, 2, 2, b.getRot());
    		} else {
    			batcher.draw(AssetLoader.ballRed, world.getBall().getPos().x, world.getBall().getPos().y, 7, 7, 14, 14, 2, 2, world.getBall().getRot());
    		}
	}
}
