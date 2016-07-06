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

    private float width;

    private float height;

    private Vector2 position = new Vector2();

    private Vector2 scale = new Vector2();

    private float rotation;

    private Sprite sprite;

    private final Vector2 center = new Vector2();

    private final Vector2 halfSize = new Vector2();

    private float rot;

    private static final Vector2 tmp = new Vector2();

    private int drawOrder;

    private Texture texture;

    public boolean shouldDraw = true;

    public Layer spriteLayer = Layer.DEFAULT;

    public SpriteComponent(Texture texture, boolean flip, Body body,
                           Color color, Vector2 size, Vector2 center, float rotationInDegrees,
                           int drawOrder) {
        this.texture = texture;
        this.sprite = new Sprite(texture);
        size.set(Converters.ToWorld(size));
        center.set(Converters.ToWorld(center));
        initTexture(flip, color, body, size, center, rotationInDegrees);
        setLayer(drawOrder);
    }

    private void initTexture(boolean flip, Color color, Body body,
                             Vector2 size, Vector2 center, float rotationInDegrees) {
        Vector2 s = new Vector2(size.x, size.y);
        this.sprite.flip(flip, false);
        this.sprite.setColor(color);
        this.rot = rotationInDegrees;
        this.sprite.setSize(size.x, size.y);
        this.sprite.setOrigin(size.x / 2, size.y / 2);
        this.halfSize.set(size.x / 2, size.y / 2);
        this.center.set(center.x, center.y);

        if (body != null) {
            tmp.set(body.getPosition());
            this.sprite.setPosition(tmp.x - s.x / 2, tmp.y - s.y / 2);

            float angle = body.getAngle() * MathUtils.radiansToDegrees;
            tmp.set(center).rotate(angle).add(body.getPosition())
                    .sub(halfSize);
            this.sprite.setRotation(rotation + angle);
        } else {
            tmp.set(center.x - s.x / 2, center.y - s.y / 2);
            this.sprite.setRotation(rotationInDegrees);
        }
        this.sprite.setPosition(tmp.x, tmp.y);
    }

    public Texture getTexture() {
        return texture;
    }

    public void draw(SpriteBatch sb) {
        float angle = rotation * MathUtils.radiansToDegrees;
        tmp.set(center).rotate(angle).add(position).sub(halfSize);
        sprite.setPosition(tmp.x, tmp.y);
        sprite.setRotation(rot + angle);
        sprite.draw(sb);
    }

    public void setLayer(int layer) {

        switch (layer) {
            case 0:
                spriteLayer = Layer.BACKGROUND3;
                break;
            case 1:
                spriteLayer = Layer.BACKGROUND2;
                break;
            case 2:
                spriteLayer = Layer.BACKGROUND1;
                break;
            case 3:
                spriteLayer = Layer.BACKGROUND0;
                break;
            case 4:
                spriteLayer = Layer.ACTOR1;
                break;
            case 5:
                spriteLayer = Layer.ACTOR2;
                break;
            case 6:
                spriteLayer = Layer.FOREGROUND1;
                break;
            case 7:
                spriteLayer = Layer.FOREGROUND2;
                break;
            case 8:
                spriteLayer = Layer.FOREGROUND3;
                break;
            case 9:
                spriteLayer = Layer.FOREGROUND4;
                break;
            default:
                spriteLayer = Layer.DEFAULT;
                break;
        }
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setPosition(Vector2 pos) {
        this.position = pos;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setScale(float x, float y) {
        scale.set(x, y);
    }

    public void setRotation(float r) {
        this.rotation = r;
    }

    public float getRotation() {
        return rotation;
    }

    public void setDrawOrder(int order) {
        drawOrder = order;
    }

    public int getDrawOrder() {
        return drawOrder;
    }

    @Override
    public void dispose() {

        sprite.getTexture().dispose();
        sprite = null;
        texture.dispose();
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
