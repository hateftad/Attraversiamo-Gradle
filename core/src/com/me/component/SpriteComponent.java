package com.me.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.me.utils.Converters;

public class SpriteComponent extends BaseComponent {

	public enum Layer {
		DEFAULT, BACKGROUND3, BACKGROUND2, BACKGROUND1, BACKGROUND0, ACTOR1, ACTOR2, FOREGROUND1, FOREGROUND2, FOREGROUND3, FOREGROUND4,
	}

	private float m_width;

	private float m_height;

	private Vector2 m_position = new Vector2();

	private Vector2 m_scale = new Vector2();

	private float m_rotation;

	private Sprite m_sprite;

	private final Vector2 m_center = new Vector2();

	private final Vector2 m_halfSize = new Vector2();

	private float m_rot;

	private static final Vector2 m_tmp = new Vector2();

	private int m_drawOrder;

	private Texture m_texture;
	
	public boolean m_shouldDraw = true;

	public Layer m_layer = Layer.DEFAULT;

	public SpriteComponent(Texture texture, boolean flip, Body body,
			Color color, Vector2 size, Vector2 center, float rotationInDegrees,
			int drawOrder) {
		m_texture = texture;
		m_sprite = new Sprite(texture);
		size.set(Converters.ToWorld(size));
		center.set(Converters.ToWorld(center));
		initTexture(flip, color, body, size, center, rotationInDegrees);
		setLayer(drawOrder);
	}

	private void initTexture(boolean flip, Color color, Body body,
			Vector2 size, Vector2 center, float rotationInDegrees) {
		Vector2 s = new Vector2(size.x, size.y);
		m_sprite.flip(flip, false);
		m_sprite.setColor(color);
		m_rot = rotationInDegrees;
		m_sprite.setSize(size.x, size.y);
		m_sprite.setOrigin(size.x / 2, size.y / 2);
		m_halfSize.set(size.x / 2, size.y / 2);
		m_center.set(center.x, center.y);

		if (body != null) {
			m_tmp.set(body.getPosition());
			m_sprite.setPosition(m_tmp.x - s.x / 2, m_tmp.y - s.y / 2);

			float angle = body.getAngle() * MathUtils.radiansToDegrees;
			m_tmp.set(m_center).rotate(angle).add(body.getPosition())
					.sub(m_halfSize);
			m_sprite.setRotation(m_rotation + angle);
		} else {
			m_tmp.set(center.x - s.x / 2, center.y - s.y / 2);
			m_sprite.setRotation(rotationInDegrees);
		}

		m_sprite.setPosition(m_tmp.x, m_tmp.y);

	}
	
	public Texture getTexture(){
		return m_texture;
	}

	public void draw(SpriteBatch sb) {
		float angle = m_rotation * MathUtils.radiansToDegrees;
		m_tmp.set(m_center).rotate(angle).add(m_position).sub(m_halfSize);
		m_sprite.setPosition(m_tmp.x, m_tmp.y);
		m_sprite.setRotation(m_rot + angle);
		m_sprite.draw(sb);
	}

	public void setLayer(int layer) {

		switch (layer) {
		case 0:
			m_layer = Layer.BACKGROUND3;
			break;
		case 1:
			m_layer = Layer.BACKGROUND2;
			break;
		case 2:
			m_layer = Layer.BACKGROUND1;
			break;
		case 3:
			m_layer = Layer.BACKGROUND0;
			break;
		case 4:
			m_layer = Layer.ACTOR1;
			break;
		case 5:
			m_layer = Layer.ACTOR2;
			break;
		case 6:
			m_layer = Layer.FOREGROUND1;
			break;
		case 7:
			m_layer = Layer.FOREGROUND2;
			break;
		case 8:
			m_layer = Layer.FOREGROUND3;
			break;
		case 9:
			m_layer = Layer.FOREGROUND4;
			break;
		default:
			m_layer = Layer.DEFAULT;
			break;
		}
	}

	public float getWidth() {
		return m_width;
	}

	public void setWidth(float width) {
		m_width = width;
	}

	public float getHeight() {
		return m_height;
	}

	public void setHeight(float height) {
		m_height = height;
	}

	public void setPosition(Vector2 pos) {
		this.m_position = pos;
	}

	public Vector2 getPosition() {
		return m_position;
	}

	public void setScale(float x, float y) {
		m_scale.set(x, y);
	}

	public void setRotation(float r) {
		this.m_rotation = r;
	}

	public float getRotation() {
		return m_rotation;
	}

	public void setDrawOrder(int order) {
		m_drawOrder = order;
	}

	public int getDrawOrder() {
		return m_drawOrder;
	}

	@Override
	public void dispose() {

		m_sprite.getTexture().dispose();
		m_sprite = null;
		m_texture.dispose();
	}

	public void addTexture(Texture texture, boolean flip, Body body,
			Color color, Vector2 tmp, Vector2 center, float f, int renderOrder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub

	}

}
