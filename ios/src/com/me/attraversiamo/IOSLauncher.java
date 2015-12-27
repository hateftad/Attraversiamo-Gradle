package com.me.attraversiamo;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.me.ads.IActivityRequestHandler;
import com.me.attraversiamo.ads.AdHandler;
import com.me.attraversiamo.analytics.AnalyticsHandler;
import com.me.config.GameConfig;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.uikit.UIApplication;

public class IOSLauncher extends IOSApplication.Delegate implements IActivityRequestHandler {


    private IOSApplication iosApplication;
    private AnalyticsHandler analyticsHandler;
    private AdHandler adHandler;

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        GameConfig cfg = new GameConfig();
        cfg.platform = GameConfig.Platform.IPHONE;
        cfg.showUI = true;
        cfg.timeStep = 1/45f;
        cfg.zoom = 5f;
        iosApplication = new IOSApplication(new Attraversiamo(cfg, this), config);
        return iosApplication;
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    public void showAds(boolean show) {
        if(adHandler == null){
            adHandler = new AdHandler(iosApplication);
        }
        adHandler.showAds(show);
    }

    @Override
    public void setScreenName(String name) {
        if(analyticsHandler == null){
            initializeAnalytics();
        }
        analyticsHandler.trackPageView(name);
    }

    private void initializeAnalytics(){
        NSDictionary infoDictionary = NSBundle.getMainBundle().getInfoDictionary();
        String trackingId = infoDictionary.get(new NSString("TRACKING_ID")).toString();
        analyticsHandler = new AnalyticsHandler(trackingId);
    }


}