package com.esaych.io.menus;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.esaych.io.AssetLoader;
import com.esaych.io.touch.Touch;

public class MenuButton {
	
	private Vector2 loc;
	private int width;
	private String content;
	private float freeze = 0;
	private float create = -1;
	private boolean locked = false;
	
	private int contentWidth;
	
	public MenuButton(int x, int y, int width, String content) {
		loc = new Vector2(x-width/2, y);
		this.width = width;
		this.content = content;
		contentWidth = (int) AssetLoader.bgfont.getBounds(content).width;
	}
	
	public MenuButton(int x, int y, int width, String content, float freeze) {
		this(x, y, width, content);
		this.freeze = freeze;
	}
	
	public boolean render(SpriteBatch batcher, Touch[] touches, float runTime) {
		if (freeze != 0 && create == -1)
			create = runTime;
		
		String modifiedContent = modify(content);
		if (!modifiedContent.equals(content)) {
			contentWidth = (int) AssetLoader.bgfont.getBounds(modifiedContent).width;
		}
		boolean beingTouched = false;
		for (Touch t : touches)
			if (!checkTouch(t).equals(""))
				beingTouched = true;
		locked = runTime-create < freeze;
		if (content.equals("%CONNECT%")) {
			String ip = Menu.mpIP;
			locked = ip.length() < 3 || !ip.contains("-") || ip.startsWith("-") || ip.endsWith("-");
		}
		if (beingTouched || locked) {
			batcher.draw(AssetLoader.p_buttonL, loc.x, loc.y, 0, 0, 6, 14, 3, 3, 0);
			batcher.draw(AssetLoader.p_buttonMid, loc.x + 18, loc.y, 0, 0, width/3-11, 14, 3, 3, 0);
			batcher.draw(AssetLoader.p_buttonR, loc.x + width - 18, loc.y, 0, 0, 6, 14, 3, 3, 0);
			if (content.equals("%MUSIC%")) {
				batcher.draw(AssetLoader.music, loc.x + width/2-6, loc.y+15, 0, 0, 19, 16, 1, 1, 0);
				if (!Options.getVolumes()[0])
					batcher.draw(AssetLoader.cancel, loc.x + width/2-6, loc.y+15, 0, 0, 19, 16, 1, 1, 0);
			} else if (content.equals("%SOUND%")) {
				batcher.draw(AssetLoader.sound, loc.x + width/2-6, loc.y+15, 0, 0, 19, 16, 1, 1, 0);
				if (!Options.getVolumes()[1])
					batcher.draw(AssetLoader.cancel, loc.x + width/2-6, loc.y+15, 0, 0, 19, 16, 1, 1, 0);
			} else if (content.equals("%LEADER%")) {
				batcher.draw(AssetLoader.hsIcon, loc.x + width/2-9, loc.y+15, 0, 0, 19, 14, 1, 1, 0);
			} else if (content.equals("%ACHIEVE%")) {
				batcher.draw(AssetLoader.achIcon, loc.x + width/2-8, loc.y+13, 0, 0, 18, 19, 1, 1, 0);
			} else
				AssetLoader.bgfont.draw(batcher, modifiedContent, loc.x + (width - contentWidth)/2, loc.y+10);
		} else {
			batcher.draw(AssetLoader.buttonL, loc.x, loc.y, 0, 0, 6, 14, 3, 3, 0);
			batcher.draw(AssetLoader.buttonMid, loc.x + 18, loc.y, 0, 0, width/3-11, 14, 3, 3, 0);
			batcher.draw(AssetLoader.buttonR, loc.x + width - 18, loc.y, 0, 0, 6, 14, 3, 3, 0);
			if (content.equals("%MUSIC%")) {
				batcher.draw(AssetLoader.music, loc.x + width/2-9, loc.y+12, 0, 0, 19, 16, 1, 1, 0);
				if (!Options.getVolumes()[0])
					batcher.draw(AssetLoader.cancel, loc.x + width/2-9, loc.y+12, 0, 0, 19, 16, 1, 1, 0);
			} else if (content.equals("%SOUND%")) {
				batcher.draw(AssetLoader.sound, loc.x + width/2-9, loc.y+12, 0, 0, 19, 16, 1, 1, 0);
				if (!Options.getVolumes()[1])
					batcher.draw(AssetLoader.cancel, loc.x + width/2-9, loc.y+12, 0, 0, 19, 16, 1, 1, 0);
			} else if (content.equals("%LEADER%")) {
				batcher.draw(AssetLoader.hsIcon, loc.x + width/2-12, loc.y+12, 0, 0, 19, 14, 1, 1, 0);
			} else if (content.equals("%ACHIEVE%")) {
				batcher.draw(AssetLoader.achIcon, loc.x + width/2-11, loc.y+10, 0, 0, 18, 19, 1, 1, 0);
			} else
				AssetLoader.bgfont.draw(batcher, modifiedContent, loc.x + (width - contentWidth)/2-3, loc.y+7);
		}
		return beingTouched;
	}
	
	public String checkTouch(Touch t) {
		if (locked)
			return "";
		if (t != null)
			if (t.y > loc.y && t.y < loc.y + 14*3)
				if (t.x > loc.x && t.x < loc.x+width)
					return content;
		return "";
	}
	
	public String modify(String content) {
		return content
				.replaceAll("%TILT%", Options.getTilt() == true ? "ON" : "OFF")
				.replaceAll("%GAMEMODE%", Options.getGameMode().name().charAt(0) + Options.getGameMode().name().substring(1).toLowerCase())
				.replaceAll("%CONNECT%", Menu.mpIP.length() == 0 ? "Connect..." : "Connect to " + Menu.mpIP);
	}
}
