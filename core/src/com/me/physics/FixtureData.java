package com.me.physics;

import com.badlogic.gdx.physics.box2d.Filter;

/**
 * Created by hateftadayon on 5/28/15.
 */
public class FixtureData {

    private Filter m_currentFilter;
    private Filter m_restoredFilter;
    private float m_currentFriction;

    public FixtureData(Filter filter, float friction){
        m_currentFilter = filter;
        m_currentFriction = friction;
        m_restoredFilter = new Filter();
        m_restoredFilter.maskBits = filter.maskBits;
        m_restoredFilter.categoryBits = filter.categoryBits;
        m_restoredFilter.groupIndex = filter.groupIndex;
    }

    public void restoreCategoryBits(){
        m_currentFilter.categoryBits = m_restoredFilter.categoryBits;
        m_currentFilter.groupIndex = m_restoredFilter.groupIndex;
        m_currentFilter.maskBits = m_restoredFilter.maskBits;
    }

    public float getCurrentFriction() {
        return m_currentFriction;
    }


    public Filter getCurrentFilter(){
        Filter filter = new Filter();
        filter.maskBits = m_currentFilter.maskBits;
        filter.categoryBits = m_currentFilter.categoryBits;
        filter.groupIndex = m_currentFilter.groupIndex;
        return filter;
    }
}
