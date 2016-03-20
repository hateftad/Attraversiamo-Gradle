package com.me.attraversiamo.android.ads;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.lang.ref.WeakReference;

/**
 * Created by hateftadayon on 12/26/15.
 */
public class AdHandler extends Handler {

    private WeakReference<AdView> weakReference;
    private static final int SHOW_ADS = 1;
    private static final int HIDE_ADS = 0;

    public AdHandler(AdView view) {
        weakReference = new WeakReference<>(view);
    }

    @Override
    public void handleMessage(Message msg) {
        if (weakReference.get() == null) {
            return;
        }
        switch (msg.arg1) {
            case AdManager.BANNER:
                switch (msg.what) {
                    case SHOW_ADS: {
                        weakReference.get().setVisibility(View.VISIBLE);
                        break;
                    }
                    case HIDE_ADS: {
                        weakReference.get().setVisibility(View.GONE);
                        break;
                    }
                }
                break;

        }

    }

    public void sendMessage(int type, boolean show) {
        Message message = new Message();
        message.arg1 = type;
        message.what = show ? SHOW_ADS : HIDE_ADS;
        sendMessage(message);
    }
}
