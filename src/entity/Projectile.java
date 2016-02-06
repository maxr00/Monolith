package entity;

import java.awt.Color;

import entity.mob.Mob;
import game.Game;
import graphics.Screen;
import graphics.Sprite;
import level.Level;

public class Projectile extends Entity{

	final private int xOrigin, yOrigin;
	private float xDir, yDir, px,py;
	private Spell spell;
	private int speed=1;
	private float life=1.5f;
	private int lifeTime=(int)(life*60);
	private int damage = 1;
	private Level level;
	private Mob target;
	
	public static enum Spell{
		Fireball	(Sprite.and,	Color.orange, 5, 1, 1.5f),
		Lightning	(Sprite.tilde,	Color.yellow, 3, 3, 1f),
		Waterball	(Sprite.o,		Color.blue,	  2, 2, 1f),
		;
		public Sprite sprite;
		public Color color;
		public int damage, speed;
		public float life;
		Spell(Sprite s, Color col, int d, int spd, float l){sprite=s; color=col; damage=d; speed=spd; life=l;}
		
		public static Spell getSpell(String name){return Spell.valueOf(name);}
	}
	
	public Projectile(int x,int y, float xDir, float yDir, Spell spell, float damagePercent, Mob target, Level level){
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
				}
			}
		}
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
	/*
	 if(  Math.abs(target.x-(px-((xDir-correctionAmt)*speed)))> Math.abs(target.x-(px-((xDir)*speed)))  ){//Left
					xDir-=correctionAmt;
			}
			if(  Math.abs(target.x-(px+((xDir+correctionAmt)*speed)))> Math.abs(target.x-(px+((xDir)*speed)))  ){//Right
					xDir+=correctionAmt;
			}
			
			if(  Math.abs(target.y-(py-((yDir-correctionAmt)*speed)))> Math.abs(target.y-(py-((yDir)*speed)))  ){//Target down
					yDir-=correctionAmt;
			}
			if(  Math.abs(target.y-(py+((yDir+correctionAmt)*speed)))> Math.abs(target.y-(py+((yDir)*speed)))  ){//Target up
					yDir+=correctionAmt;
			}
	 */
	/*			
	if(target.x<px-((xDir-correctionAmt)*speed)){//Left
			xDir-=correctionAmt;
	}
	if(target.x>px+((xDir+correctionAmt)*speed)){//Right
			xDir+=correctionAmt;
	}
	
	if(target.y<py-((yDir-correctionAmt)*speed)){//Down
			yDir-=correctionAmt;
	}
	if(target.y>py+((yDir+correctionAmt)*speed)){//Up
			yDir+=correctionAmt;
	}
	
	
	//Diagonals not working
	//trying to predict where it will be and correcting dir accordingly
	if(target.x<px-((xDir-correctionAmt)*speed*(lifeTime/(float)corrTime)) ){//Left
		xDir-=correctionAmt;
	}
	if(target.x>px+((xDir+correctionAmt)*speed*(lifeTime/(float)corrTime)) ){//Right
		xDir+=correctionAmt;
	}

	if(target.y<py-((yDir-correctionAmt)*speed*(lifeTime/(float)corrTime)) ){//Down
		yDir-=correctionAmt;
	}
	if(target.y>py+((yDir+correctionAmt)*speed*(lifeTime/(float)corrTime)) ){//Up
		yDir+=correctionAmt;
	}
*/

	
	public void render(Screen screen){
		screen.renderProjectile((int)px, (int)py, spell.sprite, spell.color.getRGB());
	}
	
	public String toString(){
		return (px+","+py +" : "+spell);
	}
}
