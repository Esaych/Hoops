package com.esaych.hoops.app;

public interface IActivityRequestHandler {
	   public void showAds(float show);
	   public void setLeaderHighscore(int highNum, String id);
	   public void setNewAchievement(String id);
	   public void servicesLogin();
	   public void servicesLogout();
	   public void openAchievements();
	   public void openLeaderboards(String id);
	   public boolean isLoggedIn();
	   public boolean isLoginFailure();
	   public int getScreenRotation();
	}