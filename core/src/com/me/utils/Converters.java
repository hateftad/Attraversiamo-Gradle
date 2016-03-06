package com.me.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Converters {

	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;

	public static float ToBox(float x)
	{
		return x * WORLD_TO_BOX;
	}

	public static float ToWorld(float x)
	{
		return x * BOX_TO_WORLD;
	}

	public static Vector2 ToBox(Vector2 in)
	{
		float x = in.x * WORLD_TO_BOX;
		float y = in.y * WORLD_TO_BOX;
		
		return new Vector2(x,y);
	}
	
	public static Vector2 ToWorld(Vector2 in)
	{
		in.x = in.x * BOX_TO_WORLD;
		in.y = in.y * BOX_TO_WORLD;
		
		return in;
	}
	
	public static Vector2 ToBox(float x, float y)
	{
		Vector2 box = new Vector2();
		box.x = x * WORLD_TO_BOX;
		box.y = y * WORLD_TO_BOX;
		return box;
	}

	public static Vector2 ToWorld(float x, float y)
	{
		Vector2 world = new Vector2();
		world.x = x * BOX_TO_WORLD;
		world.y = y * BOX_TO_WORLD;

		return world;
	}
	
	public static Color getColor(String color){
		
		if(color.equals("Red")){
			return Color.RED;
		}
		if(color.equals("Green")){
			return Color.GREEN;
		}
		return null;
		
	}


}
