package com.me.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

public class RBUserData {

    public enum Type {
        Ground, Wall, Torso, Feet, Pelvis, LeftEdge, RightEdge,
        Ladder, LeftLadder, RightLadder, TopLadder, BottomLadder,
        Box, LeftPullup, RightPullup, Hand, BoxHand, Portal, ObjectWorldCollision,
        Finish, LeftCrawl, RightCrawl, CrawlCanal, LeftPushButton, RightPushButton,
        HangHands, FootSensor, Water, RightHandHold, LeftHandHold, ColorChangeSensor, InsideCage, CageHatch, PullLedge
    }

    public static final int
            Boundary = 0x0001,
            CharacterFeet = 0x0002,
            CharacterTorso = 0x0004,
            WorldObject = 0x0008,
            CharacterSensor = 0x00010,
            WorldSensor = 0x0020,
            DEFAULT_1 = 0x0020,
            DEFAULT_2 = 0x0040,
            DEFAULT_3 = 0x0080,
            DEFAULT_4 = 0x0100,
            DEFAULT_5 = 0x0200,
            DEFAULT_6 = 0x0400,
            DEFAULT_7 = 0x0800,
            DEFAULT_8 = 0x1000,
            DEFAULT_9 = 0x2000,
            DEFAULT_10 = 0x4000,
            DEFAULT_11 = 0x8000,
            ALL = 0xFFF;


    private int m_collisionGroup;
    private int m_boxId;
    private int m_extraInfo;
    private Type m_type;

    public RBUserData(int boxId, int collisionGroup, int extra, Body body) {
        set(boxId, collisionGroup, body);
        setExtraInfo(extra);
    }

    public void setExtraInfo(int extraInfo) {
        m_extraInfo = extraInfo;
    }

    private void setFilterData(Body body, Filter filter) {
        Array<Fixture> fixtureList = body.getFixtureList();
        for (Fixture fixture : fixtureList) {
            fixture.setFilterData(filter);
        }
    }

    public void set(int boxid, int collisiongroup, Body body) {
        this.m_boxId = boxid;
        this.m_collisionGroup = collisiongroup;
        Filter filter = new Filter();
        switch (boxid) {
            case 1:
                setType(Type.Ground);
                filter.categoryBits = Boundary;
                filter.maskBits = CharacterFeet | CharacterTorso | WorldObject | Boundary | WorldSensor;
                break;
            case 2:
                setType(Type.Wall);
                break;
            case 3:
                setType(Type.Torso);
                filter.categoryBits = CharacterTorso;
                filter.maskBits = Boundary | WorldObject | WorldSensor;
                break;
            case 4:
                setType(Type.Feet);
                filter.categoryBits = CharacterFeet;
                filter.maskBits = Boundary | WorldObject | WorldSensor;
                break;
            case 5:
                setType(Type.LeftEdge);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterSensor;
                break;
            case 6:
                setType(Type.RightEdge);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterSensor;
                break;
            case 7:
                setType(Type.Pelvis);
                filter.categoryBits = CharacterSensor;
                filter.maskBits = WorldSensor;
                break;
            case 8:
                setType(Type.RightLadder);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterSensor;
                break;
            case 9:
                setType(Type.LeftLadder);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterSensor;
                break;
            case 10:
                setType(Type.BottomLadder);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterSensor;
                break;
            case 11:
                setType(Type.TopLadder);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterSensor;
                break;
            case 12:
                setType(Type.LeftPullup);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterSensor | CharacterFeet;
                break;
            case 13:
                setType(Type.Box);
                filter.categoryBits = WorldObject;
                filter.maskBits = CharacterTorso | CharacterFeet | Boundary | WorldSensor | WorldObject;
                break;
            case 14:
                setType(Type.Hand);
                filter.categoryBits = CharacterSensor;
                filter.maskBits = CharacterSensor;
                break;
            case 15:
                setType(Type.BoxHand);
                break;
            case 16:
                setType(Type.Portal);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterSensor;
                break;
            case 17:
                setType(Type.ObjectWorldCollision);
                filter.categoryBits = WorldObject;
                filter.maskBits = WorldObject;
                break;
            case 18:
                setType(Type.Finish);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterSensor;
                break;
            case 19:
                setType(Type.LeftCrawl);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterSensor;
                break;
            case 20:
                setType(Type.RightCrawl);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterSensor;
                break;
            case 21:
                setType(Type.CrawlCanal);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterSensor;
                break;
            case 22:
                setType(Type.RightPullup);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterSensor | CharacterFeet;
                break;
            case 23:
                setType(Type.RightPushButton);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterTorso;
                break;
            case 24:
                setType(Type.LeftPushButton);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterTorso;
                break;
            case 25:
                setType(Type.HangHands);
                filter.categoryBits = CharacterSensor;
                filter.maskBits = WorldSensor;
                break;
            case 26:
                setType(Type.FootSensor);
                filter.categoryBits = CharacterSensor;
                filter.maskBits = WorldSensor;
                break;
            case 27:
                setType(Type.Water);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterSensor | CharacterTorso | WorldObject;
                break;
            case 28:
                setType(Type.LeftHandHold);
                filter.categoryBits = CharacterSensor;
                filter.maskBits = CharacterSensor;
                break;
            case 29:
                setType(Type.RightHandHold);
                filter.categoryBits = CharacterSensor;
                filter.maskBits = CharacterSensor;
                break;
            case 30:
                setType(Type.PullLedge);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterSensor;
                break;
            case 31:
                setType(Type.ColorChangeSensor);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterSensor;
                break;
            case 32:
                setType(Type.InsideCage);
                filter.categoryBits = WorldSensor;
                filter.maskBits = CharacterSensor;
                break;
            case 33:
                setType(Type.CageHatch);
                filter.categoryBits = Boundary;
                filter.maskBits = ALL;
                break;
            case 34:
                filter.categoryBits = WorldSensor;
                filter.maskBits = Boundary;

            default:
                break;
        }
        setFilterData(body, filter);
    }

    public int getBoxId() {
        return this.m_boxId;
    }

    public int getCollisionGroup() {
        return this.m_collisionGroup;
    }

    public Type getType() {
        return m_type;
    }

    public void setType(Type m_type) {
        this.m_type = m_type;
    }

    public int getExtraInfo() {
        return m_extraInfo;
    }
}
