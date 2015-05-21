package com.me.attraversiamo;

import com.me.utils.GameConfig;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.me.attraversiamo.Attraversiamo;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        GameConfig cfg = new GameConfig();
        cfg.platform = GameConfig.Platform.IPHONE;
        cfg.showUI = true;
        cfg.timeStep = 1/45f;
        cfg.zoom = 5f;

        return new IOSApplication(new Attraversiamo(cfg), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}