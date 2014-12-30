package com.esaych.io.menus;

import com.esaych.io.AssetLoader;
import com.esaych.world.aspects.HighScores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Options {
	
	public static void setTilt(boolean type) {
		AssetLoader.prefs.putBoolean("Tilt", type);
		AssetLoader.prefs.flush();
	}
	
	public static boolean getTilt() {
		return AssetLoader.prefs.getBoolean("Tilt");
	}
	
	public static void setVolumes(boolean music, boolean sound) {
		AssetLoader.prefs.putBoolean("Music", music);
		AssetLoader.prefs.putBoolean("Sound", sound);
		AssetLoader.prefs.flush();
	}

    public static enum GameType {
        NORMAL, INFINITY, HARDCORE, MULTIPLAYER
    }

	public static void setGameMode(GameType mode) {
		AssetLoader.prefs.putString("GameMode", mode.name());
		AssetLoader.prefs.flush();
	}
	
	public static GameType getGameMode() {
		return GameType.valueOf(AssetLoader.prefs.getString("GameMode").toUpperCase());
	}
	
	public static GameType nextGameMode(boolean tester) { //General progression: Normal > Infinity > Hardcore > Multiplayer > Normal

        ArrayList<GameType> possibleVals = new ArrayList<GameType>(Arrays.asList(GameType.values()));
        possibleVals.add(GameType.NORMAL);

        if (!tester)
            possibleVals.remove(GameType.MULTIPLAYER);

        if (HighScores.getHighScore(GameType.NORMAL) < 100)
            possibleVals.remove(GameType.HARDCORE);

        if (HighScores.getHighScore(GameType.NORMAL) < 15)
            possibleVals.remove(GameType.INFINITY);

        for (GameType type : possibleVals) {
            if (type.equals(getGameMode()))
                return (possibleVals.get(possibleVals.indexOf(type) + 1));
        }

        return GameType.NORMAL;
	}
	
	public static Boolean[] getVolumes() {
		Boolean[] data = {AssetLoader.prefs.getBoolean("Music"), AssetLoader.prefs.getBoolean("Sound")};
		return data;
	}
}
