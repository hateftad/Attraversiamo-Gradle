package com.me.component;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.IkConstraint;
import com.esotericsoftware.spine.Slot;
import com.me.component.interfaces.TelegramEventObserverComponent;
import com.me.events.AnimationEvent;
import com.me.events.TelegramEvent;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;
import com.me.events.states.PlayerState;
import com.me.utils.Converters;

/**
 * Created by hateftadayon on 7/13/15.
 */
public class PlayerAnimationComponent extends AnimationComponent implements TelegramEventObserverComponent {

    private PlayerState finishAnimation;
    private PlayerComponent.PlayerNumber playerNumber;

    public PlayerAnimationComponent(String atlas, String skeleton, float scale, PlayerState finishAnimation, PlayerComponent.PlayerNumber playerNumber) {
        super(atlas, skeleton, scale);
        this.finishAnimation = finishAnimation;
        this.playerNumber = playerNumber;
    }

    public PlayerAnimationComponent(String atlas, String skeleton, float scale) {
        super(atlas, skeleton, scale);
    }

    public AnimationEvent getEvent() {
        return event;
    }

    @Override
    public void setAnimationState(PlayerState state) {

        if (state != previousState) {
            System.out.println("Animation State " + state);
            setState(state);
            switch (state) {
                case Walking:
                    playAnimation("walking", true);
                    break;
                case Jogging:
                    playAnimation("jogging", true);
                    break;
                case Running:
                    playAnimation("running", true);
                    break;
                case Jumping:
                    playAnimation("runJumping", false);
                    break;
                case JogJump:
                    playAnimation("jogjumping", false);
                    break;
                case Idle:
                    playAnimation("idle1", true);
                    break;
                case UpJump:
                    playAnimation("upJump", false);
                    break;
                case Falling:
                    playAnimation("falling", true);
                    break;
                case RunFalling:
                    playAnimation("runFalling", true);
                    break;
                case Hanging:
                    playAnimation("hang", true);
                    break;
                case ClimbingLedge:
                    playAnimation("climbUp", false);
                    break;
                case LadderClimbUp:
                    playAnimation("ladderClimbUp", true);
                    break;
                case LadderClimbDown:
                    playAnimation("ladderClimbDown", true);
                    break;
                case LadderHang:
                    playAnimation("ladderHang", false);
                    break;
                case Pushing:
                    playAnimation("pushing", true);
                    break;
                case LieDown:
                    playAnimation("lieDown", false);
                    break;
                case LyingDown:
                    playAnimation("lyingDown", false);
                    break;
                case PullUp:
                    playAnimation("pullUp", false);
                    break;
                case SuckIn:
                    playAnimation("suckIn", false);
                    break;
                case StandUp:
                    playAnimation("standUp", false);
                    break;
                case Crawl:
                    playAnimation("crawling", true);
                    break;
                case PressButton:
                    playAnimation("pressButton", false);
                    break;
                case RunOut:
                    playAnimation("runOut", false);
                    break;
                case HoldHandLeading:
//                    playAnimation("holdingHandsIdleLeading", false);
                    break;
                case HoldHandFollowing:
//                    playAnimation("holdingHandsIdleFollowing", false);
                    break;
                case PullingLedge:
                    playAnimation("pullingLedge", false);
                    break;
                case Swinging:
                    playAnimation("swinging", false);
                    break;
                case HoldingCage:
                    playAnimation("holdingCage", false);
                    break;
                case Drowning:
                    playAnimation("drowning", false);
                    break;
                case ClimbBox:
                    playAnimation("boxClimb", false);
                    break;
                case Landing:
                    playAnimation("landing", false);
                    break;
                case RunLanding:
                    playAnimation("runLanding", false);
                    break;
                default:
                    break;
            }
            skeleton.setToSetupPose();
        }
        previousState = state;
    }

    public void setIK(Vector2 position) {

        for (IkConstraint constraint : skeleton.getIkConstraints()) {
            constraint.setMix(100);
            Bone target = constraint.getTarget();
            target.setX(position.x);
            target.setY(position.y);
            target.getData().setX(position.x);
            target.getData().setY(position.y);
            target.updateWorldTransform();
            constraint.apply();
        }
        skeleton.updateCache();
        skeleton.updateWorldTransform();
    }

    public void rotateBoneTo(String name, Vector2 myPos, Vector2 target, boolean left) {
        Bone b = skeleton.findBone(name);
        Vector3 bonePos = new Vector3(myPos.x, myPos.y, 0);
        Vector3 targetDir = bonePos.sub(new Vector3(target.x, target.y, 0));
        double angle = Math.atan2(targetDir.y, targetDir.x);
        angle = angle * (180 / Math.PI);
        b.setRotation(left ? (float) -angle : (float) angle);

    }

    public Vector2 setBonePosition(String name, Vector2 position) {
        Bone b = skeleton.findBone(name);

        return new Vector2(b.getX(), b.getY());
    }

    public Vector2 getPositionRelative(String attachmentName) {
        Slot slot;
        if (!skeleton.getSkin().getName().equals("color")) {
            attachmentName = "silhouette/" + attachmentName;
        }
        for (Slot s : skeleton.getSlots()) {
            if (s.getAttachment() != null) {
                if (s.getAttachment().getName().equals(attachmentName)) {
                    slot = s;
                    return new Vector2(Converters.ToBox(skeleton.getX() + slot.getBone().getWorldX()), Converters.ToBox(skeleton.getY() + slot.getBone().getWorldY()));
                }
            }
        }

        return Vector2.Zero;
    }

    @Override
    public void update(SpriteBatch sb, float dt) {
        animationState.update(dt);
        animationState.apply(skeleton);
        skeleton.update(dt);
        skeleton.updateWorldTransform();
        renderer.draw(sb, skeleton);
    }

    @Override
    public void onNotify(TaskEvent event) {
        if (event.getEventType() == GameEventType.AllReachedEnd) {
            setAnimationState(finishAnimation);
        } else if (event.getEventType() == GameEventType.ColorSkin && event.getPlayerNr() == playerNumber) {
            skeleton.setSkin(ColorSkin);
        } else if (event.getEventType() == GameEventType.BlackSkin && event.getPlayerNr() == playerNumber) {
            skeleton.setSkin(BlackSkin);
        }
    }

    @Override
    public void onNotify(TelegramEvent event) {
        Entity entity = event.getEntity();
        PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
        if (playerNumber != playerComponent.getPlayerNr()) {
            if (event.getEventType() == GameEventType.HoldingHandsFollowing) {
                setAnimationState(PlayerState.HoldHandLeading);
            } else if (event.getEventType() == GameEventType.HoldingHandsLeading) {
                setAnimationState(PlayerState.HoldHandFollowing);
            }
        }
    }
}
