package entity;

import java.awt.Color;
import java.util.ArrayList;

import game.Game;
import graphics.Screen;
import graphics.Sprite;
import level.Level;
import util.Vector2i;

public class Particle extends Entity {

	private Sprite sprite;
	
	private ArrayList<Particle> particles = new ArrayList<Particle>();
	private Color color;

	private int life, alpha;
	
	private float xDir, yDir, zDir;
	private float xx=0,yy=0, zz=0;
	private float speed=1;
	private int scale;

	private RenderType renderType;
	
	public enum RenderType{
		Lighting,
		Sprite,
		Additive
	}
	
	private Particle(int x, int y, int scale, int life, float speed, Color color, Level level, RenderType type,int alpha) {
		this.level=level;
		this.x = x;
		this.y = y;
		this.xx=x;
		this.yy=y;
		this.life = life;
		this.color = color;
		this.scale = scale;
		this.sprite = new Sprite(scale,scale,color);
		this.renderType = type;
		this.alpha=alpha;
		this.speed=speed;
		
		this.life+=random.nextInt(40)-20;
		
		xDir = (float)random.nextGaussian();
		yDir = (float)random.nextGaussian();
		zz = random.nextFloat() + 2f;
	}

	public Particle(int x, int y, int scale, int life, float speed, int amt, Level level, Color[] colors, RenderType renderType) {
		this.x=x;
		this.y=y;
		for (int i = 0; i < amt; i++) {
			particles.add(new Particle(x, y, scale, life, speed, colors[(int)(Math.random()*colors.length)],level,renderType,255));
		}
		level.entities.add(this);
	}
	
	public Particle(int x, int y, int scale, int life, float speed, int amt, Level level, Color[] colors, RenderType renderType,int alpha) {
		this.x=x;
		this.y=y;
		for (int i = 0; i < amt; i++) {
			particles.add(new Particle(x, y, scale, life, speed, colors[(int)(Math.random()*colors.length)],level,renderType,alpha) );
		}
		level.entities.add(this);
	}
	
	public boolean updateParticle(){
		life--;
		if(life<0){
			return true;
		}
		
		if(zz<=0){
			zz=0;
			zDir*=-0.5f;
			xDir*=0.5f;
			yDir*=0.5f;
			if(Math.abs(zDir)<0.2f) zDir=0;
		}else
			zDir-=0.07f;
		
		move((int)(xx+xDir*speed),(int)(yy+yDir*speed)+(int)(zz+zDir*speed));
		
		return false;
	}
	
	private void move(int x,int y){
		if(!level.canMoveOn(x/Game.TILE_SIZE,y/Game.TILE_SIZE)){
			xDir*=-0.5f;
			yDir*=-0.5f;
		}
		
		xx+=xDir*speed;
		yy+=yDir*speed;
		zz+=zDir*speed;
		this.x=(int)xx;
		this.y=(int)(yy-zz);
	}
	
	ArrayList<Vector2i> pPos=new ArrayList<Vector2i>();
	public void update(){
		for(int i=particles.size()-1;i>=0;i--){
			if(particles.get(i).updateParticle())
				particles.remove(i);
		}
		if(particles.size()==0)
			removed=true;
	}
	
	public void render(Screen screen){
		screen.newAdditive();
		for(int i=0;i<particles.size();i++){
			particles.get(i).renderPixel(screen);
		}
		screen.displayAdditive();
	}
	
	public void renderPixel(Screen screen){
		switch(renderType){
		case Sprite:
			screen.renderSprite((int)xx, (int)(yy-zz), sprite);
			break;
		case Lighting:
			screen.renderLight((int)xx, (int)yy, scale, scale, color.getRGB(), null);
			break;
		case Additive:
			screen.renderAdditiveLight((int)xx, (int)yy, scale, scale, color,alpha);
			break;
		}
	}

}
