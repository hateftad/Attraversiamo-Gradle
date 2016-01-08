package com.me.events;


import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.EventData;

/**
 * Created by hateftadayon on 10/31/15.
 */
public class AnimationEvent {

    private Event m_event;

    public AnimationEvent(){
        m_event = new Event(new EventData(AnimationEventType.NONE.name()));
    }

    public void setEvent(Event event) {
        this.m_event = event;
    }

    public AnimationEventType getEventType(){
        return determineEvent(m_event.getData().getName());
    }

    private AnimationEventType determineEvent(String eventName){
        if(eventName.equalsIgnoreCase(AnimationEventType.JUMPUP.name())){
            return AnimationEventType.JUMPUP;
        } else if(eventName.equalsIgnoreCase(AnimationEventType.JUMP.name())){
            return AnimationEventType.JUMP;
        } else if(eventName.equalsIgnoreCase(AnimationEventType.PULLLEDGE.name())){
            return AnimationEventType.PULLLEDGE;
        }
        return AnimationEventType.NONE;
    }

    public void resetEvent() {
        m_event = new Event(new EventData(AnimationEventType.NONE.name()));
    }

    public enum AnimationEventType {
        JUMPUP,
        JUMP,
        PULLLEDGE,
        NONE
    }
}
