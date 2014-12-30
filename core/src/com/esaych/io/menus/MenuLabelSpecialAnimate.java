package com.esaych.io.menus;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esaych.io.AssetLoader;

public class MenuLabelSpecialAnimate extends MenuLabel {

    private float life = 0;
    private MenuAnimation anim;
    private String oldText = "";
    private int oldContentWidth;
    private float oldX;

    public MenuLabelSpecialAnimate(int x, int y, int width, String content, MenuAnimation animation) {
        super(x, y, width, content);
        anim = animation;
    }

    public enum MenuAnimation {
        LRSlide
    }

    public void update(float delta) {
        life += delta;
    }

    public void animate(String transFromText) {
        life = 0;
        oldText = modify(transFromText);
        oldContentWidth = (int) AssetLoader.font.getBounds(oldText).width;
        oldX = loc.x + (width - oldContentWidth)/2;
    }

    @Override
    public void render(SpriteBatch batcher) {
        String modifiedContent = modify(content);
        contentWidth = (int) AssetLoader.font.getBounds(modifiedContent).width;
        batcher.draw(AssetLoader.labelL, loc.x, loc.y, 0, 0, 6, 14, 2, 2, 0);
        batcher.draw(AssetLoader.labelMid, loc.x + 12, loc.y, 0, 0, width/2-11, 14, 2, 2, 0);
        batcher.draw(AssetLoader.labelR, loc.x + width - 12, loc.y, 0, 0, 6, 14, 2, 2, 0);
        renderText(batcher, modifiedContent, loc.x + (width - contentWidth)/2, loc.y+1);
    }

    public void renderText(SpriteBatch batcher, String newText, float x, float y) {

        if (oldText.equals(newText)) {
            AssetLoader.font.draw(batcher, newText, x, y);
            return;
        }

        AssetLoader.font.draw(batcher, oldText, (int) (oldX + life*life*600), y);

        int newX = (int) ((x - width/2 - contentWidth) + life*1000);
        if (newX > x || oldText == "")
            AssetLoader.font.draw(batcher, newText, x, y);
        else
            AssetLoader.font.draw(batcher, newText, newX, y);
    }

}
