package com.me.attraversiamo.android.analytics;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by hateftadayon on 12/27/15.
 */
public class AnalyticsManager {
    Tracker m_tracker;

    public AnalyticsManager(Tracker tracker){
        m_tracker = tracker;
    }

    public void setScreenName(String screenName){
        m_tracker.setScreenName(screenName);
        m_tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void trackEvent(String category, String action, String label, long value) {
        m_tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(value)
                .build());
    }
}
