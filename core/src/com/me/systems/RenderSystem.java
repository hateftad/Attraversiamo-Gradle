package com.me.systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.component.AnimationComponent;
import com.me.component.BaseComponent;
import com.me.component.ParticleComponent;
import com.me.component.PhysicsComponent;
import com.me.component.ShaderComponent;
import com.me.component.SpriteComponent;

public class RenderSystem extends EntitySystem {

	@Mapper
	ComponentMapper<PhysicsComponent> m_physics;

	@Mapper
	ComponentMapper<SpriteComponent> m_sprites;

	@Mapper
	ComponentMapper<AnimationComponent> m_animation;

	@Mapper
	ComponentMapper<ParticleComponent> m_particles;

	@Mapper
	ComponentMapper<ShaderComponent> m_shaderComps;

	private List<Entity> m_sortedEntities;

	private SpriteBatch m_batch;

	@SuppressWarnings("unchecked")
	public RenderSystem() {

		super(Aspect.getAspectForAll(PhysicsComponent.class,
				SpriteComponent.class));
		// m_camera = camera;
	}

	@Override
	protected void initialize() {
		m_batch = new SpriteBatch();
		m_sortedEntities = new ArrayList<Entity>();
	}

	@Override
	protected boolean checkProcessing() {

		return true;
	}

	@Override
	protected void begin() {
		m_batch.setProjectionMatrix(world.getSystem(CameraSystem.class)
				.getCameraComponent().getCamera().combined);
		// m_batch.begin();

	}

	@Override
	protected void end() {
		// m_batch.end();
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; m_sortedEntities.size() > i; i++) {
			process(m_sortedEntities.get(i));
		}

	}

	protected void process(Entity e) {
		m_batch.begin();

		if (m_physics.has(e)) {
			PhysicsComponent physics = m_physics.getSafe(e);
			physics.updateWorldPosition();
			if (m_sprites.has(e)) {

				SpriteComponent sprite = m_sprites.get(e);
				sprite.setPosition(physics.getWorldPosition());
				sprite.setRotation(physics.getBody().getAngle());
				if (m_animation.has(e)) {
					AnimationComponent anim = m_animation.get(e);
					anim.setPosition(physics.getPosition().x,
							physics.getPosition().y);
					anim.update(m_batch, world.delta);
				} else {
					sprite.draw(m_batch);

				}

			}

			if (m_particles.has(e)) {
				ParticleComponent p = m_particles.get(e);
				p.setPosition(physics.getWorldPosition().x,
						physics.getWorldPosition().y);
				p.draw(m_batch, world.delta);
				if (p.isCompleted()) {
					p.setStart(false);
				}
			}
		}
		m_batch.end();
		/*
		 * if(m_shaderComps.has(e)){ ShaderComponent sComp =
		 * m_shaderComps.get(e); sComp.render(m_batch,
		 * world.getSystem(CameraSystem.class).getCameraComponent().getCamera(),
		 * m_sprites.get(e)); }
		 */
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
		for (Entity e : m_sortedEntities) {
			Bag<Component> bag = new Bag<Component>();
			e.getComponents(bag);
			for (int i = 0; i < bag.size(); i++) {
				((BaseComponent) bag.get(i)).dispose();
			}
			e.deleteFromWorld();
		}
		m_sortedEntities.clear();

	}

}
