package com.me.attraversiamo.android;

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
import com.me.config.GameConfig;
import com.me.config.GameConfig.Platform;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler {

    private AnalyticsManager analyticsHandler;
    private AdManager adManager;
    private RelativeLayout rootLayout;

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

        View gameView = initializeForView(new Attraversiamo(cfg, this), config);
        rootLayout.addView(gameView);

        adManager = new AdManager(rootLayout);

        AttraversiamoApplication application = (AttraversiamoApplication) getApplication();
        analyticsHandler = new AnalyticsManager(application.getTracker(AttraversiamoApplication.TrackerName.APP_TRACKER));

        setContentView(rootLayout);
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public void showAds(boolean show) {
        if(adManager == null){
            adManager = new AdManager(rootLayout);
        }
        adManager.showAd(show);
    }

    @Override
    public void setScreenName(String name) {
        analyticsHandler.setScreenName(name);
    }
}
