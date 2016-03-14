package entity.mob;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import entity.Particle;
import entity.Particle_Exp;
import entity.Projectile;
import game.Game;
import graphics.Screen;
import graphics.Sprite;
import level.Level;
import net.PlayerMP;
import net.packet.Packet13Projectile;
import net.packet.Packet15MobUpdate;
import net.packet.Packet16RemoveMob;
import player.Spell;
import util.Node;
import util.Vector2i;

public class BasicEnemy extends Mob {

	private Vector2i target;
	private Mob targetMob;
	private int[][][] colorBlemishes;
	private boolean hasBlemishes = false;
	private final Random random = new Random();

	private int deltaX = 0, deltaY = 0;
	private ArrayList<Node> path = null;
	private Vector2i lastTarget = null;

	private float speed, shotSpeed, damage;
	
	private Pathfinding pathfind;

	public static enum Pathfinding {
		Astar, MoveToward,
		;public static Pathfinding getType(String p){return Pathfinding.valueOf(p);}
	}

	private int time = 0;
	private int pathCount = 0;

	public BasicEnemy(Level lvl, int spawnX, int spawnY, String name, char[][] characters, Pathfinding pathfind, Spell[] spells, int health, float speed,float shotSpeed, float damage, Color col, String[] statuses) {
		this.level = lvl;
		this.pathfind = pathfind;
		this.name=name;
		this.statuses=statuses;
		
		this.Health = health;
		this.color=col.getRGB();
		this.characters=characters;
		
		this.spells=spells;
		this.speed=speed;
		this.shotSpeed=shotSpeed;
		this.damage=damage;
		
		this.sprites = new Sprite[characters.length][characters[0].length];
		this.takenPos = new boolean[characters.length][characters[0].length];
		this.colorBlemishes = new int[characters.length][characters[0].length][];

		level.addMob(this);
		
		String chars="";
		for (int x = 0; x < sprites.length; x++) {
			for (int y = 0; y < sprites[x].length; y++) {
				chars+=characters[x][y];
				sprites[x][y] = Sprite.getSprite(characters[x][y]);
				if (sprites[x][y] != null) {
					takenPos[x][y] = true;
				}
			}
		}
		identifier="mob-"+name+"-"+chars+random.nextInt();
		
		if (hasBlemishes) {
			int pixel = 0;
			for (int x = 0; x < colorBlemishes.length; x++) {
				for (int y = 0; y < colorBlemishes[x].length; y++) {
					pixel = 0;
					while (pixel < Game.TILE_SIZE * Game.TILE_SIZE) {
						pixel += random.nextInt(Game.TILE_SIZE * Game.TILE_SIZE);
						int c = random.nextInt(20) + 10;
						if (pixel < colorBlemishes.length) colorBlemishes[x][y][pixel] = new Color(c, c, c).getRGB();
					}
				}
			}
		}
		move(spawnX * Game.TILE_SIZE, spawnY * Game.TILE_SIZE);
		
		if(level.getTile(this.x/Game.TILE_SIZE, this.y/Game.TILE_SIZE+1)!=null && level.getTile(this.x/Game.TILE_SIZE, this.y/Game.TILE_SIZE+1).reflectMobs){
			reflected=true;
		}else
			reflected=false;
	}
	
	double minRange=35, maxRange=56, shootRange=100;
	float xDir, yDir;
	public void update() {
		if(!hasBeenSeen)
			return;
		

		time++;
		
		if (time % 60*speed == 0 || target==null) {
			Vector2i close = null; double dist=Integer.MAX_VALUE;
			Mob closeM=null;
			for(PlayerMP p : level.players){
				if(level.getDistance(p.vector, vector)<dist){
					close = p.vector;
					closeM = p;
					dist = level.getDistance(p.vector, vector);
				}
			}
			target = close;
			targetMob = closeM;
		}
		
		switch (pathfind) {
		case Astar:
			if (time % 60*speed == 0) {
				pathFind(new Vector2i(target.getX() / Game.TILE_SIZE, target.getY() / Game.TILE_SIZE), 3500);
				deltaX = 0;
				deltaY = 0;
				if (path != null) {
					if (path.size() > 0) {
						Vector2i vector = path.get(path.size() - 1 - pathCount).vector;
						if (vector.getY() * Game.TILE_SIZE > y) deltaY++;
						if (vector.getY() * Game.TILE_SIZE < y) deltaY--;
						if (vector.getX() * Game.TILE_SIZE > x) deltaX++;
						if (vector.getX() * Game.TILE_SIZE < x) deltaX--;
					}
				}
				
				double dist=level.getDistance(vector, target);
				if(dist>maxRange || dist<minRange){
					move(deltaX * Game.TILE_SIZE, deltaY * Game.TILE_SIZE);
				}
				Packet15MobUpdate packet=new Packet15MobUpdate(x,y,Health,identifier);
				packet.writeData(Game.game.socketClient);
				
			}
		case MoveToward:
			if (time % 60*speed == 0) {
				deltaX = 0;
				deltaY = 0;
				if(target!=null){
					if (target.getX() < this.x) deltaX = -1;
					if (target.getX() > this.x) deltaX = 1;
					if (target.getY() < this.y) deltaY = -1;
					if (target.getY() > this.y) deltaY = 1;
				}				
				if(level.getDistance(vector, target)<minRange){
					//Too close, run away!
					deltaX=-deltaX;
					deltaY=-deltaY;
				}
				/*
				if(!hasShot() && level.getDistance(vector, target)<=shootRange){//No shot, and inside shooting range, try to find a shot!
					int dx=x-target.getX(), dy=y-target.getY();
					int ax=Math.abs(dx), ay=Math.abs(dy);
					deltaX=0;deltaY=0;
					
					if(ay-ax<ax && ax-ay<ay){//Closer to diagonal
						if(dx<dy){
							deltaX=1;
							if(dy!=dx+Game.TILE_SIZE){
								deltaY=-1;
							}
						}else{
							deltaX=-1;
							if(dy!=dx+Game.TILE_SIZE){
								deltaY=1;
							}
						}
					}else{
						if(ax<ay){
							if(dx<0)
								deltaX=1;
							if(dx>0)
								deltaX=-1;
						}else{
							if(dy<0)
								deltaY=1;
							if(dy>0)
								deltaY=-1;
						}
					}
					
					move(deltaX * Game.TILE_SIZE, deltaY * Game.TILE_SIZE);
				}else if(level.getDistance(vector, target)>shootRange)
					move(deltaX * Game.TILE_SIZE, deltaY * Game.TILE_SIZE);
				*/
				
				move(deltaX * Game.TILE_SIZE, deltaY * Game.TILE_SIZE);
				Packet15MobUpdate packet=new Packet15MobUpdate(x,y,Health,identifier);
				packet.writeData(Game.game.socketClient);
				
				if(level.getTile(this.x/Game.TILE_SIZE, this.y/Game.TILE_SIZE+1).reflectMobs){
					reflected=true;
				}else
					reflected=false;
			}
			if (time % 140*shotSpeed == 0) {
				if(level.getDistance(vector, target)<=shootRange){
					xDir=0;
					yDir=0;
					if(x!=target.getX()){
						if(x<target.getX()){
							if(x<target.getX()-14)
								xDir=1;
							else xDir = 0;
						}
						else if(x>target.getX()){
							if(x>target.getX()+14)
								xDir=-1;
							else xDir = 0;
						}
					}
					if(y!=target.getY()){
						if(y<target.getY()){
							if(y<target.getY()-14)
								yDir=1;
							else yDir = 0;
						}else if(y<target.getY()){
							if(y>target.getY()+14)
								yDir=-1;
							else yDir = 0;
						}
					}
					if(xDir!=0 || yDir!=0){
						Spell s=spells[(int)(Math.random()*spells.length)];
						Packet13Projectile projPacket = new Packet13Projectile(x, y, xDir, yDir, s.name,damage,targetMob.identifier,identifier);
						projPacket.writeData(Game.game.socketClient);
					}
				}
			}
			break;
		}
	}

	private boolean hasShot(){
		if( Math.abs(target.getX()-this.x) == Math.abs(target.getY()-this.y) ){
			return true;
		}
		if(target.getX()-this.x==0 || target.getY()-this.y==0){ // R
			return true;
		}
		return false;
	}
	
	private void shoot(){
		xDir=0;
		yDir=0;
		if( Math.abs(target.getX()-this.x) == Math.abs(target.getY()-this.y) ){//Diagonal
			if(target.getX()>this.x && target.getY()>this.y){//UR
				xDir=1;
				yDir=1;
			}else if(target.getX()<this.x && target.getY()>this.y){//UL
				xDir=-1;
				yDir=1;
			}else if(target.getX()>this.x && target.getY()<this.y){//DR
				xDir=1;
				yDir=-1;
			}else if(target.getX()<this.x && target.getY()<this.y){//DL
				xDir=-1;
				yDir=-1;
			}
		}else{//L,R,U,D
			if(target.getX()>this.x && target.getY()-this.y==0){ // R
				xDir=1;
			}else if(target.getX()<this.x && target.getY()-this.y==0){// L
				xDir=-1;
			}else if(target.getX()-this.x==0 && target.getY()>this.y){// U
				yDir=1;
			} else if(target.getX()-this.x==0 && target.getY()<this.y){// D
				yDir=-1;
			}
		}
		if(xDir!=0 || yDir!=0){
			Spell s=spells[(int)(Math.random()*spells.length)];
			Packet13Projectile projPacket = new Packet13Projectile(x, y, xDir, yDir, s.name,0.5f,null,identifier);
			projPacket.writeData(Game.game.socketClient);
		}
	}
	
	protected void pathFind(Vector2i point, int maxTries) {
		if (lastTarget == null || !lastTarget.equals(point)) {
			path = level.getPath(new Vector2i(x / Game.TILE_SIZE, y / Game.TILE_SIZE), point, maxTries);
			lastTarget = new Vector2i(target.getX() / Game.TILE_SIZE, target.getY() / Game.TILE_SIZE);
			pathCount = 0;
		} else if (path != null && pathCount < path.size() - 1) pathCount++;
	}

	public void render(Screen screen) {
		if(render){
			if(lockedOnto){
				screen.renderBackground(this.x-1, this.y-1, sprites.length*Game.TILE_SIZE+2, sprites[0].length*Game.TILE_SIZE+2, new Color(201,175,40).getRGB());
				screen.renderBackground(this.x, this.y, sprites.length*Game.TILE_SIZE, sprites[0].length*Game.TILE_SIZE, Screen.defaultBackground);
			}
			for (int x = 0; x < sprites.length; x++) {
				for (int y = 0; y < sprites[x].length; y++) {
					if (sprites[x][y] != null) {
						screen.renderSprite(this.x + x * Game.TILE_SIZE, this.y + y * Game.TILE_SIZE, sprites[x][y]);
						//new Color(112,39,195)
						if (colorBlemishes != null && colorBlemishes[x][y] != null){
							screen.renderLight(this.x + x * Game.TILE_SIZE, this.y + y * Game.TILE_SIZE, sprites[x][y].WIDTH, sprites[x][y].HEIGHT, color, colorBlemishes[x][y]);
						}else{
							screen.renderLight(this.x + x * Game.TILE_SIZE, this.y + y * Game.TILE_SIZE, sprites[x][y].WIDTH, sprites[x][y].HEIGHT, color, null);
						}
						
						if(reflected){
							screen.renderSpriteReflected(this.x, this.y+Game.TILE_SIZE-1, sprites[x][y],this.color,Color.blue.getRGB());
						}
					}
				}
			}
			screen.renderOutline((int)(x-screen.xOffset-screen.shake_xOffset), (int)(y-screen.yOffset-screen.shake_yOffset), sprites.length*Game.TILE_SIZE, sprites[0].length*Game.TILE_SIZE, 1, 0);
		}
	}
	
	int particlesPerDamage=400;
	public void damage(int damage, int xDir, int yDir){
		Health-=damage;
		if(Health<=0){
			removed=true;
			
			Packet16RemoveMob packet=new Packet16RemoveMob(identifier);
			packet.writeData(Game.game.socketServer);
			
			for(int tx=0;tx<takenPos.length;tx++){
				for(int ty=0;ty<takenPos[tx].length;ty++){
					if(takenPos[tx][ty]){
						int xp=tx*Game.TILE_SIZE, yp=ty*Game.TILE_SIZE;
						if(level.getTile((xp+x)/Game.TILE_SIZE, (yp+y)/Game.TILE_SIZE)!=null)
							level.getTile((xp+x)/Game.TILE_SIZE, (yp+y)/Game.TILE_SIZE).doRender(true);
					}
				}
			}
			new Particle(x + Game.TILE_SIZE/2 + xDir*Game.TILE_SIZE,y + Game.TILE_SIZE/2 + yDir*Game.TILE_SIZE,1,600,0.3f,particlesPerDamage*damage*2,level,new Color[]{Color.red,new Color(150,0,0)},Particle.RenderType.Lighting,150);
			new Particle(x + Game.TILE_SIZE/2 + xDir*Game.TILE_SIZE,y + Game.TILE_SIZE/2 + yDir*Game.TILE_SIZE,2,1200,0.1f,5*((takenPos.length+takenPos[0].length)/2),level,new Color[]{Color.lightGray},Particle.RenderType.Sprite);
			
			new Particle_Exp(x + Game.TILE_SIZE/2 + xDir*Game.TILE_SIZE,y + Game.TILE_SIZE/2 + yDir*Game.TILE_SIZE,1,1200,0.1f,10,level,new Color[]{Color.yellow},1);
		}else
			new Particle(x + Game.TILE_SIZE/2 + xDir*Game.TILE_SIZE,y + Game.TILE_SIZE/2 + yDir*Game.TILE_SIZE,1,600,0.1f,particlesPerDamage*damage,level,new Color[]{Color.red,new Color(150,0,0)},Particle.RenderType.Lighting,150);
		
		
		Packet15MobUpdate packet=new Packet15MobUpdate(x,y,Health,identifier);
		packet.writeData(Game.game.socketClient);
	}

}
