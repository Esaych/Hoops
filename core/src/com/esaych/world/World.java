package com.esaych.world;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.esaych.hoops.screen.GameScreen;
import com.esaych.io.menus.Menu;
import com.esaych.io.menus.MenuTransition;
import com.esaych.io.menus.Options;
import com.esaych.io.touch.Joystick;
import com.esaych.objects.Explosion;
import com.esaych.objects.Hoop;
import com.esaych.objects.hoop.LifeHoop;
import com.esaych.objects.physical.Ball;
import com.esaych.objects.physical.Bomb;
import com.esaych.objects.physical.ball.MultiBall;
import com.esaych.world.aspects.Broadcast;
import com.esaych.world.aspects.HoopBlaster;
import com.esaych.world.aspects.Lives;
import com.esaych.world.aspects.PowerUps;
import com.esaych.world.aspects.PowerUps.PowerType;

public abstract class World {

	protected int gameHeight;
	protected int gameWidth;
	protected int score = 0;
	protected int gameHighScore = 0;
	protected int highScore;
	
	protected Ball ball;
	protected Bomb bomb;
	protected Hoop hoop;
	protected Explosion exp;
	protected HoopBlaster hoopBlaster;
	
	protected ArrayList<LifeHoop> lifeHoops;
	protected ArrayList<MultiBall> multiBall;
	
	protected Joystick joystick;
	protected Menu menu;
    protected MenuTransition menuTransition;
	protected Broadcast broadcast;
	protected Lives lives;
	
	protected GameScreen gs;
	
	protected boolean hsAnnounce = false;
	private boolean displayHS = false;
	
	
	public World(int width, int height, GameScreen gamescreen) {
		gs = gamescreen;
		gameHeight = height;
		gameWidth = width;
		broadcast = new Broadcast(width);
		hoopBlaster = new HoopBlaster(this);
		multiBall = new ArrayList<MultiBall>();
		lives = new Lives(0);
        menuTransition = new MenuTransition();
	}
	
	public abstract void update(float delta, float runTime);
	
	public abstract void reset();
	
	public Ball getBall() {
		return ball;
	}
	
	public Bomb getBomb() {
		return bomb;
	}
	
	public Hoop getHoop() {
		return hoop;
	}
	
	public Broadcast getBroadcast() {
		return broadcast;
	}
	
	public Menu getMenu() {
		return menu;
	}

    public MenuTransition getMenuTransition() {
        return menuTransition;
    }
	
	public Joystick getJoystick() {
		return joystick;
	}
	
	public Explosion getExplosion() {
		return exp;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getGameHighScore() {
		return gameHighScore;
	}
	
	public int getHeight() {
		return gameHeight;
	}
	
	public int getWidth() {
		return gameWidth;
	}
	
	public int getWidth(double factor) {
		return (int) (gameWidth * factor);
	}
	
	public int getHighScore() {
		return highScore;
	}
	
	public void setHighScore(int i) {
		highScore = i;
	}
	
	public GameScreen getGameScreen() {
		return gs;
	}
	
	public int getLife() {
		return lives.getLife();
	}
	
	public ArrayList<LifeHoop> getLifeHoops() {
		return lifeHoops;
	}
	
	public HoopBlaster getHoopBlaster() {
		return hoopBlaster;
	}
	
	public void setLife(int l) {
		lives.setLife(l);
	}
	
	public void incLife(int l) {
		lives.incLife(l);
	}
	
	public boolean hasExplosion() {
		return exp != null;
	}
	
	public abstract void terminateExplosion();
	
	public boolean shouldDisplayHS() {
		return displayHS;
	}
	
	public void multiBall() {
		int ballNum = 0;
		while (ballNum < 2) {
			int x = (int) (gameWidth * Math.random());
			int y = (int) (gameHeight * Math.random());
			if (new Vector2(x, y).dst(bomb.getPos()) > 10) {
				multiBall.add(new MultiBall(this, x, y));
				ballNum++;
			}
		}
	}
	
	public ArrayList<MultiBall> getMultiBall() {
		return multiBall;
	}
	
	public void incScore(int x) {
		score += x;
		if (x < 0)
			hsAnnounce = true;
		if (score > gameHighScore)
			gameHighScore = score;
		if (score > highScore) {
			highScore = score;
			displayHS = true;
			if (hsAnnounce) {
				broadcast.bc("NEW HIGH SCORE", 3);
				hsAnnounce = false;
			}
			if (Options.getGameMode().equals(Options.GameType.NORMAL)) {
				switch (highScore) {
				case 10:
					PowerUps.addHoop(PowerUps.getPowerHoop(PowerType.FIRST_INVINCIBLE, this));
		    		gs.addAchievement("CgkIkaqY2a8QEAIQAg");
					break;
                case 15:
                    broadcast.bc("High Score 15: Infinity Game Mode Unlocked", 5);
                    break;
				case 20:
					broadcast.bc("High Score 20: Bomb Depressant Powerup Unlocked", 5);
					PowerUps.addHoop(PowerUps.getPowerHoop(PowerType.SLOW_BOMB, this));
					break;
				case 30:
					broadcast.bc("High Score 30: Multi Ball Powerup Unlocked", 5);
					PowerUps.addHoop(PowerUps.getPowerHoop(PowerType.MULTI_BALL, this));
					break;
				case 40:
					broadcast.bc("High Score 40: Bomb Shrinker Powerup Unlocked", 5);
					PowerUps.addHoop(PowerUps.getPowerHoop(PowerType.MINI_BOMB, this));
					break;
				case 50:
					broadcast.bc("High Score 50: Bomb Repellent Powerup Unlocked", 5);
					PowerUps.addHoop(PowerUps.getPowerHoop(PowerType.BOMB_REPELLENT, this));
					break;
				case 70:
					if (!gs.isLoggedIn())
						broadcast.bc("High Score 70: Hoop Blaster Powerup Unlocked", 5);
		    		gs.addAchievement("CgkIkaqY2a8QEAIQAw");
					break;
				case 100:
					if (!gs.isLoggedIn())
						broadcast.bc("High Score 100: Hardcore Gamemode Unlocked", 5);
		    		gs.addAchievement("CgkIkaqY2a8QEAIQCg");
		    		break;
				case 500:
					gs.addAchievement("CgkIkaqY2a8QEAIQCQ");
					break;
				default:
					break;
				}
			}
		}
	}

}
