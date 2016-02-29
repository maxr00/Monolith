package player;

import java.awt.Color;
import java.util.Random;

import entity.Particle;
import entity.Particle_Exp;
import entity.mob.Mob;
import game.Game;
import game.Menu;
import graphics.Popup;
import graphics.Screen;
import graphics.Sprite;
import graphics.UI;
import input.Keyboard;
import input.Keyboard.Key;
import input.MouseHandler;
import level.Level;
import net.PlayerMP;
import net.packet.Packet12Move;
import net.packet.Packet21PlayerPause;
import player.Spell.Rune;

public class Player extends Mob {

	protected Keyboard input;
	protected MouseHandler mouse;
	protected Screen screen;
	private int inputX, inputY;
	private int castX,castY;
	private int[][][] colorBlemishes;
	public Mob lockedOn;
	private final static Random random = new Random();
	
	public boolean paused;
	
	private String username;
	
	private boolean hasBlemishes = false;
	
	private int Experience, Level, expToNextLevel=15, timesLeveledUp=0;
	
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
		
		Spell.setRunes(new Spell.Rune[]{Spell.Rune.Electricity,	Spell.Rune.Fire,	Spell.Rune.Water,
										Spell.Rune.Air,			Spell.Rune.Earth,	Spell.Rune.Shadow,
										Spell.Rune.Darkness,	Spell.Rune.Sickness,Spell.Rune.Light,});
		UI.combatUI.setRunes();
	}
	
	public void joinLevel(Level lvl,int spawnX,int spawnY){
		level = lvl;
		move(spawnX*Game.TILE_SIZE,spawnY*Game.TILE_SIZE);
		level.addPlayer((PlayerMP)this);
	}

	private int moveTime=0, castCooldown=0, castFinishCooldown=0;
	boolean cast;
	
	public boolean inMenu;
	public Menu menu;
	
	public void update() {
		if(level==null)
			return;
		
		if(Key.pause.onPress){
			inMenu=!inMenu;
			if(inMenu)
				pause();
			else
				unPause();
		}else
		if(Key.levelUp.onPress && timesLeveledUp<Level){
			inMenu=!inMenu;
			if(inMenu){
				if((timesLeveledUp+1)%5==0){
					Rune.setRuneMenu();
					menu=Menu.LEVEL_UP_RUNE.load();
				}else{
					Spell.setLevelMenu();
					menu=Menu.LEVEL_UP_SPELL.load();
				}
				menu.active=true;
				paused=true;
			}else{
				unPause();
			}
		}
		
		if(Key.select.onPress){
			new Popup("POPUPS ARE COOL",new Color(random.nextInt(150)+100,random.nextInt(150)+100,random.nextInt(150)+100),3f);
		}
		
		if(inMenu)
			menuUpdate();
		else
			gameplayUpdate();
		
		Popup.update();
	}
	
	public void unPause(){
		inMenu=false;
		menu.active=false;
		menu=null;
		paused=false;
		
		Packet21PlayerPause packet = new Packet21PlayerPause(identifier, paused);
		packet.writeData(Game.game.socketClient);
	}
	
	public void pause(){
		inMenu=true;
		menu=Menu.PAUSE;
		menu.active=true;
		paused=true;
		
		Packet21PlayerPause packet = new Packet21PlayerPause(identifier, paused);
		packet.writeData(Game.game.socketClient);
	}
		
	public void swapRuneMenu(Rune[] preReq){
		Rune.setRuneSwapMenu(preReq);
		menu.active=false;
		menu=Menu.LEVEL_UP_REPLACE_RUNE.load();
	}
	private void menuUpdate(){
		if (Key.up.onPress)
			menu.selectPrevious();
		if (Key.down.onPress)
			menu.selectNext();
		if(Key.select.onPress)
			menu.select();
		if(Key.back.onPress)
			if(menu.back()!=null)
				menu=menu.back();
	}
	
	private void gameplayUpdate(){
		
		//Debug
		if(mouse.isPressed && mouse.button==2){
			int x=(mouse.x/Game.scale +(this.x - screen.width/2));
			int y=(mouse.y/Game.scale +(this.y - screen.height/2) + Game.TILE_SIZE/2);
			
			new Particle_Exp(x,y,1,1200,0.1f,10,level,new Color[]{Color.yellow},1);
		}
		
		
		moveTime++;
		inputX = 0;	inputY = 0;
		if(Key.castSpell.onPress)
			cast=true;//!cast;
		if(Key.clearSpell.pressed)
			cast=false;
		if(moveTime>15){
			if (Key.right.pressed){ inputX=1; moveTime=0;}
			if (Key.left.pressed){ inputX=-1; moveTime=0;}
			if (Key.down.pressed){ inputY=1; moveTime=0;}
			if (Key.up.pressed){ inputY=-1; moveTime=0;}
		}
		
		if (inputX != 0 || inputY != 0){
			move(inputX*Game.TILE_SIZE, inputY*Game.TILE_SIZE);
			
			Packet12Move packet = new Packet12Move(getUsername(),x,y);
			packet.writeData(Game.game.socketClient);
		}
		
		if(Key.toggleLock.onPress && lockedOn==null){
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
		}
		else if(Key.toggleLock.onPress && lockedOn!=null){
				lockedOn.lockedOnto=false;
				lockedOn=null;
			}

		if(castFinishCooldown<=0){
			if(!cast){
				for(int i=0;i<9;i++)
					if(Keyboard.runes[i].onPress) Combat.combatPressRune(i);
				Combat.notHeldCast();
			}
			
			if(Key.clearSpell.pressed){
				Combat.clearSpell();
			}
			
			if(cast){
				castCooldown++;
				castX = 0;	castY = 0; //Default case, unreachable otherwise
				if(Keyboard.runes[0].offPress){ castX=-1; castY=-1; }
				if(Keyboard.runes[1].offPress){ castX=0; castY=-1; }
				if(Keyboard.runes[2].offPress){ castX=1; castY=-1; }
				if(Keyboard.runes[3].offPress){ castX=-1; castY=0; }
				if(Keyboard.runes[4].offPress){ castX=0; castY=0; }
				if(Keyboard.runes[5].offPress){ castX=1; castY=0; }
				if(Keyboard.runes[6].offPress){ castX=-1; castY=1; }
				if(Keyboard.runes[7].offPress){ castX=0; castY=1; }
				if(Keyboard.runes[8].offPress){ castX=1; castY=1; }
				
				//if(level.canMoveOn((int)(x/Game.TILE_SIZE+castX),(int)(y/Game.TILE_SIZE+castY)))
				Combat.holdCast(castX, castY);//castX,castY);
				if(Key.castSpell.pressed)
					Combat.chargeSpell();
				//else Combat.notHeldCast();
				if((castX!=0 || castY!=0) && castCooldown>=10){
					castCooldown=0;
					Combat.holdCast(castX, castY);//(castX, castY);
					Combat.castSpell();
					if(Combat.shotsRemaining<=0){
						Combat.notHeldCast();
						cast=false;
						castFinishCooldown=40;
						for(int i=0;i<UI.combatUI.colors.length;i++){
							for(int j=0;j<UI.combatUI.colors[i].length;j++){
								UI.combatUI.colors[i][j]=Color.darkGray;
							}
						}
					}
				}//else if(input.runeKeyOff[4]){ 
					//Cast on self
				//}
			}
		}else{
			castFinishCooldown--;
			if(castFinishCooldown>0){
				for(int i=0;i<UI.combatUI.colors.length;i++){
					for(int j=0;j<UI.combatUI.colors[i].length;j++){
						UI.combatUI.colors[i][j]=Color.darkGray;
					}
				}
			}else{
				for(int i=0;i<UI.combatUI.colors.length;i++){
					for(int j=0;j<UI.combatUI.colors[i].length;j++){
						UI.combatUI.colors[i][j]=Color.white;
					}
				}
				UI.combatUI.setRunes();
				UI.combatUI.colors=UI.combatUI.startColors;
			}
		}
	}
	
	int particlesPerDamage=400;
	public void damage(int damage, float xDir, float yDir){
		Health-=damage;
		
		if(screen!=null)
			screen.setShakeEffect(0.1f, 2, 2, 10);
		
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
			
			new Popup(username.toUpperCase() +" HAS BEEN SLAIN",Color.red,5f);
		}else{
			new Particle(x + Game.TILE_SIZE/2 + (int)(xDir*Game.TILE_SIZE),y + Game.TILE_SIZE/2 + (int)(yDir*Game.TILE_SIZE),1,45,0.5f,damage*5,level,new Color[]{Color.red,new Color(150,0,0)},Particle.RenderType.Sprite,150);
			new Particle(x + Game.TILE_SIZE/2 + (int)(xDir*Game.TILE_SIZE),y + Game.TILE_SIZE/2 + (int)(yDir*Game.TILE_SIZE),1,600,0.1f,particlesPerDamage*damage,level,new Color[]{Color.red,new Color(150,0,0)},Particle.RenderType.Additive,150);
		}
	}
	
	public void render(Screen screen){
		if(render){
			for(int y=0;y<colorBlemishes.length;y++){
				for(int x=0;x<colorBlemishes.length;x++){
					screen.renderSprite(this.x, this.y, sprites[x][y]);
					screen.renderLight(this.x, this.y, sprites[x][y].WIDTH, sprites[x][y].HEIGHT, this.color, colorBlemishes[x][y]);
				}
			}
		}
		
		if(inMenu){
			if(menu!=null)
				menu.active=true;//render(screen);
			Combat.dontRender(screen);
			UI.healthUI.active=false;//render(screen);
			UI.statusUI.active=false;//render(screen);
			UI.levelReadyUI.active=false;
		}else{
			if(menu!=null)
				menu.active=false;
			Combat.render(screen);
			UI.healthUI.active=true;//render(screen);
			UI.statusUI.active=true;//render(screen);
			if(timesLeveledUp<Level && UI.statusUI.sprites==null)
				UI.levelReadyUI.active=true;
			else
				UI.levelReadyUI.active=false;
		}
		
		//screen.renderGlow(x, y, Game.TILE_SIZE, Game.TILE_SIZE, Color.yellow, 100, 25);
		//screen.renderSprite( (int)(((mouse.x/((float)Game.scale)+x-screen.width/2f) / ((float)screen.width/2) - 1)*Game.TILE_SIZE+x),(int)(((mouse.y/((float)Game.scale)+y-screen.height/2f) / ((float)screen.height/2) - 1)*Game.TILE_SIZE+y),Sprite.percent);
		//screen.renderSprite(mouse.x/Game.scale+x-screen.width/2,mouse.y/Game.scale+y-screen.height/2, Sprite._0);
	}

	public String getUsername() {
		return username;
	}
	
	public String getStatus(){	
		return username +", LVL:"+Level +" HP:" +Health +" ON TILE "+x/Game.TILE_SIZE +","+y/Game.TILE_SIZE;
	}
	
	public String getObservation(){	
		if(input!=null)
			return "It's me!";
		else
			return "My friend?";
	}

	public void handleStatus(Screen screen) {
		if(level==null) return;
		int x=(int)((mouse.x/Game.scale +screen.xOffset) /Game.TILE_SIZE);
		int y=(int)((mouse.y/Game.scale +screen.yOffset + Game.TILE_SIZE/2) /Game.TILE_SIZE);
		//int x=(mouse.x/Game.scale +(this.x - screen.width/2)) /Game.TILE_SIZE;
		//int y=(mouse.y/Game.scale +(this.y - screen.height/2) + Game.TILE_SIZE/2) /Game.TILE_SIZE;
		if(level.getMobOn(x, y)!=null){
			if(level.getMobOn(x, y) instanceof Player){
				UI.statusUI.setStatus(level.getMobOn(x, y).getStatus(),level.getMobOn(x,y).getObservation());
			}else if(level.getMobOn(x, y).isSeen){
				if(lockedOn!=null)
					lockedOn.lockedOnto=false;
				UI.statusUI.setStatus(level.getMobOn(x, y).getStatus(),level.getMobOn(x,y).getObservation());
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
				UI.statusUI.setStatus("LOCKED ON:" +lockedOn.getStatus(),lockedOn.getObservation());
				for(int i=0;i<"LOCKED ON:".length();i++)
					UI.statusUI.colors[i][0]=Color.yellow;
			}else{
				lockedOn=null;
			}
		}
	}

	public void addExp(int i) {
		Experience+=i;
		if(Experience >= expToNextLevel){
			Level++;
			expToNextLevel*=1.5f;
			
			new Popup("LEVELED UP TO LEVEL " +Level,Color.green,5f);
		}
	}
	
	public void leveledUp(){
		timesLeveledUp++;
	}
}
