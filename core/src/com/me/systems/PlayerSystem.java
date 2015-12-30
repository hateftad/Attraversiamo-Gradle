package com.me.systems;

import com.artemis.Aspect;
import com.badlogic.gdx.InputProcessor;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.me.component.*;
import com.me.component.interfaces.TaskEventObserverComponent;
import com.me.events.TaskEvent;
import com.me.ui.InputManager;
import com.me.utils.Converters;

/**
 * Created by hateftadayon on 7/14/15.
 */
public abstract class PlayerSystem extends GameEntityProcessingSystem implements InputProcessor {

    protected final static int left = 0, right = 1, up = 2, down = 3, jump = 4, rag = 5,
            changePlayer = 6, action = 7, skinChange = 8;

    protected boolean m_process = true;
    protected InputManager m_inputMgr;

    public PlayerSystem(Aspect aspect) {
        super(aspect);
    }

    protected void animateBody(PhysicsComponent ps, PlayerComponent player,
                             AnimationComponent animation) {

        int rot = player.isFacingLeft() ? -1 : 1;
        for (Slot slot : animation.getSkeleton().getSlots()) {
            if (!(slot.getAttachment() instanceof RegionAttachment))
                continue;
            String attachment = slot.getBone().getData().getName();
            if (ps.getBody(attachment) != null) {
                float x = (Converters.ToBox(slot.getBone().getWorldX()));
                float x2 = (ps.getBody().getPosition().x + x);
                float y = Converters.ToBox(slot.getBone().getWorldY());
                float y2 = (ps.getBody().getPosition().y + y);
                ps.getBody(attachment).setTransform(x2, animation.getCenter().y + y2, 0);
            }
        }
    }

    protected boolean isDead(PhysicsComponent ps) {

        if (ps.getPosition().y < -100) {
            return true;
        }
        return false;
    }

    public void restartSystem() {
        m_inputMgr.reset();
    }

    public void clearSystem() {
    }

    public void toggleProcessing(boolean process) {
        m_process = process;
    }

    @Override
    protected boolean checkProcessing() {
        // TODO Auto-generated method stub
        return m_process;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
