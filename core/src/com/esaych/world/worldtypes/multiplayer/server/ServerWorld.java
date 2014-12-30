package com.esaych.world.worldtypes.multiplayer.server;

import com.esaych.hoops.screen.GameScreen;
import com.esaych.world.worldtypes.multiplayer.MultiplayerWorld;

/**
 * World for a hosted game, connects to all Client Worlds, does all computing and runs game
 * @author Samuel Holmberg
 *
 */
public class ServerWorld extends MultiplayerWorld {
	
	public ServerWorld(int width, int height, GameScreen gamescreen) {
		super(width, height, gamescreen);
		System.out.println("Server World created");
	}

	@Override
	public void update(float delta, float runTime) {
		super.update(delta, runTime);
	}
	
}
