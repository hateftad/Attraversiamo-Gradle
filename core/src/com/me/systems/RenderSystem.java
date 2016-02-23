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
	ComponentMapper<PhysicsComponent> m_physics;

	@Mapper
	ComponentMapper<SpriteComponent> m_sprites;

	@Mapper
	ComponentMapper<PlayerAnimationComponent> m_playerAnimation;


    @Mapper
    ComponentMapper<LevelAnimationComponent> m_levelAnimation;

	@Mapper
	ComponentMapper<SingleParticleComponent> m_particles;

	@Mapper
	ComponentMapper<ContinuousParticles> m_continuousParticles;

    @Mapper
    ComponentMapper<EventParticleComponent> m_eventParticles;

	@Mapper
	ComponentMapper<ShaderComponent> m_shaderComps;

	private List<Entity> m_sortedEntities;

	private SpriteBatch m_batch;

    private OrthographicCamera m_camera;

	@SuppressWarnings("unchecked")
	public RenderSystem(OrthographicCamera camera) {

		super(Aspect.getAspectForAll(PhysicsComponent.class,
				SpriteComponent.class));
		m_camera = camera;
	}

	@Override
	protected void initialize() {
		m_batch = new SpriteBatch();
		m_sortedEntities = new ArrayList<>();
	}

	@Override
	protected boolean checkProcessing() {

		return true;
	}

	@Override
	protected void begin() {
		m_batch.setProjectionMatrix(m_camera.combined);
		// m_batch.begin();

	}

	@Override
	protected void end() {
		// m_batch.end();
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (Entity sortedEntity : m_sortedEntities) {
			process(sortedEntity);
		}
	}

	protected void process(Entity e) {
		m_batch.begin();

		if (m_physics.has(e)) {

			if (m_continuousParticles.has(e)) {
				ContinuousParticles particles = m_continuousParticles.get(e);
				if (particles.needsDrawAndUpdate()) {
					particles.draw(m_batch, world.delta);
				}
			}

            if (m_eventParticles.has(e)) {
                EventParticleComponent particles = m_eventParticles.get(e);
                if (particles.needsDrawAndUpdate()) {
                    particles.draw(m_batch, world.delta);
                }
            }

			PhysicsComponent physics = m_physics.getSafe(e);
			physics.updateWorldPosition();
			if (m_sprites.has(e)) {

				SpriteComponent sprite = m_sprites.get(e);
				sprite.setPosition(physics.getWorldPosition());
				sprite.setRotation(physics.getBody().getAngle());
				if (m_playerAnimation.has(e)) {
					PlayerAnimationComponent anim = m_playerAnimation.get(e);
					anim.setPosition(physics.getPosition());
					anim.update(m_batch, world.delta);
                } else {
					if(sprite.m_shouldDraw){
						sprite.draw(m_batch);
					}
				}
                if(m_levelAnimation.has(e)) {
                    LevelAnimationComponent anim = m_levelAnimation.get(e);
                    anim.setPosition(physics.getPosition());
                    anim.update(m_batch, world.delta);
                }


			}


			if (m_particles.has(e)) {
				ParticleComponent particles = m_particles.get(e);
                particles.setPosition(physics.getWorldPosition());
				if(particles.needsDrawAndUpdate()) {
					particles.draw(m_batch, world.delta);
				}
			}

		}
		m_batch.end();

		if (m_shaderComps.has(e)) {
			ShaderComponent sComp = m_shaderComps.get(e);
			sComp.render(m_batch, m_camera, m_sprites.get(e));
		}

	}

	@Override
	protected void inserted(Entity e) {

		m_sortedEntities.add(e);
		Collections.sort(m_sortedEntities, new Comparator<Entity>() {
			@Override
			public int compare(Entity e1, Entity e2) {
				SpriteComponent s1 = m_sprites.get(e1);
				SpriteComponent s2 = m_sprites.get(e2);
				return s1.m_layer.compareTo(s2.m_layer);

			}
		});

	}

	@Override
	protected void removed(Entity e) {
		m_sortedEntities.remove(e);
	}

	public void dispose() {

		m_batch.dispose();
        Iterator<Entity> iterator = m_sortedEntities.iterator();
		while(iterator.hasNext()){
			Bag<Component> bag = new Bag<>();
			Entity e = iterator.next();
            e.getComponents(bag);
			for (int i = 0; i < bag.size(); i++) {
				((BaseComponent) bag.get(i)).dispose();
			}
			iterator.remove();

		}
		m_sortedEntities.clear();

	}

}
