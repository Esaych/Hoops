package com.esaych.world.aspects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.esaych.io.AssetLoader;
import com.esaych.world.World;

public class LifeBar {

    private int max;
    private int amount;
    private World world;
    private boolean saidSpawn;

    public LifeBar(World world) {
        setMax(10);
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean shouldSpawnLife() {
        if (saidSpawn == false && amount == max) {
            saidSpawn = true;
            return true;
        } else
            return false;
    }

    public void givePenalty(int score) {
        int x = score / 10;
        if (x > 5)
            x = 5;
        inc(-x);
    }

    public void inc(int num) {
        this.amount += num;
        if (amount < 0)
            reset();
        if (amount > max)
            amount = max;
    }

    public void reset() {
        amount = 0;
        saidSpawn = false;
    }

    public void update(float delta) {
        this.delta = delta;
    }

    private float delta = 0;
    private float tempAmount = 0;

    public void render(SpriteBatch batcher, ShapeRenderer shapeRenderer) {
        batcher.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);

        tempAmount += (amount-tempAmount)*(Math.abs(5-tempAmount)+3)/2*delta;

        shapeRenderer.rect(3, 47+(10-tempAmount)*25, 3, tempAmount*25);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.line(1, 52, 1, 297);
        shapeRenderer.line(7, 52, 7, 297);
        shapeRenderer.end();
        batcher.begin();
        batcher.draw(AssetLoader.heart, 0, 45, 9, 7);
        batcher.draw(AssetLoader.heartHoop, 0, 44+(10-tempAmount)*25, 9, 7);
    }
}
