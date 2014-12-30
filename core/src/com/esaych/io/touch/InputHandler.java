package com.esaych.io.touch;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.esaych.hoops.screen.GameScreen;
import com.esaych.hoops.screen.GameScreen.GameState;

public class InputHandler implements InputProcessor {
	
	GameScreen gs;
	private HashMap<Character, Integer> keys = new HashMap<Character, Integer>();
	private boolean key1 = true;
	
	
	public InputHandler(GameScreen gamescreen) {
		gs = gamescreen;
		TouchData.setSize(gs.getWorld().getWidth(), gs.getWorld().getHeight());
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
	}

	@Override
	public boolean keyDown(int keycode) {
		if ((keycode == Keys.BACK || keycode == Keys.MENU || keycode == Keys.ESCAPE)
				&& !gs.getWorld().hasExplosion()
				&& !gs.gameState.equals(GameState.FROZEN)) {
			if (gs.gameState.equals(GameState.GAMEOVER)) {
				gs.setState(GameState.STARTMENU);
			} else if (!gs.gameState.equals(GameState.STARTMENU))
				gs.setState(GameState.FROZEN);
			else
				Gdx.app.exit();
		} else if (keycode == Keys.UP) {
			TouchData.touchDAt(Gdx.graphics.getWidth(), 0, key1 ? 0:1);
			keys.put('u', key1 ? 0:1);
			key1 = !key1;
		} else if (keycode == Keys.DOWN) {
			TouchData.touchDAt(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), key1 ? 0:1);
			keys.put('d', key1 ? 0:1);
			key1 = !key1;
		} else if (keycode == Keys.LEFT) {
			TouchData.touchDAt(0, 0, key1 ? 0:1);
			keys.put('l', key1 ? 0:1);
			key1 = !key1;
		} else if (keycode == Keys.RIGHT) {
			TouchData.touchDAt(Gdx.graphics.getWidth()/4+1, 0, key1 ? 0:1);
			keys.put('r', key1 ? 0:1);
			key1 = !key1;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.UP) {
			TouchData.touchUAt(Gdx.graphics.getWidth(), 0, keys.get('u'));
			key1 = keys.remove('u') == 0;
		} else if (keycode == Keys.DOWN) {
			TouchData.touchUAt(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), keys.get('d'));
			key1 = keys.remove('d') == 0;
		} else if (keycode == Keys.LEFT) {
			TouchData.touchUAt(0, 0, keys.get('l'));
			key1 = keys.remove('l') == 0;
		} else if (keycode == Keys.RIGHT) {
			TouchData.touchUAt(Gdx.graphics.getWidth()/4+1, 0, keys.get('r'));
			key1 = keys.remove('r') == 0;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		TouchData.touchDAt(x, y, pointer);
		float[] xy = TouchData.convertToGameSize(x, y);
		x = (int) xy[0];
		y = (int) xy[1];
		//Pause Button
		if (y <= 25 && x > gs.getWorld().getWidth()-25 && !gs.getWorld().getGameScreen().gameState.equals(GameState.STARTMENU) && !gs.getWorld().getGameScreen().gameState.equals(GameState.GAMEOVER) && !gs.getWorld().hasExplosion())
			if (gs.getWorld().getGameScreen().gameState.equals(GameState.FROZEN))
				gs.getWorld().getMenu().disable();
			else
				gs.getWorld().getGameScreen().setState(GameState.FROZEN);
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		TouchData.touchUAt(x, y, pointer);
		if (gs.getWorld().getMenu() != null)
			gs.getWorld().getMenu().touchUAt(x, y, pointer);
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		TouchData.dragAt(x, y, pointer);
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
