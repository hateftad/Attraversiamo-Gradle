package com.me.component;

import com.me.physics.RayCastListener;
import com.me.physics.RaySet;

/**
 * Created by hateftadayon on 10/4/16.
 */
public class FeetRayCastComponent extends RayCastComponent {
    public FeetRayCastComponent(RaySet raySet, RayCastListener rayCastCallback) {
        super(raySet, rayCastCallback);
    }
}
