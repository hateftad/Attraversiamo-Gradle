package com.me.component;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.Slot;
import com.me.event.GameEvent;
import com.me.event.GameEventType;
import com.me.utils.Converters;

/**
 * Created by hateftadayon on 7/13/15.
 */
public class PlayerAnimationComponent extends AnimationComponent {

    private AnimState m_finishAnimation;

    public PlayerAnimationComponent(String atlas, String skeleton, float scale, AnimState finishAnimation) {
        super(atlas, skeleton, scale);
        m_finishAnimation = finishAnimation;
    }

    @Override
    public void setAnimationState(AnimState state) {

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

    @Override
    public void update(SpriteBatch sb, float dt) {
        m_animationState.update(dt);
        m_animationState.apply(m_skeleton);
        m_skeleton.update(dt);
        m_skeleton.updateWorldTransform();
        m_renderer.draw(sb, m_skeleton);
    }

    @Override
    public void onNotify(GameEventType event) {
        if(event == GameEventType.AllReachedEnd){
            setAnimationState(m_finishAnimation);
        }
    }
}
