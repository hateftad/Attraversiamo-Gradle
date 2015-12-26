package com.me.attraversiamo.android.ads;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.google.android.gms.ads.AdView;

import java.lang.ref.WeakReference;

/**
 * Created by hateftadayon on 12/26/15.
 */
public class AdHandler extends Handler {

    private WeakReference<AdView> weakReference;
    private static final int SHOW_ADS = 1;
    private static final int HIDE_ADS = 0;

    public AdHandler(AdView view) {
        weakReference = new WeakReference<AdView>(view);
    }

    @Override
    public void handleMessage(Message msg) {
        if (weakReference.get() == null) {
            return;
        }
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
    }

    public void sendMessage(boolean show){
        sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
    }
}
