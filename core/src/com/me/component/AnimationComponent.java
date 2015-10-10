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
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader;
import com.me.loaders.RubeImage;
import com.me.utils.Converters;

public abstract class AnimationComponent extends GameEventObserverComponent {

	protected SkeletonRenderer m_renderer;

    protected TextureAtlas m_atlas;

    protected Skeleton m_skeleton;

    protected SkeletonData m_skeletonData;

    protected AnimationState m_animationState;

    protected AnimState m_state;

	protected AnimState m_previousState;

    protected Vector2 m_center;

    protected boolean m_isCompleted;

	public enum AnimState{
		WALKING, IDLE, JUMPING, RUNNING, JOGGING, JOGJUMP,
		DYING, UPJUMP, HANGING, 
		CLIMBING, LADDERCLIMBUP, LADDERCLIMBDOWN, 
		LADDERHANG, FALLING, PUSHING,
		LIEDOWN, PULLUP, SUCKIN, WALKOUT, CRAWL, STANDUP,
		LYINGDOWN, PRESSBUTTON, RUNOUT
	}

	public AnimationComponent(String atlas, String skeleton, float scale){
		m_renderer = new SkeletonRenderer();
		m_atlas = new TextureAtlas(Gdx.files.internal(atlas+".atlas"));
		AtlasAttachmentLoader atlasLoader = new AtlasAttachmentLoader(m_atlas);
		SkeletonJson json = new SkeletonJson(atlasLoader);
		json.setScale(scale);
		m_skeletonData = json.readSkeletonData(Gdx.files.internal(skeleton + ".json"));
		Gdx.gl20.glDepthMask(false);
	}

	public AnimationStateData setUp(RubeImage image){

		AnimationStateData stateData = new AnimationStateData(m_skeletonData);
		Vector2 size = new Vector2(image.width, image.height);
		size = Converters.ToWorld(size);
		m_center = new Vector2();
		m_center.set(image.center.x - size.x/2, image.center.y - (size.y/2));
		m_animationState = new AnimationState(stateData);
		m_animationState.setAnimation(0, "running", true);
		m_animationState.addListener(new AnimationStateListener() {

            @Override
            public void start(int trackIndex) {
                m_isCompleted = false;
            }

            @Override
            public void event(int trackIndex, Event event) {

            }

            @Override
            public void end(int trackIndex) {
                m_isCompleted = true;
            }

            @Override
            public void complete(int trackIndex, int loopCount) {
                m_isCompleted = true;
            }
        });

		m_skeleton = new Skeleton(m_skeletonData);
		m_skeleton.setX(m_center.x);
		m_skeleton.setY(m_center.y);
		Skin skin = m_skeletonData.findSkin("silhouette");
		m_skeleton.setSkin(skin);
		m_center = Converters.ToBox(m_center);
		m_skeleton.updateWorldTransform();
		return stateData;
	}

	public void setUp(Vector2 center, String animation){
		AnimationStateData stateData = new AnimationStateData(m_skeletonData);
		m_animationState = new AnimationState(stateData);
		m_animationState.setAnimation(0, animation, false);
		m_animationState.addListener(new AnimationStateListener() {

			@Override
			public void start(int trackIndex) {
				// TODO Auto-generated method stub
				m_isCompleted = false;
			}

			@Override
			public void event(int trackIndex, Event event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void end(int trackIndex) {
				// TODO Auto-generated method stub
				m_isCompleted = true;	
			}

			@Override
			public void complete(int trackIndex, int loopCount) {
				// TODO Auto-generated method stub
				m_isCompleted = true;
			}
		});

		m_skeleton = new Skeleton(m_skeletonData);
		m_skeleton.setX(center.x);
		m_skeleton.setY(center.y);
		m_center = Converters.ToBox(center);
		m_skeleton.updateWorldTransform();
	}

	public TextureAtlas getAtlas(){
		return m_atlas;
	}

	public Vector2 getcenter(){
		return m_center;
	}

	public void setPosition(Vector2 position){
		m_skeleton.setX(Converters.ToWorld(position.x));
		m_skeleton.setY(Converters.ToWorld(m_center.y + position.y));
	}

	public void setFacing(boolean left){
		m_skeleton.setFlipX(left);
	}

	public void playAnimation(String name, boolean loop){
		m_animationState.setAnimation(0, name, loop);
	}

	public void addAnimation(String name, boolean loop, float delay){
		m_animationState.addAnimation(0, name, loop, delay);
	}

	public boolean isCompleted(){		
		return m_isCompleted;
	}

	public float getTime(){
		return m_animationState.getCurrent(0).getTime();
	}

	public AnimationState getAnimationState(){
		return m_animationState;
	}

	public AnimState getState(){
		return m_state;
	}

	public void setState(AnimState state){
		m_state = state;
	}

	public void setSkin(String skinName){
		m_skeleton.setSkin(m_skeletonData.findSkin(skinName));
	}

	public abstract void setAnimationState(AnimState animationState);

	public boolean isCompleted(AnimState state){
		return ((state == m_state) && (m_isCompleted));
	}

	public Skeleton getSkeleton(){
		return m_skeleton;
	}

	public void setupPose(){
		m_skeleton.setBonesToSetupPose();
	}

	public float getX(){
		return m_skeleton.getX();
	}

	public float getY(){
		return m_skeleton.getY();
	}
	
	public abstract Vector2 getPositionRelative(String attachmentName);

	public abstract void update(SpriteBatch sb, float dt);

	@Override
	public void dispose() {
		m_skeleton.getBones().clear();
		m_animationState.clearTracks();
		m_renderer = null;
		m_atlas.getRegions().clear();
		m_atlas.getTextures().clear();
		m_atlas.dispose();
	}

	@Override
	public void restart() {
		setAnimationState(AnimState.IDLE);
		setFacing(false);
	}

}
