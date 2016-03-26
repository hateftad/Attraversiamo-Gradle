package com.me.attraversiamo.android.gpg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.badlogic.gdx.Gdx;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.me.ads.PlayServices;
import com.me.attraversiamo.android.R;

/**
 * Created by hateftadayon on 3/17/16.
 */
public class PlayServicesManager implements PlayServices {

    private final static int requestCode = 1;
    private GameHelper gameHelper;
    private Activity activity;

    public PlayServicesManager(Activity activity){
        this.activity = activity;

        gameHelper = new GameHelper(activity, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(false);

        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
            @Override
            public void onSignInFailed() {
                System.out.println("Sign in failed");
            }

            @Override
            public void onSignInSucceeded() {
                System.out.println("Sign in success");
            }
        };

        gameHelper.setup(gameHelperListener);
    }

    public void onStart(Activity activity){
        gameHelper.onStart(activity);
    }

    public void onStop(){
        gameHelper.onStop();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        gameHelper.onActivityResult(requestCode, requestCode, data);
    }

    @Override
    public void signIn() {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void signOut() {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.signOut();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void rateGame() {
        String str = "Your PlayStore Link";
        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    }

    @Override
    public void unlockAchievement() {
        Games.Achievements.unlock(gameHelper.getApiClient(),
                activity.getString(R.string.achievement_first));
    }

    @Override
    public void submitScore(int highScore) {
        if (isSignedIn()) {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(),
                    activity.getString(R.string.leaderboard_fastest), highScore);
        }
    }

    @Override
    public void showAchievement() {
        if (isSignedIn()) {
            activity.startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), requestCode);
        } else {
            signIn();
        }
    }

    @Override
    public void showScore() {
        if (isSignedIn()) {
            activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
                    activity.getString(R.string.leaderboard_fastest)), requestCode);
        } else {
            signIn();
        }
    }

    @Override
    public boolean isSignedIn() {
        return gameHelper.isSignedIn();
    }
}
