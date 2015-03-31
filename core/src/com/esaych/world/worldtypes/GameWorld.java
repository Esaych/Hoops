package com.esaych.world.worldtypes;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.esaych.hoops.screen.GameScreen;
import com.esaych.hoops.screen.GameScreen.GameState;
import com.esaych.io.AssetLoader;
import com.esaych.io.menus.Menu;
import com.esaych.io.menus.MenuTransition;
import com.esaych.io.menus.Options;
import com.esaych.io.touch.Joystick;
import com.esaych.objects.Explosion;
import com.esaych.objects.Hoop;
import com.esaych.objects.PowerUp;
import com.esaych.objects.hoop.LifeHoop;
import com.esaych.objects.hoop.PowerHoop;
import com.esaych.objects.physical.Ball;
import com.esaych.objects.physical.Bomb;
import com.esaych.objects.physical.ball.GameBall;
import com.esaych.objects.physical.ball.MultiBall;
import com.esaych.world.World;
import com.esaych.world.aspects.HoopBlaster;
import com.esaych.world.aspects.PowerUps;

public abstract class GameWorld extends World {

	protected ArrayList<Integer> recievedLife = new ArrayList<Integer>();
	protected boolean tut1 = false;
	protected boolean tut2 = false;
	protected Ball explodedBall;

	public GameWorld(int width, int height, GameScreen gamescreen) {
		super(width, height, gamescreen);
		ball = new GameBall(this, width/2-77, height/10 + 12);
		bomb = new Bomb(this, width/2-8, height/10 + 10);
		hoop = new Hoop(this, -100, -100);
		hoop.setLife(1);
		menu = new Menu(this);
		joystick = new Joystick(this);
		hoopBlaster = new HoopBlaster(this);
		lifeHoops = new ArrayList<LifeHoop>();
	}

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
        lives.update(delta);

		manageAds();
	}

	public abstract boolean checkTutorial();

	public void manageAds() {
		float adsShow = 1;
		if (highScore < 15)
			adsShow = 0;
		else if (PowerUps.getPowerUps().size() > 0)
			adsShow = .25f;
		else if (ball.getPos().y > 230 && ball.getPos().x > getWidth(.5))
			adsShow = .25f;
		else if (bomb.getPos().y > 230 && bomb.getPos().x > getWidth(.5))
			adsShow = .25f;
		else if (hoop.getPos().y > 230 && hoop.getPos().x > getWidth(.5))
			adsShow = .25f;
		gs.showAds(adsShow);
	}

	public void collisionCheck() {
		hoopCollisionCheck();
		bombCollisionCheck();
		otherHoopsCollisionCheck();
	}

	public void hoopCollisionCheck() {
		if (Intersector.overlaps(ball.getBounds(), hoop.getBounds())) {
			addHoopPoint();
		} else {
			for (Ball ball : getMultiBall()) {
				if (Intersector.overlaps(ball.getBounds(), hoop.getBounds())) {
					addHoopPoint();
					break;
				}
			}
		}
	}

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
                lives.incLife(1, hoop.getPos().x, hoop.getPos().y);
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

	public void bombCollisionCheck() {
		if (!PowerUps.hasPower("Invincibility") && Intersector.overlaps(ball.getBounds(), bomb.getBounds())) {
			playBombCollision();
			explodedBall = ball;
		} else {
			for (Ball ball : getMultiBall()) {
				if (!PowerUps.hasPower("Invincibility") && Intersector.overlaps(ball.getBounds(), bomb.getBounds())) {
					playBombCollision();
					explodedBall = ball;
					break;
				}
			}
		}
	}

	public abstract void addHoopPoint();

    private boolean hoopsWereBlasting = false;

	public void playBombCollision() {
		if (Options.getVolumes()[1])
			AssetLoader.explode.play();
		gs.gameState = GameState.FROZEN;
        if (hoopBlaster.isBlasting())
            hoopsWereBlasting = true;
		exp = new Explosion(bomb.getPos().x, bomb.getPos().y, bomb.getRot(), this);
		Gdx.input.vibrate(500);
	}

	public Ball getExplodedBall() {
		return explodedBall;
	}

	public void terminateExplosion() {
		exp = null;
		gs.gameState = GameState.PLAYING;
		if (explodedBall instanceof MultiBall) {
			multiBall.remove(explodedBall);
			int x = (int) (Math.random() * getWidth());
			int y = (int) (Math.random() * getHeight());
			bomb.setPos(x, y);
            PowerUps.addPower(new PowerUp(this, "2 Second Invincibility", AssetLoader.bcOrange, 2), score < 10);
            if (hoopsWereBlasting) {
                hoopsWereBlasting = false;
                hoopBlaster.resume();
            }
		} else if (multiBall.size() > 0) {
			Ball kill = multiBall.remove(0);
			ball.setPos(kill.getPos().x, kill.getPos().y);
			int x = (int) (Math.random() * getWidth());
			int y = (int) (Math.random() * getHeight());
			bomb.setPos(x, y);
            PowerUps.addPower(new PowerUp(this, "2 Second Invincibility", AssetLoader.bcOrange, 2), score < 10);
            if (hoopsWereBlasting) {
                hoopsWereBlasting = false;
                hoopBlaster.resume();
            }
		} else {
            processExplosion();
            hoopsWereBlasting = false;
        }
	}

    public abstract void processExplosion();

}
