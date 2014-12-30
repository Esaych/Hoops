package com.esaych.world.worldtypes.multiplayer.server;

import com.esaych.hoops.screen.GameScreen;
import com.esaych.io.MP;
import com.esaych.objects.physical.PredictedBomb;
import com.esaych.objects.physical.ball.GameBall;
/**
 * Hoops Spree Multiplayer world
 * @author Samuel Holmberg
 *
 */
public class HoopsSpreeWorld extends ServerWorld {
	
	private int lastUpdate = 0;
	
	public HoopsSpreeWorld(int width, int height, GameScreen gamescreen) {
		super(width, height, gamescreen);
		setLife(10);
		MP.send("LIFE 10");
	}
	
	@Override
	public void update(float delta, float runTime) {
		super.update(delta, runTime);
		if (lastUpdate > 50) {
			syncData();
			lastUpdate = 0;
		} else if (lastUpdate % 10 == 0) {
			GameBall ball = (GameBall) getBall();
			MP.send("GHOSTBALL " + ball.getPos().x/gameWidth + " " + ball.getPos().y + " " + ball.getVel().x/gameWidth + " " + ball.getVel().y + " k" + (ball.up?'u':"")+(ball.down?'d':"")+(ball.left?'l':"")+(ball.right?'r':""));
		}
		lastUpdate++;
	}
	
	public void syncData() {
		PredictedBomb b = (PredictedBomb) bomb;
		MP.send("BOMB " + b.getPos().x/gameWidth + " " + b.getPos().y + " " + b.getVel().x/gameWidth + " " + b.getVel().y + " " + b.getMovePattern());
		MP.send("SCORE " + score + " " + opponentScore);
	}
	
}
