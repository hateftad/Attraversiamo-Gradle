package com.me.attraversiamo;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.utils.Logger;
import com.me.ads.IActivityRequestHandler;
import com.me.config.GameConfig;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.pods.google.mobileads.GADAdSize;
import org.robovm.pods.google.mobileads.GADBannerView;
import org.robovm.pods.google.mobileads.GADBannerViewDelegateAdapter;
import org.robovm.pods.google.mobileads.GADRequest;
import org.robovm.pods.google.mobileads.GADRequestError;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import org.robovm.apple.uikit.UIScreen;

import java.util.ArrayList;
import java.util.List;

public class IOSLauncher extends IOSApplication.Delegate implements IActivityRequestHandler {

    private static final Logger log = new Logger(IOSLauncher.class.getName(), Application.LOG_DEBUG);
    private static final boolean USE_TEST_DEVICES = true;
    private GADBannerView adview;
    private boolean adsInitialized = false;
    private IOSApplication iosApplication;
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

    public void initializeAds() {
        if (!adsInitialized) {
            log.debug("Initalizing ads...");

            adsInitialized = true;

            adview = new GADBannerView(GADAdSize.SmartBannerLandscape());
            adview.setAdUnitID("ca-app-pub-8364054019750662/3829375835"); //put your secret key here
            adview.setRootViewController(iosApplication.getUIViewController());
            iosApplication.getUIViewController().getView().addSubview(adview);

            final GADRequest request = new GADRequest();
            if (USE_TEST_DEVICES) {
                final List<String> testDevices = new ArrayList<>();
                testDevices.add("21fcf2728dddd9a7e4ca8049b1ed5a76");
                request.setTestDevices(testDevices);
                log.debug("Test devices: " + request.getTestDevices());
            }

            adview.setDelegate(new GADBannerViewDelegateAdapter() {
                @Override
                public void didReceiveAd(GADBannerView view) {
                    super.didReceiveAd(view);
                    log.debug("didReceiveAd");
                }

                @Override
                public void didFailToReceiveAd(GADBannerView view,
                                               GADRequestError error) {
                    super.didFailToReceiveAd(view, error);
                    log.debug("didFailToReceiveAd:" + error);
                }
            });

            adview.loadRequest(request);

            log.debug("Initalizing ads complete.");
        }
    }

    @Override
    public void showAds(boolean show) {
        initializeAds();

        final CGSize screenSize = UIScreen.getMainScreen().getBounds().getSize();
        double screenWidth = screenSize.getWidth();

        final CGSize adSize = adview.getBounds().getSize();
        double adWidth = adSize.getWidth();
        double adHeight = adSize.getHeight();

        log.debug(String.format("Hidding ad. size[%s, %s]", adWidth, adHeight));

        float bannerWidth = (float) screenWidth;
        float bannerHeight = (float) (bannerWidth / adWidth * adHeight);

        if(show) {
            adview.setFrame(new CGRect((screenWidth / 2) - adWidth / 2, 0, bannerWidth, bannerHeight));
        } else {
            adview.setFrame(new CGRect(0, -bannerHeight, bannerWidth, bannerHeight));
        }
    }

}