package com.me.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationState.AnimationStateListener;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader;
import com.me.component.interfaces.TaskEventObserverComponent;
import com.me.events.AnimationEvent;
import com.me.events.states.PlayerState;
import com.me.loaders.RubeImage;
import com.me.utils.Converters;

public abstract class AnimationComponent extends BaseComponent implements TaskEventObserverComponent {

	protected SkeletonRenderer renderer;

    protected TextureAtlas atlas;

    protected Skeleton skeleton;

    protected SkeletonData skeletonData;

    protected AnimationState animationState;

    protected PlayerState state;

	protected PlayerState previousState;

    protected Vector2 center;

    protected AnimationEvent event;

    protected boolean isCompleted;

    protected static final String BlackSkin = "silhouette", ColorSkin = "color";


    public AnimationComponent(TextureAtlas atlas, String skeleton, float scale){
        this.renderer = new SkeletonRenderer();
        this.atlas = atlas;
        AtlasAttachmentLoader atlasLoader = new AtlasAttachmentLoader(this.atlas);
        SkeletonJson json = new SkeletonJson(atlasLoader);
        json.setScale(scale);
        this.skeletonData = json.readSkeletonData(Gdx.files.internal(skeleton + ".json"));
        Gdx.gl20.glDepthMask(false);
    }

	public AnimationComponent(String atlas, String skeleton, float scale){
        this(new TextureAtlas(Gdx.files.internal(atlas+".atlas")), skeleton, scale);
	}

	public AnimationStateData setUp(RubeImage image){
		this.state = PlayerState.Idle;
		AnimationStateData stateData = new AnimationStateData(skeletonData);
		Vector2 size = new Vector2(image.width, image.height);
		size = Converters.ToWorld(size);
		this.center = new Vector2();
		this.center.set(image.center.x - size.x/2, image.center.y - (size.y/2));
		this.animationState = new AnimationState(stateData);
		//animationState.setAnimation(0, "running", true);
		this.animationState.addListener(new AnimationStateListener() {

            @Override
            public void start(int trackIndex) {
                isCompleted = false;
            }

            @Override
            public void event(int trackIndex, Event event) {
                System.out.println(event.getData().getName());
                setEvent(event);
            }

            @Override
            public void end(int trackIndex) {
                isCompleted = true;
            }

            @Override
            public void complete(int trackIndex, int loopCount) {
                isCompleted = true;
            }
        });
		this.event = new AnimationEvent();

		this.skeleton = new Skeleton(skeletonData);
		this.skeleton.setX(center.x);
		this.skeleton.setY(center.y);
		Skin skin = skeletonData.findSkin("silhouette");
		this.skeleton.setSkin(skin);
		this.center = Converters.ToBox(center);
		this.skeleton.updateWorldTransform();
		return stateData;
	}

	public void setUp(Vector2 center, String animation){
		AnimationStateData stateData = new AnimationStateData(skeletonData);
		animationState = new AnimationState(stateData);
		animationState.setAnimation(0, animation, false);
		animationState.addListener(new AnimationStateListener() {

			@Override
			public void start(int trackIndex) {
				// TODO Auto-generated method stub
				isCompleted = false;
			}

			@Override
			public void event(int trackIndex, Event event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void end(int trackIndex) {
				// TODO Auto-generated method stub
				isCompleted = true;
			}

			@Override
			public void complete(int trackIndex, int loopCount) {
				// TODO Auto-generated method stub
				isCompleted = true;
			}
		});

		this.skeleton = new Skeleton(skeletonData);
		this.skeleton.setX(center.x);
		this.skeleton.setY(center.y);
		this.center = Converters.ToBox(center);
		this.skeleton.updateWorldTransform();
	}

	public TextureAtlas getAtlas(){
		return atlas;
	}

	public Vector2 getCenter(){
		return center;
	}

	public void setPosition(Vector2 position){
		skeleton.setX(Converters.ToWorld(position.x));
		skeleton.setY(Converters.ToWorld(center.y + position.y));
	}

	public void setRotation(float rotation){
		Bone bone = skeleton.findBone("root");
		bone.setRotation(rotation);
		//System.out.println("Rotation "+rotation);
	}

	public void setFacing(boolean left){
		skeleton.setFlipX(left);
	}

	public void playAnimation(String name, boolean loop){
		animationState.setAnimation(0, name, loop);
	}

	public void addAnimation(String name, boolean loop, float delay){
		animationState.addAnimation(0, name, loop, delay);
	}

	public boolean isCompleted(){		
		return isCompleted;
	}

	public float getTime(){
		return animationState.getCurrent(0).getTime();
	}

	public AnimationState getAnimationState(){
		return animationState;
	}

	public PlayerState getState(){
		return state;
	}

	public void setState(PlayerState state){
		this.state = state;
	}

	public void setSkin(String skinName){
		skeleton.setSkin(skeletonData.findSkin(skinName));
	}

	public abstract void setAnimationState(PlayerState animationState);

	public boolean isCompleted(PlayerState state){
		return ((this.state == state) && (isCompleted));
	}

	public Skeleton getSkeleton(){
		return skeleton;
	}

	public void setupPose(){
		skeleton.setBonesToSetupPose();
        //skeleton.setToSetupPose();
	}

	public float getX(){
		return skeleton.getX();
	}

	public float getY(){
		return skeleton.getY();
	}
	
	public abstract void update(SpriteBatch sb, float dt);

    public PlayerState getPreviousState(){
        return previousState;
    }

	@Override
	public void dispose() {
		skeleton.getBones().clear();
		animationState.clearTracks();
		renderer = null;
		atlas.getRegions().clear();
		atlas.getTextures().clear();
		atlas.dispose();
	}

	@Override
	public void restart() {
		setAnimationState(PlayerState.Idle);
		setFacing(false);
	}

	public void setEvent(Event event) {
		this.event.setEvent(event);
	}
}
