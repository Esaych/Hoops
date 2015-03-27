package com.esaych.world.aspects;

import java.util.ArrayList;

import com.esaych.io.AssetLoader;
import com.esaych.io.menus.Options;
import com.esaych.objects.Hoop;
import com.esaych.objects.PowerUp;
import com.esaych.objects.hoop.PowerHoop;
import com.esaych.world.World;

public class HoopBlaster {

	private int blastAmount;
	private ArrayList<Hoop> hoopBlaster;
	private World world;
	private boolean blasting = false;
	
	public HoopBlaster(World world) {
		this.world = world;
		hoopBlaster = new ArrayList<Hoop>();
	}
	
	public void blastHoops() {
		blasting = true;
		blastAmount = 0;
	}
	
	public void update(float delta) {
    	for (Hoop hoop : hoopBlaster) {
    		hoop.update(delta);
    	}
		if (blasting)
			addBlastedHoops();
	}
	
	public void terminate() {
		hoopBlaster.clear();
		blasting = false;
	}
	
	public void removeHoop(Hoop hoop) {
		hoopBlaster.remove(hoop);
        if (blastAmount > 100 && hoopBlaster.size() == 0) {
            world.getGameScreen().addAchievement("CgkIkaqY2a8QEAIQBA");
            blastAmount = 0;
        }
	}

	public boolean isBlasting() {
		return blasting;
	}

    public void stopBlasting() {
        blasting = false;
    }

    public void resume() {
        blasting = true;
    }
	
	public ArrayList<Hoop> getHoopsList() {
		return hoopBlaster;
	}
	
	public void addBlastedHoops() {
		if (blastAmount >= world.getHighScore() - world.getScore()) {
			blasting = false;
			world.getBroadcast().bc(world.getHighScore() - world.getScore() + " Hoops Blasted!", 5);
		} else {
			hoopBlaster.add(new Hoop(world));
			blastAmount++;
			if (Options.getVolumes()[1])
				AssetLoader.newhoop.play();
		}
	}
}
