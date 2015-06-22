package com.me.component;

import com.badlogic.gdx.math.Vector2;

public class TouchComponent extends BaseComponent {

	public boolean m_groundTouch;
	public boolean m_edgeTouch;
	public boolean m_wallTouch;
	public boolean m_ladderTouch;
	public boolean m_boxTouch;
	public boolean m_footEdgeL;
	public boolean m_footEdgeR;
	public boolean m_footEdge;
	public boolean m_rightPushArea;
	public boolean m_leftPushArea;
	public boolean m_pushArea;
	public boolean m_handTouch;
	public boolean m_feetToBox;
	public boolean m_endReach;
	public Vector2 m_touchCenter;

	public TouchComponent() {
		m_groundTouch = false;
		m_edgeTouch = false;
		m_wallTouch = false;
		m_ladderTouch = false;
		m_boxTouch = false;
		m_footEdgeL = false;
		m_footEdgeR = false;
		m_handTouch = false;
		m_endReach = false;
		m_touchCenter = new Vector2();
	}

	@Override
	public void dispose() {

	}

	public boolean endReached() {
		return m_endReach;
	}

	@Override
	public void restart() {
		m_groundTouch = false;
		m_edgeTouch = false;
		m_wallTouch = false;
		m_ladderTouch = false;
		m_boxTouch = false;
		m_footEdgeL = false;
		m_footEdgeR = false;
		m_handTouch = false;
		m_endReach = false;
		m_touchCenter = Vector2.Zero;

	}

}
