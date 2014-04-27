package com.me.loaders;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationStateData;
import com.me.component.AnimationComponent;
import com.me.component.CameraComponent;
import com.me.component.CrawlComponent;
import com.me.component.GrabComponent;
import com.me.component.HangComponent;
import com.me.component.JointComponent;
import com.me.component.JumpComponent;
import com.me.component.LadderClimbComponent;
import com.me.component.LevelComponent;
import com.me.component.LightComponent;
import com.me.component.MovementComponent;
import com.me.component.ParticleComponent;
import com.me.component.ParticleComponent.ParticleType;
import com.me.component.PhysicsComponent;
import com.me.component.PlayerOneComponent;
import com.me.component.PlayerTwoComponent;
import com.me.component.PushComponent;
import com.me.component.RagDollComponent;
import com.me.component.TouchComponent;
import com.me.component.TriggerComponent;
import com.me.component.VelocityLimitComponent;
import com.me.component.AnimationComponent.AnimState;
import com.me.component.PlayerComponent;
import com.me.component.SpriteComponent;
import com.me.loaders.BodySerializer.BodyUserData;
import com.me.loaders.RubeScene.Indexes;
import com.me.physics.JointFactory;
import com.me.physics.PhysicsListenerSetup;
import com.me.physics.RBUserData;
import com.me.systems.CameraSystem;
import com.me.utils.Converters;
import com.me.utils.LevelConfig;
import com.me.utils.PlayerConfig;

public class EntityLoader {

	private RubeScene m_scene;
	private RubeSceneLoader m_loader;
	private WeakHashMap<String, Texture> m_textureMap;
	private static final String LVLPATH = "data/level/";
	private static final String CHARPATH = "data/character/";

	public EntityLoader()
	{
		m_textureMap = new WeakHashMap<String, Texture>();
		m_loader = new RubeSceneLoader();
	}

	private void clearLoader(){
		
		if(m_scene!=null){
			m_scene.clear();
		}
		if(m_textureMap.size() > 0){	
			m_textureMap.clear();
		}

	}
	
	public void dispose(){
		m_scene.dispose();
		m_scene.clear();
		for(Texture t: m_textureMap.values()){
			t.dispose();
		}
		m_textureMap.clear();
		
	}

	public void loadLevel(LevelConfig config, World entityWorld, com.badlogic.gdx.physics.box2d.World physicsWorld, RayHandler rh)
	{
		String levelDirectory = config.getLevelName();
		clearLoader();
		
		m_scene = m_loader.loadScene(Gdx.files.internal("data/level/"+levelDirectory+"/"+config.getLevelName()+".json"));
		Array<Body> bodies = m_scene.getBodies();

		Vector2 bodyPos = new Vector2();
		Vector2 tmp = new Vector2();
		PhysicsComponent pComp = null;
		SpriteComponent sComp = null;
		Entity entity = null;
		RubeImage image = null;
		List<Body> tempList = new ArrayList<Body>();

		for (int i = 0; i < bodies.size; i++) {

			Body body = bodies.get(i);

			Array<RubeImage> images = m_scene.getMappedImage(body);
			if (images!=null) {

				bodyPos.set(body.getPosition());
				image =  images.get(0);
				String tName = image.file;
				
				
				if (tName != null)
				{
					tmp.set(image.width, image.height);
					String textureFileName = null;
					if(!tName.contains("common")){
						textureFileName = LVLPATH +levelDirectory+"/" + tName;
					}else{
						textureFileName = LVLPATH + tName;
					}
					System.out.println(textureFileName);
					Texture texture = m_textureMap.get(textureFileName);
					if (texture == null)
					{
						texture = new Texture(textureFileName);
						texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Nearest);
						m_textureMap.put(textureFileName, texture);
					}
					if (image.body != null) {
						sComp = new SpriteComponent(texture, image.flip, image.body, 
								image.color, tmp, image.center, 
								image.angleInRads * MathUtils.radiansToDegrees, image.renderOrder);

						pComp = new PhysicsComponent(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
						entity = entityWorld.createEntity();
						entity.addComponent(pComp);
						entity.addComponent(sComp);
						//entity.addToWorld();

					}
				}

			}
			else{
				pComp = new PhysicsComponent(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
				entity = entityWorld.createEntity();
				entity.addComponent(pComp);
				//entity.addToWorld();
			}
			if(m_scene.getCustom(body, "bodyType", "").equals("light"))
			{
				ConeLight light = new ConeLight(rh, 500, Color.GREEN, 500, Converters.ToWorld(body.getPosition().x),Converters.ToWorld(body.getPosition().y),180, 180);
				entity.addComponent(new LightComponent(light, ((BodyUserData) body.getUserData()).mName));
				entity.addComponent(new TriggerComponent());
				entityWorld.getManager(GroupManager.class).add(entity, "lights");
			}
			
			if(m_scene.getCustom(body, "bodyType", "").equals("skyLight")){
				
				CameraComponent camComp = entityWorld.getSystem(CameraSystem.class).getCameraComponent();
				entity.addComponent(camComp);
				PointLight light = new PointLight(rh, 50, config.getLightColor(), 1000, camComp.getCamera().position.x, camComp.getCamera().position.y);				
				entity.addComponent(new LightComponent(light, "cameraLight"));
				entityWorld.getManager(GroupManager.class).add(entity, "lights");
			}

			loadFixtures(pComp, body);

			BodyUserData ud = (BodyUserData) body.getUserData();
			if(ud.mName.equals("box")){
				pComp.setMass(20f, ud.mName);
			}
			if(ud.mName.equals("portal")){
				entity.addComponent(new ParticleComponent("fire", ParticleType.PORTAL));
				entity.addComponent(new TriggerComponent());
				LevelComponent levelComp = new LevelComponent(config);
				config.setLevelComponent(levelComp);
				entity.addComponent(levelComp);
			}
			if(ud.mName.equals("finish")){
				entity.addComponent(new TriggerComponent());
				LevelComponent levelComp = new LevelComponent(config);
				config.setLevelComponent(levelComp);
				entity.addComponent(levelComp);
			}
			if(ud.mName.equals("point")){
				entity.addComponent(new ParticleComponent("point", ParticleType.PICKUP));
				entity.addComponent(new TriggerComponent());
			}
			if(ud.mName.equals("minX")){
				config.m_minX = Converters.ToWorld(body.getPosition().x);
				System.out.println("Minx " + Converters.ToWorld(body.getPosition().x));			
			}
			if(ud.mName.equals("maxX")){
				config.m_maxX = Converters.ToWorld(body.getPosition().x);
				System.out.println("MaxX " + Converters.ToWorld(body.getPosition().x));
			}
			if(ud.mName.equals("minY")){
				config.m_minY = Converters.ToWorld(body.getPosition().y);
				System.out.println("MinY " + Converters.ToWorld(body.getPosition().y));
			}
			
			pComp.setRBUserData(pComp.getBody(ud.mName), new RBUserData(ud.mBoxIndex, ud.mCollisionGroup));
			pComp.setUserData(entity, ((BodyUserData) body.getUserData()).mName);
			tempList.add(pComp.getBody(ud.mName));
			entity.addToWorld();
			entityWorld.getManager(GroupManager.class).add(entity, "worldObjects");
		}

		loadBodyJoints(physicsWorld, tempList);


		tempList.clear();
	}


	public Entity loadCharacter(PlayerConfig config, World entityWorld, com.badlogic.gdx.physics.box2d.World physicsWorld, RayHandler rh)
	{

		String characterPath = config.m_name + "/";
		clearLoader();

		m_scene = m_loader.loadScene(Gdx.files.internal(CHARPATH+characterPath+"/"+config.getName()+".json"));
		Array<Body> bodies = m_scene.getBodies();

		Vector2 bodyPos = new Vector2();
		Vector2 tmp = new Vector2();
		PhysicsComponent pComp = null;
		SpriteComponent sComp = null;
		Entity entity = entityWorld.createEntity();
		RubeImage image = null;
		List<Body> tempList = new ArrayList<Body>();

		for (int i = 0; i < bodies.size; i++) {

			Body body = bodies.get(i);

			Array<RubeImage> images = m_scene.getMappedImage(body);
			if (images!=null) {

				bodyPos.set(body.getPosition());
				image =  images.get(0);; 
				String tName = image.file;
				if (tName != null)
				{
					tmp.set(image.width, image.height);
					String textureFileName = CHARPATH+characterPath + tName;
					Texture texture = m_textureMap.get(textureFileName);
					if (texture == null)
					{
						texture = new Texture(textureFileName);
						texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Nearest);
						m_textureMap.put(textureFileName, texture);
					}
					if (image.body != null) {
						sComp = new SpriteComponent(texture, image.flip, image.body, 
								image.color, tmp, image.center, 
								image.angleInRads * MathUtils.radiansToDegrees, image.renderOrder);
						if(pComp!=null){
							pComp.addBody(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
						}else{
							pComp = new PhysicsComponent(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
							entity.addComponent(pComp);
						}
						entity.addComponent(sComp);
					}
				}

			}
			else{
				if(m_scene.getCustom(body, "characterType", "").equals("leg")){

					if(pComp!=null){
						pComp.addBody(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
					}else{
						pComp = new PhysicsComponent(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
						pComp.setMass(0.001f, ((BodyUserData) body.getUserData()).mName);
						entity.addComponent(pComp);
					}
				} else if(m_scene.getCustom(body, "characterType", "").equals("hand")){
					if(pComp!=null)
						pComp.addBody(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
					else{
						pComp = new PhysicsComponent(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
						entity.addComponent(pComp);
					}
				} else if(m_scene.getCustom(body, "characterType", "").equals("LHand")){
					if(pComp!=null){
						pComp.addBody(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
						pComp.setMass(0.01f, ((BodyUserData) body.getUserData()).mName);
					}
					else{
						pComp = new PhysicsComponent(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
						entity.addComponent(pComp);
					}
				}else{
					if(pComp!=null)
						pComp.addBody(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
					else{
						pComp = new PhysicsComponent(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
						entity.addComponent(pComp);
					}
				}
			}

			loadFixtures(pComp, body);

			String skelName = m_scene.getCustom(body, "skeleton", "failed");
			String atlasName = m_scene.getCustom(body, "atlas", "failed");
			AnimationComponent anim = null;
			AnimationStateData stateData = null;
			if(!skelName.equals("failed") && !atlasName.equals("failed"))
			{
				anim = new AnimationComponent(CHARPATH+characterPath+atlasName, CHARPATH+characterPath+skelName, 1.3f);
				entity.addComponent(anim);
			}
			if(m_scene.getCustom(body, "characterType", "").equals("playerOne"))
			{
				PlayerComponent p = new PlayerComponent(m_scene.getCustom(body, "characterType", ""));
				p.setActive(config.m_active);
				p.setFacingLeft(config.m_facingleft);
				p.setCanBecomeInactive(config.m_canDeactivate);
				//entity.addComponent(new LightComponent(light, ((BodyUserData) body.getUserData()).mName));
				entity.addComponent(p);
				entity.addComponent(new TouchComponent());
				entity.addComponent(new MovementComponent());
				entity.addComponent(new JointComponent());
				entity.addComponent(new HangComponent());
				entity.addComponent(new RagDollComponent());
				entity.addComponent(new LadderClimbComponent());
				entity.addComponent(new VelocityLimitComponent(8, 12, 5));
				entity.addComponent(new PushComponent());
				entity.addComponent(new JumpComponent());
				entity.addComponent(new GrabComponent());
				entity.addComponent(new PlayerOneComponent());
				entity.addComponent(new TriggerComponent());
				pComp.setName(((BodyUserData) body.getUserData()).mName);
				pComp.setIsPlayer(true);
				stateData = anim.setUp(image);
				anim.setAnimationState(AnimState.IDLE);
				stateData.setMix("idle", "jogging", 0.4f);
				stateData.setMix("runjumping", "running", 0.6f);
				stateData.setMix("jogging", "running", 0.4f);
				//stateData.setMix("falling", "idle", 0.1f);
				stateData.setMix("runjumping", "upJump", 0.1f);
				stateData.setMix("upJump", "running", 0.2f);
				stateData.setMix("idle", "climbUp", 0.6f);
				stateData.setMix("jogging", "pushing", 0.5f);
				stateData.setMix("idle", "pushing", 0.4f);
				stateData.setMix("ladderHang", "running", 0.1f);
				pComp.setPosition(config.m_playerPosition);
				//stateData.setMix("lieDown", "running", 0.3f);

			}else if(m_scene.getCustom(body, "characterType","").equals("playerTwo")){
				PlayerComponent p = new PlayerComponent(m_scene.getCustom(body, "characterType", ""));
				p.setActive(config.m_active);
				p.setFacingLeft(config.m_facingleft);
				p.setCanBecomeInactive(config.m_canDeactivate);
				
				pComp.setName(((BodyUserData) body.getUserData()).mName);
				pComp.setMass(0.001f, ((BodyUserData) body.getUserData()).mName);
				pComp.setIsPlayer(true);
				stateData = anim.setUp(image);
				anim.setAnimationState(AnimState.IDLE);
				stateData.setMix("idle", "walking", 0.3f);
				stateData.setMix("walking", "running", 0.6f);
				stateData.setMix("jumping", "running", 0.2f);
				stateData.setMix("walking", "jumping", 0.2f);
				//stateData.setMix("lieDown", "lyingDown", 0.2f);
				//stateData.setMix("crawling", "lyingDown", 0.2f);
				entity.addComponent(p);
				entity.addComponent(new MovementComponent());
				VelocityLimitComponent vel = new VelocityLimitComponent(4.5f, 8);
				vel.m_crawlLimit = 2.5f;
				entity.addComponent(vel);
				entity.addComponent(new TouchComponent());
				entity.addComponent(new JumpComponent());
				entity.addComponent(new GrabComponent());
				entity.addComponent(new PlayerTwoComponent());
				entity.addComponent(new TriggerComponent());
				entity.addComponent(new CrawlComponent());
				pComp.setPosition(config.m_playerPosition);
			}

			BodyUserData ud = (BodyUserData) body.getUserData();
			pComp.setRBUserData(pComp.getBody(ud.mName), new RBUserData(ud.mBoxIndex, ud.mCollisionGroup));
			pComp.setUserData(entity, ud.mName);
			tempList.add(pComp.getBody(ud.mName));
		}
		loadBodyJoints(physicsWorld, tempList);
		//loadBodyParts(entity, ps);
		PhysicsListenerSetup setup = new PhysicsListenerSetup();
		setup.setPlayerPhysics(pComp);
		tempList.clear();
		entity.addToWorld();
		entityWorld.getManager(GroupManager.class).add(entity, "players");
		return entity;
	}

	private void loadBodyJoints(com.badlogic.gdx.physics.box2d.World physicsWorld, List<Body> tempList)
	{
		Array<Joint> joints = m_scene.getJoints();
		if(joints == null)
			return;
		for (int j = 0; j < joints.size; j++) {

			Joint joint = joints.get(j);
			Indexes ind = m_scene.getJointBodyIndex(j);

			if (joint.getType() == JointType.DistanceJoint) {

				DistanceJointDef jDef = (DistanceJointDef) ind.jointDef;
				JointFactory.getInstance().createJoint(
						tempList.get(ind.first), 
						tempList.get(ind.second),
						jDef, physicsWorld);
			}
			if (joint.getType() == JointType.RevoluteJoint){
				RevoluteJointDef jDef = (RevoluteJointDef) ind.jointDef;
				JointFactory.getInstance().createJoint(
						tempList.get(ind.first), 
						tempList.get(ind.second),
						jDef,physicsWorld);
			}
			if (joint.getType() == JointType.WheelJoint){
				WheelJointDef jDef = (WheelJointDef) ind.jointDef;
				JointFactory.getInstance().createJoint(
						tempList.get(ind.first), 
						tempList.get(ind.second),
						jDef,physicsWorld);
				//	m_wheels.add((WheelJoint)jD);
			}
			if(joint.getType() == JointType.WeldJoint){
				WeldJointDef jDef = (WeldJointDef) ind.jointDef;
				JointFactory.getInstance().createJoint(
						tempList.get(ind.first),
						tempList.get(ind.second),
						jDef, physicsWorld);
			}


		}
	}
	/*
	private Vector2 vector = new Vector2();
	private void loadBodyParts(Entity e, PhysicsSystem pSystem)
	{
		AnimationComponent anim = e.getComponent(AnimationComponent.class);
		PhysicsComponent ps = e.getComponent(PhysicsComponent.class);
		final TextureAtlas atlas = anim.getAtlas();

		AtlasAttachmentLoader atlasLoader = new AtlasAttachmentLoader(atlas);
		SkeletonJson json = new SkeletonJson(atlasLoader);
		json.setScale(1.3f);
		SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("skeleton.json"));

		Skeleton skeleton = new Skeleton(skeletonData);

		for (Slot slot : skeleton.getSlots()) {
			if (!(slot.getAttachment() instanceof RegionAttachment)) continue;
			RegionAttachment attachment = (RegionAttachment)slot.getAttachment();

			PolygonShape boxPoly = new PolygonShape();
			boxPoly.setAsBox(
					Converters.ToBox(attachment.getWidth() / 2 * attachment.getScaleX()),
					Converters.ToBox(attachment.getHeight() / 2 * attachment.getScaleY()),
					Converters.ToBox(vector.set(attachment.getX(), attachment.getY())),
					attachment.getRotation() * MathUtils.degRad);
			System.out.println(attachment.getName());
			BodyDef boxBodyDef = new BodyDef();
			boxBodyDef.type = BodyType.StaticBody;
			ps.createBody(pSystem.getWorld(),boxBodyDef, attachment.getName());
			ps.createFixture(boxPoly, attachment.getName());

			boxPoly.dispose();
		}
	}
	 */
	private void loadFixtures(PhysicsComponent pComp, Body body)
	{
		Array<Fixture> fixtures = body.getFixtureList();

		if ((fixtures != null) && (fixtures.size > 0))
		{
			// for each fixture on the body...
			for (int j = 0; j < fixtures.size; j++)
			{
				Fixture fixture = fixtures.get(j);
				pComp.createFixture(fixture, ((BodyUserData) body.getUserData()).mName);
			}
		}
	}

}
