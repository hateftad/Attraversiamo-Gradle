package com.me.systems;

import box2dLight.RayHandler;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.me.component.CameraComponent;
import com.me.component.LightComponent;
import com.me.component.PhysicsComponent;
import com.me.component.PlayerComponent;
import com.me.component.SpriteComponent;
import com.me.ui.InputManager;
import com.me.utils.Converters;
import com.me.utils.GlobalConfig;
import com.me.utils.LevelConfig;

public class CameraSystem extends EntityProcessingSystem implements InputProcessor{

	@Mapper ComponentMapper<CameraComponent> m_cameraComp;
	@Mapper ComponentMapper<PlayerComponent> m_playerComp;
	@Mapper ComponentMapper<PhysicsComponent> m_physicsComp;
	@Mapper ComponentMapper<LightComponent> m_lightComps;
	@Mapper ComponentMapper<SpriteComponent> m_spriteComps;

	private Vector3 m_camPos = new Vector3();

	private Vector3 m_currentCamPos = new Vector3();
	private CameraComponent m_camera;
	private RayHandler m_rayHandler;
	private Vector2 activePosition = new Vector2();
	private Box2DDebugRenderer m_debugDrawer;
	private boolean debug;
	private boolean m_process;
	private boolean m_showUI;
	private InputManager m_inputMgr;

	@SuppressWarnings("unchecked")
	public CameraSystem(RayHandler rh, World world) {
		super(Aspect.getAspectForOne(CameraComponent.class, PlayerComponent.class));
		m_rayHandler = rh;
		m_debugDrawer = new Box2DDebugRenderer();

		OrthographicCamera camera = new OrthographicCamera();  
		camera.viewportHeight = Gdx.graphics.getHeight();  
		camera.viewportWidth = Gdx.graphics.getWidth();  
		camera.position.set(camera.viewportWidth * .5f, camera.viewportHeight * .5f, 0f);  
		camera.zoom = GlobalConfig.getInstance().config.zoom;
		camera.update();

		m_camera = new CameraComponent(camera);
		m_showUI = GlobalConfig.getInstance().config.showUI;
		m_inputMgr = InputManager.getInstance();
	}
	
	public void setLimits(LevelConfig config){
		m_camera.setLimit(config.m_maxX, config.m_minX, config.m_minY);
	}

	public CameraComponent getCameraComponent(){
		return m_camera;
	}

	public RayHandler getRayHandler(){
		return m_rayHandler;
	}

	public void toggleProcess(boolean process){
		m_process = process;
	}

	@Override
	protected boolean checkProcessing() {
		return m_process;
	}
	float zoom;
	@Override
	protected void process(Entity e) {
		m_inputMgr.update(world.delta, m_showUI);
		
		if(m_cameraComp.has(e)){
			m_camera = m_cameraComp.get(e);
			m_camera.update(world.delta);
			m_rayHandler.setCombinedMatrix(m_camera.getCombined());
			m_rayHandler.updateAndRender();
		}

		if(m_playerComp.has(e)){

			if(m_playerComp.get(e).isActive()){
				PhysicsComponent ps = m_physicsComp.get(e);
				m_camera.moveTo(ps.getWorldPosition());
				activePosition = ps.getPosition();
			}
			else{
				PhysicsComponent ps = m_physicsComp.get(e);
				//zoom = Math.abs((activePosition.x - ps.getPosition().x));
				if(zoom < 30){
					zoom = 30;
				}
				//m_camera.setZoom(zoom * 0.3f);
			}
		}
		if(m_lightComps.has(e)){
			m_lightComps.get(e).setPosition(m_camera.getPosition().x , m_camera.getPosition().y + 2000);
			if(m_spriteComps.has(e)){
				m_physicsComp.get(e).setPosition(Converters.ToBox(m_camera.getPosition().x , m_camera.getPosition().y + 2150));
			}
		}
		if(debug){
			Matrix4 m = m_camera.getCombined();
			m_debugDrawer.render(world.getSystem(PhysicsSystem.class).getWorld(), m.scale(100, 100, 0));
		}
		
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		if(keycode == Keys.E){
			debug = !debug;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		m_camPos.set(screenX,screenY,0);
		m_camera.unproject(m_camPos);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		m_currentCamPos.set(screenX,screenY,0);
		m_camera.unproject(m_currentCamPos);
		m_camera.sub(m_currentCamPos.sub(m_camPos));
		m_camera.update(world.delta);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {

		m_camera.setZoom(m_camera.getZoom() + (amount * 1f));
		if (m_camera.getZoom() < 0.1f)
		{
			m_camera.setZoom(0.1f);
		}
		m_camera.update(world.delta);
		return false;
	}
	
	public void dispose(){
		m_rayHandler.dispose();
	}

}
