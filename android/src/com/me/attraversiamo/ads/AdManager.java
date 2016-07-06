package com.me.attraversiamo.ads;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.ads.*;
import com.me.attraversiamo.AttraversiamoApplication;
import com.me.attraversiamo.BuildConfig;
import com.me.attraversiamo.R;

/**
 * Created by hateftadayon on 12/27/15.
 */
public class AdManager {

    private static AdHandler handler;
    public static final int BANNER = 3;
    private InterstitialAd interstitialAd;

    public AdManager(RelativeLayout layout){
        setUpAd(layout);
        setUpInterstitital(layout.getContext());
    }

    private void setUpAd(RelativeLayout layout) {
        AdView adView = new AdView(layout.getContext());
        adView.setAdUnitId(layout.getContext().getString(BuildConfig.DEBUG ? R.string.test_banner_ad_unit_id : R.string.ad_bottom_banner_menu));
        adView.setAdSize(AdSize.SMART_BANNER);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("9DA44459A535B273857088410DB94505")
                .build();
        adView.loadAd(adRequest);
        adView.setVisibility(View.GONE);
        adView.setAdListener(new AdOperationListener());

        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        layout.addView(adView, adParams);

        handler = new AdHandler(adView);
    }

    private void setUpInterstitital(Context context){
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("9DA44459A535B273857088410DB94505")
                .build();

        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(context.getString(BuildConfig.DEBUG ? R.string.test_interstitial_ad_unit_id : R.string.between_level_interstitial));
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Toast.makeText(AttraversiamoApplication.getContext(), "Ad Closed", Toast.LENGTH_SHORT).show();
//                requestNewInterstitial();
//                beginPlayingGame();
            }

            @Override
            public void onAdLoaded() {
                Toast.makeText(AttraversiamoApplication.getContext(), "onAdLoaded", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showBannerAd(boolean show) {
        handler.sendMessage(AdManager.BANNER, show);
    }

    public void showInterstitialAd(Activity activity) {
        try {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                        Toast.makeText(AttraversiamoApplication.getContext(), "Showing Interstitial", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        AdRequest interstitialRequest = new AdRequest.Builder().build();
                        interstitialAd.loadAd(interstitialRequest);
                        Toast.makeText(AttraversiamoApplication.getContext(), "Loading Interstitial", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ignored) {
        }
    }
}
