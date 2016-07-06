package com.me.component;


import com.me.component.interfaces.TelegramEventObserverComponent;
import com.me.events.TaskEvent;
import com.me.events.TelegramEvent;

/**
 * Created by hateftadayon on 12/30/15.
 */
public class HandHoldComponent extends BaseComponent implements TelegramEventObserverComponent{

    private boolean holdingHands;
    private boolean isLeading;
    public HandHoldComponent(){

    }

    public boolean isHoldingHands(){
        return holdingHands;
    }
    public void setHoldingHands(boolean holding){
        holdingHands = holding;
    }
    public void setIsLeading(boolean isLeading){
        this.isLeading = isLeading;
    }
    public boolean isLeading(){
        return isLeading;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void onNotify(TelegramEvent event) {
        holdingHands = true;
    }

    @Override
    public void onNotify(TaskEvent event) {

    }
}
