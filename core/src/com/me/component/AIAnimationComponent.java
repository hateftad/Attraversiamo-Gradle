package com.me.component;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.Skeleton;
import com.me.events.AnimationEvent;
import com.me.events.TaskEvent;
import com.me.events.states.PlayerState;

/**
 * Created by hateftadayon on 10/15/16.
 */
public class AIAnimationComponent extends AnimationComponent {


    public AIAnimationComponent(String atlas, String skeleton, float scale) {
        super(atlas, skeleton, scale);
    }

    @Override
    public void skeletonReady(Skeleton skeleton) {
    }

    public AnimationEvent getEvent() {
        return event;
    }

    @Override
    public void setAnimationState(PlayerState state) {

        if (state != previousState) {
            System.out.println(getState());
            setState(state);
            switch (state) {
                case Walking:
                    playAnimation(0, "walking", true);
                    break;
                case Jogging:
                    playAnimation(0, "jogging", true);
                    break;
                case Running:
                    playAnimation(0, "running", true);
                    break;
                case Jumping:
                    playAnimation(0, "runJumping", false);
                    break;
                case JogJump:
                    playAnimation(0, "jogjumping", false);
                    break;
                case Idle:
                    playAnimation(0, "idle1", true);
                    break;
                case UpJump:
                    playAnimation(0, "upJump", false);
                    break;
                case Falling:
                    playAnimation(0, "falling", true);
                    break;
                case RunFalling:
                    playAnimation(0, "runFalling", true);
                    break;
                case Hanging:
                    playAnimation(0, "wallHang", true);
                    break;
                case ClimbingLedge:
                    playAnimation(0, "climbUp", false);
                    break;
                case LadderHang:
                    playAnimation(0, "ladderHang", false);
                    break;
                case Pushing:
                    playAnimation(0, "pushing", true);
                    break;
                case LieDown:
                    playAnimation(0, "lieDown", false);
                    break;
                case LyingDown:
                    playAnimation(0, "lyingDown", false);
                    break;
                case PullUp:
                    playAnimation(0, "pullUp", false);
                    break;
                case StandUp:
                    playAnimation(0, "standUp", false);
                    break;
                case Crawl:
                    playAnimation(0, "crawling", true);
                    break;
                case RunOut:
                    playAnimation(0, "runOut", false);
                    break;
                case Drowning:
                    playAnimation(0, "drowning", false);
                    break;
                case Landing:
                    playAnimation(0, "landing", false);
                    break;
                case RunLanding:
                    playAnimation(0, "runLanding", false);
                    break;
                default:
                    break;
            }
            skeleton.setToSetupPose();
        }
        previousState = state;
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

    }
}