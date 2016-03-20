package com.me.component;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.me.level.Level;

public class CameraComponent extends BaseComponent {

	private OrthographicCamera camera;
	
	private Vector3 target = new Vector3();
	
	private float maxX, minX, minY, maxY;
	private float lerp;


    public CameraComponent(OrthographicCamera camera){
		this.camera = camera;
		this.lerp = 0.07f;
	}
	
	public OrthographicCamera getCamera(){
		return camera;
	}
	
	public void setLimit(Level.LevelBoundaries boundaries){
		maxX = boundaries.maxX;
		minX = boundaries.minX;
		minY = boundaries.minY;
		maxY = boundaries.maxY;
	}
	
	public void moveTo(Vector2 target){
		this.target.set(target.x, target.y, 0);
	}
	
	public void update(float delta){
		
		Vector3 position = getCamera().position;
		
		position.x += (target.x - position.x) * lerp;
		position.y += (target.y - position.y) * lerp;
		if(position.y < minY){
			position.y = minY;
		}
        if(position.y > maxY){
            position.y = maxY;
        }
        if(position.x > maxX){
            position.x = maxX;
        }
        if(position.x < minX){
            position.x = minX;
        }

        camera.update();
    }
	
	public void unproject(Vector3 vec){
		camera.unproject(vec);
	}
	
	public void sub(Vector3 vec){
		camera.position.sub(vec);
	}
	
	public void setPosition(float x, float y){
		camera.position.set(x, y, 0);
	}
	
	public Vector3 getPosition(){
		return camera.position;
	}
	
	public float getZoom(){
		return camera.zoom;
	}
	public void setZoom(float z){
		camera.zoom = z;
	}
	
	public Matrix4 getCombined(){
		return new Matrix4(camera.combined);
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}
	
}
