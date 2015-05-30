package com.me.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.me.component.AnimationComponent.AnimState;
import com.me.tasks.OpenDoorTask;
import com.me.tasks.ReachEndTask;
import com.me.tasks.Task;

public class PlayerConfig {
	
	public Vector2 m_playerPosition;
 	public boolean m_facingleft;
 	public boolean m_active;
 	public boolean m_canDeactivate;
 	public String m_name;
 	public AnimState m_finishAnimation;
 	public float minimumY;
 	private String m_skinName;
	private Array<Task> m_tasks = new Array<Task>();
 	
 	public PlayerConfig(){
 		m_playerPosition = new Vector2();
 	}
 	
 	public String getName(){
 		return m_name;
 	}

	public void addTask(String task){
		if(task.equalsIgnoreCase("OpenDoor")){
			m_tasks.add(new OpenDoorTask());
		} else if(task.equalsIgnoreCase("TouchEnd")){
			m_tasks.add(new ReachEndTask());
		}
	}

 	public Array<Task> getTasks(){
		return m_tasks;
	}

    public void resetTasks(){
        for(Task task : m_tasks){
            task.reset();
        }
    }

 	public void setFinishAnimation(String state){
 		
 		if(state.equals("suckin")){
 			m_finishAnimation = AnimState.SUCKIN;
 		}
 		if(state.equals("walkout")){
 			m_finishAnimation = AnimState.WALKOUT;
 		}
 		if(state.equals("runOut")){
 			m_finishAnimation = AnimState.RUNOUT;
 		}
 	}
 	
	public void setSkinName(String name){
		m_skinName = name;
	}
	
	public String getSkinName(){
		return m_skinName;
	}
}
