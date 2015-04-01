package com.esaych.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.esaych.world.World;

public abstract class Physical {
	protected Vector2 pos;
	protected Vector2 vel;
	protected Vector2 acc;
	protected World world;
	
	protected int gravity;
	private int XMaxSpeed;
	private int YMaxSpeed;
	private float rot;
	private float frictionScale;
	
	private Circle bounds;
	
	public Physical(World world, int x, int y) {
		this.world = world;
		setGravity(0);
		pos = new Vector2(x, y);
		vel = new Vector2(0, 0);
		acc = new Vector2(0, gravity);
		setRotation(0);
		setFrictionScale(.99f);
		setBounds(new Circle(pos.x + 7, pos.y + 7, 14));
		setXMaxSpeed(1000);
		setYMaxSpeed(1000);
	}
	
	public void update(float delta) {
		//Physics
		vel.add(acc.cpy().scl(delta));
		vel.x = (float) (vel.x*frictionScale);
		pos.add(vel.cpy().scl(delta));
		
		//Speed Check
		if (vel.x > XMaxSpeed)
			vel.x = XMaxSpeed;
		if (vel.x < -XMaxSpeed)
			vel.x = -XMaxSpeed;
		if (vel.y > YMaxSpeed)
			vel.y = YMaxSpeed;
		if (vel.y < -YMaxSpeed)
			vel.y = -YMaxSpeed;
		
		//Ceiling/Floor check
		if (pos.y > world.getHeight()-21) {
			vel.y *= -.8;
			pos.y = world.getHeight()-21;
		} else if (pos.y < 7) {
			vel.y *= -.8;
			pos.y = 7;
		}
		
		//Side Check
		if (world.getScore() > 300) { //somewhat of a surprise >:)
			if (pos.x > world.getWidth()+14)
				pos.x = -20;
			else if (pos.x < -20)
				pos.x = world.getWidth()+14;
		} else {
			if (pos.x > world.getWidth()-21) {
				vel.x *= -.8;
				pos.x = world.getWidth()-21;
			} else if (pos.x < 7) {
				vel.x *= -.8;
				pos.x = 7;
			}
		}
	}
	
	public int getXMaxSpeed() {
		return XMaxSpeed;
	}
	
	public void setXMaxSpeed(int x) {
		XMaxSpeed = x;
	}
	
	public int getYMaxSpeed() {
		return YMaxSpeed;
	}
	
	public void setYMaxSpeed(int y) {
		YMaxSpeed = y;
	}
	
	public Vector2 getPos() {
		return pos;
	}
	
	public void setPos(float x, float y) {
		pos.x = x;
		pos.y = y;
		vel = new Vector2(0, 0);
	}
	
	public int getRot() {
		return (int) rot;
	}
	
	public Vector2 getVel() {
		return vel;
	}
	
	public void setGravity(int gravity) {
		this.gravity = gravity;
		acc = new Vector2(0, gravity);
	}
	
	public Circle getBounds() {
		return bounds;
	}
	
	public void addVelocity(Vector2 vec) {
		vel.add(vec);
	}
	
	public void setVelocity(Vector2 vec) {
		vel = vec;
	}
	
	public void setBounds(Circle circle) {
		bounds = circle;
	}
	
	public void setFrictionScale(float fr) {
		frictionScale = fr;
	}
	
	public void rotate(float amount, float delta) {
		rot += delta*amount;
	}
	
	public void setRotation(float degrees) {
		rot = degrees;
	}
	
	public abstract void render(SpriteBatch batcher, float runTime);
	
}
