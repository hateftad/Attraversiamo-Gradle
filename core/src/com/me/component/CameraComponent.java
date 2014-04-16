package com.me.component;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraComponent extends BaseComponent {

	private OrthographicCamera m_camera;
	
	private Vector3 m_target = new Vector3();

	
	public CameraComponent(OrthographicCamera camera){
		m_camera = camera;
	}
	
	public OrthographicCamera getCamera()
	{
		return m_camera;
	}
	
	public void moveTo(Vector2 target){
		m_target.set(target.x, target.y + 800, 0);
	}
	
	public void update(float delta)
	{
		
		
		float lerp = 0.07f;
		Vector3 position = this.getCamera().position;
		
		position.x += (m_target.x - position.x) * lerp;
		position.y += (m_target.y - position.y) * lerp;
		if(getCamera().position.y < 0){
			getCamera().position.y = 0;
			position.y = 0;
		}
		
		m_camera.update();
		
		if(position.x > 8500){
			position.x = 8500;
		} else if(position.x < 0){
			position.x = 0;
		}
		
		
		
		
	}
	
	public void unproject(Vector3 vec){
		m_camera.unproject(vec);
	}
	
	public void sub(Vector3 vec){
		m_camera.position.sub(vec);
	}
	
	public void setPosition(float x, float y){
		m_camera.position.set(x, y, 0);
	}
	
	public Vector3 getPosition(){
		return m_camera.position;
	}
	
	public float getZoom(){
		return m_camera.zoom;
	}
	public void setZoom(float z){
		m_camera.zoom = z;
	}
	
	public Matrix4 getCombined(){
		return new Matrix4(m_camera.combined);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
