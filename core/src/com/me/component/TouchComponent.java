package com.me.component;

import com.badlogic.gdx.math.Vector2;

public class TouchComponent extends BaseComponent {

	public boolean m_edgeTouch;
    public boolean m_pullEdgeTouch;
	public boolean m_ladderTouch;
	public boolean m_boxTouch;
	public boolean m_footEdgeL;
	public boolean m_footEdgeR;
	public boolean m_footEdge;
	public boolean m_rightPushArea;
	public boolean m_leftPushArea;
	public boolean m_pushArea;
	public boolean m_handTouch;
	public Vector2 m_touchCenter;
    public boolean m_handHoldArea;
    public boolean m_leftHoldArea;
    public boolean m_rightHoldArea;
    public boolean m_canCrawl;

    public TouchComponent() {
		m_edgeTouch = false;
		m_ladderTouch = false;
		m_boxTouch = false;
		m_footEdgeL = false;
		m_footEdgeR = false;
		m_handTouch = false;
		m_touchCenter = Vector2.Zero;
	}

    public boolean isHanging(){
        return m_edgeTouch;
    }

    public boolean canPullUp(){
        return m_handTouch && m_footEdge;
    }

	@Override
	public void dispose() {

	}

	@Override
	public void restart() {
		m_edgeTouch = false;
		m_ladderTouch = false;
		m_boxTouch = false;
		m_footEdgeL = false;
		m_footEdgeR = false;
		m_handTouch = false;
		m_touchCenter = Vector2.Zero;

	}

}
