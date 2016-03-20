package com.me.events;


import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.EventData;

/**
 * Created by hateftadayon on 10/31/15.
 */
public class AnimationEvent {

    private Event event;

    public AnimationEvent(){
        event = new Event(0, new EventData(AnimationEventType.NONE.name()));
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public AnimationEventType getEventType(){
        return determineEvent(event.getData().getName());
    }

    private AnimationEventType determineEvent(String eventName){
        if(eventName.equalsIgnoreCase(AnimationEventType.JUMPUP.name())){
            return AnimationEventType.JUMPUP;
        } else if(eventName.equalsIgnoreCase(AnimationEventType.JUMP.name())){
            return AnimationEventType.JUMP;
        } else if(eventName.equalsIgnoreCase(AnimationEventType.PULLLEDGE.name())){
            return AnimationEventType.PULLLEDGE;
        } else if (eventName.equalsIgnoreCase(AnimationEventType.SWING.name())){
            return AnimationEventType.SWING;
        } else if (eventName.equalsIgnoreCase(AnimationEventType.PRESSINGBUTTON.name())){
            return AnimationEventType.PRESSINGBUTTON;
        }
        return AnimationEventType.NONE;
    }

    public void resetEvent() {
        event = new Event(0, new EventData(AnimationEventType.NONE.name()));
    }

    public enum AnimationEventType {
        JUMPUP,
        JUMP,
        PULLLEDGE,
        SWING,
        PRESSINGBUTTON,
        NONE
    }
}
