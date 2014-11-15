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

public class AnimationComponent extends BaseComponent {

	private SkeletonRenderer m_renderer;

	private TextureAtlas m_atlas;

	private Skeleton m_skeleton;

	private SkeletonData m_skeletonData;

	private AnimationState m_animationState;

	private AnimState m_state;

	private AnimState m_previousState;

	private Vector2 m_center;

	public boolean m_isCompleted;

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
		m_skeletonData = json.readSkeletonData(Gdx.files.internal(skeleton+".json"));
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

	public void setPosition(float x, float y){
		m_skeleton.setX(Converters.ToWorld(x));
		m_skeleton.setY(Converters.ToWorld(m_center.y + y));
	}

	public Vector2 getBonePosition(String name){
		Bone b = m_skeleton.findBone(name);
		return new Vector2(b.getX(), b.getY());
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

	public void addAnimation(AnimState state, boolean loop, float delay){

		if(state != m_previousState)
		{
			setState(state);
			switch(state)
			{
			case WALKING:
				addAnimation("walking", loop, delay);
				break;
			case JOGGING:	
				addAnimation("jogging", loop, delay);
				break;
			case RUNNING:
				addAnimation("running", loop, delay);
				break;
			case JUMPING:
				addAnimation("runjumping", loop, delay);
				break;
			case JOGJUMP:
				addAnimation("jogjumping", loop, delay);
				break;
			case IDLE:
				addAnimation("idle", loop, delay);
				break;
			case UPJUMP:
				addAnimation("upJump", loop, delay);
				break;
			case FALLING:
				addAnimation("falling", loop, delay);
				break;
			case HANGING:
				addAnimation("hang", loop, delay);
				break;
			case CLIMBING:
				addAnimation("climbUp", loop, delay);
				break;
			case LADDERCLIMBUP:
				addAnimation("ladderClimbUp", loop, delay);
				break;
			case LADDERCLIMBDOWN:
				addAnimation("ladderClimbDown", loop, delay);
				break;
			case LADDERHANG:
				addAnimation("ladderHang", loop, delay);
				break;
			case PUSHING:
				addAnimation("pushing", loop, delay);
				break;
			case LIEDOWN:
				addAnimation("lieDown", loop, delay);
				break;
			case LYINGDOWN:
				addAnimation("lyingDown", loop, delay);
				break;
			case PULLUP:
				addAnimation("pullUp", loop, delay);
				break;
			case SUCKIN:
				addAnimation("suckIn", loop, delay);
				break;
			case STANDUP:
				addAnimation("standUp", loop, delay);
				break;
			case CRAWL:
				addAnimation("crawling", loop, delay);
				break;
			default:
				break;
			}
			//m_skeleton.setToSetupPose();
		}
		m_previousState = state;
	}
	
	public void setSkin(String skinName){
		m_skeleton.setSkin(m_skeletonData.findSkin(skinName));
	}

	public void setAnimationState(AnimState state){

		if(state != m_previousState)
		{
			setState(state);
			switch(state)
			{
			case WALKING:
				playAnimation("walking", true);
				break;
			case JOGGING:	
				playAnimation("jogging", true);
				break;
			case RUNNING:
				playAnimation("running", true);
				break;
			case JUMPING:
				playAnimation("runJumping", false);
				break;
			case JOGJUMP:
				playAnimation("jogjumping", false);
				break;
			case IDLE:
				playAnimation("idle1", true);
				break;
			case UPJUMP:
				playAnimation("upJump", false);
				break;
			case FALLING:
				playAnimation("falling", true);
				break;
			case HANGING:
				playAnimation("hang", true);
				break;
			case CLIMBING:
				playAnimation("climbUp", false);
				break;
			case LADDERCLIMBUP:
				playAnimation("ladderClimbUp", true);
				break;
			case LADDERCLIMBDOWN:
				playAnimation("ladderClimbDown", true);
				break;
			case LADDERHANG:
				playAnimation("ladderHang", false);
				break;
			case PUSHING:
				playAnimation("pushing", true);
				break;
			case LIEDOWN:
				playAnimation("lieDown", false);
				break;
			case LYINGDOWN:
				playAnimation("lyingDown", false);
				break;
			case PULLUP:
				playAnimation("pullUp", false);
				break;
			case SUCKIN:
				playAnimation("suckIn", false);
				break;
			case STANDUP:
				playAnimation("standUp", false);
				break;
			case CRAWL:
				playAnimation("crawling", true);
				break;
			case PRESSBUTTON:
				playAnimation("pressButton", false);
				break;
			case RUNOUT:
				playAnimation("runOut", false);
				break;
			default:
				break;
			}
			m_skeleton.setToSetupPose();
		}
		m_previousState = state;
	}

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
	
	public Vector2 getPositionRelative(String attachmentName){
		Slot slot = null;
		
		if(!m_skeleton.getSkin().getName().equals("color")){
			attachmentName = "silhouette/"+attachmentName;
		}
		
		for(Slot s : m_skeleton.getSlots()){
			if(s.getAttachment() != null){
				//System.out.println(s.getAttachment().getName());
				if(s.getAttachment().getName().equals(attachmentName)){
					slot = s;
					break;
				}
			}
		}
		
		return new Vector2(Converters.ToBox(m_skeleton.getX() + slot.getBone().getWorldX()), Converters.ToBox(m_skeleton.getY() + slot.getBone().getWorldY()));
	}

	public void update(SpriteBatch sb, float dt){
		m_animationState.update(dt);
		m_animationState.apply(m_skeleton);
		m_skeleton.update(dt);
		m_skeleton.updateWorldTransform();
		m_renderer.draw(sb, m_skeleton);
	}

	@Override
	public void dispose() {
		m_skeleton.getBones().clear();
		m_skeleton.getData().clear();
		//m_animationState.clearAnimation();
		m_animationState.clearTracks();
		m_renderer = null;
		m_atlas.getRegions().clear();
		m_atlas.getTextures().clear();
		m_atlas.dispose();
	}
	private AnimState prevState = AnimState.DYING;
	public void printStateChange(){
		if(prevState != m_state){
			System.out.println(m_state.name());
			prevState = m_state;
		}
	}

	@Override
	public void restart() {
		setAnimationState(AnimState.IDLE);
		setFacing(false);
	}

}
