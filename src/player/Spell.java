package player;

import java.awt.Color;
import java.util.ArrayList;

import game.Menu;
import graphics.Sprite;
import graphics.UI;
import player.Spell.Rune;

public class Spell {
	//Default
	//									      (name,			type,			chr, Color,       			D, S, 	 L,     hold, shots, runes,									preReq,					Affect, 		time,  amt, unlocked)
	public static Spell Shock		=new Spell("Shock",			Type.Projectile,'!', Color.cyan,		 	5, 2f,	 1f, 	1.5f,		2, new Rune[]{Rune.Electricity},			null,												true);
	public static Spell Fireball 	=new Spell("Fireball",		Type.Projectile,'&', Color.orange, 			5, 1f, 	 1.5f,  1f, 	3, new Rune[]{Rune.Fire},					null,												true);
	public static Spell Splash		=new Spell("Splash",		Type.Projectile,'>', Color.blue, 			1, 1f,	 0.75f,	0.7f, 	8, new Rune[]{Rune.Water},					null,					Affect.Wet,		0.5f,  1,	true);
	public static Spell Airblast 	=new Spell("Airblast",		Type.Projectile,'=', Color.cyan, 			3, 3f, 	 1f, 	1f, 	4, new Rune[]{Rune.Air}, 					null,					Affect.Slow,	0.25f, 1,	true);
	public static Spell Earthball	=new Spell("Earthball",		Type.Projectile,'#', new Color(0x8f6b38),	4, 1f, 	 1f, 	1f, 	3, new Rune[]{Rune.Earth},					null,												true);
	public static Spell Shadowblast =new Spell("Shadowblast",	Type.Projectile,'&', new Color(112,39,195), 3, 1f, 	 1f, 	1f, 	4, new Rune[]{Rune.Shadow},					null,												true);
	public static Spell Trick		=new Spell("Trick",			Type.Projectile,'?', Color.gray,			3, 0.01f,4f, 	1.5f,		2, new Rune[]{Rune.Darkness},				null,												true);
	public static Spell Poisoncloud	=new Spell("Poisoncloud",	Type.Projectile,'*', Color.green,		 	0, 0.01f,5f, 	2f, 	2, new Rune[]{Rune.Poison},					null,					Affect.Poison,	0.25f, 2,	true);
	public static Spell Sunspot		=new Spell("Earthball",		Type.Projectile,'f', new Color(0x8f6b38),	4, 1f, 	 1f, 	1f, 	3, new Rune[]{Rune.Light},					null,												true);
	
	//Level 1 Unlocks
	public static Spell Lightning	=new Spell("Lightning",		Type.Projectile,'~', Color.yellow, 			3, 3, 	 1f, 	1f, 	6, new Rune[]{Rune.Electricity,Rune.Air},   new Spell[]{Shock},									false);
	public static Spell FireWhip	=new Spell("FireWhip",		Type.Projectile,'/', Color.red,			 	7, 0.5f, 4f,	2.5f,		1, new Rune[]{Rune.Fire,Rune.Air},			new Spell[]{Fireball},	Affect.onFire,	0.5f,  1,	false);
	public static Spell Waterball	=new Spell("Waterbll",		Type.Projectile,'o', Color.blue, 			3, 2.5f, 1f, 	1f, 	4, new Rune[]{Rune.Water,Rune.Air},			new Spell[]{Splash},	Affect.Wet,		0.5f,  1,	false);
	public static Spell Airwave		=new Spell("Airwave",		Type.Projectile,')', Color.white, 			3, 3.25f,0.5f, 	0.9f, 	5, new Rune[]{Rune.Air,Rune.Light},   		new Spell[]{Airblast},								false);
	public static Spell Rockslam	=new Spell("Rockslam",		Type.Projectile,'0', new Color(0x8f6b38),	7, 1f,	 0.1f, 	1f, 	1, new Rune[]{Rune.Earth,Rune.Air},   		new Spell[]{Earthball},								false);
	public static Spell ShadowPuppet=new Spell("ShadowPuppet",	Type.Projectile,'%', new Color(112,39,195),	4, 2f,	 2f, 	2.5f, 	8, new Rune[]{Rune.Shadow,Rune.Darkness},	new Spell[]{Shadowblast},							false);
	public static Spell DarkLight	=new Spell("DarkLight",		Type.Projectile,'#', Color.darkGray,		5, 3f,	 1f, 	1.5f, 	5, new Rune[]{Rune.Darkness,Rune.Light},	new Spell[]{Trick},									false);
	public static Spell PoisonDart	=new Spell("PoisonDart",	Type.Projectile,',', Color.green,			2, 3.5f, 1f, 	2f, 	8, new Rune[]{Rune.Poison,Rune.Air},		new Spell[]{Poisoncloud},Affect.Poison,	0.5f,	1,	false);
	public static Spell Lightbulb	=new Spell("Lightbulb",		Type.Projectile,'?', Color.yellow,			5, 2f,	 2f, 	1.25f, 	4, new Rune[]{Rune.Light,Rune.Electricity},	new Spell[]{Sunspot},								false);
	
	//Level 2 Unlocks
	
	
	//Levels 3-7
	//
	//
	
	//Requires additional runes
	public static Spell MagmaSnake	=new Spell("MagmaSnake",	Type.Projectile,'%', Color.red,			 	7, 0.5f,4f,		2f,		1, new Rune[]{Rune.Magma},					null,												true);
	
	//Next steps:
	//		Create Runes
	//		Create Spells
	//		Remove Particle.Spells and Combat.Spells, and fix any errors from that.
	
	public static enum Rune{
		//Default
		Electricity	('L',Color.yellow,			true,null),
		Fire		('F',Color.orange,			true,null),
		Water		('W',Color.blue,			true,null),
		Air			('A',Color.cyan,			true,null),
		Earth		('E',new Color(0x8f6b38),	true,null),
		Shadow		('S',new Color(112,39,195),	true,null),
		Darkness 	('D',Color.darkGray,		true,null),
		Poison		('P',Color.green,			true,null),
		Light		('i',new Color(244,234,102),true,null),
		
		//Rune 1st Choices
		Flux		('X',Color.magenta,			false,new Rune[]{Electricity}),
		Magma		('M',Color.red,				false,new Rune[]{Fire}),//Change color to red-orange
		Ice			('C',Color.cyan,			false,new Rune[]{Water}),//Change color to light blue
		Storm		('T',Color.gray,			false,new Rune[]{Air}),//Change color to gray-blue
		Gravity		('G',Color.green,			false,new Rune[]{Earth}),//Change color to blue-green
		Evil		('V',Color.red,				false,new Rune[]{Shadow}),
		Illusion	('I',Color.gray,			false,new Rune[]{Darkness}),
		Sickness	('s',Color.green,			false,new Rune[]{Poison}),//Change color to another green
		Life		('F',Color.white,			false,new Rune[]{Light}),
		
		//Rune 2nd Choices (use symbols like %,$,#)
		Magnetism	('U',Color.lightGray,		false,new Rune[]{Flux}),//Change color to something?

		//Rune 3rd Choices
		Technology	('$',Color.gray,			false,new Rune[]{Magnetism}),
		
		//Rune 4th Choices
		Laser		('+',Color.red,				false,new Rune[]{Technology}),
		
		//Special, Requires 2 4th tier runes
		Magic('@',Color.white,					false,new Rune[]{Laser})//Change color and symbol every update
		;
		
		public char character;
		public Color color;
		public boolean active;
		public Rune[] preReq;
		public static ArrayList<Rune> allRunes;
		Rune(char c,Color col,boolean active,Rune[] preReq){
			character=c;
			color=col;
			this.active=active;
			this.preReq=preReq;
		}
		public static void replaceRune(Rune base, Rune replace){
			base.active=false;
			replace.active=true;
			for(int i=0;i<runes.length;i++){
				if(runes[i]==base)
					runes[i]=replace;
			}
			UI.combatUI.setRunes();
		}
		public static Rune getRune(int i){
			return runes[i];
		}
		public static ArrayList<Rune> getAvailableRunes(){
			ArrayList<Rune> available=new ArrayList<Rune>();
			for(Rune s : Rune.values()){
				if(!s.active && s.preReq!=null){
					boolean av=true;
					for(Rune p : s.preReq)
						if(!p.active){
							av=false;
						}
					if(av)
						available.add(s);
				}
			}
			return available;
		}
		
		public static void setRuneMenu(){
			ArrayList<Rune> available=getAvailableRunes();
			
			String[] lines= new String[9];
			for(int i=0;i<lines.length;i++){
				if(i<available.size()){
					lines[i] = available.get(i).toString().toUpperCase();
					lines[i] += "  -  REQUIRES: ";//"Prerequisites:";
					if(available.get(i).preReq!=null){
						for(int p=0;p<available.get(i).preReq.length;p++){
							if(i<available.size() && p<available.get(i).preReq.length){
								lines[i] += available.get(i).preReq[p].toString().toUpperCase();
								if(p<available.get(i).preReq.length-1)
									lines[i]+=",";
							}else{
								lines[i]+="";
							}
						}
					}
				}else{
					lines[i]="";
					System.out.println(i +"/ " +available.size());
				}
			}
			if(available.size()>0)
				Menu.LEVEL_UP_RUNE.setLines(lines, 6);
			else
				Menu.LEVEL_UP_RUNE.setLines(new String[]{"N O  U P G R A D E S  A V A I L A B L E",""}, 3);
		}
		public static void setRuneSwapMenu(Rune[] preReq){
			ArrayList<Rune> list = getRunesForSwap(preReq);
			
			String[] lines=new String[9];
			for(int i=0;i<lines.length;i++){
				if(i<list.size())
					lines[i]=list.get(i).toString().toUpperCase();
				else
					lines[i]="";
			}
			Menu.LEVEL_UP_REPLACE_RUNE.setLines(lines, 6);
		}
		public static ArrayList<Rune> getRunesForSwap(Rune[] preReq) {
			ArrayList<Rune> list = new ArrayList<Rune>();
			for(Rune r:runes){
				boolean good=true;
				for(int i=0;i<preReq.length;i++){
					if(r==preReq[i])
						good=false;
				}
				if(good)
					list.add(r);
			}
			return list;
		}
		
		public static void update(){
			if(Magic.active){
				Magic.character=(char)((Math.random()*93)+33);
				Magic.color=new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));
				UI.combatUI.setRunes();
			}
		}

	}
	
	public enum Type{
		Projectile, Trap, Laser,
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
	
	public Type type;
	
	private Spell(String name,Type type,char character, Color color, int damage, float speed, float life, float holdTime, int shots, Rune[] runes, Spell[] preReq, boolean unlocked){
		this(name,type,character,color,damage,speed,life,holdTime,shots,runes,preReq,null,0,0,unlocked);
	}
	
	private Spell(String name, Type type, char character, Color color, int damage, float speed, float life, float holdTime, int shots, Rune[] runes, Spell[] preReq, Affect affect, float affectTime, int affectAmt,boolean unlocked){
		this.name=name;
		this.type=type;
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
			if(!s.unlocked || count!=s.runesNeeded.length)
				continue;
			else{
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
		if((float)count/(holdTime*60f)<0.5)
			return 0.33f;
		if((float)count/(holdTime*60f)<1)
			return 0.5f;
		return 1;
	}
	
	public static void setLevelMenu(){
		ArrayList<Spell> available=new ArrayList<Spell>();
		for(Spell s : spells){
			if(!s.unlocked){
				boolean av=true;
				if(s.preReq!=null)
					for(Spell p : s.preReq)
						if(!p.unlocked)
							av=false;
				if(av)
					for(Rune r : s.runesNeeded)
						if(!r.active)
							av=false;
				if(av)
					available.add(s);
			}
		}
		String[] lines= new String[15];
		for(int i=0;i<lines.length;i++){
			if(i<available.size()){
				lines[i] = available.get(i).name.toUpperCase();
				lines[i] += "  -  REQUIRES: ";//"Prerequisites:";
				if(available.get(i).preReq!=null){
					for(int p=0;p<available.get(i).preReq.length;p++){
						if(i<available.size() && p<available.get(i).preReq.length){
							lines[i] += available.get(i).preReq[p].name.toUpperCase();
							if(p<available.get(i).preReq.length-1)
								lines[i]+=",";
						}else{
							lines[i]+="";
						}
					}
				}
			}else{
				lines[i]="";
			}
		}
		if(available.size()>0)
			Menu.LEVEL_UP_SPELL.setLines(lines, 4);
		else
			Menu.LEVEL_UP_SPELL.setLines(new String[]{"N O  U P G R A D E S  A V A I L A B L E",""}, 3);
	}
	
	public static void unlockSpell(int index){
		ArrayList<Spell> available=new ArrayList<Spell>();
		for(Spell s : spells){
			if(!s.unlocked){
				boolean av=true;
				if(s.preReq!=null)
					for(Spell p : s.preReq)
						if(!p.unlocked)
							av=false;
				if(av)
					available.add(s);
			}
		}
		available.get(index).unlocked=true;
	}
	
	public static void setSpellBookPage(Spell spell){
		String r1="",r2="",r3="";
		for(int i=0;i<spell.runesNeeded.length;i++){
			if(i<3) r1+=spell.runesNeeded[i].toString().toUpperCase();
			else if(i<6) r2+=spell.runesNeeded[i].toString().toUpperCase();
			else if(i<9) r3+=spell.runesNeeded[i].toString().toUpperCase();
		}
		String[] lines={
				"----------------------------------------------",
				"                  SPELL BOOK                  ",
				"----------------------------------------------",
				"                                              ",
				"NAME:   "+spell.name.toUpperCase(),
				"                                              ",
				"TYPE:   " +spell.type.toString().toUpperCase(),
				"                                              ",
				"DAMAGE: " +spell.damage,
				"                                              ",
				"SPEED:  " +spell.speed,
				"                                              ",
				"                                              ",
				"RUNES:                                        ",
				"                                              ",
				r1,
				"                                              ",
				r2,
				"                                              ",
				r3,
				"                                              ",
				"                                              ",
				"                PREVIOUS PAGE                 ",
				"                                              ",
				"                  NEXT PAGE                   ",
				"                                              ",
				"                    CANCEL                    ",
				"---------------------------------------------/",
		};
		Menu.SPELL_BOOK.setLines(lines, 0);
	}
	
}
