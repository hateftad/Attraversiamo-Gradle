package com.me.ads;

/**
 * Created by hateftadayon on 12/26/15.
 */
public interface IActivityRequestHandler {
    void showBannerAd(boolean show);
    void showInterstitialAd();
    void setScreenName(String name);
}
