package com.me.attraversiamo.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.me.ads.IActivityRequestHandler;
import com.me.attraversiamo.Attraversiamo;
import com.me.attraversiamo.android.ads.AdManager;
import com.me.attraversiamo.android.analytics.AnalyticsManager;
import com.me.attraversiamo.android.gpg.PlayServicesManager;
import com.me.config.GameConfig;
import com.me.config.GameConfig.Platform;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler{

    private AnalyticsManager analyticsHandler;
    private AdManager adManager;
    private RelativeLayout rootLayout;
    private PlayServicesManager playServicesManager;

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
        playServicesManager = new PlayServicesManager(this);
        Attraversiamo attraversiamo = new Attraversiamo(cfg, this);
        attraversiamo.setPlayServices(playServicesManager);
        View gameView = initializeForView(attraversiamo, config);
        rootLayout.addView(gameView);

        adManager = new AdManager(rootLayout);

        AttraversiamoApplication application = (AttraversiamoApplication) getApplication();
        analyticsHandler = new AnalyticsManager(application.getTracker(AttraversiamoApplication.TrackerName.APP_TRACKER));

        setContentView(rootLayout);
        adManager = new AdManager(rootLayout);
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        playServicesManager.onStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
        playServicesManager.onStop();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        playServicesManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showBannerAd(boolean show) {
        adManager.showBannerAd(show);
    }

    @Override
    public void showInterstitialAd() {
        adManager.showInterstitialAd(this);
    }

    @Override
    public void setScreenName(String name) {
        analyticsHandler.setScreenName(name);
    }


}
