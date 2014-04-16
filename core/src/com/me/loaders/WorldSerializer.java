package com.me.loaders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.ReadOnlySerializer;
import com.badlogic.gdx.utils.JsonValue;

import com.me.loaders.RubeImage;
import com.me.loaders.RubeDefaults;
import com.me.loaders.RubeScene;

public class WorldSerializer extends ReadOnlySerializer<World>
{
	private final BodySerializer 	bodySerializer;
	private final JointSerializer 	jointSerializer;
	private final ImageSerializer imageSerializer;
	private RubeScene scene;
	
	public WorldSerializer(RubeScene scene, Json _json)
	{
		this.scene = scene;
		
		bodySerializer = new BodySerializer(scene,_json);
		_json.setSerializer(Body.class, bodySerializer);
		
		jointSerializer = new JointSerializer(scene,_json);
		_json.setSerializer(Joint.class, jointSerializer);
		
		imageSerializer = new ImageSerializer(scene);
		_json.setSerializer(RubeImage.class, imageSerializer);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public World read(Json json, JsonValue jsonData, Class type) 
	{
		boolean allowSleep = json.readValue("allowSleep", boolean.class, RubeDefaults.World.allowSleep, jsonData);
		boolean autoClearForces = json.readValue("autoClearForces", boolean.class, RubeDefaults.World.autoClearForces, jsonData);
		boolean continuousPhysics = json.readValue("continuousPhysics", boolean.class, RubeDefaults.World.continuousPhysics, jsonData);
		boolean warmStarting = json.readValue("warmStarting", boolean.class, RubeDefaults.World.warmStarting, jsonData);
		
		Vector2 gravity = json.readValue("gravity", Vector2.class, RubeDefaults.World.gravity, jsonData);
		
		World world = new World(gravity, allowSleep);
		world.setAutoClearForces(autoClearForces);
		world.setContinuousPhysics(continuousPhysics);
		world.setWarmStarting(warmStarting);
		scene.parseCustomProperties(json, world, jsonData);
		
		// Bodies
		bodySerializer.setWorld(world);
		Array<Body> bodies = json.readValue("body", Array.class, Body.class, jsonData);
		scene.setBodies(bodies);
		
		// Joints
		// joints are done in two passes because gear joints reference other joints
		// First joint pass
		jointSerializer.init(world, bodies, null);
		Array<Joint> joints = json.readValue("joint", Array.class, Joint.class, jsonData);
		// Second joint pass
		//jointSerializer.init(world, bodies, joints);
		//joints = json.readValue("joint", Array.class, Joint.class, jsonData);
		scene.setJoints(joints);
		
		// Images
		Array<RubeImage> images = json.readValue("image", Array.class, RubeImage.class, jsonData);
		scene.setImages(images);
		if (images != null)
		{
		   for (int i = 0; i < images.size; i++)
		   {
		      RubeImage image = images.get(i);
		      scene.setMappedImage(image.body, image);
		   }
		}
		return world;
	}

}
