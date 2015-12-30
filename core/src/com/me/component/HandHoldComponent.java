package com.me.component;


/**
 * Created by hateftadayon on 12/30/15.
 */
public class HandHoldComponent extends BaseComponent {

    private boolean m_holdingHands;
    public HandHoldComponent(){

    }

    public boolean isHoldingHands(){
        return m_holdingHands;
    }
    public void setHoldingHands(boolean holding){
        m_holdingHands = holding;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }

}
