package com.me.component;

import com.badlogic.gdx.utils.Array;
import com.me.component.interfaces.TaskEventObserverComponent;

/**
 * Created by hateftadayon on 7/11/15.
 */
public abstract class TaskComponent extends BaseComponent implements TaskEventObserverComponent {

    protected int m_nrfinishers;
    protected Array<PlayerComponent.PlayerNumber> m_finishers;

    public TaskComponent(int finishers){
        m_nrfinishers = finishers;
        m_finishers = new Array<PlayerComponent.PlayerNumber>(m_nrfinishers);
    }

    public boolean allFinished(){
        return m_finishers.size == m_nrfinishers;
    }
}
