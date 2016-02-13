package player;

import java.awt.Color;
import java.util.Random;

import entity.Particle;
import entity.mob.Mob;
import game.Game;
import graphics.Screen;
import graphics.Sprite;
import graphics.UI;
import input.Keyboard;
import input.MouseHandler;
import level.Level;
import net.PlayerMP;
import net.packet.Packet12Move;
import net.packet.Packet19RequestLevel;

public class Player extends Mob {

	protected Keyboard input;
	protected MouseHandler mouse;
	protected Screen screen;
	private int inputX, inputY;
	private int castX,castY;
	private int[][][] colorBlemishes;
	public Mob lockedOn;
	private final static Random random = new Random();
	
	private String username;
	
	private boolean hasBlemishes = false;
	
	public Player(Keyboard in, MouseHandler mouse, Screen screen, Level lvl, String name, int spawnX, int spawnY, int color) {
		Health=25;
		username = name;
		this.name=username;
		
		identifier = "Player-"+username;
		
		this.color=color;//Color.yellow;
		
		input = in;
		this.mouse=mouse;
		this.screen=screen;
		
		sprites=new Sprite[1][1];
		sprites[0][0] = Sprite._at;
		
		takenPos=new boolean[1][1];
		takenPos[0][0]=true;
		
		colorBlemishes = new int[1][1][];
		colorBlemishes[0][0] = new int[sprites[0][0].pixels.length];
		
		if(hasBlemishes){
			int pixel = 0;
			for(int y=0;y<colorBlemishes.length;y++){
				for(int x=0;x<colorBlemishes.length;x++){
					pixel = 0;
					while(pixel < Game.TILE_SIZE*Game.TILE_SIZE){
						pixel += random.nextInt(Game.TILE_SIZE*Game.TILE_SIZE);
						int c=random.nextInt(20)+10;
						if(pixel<colorBlemishes.length)
							colorBlemishes[x][y][pixel] = new Color(c,c,c).getRGB();
					}
				}
			}
		}
		if(in!=null){
			Combat.player = this;
		}
		if(lvl!=null){
			joinLevel(lvl,spawnX,spawnY);
		}else
			System.out.println(username+" spawned with no level!");
	}
	
	public void joinLevel(Level lvl,int spawnX,int spawnY){
		level = lvl;
		move(spawnX*Game.TILE_SIZE,spawnY*Game.TILE_SIZE);
		level.addPlayer((PlayerMP)this);
	}

	private int moveTime=0;
	boolean cast;
	public void update() {
		if(level==null)
			return;
		
		
		moveTime++;
		inputX = 0;	inputY = 0;
		if(input.onCastSpell)
			cast=true;//!cast;
		if(input.clearSpell)
			cast=false;
		if(moveTime>15){
			if (input.right){ inputX=1; moveTime=0;}
			if (input.left){ inputX=-1; moveTime=0;}
			if (input.down){ inputY=1; moveTime=0;}
			if (input.up){ inputY=-1; moveTime=0;}
		}

		if(!cast){
			for(int i=0;i<9;i++)
				if(input.runeKeyOn[i]) Combat.combatPressRune(i);
			Combat.notHeldCast();
		}
		
		if(input.clearSpell){
			Combat.clearSpell();
		}
		
		if (inputX != 0 || inputY != 0){
			move(inputX*Game.TILE_SIZE, inputY*Game.TILE_SIZE);
			
			Packet12Move packet = new Packet12Move(getUsername(),x,y);
			packet.writeData(Game.game.socketClient);
		}
		
		if(input.onToggleLock && lockedOn==null){
			Mob close = null; double dist=Integer.MAX_VALUE;
			for(Mob m : level.mobs){
				if(m.isSeen && level.getDistance(m.vector, vector)<dist){
					close = m;
					dist = level.getDistance(m.vector, vector);
				}
			}
			lockedOn = close;
			if(lockedOn!=null)
				lockedOn.lockedOnto=true;
		}else 
		if(input.onToggleLock && lockedOn!=null){
			lockedOn.lockedOnto=false;
			lockedOn=null;
		}

		
		if(cast){
			castX = 0;	castY = 0; //Default case, unreachable otherwise
			if(input.runeKeyOff[0]){ castX=-1; castY=-1; }
			if(input.runeKeyOff[1]){ castX=0; castY=-1; }
			if(input.runeKeyOff[2]){ castX=1; castY=-1; }
			if(input.runeKeyOff[3]){ castX=-1; castY=0; }
			if(input.runeKeyOff[4]){ castX=0; castY=0; }
			if(input.runeKeyOff[5]){ castX=1; castY=0; }
			if(input.runeKeyOff[6]){ castX=-1; castY=1; }
			if(input.runeKeyOff[7]){ castX=0; castY=1; }
			if(input.runeKeyOff[8]){ castX=1; castY=1; }
			
			//if(level.canMoveOn((int)(x/Game.TILE_SIZE+castX),(int)(y/Game.TILE_SIZE+castY)))
				Combat.holdCast(castX, castY);//castX,castY);
			if(input.castSpell)
				Combat.chargeSpell();
			//else Combat.notHeldCast();
			if(castX!=0 || castY!=0){
				Combat.holdCast(castX, castY);//(castX, castY);
				Combat.castSpell();
				if(Combat.shotsRemaining<=0){
					Combat.notHeldCast();
					cast=false;
				}
			}else if(input.runeKeyOff[4]){ 
				//Cast on self
			}
		}
	}
	
	int particlesPerDamage=400;
	public void damage(int damage, float xDir, float yDir){
		Health-=damage;
		
		if(input!=null)
			for(int h=0;h<24;h++){
				if(Health>h){
					UI.healthUI.colors[h+7][0]=UI.healthUI.startColors[h+7][0];
				}else
					UI.healthUI.colors[h+7][0]=Color.black;
			}
		
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
		}else
			new Particle(x + Game.TILE_SIZE/2 + (int)(xDir*Game.TILE_SIZE),y + Game.TILE_SIZE/2 + (int)(yDir*Game.TILE_SIZE),1,600,0.1f,particlesPerDamage*damage,level,new Color[]{Color.red,new Color(150,0,0)},Particle.RenderType.Additive,150);
	}
	
	public void render(Screen screen){
 		Combat.render(screen);
		UI.healthUI.render(screen);
		UI.statusUI.render(screen);
		for(int y=0;y<colorBlemishes.length;y++){
			for(int x=0;x<colorBlemishes.length;x++){
				screen.renderSprite(this.x, this.y, sprites[x][y]);
				screen.renderLight(this.x, this.y, sprites[x][y].WIDTH, sprites[x][y].HEIGHT, this.color, colorBlemishes[x][y]);
			}
		}
		
		//screen.renderSprite( (int)(((mouse.x/((float)Game.scale)+x-screen.width/2f) / ((float)screen.width/2) - 1)*Game.TILE_SIZE+x),(int)(((mouse.y/((float)Game.scale)+y-screen.height/2f) / ((float)screen.height/2) - 1)*Game.TILE_SIZE+y),Sprite.percent);
		//screen.renderSprite(mouse.x/Game.scale+x-screen.width/2,mouse.y/Game.scale+y-screen.height/2, Sprite._0);
	}

	public String getUsername() {
		return username;
	}
	
	public String getStatus(){	
		return username +".  HP:" +Health +"  ON TILE "+x/Game.TILE_SIZE +","+y/Game.TILE_SIZE;
	}

	public void handleStatus(Screen screen) {
		if(level==null) return;
		int x=(mouse.x/Game.scale +(this.x - screen.width/2)) /Game.TILE_SIZE;
		int y=(mouse.y/Game.scale +(this.y - screen.height/2)) /Game.TILE_SIZE;
		if(level.getMobOn(x, y)!=null){
			if(level.getMobOn(x, y).isSeen){
				if(lockedOn!=null)
					lockedOn.lockedOnto=false;
				UI.statusUI.setStatus(level.getMobOn(x, y).getStatus());
				if(mouse.isPressed && level.getMobOn(x, y)!=this){
					lockedOn=level.getMobOn(x, y);
				}
			}
		}else{
			UI.statusUI.clearStatus();
			if(mouse.onPress){
				if(lockedOn!=null)
					lockedOn.lockedOnto=false;
				lockedOn=null;
			}
		}
		if(lockedOn!=null){
			if(!lockedOn.isRemoved()){
				lockedOn.lockedOnto=true;
				UI.statusUI.setStatus("LOCKED ON:" +lockedOn.getStatus());
				for(int i=0;i<"LOCKED ON:".length();i++)
					UI.statusUI.colors[i][0]=Color.yellow;
			}else{
				lockedOn=null;
			}
		}
	}
	
}
