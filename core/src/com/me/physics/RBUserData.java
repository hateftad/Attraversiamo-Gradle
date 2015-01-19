package com.me.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

public class RBUserData {

	public enum Type {
		GROUND, WALL, TORSO, FEET, PELVIS, LEFTEDGE, RIGHTEDGE, 
		LADDER, LEFTLADDER, RIGHTLADDER, TOPLADDER, BOTTOMLADDER,
		BOX, LEFTPULLUP, RIGHTPULLUP, HAND, BOXHAND, PORTAL, BOXFOOT, 
		FINISH, LEFTCRAWL, RIGHTCRAWL, CRAWLCANAL, LPUSHAREA, RPUSHAREA,
		HANGHANDS, FOOTSENSOR
	}

		public static final int
				Boundary = 			0x0001,
				Character = 		0x0002,
				WorldObject = 		0x0004,
				CharacterSensor = 	0x0008,
				WorldSensor = 		0x0010,
				DEFAULT_1 = 		0x0020,
				DEFAULT_2 = 		0x0040,
				DEFAULT_3 = 		0x0080,
				DEFAULT_4 = 		0x0100,
				DEFAULT_5 = 		0x0200,
				DEFAULT_6 = 		0x0400,
				DEFAULT_7 = 		0x0800,
				DEFAULT_8 = 		0x1000,
				DEFAULT_9 = 		0x2000,
				DEFAULT_10 = 		0x4000,
				DEFAULT_11 = 		0x8000,
				ALL = 				0xFFFF;


	private int m_collisionGroup;
	private int m_boxId;
	private Type m_type;

	public RBUserData(int boxId, int collisionGroup, Body body) {
		set(boxId, collisionGroup, body);

	}

	private void setFilterData(Body body, Filter filter){
		Array<Fixture> fixtureList = body.getFixtureList();
		for(Fixture fixture:fixtureList){
			fixture.setFilterData(filter);
		}
	}

	public void set(int boxid, int collisiongroup, Body body) {
		this.m_boxId = boxid;
		this.m_collisionGroup = collisiongroup;
		Filter filter = new Filter();
		switch (boxid) {
		case 1:
			setType(Type.GROUND);
			filter.categoryBits = Boundary;
			filter.maskBits = Character | WorldObject | Boundary;
			setFilterData(body, filter);
			break;
		case 2:
			setType(Type.WALL);
			break;
		case 3:
			setType(Type.TORSO);
			filter.categoryBits = Character;
			filter.maskBits = Boundary | WorldObject | WorldSensor;
			setFilterData(body, filter);
			break;
		case 4:
			setType(Type.FEET);
			filter.categoryBits = Character;
			filter.maskBits = Boundary | WorldObject;
			setFilterData(body, filter);
			break;
		case 5:
			setType(Type.LEFTEDGE);
			filter.categoryBits = WorldSensor;
			filter.maskBits = CharacterSensor;
			setFilterData(body, filter);
			break;
		case 6:
			setType(Type.RIGHTEDGE);
			filter.categoryBits = WorldSensor;
			filter.maskBits = CharacterSensor;
			setFilterData(body, filter);
			break;
		case 7:
			setType(Type.PELVIS);
			filter.categoryBits = CharacterSensor;
			filter.maskBits = WorldSensor;
			setFilterData(body, filter);
			break;
		case 8:
			setType(Type.RIGHTLADDER);
			filter.categoryBits = WorldSensor;
			filter.maskBits = CharacterSensor;
			setFilterData(body, filter);
			break;
		case 9:
			setType(Type.LEFTLADDER);
			filter.categoryBits = WorldSensor;
			filter.maskBits = CharacterSensor;
			setFilterData(body, filter);
			break;
		case 10:
			setType(Type.BOTTOMLADDER);
			filter.categoryBits = WorldSensor;
			filter.maskBits = CharacterSensor;
			setFilterData(body, filter);
			break;
		case 11:
			setType(Type.TOPLADDER);
			filter.categoryBits = WorldSensor;
			filter.maskBits = CharacterSensor;
			setFilterData(body, filter);
			break;
		case 12:
			setType(Type.LEFTPULLUP);
			filter.categoryBits = WorldSensor;
			filter.maskBits = CharacterSensor;
			setFilterData(body, filter);
			break;
		case 13:
			setType(Type.BOX);
			filter.categoryBits = WorldObject;
			filter.maskBits = Character | Boundary;
			setFilterData(body, filter);
			break;
		case 14:
			setType(Type.HAND);
			filter.categoryBits = CharacterSensor;
			filter.maskBits = CharacterSensor;
			setFilterData(body, filter);
			break;
		case 15:
			setType(Type.BOXHAND);
			break;
		case 16:
			setType(Type.PORTAL);
			filter.categoryBits = WorldSensor;
			filter.maskBits = CharacterSensor;
			setFilterData(body, filter);
			break;
		case 17:
			setType(Type.BOXFOOT);
			break;
		case 18:
			setType(Type.FINISH);
			filter.categoryBits = WorldSensor;
			filter.maskBits = CharacterSensor;
			setFilterData(body, filter);
			break;
		case 19:
			setType(Type.LEFTCRAWL);
			filter.categoryBits = WorldSensor;
			filter.maskBits = CharacterSensor;
			setFilterData(body, filter);
			break;
		case 20:
			setType(Type.RIGHTCRAWL);
			filter.categoryBits = WorldSensor;
			filter.maskBits = CharacterSensor;
			setFilterData(body, filter);
			break;
		case 21:
			setType(Type.CRAWLCANAL);
			filter.categoryBits = WorldSensor;
			filter.maskBits = CharacterSensor;
			setFilterData(body, filter);
			break;
		case 22:
			setType(Type.RIGHTPULLUP);
			filter.categoryBits = WorldSensor;
			filter.maskBits = CharacterSensor;
			setFilterData(body, filter);
			break;
		case 23:
			setType(Type.RPUSHAREA);
			filter.categoryBits = WorldSensor;
			filter.maskBits = Character;
			setFilterData(body, filter);
			break;
		case 24:
			setType(Type.LPUSHAREA);
			filter.categoryBits = WorldSensor;
			filter.maskBits = Character;
			setFilterData(body, filter);
			break;
		case 25:
			setType(Type.HANGHANDS);
			filter.categoryBits = CharacterSensor;
			filter.maskBits = WorldSensor;
			setFilterData(body, filter);
			break;
		case 26:
			setType(Type.FOOTSENSOR);
			filter.categoryBits = CharacterSensor;
			filter.maskBits = WorldSensor;
			setFilterData(body, filter);
			break;
		default:
			break;
		}

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
}
