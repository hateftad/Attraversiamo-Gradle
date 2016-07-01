package com.me.attraversiamo.ads;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.utils.Logger;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.apple.uikit.UIWindow;
import org.robovm.pods.google.mobileads.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hateftadayon on 12/27/15.
 */
public class AdManager {

    private static final Logger log = new Logger(AdManager.class.getName(), Application.LOG_DEBUG);
    private static final boolean USE_TEST_DEVICES = true;

    private GADBannerView adview;
    private GADInterstitial interstitial;
    private UIWindow window;
    private UIViewController rootViewController;

    public AdManager(IOSApplication application) {
        initializeAds(application);
        rootViewController = new UIViewController();
        window = new UIWindow(UIScreen.getMainScreen().getBounds());
        window.setRootViewController(rootViewController);
        window.addSubview(rootViewController.getView());
        initializeInterstitial();
    }

    private void initializeInterstitial() {

        NSDictionary infoDictionary = NSBundle.getMainBundle().getInfoDictionary();
        String interstitialId = infoDictionary.get(new NSString("AD_UNIT_ID_FOR_INTERSTITIAL_TEST")).toString();

        interstitial = new GADInterstitial(interstitialId);
        interstitial.setDelegate(new GADInterstitialDelegateAdapter() {
            @Override
            public void didReceiveAd (GADInterstitial ad) {
                System.out.println("Did receive ad.");
            }

            @Override
            public void didDismissScreen(GADInterstitial ad) {
                window.setHidden(true);
                initializeInterstitial();
            }

            @Override
            public void didFailToReceiveAd (GADInterstitial ad, GADRequestError error) {
                System.out.println(error.description());
                System.out.println(error.getErrorCode());
            }
        });

        interstitial.loadRequest(new GADRequest());

    }

    public void initializeAds(IOSApplication application) {
        //log.debug("Initalizing ads...");

        NSDictionary infoDictionary = NSBundle.getMainBundle().getInfoDictionary();
        String bannerId = infoDictionary.get(new NSString("AD_UNIT_ID_FOR_BANNER_TEST")).toString();

        UIViewController rootViewController = application.getUIViewController();
        adview = new GADBannerView(GADAdSize.SmartBannerLandscape());
        adview.setAdUnitID(bannerId); //put your secret key here
        adview.setRootViewController(rootViewController);
        rootViewController.getView().addSubview(adview);

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

    public void showAds(boolean show) {

        final CGSize screenSize = UIScreen.getMainScreen().getBounds().getSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();

        final CGSize adSize = adview.getBounds().getSize();
        double adWidth = adSize.getWidth();
        double adHeight = adSize.getHeight();

        log.debug(String.format("Hidding ad. size[%s, %s]", adWidth, adHeight));

        float bannerWidth = (float) screenWidth;
        float bannerHeight = (float) (bannerWidth / adWidth * adHeight);

        if (show) {
            adview.setFrame(new CGRect((screenWidth / 2) - adWidth / 2, screenHeight - adHeight, bannerWidth, bannerHeight));
        } else {
            adview.setFrame(new CGRect(0, -bannerHeight, bannerWidth, bannerHeight));
        }
    }

    public void showInterstitialAd(){
        log.debug("interstitial isReady ?"+ interstitial.isReady());
        if (interstitial.isReady()) {
            if (rootViewController == null) {
                rootViewController = new UIViewController();
            }
            if (window == null) {
                window = new UIWindow(UIScreen.getMainScreen().getBounds());
                window.setRootViewController(rootViewController);
            }
            window.makeKeyAndVisible();
            interstitial.present(rootViewController);
        } else {
            interstitial.loadRequest(new GADRequest());
        }
    }
}
