package com.esaych.world.worldtypes;

import com.badlogic.gdx.math.Intersector;
import com.esaych.hoops.screen.GameScreen;
import com.esaych.hoops.screen.GameScreen.GameState;
import com.esaych.io.AssetLoader;
import com.esaych.io.menus.Options;
import com.esaych.io.menus.Menu.MenuType;
import com.esaych.objects.Hoop;
import com.esaych.objects.PowerUp;
import com.esaych.objects.hoop.LifeHoop;
import com.esaych.objects.hoop.PowerHoop;
import com.esaych.objects.physical.Ball;
import com.esaych.objects.physical.ball.MultiBall;
import com.esaych.world.aspects.HighScores;
import com.esaych.world.aspects.LifeBar;
import com.esaych.world.aspects.Music;
import com.esaych.world.aspects.PowerUps;
import com.esaych.world.aspects.PowerUps.PowerType;

import java.util.ArrayList;

public class DefaultWorld extends GameWorld {

    private LifeBar lifeBar;

	public DefaultWorld(int width, int height, GameScreen gamescreen) {
		super(width, height, gamescreen);
		setLife(5);
		highScore = HighScores.getHighScore(Options.GameType.NORMAL);
		hsAnnounce = (highScore != 0);
        lifeBar = new LifeBar(this);
	}

    @Override
    public void update(float delta, float runTime) {
        //Updates
        ball.update(delta);
        bomb.update(delta);
        broadcast.update(delta);
        hoop.update(delta);

        for (Hoop hoop : lifeHoops) {
            hoop.update(delta);
        }
        PowerUps.update(delta);
        for (MultiBall ball : getMultiBall())
            ball.update(delta);

        if (!checkTutorial())
            collisionCheck();

        hoopBlaster.update(delta);
        lifeBar.update(delta);
        manageAds();
    }

	@Override
	public boolean checkTutorial() {
		if (gs.gameState != GameState.TUTORIAL) {
			if (getHighScore() == 0 && !tut1) {
				menu.setType(MenuType.WELCOME);
				tut1 = true;
				gs.setState(GameState.TUTORIAL);
				return true;
			}
			if (getHighScore() == 10 && !tut2) {
                lifeBar.reset();
				gs.setState(GameState.TUTORIAL);
				menu.setType(MenuType.HIGH_TEN);
				tut2 = true;
				return true;
			}
		}
    	return false;
	}

	@Override
	public void addHoopPoint() {
		if (Options.getVolumes()[1])
			AssetLoader.point.play();
		incScore(1);
		hoop.move();
        lifeBar.inc(1);
		int streak = HighScores.addStreak(1);
        if (highScore >= 70) {
            if (streak == 30 && highScore - score > 10) {
                broadcast.bc("30 Hoops / 0 Explosions: Hoop Blaster UNLEASHED", 5);
                PowerUps.addHoop(PowerUps.getPowerHoop(PowerType.HOOP_BLASTER, this));
            } else if (streak % 10 == 0 && streak != 10) {
                broadcast.bc(streak + " Hoops / 0 Deaths", 5);
            }
        }
		PowerUps.addRandomHoop(this, score, highScore);
		if (getHighScore() == 10 && !tut2)
			lifeHoops.add(new LifeHoop(this, getWidth(.8), 180));
        else if (lifeBar.shouldSpawnLife() && getHighScore() > 10) {
			lifeHoops.add(new LifeHoop(this));
        }
	}

	@Override
	public void reset() {
		ball.setPos(gameWidth/2-7, gameHeight/2-7);
		bomb.setPos(gameWidth/2-11, gameHeight/2-11-70);
		hoop.setPos(gameWidth/2-11, gameHeight/2-5+70);
		setLife(5);
		setHighScore(HighScores.getHighScore(Options.GameType.NORMAL));
	}

    @Override
    public void processExplosion() {
        if (life <= 1) {
            Music.playEndMusic();
            HighScores.addHS(gameHighScore, Options.GameType.NORMAL, gs);
            HighScores.clearStreak();
            hoopBlaster.terminate();
            menu.setType(MenuType.GAMEOVER);
            return;
        } else {
            incLife(-1);
            lifeBar.givePenalty(score);
            HighScores.clearStreak();
            int x = (int) (Math.random() * getWidth());
            int y = (int) (Math.random() * getHeight());
            bomb.setPos(x, y);
            hoopBlaster.terminate();
            PowerUps.addPower(new PowerUp(this, "2 Second Invincibility", AssetLoader.bcOrange, 2), score < 10);
        }
    }

    @Override
    public void otherHoopsCollisionCheck() {
        ArrayList<Hoop> deadHoops = new ArrayList<Hoop>();
        for (PowerHoop hoop : PowerUps.getPowerHoops()) {
            if (Intersector.overlaps(ball.getBounds(), hoop.getBounds())) {
                deadHoops.add(hoop);
            } else {
                for (Ball ball : multiBall) {
                    if (Intersector.overlaps(ball.getBounds(), hoop.getBounds())) {
                        deadHoops.add(hoop);
                        break;
                    }
                }
            }
        }
        for (Hoop hoop : hoopBlaster.getHoopsList()) {
            if (Intersector.overlaps(ball.getBounds(), hoop.getBounds())) {
                deadHoops.add(hoop);
                incScore(1);
                lifeBar.inc(1);//                                <Change
                if (lifeBar.shouldSpawnLife()) {
                    lifeHoops.add(new LifeHoop(this));
                }
                if (Options.getVolumes()[1])
                    AssetLoader.point.play();
            } else {
                for (Ball ball : multiBall) {
                    if (Intersector.overlaps(ball.getBounds(), hoop.getBounds())) {
                        deadHoops.add(hoop);
                        incScore(1);
                        if (Options.getVolumes()[1])
                            AssetLoader.point.play();
                        break;
                    }
                }
            }
        }
        for (Hoop hoop : lifeHoops) {
            if (Intersector.overlaps(ball.getBounds(), hoop.getBounds())) {
                deadHoops.add(hoop);
                incLife(1);
                lifeBar.reset(); //                               <Change
                broadcast.bc("Life Hoop Collected", 3);
                if (score % 10 == 0 && score > 0 && !recievedLife.contains(score)) {
                    recievedLife.add(score);
                }
                if (Options.getVolumes()[1])
                    AssetLoader.point.play();
            } else {
                for (Ball ball : multiBall) {
                    if (Intersector.overlaps(ball.getBounds(), hoop.getBounds())) {
                        deadHoops.add(hoop);
                        incLife(1);
                        lifeBar.reset();
                        broadcast.bc("Life Hoop Collected", 3);
                        if (score % 10 == 0 && score > 0 && !recievedLife.contains(score)) {
                            recievedLife.add(score);
                        }
                        if (Options.getVolumes()[1])
                            AssetLoader.point.play();
                        break;
                    }
                }
            }
        }
        for (Hoop hoop : deadHoops) {
            if (hoop instanceof PowerHoop)
                PowerUps.transferHoopToPower((PowerHoop) hoop);
            else if (hoop instanceof LifeHoop)
                lifeHoops.remove(hoop);
            else
                hoopBlaster.removeHoop(hoop);
        }
    }

    public LifeBar getLifeBar() {
        return lifeBar;
    }
}
