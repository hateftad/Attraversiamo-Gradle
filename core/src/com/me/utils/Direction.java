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

    public int m_currentDirection = Undefined;

    public Direction(int startDirection){
        m_currentDirection = startDirection;
    }

    public void LeftToRight(){
        if(m_currentDirection == Left){
            m_currentDirection = Right;
        } else {
            m_currentDirection = Left;
        }
    }

    public void upToDown(){
        if(m_currentDirection == Up){
            m_currentDirection = Down;
        } else {
            m_currentDirection = Up;
        }
    }
}
