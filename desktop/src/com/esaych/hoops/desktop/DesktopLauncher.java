package com.esaych.hoops.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.esaych.hoops.Hoops;
import com.esaych.hoops.app.IActivityRequestHandler;

public class DesktopLauncher implements IActivityRequestHandler {
    public static DesktopLauncher application;

    public static void main(String[] args) {
        if (application == null) {
            application = new DesktopLauncher();
        }
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Hoops";
        int rand = (int) ((Math.random() * (1080 - 800)) + 800);
        cfg.width = rand;
        cfg.height = 500;
//		cfg.width = 500;
//		cfg.height = 300;
//		cfg.width = 200;
//		cfg.height = 100;

        cfg.x = 0;
        cfg.y = 10;
        new LwjglApplication(new Hoops(application), cfg);
//		} else {
//			cfg.x = 0;
//			cfg.y = 0;
//			new LwjglApplication(new Hoops(application), cfg);
//		}
    }

    @Override
    public void showAds(float show) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setLeaderHighscore(int highNum, String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setNewAchievement(String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void servicesLogin() {
        // TODO Auto-generated method stub

    }

    @Override
    public void openAchievements() {
        // TODO Auto-generated method stub

    }

    @Override
    public void openLeaderboards(String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isLoggedIn() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void servicesLogout() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isLoginFailure() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getScreenRotation() {
        return 0;
    }
}
