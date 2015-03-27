package com.esaych.world.aspects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.esaych.io.AssetLoader;
import com.esaych.world.World;

public class LifeBar {

    private int max;
    private float amount;
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
        float x = score / 10f;
        inc(-x);
    }

    public void inc(float num) {
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

    public void render(SpriteBatch batcher, ShapeRenderer shapeRenderer, World world) {
        batcher.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(new Color(154/255f, 177/255f, 211/255f, 1));
//        shapeRenderer.line(3, 52, 3, 297);
//        shapeRenderer.line(6, 52, 6, 297);

        shapeRenderer.setColor(Color.RED);

        tempAmount += (amount-tempAmount)*(Math.abs(5-tempAmount)+3)/2*delta;

//        shapeRenderer.rect(3, 47 + (10 - tempAmount) * 25, 3, tempAmount * 25);
        shapeRenderer.rect(world.getWidth(.5), world.getHeight()-4, (tempAmount/20) * world.getWidth(), 3);
        shapeRenderer.end();
        batcher.begin();
//        batcher.draw(AssetLoader.heart, 0, 45, 9, 7); previous -- uglier design
//        batcher.draw(AssetLoader.heartHoop, 0, 44+(10-tempAmount)*25, 9, 7);
    }
}
