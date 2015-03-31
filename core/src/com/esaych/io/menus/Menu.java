package com.esaych.io.menus;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.esaych.hoops.screen.GameScreen.GameState;
import com.esaych.io.AssetLoader;
import com.esaych.io.MP;
import com.esaych.io.MultiplayerClient;
import com.esaych.io.MultiplayerServer;
import com.esaych.io.Networking;
import com.esaych.io.touch.Touch;
import com.esaych.io.touch.TouchData;
import com.esaych.world.World;
import com.esaych.world.aspects.HighScores;
import com.esaych.world.aspects.Music;
import com.esaych.world.aspects.PowerUps;
import com.esaych.world.worldtypes.multiplayer.MultiplayerWorld;
import com.esaych.world.worldtypes.multiplayer.server.HoopsSpreeWorld;

public class Menu {

	public MenuType type;
	private World world;

	private boolean waitingLogin = false;
	private boolean waitingLogout = false;
	private boolean rendering = false;
	private MenuType finishRenderSetType = null;
    private boolean locked = true;

	private ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
	private ArrayList<MenuLabel> labels = new ArrayList<MenuLabel>();

	private int developerMode = 0;

	public Menu(World world) {
		this.world = world;
		type = MenuType.DISABLED;
	}
	
	public boolean isEnabled() {
		return !type.equals(MenuType.DISABLED);
	}

	public void disable() {
		world.getGameScreen().setState(GameState.PLAYING);
	}

	public void touchDAt(int x, int y, int pointer) {
		if (y <= 25 && x > world.getWidth()-25 && !world.getGameScreen().gameState.equals(GameState.FROZEN)) {
			world.getGameScreen().setState(GameState.PLAYING);
		}
	}
	
	public boolean isTester() {
		return developerMode >= 10;
	}

	public enum MenuType {
		DISABLED, START, PAUSE, OPTIONS, GAMEOVER, WELCOME, WELCOME_CONTROLS, WELCOME_HARDCORE, WELCOME_INFINITY, HIGH_TEN, CONFIRM_END, MP_CHOOSE, MP_SERVER_WAIT, MP_CLIENT_CONNECTING, MP_GAMEOVER, MP_LOST_CONNECT
	}

    public void updateLabels(float delta) {
        for (MenuLabel label : labels)
            label.update(delta);
        System.out.println("Labels Update");
        world.getMenuTransition().update(delta, world);
    }

    public void allowTypeChange(boolean status) {
        locked = !status;
    }

    private String hs = "%GAMEMODE% High Score: %HIGHSCORE%";

	public void setType(MenuType mType) {
        if (locked) {
            System.out.println("Locked: " + mType.name());
            MenuTransition.transitionTo(mType);
            return;
        }
		if (rendering) {
			finishRenderSetType = mType;
			return;
		}
        locked = true; //relock for the next transition
        System.out.println("Relocked");
		type = mType;
		switch (type) {
		case DISABLED:
			labels.clear();
			buttons.clear();
			break;
		case START:
			labels.clear();
			labels.add(new MenuLabelSpecialAnimate(world.getWidth(.5), 100, world.getWidth(1.2), hs, MenuLabelSpecialAnimate.MenuAnimation.LRSlide));
			buttons.clear();
			buttons.add(new MenuButton(world.getWidth(.5), 150, world.getWidth(.5), "Play Game"));
			buttons.add(new MenuButton(world.getWidth(.68), 210, world.getWidth(.35), "Options"));
			buttons.add(new MenuButton(world.getWidth(.32), 210, world.getWidth(.35), "Mode: %GAMEMODE%"));
			if (world.getGameScreen().isLoggedIn()) {
				buttons.add(new MenuButton(world.getWidth(.1875), 150, world.getWidth(.125), "%LEADER%"));
				buttons.add(new MenuButton(world.getWidth(.8125), 150, world.getWidth(.125), "%ACHIEVE%"));
			}
			//			Music.playTitle();
			break;
		case WELCOME:
			labels.clear();
			buttons.clear();
			buttons.add(new MenuButton(world.getWidth(.75), 257, world.getWidth(.3), "Okay, what else?", 2));
			break;
		case WELCOME_CONTROLS:
			labels.clear();
			buttons.clear();
			buttons.add(new MenuButton(world.getWidth(.75), 257, world.getWidth(.3), "Okay, got it!", 2));
			break;
		case HIGH_TEN:
			labels.clear();
			buttons.clear();
			buttons.add(new MenuButton(world.getWidth(.75), 245, world.getWidth(.3), "Okay, got it!", 2));
			break;
		case WELCOME_HARDCORE:
			labels.clear();
			buttons.clear();
			buttons.add(new MenuButton(world.getWidth(.75), 245, world.getWidth(.3), "Okay, got it!", 1));
            break;
        case WELCOME_INFINITY:
            labels.clear();
            buttons.clear();
            buttons.add(new MenuButton(world.getWidth(.75), 245, world.getWidth(.3), "Okay, got it!", 1));
            break;
        case PAUSE:
            labels.clear();
			labels.add(new MenuLabel(world.getWidth(.5), 100, world.getWidth(1.2), hs));
			buttons.clear();
			buttons.add(new MenuButton(world.getWidth(.5), 150, world.getWidth(.5), "Resume Game"));
			buttons.add(new MenuButton(world.getWidth(.68), 210, world.getWidth(.35), "Options"));
			buttons.add(new MenuButton(world.getWidth(.32), 210, world.getWidth(.35), "End Game"));
			break;
		case CONFIRM_END:
			labels.clear();
			labels.add(new MenuLabel(world.getWidth(.5), 35, world.getWidth(1.2), "Are you sure?"));
			buttons.clear();
			buttons.add(new MenuButton(world.getWidth(.5), 100, world.getWidth(.5), "Yes, KILL ME NOW"));
			buttons.add(new MenuButton(world.getWidth(.5), 160, world.getWidth(.5), "Yes, RESTART GAME"));
			buttons.add(new MenuButton(world.getWidth(.5), 220, world.getWidth(.5), "NO WAIT! GO BACK"));
			break;
		case OPTIONS:
			labels.clear();
			labels.add(new MenuLabel(world.getWidth(.5), 35, world.getWidth(1.2), "Game Options"));
			buttons.clear();
			buttons.add(new MenuButton(world.getWidth() - (world.getWidth(.25)) - (world.getWidth(.25)-15)/2, 120, world.getWidth(.25)-15, "%SOUND%"));
			buttons.add(new MenuButton(world.getWidth() - (world.getWidth(.25)) + (world.getWidth(.25)-15)/2, 120, world.getWidth(.25)-15, "%MUSIC%"));
			buttons.add(new MenuButton(world.getWidth(.25), 120, world.getWidth(.5)-30, "Tilt: %TILT%"));
			buttons.add(new MenuButton(world.getWidth(.25), 200, world.getWidth(.5)-30, "Log" + (world.getGameScreen().isLoggedIn() ? "out of" : "in to") + " Google Play"));
			buttons.add(new MenuButton(world.getWidth() - (world.getWidth(.25)), 200, world.getWidth(.5)-30, "Back"));
			break;
		case MP_CHOOSE:
			labels.clear();
			labels.add(new MenuLabel(world.getWidth(.25), 35, world.getWidth(.45), "Join Game"));
			labels.add(new MenuLabel(world.getWidth(.75), 35, world.getWidth(.45), "Start Multiplayer"));
			buttons.clear();
			buttons.add(new MenuButton(world.getWidth(.25), 80, world.getWidth(.15), "2"));
			buttons.add(new MenuButton(world.getWidth(.25)-world.getWidth(.15), 80, world.getWidth(.15), "1"));
			buttons.add(new MenuButton(world.getWidth(.25)+world.getWidth(.15), 80, world.getWidth(.15), "3"));
			buttons.add(new MenuButton(world.getWidth(.25), 120, world.getWidth(.15), "5"));
			buttons.add(new MenuButton(world.getWidth(.25)-world.getWidth(.15), 120, world.getWidth(.15), "4"));
			buttons.add(new MenuButton(world.getWidth(.25)+world.getWidth(.15), 120, world.getWidth(.15), "6"));
			buttons.add(new MenuButton(world.getWidth(.25), 160, world.getWidth(.15), "8"));
			buttons.add(new MenuButton(world.getWidth(.25)-world.getWidth(.15), 160, world.getWidth(.15), "7"));
			buttons.add(new MenuButton(world.getWidth(.25)+world.getWidth(.15), 160, world.getWidth(.15), "9"));
			buttons.add(new MenuButton(world.getWidth(.25), 200, world.getWidth(.15), "0"));
			buttons.add(new MenuButton(world.getWidth(.25)-world.getWidth(.15), 200, world.getWidth(.15), "<"));
			buttons.add(new MenuButton(world.getWidth(.25)+world.getWidth(.15), 200, world.getWidth(.15), "-"));
			buttons.add(new MenuButton(world.getWidth(.25), 240, world.getWidth(.5)-30, "%CONNECT%"));
			buttons.add(new MenuButton(world.getWidth(.75), 100, world.getWidth(.5)-30, "Hoop Spree"));
			buttons.add(new MenuButton(world.getWidth(.75), 160, world.getWidth(.5)-30, "Bomb Against Me"));
			buttons.add(new MenuButton(world.getWidth(.75), 220, world.getWidth(.5)-30, "Back"));
			break;
		case MP_SERVER_WAIT:
			labels.clear();
			labels.add(new MenuLabel(world.getWidth(.5), 35, world.getWidth(1.2), "Waiting Other User Connection"));
			labels.add(new MenuLabel(world.getWidth(.5), 95, world.getWidth(.4), "Fancy Loading Animation"));
			labels.add(new MenuLabel(world.getWidth(.25), 155, world.getWidth(.45), "Friend Enter Server Code: "));
			labels.add(new MenuLabel(world.getWidth(.75), 155, world.getWidth(.45), Networking.getCode()));
			buttons.clear();
			buttons.add(new MenuButton(world.getWidth(.5), 220, world.getWidth(.8)-30, "Abort Connection"));
			break;
		case MP_CLIENT_CONNECTING:
			labels.clear();
			labels.add(new MenuLabel(world.getWidth(.5), 35, world.getWidth(1.2), "Searching for Connection..."));
			labels.add(new MenuLabel(world.getWidth(.5), 95, world.getWidth(.4), "Fancy Loading Animation"));
			buttons.clear();
			buttons.add(new MenuButton(world.getWidth(.5), 220, world.getWidth(.8)-30, "Abort Connection"));
			break;
		case MP_LOST_CONNECT:
			labels.clear();
			labels.add(new MenuLabel(world.getWidth(.5), 78, world.getWidth(1.2), "Opponent Lost Connection"));
			labels.add(new MenuLabel(world.getWidth(.25), 106, world.getWidth(.45), "Your Score"));
			labels.add(new MenuLabel(world.getWidth(.75), 106, world.getWidth(.45), "Opponent Score"));
			buttons.clear();
			buttons.add(new MenuButton(world.getWidth(.5), 252, world.getWidth(.5), "Main Menu"));
			if (world.getGameScreen().isLoggedIn()) {
				buttons.add(new MenuButton(world.getWidth(.1875), 252, world.getWidth(.125), "%LEADER%"));
				buttons.add(new MenuButton(world.getWidth(.8125), 252, world.getWidth(.125), "%ACHIEVE%"));
			}
			MPworld = (MultiplayerWorld) world;
			myScore = MPworld.getScore() - MPworld.getOpponentScore();
			theirScore = MPworld.getOpponentScore();
			world.getGameScreen().setState(GameState.GAMEOVER);
			break;
		case MP_GAMEOVER:
			labels.clear();
			if (world instanceof HoopsSpreeWorld)
				labels.add(new MenuLabel(world.getWidth(.5), 78, world.getWidth(1.2), "Hoops Spree Results"));
			labels.add(new MenuLabel(world.getWidth(.25), 106, world.getWidth(.45), "Your Score"));
			labels.add(new MenuLabel(world.getWidth(.75), 106, world.getWidth(.45), "Opponent Score"));
			buttons.clear();
			buttons.add(new MenuButton(world.getWidth(.5), 252, world.getWidth(.5), "Main Menu"));
			if (world.getGameScreen().isLoggedIn()) {
				buttons.add(new MenuButton(world.getWidth(.1875), 252, world.getWidth(.125), "%LEADER%"));
				buttons.add(new MenuButton(world.getWidth(.8125), 252, world.getWidth(.125), "%ACHIEVE%"));
			}
			MPworld = (MultiplayerWorld) world;
			myScore = MPworld.getScore() - MPworld.getOpponentScore();
			theirScore = MPworld.getOpponentScore();
			world.getGameScreen().setState(GameState.GAMEOVER);
			break;
		case GAMEOVER:
			labels.clear();
			buttons.clear();
			buttons.add(new MenuButton(world.getWidth(.5), 252, world.getWidth(.5), "Main Menu"));
			if (world.getGameScreen().isLoggedIn()) {
				buttons.add(new MenuButton(world.getWidth(.1875), 252, world.getWidth(.125), "%LEADER%"));
				buttons.add(new MenuButton(world.getWidth(.8125), 252, world.getWidth(.125), "%ACHIEVE%"));
			}
			highScoreY = 108;
			highScoreYVel = 0;
			world.getGameScreen().setState(GameState.GAMEOVER);
			break;
		default:
			break;
		}
	}
	
	private float highScoreY = 108;
	private float highScoreYVel = 0;
	public static String mpIP = "";
	private MultiplayerWorld MPworld = null;
	private int myScore = 0;
	private int theirScore= 0;

	public void render(ShapeRenderer shapeRenderer, SpriteBatch batcher, float runTime) {
		switch (type) {
		case START:
		case PAUSE:
			batcher.draw(AssetLoader.logo, (world.getWidth()-165)/2, world.getHeight()/10, 78, 18, 157, 37, 2, 2, 0);
			break;
		case WELCOME:
			batcher.draw(AssetLoader.helpWelcome, world.getWidth(.5)-104, 25, 0, 0, 208, 29, 1, 1, 0);
			batcher.draw(AssetLoader.helpBall, world.getBall().getPos().x-190, world.getBall().getPos().y-3, 0, 0, 180, 70, 1, 1, 0);
			batcher.draw(AssetLoader.helpHoop, world.getHoop().getPos().x+40, world.getHoop().getPos().y-5, 0, 0, 209, 44, 1, 1, 0);
			batcher.draw(AssetLoader.helpBomb, world.getBomb().getPos().x+40, world.getBomb().getPos().y, 0, 0, 251, 70, 1, 1, 0);
			break;
		case WELCOME_CONTROLS:
			batcher.end();
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.line(world.getWidth(.5), 0, world.getWidth(.5), world.getHeight());
			shapeRenderer.line(world.getWidth(.5), world.getHeight()/2, world.getWidth(), world.getHeight()/2);
			shapeRenderer.line(world.getWidth(.25), 0, world.getWidth(.25), world.getHeight());
			shapeRenderer.end();
			batcher.begin();
			batcher.draw(AssetLoader.helpControls, world.getWidth(.5)-48, 25, 0, 0, 97, 25, 1, 1, 0);
			batcher.draw(AssetLoader.helpControlNote, world.getWidth()-210, world.getHeight()/2-20, 0, 0, 152, 40, 1, 1, 0);
			TextBounds b = AssetLoader.font.getBounds("UP AREA");
			AssetLoader.font.draw(batcher, "UP AREA", world.getWidth(.75) - b.width/2, world.getHeight()*.2f);
			b = AssetLoader.font.getBounds("DOWN AREA");
			AssetLoader.font.draw(batcher, "DOWN AREA", world.getWidth(.75) - b.width/2, world.getHeight()*.7f);
			b = AssetLoader.font.getBounds("LEFT AREA");
			AssetLoader.font.draw(batcher, "LEFT AREA", world.getWidth(.125)-b.width/2, world.getHeight()*.45f);
			b = AssetLoader.font.getBounds("RIGHT AREA");
			AssetLoader.font.draw(batcher, "RIGHT AREA", world.getWidth(.375)-b.width/2, world.getHeight()*.45f);
			break;
		case WELCOME_HARDCORE:
			batcher.draw(AssetLoader.helpHardcore, world.getWidth(.5)-120, 25, 0, 0, 241, 25, 1, 1, 0);
			batcher.draw(AssetLoader.helpOneLife, world.getBall().getPos().x-103, world.getBall().getPos().y-30, 0, 0, 207, 28, 1, 1, 0);
			break;
        case WELCOME_INFINITY:
            batcher.draw(AssetLoader.helpInfinity, world.getWidth(.5)-107, 25, 0, 0, 215, 29, 1, 1, 0);
            batcher.draw(AssetLoader.helpInfinityInstruct, world.getWidth(.75)-81, world.getBall().getPos().y-30, 0, 0, 162, 47, 1, 1, 0);
            break;
		case HIGH_TEN:
			batcher.draw(AssetLoader.helpMoreHoops, world.getWidth(.5)-71, 25, 0, 0, 143, 28, 1, 1, 0);
			batcher.draw(AssetLoader.helpLife, world.getLifeHoops().get(0).getPos().x-226, world.getLifeHoops().get(0).getPos().y-5, 0, 0, 206, 47, 1, 1, 0);
			batcher.draw(AssetLoader.helpPower, PowerUps.getPowerHoops().get(0).getPos().x-185, PowerUps.getPowerHoops().get(0).getPos().y-5, 0, 0, 165, 70, 1, 1, 0);
			break;
		case OPTIONS:
			if (waitingLogin && world.getGameScreen().isLoggedIn()) {
				waitingLogin = false;
				setType(lastType);
			} else if (waitingLogin && world.getGameScreen().isLoginFailure()) {
				System.out.println("Game Login Fail: " + world.getGameScreen().isLoginFailure());
				waitingLogin = false;
				setType(lastType);
			} else if (waitingLogout && !world.getGameScreen().isLoggedIn()) {
				waitingLogout = false;
				setType(lastType);
			}
			break;
		case MP_CLIENT_CONNECTING:
			if (!MultiplayerClient.isEnabled())
				MultiplayerClient.connect(mpIP.replaceAll("-", "."), 6866, world.getGameScreen());
			break;
		case MP_GAMEOVER:
			batcher.end();
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.rect(0, 0, world.getWidth(), world.getHeight());
			shapeRenderer.end();
			batcher.begin();
			batcher.draw(AssetLoader.gameover, world.getWidth(.5)-147, 10, 0, 0, 147, 30, 2, 2, 0);
			AssetLoader.bigfont.draw(batcher, String.valueOf(myScore), world.getWidth(.25)-AssetLoader.bigfont.getBounds(String.valueOf(myScore)).width/2, 140);
			AssetLoader.bigfont.draw(batcher, String.valueOf(theirScore), world.getWidth(.75)-AssetLoader.bigfont.getBounds(String.valueOf(theirScore)).width/2, 140);
			if (myScore > theirScore)
				batcher.draw(AssetLoader.winner, world.getWidth(.5)-79, 180, 0, 0, 79, 23, 2, 2, -10);
			else if (myScore == theirScore)
				batcher.draw(AssetLoader.tie, world.getWidth(.5)-34, 180, 0, 0, 34, 23, 2, 2, -10);
			else
				batcher.draw(AssetLoader.loser, world.getWidth(.5)-64, 180, 0, 0, 64, 23, 2, 2, -10);
			break;
		case MP_LOST_CONNECT:
			batcher.end();
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.rect(0, 0, world.getWidth(), world.getHeight());
			shapeRenderer.end();
			batcher.begin();
			batcher.draw(AssetLoader.gameover, world.getWidth(.5)-147, 10, 0, 0, 147, 30, 2, 2, 0);
			AssetLoader.bigfont.draw(batcher, String.valueOf(myScore), world.getWidth(.25)-AssetLoader.bigfont.getBounds(String.valueOf(myScore)).width/2, 140);
			AssetLoader.bigfont.draw(batcher, String.valueOf(theirScore), world.getWidth(.75)-AssetLoader.bigfont.getBounds(String.valueOf(theirScore)).width/2, 140);
			batcher.draw(AssetLoader.forfeit, world.getWidth(.5)-85, 180, 0, 0, 85, 23, 2, 2, -10);
			break;
		case GAMEOVER:
			for (Touch t : TouchData.getTouches()) {
				if (t != null)
					highScoreYVel += t.clearDirection().y * .3;
			}
			highScoreY += highScoreYVel;
			highScoreYVel *= .7;
			if (highScoreY > 108)
				highScoreY = 108;
			if (highScoreY < 108 - 10*28)
				highScoreY = 108 - 10*28;
			labels.clear();
			boolean shownHS = false;
			for (int a = 0; a < 15; a++) {
				int score = HighScores.getScore(a, Options.getGameMode());
				if (!shownHS && score == world.getGameHighScore()) {
					shownHS = true;
					if (highScoreY + 28*a < 78)
						continue;
					labels.add(new MenuLabel(world.getWidth(.5), (int) (highScoreY + 28*a), world.getWidth(.55), ">    " + score + "    <"));
				} else {
					if (highScoreY + 28*a < 78)
						continue;
					labels.add(new MenuHighScoreLabel(world.getWidth(.5), (int) (highScoreY + 28*a), world.getWidth(.55), score));
				}
			}
			if (!shownHS) {
				labels.add(new MenuLabel(world.getWidth(.5), (int) (highScoreY + 28*14), world.getWidth(.55), ">    " + world.getGameHighScore() + "    <"));
			}
			labels.add(new MenuLabel(world.getWidth(.5), 78, world.getWidth(.6), "High Scores"));
			batcher.end();
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.rect(0, 0, world.getWidth(), world.getHeight());
			shapeRenderer.end();
			batcher.begin();
			batcher.draw(AssetLoader.gameover, world.getWidth(.5)-147, 10, 0, 0, 147, 30, 2, 2, 0);
			break;
		default:
			break;
		}
		rendering = true;
		for (MenuLabel l : labels)
			l.render(batcher);
		for (MenuButton b : buttons)
			b.render(batcher, TouchData.getTouches(), runTime);
		rendering = false;
		if (finishRenderSetType != null) {
			setType(finishRenderSetType);
			finishRenderSetType = null;
		}
	}

	private MenuType lastType;

	public void touchUAt(int x, int y, int pointer) {
		float[] xy = TouchData.convertToGameSize(x, y);
		x = (int) xy[0];
		y = (int) xy[1];
		MenuType toType = null;
		for (MenuButton b : buttons) {
			String check = b.checkTouch(new Touch(x, y));
            if (check.equals("Play Game")) {
                if (Options.getGameMode().equals(Options.GameType.MULTIPLAYER)) {
                    lastType = type;
                    mpIP = "";
                    toType = MenuType.MP_CHOOSE;
                    break;
                }


                toType = MenuType.DISABLED;
                developerMode = 0;

            } else if (check.equals("Resume Game") || check.equals("Okay, got it!")) {
                toType = MenuType.DISABLED;
                developerMode = 0;

            } else if (check.equals("Options")) {
                lastType = type;
                toType = MenuType.OPTIONS;
                developerMode = 0;

            } else if (check.equals("Abort Connection")) {
                MP.send("END_CONNECT");


                toType = lastType;

            } else if (check.equals("Back") || check.equals("NO WAIT! GO BACK")) {
                toType = lastType;

            } else if (check.equals("Main Menu")) {
                if (highScoreYVel > .001)
                    return;
                world.getGameScreen().setState(GameState.STARTMENU);

            } else if (check.equals("End Game")) {
                lastType = type;
                toType = MenuType.CONFIRM_END;

            } else if (check.equals("Yes, KILL ME NOW")) {
                HighScores.addHS(world.getGameHighScore(), Options.getGameMode(), world.getGameScreen());
                world.getGameScreen().setState(GameState.STARTMENU);

            } else if (check.equals("Yes, RESTART GAME")) {
                world.getGameScreen().resetWorld(false);
                toType = MenuType.DISABLED;

            } else if (check.equals("%MUSIC%")) {
                Options.setVolumes(!Options.getVolumes()[0], Options.getVolumes()[1]);
                if (!Options.getVolumes()[0])
                    Music.stop();
                else
                    Music.playIntro();

            } else if (check.equals("%SOUND%")) {
                Options.setVolumes(Options.getVolumes()[0], !Options.getVolumes()[1]);

            } else if (check.equals("%LEADER%")) {
                world.getGameScreen().openLeaderboards(Options.getGameMode());

            } else if (check.equals("%ACHIEVE%")) {
                world.getGameScreen().openAchievements();

            } else if (check.equals("Tilt: %TILT%")) {
                Options.setTilt(!Options.getTilt());
                developerMode++;

            } else if (check.equals("Mode: %GAMEMODE%")) {
                for (MenuLabel label : labels) //if there are more animations later, this has to be changed
                    if (label instanceof MenuLabelSpecialAnimate)
                        ((MenuLabelSpecialAnimate) label).animate(hs);
                Options.setGameMode(Options.nextGameMode(isTester()));
            } else if (check.equals("%CONNECT%")) {
                System.out.println("MENU: Connecting to Server");
                MultiplayerClient.connect(mpIP.replaceAll("-", "."), 6866, world.getGameScreen());
                toType = MenuType.MP_CLIENT_CONNECTING;

            } else if (check.equals("Hoop Spree") || check.equals("Bomb Against Me")) {
                System.out.println("MENU: Starting Server");
                MultiplayerServer.start(6866, world.getGameScreen(), check);
                toType = MenuType.MP_SERVER_WAIT;

            } else if (check.equals("Login to Google Play")) {
                world.getGameScreen().login();
                waitingLogin = true;

            } else if (check.equals("Logout of Google Play")) {
                world.getGameScreen().logout();
                waitingLogout = true;

            } else if (check.equals("Okay, what else?")) {
                setType(MenuType.WELCOME_CONTROLS);

            } else if (check.equals("0") || check.equals("1") || check.equals("2") || check.equals("3") || check.equals("4") || check.equals("5") || check.equals("6") || check.equals("7") || check.equals("8") || check.equals("9") || check.equals("-")) {
                if (mpIP.length() < 7)
                    mpIP += check;

            } else if (check.equals("<")) {
                if (!mpIP.equals(""))
                    mpIP = mpIP.substring(0, mpIP.length() - 1);

            }
			if (!check.equals("") && Options.getVolumes()[1]) {
				AssetLoader.button_click.play(.5f);
			}
		}
		if (toType != null) {
			if (toType == MenuType.DISABLED)
				disable();
			else
				setType(toType);
		}
	}
}
