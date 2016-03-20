package com.me.physics;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.*;

public class JointFactory {


    private World world;
    private ArrayList<JointDef> joints = new ArrayList<>();

    private JointFactory() {
    }

    private static class SingletonHolder {
        private static final JointFactory INSTANCE = new JointFactory();
    }

    public void initialize(World world) {
        this.world = world;
    }

    public static JointFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public RevoluteJointDef createRevoluteJoint(Body b1, Body b2,
                                                Vector2 anchor1, Vector2 anchor2, float LLimit, float ULimit, boolean motor) {
        RevoluteJointDef jDef = new RevoluteJointDef();
        jDef.bodyA = b1;
        jDef.bodyB = b2;
        jDef.enableLimit = true;
        jDef.localAnchorA.set(anchor1);
        jDef.localAnchorB.set(anchor2);
        jDef.collideConnected = true;
        jDef.referenceAngle = 0;
        jDef.lowerAngle = -LLimit * MathUtils.degreesToRadians;
        jDef.upperAngle = ULimit * MathUtils.degreesToRadians;
        jDef.enableMotor = motor;

        return jDef;
    }

    public DistanceJointDef createDistanceJoint(Body b1, Body b2, Vector2 anchor1, Vector2 anchor2, boolean col, float length) {
        DistanceJointDef jDef = new DistanceJointDef();
        jDef.bodyA = b1;
        jDef.bodyB = b2;
        jDef.collideConnected = col;
        jDef.localAnchorA.set(anchor2);
        jDef.localAnchorB.set(anchor1);
        jDef.length = length;

        return jDef;
    }

    public WeldJointDef createWeldJoint(Body b1, Body b2, Vector2 anchor1, Vector2 anchor2, boolean col) {
        WeldJointDef jDef = new WeldJointDef();
        jDef.bodyA = b1;
        jDef.bodyB = b2;
        jDef.localAnchorA.set(anchor1);
        jDef.localAnchorB.set(anchor2);
        jDef.collideConnected = col;
        return jDef;
    }

    public PrismaticJointDef createPrismJoint(Body b1, Body b2, Vector2 anchor1, Vector2 anchor2, float uL, float lL, boolean col, boolean m, Vector2 lAxis) {
        PrismaticJointDef jDef = new PrismaticJointDef();
        jDef.bodyA = b1;
        jDef.bodyB = b2;
        jDef.localAnchorA.set(anchor1);
        jDef.localAnchorB.set(anchor2);
        jDef.lowerTranslation = lL;
        jDef.upperTranslation = uL;
        jDef.enableMotor = m;
        jDef.enableLimit = true;
        jDef.localAxisA.set(lAxis);
        jDef.maxMotorForce = 400;
        jDef.motorSpeed = 10;
        return jDef;

    }

    public Joint createJoint(Body bodyA, Body bodyB, JointDef jDef, World world) {
        jDef.bodyA = bodyA;
        jDef.bodyB = bodyB;
        return world.createJoint(jDef);
    }

    public Joint createJoint(JointDef jDef) {
        if (joints.contains(jDef)) {
            System.out.println("already is one");
            return null;
        } else {
            joints.add(jDef);
            Joint j = null;
            j = world.createJoint(jDef);

            return j;
        }
    }

    public void destroyJoint(Joint j) {
        world.destroyJoint(j);
    }

    public void printInfo() {
        System.out.println("Nr of joints " + world.getJointCount());
    }

    public void destroy() {
        world.dispose();
    }


}
