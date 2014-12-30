package com.esaych.hoops.app;

public interface GameServiceInterface {
	 
    public void login();
    public void logOut();

    public boolean isSignedIn();

    public void submitScore(String score);
    public void unlockAchievements(String achievementID);

    public void showScores(String id);
    public void showAchievements();
}