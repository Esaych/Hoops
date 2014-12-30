package com.esaych.world.worldtypes;

import com.esaych.hoops.screen.GameScreen;
import com.esaych.io.AssetLoader;
import com.esaych.io.menus.Menu;
import com.esaych.io.menus.Options;
import com.esaych.objects.PowerUp;
import com.esaych.world.aspects.HighScores;
import com.esaych.world.aspects.PowerUps;

public class InfinityWorld extends GameWorld {

    public InfinityWorld(int width, int height, GameScreen gamescreen) {
        super(width, height, gamescreen);
        setLife(0);
        highScore = HighScores.getHighScore(Options.GameType.INFINITY);
        hsAnnounce = (highScore != 0);
    }

    @Override
    public boolean checkTutorial() {
        if (gs.gameState != GameScreen.GameState.TUTORIAL && getHighScore() == 0 && !tut1) {
            menu.setType(Menu.MenuType.WELCOME_INFINITY);
            tut1 = true;
            gs.setState(GameScreen.GameState.TUTORIAL);
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
        if (streak == 30 && highScore >= 70 && highScore-score > 10) {
            broadcast.bc("30 Hoops / 0 Explosions: Hoop Blaster UNLEASHED", 5);
            PowerUps.addHoop(PowerUps.getPowerHoop(PowerUps.PowerType.HOOP_BLASTER, this));
        } else if (streak % 10 == 0 && streak != 10) {
            broadcast.bc(streak + " Hoops / 0 Deaths", 5);
        }
        PowerUps.addRandomHoop(this, score, highScore);
    }

    @Override
    public void reset() {
        ball.setPos(gameWidth/2-7, gameHeight/2-7);
        bomb.setPos(gameWidth/2-11, gameHeight/2-11-70);
        hoop.setPos(gameWidth/2-11, gameHeight/2-5+70);
        setLife(0);
        setHighScore(HighScores.getHighScore(Options.GameType.INFINITY));
    }

    @Override
    public void processExplosion() {
        if (gameHighScore > 100)
            incScore(-10);
        else
            incScore(gameHighScore / -10);
        HighScores.addHS(gameHighScore, Options.GameType.INFINITY, gs);
        HighScores.clearStreak();
        int x = (int) (Math.random() * getWidth());
        int y = (int) (Math.random() * getHeight());
        bomb.setPos(x, y);
        hoopBlaster.terminate();
        PowerUps.addPower(new PowerUp(this, "2 Second Invincibility", AssetLoader.bcOrange, 2), score < 10);
    }
}
