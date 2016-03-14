package entity;

import java.awt.Color;

import entity.mob.BasicEnemy;
import entity.mob.Mob;
import game.Game;
import graphics.Screen;
import level.Level;
import player.Spell;

public class Projectile extends Entity{

	final private int xOrigin, yOrigin;
	private float xDir, yDir, px,py;
	private Spell spell;
	private float speed=1;
	private float life=1.5f;
	private int lifeTime=(int)(life*60);
	private int damage = 1;
	private Level level;
	private Mob target;
	private boolean reflected;
	
	public String sourceID;
		
	public Projectile(int x,int y, float xDir, float yDir, Spell spell, float damagePercent, Mob target, Level level, String source){
		//this.creator = creator;
		this.xDir = xDir;
		this.yDir = yDir;
		
		xOrigin = x ;//+ (int)(xDir*Game.TILE_SIZE);
		yOrigin = y ;//+ (int)(yDir*Game.TILE_SIZE);
		
		px=this.x=xOrigin;
		py=this.y=yOrigin;
		this.spell = spell;
		this.speed=spell.speed;
		this.damage=(int)(spell.damage*damagePercent);
		this.life=spell.life;
		this.lifeTime=(int)(life*60);
		this.target=target;
		
		this.sourceID=source;
		
		this.level=level;
		level.entities.add(this);
	}

	private int lifeCount=0;
	public void update(){
		lifeCount++;
		if(lifeCount>lifeTime){
			removed=true;
			new Particle((int)px,(int)py,1,120,0.15f,25,level,new Color[]{Color.gray,Color.lightGray},Particle.RenderType.Sprite);
			return;
		}
		move();
		this.x=(int)px;
		this.y=(int)py;
		//Check corners for collision
		for(int cy=0;cy<Game.TILE_SIZE;cy+=Game.TILE_SIZE-1){
			for(int cx=0;cx<Game.TILE_SIZE;cx+=Game.TILE_SIZE-1){
				if(xOrigin/Game.TILE_SIZE==(int)((px+cx)/Game.TILE_SIZE) && yOrigin/Game.TILE_SIZE==(int)((py+cy)/Game.TILE_SIZE)){
					continue;
				}
				if( !level.canMoveOn((int)((px+cx)/Game.TILE_SIZE), (int)((py+cy)/Game.TILE_SIZE)) ){ //Hit wall or mob   (!creator.isOnTile(x/Game.TILE_SIZE, y/Game.TILE_SIZE)) && 
					if(level.getTile((int)((px+cx)/Game.TILE_SIZE), (int)((py+cy)/Game.TILE_SIZE)).isSolid()){
						new Particle((int)px,(int)py,1,120,1,100,level,new Color[]{new Color(level.getTile((int)((px+cx)/Game.TILE_SIZE), (int)((py+cy)/Game.TILE_SIZE)).tint)},Particle.RenderType.Sprite);
					}else{//Hit mob
						level.getMobOn((int)((px+cx)/Game.TILE_SIZE),(int)((py+cy)/Game.TILE_SIZE)).damage(damage,-xDir,-yDir);
						//new Particle(x,y,1,600,0.1f,2000,level,new Color[]{Color.red,new Color(150,0,0)},Particle.RenderType.Additive,150);
					}
					removed=true;
				}else{
					Entity e = level.getEntityOn((int)((px+cx)/Game.TILE_SIZE),(int)((py+cy)/Game.TILE_SIZE));
					if(e!=null && e!=this && e instanceof Projectile && !((Projectile)e).sourceID.equals(sourceID) && ((Projectile)e).target != target){
						((Projectile) e).hitProjectile();
						hitProjectile();
					}
				}
			}
		}
		
		if(level.getTile(this.x/Game.TILE_SIZE, this.y/Game.TILE_SIZE+1).reflectMobs)
			reflected=true;
		else
			reflected=false;
	}
	
	private void hitProjectile() {
		new Particle(x + Game.TILE_SIZE/2 + (int)(xDir*Game.TILE_SIZE),y + Game.TILE_SIZE/2 + (int)(yDir*Game.TILE_SIZE),1,45,0.5f,damage*5,level,new Color[]{spell.color},Particle.RenderType.Sprite,150);
		removed=true;
	}

	final float correctionAmt=0.09f;
	int corrCount, corrTime=3;
	public void move(){
		corrCount++;
		if(target!=null && corrCount>=corrTime){
			corrCount=0;
			
			//Find desired angle then move towards it
			double desired=Math.atan2(target.y-py, target.x-px);
			double desX=Math.cos(desired);
			double desY=Math.sin(desired);
			
			if(xDir<desX)
				xDir+=correctionAmt;
			else if(xDir>desX)
				xDir-=correctionAmt;
			if(yDir<desY)
				yDir+=correctionAmt;
			else if(yDir>desY)
				yDir-=correctionAmt;
			
			xDir= xDir>1 ? 1 : xDir;
			xDir= xDir<-1 ? -1 : xDir;
			yDir= yDir>1 ? 1 : yDir;
			yDir= yDir<-1 ? -1 : yDir;
		}
		
		px+=xDir * speed;
		py+=yDir * speed;
		
	}
	
	public void render(Screen screen){
		if(render){
			screen.renderProjectile((int)px, (int)py, spell.sprite, spell.color.getRGB());
			screen.renderGlow(x, y, Game.TILE_SIZE, Game.TILE_SIZE, spell.color, 15, 50);
			
			if(reflected){
				screen.renderSpriteReflected(this.x, this.y+Game.TILE_SIZE-1, spell.sprite, spell.color.getRGB(), Color.blue.getRGB());
			}
			//screen.splitRGB_UL((int)(x-screen.xOffset), (int)(y-screen.yOffset), Game.TILE_SIZE, Game.TILE_SIZE, 10,20,false, 10, true,false,false);
			
			//screen.renderGlow(x, y, Game.TILE_SIZE, Game.TILE_SIZE, new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256)), random.nextInt(30), 255);
			//screen.splitRGB_UL((int)(x-screen.xOffset), (int)(y-screen.yOffset), Game.TILE_SIZE, Game.TILE_SIZE, 30,20,false, 1, random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
			//screen.splitRGB_UL((int)(x-screen.xOffset), (int)(y-screen.yOffset), Game.TILE_SIZE, Game.TILE_SIZE, 19,20,false, 1, true,false,false);
		}
		
	}
	
	public String toString(){
		return (px+","+py +" : "+spell);
	}
}
