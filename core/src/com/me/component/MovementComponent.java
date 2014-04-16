package com.me.component;

import com.badlogic.gdx.math.Vector2;

public class MovementComponent extends BaseComponent {

	public boolean m_left;
	public boolean m_right;
	public boolean m_down;
	public boolean m_up;
	public boolean m_jump;
	public boolean m_movingUp = false;
	public boolean m_movingDown = false;
	public Vector2 m_movement;
	public boolean m_lockControls = false;
	
	public void set(boolean left, boolean right, boolean up, boolean down, boolean jump)
	{
		m_left = left;
		m_right = right;
		m_up = up;
		m_down = down;
		m_jump = jump;
		
	}
	
	public void stopUpDown()
	{
		m_movingDown = false;
		m_movingUp =false;
	}
	
	public float getX()
	{
		return m_movement.x;
	}
	
	public float getY()
	{
		return m_movement.y;
	}
	
	public boolean moved()
	{
		return (m_left) || (m_right) || (m_jump);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
