package com.me.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.badlogic.gdx.InputProcessor;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.me.component.*;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;
import com.me.events.TelegramEvent;
import com.me.events.states.PlayerState;
import com.me.ui.InputManager;
import com.me.utils.Converters;

/**
 * Created by hateftadayon on 7/14/15.
 */
public abstract class PlayerSystem extends GameEntityProcessingSystem implements InputProcessor {

    protected final static int left = 0, right = 1, up = 2, down = 3, jump = 4, rag = 5,
            changePlayer = 6, action = 7, skinChange = 8;

    protected boolean process = true;
    protected InputManager inputMgr;

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

    protected void checkFinished(TouchComponent touch, PlayerComponent player, FeetComponent feetComponent) {
        if(touch.insideFinish){
            if(feetComponent.hasCollided() && !player.isFinishing() && !player.isFinished()){
                System.out.println("finished");
                player.setFinished(true);
                notifyObservers(new TaskEvent(GameEventType.InsideFinishArea, player.getPlayerNr()));
            }
        }
    }

    protected abstract void setPlayerState(Entity entity, PlayerState state);

    protected boolean isDead(PhysicsComponent ps) {
        return ps.getPosition().y < -100;
    }

    public void restartSystem() {
        inputMgr.reset();
    }

    public void toggleProcessing(boolean process) {
        this.process = process;
    }

    @Override
    protected boolean checkProcessing() {
        // TODO Auto-generated method stub
        return process;
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
