package com.esaych.io;

import com.esaych.hoops.screen.GameScreen;
import com.esaych.hoops.screen.GameScreen.GameState;
import com.esaych.world.worldtypes.multiplayer.MultiplayerWorld;
import com.esotericsoftware.kryonet.Connection;


public class MP {
	
	public static GameScreen gameScreen;
	protected static String gameType;
	protected static NetworkListener listener;
	
	public static void send(String s) {
		MultiplayerServer.send(s);
		MultiplayerClient.send(s);
	}
	
	public static String read() {
		if (MultiplayerServer.isEnabled())
			return MultiplayerServer.read();
		else
			return MultiplayerClient.read();
	}
	
	public static void endGameConnection() {
		if (gameScreen.getWorld() instanceof MultiplayerWorld)
			((MultiplayerWorld) gameScreen.getWorld()).endConnection();
	}
	
	protected static void startGame() {
		gameScreen.setState(GameState.PLAYING);
		gameScreen.resetWorld(false);
	}
	
	public static void connected(Connection con) {
		MultiplayerServer.connected(con);
		MultiplayerClient.connected(con);
	}
	
	public static void disconnected(Connection con) {
		send("END_CONNECT");
	}
	
}
