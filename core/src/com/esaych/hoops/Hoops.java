package com.esaych.hoops;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.esaych.hoops.app.IActivityRequestHandler;
import com.esaych.hoops.screen.GameScreen;
import com.esaych.io.AssetLoader;

public class Hoops extends Game implements ApplicationListener {

    private IActivityRequestHandler reqHandler;

    public Hoops(IActivityRequestHandler handler) {
        reqHandler = handler;
    }

    @Override
    public void create() {
        AssetLoader.load();
        setScreen(new GameScreen(reqHandler));
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }
}
