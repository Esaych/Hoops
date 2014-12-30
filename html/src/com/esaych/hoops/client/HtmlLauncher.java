package com.esaych.hoops.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.esaych.hoops.Hoops;
import com.esaych.hoops.app.IActivityRequestHandler;

public class HtmlLauncher extends GwtApplication implements IActivityRequestHandler {

    public static HtmlLauncher application;

    @Override
    public GwtApplicationConfiguration getConfig () {
        return new GwtApplicationConfiguration(480, 320);
    }

    @Override
    public ApplicationListener getApplicationListener () {
        if (application == null)
            application = new HtmlLauncher();
        return new Hoops(application);
    }

    @Override
    public void showAds(float show) {
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
    public void servicesLogout() {
        // TODO Auto-generated method stub

    }

    @Override
    public void openAchievements() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isLoggedIn() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isLoginFailure() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getScreenRotation() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setLeaderHighscore(int highNum, String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void openLeaderboards(String id) {
        // TODO Auto-generated method stub

    }
}