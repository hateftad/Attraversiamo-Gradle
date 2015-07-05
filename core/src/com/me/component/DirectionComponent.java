package com.me.component;

/**
 * Created by hateftadayon on 6/1/15.
 */
public class DirectionComponent extends BaseComponent {

    public static class Direction{

        public static final int Undefined = -1;
        public static final int Left = 0;
        public static final int Right = 1;
        public static final int Up = 2;
        public static final int Down = 3;

        public int currentDirection = Undefined;
    }

    private Direction m_direction;

    public DirectionComponent(){
        m_direction = new Direction();
    }

    public int getDirection(){
        return m_direction.currentDirection;
    }

    public void setDirection(int direction){
        m_direction.currentDirection = direction;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }
}