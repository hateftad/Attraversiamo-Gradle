package com.me.attraversiamo;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.me.ads.IActivityRequestHandler;
import com.me.attraversiamo.ads.AdManager;
import com.me.attraversiamo.analytics.AnalyticsManager;
import com.me.attraversiamo.gpg.PlayServicesManager;
import com.me.config.GameConfig;
import org.robovm.apple.foundation.*;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.pods.google.GGLContext;
import org.robovm.pods.google.games.GPGManager;
import org.robovm.pods.google.signin.GIDSignIn;

public class IOSLauncher extends IOSApplication.Delegate implements IActivityRequestHandler {


    private IOSApplication iosApplication;
    private AnalyticsManager analyticsHandler;
    private AdManager adHandler;
    private PlayServicesManager playServicesManager;

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        GameConfig cfg = new GameConfig();
        cfg.platform = GameConfig.Platform.IPHONE;
        cfg.showUI = true;
        cfg.timeStep = 1 / 65f;
        cfg.zoom = 5f;
        playServicesManager = new PlayServicesManager();
        Attraversiamo attraversiamo = new Attraversiamo(cfg, this);
        attraversiamo.setPlayServices(playServicesManager);
        iosApplication = new IOSApplication(attraversiamo, config);
        return iosApplication;
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    public void showAds(boolean show) {
        if (adHandler == null) {
            adHandler = new AdManager(iosApplication);
        }
        adHandler.showAds(show);
    }

    @Override
    public void setScreenName(String name) {
        if (analyticsHandler == null) {
            initializeAnalytics();
        }
        analyticsHandler.trackPageView(name);
        analyticsHandler.trackEvent("Hello", "event", "start", 0);
    }

    private void initializeAnalytics() {
        NSDictionary infoDictionary = NSBundle.getMainBundle().getInfoDictionary();
        String trackingId = infoDictionary.get(new NSString("TRACKING_ID")).toString();
        analyticsHandler = new AnalyticsManager(trackingId);
    }

    @Override
    public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        super.didFinishLaunching(application, launchOptions);
        Foundation.log("IOSLauncher didFinishLaunching()");
        try {
            GGLContext.getSharedInstance().configure();
            GIDSignIn.getSharedInstance().setAllowsSignInWithWebView(true);
            GPGManager.getSharedInstance().signIn(PlayServicesManager.CLIENT_ID, true);
        } catch (NSErrorException e) {
            Foundation.log("Error configuring the Google context: " + e);
        }
        return true;
    }

}