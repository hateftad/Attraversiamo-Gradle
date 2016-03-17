package com.me.attraversiamo.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.me.ads.IActivityRequestHandler;
import com.me.ads.PlayServices;
import com.me.attraversiamo.Attraversiamo;
import com.me.attraversiamo.android.ads.AdManager;
import com.me.attraversiamo.android.analytics.AnalyticsManager;
import com.me.config.GameConfig;
import com.me.config.GameConfig.Platform;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler, PlayServices {

    private AnalyticsManager analyticsHandler;
    private AdManager adManager;
    private RelativeLayout rootLayout;
    private GameHelper gameHelper;
    private final static int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;

        GameConfig cfg = new GameConfig();
        cfg.platform = Platform.ANDROID;
        cfg.timeStep = 1 / 60f;
        cfg.showUI = true;
        cfg.zoom = 9f;

        rootLayout = new RelativeLayout(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        Attraversiamo attraversiamo = new Attraversiamo(cfg, this);
        attraversiamo.setPlayServices(this);
        View gameView = initializeForView(attraversiamo, config);
        rootLayout.addView(gameView);

        adManager = new AdManager(rootLayout);

        AttraversiamoApplication application = (AttraversiamoApplication) getApplication();
        analyticsHandler = new AnalyticsManager(application.getTracker(AttraversiamoApplication.TrackerName.APP_TRACKER));

        setContentView(rootLayout);

        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(false);

        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
            @Override
            public void onSignInFailed() {
            }

            @Override
            public void onSignInSucceeded() {

            }
        };

        gameHelper.setup(gameHelperListener);

    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        gameHelper.onStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
        gameHelper.onStop();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gameHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showAds(boolean show) {
        if (adManager == null) {
            adManager = new AdManager(rootLayout);
        }
        adManager.showAd(show);
    }

    @Override
    public void setScreenName(String name) {
        analyticsHandler.setScreenName(name);
    }

    @Override
    public void signIn() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void signOut() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.signOut();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void rateGame() {
        String str = "Your PlayStore Link";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    }

    @Override
    public void unlockAchievement() {
        Games.Achievements.unlock(gameHelper.getApiClient(),
                getString(R.string.achievement_first));
    }

    @Override
    public void submitScore(int highScore) {
        if (isSignedIn()) {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(),
                    getString(R.string.leaderboard_fastest), highScore);
        }
    }

    @Override
    public void showAchievement() {
        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), requestCode);
        } else {
            signIn();
        }
    }

    @Override
    public void showScore() {
        if (isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
                    getString(R.string.leaderboard_fastest)), requestCode);
        } else {
            signIn();
        }
    }

    @Override
    public boolean isSignedIn() {
        return gameHelper.isSignedIn();
    }
}
