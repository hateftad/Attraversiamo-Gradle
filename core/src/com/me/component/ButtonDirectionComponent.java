package com.me.component;

/**
 * Created by hateftadayon on 6/1/15.
 */
public class ButtonDirectionComponent extends BaseComponent {

    public static class Direction{

        public static int Undefined = -1;
        public static int Left = 0;
        public static int Right = 1;
        public static int Up = 2;
        public static int Down = 3;

        public int currentDirection = Undefined;
    }

    private Direction m_direction;

    public ButtonDirectionComponent(){
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
