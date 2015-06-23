package com.me.component;

import com.badlogic.gdx.graphics.Color;
import com.me.utils.Converters;

import box2dLight.Light;

public class LightComponent extends BaseComponent {
	
	private Light m_light;
	private String m_name;
	public LightComponent(Light l, String name){
		setName(name);
		setLight(l);
	}
	
	public void setPosition(float x, float y){
		m_light.setPosition(x,y);
	}
	
	public void setColor(Color c){
		m_light.setColor(c);
	}
	
	public void setColor(String c){
		m_light.setColor(Converters.getColor(c));
	}
	
	public void setActive(boolean active){
		m_light.setActive(active);
	}
	
	public void setAlpha(float a){
		m_light.setColor(m_light.getColor().r, m_light.getColor().g, m_light.getColor().b, a);
	}
	
	public float getAlpha(){
		return m_light.getColor().a;
	}
	
	public boolean isActive(){
		return m_light.isActive();
	}
	
	public void setLight(Light l)
	{
		m_light = l;
	}
	
	public Light getLight()
	{
		return m_light;
	}

	public String getName() {
		return m_name;
	}

	public void setName(String m_name) {
		this.m_name = m_name;
	}

	@Override
	public void dispose() {
		
		m_light.remove();
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}
}
