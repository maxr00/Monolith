package entity;

import java.awt.Color;

import entity.mob.Mob;
import game.Game;
import level.Level;
import net.PlayerMP;
import player.Player;
import util.Vector2i;

public class Particle_Exp extends Particle{

	private int expAmt, startLife;
	
	private Player target;
	
	private Particle_Exp(int x, int y, int scale, int life, float speed, Color color, Level level, int exp) {
		super(x,y,scale,life,speed,color,level,RenderType.Sprite,255);
		expAmt=exp;
		startLife=life;
		
		Player close = null; double dist=Integer.MAX_VALUE;
		for(Player m : level.players){
			if(level.getDistance(m.vector, new Vector2i((int)xx,(int)yy))<dist){
				close = m;
				dist = level.getDistance(m.vector, new Vector2i((int)xx,(int)yy));
			}
		}
		target=close;
	}
	
	public Particle_Exp(int x, int y, int scale, int life, float speed, int amt, Level level, Color[] colors, int exp) {
		this.x=x;
		this.y=y;
		for (int i = 0; i < amt; i++) {
			particles.add(new Particle_Exp(x, y, scale, life, speed, colors[(int)(Math.random()*colors.length)],level,exp) );
		}
		level.entities.add(this);		
	}
	
	public boolean updateParticle(){
		if(life<0 && infinite){
			int xxx=(int)(xx+xDir*speed), yyy=(int)(yy+yDir*speed)+(int)(zz+zDir*speed);
			if(!level.canMoveOn(xxx/Game.TILE_SIZE,yyy/Game.TILE_SIZE)){
				if(level.getMobOn(xxx/Game.TILE_SIZE, yyy/Game.TILE_SIZE) instanceof Player || level.getMobOn(xxx/Game.TILE_SIZE, yyy/Game.TILE_SIZE) instanceof PlayerMP){
					((Player)level.getMobOn(xxx/Game.TILE_SIZE, yyy/Game.TILE_SIZE)).addExp(expAmt);
					removed=true;
				}
			}
			return false;
		}
		
		life--;
		if(life<0 && !infinite){
			return true;
		}
		
		if(zz<=0){
			zz=0;
			zDir*=-1f;
			xDir*=0.5f;
			yDir*=0.5f;
			if(Math.abs(zDir)<0.2f) zDir=0;
		}else
			zDir-=0.07f;
		
		move((int)(xx+xDir*speed),(int)(yy+yDir*speed)+(int)(zz+zDir*speed));
		
		return false;
	}

	
	private void move(int x,int y){
		if(!level.canMoveOn((int)(xx/Game.TILE_SIZE),(int)((yy-zz)/Game.TILE_SIZE))){
			xDir*=-0.5f;
			yDir*=-0.5f;
			if(level.getMobOn((int)(xx/Game.TILE_SIZE),(int)((yy-zz)/Game.TILE_SIZE)) instanceof Player || level.getMobOn((int)(xx/Game.TILE_SIZE),(int)((yy-zz)/Game.TILE_SIZE)) instanceof PlayerMP){
				((Player)level.getMobOn((int)(xx/Game.TILE_SIZE),(int)((yy-zz)/Game.TILE_SIZE))).addExp(expAmt);
				removed=true;
				return;
			}
		}
		
		if(life>startLife-30){
			xx+=xDir*speed;
			yy+=yDir*speed;
			zz+=zDir*speed;
			this.x=(int)xx;
			this.y=(int)(yy-zz);
		}else{
			if(target!=null){
				double desired=Math.atan2(target.y+Game.TILE_SIZE/2-yy, target.x+Game.TILE_SIZE/2-xx);
				double desX=Math.cos(desired);
				double desY=Math.sin(desired);
				xx+=desX*speed;
				yy+=desY*speed;
				zz=0;//+=zDir*speed;
				this.x=(int)xx;
				this.y=(int)(yy-zz);
			}
		}
	}
	
}
