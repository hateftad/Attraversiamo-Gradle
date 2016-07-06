package com.me.component;

import com.badlogic.gdx.math.Vector2;

public class TouchComponent extends BaseComponent {

	public boolean edgeTouch;
    public boolean pullEdgeTouch;
	public boolean ladderTouch;
	public boolean boxTouch;
	public boolean footEdgeL;
	public boolean footEdgeR;
	public boolean footEdge;
	public boolean rightPushArea;
	public boolean leftPushArea;
	public boolean pushArea;
	public boolean handTouch;
	public Vector2 touchCenter;
    public boolean handHoldArea;
    public boolean leftHoldArea;
    public boolean rightHoldArea;
    public boolean canCrawl;
    public boolean cageTouch;
    public boolean waterTouch;
	public boolean insideFinish;

    public TouchComponent() {
		edgeTouch = false;
		ladderTouch = false;
		boxTouch = false;
		footEdgeL = false;
		footEdgeR = false;
		handTouch = false;
		touchCenter = Vector2.Zero;
	}

    public boolean isHanging(){
        return edgeTouch;
    }

    public boolean canPullUp(){
        return handTouch && footEdge;
    }

	@Override
	public void dispose() {

	}

	@Override
	public void restart() {
		edgeTouch = false;
		footEdgeL = false;
		footEdgeR = false;
		handTouch = false;
		touchCenter = Vector2.Zero;
        edgeTouch = false;
        pullEdgeTouch = false;
        ladderTouch = false;
        boxTouch = false;
        footEdge = false;
        rightPushArea = false;
        leftPushArea = false;
        pushArea = false;
        handTouch = false;
        handHoldArea = false;
        leftHoldArea = false;
        rightHoldArea = false;
        canCrawl = false;
        cageTouch = false;
        waterTouch = false;
		insideFinish = false;
	}

}
