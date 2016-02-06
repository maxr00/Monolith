package entity;

import java.util.Random;

import graphics.Screen;
import level.Level;
import util.Vector2i;

public abstract class Entity {

	public int x, y;
	public Vector2i vector = new Vector2i(0,0);
	protected boolean removed = false;
	protected final Random random=new Random();

	public Level level;
	
	public void update(){
		
	}
	
	public void render(Screen screen){
		
	}
	
	public void remove(){
		removed = true;
	}
	
	public boolean isRemoved(){ return removed; }
	
	//Method offset for updates _   void offsetForUpdates(int xOffset, int yOffset, int updates)
}
