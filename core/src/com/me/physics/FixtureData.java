package com.me.physics;

import com.badlogic.gdx.physics.box2d.Filter;

/**
 * Created by hateftadayon on 5/28/15.
 */
public class FixtureData {

    private Filter currentFilter;
    private Filter restoredFilter;
    private float currentFriction;

    public FixtureData(Filter filter, float friction){
        currentFilter = filter;
        currentFriction = friction;
        restoredFilter = new Filter();
        restoredFilter.maskBits = filter.maskBits;
        restoredFilter.categoryBits = filter.categoryBits;
        restoredFilter.groupIndex = filter.groupIndex;
    }

    public void restoreCategoryBits(){
        currentFilter.categoryBits = restoredFilter.categoryBits;
        currentFilter.groupIndex = restoredFilter.groupIndex;
        currentFilter.maskBits = restoredFilter.maskBits;
    }

    public float getCurrentFriction() {
        return currentFriction;
    }


    public Filter getCurrentFilter(){
        Filter filter = new Filter();
        filter.maskBits = currentFilter.maskBits;
        filter.categoryBits = currentFilter.categoryBits;
        filter.groupIndex = currentFilter.groupIndex;
        return filter;
    }
}
