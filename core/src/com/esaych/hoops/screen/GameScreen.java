package com.esaych.hoops.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.esaych.hoops.app.IActivityRequestHandler;
import com.esaych.io.AssetLoader;
import com.esaych.io.MP;
import com.esaych.io.MultiplayerClient;
import com.esaych.io.MultiplayerServer;
import com.esaych.io.menus.Menu.MenuType;
import com.esaych.io.menus.Options;
import com.esaych.io.touch.InputHandler;
import com.esaych.io.touch.TouchData;
import com.esaych.world.Renderer;
import com.esaych.world.World;
import com.esaych.world.aspects.HighScores;
import com.esaych.world.aspects.Music;
import com.esaych.world.aspects.PowerUps;
import com.esaych.world.worldtypes.DefaultWorld;
import com.esaych.world.worldtypes.HardcoreWorld;
import com.esaych.world.worldtypes.InfinityWorld;
import com.esaych.world.worldtypes.IntroWorld;
import com.esaych.world.worldtypes.StartMenuWorld;
import com.esaych.world.worldtypes.multiplayer.MultiplayerWorld;
import com.esaych.world.worldtypes.multiplayer.client.ClientWorld;
import com.esaych.world.worldtypes.multiplayer.server.HoopsSpreeWorld;

public class GameScreen implements Screen {
	
	private World world;
	private Renderer renderer;
	private float runTime = 0;
	private IActivityRequestHandler reqHandler;
	
	private int gameHeight;
	private int gameWidth;
	
	public GameState gameState;

	public GameScreen(IActivityRequestHandler reqHandler) {
		this.reqHandler = reqHandler;
		setState(GameState.START);
		showAds(0);
        gameHeight = 300;
        gameWidth = (Gdx.graphics.getWidth() * gameHeight) / Gdx.graphics.getHeight();
        world = new IntroWorld(gameWidth, gameHeight, this);
		renderer = new Renderer(this, gameWidth, gameHeight);
		Gdx.input.setInputProcessor(new InputHandler(this));
		HighScores.load();
//		setState(GameState.STARTMENU);
		
//		MultiplayerServer.start(6866, this);
		
//		MultiplayerClient.connect("56.1", 6866, this);
	}
	
	public void print(Object s) {
		System.out.flush();
		System.out.println(s);
	}
	
	public enum GameState {
		START, STARTMENU, PLAYING, FROZEN, GAMEOVER, TUTORIAL;
	}
	
	public void setState(GameState state) {
		GameState prevState = gameState;
		gameState = state;
		switch (gameState) {
		case START:
			break;
		case PLAYING:
			MP.send("PLAYING");
			TouchData.clear();
			world.getMenu().setType(MenuType.DISABLED);
			if (prevState == GameState.STARTMENU ) {
				resetWorld(false);
			}

            if (prevState != GameState.TUTORIAL)
			    Music.playGameMusic();
			
			if (world.getHighScore() > 15)
				showAds(1);
			else
				showAds(0);
			
			break;
		case FROZEN:
			MP.send("FROZEN");
			Music.playPauseMusic();
			world.getMenu().setType(MenuType.PAUSE);
			showAds(0);
			world.getBroadcast().bc("", 0);
			break;
		case STARTMENU:
			MP.send("END_CONNECT");
			resetWorld(true);
			world.getMenu().setType(MenuType.START);
			if (!AssetLoader.prefs.getBoolean("LoginRequested")) {
				AssetLoader.prefs.putBoolean("LoginRequested", true);
				AssetLoader.prefs.flush();
				login();
			}
			Music.playIntro();
			break;
		case GAMEOVER:
			MP.send("OVER");
			showAds(0);
			runTime = 0;
			break;
		case TUTORIAL:
			showAds(0);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void render(float delta) {
		switch (gameState) {
		case START:
			runTime += delta;
			((IntroWorld) world).update(delta, runTime);
			if (world.hasExplosion())
				world.getExplosion().update(delta);
			renderer.introRender(runTime);
			break;
		case STARTMENU:
		case PLAYING:
			runTime += delta;
			world.update(delta, runTime);
			renderer.render(runTime);
			break;
		case FROZEN:
			if (world.hasExplosion())
				world.getExplosion().update(delta);
			if (world instanceof MultiplayerWorld)
				((MultiplayerWorld) world).checkReadLine();
			renderer.render(runTime);
			break;
		case GAMEOVER:
			renderer.onlyMenuRender(0);
			break;
		case TUTORIAL:
			runTime += delta/2;
			renderer.render(runTime);
			world.getBomb().rotate(20, delta);
			break;
		}
		Music.incrementRunTime(delta);
	}
	
	public void showAds(float alpha) {
		reqHandler.showAds(alpha);
	}
	
	public void submitHighScore(int i, Options.GameType type) {
        switch (type) {
            case NORMAL:
                reqHandler.setLeaderHighscore(i, "CgkIkaqY2a8QEAIQAQ");
                break;
            case INFINITY:
                break;
            case HARDCORE:
                reqHandler.setLeaderHighscore(i, "CgkIkaqY2a8QEAIQCw");
                break;
        }
    }

	public void addAchievement(String id) {
		reqHandler.setNewAchievement(id);
	}
	
	public void login() {
		reqHandler.servicesLogin();
	}
	
	public void logout() {
		reqHandler.servicesLogout();
	}
	
	public void openLeaderboards(Options.GameType gameType) {
        if (gameType.equals(Options.GameType.NORMAL))
            reqHandler.openLeaderboards("CgkIkaqY2a8QEAIQAQ");
        else if (gameType.equals(Options.GameType.HARDCORE))
		    reqHandler.openLeaderboards("CgkIkaqY2a8QEAIQCw");
	}
	
	public void openAchievements() {
		reqHandler.openAchievements();
	}
	
	public boolean isLoggedIn() {
		return reqHandler.isLoggedIn();
	}
	
	public boolean isLoginFailure() {
		return reqHandler.isLoginFailure();
	}
	
	public int getScreenRotation() {
		return reqHandler.getScreenRotation();
	}
	
	public World getWorld() {
		return world;
	}
	
	public void resetWorld(boolean isStartMenu) {
		if (isStartMenu)
			world = new StartMenuWorld(gameWidth, gameHeight, this);
		else if (Options.getGameMode().equals(Options.GameType.NORMAL))
			world = new DefaultWorld(gameWidth, gameHeight, this);
        else if (Options.getGameMode().equals(Options.GameType.INFINITY))
            world = new InfinityWorld(gameWidth, gameHeight, this);
		else if (Options.getGameMode().equals(Options.GameType.HARDCORE))
			world = new HardcoreWorld(gameWidth, gameHeight, this);
		else if (Options.getGameMode().equals(Options.GameType.MULTIPLAYER)) {
			if (MultiplayerClient.isEnabled())
				world = new ClientWorld(gameWidth, gameHeight, this);
			else if (MultiplayerServer.isEnabled()) {
				if (MultiplayerServer.getGameType().equals("Hoop Spree"))
					world = new HoopsSpreeWorld(gameWidth, gameHeight, this);
				else if (MultiplayerServer.getGameType().equals("Bomb Suicide"))
					System.out.println("tbp");
				else
					System.err.println("MP world reset type undetectable");
			} else
				System.err.println("No MP connections for world reset");
		} else
			System.err.println("World reset type undetectable");
		
		renderer.updateWorld();
		world.reset();
		HighScores.clearStreak();
		PowerUps.clearPowerUps();
		renderer.updateWorld();
	}

	@Override
	public void resize(int width, int height) {
		System.out.println("Resized");
	}

	@Override
	public void show() {
		System.out.println("Showing");
	}

	@Override
	public void hide() {
		MP.send("END_CONNECT");
        HighScores.flush();
		System.out.println("Closing");
	}

	@Override
	public void pause() {
		System.out.println("Pause...");
	}

	@Override
	public void resume() {
		if (world.getMenu() != null) {
			if (world.getMenu().type == MenuType.OPTIONS) {
				world.getMenu().setType(MenuType.OPTIONS);
			}
			if (gameState == GameState.PLAYING)
				setState(GameState.FROZEN);
		}
		System.out.println("Resume!");
	}

	@Override
	public void dispose() {
		System.out.println("DISPOSED");
	}

}
