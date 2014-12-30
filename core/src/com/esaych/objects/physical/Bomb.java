package com.esaych.objects.physical;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.esaych.io.AssetLoader;
import com.esaych.objects.Physical;
import com.esaych.world.World;
import com.esaych.world.aspects.PowerUps;

public class Bomb extends Physical {
	
	protected int push;
	protected int movementFactor;
	protected Random random;
	private boolean hasShrink;
	
	public Bomb(World world, int x, int y) { 
		super(world, x, y);
		setBounds(new Circle(pos.x + 11, pos.y + 11, 22));
		setFrictionScale(.999f);
		setPush(100);
		setMovementFactor(100);
		random = new Random();
	}
	
	public void update(float delta) {
		hasShrink = PowerUps.hasPower("Shrink");
		if (hasShrink)
			delta *= .5;
		if (PowerUps.hasPower("Depressant"))
			delta *= .2;
		super.update(delta);
//		if (PowerUps.hasPower("Matrix") && !PowerUps.hasPower("Invinc")) {
//			calcMatrix();
//		}
		if (PowerUps.hasPower("Repellent")) {
			calcRepellent();
		}
		if (world.getScore() > 0) {
			if (world.getScore() >= 49)
				rotate(10 * (50), delta);
			else
				rotate(10 * (world.getScore()+1), delta);
			randMovement(delta);
		}
		if (hasShrink) {
			setBounds(new Circle(pos.x + 11, pos.y + 11, 11));
		} else {
			setBounds(new Circle(pos.x + 11, pos.y + 11, 22));
		}
	}
	
	protected void randMovement(float delta) {
		int rand = random.nextInt((int) (movementFactor * (1/(delta + .000001)) / (world.getScore() * 3)) + 1);
		if (rand == 1) {
			int degrees = (int) (random.nextInt(360));
			addVelocity(new Vector2((float)(push*Math.cos(Math.toRadians(degrees))), (float)(push*Math.sin(Math.toRadians(degrees)))));
		}
	}
	
//	private void calcMatrix() {
//		
//		/*
//		 * World slows down
//		 * Bomb moves out of way
//		 * Ball is directly affected by touch. No velocity defined.
//		 */
//		
//		Ball b = world.getBall();
//		System.out.println("BALL ANGLE: " + b.getVel().angle());
//		Circle ballB = b.getBounds();
//		Circle bombB = getBounds();
//		float dist = new Vector2(ballB.x, ballB.y).dst(new Vector2(bombB.x, bombB.y));
//		if (dist > 60)
//			return;
//		Vector2 bvel = b.getVel();
//		Vector2 left = new Vector2((float)Math.cos(Math.toRadians(bvel.angle()+90)), (float)Math.sin(Math.toRadians(bvel.angle()+90)));
//		Vector2 right = new Vector2((float)Math.cos(Math.toRadians(bvel.angle()-90)), (float)Math.sin(Math.toRadians(bvel.angle()-90)));
//		Vector2 leftPos = b.getPos().cpy().add(left.scl(bvel.len()/10));
//		Vector2 rightPos = b.getPos().cpy().add(right.scl(bvel.len()/10));
//		System.out.println();
//		if (pos.dst(leftPos) < pos.dst(rightPos)) {
//			vel.add(left.div(left.len()/20));
//		} else {
//			vel.add(right.div(right.len()/20));
//		}
//	}
	
	private void calcRepellent() {
		int degrees = 0;
		Vector2 vec = new Vector2(world.getBall().getPos().x - pos.x, world.getBall().getPos().y - pos.y);
		vec.scl(1/vec.len());
		degrees = (int) vec.angle() + 180;
		double speed = world.getScore()/20 + 3;
		addVelocity(new Vector2((float)(speed*Math.cos(Math.toRadians(degrees))), (float)(speed*Math.sin(Math.toRadians(degrees)))));
	}
	
	public void setMovementFactor(int fr) {
		movementFactor = fr;
	}
	
	public void setPush(int ps) {
		push = ps;
	}
	
	public void render(SpriteBatch batcher, float runTime) {
		if (hasShrink)
    		batcher.draw(AssetLoader.bombAnim.getKeyFrame(runTime), pos.x, pos.y, 11, 11, 22, 22, 1, 1, getRot());
    	else
    		batcher.draw(AssetLoader.bombAnim.getKeyFrame(runTime), pos.x, pos.y, 11, 11, 22, 22, 2, 2, getRot());
	}
	
}
