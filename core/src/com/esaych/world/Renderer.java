package com.esaych.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.esaych.hoops.screen.GameScreen;
import com.esaych.hoops.screen.GameScreen.GameState;
import com.esaych.io.AssetLoader;
import com.esaych.io.touch.Touch;
import com.esaych.io.touch.TouchData;
import com.esaych.objects.Hoop;
import com.esaych.objects.PowerUp;
import com.esaych.objects.hoop.LifeHoop;
import com.esaych.objects.hoop.PowerHoop;
import com.esaych.objects.physical.Ball;
import com.esaych.objects.physical.ball.MultiBall;
import com.esaych.world.aspects.PowerUps;
import com.esaych.world.worldtypes.DefaultWorld;
import com.esaych.world.worldtypes.GameWorld;
import com.esaych.world.worldtypes.multiplayer.MultiplayerWorld;

public class Renderer {
	
	private GameScreen gs;
	private World world;
	
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batcher;
	
	public Renderer(GameScreen gamescreen, int gameWidth, int gameHeight) {
		
		gs = gamescreen;
		world = gs.getWorld();
		
		cam = new OrthographicCamera();
		cam.setToOrtho(true, gameWidth, gameHeight);
		
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		
		bgWdth = (1920 * gameHeight) / 720;
		bgOffset = (bgWdth - gameWidth)/2;
	}

	private int bgOffset = 0;
	private int bgWdth = 0;
	
	public void render(float runTime) {
		
		//Color Filters
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
        //Start Batcher
        batcher.begin();
        batcher.disableBlending();
        
        //Draw Background
        batcher.draw(AssetLoader.bg, 0 - bgOffset - (world.getWidth(.5)-world.getBall().getPos().x)/80, 0, 0, 0, bgWdth, world.getHeight(), 1, 1, 0);
        batcher.enableBlending();
        batcher.draw(AssetLoader.bgcity, 0 - bgOffset, 0, 0, 0, bgWdth, world.getHeight(), 1, 1, 0);
       
        
        
        if (world.getGameScreen().gameState != GameState.STARTMENU) {
        	//Draw Score
        	if (world instanceof MultiplayerWorld) {
        		AssetLoader.bgfont.draw(batcher, world.getGameHighScore() + "  -vs-  " + ((MultiplayerWorld)world).getOpponentScore(), 2, 0);
        		AssetLoader.font.draw(batcher, String.valueOf(world.getGameHighScore()), 3, -1);
        		AssetLoader.hsfont.draw(batcher, "  -vs-  ", 3 + AssetLoader.font.getBounds(String.valueOf(world.getGameHighScore())).width, -1);
        		AssetLoader.rdfont.draw(batcher, String.valueOf(((MultiplayerWorld)world).getOpponentScore()), 3 + AssetLoader.font.getBounds(world.getGameHighScore() + "  -vs-  ").width, -1);
        	} else {
        		AssetLoader.bgfont.draw(batcher, String.valueOf(world.getGameHighScore()), 2, 0);
        		if (world.getHighScore() == world.getGameHighScore() && world.shouldDisplayHS()) {
        			AssetLoader.hsfont.draw(batcher, String.valueOf(world.getGameHighScore()), 3, -1);
        		} else {
        			AssetLoader.font.draw(batcher, String.valueOf(world.getGameHighScore()), 3, -1);
        		}
        		int scoreDif = world.getGameHighScore()-world.getScore();
        		if (scoreDif > 0) {
        			String data = (world.getGameHighScore() < 15? " PENALTY" : "");
        			AssetLoader.bgfontSm.draw(batcher, "-" + scoreDif + data, 2, 17);
        			AssetLoader.fontSm.draw(batcher, "-" + scoreDif + data, 3, 16);
        		}
        	}

        	//Draw Pause Button
        	batcher.draw(AssetLoader.pause, world.getWidth()-19, 1, 0, 0, 6, 6, 3, 3, 0);
        	
//        	//Draw Joystick
//        	if (Options.getControls().equals("JOYSTICK")) {
//        		world.getJoystick().render(batcher);
//        	}
        	
//        	if (Options.getArrows()) {
        	batcher.end();
        	shapeRenderer.begin(ShapeType.Line);
        	shapeRenderer.setColor(Color.WHITE);
        	shapeRenderer.triangle(world.getWidth(.125)-8, world.getHeight()-25, world.getWidth(.125)+8, world.getHeight()-10, world.getWidth(.125)+8, world.getHeight()-40);
        	shapeRenderer.triangle(3*world.getWidth(.125)+8, world.getHeight()-25, 3*world.getWidth(.125)-8, world.getHeight()-10, 3*world.getWidth(.125)-8, world.getHeight()-40);
        	shapeRenderer.triangle(world.getWidth()-40, world.getHeight()/2-30, world.getWidth()-10, world.getHeight()/2-30, world.getWidth()-25, world.getHeight()/2-46);
        	shapeRenderer.triangle(world.getWidth()-40, world.getHeight()/2+30, world.getWidth()-10, world.getHeight()/2+30, world.getWidth()-25, world.getHeight()/2+46);
        	shapeRenderer.end();
        	shapeRenderer.begin(ShapeType.Filled);
        	for (Touch t : TouchData.getTouches()) {
        		if (t != null) {
        			if (t.x < world.getWidth(.5)) {
        				if (t.x < world.getWidth(.25))
        					shapeRenderer.triangle(world.getWidth(.125)-8, world.getHeight()-25, world.getWidth(.125)+8, world.getHeight()-10, world.getWidth(.125)+8, world.getHeight()-40);
        				else
        					shapeRenderer.triangle(3*world.getWidth(.125)+8, world.getHeight()-25, 3*world.getWidth(.125)-8, world.getHeight()-10, 3*world.getWidth(.125)-8, world.getHeight()-40);
        			} else {
        				if (t.y < world.getHeight()/2)
        					shapeRenderer.triangle(world.getWidth()-40, world.getHeight()/2-30, world.getWidth()-10, world.getHeight()/2-30, world.getWidth()-25, world.getHeight()/2-46);
        				else
        					shapeRenderer.triangle(world.getWidth()-40, world.getHeight()/2+30, world.getWidth()-10, world.getHeight()/2+30, world.getWidth()-25, world.getHeight()/2+46);
        			}
        		}
        	}
        	shapeRenderer.end();
        	batcher.begin();
//        	}
        	//Draw life
        	world.lives.render(batcher, world.getWidth());
			
        	//Draw Broadcasts
        	world.broadcast.render(batcher);
        	
        	//Draw PowerUp Bars
        	PowerUps.render(batcher, shapeRenderer, world);
        	
        	//Draw PowerHoops
        	for (PowerHoop power : PowerUps.getPowerHoops()) {
        		power.render(batcher);
        	}

            //Draw LifeBar
            if(world instanceof DefaultWorld)
                ((DefaultWorld) world).getLifeBar().render(batcher, shapeRenderer, world);

        	//Draw LifeHoops
        	for (LifeHoop hoop : world.getLifeHoops()) {
        		hoop.render(batcher);
        	}

        	//Draw Hoop
        	world.hoop.render(batcher);
        	
        	for (Hoop hoop : world.hoopBlaster.getHoopsList()) {
        		hoop.render(batcher);
        	}
        }
        
        
        
        
        //Explosion ifs
        if (world.hasExplosion()) {
        	if (world instanceof GameWorld) { //Leave the exploding ball render up to the explosion, render all other balls
        		GameWorld gw = (GameWorld) world;
        		if (gw.getExplodedBall() instanceof MultiBall) {
        			world.getBall().render(batcher, runTime);
        		}
        		for (Ball ball : world.getMultiBall()) {
        			if (gw.getExplodedBall() != ball)
        				ball.render(batcher, runTime);
        		}
        	}
        	world.getExplosion().render(batcher, shapeRenderer);
        } else {
        	//No Explosion? Draw Ball then Bomb
        	
        	if (world instanceof MultiplayerWorld) {
        		((MultiplayerWorld)world).getOpponentBall().render(batcher, runTime);
        	}
        	
        	world.getBall().render(batcher, runTime);
        	
        	//Draw MultiBalls
        	for (Ball ball : world.getMultiBall()) {
        		ball.render(batcher, runTime);
        	}
        	//Draw Powerballs
        	for (PowerUp power : PowerUps.getPowerUps()) {
        		power.render(batcher);
        	}
        	
        	world.getBomb().render(batcher, runTime);
        }
        
        //Menus
        if (world.getMenu().isEnabled()) {
        	world.getMenu().render(shapeRenderer, batcher, runTime);
            world.getMenuTransition().render(batcher, bgOffset, bgWdth, world);
        }
        
        //Complete drawing
        batcher.end();
        
	}
	
	public void onlyMenuRender(float runTime) {
		batcher.begin();
        if (world.getMenu().isEnabled()) {
        	world.getMenu().render(shapeRenderer, batcher, runTime);
        }
        batcher.end();
	}
	
	private boolean introHasHappened = false;
	
	public void introRender(float runTime) {       
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batcher.begin();
        batcher.disableBlending();
        
        //Draw Background
        batcher.draw(AssetLoader.bg, 0 - bgOffset - (world.getWidth(.5)-(world.getWidth(.5)-77))/80, 0, 0, 0, bgWdth, world.getHeight(), 1, 1, 0);
        batcher.enableBlending();
        batcher.draw(AssetLoader.bgcity, 0 - bgOffset, 0, 0, 0, bgWdth, world.getHeight(), 1, 1, 0);
        batcher.end();
        
        if (world.hasExplosion()) { //The explosion
        	introHasHappened = true;
            
        	float expRT = world.getExplosion().getRunTime();
        	//Enable Transparency
        	Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            //Draw background darkness
        	shapeRenderer.begin(ShapeType.Filled);
        	shapeRenderer.setColor(new Color(0, 0, 0, (float) (1 - (expRT/.7))));
        	shapeRenderer.rect(0, 0, world.getWidth(), world.getHeight());
        	shapeRenderer.end();
        	//Draw Explosion then Ball, changing color depending on RunTime of Explosion (No bomb is drawn)
        	batcher.begin();
			batcher.draw(AssetLoader.bexplosion.getKeyFrame(expRT), world.getExplosion().getPos().x, world.getExplosion().getPos().y, 30, 30, 60, 60, 3, 3, 0);
        	if (expRT < .2) {
        		batcher.draw(AssetLoader.ballAnim.getKeyFrame(runTime), world.getBall().getPos().x, world.getBall().getPos().y, 7, 7, 14, 14, 2, 2, world.getBall().getRot());
        	} else if (expRT < .5) {
        		world.getBall().render(batcher, runTime);
        	}
        	batcher.end();
        } else if (!introHasHappened) {
        	//No explosion yet? Draw a black background
        	shapeRenderer.begin(ShapeType.Filled);
            shapeRenderer.setColor(0f, 0f, 0f, 1);
            shapeRenderer.rect(0, 0, world.getWidth(), world.getHeight());
            shapeRenderer.end();
            //draw the rest
            batcher.begin();
            batcher.draw(AssetLoader.cmpny, (world.getWidth()-256)/2, (world.getHeight()-64)/2, 256, 64);
//            batcher.draw(AssetLoader.bombShadow, 431, world.getHeight()/10-19, 30, 30, 60, 60, 1, 1, 0);
			world.getBall().render(batcher, runTime);
			batcher.draw(AssetLoader.bombAnim.getKeyFrame(runTime), 450, world.getHeight()/10, 11, 11, 22, 22, 2, 2, 0);
        	batcher.end();
        }
	}
	
	public void updateWorld() {
		world = gs.getWorld();
	}
	
}
