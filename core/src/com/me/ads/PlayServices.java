package com.me.ads;

/**
 * Created by hateftadayon on 3/16/16.
 */
public interface PlayServices {
    void signIn();

    void signOut();

    void rateGame();

    void unlockAchievement();

    void submitScore(int highScore);

    void showAchievement();

    void showScore();

    boolean isSignedIn();
}
