package com.esaych.world.worldtypes;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.esaych.hoops.screen.GameScreen;
import com.esaych.hoops.screen.GameScreen.GameState;
import com.esaych.io.AssetLoader;
import com.esaych.io.menus.Options;
import com.esaych.objects.Explosion;
import com.esaych.objects.physical.ball.IntroBall;
import com.esaych.world.World;
import com.esaych.world.aspects.Music;

public class IntroWorld extends World {
	
	private Circle bombArea;
	
	public IntroWorld(int width, int height, GameScreen gamescreen) {
		super(width, height, gamescreen);
		ball = new IntroBall(this, -20, 20);
		bombArea = new Circle(450, getHeight()/10, 22);
	}
	
	public void update(float delta, float runTime) {
		if (runTime > 1)
			if (!hasExplosion()) {
				((IntroBall) ball).update(delta, runTime-1);
				if (Intersector.overlaps(ball.getBounds(), bombArea) || runTime > 4) {
					exp = new Explosion(450, getHeight()/10, 0, this);
					if (Options.getVolumes()[1])
						AssetLoader.explode.play();
					Music.playIntro();
				}
			}
	}
	
	public void terminateExplosion() {
		exp = null;
		getGameScreen().setState(GameState.STARTMENU);
	}

	@Override
	public void reset() {
		//lol, what is there to reset?
	}

}