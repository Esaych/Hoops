package com.esaych.world.aspects;

import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esaych.hoops.screen.GameScreen;
import com.esaych.io.AssetLoader;
import com.esaych.io.menus.Options;

public class HighScores {

    private static HashMap<Options.GameType, int[]> HSLists = new HashMap<Options.GameType, int[]>();

	private static int streak = 0;

    public static void initialize() {
        for (Options.GameType type : Options.GameType.values()) {
            AssetLoader.prefs.putString("HighScores_" + type.name(), "0;0;0;0;0;0;0;0;0;0;0;0;0;0;0");
        }

        //cleanup code from previous versions (< 1.6)
        String oldHS = AssetLoader.prefs.getString("HighScores");
        if (oldHS.length() > 0)
            AssetLoader.prefs.putString("HighScores_NORMAL", oldHS);
        oldHS = AssetLoader.prefs.getString("HCHighScores");
        if (oldHS.length() > 0)
            AssetLoader.prefs.putString("HighScores_HARDCORE", oldHS);
        AssetLoader.prefs.flush();
    }

	public static void load() {
        for (Options.GameType type : Options.GameType.values()) {
            HSLists.put(type, new int[15]);

            int i = 0;
            for (String s : AssetLoader.prefs.getString("HighScores_" + type.name()).split(";")) {
                HSLists.get(type)[i] = Integer.parseInt(s);
                i++;
            }

            Arrays.sort(HSLists.get(type));
        }
	}
	
	public static void flush() {
        for (Options.GameType type : Options.GameType.values()) {
            flush(type);
        }
	}

    public static void flush(Options.GameType type) {
        String hs = "";
        for (int i : HSLists.get(type))
            hs += i+";";
        AssetLoader.prefs.putString("HighScores_" + type.name(), hs.substring(0, hs.length()-1));
        AssetLoader.prefs.flush();
    }
	
	public static int getHighScore(Options.GameType type) {
		return HSLists.get(type)[14];
	}
	
	public static void addHS(int hs, Options.GameType type, GameScreen gs) {

        int[] list = HSLists.get(type);

        if (list[0] >= hs)
            return;
        list[0] = hs;
        Arrays.sort(list);
        flush(type);
        gs.submitHighScore(list[14], type);
	}

	public static int getScore(int loc, Options.GameType type) {
		return HSLists.get(type)[14-loc];
	}
	
	public static int addStreak(int amount) {
		streak += amount;
		return streak;
	}
	
	public static void clearStreak() {
		streak = 0;
	}

}