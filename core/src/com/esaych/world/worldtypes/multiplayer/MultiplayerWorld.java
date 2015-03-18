package com.esaych.world.worldtypes.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esaych.hoops.screen.GameScreen;
import com.esaych.hoops.screen.GameScreen.GameState;
import com.esaych.io.AssetLoader;
import com.esaych.io.MP;
import com.esaych.io.menus.Menu.MenuType;
import com.esaych.io.menus.Options;
import com.esaych.objects.Explosion;
import com.esaych.objects.PowerUp;
import com.esaych.objects.physical.Ball;
import com.esaych.objects.physical.PredictedBomb;
import com.esaych.objects.physical.ball.GhostBall;
import com.esaych.objects.physical.ball.MultiBall;
import com.esaych.world.aspects.HighScores;
import com.esaych.world.aspects.Music;
import com.esaych.world.aspects.PowerUps;
import com.esaych.world.worldtypes.GameWorld;

public abstract class MultiplayerWorld extends GameWorld {

	protected int opponentScore = 0;
	protected GhostBall opponentBall;
	protected String readLine = "";
	
	public MultiplayerWorld(int width, int height, GameScreen gamescreen) {
		super(width, height, gamescreen);
		bomb = new PredictedBomb(this, (int) bomb.getPos().x, (int) bomb.getPos().y);
		hoop.setPos(gameWidth/2-11, gameHeight/2-5+70);
		opponentBall = new GhostBall(this, width/2-77, height/10 + 12);
	}
	
	public void update(float delta, float runTime) {
		super.update(delta, runTime);
		opponentBall.update(delta);
		checkReadLine();
	}
	
	public void checkReadLine() {
		readLine = MP.read();
		if (readLine.startsWith("GHOSTBALL ")) {
			System.out.println("Comp: " + readLine);
			String[] dat = readLine.split(" ");
			opponentBall.setPos(Float.parseFloat(dat[1]) * gameWidth, Float.parseFloat(dat[2]));
			opponentBall.setVelocity(new Vector2(Float.parseFloat(dat[3]) * gameWidth, Float.parseFloat(dat[4])));
			String udlr = dat[5];
			opponentBall.up = udlr.contains("u");
			opponentBall.down = udlr.contains("d");
			opponentBall.left = udlr.contains("l");
			opponentBall.right = udlr.contains("r");
		} else if (readLine.equals("FROZEN")) {
			if (gs.gameState != GameState.FROZEN)
				gs.setState(GameState.FROZEN);
		} else if (readLine.equals("PLAYING")) {
			if (gs.gameState != GameState.PLAYING)
				gs.setState(GameState.PLAYING);
		} else if (readLine.equals("OVER")) {
			if (gs.gameState == GameState.GAMEOVER)
				return;
			Music.playEndMusic();
			HighScores.clearStreak();
			hoopBlaster.terminate();
			menu.setType(MenuType.MP_GAMEOVER);
		} else if (readLine.equals("END_CONNECT")) {
			endConnection();
		} else if (readLine.startsWith("HOOP ")) {
			String[] dat = readLine.split(" ");
			hoop.setPos(Float.parseFloat(dat[1]) * gameWidth, Float.parseFloat(dat[2]));
			opponentScore++;
		} else if (readLine.startsWith("LIFE ")) {
			setLife(Integer.parseInt(readLine.split(" ")[1]));
		} else if (readLine.startsWith("SCORE ")) {
			String[] dat = readLine.split(" ");
			opponentScore = Integer.parseInt(dat[1]);
			gameHighScore = Integer.parseInt(dat[2]);
			score = gameHighScore;
		} else if (readLine.equals("EXPLOSION")) {
			isMyFault = false;
			if (Options.getVolumes()[1])
				AssetLoader.explode.play();
			gs.gameState = GameState.FROZEN;
			exp = new Explosion(bomb.getPos().x, bomb.getPos().y, bomb.getRot(), this);
			Gdx.input.vibrate(500);
		}
	}
	
	public void endConnection() {
		if (gs.gameState == GameState.GAMEOVER)
			return;
		Music.playEndMusic();
		HighScores.clearStreak();
		hoopBlaster.terminate();
		menu.setType(MenuType.MP_LOST_CONNECT);
	}
	
	public int getOpponentScore() {
		return opponentScore;
	}
	
	public GhostBall getOpponentBall() {
		return opponentBall;
	}
	
	@Override
	public int getScore() {
		return score + opponentScore;
	}

	@Override
	public boolean checkTutorial() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean isMyFault = true;
	
	@Override
	public void terminateExplosion() {
		exp = null;
		gs.gameState = GameState.PLAYING;
		if (!isMyFault)
			return;
		if (explodedBall instanceof MultiBall) {
			multiBall.remove(explodedBall);
			PowerUps.addPower(new PowerUp(this, "2 Second Invincibility", AssetLoader.bcOrange, 2), score < 10);
		} else if (multiBall.size() > 0) {
			Ball kill = multiBall.remove(0);
			ball.setPos(kill.getPos().x, kill.getPos().y);
			PowerUps.addPower(new PowerUp(this, "2 Second Invincibility", AssetLoader.bcOrange, 2), score < 10);
		} else if (lives.getLife() <= 1) {
			incScore(-5);
			Music.playEndMusic();
			HighScores.clearStreak();
			hoopBlaster.terminate();
			MP.send("SCORE " + score + " " + opponentScore);
			MP.send("OVER");
			return;
		} else {
			incScore(-5);
			incLife(-1);
			HighScores.clearStreak();
			hoopBlaster.terminate();
			PowerUps.addPower(new PowerUp(this, "2 Second Invincibility", AssetLoader.bcOrange, 2), score < 10);
		}
		MP.send("SCORE " + score + " " + opponentScore);
		MP.send("LIFE " + getLife());
	}

	@Override
	public void incScore(int x) {
		score += x;
		gameHighScore = score;
	}

	@Override
	public void playBombCollision() {
		if (gs.gameState == GameState.GAMEOVER)
			return;
		if (Options.getVolumes()[1])
			AssetLoader.explode.play();
		gs.gameState = GameState.FROZEN;
		isMyFault = true;
		MP.send("EXPLOSION");
		exp = new Explosion(bomb.getPos().x, bomb.getPos().y, bomb.getRot(), this);
		Gdx.input.vibrate(500);
	}
	
	@Override
	public void addHoopPoint() {
		if (Options.getVolumes()[1])
			AssetLoader.point.play();
		incScore(1);
		hoop.move();
		MP.send("HOOP " + (hoop.getPos().x/gameWidth) + " " + hoop.getPos().y);
		HighScores.addStreak(1);
		/*
		if (streak == 30 && highScore >= 70 && highScore-score > 10) {
			broadcast.bc("30 Hoops / 0 Explosions: Hoop Blaster UNLEASHED", 5);
			PowerUps.addHoop(PowerUps.getPowerHoop(PowerType.HOOP_BLASTER, this));
		} else if (streak % 10 == 0 && streak != 10) {
			broadcast.bc(streak + " Hoops / 0 Deaths", 5);
		}
		PowerUps.addRandomHoop(this, score, highScore);
		if (score % 10 == 0 && score > 0 && !recievedLife.contains(score)) {
			if (getHighScore() == 10 && !tut2)
				lifeHoops.add(new LifeHoop(this, getWidth(.8), 180));
			else
				lifeHoops.add(new LifeHoop(this));
			recievedLife.add(score);
		}
		*/
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void processExplosion() {
        if (lives.getLife() <= 1) {
            Music.playEndMusic();
            HighScores.addHS(gameHighScore, Options.GameType.MULTIPLAYER, gs);
            HighScores.clearStreak();
            hoopBlaster.terminate();
            menu.setType(MenuType.GAMEOVER);
            return;
        } else {
            incLife(-1);
            HighScores.clearStreak();
            int x = (int) (Math.random() * getWidth());
            int y = (int) (Math.random() * getHeight());
            bomb.setPos(x, y);
            hoopBlaster.terminate();
            PowerUps.addPower(new PowerUp(this, "2 Second Invincibility", AssetLoader.bcOrange, 2), score < 10);
        }
    }
	
}
