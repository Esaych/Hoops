package com.esaych.io.menus;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.esaych.io.AssetLoader;
import com.esaych.world.aspects.HighScores;

public class MenuLabel {
	
	protected Vector2 loc;
	protected int width;
	protected String content;
	
	protected int contentWidth;
	
	public MenuLabel(int x, int y, int width, String content) {
		loc = new Vector2(x-width/2, y);
		this.width = width;
		this.content = content;
		contentWidth = (int) AssetLoader.font.getBounds(content).width;
	}

    public void update(float delta){};

	public void render(SpriteBatch batcher) {
		String modifiedContent = modify(content);
		contentWidth = (int) AssetLoader.font.getBounds(modifiedContent).width;
		batcher.draw(AssetLoader.labelL, loc.x, loc.y, 0, 0, 6, 14, 2, 2, 0);
		batcher.draw(AssetLoader.labelMid, loc.x + 12, loc.y, 0, 0, width/2-11, 14, 2, 2, 0);
		batcher.draw(AssetLoader.labelR, loc.x + width - 12, loc.y, 0, 0, 6, 14, 2, 2, 0);
		AssetLoader.font.draw(batcher, modifiedContent, loc.x + (width - contentWidth)/2, loc.y+1);
	}
	
	public String modify(String content) {
		return content.replaceAll("%HIGHSCORE%", HighScores.getHighScore(Options.getGameMode()) + "")
                .replaceAll("%GAMEMODE%", Options.getGameMode().name().charAt(0) + Options.getGameMode().name().substring(1).toLowerCase());
	}
}
