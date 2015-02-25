package com.esaych.world.aspects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.esaych.io.AssetLoader;
import com.esaych.io.menus.Options;
import com.esaych.objects.Hoop;
import com.esaych.objects.PowerUp;
import com.esaych.objects.hoop.PowerHoop;
import com.esaych.world.World;
import com.esaych.world.worldtypes.DefaultWorld;

public class PowerUps {
	
	private static ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
	private static ArrayList<PowerHoop> powerHoops = new ArrayList<PowerHoop>();
	
	public static ArrayList<PowerUp> getPowerUps() {
		return powerUps;
	}
	
	public static ArrayList<PowerHoop> getPowerHoops() {
		return powerHoops;
	}
	
	public static void update(float delta) {
		ArrayList<PowerUp> deadPwrs = new ArrayList<PowerUp>();
		for (PowerUp pwr : powerUps) {
			if (pwr.shouldTerminate(delta))
				deadPwrs.add(pwr);
		}
		for (PowerUp pwr : deadPwrs) {
			powerUps.remove(pwr);
		}
		for (Hoop hoop : powerHoops) {
			hoop.update(delta);
		}
	}
	
	public static void removeHoop(PowerHoop hoop) {
		powerHoops.remove(hoop);
	}
	
	public static void removePower(PowerUp pwr) {
		powerUps.remove(pwr);
	}
	
	public static void clearPowerUps() {
		powerUps.clear();
		powerHoops.clear();
	}
	
	public static void transferHoopToPower(PowerHoop hoop) {
		if (hoop.name.equals("Hoop Blaster"))
			hoop.world.getHoopBlaster().blastHoops();
		else if (hoop.name.equals("Multi Ball"))
			hoop.world.multiBall();
		else
			addPower(new PowerUp(hoop.world, hoop.name, hoop.ball, hoop.duration), true);
		powerHoops.remove(hoop);
	}
	
	public static void addHoop(PowerHoop pwrHoop) {
		for (PowerHoop pwr : powerHoops) {
			if (pwr.equals(pwrHoop)) {
				return;
			}
		}
		powerHoops.add(pwrHoop);
	}
	
	public static void addPower(PowerUp pwrUp, boolean shouldAnnounce) {
		powerUps.add(pwrUp);
		if (Options.getVolumes()[1])
			AssetLoader.powerup.play();
		if (shouldAnnounce)
			pwrUp.world.getBroadcast().bc(pwrUp.name + " Activated", 3);
		
	}
	
	public static boolean hasPower(String name) {
		for (PowerUp pwr : powerUps) {
			String pwrName = pwr.name.toUpperCase();
			if (pwrName.contains(name.toUpperCase()))
				return true;
		}
		return false;
	}
	
	public static enum PowerType {
		FIRST_INVINCIBLE, INVINCIBLE, SLOW_BOMB, MULTI_BALL, MINI_BOMB, BOMB_REPELLENT, HOOP_BLASTER
	}
	
	public static PowerHoop getPowerHoop(PowerType name, World world) {
		switch (name) {
		case FIRST_INVINCIBLE:
			return new PowerHoop(world, world.getWidth(.8), 70, "Extended Invincibility", AssetLoader.hcOrange, AssetLoader.bcOrange, 10);
		case INVINCIBLE:
			return new PowerHoop(world, "Extended Invincibility", AssetLoader.hcOrange, AssetLoader.bcOrange, 10);
		case SLOW_BOMB:
			return new PowerHoop(world, "Bomb Depressant", AssetLoader.hcAqua, AssetLoader.bcAqua, 10);
		case MULTI_BALL:
			return new PowerHoop(world, "Multi Ball", AssetLoader.hcGreen, AssetLoader.bcGreen, 10);
		case MINI_BOMB:
			return new PowerHoop(world, "Bomb Shrinker", AssetLoader.hcBlue, AssetLoader.bcBlue, 10);
		case BOMB_REPELLENT:
			return new PowerHoop(world, "Bomb Repellent", AssetLoader.hcYellow, AssetLoader.bcYellow, 10);
		case HOOP_BLASTER:
			return new PowerHoop(world, "Hoop Blaster", AssetLoader.hcPurple, AssetLoader.bcPurple, 10);
		default:
			return null;
		}
	}

    private static int timeWithoutPowerHoop = 0;

	public static void addRandomHoop(World world, int score, int highScore) {
		if (score < 10)
			return;
		if (score % 10 == 0)
			return;
		if (((int) (Math.random()*10)) != 1 && timeWithoutPowerHoop < 15) {
            timeWithoutPowerHoop++;
            return;
        }
        timeWithoutPowerHoop = 0;
		ArrayList<PowerHoop> possible = new ArrayList<PowerHoop>();
		if (highScore >= 10) {
			possible.add(getPowerHoop(PowerType.INVINCIBLE, world));
			possible.add(getPowerHoop(PowerType.INVINCIBLE, world));
		}
		if (highScore >= 20) {
			possible.add(getPowerHoop(PowerType.SLOW_BOMB, world));
			possible.add(getPowerHoop(PowerType.SLOW_BOMB, world));
		}
		if (highScore >= 30) {
			possible.add(getPowerHoop(PowerType.MULTI_BALL, world));
			possible.add(getPowerHoop(PowerType.MULTI_BALL, world));
		}
		if (highScore >= 40) {
			possible.add(getPowerHoop(PowerType.MINI_BOMB, world));
			possible.add(getPowerHoop(PowerType.MINI_BOMB, world));
		}
		if (highScore >= 50) {
			possible.add(getPowerHoop(PowerType.BOMB_REPELLENT, world));
			possible.add(getPowerHoop(PowerType.BOMB_REPELLENT, world));
		}
		if (highScore >= 70 && highScore-score > 10 && world instanceof DefaultWorld && score > 30)
			possible.add(getPowerHoop(PowerType.HOOP_BLASTER, world));
		
		addHoop(possible.get((int) (Math.random()*possible.size())));
	}

    public static void render(SpriteBatch batcher, ShapeRenderer shapeRenderer, World world) {
        int powerSize = PowerUps.getPowerUps().size();
        if (powerSize > 0) {
            batcher.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            for (PowerUp power : PowerUps.getPowerUps()) {
                float width = (power.duration - power.lifeTime) * 25;
                TextureRegion ball = power.ballSprite;
                Color color = null;
                if (width < 37 && ((int)width/5%2) == 0) {
                    color = Color.RED;
                } else if (ball.equals(AssetLoader.bcAqua)) {
                    color = new Color(0, 243/255f, 1, 1);
                } else if (ball.equals(AssetLoader.bcBlue)) {
                    color = new Color(0, 0, 1, 1);
                } else if (ball.equals(AssetLoader.bcGreen)) {
                    color = new Color(69/255f, 1, 0, 1);
                } else if (ball.equals(AssetLoader.bcOrange)) {
                    color = new Color(1, 157/255f, 0, 1);
                } else {
                    color = new Color(247/255f, 1, 0, 1);
                }
                shapeRenderer.setColor(color);
                shapeRenderer.rect(world.getWidth()-width-1, world.getHeight()-(4*powerSize), width, 3);
                powerSize--;
            }
            shapeRenderer.end();
            batcher.begin();
        }
    }
}
