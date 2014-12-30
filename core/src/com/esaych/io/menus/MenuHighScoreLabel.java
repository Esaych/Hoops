package com.esaych.io.menus;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esaych.io.AssetLoader;

public class MenuHighScoreLabel extends MenuLabel {
	public MenuHighScoreLabel(int x, int y, int width, int content) {
		super(x, y, width, (content == 0? "--" : content+""));
	}
	
	public void render(SpriteBatch batcher) {
		batcher.draw(AssetLoader.hslabelL, loc.x, loc.y, 0, 0, 6, 14, 2, 2, 0);
		batcher.draw(AssetLoader.hslabelMid, loc.x + 12, loc.y, 0, 0, width/2-11, 14, 2, 2, 0);
		batcher.draw(AssetLoader.hslabelR, loc.x + width - 12, loc.y, 0, 0, 6, 14, 2, 2, 0);
		AssetLoader.font.draw(batcher, content, loc.x + (width - contentWidth)/2, loc.y+1);
	}
}
