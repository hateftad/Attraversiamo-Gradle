package com.me.attraversiamo.android.ads;

import com.google.android.gms.ads.*;

/**
 * Created by hateftadayon on 12/26/15.
 */
public class AdOperationListener extends AdListener {

    public AdOperationListener() {
    }

    public void onAdClosed() {
        System.out.println("onAdClosed");
    }

    public void onAdFailedToLoad(int errorCode) {
        System.out.println("onAdFailedToLoad(int " + errorCode + ")");
    }

    public void onAdLeftApplication() {
        System.out.println("onAdLeftApplication");
    }

    public void onAdOpened() {
        System.out.println("onAdOpened");
    }

    public void onAdLoaded() {
        System.out.println("onAdLoaded");
    }

}
