package player;

import java.awt.Color;

import entity.Particle;
import entity.Projectile;
import game.Game;
import graphics.Screen;
import graphics.Sprite;
import graphics.UI;
import net.packet.Packet03Projectile;

public class Combat {
	
	public static Player player;
	//private static int count=0;
	private static boolean renderDir;
	private static int[] pressed=new int[9];
	private static double xDir=0, yDir=0;
	private static int heldCount=0;
	
	private static Color[] pressedColors={	Color.red, 		Color.yellow,	Color.cyan,
											Color.darkGray,	Color.green,	Color.pink,
											Color.magenta,	Color.orange,	Color.blue};
	
	public static void combatPressRune(int runePressed){
		//count++;
		if(pressed[runePressed]==0)
			pressed[runePressed]=1;
		else
			pressed[runePressed]=0;
			
		if(pressed[runePressed]==0)
			UI.combatUI.colors[(runePressed)%3*2+1][(runePressed)/3*2+2]=Color.white; //Disable
		else
			UI.combatUI.colors[(runePressed)%3*2+1][(runePressed)/3*2+2]=pressedColors[runePressed];
		castSpell = Spell.getSpell(pressed);
		if(castSpell!=null)
			shotsRemaining = castSpell.remainingShots;
		else
			shotsRemaining = 1;
		
		if(shotsRemaining>10){
			UI.combatUIDir.sprites[3][0]=Sprite.getSprite(Character.forDigit(shotsRemaining/10, 10));
			UI.combatUIDir.sprites[4][0]=Sprite.getSprite(Character.forDigit(shotsRemaining%10, 10));
		}else
			UI.combatUIDir.sprites[3][0]=Sprite.getSprite(Character.forDigit(shotsRemaining%10, 10));
	}
	
	public static void clearSpell(){
		//count=0;
		UI.combatUIDir.sprites[3][0]=Sprite.getSprite('=');
		castSpell=null;
		shotsRemaining=0;
		UI.combatUIDir.setDefaultColor(Color.white);
		for(int i=0;i<pressed.length;i++){
			pressed[i]=0;
			UI.combatUI.colors[i%3*2+1][i/3*2+2]=Color.white;
		}
	}
	
	public static void holdCast(int xDir, int yDir){//float xDir, float yDir){
		renderDir=true;
		Combat.xDir=xDir;//xDir;
		Combat.yDir=yDir;//yDir;
		
		if(xDir!=0 || yDir!=0){

		}
	}
	
	public static void chargeSpell(){
		if(castSpell!=null){
			//if(castSpell.remainingShots==shotsRemaining)
				heldCount++;
			if(castSpell.getDamagePercent(heldCount)==0.33f)
				UI.combatUIDir.setDefaultColor(Color.yellow);
			if(castSpell.getDamagePercent(heldCount)==0.5f)
				UI.combatUIDir.setDefaultColor(Color.orange);
			if(castSpell.getDamagePercent(heldCount)==1)
				UI.combatUIDir.setDefaultColor(Color.red);
		}else{
			if(heldCount/60f<0.5f)
				UI.combatUIDir.setDefaultColor(Color.yellow);
			else if(heldCount/60f<1f)
				UI.combatUIDir.setDefaultColor(Color.orange);
			if(heldCount/60f==1)
				UI.combatUIDir.setDefaultColor(Color.red);
		}
	}
	
	public static void notHeldCast(){
		heldCount=0;
		xDir=0;
		yDir=0;
		renderDir=false;
		UI.combatUIDir.setDefaultColor(Color.white);
	}
	
	static Spell castSpell;
	static int shotsRemaining;
	public static void castSpell(){
		//castSpell = Spell.getSpell(pressed);
		if(xDir !=0 || yDir !=0){
			if(castSpell!=null){
				//Since a projectile packet is sent, creating a projectile now makes 2 projectiles for the client.
				//new Projectile(player.x + Game.TILE_SIZE/2, player.y + Game.TILE_SIZE/2, xDir, yDir, SpellToProjectile(castSpell),castSpell.getDamagePercent(heldCount),player.level);
				Packet03Projectile packet = new Packet03Projectile(player.x, player.y, (float)xDir, (float)yDir, SpellToProjectile(castSpell).name(),castSpell.getDamagePercent(heldCount),player.lockedOn==null ? "null" : player.lockedOn.identifier);
				packet.writeData(Game.game.socketClient);
			}else{
				new Particle(player.x + Game.TILE_SIZE/2 + (int)(xDir*Game.TILE_SIZE), player.y + Game.TILE_SIZE/2 + (int)(yDir*Game.TILE_SIZE),1,120,0.2f,50,player.level,new Color[]{Color.gray,Color.darkGray},Particle.RenderType.Sprite);
			}
		}
		shotsRemaining--;
		if(shotsRemaining>10){
			UI.combatUIDir.sprites[3][0]=Sprite.getSprite(Character.forDigit(shotsRemaining/10, 10));
			UI.combatUIDir.sprites[4][0]=Sprite.getSprite(Character.forDigit(shotsRemaining%10, 10));
		}else
			UI.combatUIDir.sprites[3][0]=Sprite.getSprite(Character.forDigit(shotsRemaining%10, 10));
		if(shotsRemaining<=0)
			clearSpell();
	}
	
	public static void render(Screen screen){
		if(renderDir && (xDir !=0 || yDir !=0)){
			//screen.renderUI(screen.width/2+xDir*Game.TILE_SIZE, screen.height/2+yDir*Game.TILE_SIZE, Sprite.x, Color.white.getRGB());
		}
		if(renderDir){
			UI.combatUIDir.render(screen);
		}else
			UI.combatUI.render(screen);
	}
	
	private static Projectile.Spell SpellToProjectile(Spell spell){
		switch(spell){
		case Fireball: return Projectile.Spell.Fireball;
		case Lightning: return Projectile.Spell.Lightning;
		case Waterball: return Projectile.Spell.Waterball;
		default:return null;
		}
	}
	
	
	// Red  	  Yellow	Cyan
	// DarkGray   Green 	Pink
	// Magenta	  Orange 	Blue
	
	public static enum Spell{
		Fireball	(new int[]{1,0,0,0,0,0,0,0,0}, 1f, 	 3),
		Lightning	(new int[]{0,1,0,0,0,0,0,0,0}, 0.5f, 60),
		Waterball	(new int[]{0,0,0,0,0,0,0,0,1}, 0.5f, 7)
		;
		static Spell[] SpellList={Fireball, Lightning, Waterball};
		int[] spellRunes;
		float holdTime;
		public int remainingShots;
		Spell(int[] runes, float holdTime, int numberOfShots){
			spellRunes=runes;
			this.holdTime=holdTime*60;
			remainingShots=numberOfShots;
		}
		
		public float getDamagePercent(int count){
			if((float)count/holdTime<0.5)
				return 0.33f;
			if((float)count/holdTime<1)
				return 0.5f;
			return 1;
		}
		
		static Spell getSpell(int[] runes){
			for(Spell s : SpellList){
				for(int i=0;i<runes.length;i++){
					if(s.spellRunes[i]!=runes[i])
						break;
					if(i==runes.length-1)
						return s;
				}
			}
			return null;
		}
	}
	
}