package com.me.utils;

import com.badlogic.gdx.math.Vector3;

public class Helpers {
	
	public static float length(Vector3 v1, Vector3 v2){
		
		return ((v1.x - v2.x) * (v1.x - v2.x)) + ((v1.y - v2.y) * (v1.y - v2.y));
	}
	
	public static Vector3 sub(Vector3 v1, Vector3 v2){
		
		return new Vector3(v1.x - v2.x, v1.y-v2.y, v1.z - v2.z );
	}
}
