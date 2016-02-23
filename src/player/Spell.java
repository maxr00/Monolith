package player;

import java.awt.Color;
import java.util.ArrayList;

import graphics.Sprite;

public class Spell {
	//Default
	//									      (name,			chr, Color,       			D, S, 	 L,     hold, shots, runes,									preReq,					Affect, 		time,  amt, unlocked)
	public static Spell Shock		=new Spell("Shock",			'!', Color.cyan,		 	5, 2f,	 1f, 	1f,		2, new Rune[]{Rune.Electricity},			null,												true);
	public static Spell Fireball 	=new Spell("Fireball",		'&', Color.orange, 			5, 1f, 	 1.5f,  0.5f, 	3, new Rune[]{Rune.Fire},					null,												true);
	public static Spell Splash		=new Spell("Splash",		'>', Color.blue, 			1, 1f,	 0.75f,	0.2f, 	8, new Rune[]{Rune.Water},					null,					Affect.Wet,		0.5f,  1,	true);
	public static Spell Airblast 	=new Spell("Airblast",		'=', Color.cyan, 			3, 3f, 	 1f, 	0.5f, 	4, new Rune[]{Rune.Air}, 					null,					Affect.Slow,	0.25f, 1,	true);
	public static Spell Earthball	=new Spell("Earthball",		'#', new Color(0x8f6b38),	4, 1f, 	 1f, 	0.5f, 	3, new Rune[]{Rune.Earth},					null,												true);
	public static Spell Shadowblast =new Spell("Shadowblast",	'&', new Color(112,39,195), 3, 2f, 	 1f, 	0.5f, 	4, new Rune[]{Rune.Shadow},					null,												true);
	public static Spell Trick		=new Spell("Trick",			'?', Color.gray,			3, 0f, 	 4f, 	1f,		2, new Rune[]{Rune.Darkness},				null,												true);
	public static Spell Poisoncloud	=new Spell("Poisoncloud",	'*', Color.green,		 	0, 0f, 	 5f, 	1.5f, 	2, new Rune[]{Rune.Poison},					null,					Affect.Poison,	0.25f, 2,	true);
	public static Spell Sunspot		=new Spell("Earthball",		'f', new Color(0x8f6b38),	4, 1f, 	 1f, 	0.5f, 	3, new Rune[]{Rune.Light},					null,												true);
	
	//Level 1 Unlocks
	public static Spell Lightning	=new Spell("Lightning",		'~', Color.yellow, 			3, 3, 	 1f, 	0.5f, 	6, new Rune[]{Rune.Electricity,Rune.Air},   new Spell[]{Shock},									false);
	public static Spell FireWhip	=new Spell("FireWhip",		'/', Color.red,			 	7, 0.5f, 4f,	2f,		1, new Rune[]{Rune.Fire},					new Spell[]{Fireball},	Affect.onFire,	0.5f,  1,	false);
	public static Spell Waterball	=new Spell("Waterbll",		'o', Color.blue, 			3, 2.5f, 1f, 	0.5f, 	4, new Rune[]{Rune.Water},					new Spell[]{Splash},	Affect.Wet,		0.5f,  1,	false);
	
	//Requires additional runes
	public static Spell MagmaSnake	=new Spell("MagmaSnake",	'%', Color.red,			 	7, 0.5f,4f,		2f,		1, new Rune[]{Rune.Magma},					null,												false);
	
	//Next steps:
	//		Create Runes
	//		Create Spells
	//		Remove Particle.Spells and Combat.Spells, and fix any errors from that.
	
	
	public enum Rune{
		//Default
		Electricity	('L',Color.yellow),
		Fire		('F',Color.orange),
		Water		('W',Color.blue),
		Air			('A',Color.cyan),
		Earth		('E',new Color(0x8f6b38)),
		Shadow		('S',new Color(112,39,195)),
		Darkness 	('D',Color.darkGray),
		Poison		('P',Color.green),
		Light		('T',new Color(244,234,102)),
		
		//Rune 1st Choices
		Flux		('X',Color.magenta),
		Magma		('M',Color.red),//Change color to red-orange
		Ice			('C',Color.cyan),//Change color to light blue
		Storm		('T',Color.gray),//Change color to gray-blue
		Gravity		('G',Color.green),//Change color to blue-green
		Evil		('V',Color.red),
		Illusion	('I',Color.gray),
		Sickness	('K',Color.green),//Change color to another green
		Life		('F',Color.white),
		
		//Rune 2nd Choices (use symbols like %,$,#)
		Magnetism	('U',Color.lightGray),//Change color to something?
		Technology	('$',Color.gray),
		Laser		('Z',Color.red),
		
		//Special
		Magic('@',Color.white)//Change color and symbol every update
		;
		
		public char character;
		public Color color;
		public Spell preReq;
		Rune(char c,Color col){
			character=c;
			color=col;
		}
	}
	
	public enum Affect{
		Wet, Slow, Poison, onFire
	}
	
	//Projectile
	public Sprite sprite;
	public Color color;
	public int damage;
	public float life,speed;
	
	//Combat
	public int[] spellRunes;
	public float holdTime;
	public int shots;
	
	
	public Rune[] runesNeeded;
	public Spell[] preReq;
	public Affect affect;
	public float affectTime;
	public int affectAmt;
	public boolean unlocked;
	public String name;
	
	private Spell(String name,char character, Color color, int damage, float speed, float life, float holdTime, int shots, Rune[] runes, Spell[] preReq, boolean unlocked){
		this(name,character,color,damage,speed,life,holdTime,shots,runes,preReq,null,0,0,unlocked);
	}
	
	private Spell(String name, char character, Color color, int damage, float speed, float life, float holdTime, int shots, Rune[] runes, Spell[] preReq, Affect affect, float affectTime, int affectAmt,boolean unlocked){
		this.name=name;
		this.sprite=Sprite.getSprite(character);
		this.color=color;
		this.damage=damage;
		this.speed=speed;
		this.life=life;
		this.holdTime=holdTime;
		this.shots=shots;
		this.runesNeeded=runes;
		this.preReq=preReq;
		this.affect=affect;
		this.affectAmt=affectAmt;
		this.unlocked=unlocked;
		if(spells==null)
			spells=new ArrayList<Spell>();
		spells.add(this);
	}

	public static ArrayList<Spell> spells;
	public static Rune[] runes;
	
	public static void setRunes(Rune[] runes){
		Spell.runes=runes;
	}
	
	public static Spell getSpell(int[] pressed) {
		int count=0;
		for(int i=0;i<pressed.length;i++)
			if(pressed[i]==1)
				count++;
		for(Spell s : spells){
			if(count!=s.runesNeeded.length)
				continue;
			
			boolean allPressed=true;
			for(int r=0;r<s.runesNeeded.length;r++){
				boolean good=false;
				for(int i=0;i<pressed.length;i++){
					if(pressed[i]==1 && runes[i]==s.runesNeeded[r]){
						good=true;
						break;
					}
				}
				if(!good)
					allPressed=false;
			}
			if(allPressed)
				return s;
		}
		return null;
	}
	
	public static Spell getSpell(String name){
		for(Spell s : spells)
			if(s.name.equals(name))
				return s;
		return null;
	}
	
	public float getDamagePercent(int count){
		if((float)count/holdTime<0.5)
			return 0.33f;
		if((float)count/holdTime<1)
			return 0.5f;
		return 1;
	}
	
}
