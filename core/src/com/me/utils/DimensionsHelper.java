package com.me.utils;

public class DimensionsHelper {
	
	public static float initDimension(float height, float width){
		float zoom = 0f;
		//iphone 4
		
		if(height == 480 && width == 800){
			zoom = 9.5f;
		}
		if(height == 640 && width == 960){
			zoom = 9f;
		}
		//iphone 5
		if(height == 640 && width == 1136){
			zoom = 8f;
		}
		if(height == 720 && width == 1280){
			zoom = 6f;
		}
		//ipad mini & other
		if(height == 768 && width == 1024){
			zoom = 6f;
		}
		if(height == 800 && width == 1280){
			zoom = 6f;
		}
		if(height == 1080 && width == 1920){
			zoom = 5f;
		}
		//ipad
		if(height == 1536 && width == 2048){
			zoom = 4f;
		}
		
		return zoom;
	}

}
