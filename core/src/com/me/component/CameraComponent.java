package com.me.component;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraComponent extends BaseComponent {

	private OrthographicCamera m_camera;
	
	private Vector3 m_target = new Vector3();
	
	private float m_maxX, m_minX, m_minY;
	private float m_lerp;

	
	public CameraComponent(OrthographicCamera camera){
		m_camera = camera;
		m_lerp = 0.07f;
	}
	
	public OrthographicCamera getCamera(){
		return m_camera;
	}
	
	public void setLimit(float maxX, float minX, float minY){
		m_maxX = maxX;
		m_minX = minX;
		m_minY = minY;
	}
	
	public void moveTo(Vector2 target){
		m_target.set(target.x, target.y + 1000, 0);
	}
	
	public void update(float delta){
		
		Vector3 position = this.getCamera().position;
		
		position.x += (m_target.x - position.x) * m_lerp;
		position.y += (m_target.y - position.y) * m_lerp;
		if(getCamera().position.y < m_minY){
			getCamera().position.y = m_minY;
			position.y = m_minY;
		}
		
		m_camera.update();
		
		if(position.x > m_maxX){
			position.x = m_maxX;
		} else if(position.x < m_minX){
			position.x = m_minX;
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
		
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}
	
}
