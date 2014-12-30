package com.esaych.objects.physical;

import com.badlogic.gdx.math.Vector2;
import com.esaych.world.World;
/**
 * Bomb that has a predetermined random pattern of movement
 * @author Samuel Holmberg
 *
 */
public class PredictedBomb extends Bomb {
	
	private String movePattern = "-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-;-";
	
	public PredictedBomb(World world, int x, int y) {
		super(world, x, y);
	}
	
	@Override
	public void randMovement(float delta) {
		int rand = random.nextInt((int) (movementFactor * (1/(delta + .000001)) / world.getScore() * 3) + 1);
		if (rand == 1) {
			int degrees = (int) (random.nextInt(360));
			movePattern += ";" + (float)(push*Math.cos(Math.toRadians(degrees))/world.getWidth()) + "," + (float)(push*Math.sin(Math.toRadians(degrees)));
		} else
			movePattern += ";-";
		String[] moves = movePattern.split(";");
		if (!moves[0].equals("-"))
			addVelocity(new Vector2(Float.parseFloat(moves[0].split(",")[0]) * world.getWidth(), Float.parseFloat(moves[0].split(",")[1])));
		movePattern = movePattern.substring(movePattern.indexOf(';')+1);
	}
	
	public void setMovePattern(String pattern) {
		movePattern = pattern;
	}
	
	public String getMovePattern() {
		return movePattern;
	}
	
}
