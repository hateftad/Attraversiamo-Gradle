package com.me.systems;

import java.util.*;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.component.*;

public class RenderSystem extends EntitySystem {

    @Mapper
    ComponentMapper<PhysicsComponent> physicsComponents;
    @Mapper
    ComponentMapper<SpriteComponent> spritesComponents;
    @Mapper
    ComponentMapper<PlayerAnimationComponent> playerAnimation;
    @Mapper
    ComponentMapper<LevelAnimationComponent> levelAnimation;
    @Mapper
    ComponentMapper<SingleParticleComponent> singleParticles;
    @Mapper
    ComponentMapper<ContinuousParticles> continuousParticles;
    @Mapper
    ComponentMapper<EventParticleComponent> eventParticles;
    @Mapper
    ComponentMapper<ShaderComponent> shaderComps;

    private List<Entity> sortedEntities;

    private SpriteBatch batch;

    private OrthographicCamera camera;

    @SuppressWarnings("unchecked")
    public RenderSystem(OrthographicCamera camera) {

        super(Aspect.getAspectForAll(PhysicsComponent.class,
                SpriteComponent.class));
        this.camera = camera;
    }

    @Override
    protected void initialize() {
        this.batch = new SpriteBatch();
        this.sortedEntities = new ArrayList<>();
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    @Override
    protected void begin() {
        this.batch.begin();
        this.batch.setProjectionMatrix(camera.combined);
    }

    @Override
    protected void end() {
        this.batch.end();
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        for (Entity sortedEntity : sortedEntities) {
            process(sortedEntity);
        }
    }

    protected void process(Entity entity) {

        if (physicsComponents.has(entity)) {

            drawParticles(entity);

            PhysicsComponent physics = physicsComponents.getSafe(entity);
            physics.updateWorldPosition();
            if (spritesComponents.has(entity)) {

                SpriteComponent sprite = spritesComponents.get(entity);
                sprite.setPosition(physics.getWorldPosition());
                sprite.setRotation(physics.getBody().getAngle());
                if (playerAnimation.has(entity)) {
                    PlayerAnimationComponent anim = playerAnimation.get(entity);
                    anim.setPosition(physics.getPosition());
                    anim.update(batch, world.delta);
                } else {
                    if (sprite.shouldDraw) {
                        sprite.draw(batch);
                    }
                }
                if (levelAnimation.has(entity)) {
                    LevelAnimationComponent anim = levelAnimation.get(entity);
                    anim.setPosition(physics.getPosition());
                    anim.update(batch, world.delta);
                }
            }

        }


        if (shaderComps.has(entity)) {
            ShaderComponent sComp = shaderComps.get(entity);
            sComp.render(batch, camera, spritesComponents.get(entity));
        }

    }

    private void drawParticles(Entity entity) {
        if (continuousParticles.has(entity)) {
            ContinuousParticles particles = continuousParticles.get(entity);
            if (particles.needsDrawAndUpdate()) {
                particles.draw(batch, world.delta);
            }
        }

        if (eventParticles.has(entity)) {
            EventParticleComponent particles = eventParticles.get(entity);
            if (particles.needsDrawAndUpdate()) {
                particles.draw(batch, world.delta);
            }
        }

        if (singleParticles.has(entity)) {
            SingleParticleComponent particles = singleParticles.get(entity);
            if (particles.needsDrawAndUpdate()) {
                particles.draw(batch, world.delta);
            }
        }
    }

    @Override
    protected void inserted(Entity e) {

        sortedEntities.add(e);
        Collections.sort(sortedEntities, new Comparator<Entity>() {
            @Override
            public int compare(Entity e1, Entity e2) {
                SpriteComponent s1 = spritesComponents.get(e1);
                SpriteComponent s2 = spritesComponents.get(e2);
                return s1.spriteLayer.compareTo(s2.spriteLayer);
            }
        });
    }

    @Override
    protected void removed(Entity e) {
        sortedEntities.remove(e);
    }

    public void dispose() {

        batch.dispose();
        Iterator<Entity> iterator = sortedEntities.iterator();
        while (iterator.hasNext()) {
            Bag<Component> bag = new Bag<>();
            Entity e = iterator.next();
            e.getComponents(bag);
            for (int i = 0; i < bag.size(); i++) {
                ((BaseComponent) bag.get(i)).dispose();
            }
            iterator.remove();

        }
        sortedEntities.clear();

    }

}
