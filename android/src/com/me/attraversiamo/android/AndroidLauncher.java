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
import com.me.attraversiamo.android.ads.AdHandler;
import com.me.attraversiamo.android.ads.AdOperationListener;
import com.me.config.GameConfig;
import com.me.config.GameConfig.Platform;
import com.google.android.gms.ads.*;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler {

    private static AdHandler handler;
    private AnalyticsHandler analyticsHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        GameConfig cfg = new GameConfig();
        cfg.platform = Platform.ANDROID;
        cfg.timeStep = 1 / 60f;
        cfg.showUI = true;
        cfg.zoom = 9f;

        RelativeLayout layout = new RelativeLayout(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        View gameView = initializeForView(new Attraversiamo(cfg, this), config);
        layout.addView(gameView);

        setUpAd(layout);

        AttraversiamoApplication application = (AttraversiamoApplication) getApplication();
        analyticsHandler = new AnalyticsHandler(application.getTracker(AttraversiamoApplication.TrackerName.APP_TRACKER));

        setContentView(layout);
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


    private void setUpAd(RelativeLayout layout) {


        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.ad_bottom_banner_menu));
        adView.setAdSize(AdSize.SMART_BANNER);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("BA5C2F7E987BAD8BD945FD845BDF5D2F")
                .build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdOperationListener());

        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        layout.addView(adView, adParams);

        handler = new AdHandler(adView);
    }

    @Override
    public void showAds(boolean show) {
        handler.sendMessage(show);
    }

    @Override
    public void setScreenName(String name) {
        analyticsHandler.setScreenName(name);
    }
}
