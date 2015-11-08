package com.me.component;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.me.component.interfaces.TaskEventObserverComponent;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * Created by hateftadayon on 7/11/15.
 */
public abstract class TaskComponent extends BaseComponent implements TaskEventObserverComponent {

    protected int m_nrfinishers;
    protected ObjectMap<PlayerComponent.PlayerNumber, Boolean> m_finishers;

    public TaskComponent(int finishers){
        m_nrfinishers = finishers;
        m_finishers = new ObjectMap<PlayerComponent.PlayerNumber, Boolean>(m_nrfinishers);
    }

    public boolean allFinished(){
        return m_finishers.size == m_nrfinishers;
    }
}
