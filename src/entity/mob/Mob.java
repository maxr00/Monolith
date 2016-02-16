package entity.mob;

import java.awt.Color;

import entity.Entity;
import entity.Particle;
import entity.Particle_Exp;
import entity.Projectile;
import game.Game;
import graphics.Screen;
import graphics.Sprite;

public abstract class Mob extends Entity {
	
	protected Sprite[][] sprites;
	public char[][] characters;
	public float updateRange=125*Game.scale;
	public int Health;
	public int color;
	public boolean lockedOnto;
	
	public boolean hasBeenSeen, isSeen;
	
	public Projectile.Spell[] spells;
	
	public String identifier, name;
	
	protected boolean[][] takenPos;
	
	public void move(int deltaX, int deltaY) {
		boolean canMoveX=false, canMoveY=false;
		for(int tx=0;tx<takenPos.length;tx++){
			for(int ty=0;ty<takenPos[tx].length;ty++){
				if(takenPos[tx][ty]){
					int xp=tx*Game.TILE_SIZE, yp=tx*Game.TILE_SIZE;
					if(level.canMoveOn((xp + x + deltaX)/Game.TILE_SIZE, (yp + y + deltaY)/Game.TILE_SIZE)){
						canMoveX=true; canMoveY=true;
					}else if(level.canMoveOn((xp + x + deltaX)/Game.TILE_SIZE, (yp + y)/Game.TILE_SIZE)){
						canMoveX=true;
					}else if(level.canMoveOn((xp + x)/Game.TILE_SIZE, (yp + y + deltaY)/Game.TILE_SIZE)){
						canMoveY=true;
					}else{
						canMoveX=false;
						canMoveY=false;
					}
				}
			}
		}
		if(!canMoveX && !canMoveY)
			return;
		/*for(int tx=0;tx<takenPos.length;tx++){
			for(int ty=0;ty<takenPos[tx].length;ty++){
				if(takenPos[tx][ty]){
					int xp=tx*Game.TILE_SIZE, yp=ty*Game.TILE_SIZE;
					if(level.getTile((xp+x)/Game.TILE_SIZE, (yp+y)/Game.TILE_SIZE)!=null)
						level.getTile((xp+x)/Game.TILE_SIZE, (yp+y)/Game.TILE_SIZE).doRender(true);
				}
			}
		}*/
		if(canMoveX){
			x+=deltaX;
			vector.setX(x);
		}
		if(canMoveY){
			y+=deltaY;
			vector.setY(y);
		}
		/*
		for(int tx=0;tx<takenPos.length;tx++){
			for(int ty=0;ty<takenPos[tx].length;ty++){
				if(takenPos[tx][ty]){
					int xp=tx*Game.TILE_SIZE, yp=ty*Game.TILE_SIZE;
					if(level.getTile((xp+x)/Game.TILE_SIZE, (yp+y)/Game.TILE_SIZE)!=null)
						level.getTile((xp+x)/Game.TILE_SIZE, (yp+y)/Game.TILE_SIZE).doRender(false);
				}
			}
		}*/
	}
	
	public void moveTo(int x, int y) {
		this.move(x-this.x, y-this.y);
	}
		
	public boolean isOnTile(int tx,int ty){
		for(int x=0;x<takenPos.length;x++){
			for(int y=0;y<takenPos[x].length;y++){
				if(takenPos[x][y] && tx==(this.x/Game.TILE_SIZE)+x && ty==(this.y/Game.TILE_SIZE)+y)
					return true;
			}
		}
		return false;//(x/Game.TILE_SIZE==tx && y/Game.TILE_SIZE==ty);
	}

	int particlesPerDamage=400;
	public void damage(int damage, float xDir, float yDir){
		Health-=damage;
		if(Health<=0){
			removed=true;
			for(int tx=0;tx<takenPos.length;tx++){
				for(int ty=0;ty<takenPos[tx].length;ty++){
					if(takenPos[tx][ty]){
						int xp=tx*Game.TILE_SIZE, yp=ty*Game.TILE_SIZE;
						if(level.getTile((xp+x)/Game.TILE_SIZE, (yp+y)/Game.TILE_SIZE)!=null)
							level.getTile((xp+x)/Game.TILE_SIZE, (yp+y)/Game.TILE_SIZE).doRender(true);
					}
				}
			}
			new Particle(x + Game.TILE_SIZE/2 + (int)(xDir*Game.TILE_SIZE),y + Game.TILE_SIZE/2 + (int)(yDir*Game.TILE_SIZE),1,-600,0.3f,particlesPerDamage*damage*2,level,new Color[]{Color.red,new Color(150,0,0)},Particle.RenderType.Additive,150);
			new Particle(x + Game.TILE_SIZE/2 + (int)(xDir*Game.TILE_SIZE),y + Game.TILE_SIZE/2 + (int)(yDir*Game.TILE_SIZE),2,-1200,0.1f,5*((takenPos.length+takenPos[0].length)/2),level,new Color[]{Color.lightGray},Particle.RenderType.Sprite);

			new Particle_Exp(x + Game.TILE_SIZE/2 + (int)(xDir*Game.TILE_SIZE),y + Game.TILE_SIZE/2 + (int)(yDir*Game.TILE_SIZE),1,1200,0.5f,10,level,new Color[]{Color.yellow},1);
		}else
			new Particle(x + Game.TILE_SIZE/2 + (int)(xDir*Game.TILE_SIZE),y + Game.TILE_SIZE/2 + (int)(yDir*Game.TILE_SIZE),1,600,0.1f,particlesPerDamage*damage,level,new Color[]{Color.red,new Color(150,0,0)},Particle.RenderType.Additive,150);
	}
	
	public void update(){
		
	}
	
	public void render(Screen screen){
		
	}
	
	public String getStatus(){
		return name +".  HP:" +Health +"  ON TILE "+x/Game.TILE_SIZE +","+y/Game.TILE_SIZE;
	}
}
