package com.esaych.world.worldtypes;

import java.util.ArrayList;

import com.esaych.hoops.screen.GameScreen;
import com.esaych.io.menus.Menu;
import com.esaych.io.menus.MenuLabelSpecialAnimate;
import com.esaych.io.touch.Joystick;
import com.esaych.objects.Hoop;
import com.esaych.objects.hoop.LifeHoop;
import com.esaych.objects.physical.Bomb;
import com.esaych.objects.physical.ball.GameBall;
import com.esaych.world.World;
import com.esaych.world.aspects.HoopBlaster;

public class StartMenuWorld extends World {

	public StartMenuWorld(int width, int height, GameScreen gamescreen) {
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

	@Override
	public void update(float delta, float runTime) {
		ball.update(delta);
		bomb.rotate(50, delta);
        menu.updateLabels(delta);
	}

	@Override
	public void reset() {
		ball = new GameBall(this, gameWidth/2-77, gameHeight/10 + 12);
		bomb = new Bomb(this, gameWidth/2-8, gameHeight/10 + 10);
		hoop = new Hoop(this, -100, -100);
		hoop.setLife(1);
		menu = new Menu(this);
		joystick = new Joystick(this);
		hoopBlaster = new HoopBlaster(this);
		lifeHoops = new ArrayList<LifeHoop>();
	}

	@Override
	public void terminateExplosion() {
	}

}
