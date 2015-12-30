package com.me.component;


import com.me.component.interfaces.TelegramEventObserverComponent;
import com.me.events.TaskEvent;
import com.me.events.TelegramEvent;

/**
 * Created by hateftadayon on 12/30/15.
 */
public class HandHoldComponent extends BaseComponent implements TelegramEventObserverComponent{

    private boolean m_holdingHands;
    private boolean m_isLeading;
    public HandHoldComponent(){

    }

    public boolean isHoldingHands(){
        return m_holdingHands;
    }
    public void setHoldingHands(boolean holding){
        m_holdingHands = holding;
    }
    public void setIsLeading(boolean isLeading){
        m_isLeading = isLeading;
    }
    public boolean isLeading(){
        return m_isLeading;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void onNotify(TelegramEvent event) {
        m_holdingHands = true;
    }

    @Override
    public void onNotify(TaskEvent event) {

    }
}
