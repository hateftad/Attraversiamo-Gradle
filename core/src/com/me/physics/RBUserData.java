package com.me.physics;

public class RBUserData {

	public enum Type
	{
		GROUND,
		WALL,
		TORSO,
		FEET,
		PELVIS,
		LEFTEDGE,
		RIGHTEDGE,
		LADDER,
		LEFTLADDER,
		RIGHTLADDER,
		TOPLADDER,
		BOTTOMLADDER,
		BOX,
		LEFTPULLUP,
		RIGHTPULLUP,
		HAND,
		BOXHAND,
		PORTAL,
		BOXFOOT,
		FINISH,
		LEFTCRAWL,
		RIGHTCRAWL,
		CRAWLCANAL
	}
	
	private int m_collisionGroup;
	private int m_boxId;
	private Type m_type;
	public RBUserData(int boxId, int collisionGroup)
	{
		set(boxId, collisionGroup);
	}
	
	public void set(int boxid,int collisiongroup){
		this.m_boxId=boxid;
		this.m_collisionGroup=collisiongroup;
		
		switch (boxid) {
		case 1:
			setType(Type.GROUND);
			break;
		case 2:
			setType(Type.WALL);
			break;
		case 3:
			setType(Type.TORSO);
			break;
		case 4:
			setType(Type.FEET);
			break;
		case 5:
			setType(Type.LEFTEDGE);
			break;
		case 6:
			setType(Type.RIGHTEDGE);
			break;
		case 7:
			setType(Type.PELVIS);
			break;
		case 8:
			setType(Type.RIGHTLADDER);
			break;
		case 9:
			setType(Type.LEFTLADDER);
			break;
		case 10:
			setType(Type.BOTTOMLADDER);
			break;
		case 11:
			setType(Type.TOPLADDER);
			break;
		case 12:
			setType(Type.LEFTPULLUP);
			break;
		case 13:
			setType(Type.BOX);
			break;
		case 14:
			setType(Type.HAND);
			break;
		case 15:
			setType(Type.BOXHAND);
			break;
		case 16:
			setType(Type.PORTAL);
			break;
		case 17:
			setType(Type.BOXFOOT);
			break;
		case 18:
			setType(Type.FINISH);
			break;
		case 19:
			setType(Type.LEFTCRAWL);
			break;
		case 20:
			setType(Type.RIGHTCRAWL);
			break;
		case 21:
			setType(Type.CRAWLCANAL);
			break;
		case 22:
			setType(Type.RIGHTPULLUP);
			break;
		default:
			break;
		}
		
	}
	public int getBoxId(){
		return this.m_boxId;
	}
	
	public int getCollisionGroup(){ 
		return this.m_collisionGroup;
	}
	public Type getType() {
		return m_type;
	}
	public void setType(Type m_type) {
		this.m_type = m_type;
	}
}
