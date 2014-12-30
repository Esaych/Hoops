package com.esaych.world.worldtypes.multiplayer.client;

import com.badlogic.gdx.math.Vector2;
import com.esaych.hoops.screen.GameScreen;
import com.esaych.io.MP;
import com.esaych.objects.physical.PredictedBomb;
import com.esaych.objects.physical.ball.GameBall;
import com.esaych.world.worldtypes.multiplayer.MultiplayerWorld;

/**
 * World that connects to a Server World, does nothing but mirror what the server tells it to
 * @author Samuel Holmberg
 *
 */
public class ClientWorld extends MultiplayerWorld {
	
	public ClientWorld(int width, int height, GameScreen gamescreen) {
		super(width, height, gamescreen);
		System.out.println("Client World Created");
	}
	
	@Override
	public void update(float delta, float runTime) {
		super.update(delta, runTime);
		if (readLine.startsWith("GHOSTBALL ")) {
			GameBall ball = (GameBall) getBall();
			MP.send("GHOSTBALL " + ball.getPos().x/gameWidth + " " + ball.getPos().y + " " + ball.getVel().x/gameWidth + " " + ball.getVel().y + " k" + (ball.up?'u':"")+(ball.down?'d':"")+(ball.left?'l':"")+(ball.right?'r':""));
		} else if (readLine.startsWith("BOMB ")) {
			String[] dat = readLine.split(" ");
			bomb.setPos(Float.parseFloat(dat[1])*gameWidth, Float.parseFloat(dat[2]));
			bomb.setVelocity(new Vector2(Float.parseFloat(dat[3])*gameWidth, Float.parseFloat(dat[4])));
			((PredictedBomb) bomb).setMovePattern(readLine.split(" ")[5]);
		}
	}

}
