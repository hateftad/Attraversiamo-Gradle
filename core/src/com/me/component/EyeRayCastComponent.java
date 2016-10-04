package com.me.component;

import com.me.physics.EyeRay;
import com.me.physics.EyeRayCastListener;

/**
 * Created by hateftadayon on 10/4/16.
 */
public class EyeRayCastComponent extends RayCastComponent {
    public EyeRayCastComponent(EyeRay eyeRay, EyeRayCastListener eyeRayCastListener) {
        super(eyeRay, eyeRayCastListener);
    }
}
