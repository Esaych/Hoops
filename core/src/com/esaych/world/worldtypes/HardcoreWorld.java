package com.esaych.world.worldtypes;

import com.esaych.hoops.screen.GameScreen;
import com.esaych.hoops.screen.GameScreen.GameState;
import com.esaych.io.AssetLoader;
import com.esaych.io.menus.Menu.MenuType;
import com.esaych.io.menus.Options;
import com.esaych.world.aspects.HighScores;
import com.esaych.world.aspects.Music;
import com.esaych.world.aspects.PowerUps;

public class HardcoreWorld extends GameWorld {

	public HardcoreWorld(int width, int height, GameScreen gamescreen) {
		super(width, height, gamescreen);
		setLife(1);
		highScore = HighScores.getHighScore(Options.GameType.HARDCORE);
		hsAnnounce = (highScore != 0);
	}

	@Override
	public boolean checkTutorial() {
			if (gs.gameState != GameState.TUTORIAL && getHighScore() == 0 && !tut1) {
				menu.setType(MenuType.WELCOME_HARDCORE);
				tut1 = true;
				gs.setState(GameState.TUTORIAL);
				return true;
			}
		return false;
	}

	@Override
	public void addHoopPoint() {
		if (Options.getVolumes()[1])
			AssetLoader.point.play();
		incScore(1);
		hoop.move();
		int streak = HighScores.addStreak(1);
		if (streak % 10 == 0 && streak != 10) {
			broadcast.bc(streak + " Hoops / 0 Deaths", 5);
		}
		PowerUps.addRandomHoop(this, score, highScore);
	}

	@Override
	public void reset() {
		ball.setPos(gameWidth/2-7, gameHeight/2-7);
		bomb.setPos(gameWidth/2-11, gameHeight/2-11-70);
		hoop.setPos(gameWidth/2-11, gameHeight/2-5+70);
		setLife(1);
        setHighScore(HighScores.getHighScore(Options.GameType.HARDCORE));
    }

    @Override
    public void processExplosion() {
        Music.playEndMusic();
        HighScores.addHS(gameHighScore, Options.GameType.HARDCORE, gs);
        HighScores.clearStreak();
        hoopBlaster.terminate();
        menu.setType(MenuType.GAMEOVER);
        return;
    }

}
