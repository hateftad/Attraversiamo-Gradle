package com.me.attraversiamo.android.ads;

import android.widget.RelativeLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.me.attraversiamo.android.R;

/**
 * Created by hateftadayon on 12/27/15.
 */
public class AdManager {

    private static AdHandler handler;

    public AdManager(RelativeLayout layout){
        setUpAd(layout);
    }

    private void setUpAd(RelativeLayout layout) {
        AdView adView = new AdView(layout.getContext());
        adView.setAdUnitId(layout.getContext().getString(R.string.ad_bottom_banner_menu));
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

    public void showAd(boolean show) {
        handler.sendMessage(show);
    }
}
