package com.esaych.io.menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esaych.io.AssetLoader;
import com.esaych.world.World;

/**
 * Created by Samuel Holmberg on 3/26/2015.
 * For the button clicking transition
 */
public class MenuTransition {

    private static Menu.MenuType transitionToType;
    private static float timeAlive = 0;
    private static boolean transitioning = false;
    private static boolean typeSet = false;

    public static void transitionTo(Menu.MenuType type) {
        transitionToType = type;
        transitioning = true;
        typeSet = false;
        timeAlive = 0;
    }

    public void update(float delta, World world) {
        System.out.println("Time Alive: " + timeAlive);
        if (transitioning)
            timeAlive+=delta;
        if (timeAlive > .5) {
            transitioning = false;
        }

        if (timeAlive > .25 && !typeSet) {
            world.getMenu().allowTypeChange(true);
            world.getMenu().setType(transitionToType);
            typeSet = true;
        }
    }

    public void render(SpriteBatch batcher, int bgOffset, int bgWdth, World world) {
        if (!transitioning)
            return;

        //max opacity at .25s, min at 0s and .5s

        float opacity = 1-Math.abs(.25f-timeAlive)*4f;

        Color c = batcher.getColor();
        batcher.setColor(c.r, c.g, c.b, opacity);
        batcher.draw(AssetLoader.bg, 0 - bgOffset - (world.getWidth(.5)-world.getBall().getPos().x)/80, 0, 0, 0, bgWdth, world.getHeight(), 1, 1, 0);
        batcher.draw(AssetLoader.bgcity, 0 - bgOffset, 0, 0, 0, bgWdth, world.getHeight(), 1, 1, 0);
        batcher.setColor(c.r, c.g, c.b, 1f);
    }

}
