package com.me.component;

import com.me.component.interfaces.BinaryEventObserverComponent;
import com.me.events.BinaryEvent;
import com.me.events.GameEventType;

/**
 * Created by hateftadayon on 12/30/15.
 */
public class HandHoldComponent extends BaseComponent implements BinaryEventObserverComponent {

    private boolean m_holdingHands;

    public boolean isHoldingHands(){
        return m_holdingHands;
    }

    public HandHoldComponent(){

    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void onNotify(BinaryEvent event) {
        if(event.getEventType() == GameEventType.HoldingHands ){
            m_holdingHands = event.isActive();
        }
    }
}
