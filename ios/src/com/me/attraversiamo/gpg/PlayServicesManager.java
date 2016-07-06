package com.me.attraversiamo.gpg;

import com.me.ads.PlayServices;
import org.robovm.apple.foundation.Foundation;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.pods.google.games.GPGLauncherController;
import org.robovm.pods.google.games.GPGManager;
import org.robovm.pods.google.games.GPGStatusDelegateAdapter;
import org.robovm.pods.google.signin.GIDSignIn;
import org.robovm.pods.google.signin.GIDSignInUIDelegateAdapter;

/**
 * Created by hateftadayon on 3/17/16.
 */
public class PlayServicesManager implements PlayServices {
    public static final String CLIENT_ID = "348314594616-e23t3p1t7h6jqihbopgs456job8lvlmu.apps.googleusercontent.com";

    public PlayServicesManager() {
        GPGManager.getSharedInstance().setStatusDelegate(new GPGStatusDelegateAdapter() {
            @Override
            public void didFinishGamesSignIn(NSError error) {
                Foundation.log("didFinishGamesSignIn(" + error + ")");
            }

            @Override
            public void didFinishGamesSignOut(NSError error) {
                Foundation.log("didFinishGamesSignOut(" + error + ")");
            }

            @Override
            public void didFinishGoogleAuth(NSError error) {
                Foundation.log("didFinishGoogleAuth(" + error + ")");
            }

            @Override
            public boolean shouldReauthenticate(NSError error) {
                Foundation.log("shouldReauthenticate(" + error + ")");
                return false;
            }

            @Override
            public void willReauthenticate(NSError error) {
                Foundation.log("willReauthenticate(" + error + ")");
            }

            @Override
            public void didDisconnect(NSError error) {
                Foundation.log("didDisconnect(" + error + ")");
            }
        });

        GIDSignIn.getSharedInstance().setUiDelegate(new GIDSignInUIDelegateAdapter() {
            private UIViewController libgdxViewController;

            @Override
            public void willDispatch(GIDSignIn signIn, NSError error) {
                Foundation.log("willDispatch()");
            }

            @Override
            public void presentViewController(GIDSignIn signIn, UIViewController viewController) {
                Foundation.log("presentViewController()");
                libgdxViewController = UIApplication.getSharedApplication().getKeyWindow().getRootViewController();
                UIApplication.getSharedApplication().getKeyWindow().setRootViewController(viewController);
            }

            @Override
            public void dismissViewController(GIDSignIn signIn, UIViewController viewController) {
                Foundation.log("dismissViewController()");
                UIApplication.getSharedApplication().getKeyWindow().setRootViewController(libgdxViewController);
            }
        });
    }

    @Override
    public void signIn() {
        Foundation.log("signIn()");
        GPGManager.getSharedInstance().signIn(CLIENT_ID, false);
    }

    @Override
    public void signOut() {
        Foundation.log("signOut()");
        GPGManager.getSharedInstance().signOut();
    }

    @Override
    public void rateGame() {

    }

    @Override
    public void unlockAchievement() {

    }

    @Override
    public void submitScore(int highScore) {

    }

    @Override
    public void showScore() {
        if (isSignedIn()) {
            GPGLauncherController.getSharedInstance().presentLeaderboardList();
        } else {
            signIn();
        }
    }

    @Override
    public void showAchievement() {
        if (isSignedIn()) {
            GPGLauncherController.getSharedInstance().presentAchievementList();
        } else {
            signIn();
        }
    }

    @Override
    public boolean isSignedIn() {
        return GPGManager.getSharedInstance().isSignedIn();
    }
}
