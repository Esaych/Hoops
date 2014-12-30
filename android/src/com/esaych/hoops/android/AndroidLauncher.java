package com.esaych.hoops.android;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.esaych.hoops.Hoops;
import com.esaych.hoops.R;
import com.esaych.hoops.app.GameServiceInterface;
import com.esaych.hoops.app.IActivityRequestHandler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus.PlusOptions;
import com.google.example.games.basegameutils.GameHelper;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AndroidLauncher extends AndroidApplication implements GameServiceInterface, GameHelper.GameHelperListener, IActivityRequestHandler
{
    protected AdView adView;

    private static float adAlpha = 0;

    protected GameHelper mHelper;
    final static int RC_UNUSED = 9002;

    private static boolean loggedIn = false;
    private static boolean loginFail = false;
    private static boolean waitingResponse = false;
    private static int timeSinceResponse = 0;

    @SuppressLint({ "InlinedApi", "NewApi" })
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new GameHelper(this, GameHelper.CLIENT_ALL);
	    /*
		//Set up the window
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
		newUiOptions = uiOptions;

		boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
		boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

		if (!hasMenuKey && !hasBackKey) {
			// Navigation bar hiding:  Backwards compatible to ICS.
			if (Build.VERSION.SDK_INT >= 14) {
				newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
			}

			// Status bar hiding: Backwards compatible to Jellybean
			if (Build.VERSION.SDK_INT >= 16) {
				newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
			}

			if (Build.VERSION.SDK_INT >= 18) {
				newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
			}
		} else {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		*/

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        setContentView(R.layout.main);

	    /*
		getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		setContentView(R.layout.main);
		*/

        mHelper.setPlusApiOptions(PlusOptions.builder().build());
        mHelper.setup(this);
        mHelper.setMaxAutoSignInAttempts(0);

        // Get the layout for the Game, has nothing but the game in it.
        LinearLayout layout = (LinearLayout) findViewById(R.id.gameLinLayout);

        // Create the libgdx View
        View gameView = initializeForView(new Hoops(this));

        // Add the libgdx view
        layout.addView(gameView);

        RelativeLayout adLayout = (RelativeLayout) findViewById(R.id.adLayout);

        //      Create and setup the AdMob view
        adView = new AdView(this); // Put in your secret key here
        adView.setAdUnitId("ca-app-pub-8334585094632512/3980558387");

        //      Approximate size of advertisement on screen
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float width = dpWidth/2;

        if (width > 728)
            adView.setAdSize(AdSize.LEADERBOARD);
        else if (width > 468)
            adView.setAdSize(AdSize.FULL_BANNER);
        else if (width > 320)
            adView.setAdSize(AdSize.BANNER);
        else
            adView.setAdSize(AdSize.SMART_BANNER);

        System.out.println("AdSize Chosen: " + adView.getAdSize().toString());

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        adView.loadAd(adRequest);

        RelativeLayout.LayoutParams adParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        // Add the AdMob view
        adLayout.addView(adView, adParams);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onPause() {
        adView.pause();
        super.onPause();
    }

    @SuppressLint("InlinedApi")
    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE );
    }

    @Override
    public void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }

    @Override
    public void showAds(float show) {
        Message msg = new Message(); Bundle bundle = new Bundle();
        bundle.putFloat("ShowAds", show); msg.setData(bundle);
        handler.sendMessage(msg);
    }

    @Override
    public void setLeaderHighscore(int highNum, String id) {
        Message msg = new Message(); Bundle bundle = new Bundle();
        bundle.putString("HighScore", highNum+":"+id); msg.setData(bundle);
        handler.sendMessage(msg);
    }

    @Override
    public void servicesLogin() {
        Message msg = new Message(); Bundle bundle = new Bundle();
        bundle.putInt("Login", 0); msg.setData(bundle);
        handler.sendMessage(msg);
    }

    @Override
    public void servicesLogout() {
        Message msg = new Message(); Bundle bundle = new Bundle();
        bundle.putInt("Logout", 0); msg.setData(bundle);
        handler.sendMessage(msg);
    }

    @Override
    public void setNewAchievement(String id) {
        Message msg = new Message(); Bundle bundle = new Bundle();
        bundle.putString("Achievement", id); msg.setData(bundle);
        handler.sendMessage(msg);
    }

    @Override
    public void openAchievements() {
        Message msg = new Message(); Bundle bundle = new Bundle();
        bundle.putInt("OpenAchievements", 0); msg.setData(bundle);
        handler.sendMessage(msg);
    }

    @Override
    public void openLeaderboards(String id) {
        Message msg = new Message(); Bundle bundle = new Bundle();
        bundle.putString("OpenLeaderboards", id); msg.setData(bundle);
        handler.sendMessage(msg);
    }

    @Override
    public boolean isLoggedIn() {
        Message msg = new Message(); Bundle bundle = new Bundle();
        bundle.putInt("CheckSignIn", 0); msg.setData(bundle);
        handler.sendMessage(msg);
        return loggedIn;
    }

    @Override
    public boolean isLoginFailure() {
        Message msg = new Message(); Bundle bundle = new Bundle();
        bundle.putInt("CheckSignIn", 0); msg.setData(bundle);
        handler.sendMessage(msg);
        if (loginFail) {
            loginFail = false;
            return true;
        }
        return loginFail;
    }

    @Override
    public int getScreenRotation() {
        return getWindowManager().getDefaultDisplay().getRotation();
    }

    @SuppressLint("HandlerLeak")
    protected Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (bundle.containsKey("HighScore")) {
                String highScore = bundle.getString("HighScore");
                submitScore(highScore);
            } else if (bundle.containsKey("Achievement")) {
                String achID = bundle.getString("Achievement");
                unlockAchievements(achID);
            } else if (bundle.containsKey("OpenLeaderboards")) {
                String leadID = bundle.getString("OpenLeaderboards");
                showScores(leadID);
            } else if (bundle.containsKey("OpenAchievements")) {
                showAchievements();
            } else if (bundle.containsKey("Login")) {
                if (!waitingResponse)
                    login();
                else
                    Toast.makeText(getApplicationContext(), "Login commencing, please wait...", Toast.LENGTH_LONG).show();
            } else if (bundle.containsKey("Logout")) {
                logOut();
            } else if (bundle.containsKey("CheckSignIn")) {
                loggedIn = isSignedIn();
            } else if (bundle.containsKey("ShowAds")) {
                float viewAlpha = bundle.getFloat("ShowAds");
                if (viewAlpha == 0) {
                    adView.setVisibility(View.GONE);
                    adAlpha = 0;
                } else
                    adView.setVisibility(View.VISIBLE);

                adAlpha += (viewAlpha-adAlpha)*.1;
                adView.setAlpha(adAlpha);
            }
        }
    };






    @Override
    public void showAchievements() {
        if (isSignedIn()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
            startActivityForResult(Games.Achievements.getAchievementsIntent(mHelper.getApiClient()), RC_UNUSED);
        } else {

        }
    }

    @Override
    public void showScores(String id) {
        if (isSignedIn()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mHelper.getApiClient(), id), RC_UNUSED);
        } else {

        }
    }

    @Override
    public boolean isSignedIn() {
        View decorView = getWindow().getDecorView();
        int decor = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (decorView.getSystemUiVisibility() != decor)
            decorView.setSystemUiVisibility(decor);
        if (waitingResponse)
            timeSinceResponse++;
        if (timeSinceResponse > 60) {
            Toast.makeText(getApplicationContext(), "An error occured, try again later...", Toast.LENGTH_SHORT).show();
            waitingResponse = false;
            timeSinceResponse = 0;
            loginFail = true;
        }
        return mHelper.isSignedIn();
    }

    @Override
    public void logOut() {
        timeSinceResponse = 0;
        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
        try {
            runOnUiThread(new Runnable() {
                // @Override
                public void run() {
                    mHelper.signOut();
                }
            });
        } catch (final Exception ex) {

        }
    }

    @Override
    public void login() {
        timeSinceResponse = 0;
        Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_LONG).show();
        try {
            runOnUiThread(new Runnable() {
                // @Override
                public void run() {
                    mHelper.beginUserInitiatedSignIn();
                }
            });
        } catch (final Exception ex) {

        }
        waitingResponse = true;
    }

    @Override
    public void submitScore(String score) {
        if (mHelper.isSignedIn()){
            Games.Leaderboards.submitScore(mHelper.getApiClient(), score.split(":")[1], Integer.parseInt(score.split(":")[0]));
        }
    }

    @Override
    public void onSignInFailed() {
        loggedIn = false;
        loginFail = true;
        System.out.println("Sign in failed");
        waitingResponse = false;
    }

    @Override
    public void onSignInSucceeded() {
        loggedIn = true;
        System.out.println("Sign in succeeded");
        waitingResponse = false;
    }

    @Override
    public void unlockAchievements(String achievementID) {
        if (mHelper.isSignedIn()){
            Games.Achievements.unlock(mHelper.getApiClient(), achievementID);
        }
        else {
        }
    }





    @Override
    protected void onStart() {
        super.onStart();
        mHelper.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHelper.onStop();
    }

    @Override
    protected void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
        mHelper.onActivityResult(request, response, data);
    }

}