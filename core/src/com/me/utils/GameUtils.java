package com.me.utils;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by hateftadayon on 2/23/16.
 */
public class GameUtils {

    public static Color getColor(String stringColor){
        Color color ;
        if(stringColor.equalsIgnoreCase("gray")) {
            color = Color.GRAY;
        } else if(stringColor.equalsIgnoreCase("blue")){
            color = Color.BLUE;
        } else if(stringColor.equalsIgnoreCase("Red")){
            color = Color.RED;
        } else if(stringColor.equalsIgnoreCase("Black")){
            color = Color.BLACK;
        } else if(stringColor.equalsIgnoreCase("Green")){
            color = Color.GREEN;
        } else if(stringColor.equalsIgnoreCase("Clear")){
            color = Color.CLEAR;
        } else if(stringColor.equalsIgnoreCase("Magenta")){
            color = Color.MAGENTA;
        } else if(stringColor.equalsIgnoreCase("Yellow")){
            color = Color.YELLOW;
        } else {
            color = Color.WHITE;
        }
        return color;
    }
}
