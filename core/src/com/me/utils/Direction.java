package com.me.utils;

/**
 * Created by hateftadayon on 7/13/15.
 */
public class Direction {

    public static final int Undefined = -1;
    public static final int Left = 0;
    public static final int Right = 1;
    public static final int Up = 2;
    public static final int Down = 3;

    public int currentDirection = Undefined;

    public Direction(int startDirection){
        currentDirection = startDirection;
    }

    public void LeftToRight(){
        if(currentDirection == Left){
            currentDirection = Right;
        } else {
            currentDirection = Left;
        }
    }

    public void upToDown(){
        if(currentDirection == Up){
            currentDirection = Down;
        } else {
            currentDirection = Up;
        }
    }
}
