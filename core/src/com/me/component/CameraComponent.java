package com.me.component;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.me.level.Level;

public class CameraComponent extends BaseComponent {

	private OrthographicCamera m_camera;
	
	private Vector3 m_target = new Vector3();
	
	private float m_maxX, m_minX, m_minY, m_maxY;
	private float m_lerp;


    public CameraComponent(OrthographicCamera camera){
		m_camera = camera;
		m_lerp = 0.07f;
	}
	
	public OrthographicCamera getCamera(){
		return m_camera;
	}
	
	public void setLimit(Level.LevelBoundaries boundaries){
		m_maxX = boundaries.maxX;
		m_minX = boundaries.minX;
		m_minY = boundaries.minY;
		m_maxY = boundaries.maxY;
	}
	
	public void moveTo(Vector2 target){
		m_target.set(target.x, target.y, 0);
	}
	
	public void update(float delta){
		
		Vector3 position = getCamera().position;a
		
		position.x += (m_target.x - position.x) * m_lerp;
		position.y += (m_target.y - position.y) * m_lerp;
		if(position.y < m_minY){
			position.y = m_minY;
		}
        if(position.y > m_maxY){
            position.y = m_maxY;
        }
        if(position.x > m_maxX){
            position.x = m_maxX;
        }
        if(position.x < m_minX){
            position.x = m_minX;
        }

        m_camera.update();
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
