package com.me.attraversiamo.analytics;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.utils.Logger;
import org.robovm.apple.foundation.NSNumber;
import org.robovm.pods.google.analytics.GAI;
import org.robovm.pods.google.analytics.GAIDictionaryBuilder;
import org.robovm.pods.google.analytics.GAILogLevel;
import org.robovm.pods.google.analytics.GAITracker;

/**
 * Created by hateftadayon on 12/27/15.
 */
public class AnalyticsHandler {

    private String id;
    private static final Logger log = new Logger(AnalyticsHandler.class.getName(), Application.LOG_DEBUG);

    private GAITracker tracker;

    public AnalyticsHandler(String id) {
        log.debug("Tracking ID is "+ id);
        this.id = id;
        startSession();
    }

    public void startSession() {
        GAI gai = GAI.getSharedInstance();
        gai.setDryRun(false);
        gai.setTracksUncaughtExceptions(true);
        gai.getLogger().setLogLevel(GAILogLevel.Verbose);
        gai.setDispatchInterval(20.0);
        tracker = gai.getTracker(id);
    }

    public void trackEvent(String category, String action, String label, long value) {
        tracker.send(GAIDictionaryBuilder.createEvent(category, action, label, NSNumber.valueOf(value)).build());
    }

    public void trackPageView(String name) {
        tracker.put("kGAIScreenName", name);
        tracker.send(GAIDictionaryBuilder.createScreenView().build());
    }

    public void trackTiming(String category, long timeInMilliseconds, String name, String label) {
        tracker.send(GAIDictionaryBuilder.createTiming(category, NSNumber.valueOf(timeInMilliseconds), name, label).build());
    }

    public void trackException(String description, Throwable t, boolean fatal) {
        tracker.send(GAIDictionaryBuilder.createException(description, NSNumber.valueOf(fatal)).build());
    }

    public void dispatch() {
        //System.out.println("dispatching");
        GAI.getSharedInstance().dispatch();
    }
}
