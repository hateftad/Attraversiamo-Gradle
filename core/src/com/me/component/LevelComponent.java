package com.me.component;

import com.me.component.interfaces.GameEventObserverComponent;
import com.me.event.GameEventType;

/**
 * Created by hateftadayon on 7/11/15.
 */
public class LevelComponent extends BaseComponent implements GameEventObserverComponent {


    private boolean m_isFinished;

    public LevelComponent(){

    }

    public boolean isFinished(){
        return m_isFinished;
    }

    @Override
    public void onNotify(GameEventType event) {
        if(event == GameEventType.LevelFinished){
            m_isFinished = true;
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }
}
