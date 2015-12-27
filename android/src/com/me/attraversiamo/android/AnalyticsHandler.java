package com.me.attraversiamo.android;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by hateftadayon on 12/27/15.
 */
public class AnalyticsHandler {
    Tracker m_tracker;

    public AnalyticsHandler(Tracker tracker){
        m_tracker = tracker;
    }

    public void setScreenName(String screenName){
        m_tracker.setScreenName(screenName);
        m_tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
