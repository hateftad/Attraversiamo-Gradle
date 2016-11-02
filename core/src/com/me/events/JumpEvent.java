package com.me.events;

import com.badlogic.gdx.ai.steer.behaviors.Jump;
import com.badlogic.gdx.math.Vector2;
import com.me.systems.GameEntityWorld;

/**
 * Created by hateftadayon on 10/27/16.
 */
public class JumpEvent extends ActionEvent {

    private Jump.JumpDescriptor<Vector2> jumpDescriptor;

    public JumpEvent(Vector2 takeOff, Vector2 land) {
        super(GameEventType.Jump);
        jumpDescriptor = new Jump.JumpDescriptor<>(takeOff, land);
    }

    public Jump.JumpDescriptor<Vector2> getJumpDescriptor() {
        return jumpDescriptor;
    }
}
