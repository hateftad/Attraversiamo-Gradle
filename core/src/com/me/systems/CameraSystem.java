package com.me.systems;


import box2dLight.RayHandler;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.me.component.*;
import com.me.level.Level;
import com.me.ui.InputManager;
import com.me.utils.Converters;

public class CameraSystem extends EntityProcessingSystem implements InputProcessor {


    @Mapper
    ComponentMapper<PlayerComponent> playerComp;
    @Mapper
    ComponentMapper<PhysicsComponent> physicsComp;
    @Mapper
    ComponentMapper<LightComponent> lightComps;
    @Mapper
    ComponentMapper<SpriteComponent> spriteComps;
    @Mapper
    ComponentMapper<PlayerAnimationComponent> animComps;

    private Vector3 camPos = new Vector3();

    private Vector3 currentCamPos = new Vector3();
    private CameraController camera;
    private RayHandler rayHandler;
    private Vector2 activePosition = new Vector2();
    private Box2DDebugRenderer debugDrawer;
    private boolean debug;
    private boolean process;

    float zoom;

    @SuppressWarnings("unchecked")
    public CameraSystem(RayHandler rh, CameraController camera) {
        super(Aspect.getAspectForOne(PlayerComponent.class));
        rayHandler = rh;
        rayHandler.setAmbientLight(Color.WHITE);
        //rayHandler.setBlurNum(3);
        debugDrawer = new Box2DDebugRenderer();

        this.camera = camera;
    }

    public void setLevelBoundariesForCamera(Level.LevelBoundaries boundaries) {
        camera.setLimit(boundaries);
    }

    public CameraController getCameraComponent() {
        return camera;
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }

    public void toggleProcess(boolean process) {
        this.process = process;
    }

    @Override
    protected boolean checkProcessing() {
        return process;
    }

    @Override
    protected void process(Entity e) {

        if (InputManager.getInstance().shouldLockCamera()) {
            camera.update();
        }
        rayHandler.setCombinedMatrix(camera.getCamera());
        rayHandler.updateAndRender();

        if (playerComp.has(e)) {

            if (playerComp.get(e).isActive()) {
                PhysicsComponent ps = physicsComp.get(e);
                PlayerAnimationComponent anim = animComps.get(e);
                if (InputManager.getInstance().shouldLockCamera()) {
                    camera.moveTo(Converters.ToWorld(anim.getPositionRelative("torso")));
                }
                activePosition = ps.getPosition();
            } else {
//                PhysicsComponent ps = physicsComp.get(e);
//                zoom = Math.abs((activePosition.x - ps.getPosition().x));
//                if(zoom < 30){
//                    zoom = 30;
//				}
//				camera.setZoom(zoom * 0.3f);
            }
        }
        if (lightComps.has(e)) {
            lightComps.get(e).setPosition(camera.getPosition().x, camera.getPosition().y + 2000);
            if (spriteComps.has(e)) {
                physicsComp.get(e).setPosition(Converters.ToBox(camera.getPosition().x, camera.getPosition().y + 2150));
            }
        }
        if (debug) {
            Matrix4 m = camera.getCombined();
            debugDrawer.render(world.getSystem(PhysicsSystem.class).getPhysicsWorld(), m.scale(100, 100, 0));
        }

    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Keys.E) {
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
        InputManager.getInstance().setShouldLockCamera(false);
        camPos.set(screenX, screenY, 0);
        camera.unproject(camPos);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        InputManager.getInstance().setShouldLockCamera(false);
        activePosition.set(screenX, screenY);
        currentCamPos.set(screenX, screenY, 0);
        camera.unproject(currentCamPos);
        camera.sub(currentCamPos.sub(camPos));
        camera.update();
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        System.out.println(camera.getZoom() + (amount * 1f));
        camera.setZoom(camera.getZoom() + (amount * 1f));
        if (camera.getZoom() < 0.1f) {
            camera.setZoom(0.1f);
        }
        camera.update();
        return false;
    }

    public void dispose() {
        rayHandler.dispose();
    }
}
