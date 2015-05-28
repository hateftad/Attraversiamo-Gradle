package com.me.physics;

import com.badlogic.gdx.physics.box2d.Filter;

/**
 * Created by hateftadayon on 5/28/15.
 */
public class FixtureData {

    private short m_currentBits;
    private Filter m_currentFilter;
    private float m_currentFriction;

    public FixtureData(Filter filter, float friction){
        m_currentBits = filter.categoryBits;
        m_currentFilter = filter;
        m_currentFriction = friction;
    }

    public void restoreCategoryBits(){
        m_currentFilter.categoryBits = m_currentBits;
    }

    public float getCurrentFriction() {
        return m_currentFriction;
    }

    public short getCurrentBits() {
        return m_currentBits;
    }

    public Filter getCurrentFilter(){
        return m_currentFilter;
    }
}
