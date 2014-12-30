package com.esaych.hoops;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.esaych.hoops.Hoops;
import com.esaych.hoops.app.IActivityRequestHandler;

public class IOSLauncher extends IOSApplication.Delegate implements IActivityRequestHandler {

    public static IOSLauncher application;

    @Override
    protected IOSApplication createApplication() {
        if (application == null)
            application = new IOSLauncher();
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new Hoops(application), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
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