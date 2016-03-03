package entity.mob;

import java.awt.Color;
import java.util.ArrayList;

import entity.mob.BasicEnemy.Pathfinding;
import player.Spell;

public class MobInfo {
	
	//DONT USE:
	//		/
	//		,
	//		|
	//		
	//		

	public static MobInfo Gumbling			=new MobInfo("Gumbling",		Type.Common,	new char[][]{{'e'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward, 	new String[]{""});
	public static MobInfo Whopple			=new MobInfo("Whopple",			Type.Common,	new char[][]{{'f'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{""});
	public static MobInfo Gubble			=new MobInfo("Gubble",			Type.Common,	new char[][]{{'c'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{""});
	public static MobInfo Kacaka			=new MobInfo("Kacaka",			Type.Common,	new char[][]{{'!'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{""});
	public static MobInfo Fleye				=new MobInfo("Fleye",			Type.Common,	new char[][]{{'i'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{""});
	public static MobInfo Cubble			=new MobInfo("Cubble",			Type.Common,	new char[][]{{'k'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{""});
	public static MobInfo Du				=new MobInfo("Du",				Type.Common,	new char[][]{{'z'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{""});
	public static MobInfo Do				=new MobInfo("Du",				Type.Common,	new char[][]{{'Z'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{""});
	public static MobInfo Whacka			=new MobInfo("Whacka",			Type.Common,	new char[][]{{'3'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{""});
	public static MobInfo Long_Arm			=new MobInfo("Long Arm",		Type.Common,	new char[][]{{'W'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Hopefully it doesn't want a tickle fight"});

	public static MobInfo Bat				=new MobInfo("Bat",				Type.Common,	new char[][]{{'^'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Flap flap flap!"});
	public static MobInfo Ghoul				=new MobInfo("Ghoul",			Type.Common,	new char[][]{{'&'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Looks lonely"});
	public static MobInfo Ghost				=new MobInfo("Ghost",			Type.Common,	new char[][]{{'?'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Spooky!"});
	public static MobInfo Ghostly_Ghoul		=new MobInfo("Ghostly Ghoul",	Type.Common,	new char[][]{{'Q'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Doesn't look too friendly"});
	public static MobInfo Python			=new MobInfo("Python",			Type.Common,	new char[][]{{'q'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Watch out, it bites!"});
	public static MobInfo Dwarf				=new MobInfo("Dwarf",			Type.Common,	new char[][]{{'p'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Doesn't like intruders in it's fortress"});
	public static MobInfo H					=new MobInfo("H",				Type.Common,	new char[][]{{'H'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{""});
	public static MobInfo Gnome1			=new MobInfo("Gnome",			Type.Common,	new char[][]{{'b'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"It's hat is very pointy"});
	public static MobInfo Gnome2			=new MobInfo("Gnome",			Type.Common,	new char[][]{{'d'}},	10, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"It's hat is very pointy"});
	
	
	public static MobInfo Slippery_Snake	=new MobInfo("Slippery Snake",	Type.Uncommon,	new char[][]{{'~'}},		15, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"So slippery!"});
	public static MobInfo Average_Monster	=new MobInfo("Average Monster",	Type.Uncommon,	new char[][]{{'M'}},		15, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Not my type."});
	public static MobInfo Living_Bucket		=new MobInfo("Living Bucket",	Type.Uncommon,	new char[][]{{'u'}},		15, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"How?"});
	public static MobInfo Jack_OLantern		=new MobInfo("Jack O'Lantern",	Type.Uncommon,	new char[][]{{'G'}},		15, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Mr.O'Lantern doesn't play around"});
	public static MobInfo George			=new MobInfo("George",			Type.Uncommon,	new char[][]{{'G'}},		15, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{""});
	public static MobInfo Lost_Traveller	=new MobInfo("Lost Traveller",	Type.Uncommon,	new char[][]{{'P','\\'}},	15, 1f,2f, 0.5f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Why are they attacking me?"});
	
	public static MobInfo Horse				=new MobInfo("Horse",			Type.Uncommon,	new char[][]{{'n','n','P'}},		15, 1f,2f, 0.75f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Who gave this horse magic?"});
	public static MobInfo Large_Bat			=new MobInfo("Large Bat",		Type.Uncommon,	new char[][]{{'`','^','\''}},		15, 1f,2f, 0.75f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Eek!"});
	public static MobInfo Cyclops			=new MobInfo("Cyclops",			Type.Uncommon,	new char[][]{{'<','>'},{'{','}'}},	15, 1f,2f, 0.75f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Good aim, all things considered"});
	public static MobInfo Big_Ghost			=new MobInfo("Big Ghost",		Type.Uncommon,	new char[][]{{'{','\"','}'}},		15, 1f,2f, 0.75f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Wonder what it was when it was alive"});
	public static MobInfo Walking_Pot		=new MobInfo("Walking Pot",		Type.Uncommon,	new char[][]{{'2','5'}},			15, 1f,2f, 0.75f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Cool?"});
	public static MobInfo Stick				=new MobInfo("Stick",			Type.Uncommon,	new char[][]{{'\\'}},				15, 1f,2f, 0.75f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Don't think about it."});
	public static MobInfo Mega_H			=new MobInfo("Mega H",			Type.Uncommon,	new char[][]{{'|','-','|'}},		15, 1f,2f, 0.75f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{""});
	public static MobInfo Shielded_Gnome1	=new MobInfo("Shielded Gnome",	Type.Uncommon,	new char[][]{{'[','d'}},			15, 1f,2f, 0.75f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Finally thinking rationally"});
	public static MobInfo Shielded_Gnome2	=new MobInfo("Shielded Gnome",	Type.Uncommon,	new char[][]{{'b',']'}},			15, 1f,2f, 0.75f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Finally thinking rationally"});
	
	
	public static MobInfo Salty_Sailor		=new MobInfo("Salty Sailor",	Type.Rare,		new char[][]{{'T'}},		 20, 1f,2f, 1f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Not having a great time"});
	public static MobInfo Yellow_Belly		=new MobInfo("Yellow Belly",	Type.Rare,		new char[][]{{'B'}},		 20, 1f,2f, 1f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Argg"});
	public static MobInfo Royal_Pain		=new MobInfo("Royal Pain",		Type.Rare,		new char[][]{{'R'}},		 20, 1f,2f, 1f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Just go away!"});
	public static MobInfo Penny_Pincher		=new MobInfo("Penny Pincher",	Type.Rare,		new char[][]{{'$'}},		 20, 1f,2f, 1f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Don't mess with it's money!"});
	public static MobInfo Walrus			=new MobInfo("Walrus", 			Type.Rare,		new char[][]{{'^','W','w'}}, 20, 1f,2f, 1f, new Spell[]{Spell.Shadowbolt}, Pathfinding.MoveToward,	new String[]{"Oh?"});
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public final String NAME;
	public final Type TYPE;
	public final Color COLOR;
	public final char[][] CHARACTERS;
	public final String[] STATUSES;
	
	public final int HEALTH;
	public final float SPEED, SHOT_SPEED, DAMAGE_RATIO;
	
	public final Pathfinding PATHFINDING;
	public final Spell[] SPELLS;
	
	private MobInfo(String name, Type type, Color col, char[][] chars, int h, float s, float ss, float damage, Spell[] spells, Pathfinding pf, String[] statuses){
		this.NAME=name;
		this.TYPE=type;
		this.COLOR=col;
		this.CHARACTERS=chars;
		this.STATUSES=statuses;
		this.HEALTH=h;
		this.SPEED=s;
		this.SHOT_SPEED=ss;
		this.DAMAGE_RATIO=damage;
		this.PATHFINDING = pf;
		this.SPELLS=spells;
		
		switch(type){
		case Common:
			if(CommonMobs==null){
				CommonMobs=new ArrayList<MobInfo>();
			}
			CommonMobs.add(this);
			//System.out.println("C"+CommonMobs.size());
			break;
		case Uncommon:
			if(UncommonMobs==null)
				UncommonMobs=new ArrayList<MobInfo>();
			UncommonMobs.add(this);
			//System.out.println("U"+UncommonMobs.size());
			break;
		case Rare:
			if(RareMobs==null)
				RareMobs=new ArrayList<MobInfo>();
			RareMobs.add(this);
			//System.out.println("R"+RareMobs.size());
			break;
		case Ultra:
			if(UltraMobs==null)
				UltraMobs=new ArrayList<MobInfo>();
			UltraMobs.add(this);
			//System.out.println("X"+UltraMobs.size());
			break;
		case Miniboss:
			if(MinibossMobs==null)
				MinibossMobs=new ArrayList<MobInfo>();
			MinibossMobs.add(this);
			//System.out.println("M"+MinibossMobs.size());
			break;
		case Boss:
			if(BossMobs==null)
				BossMobs=new ArrayList<MobInfo>();
			BossMobs.add(this);
			//System.out.println("B"+BossMobs.size());
			break;
		}
	}
	
	//Uses default color:   new Color(112,39,195)
	private MobInfo(String name, Type type, char[][] chars, int h, float s, float ss, float damage, Spell[] spells, Pathfinding pf, String[] statuses){this(name,type,type.color,chars,h,s,ss,damage,spells,pf,statuses);}
	
	public static enum Type{
		Common(1,10, new Color(112,39,195)),
		Uncommon(0.15f,15, new Color(64,23,231)),
		Rare(0.05f,30, new Color(31,44,208)),
		Ultra(0.01f,30, new Color(23,93,231)),
		Miniboss(0.001f,50, new Color(22,141,221)),
		Boss(0,100, new Color(12,212,244))
		;
		
		public float occuranceRate;
		public int expDrop;
		public Color color;
		private Type(float occurance, int exp, Color col){
			occuranceRate=occurance;
			expDrop=exp;
			color=col;
		}
	}
	
	public static ArrayList<MobInfo> CommonMobs;
	public static ArrayList<MobInfo> UncommonMobs;
	public static ArrayList<MobInfo> RareMobs;
	public static ArrayList<MobInfo> UltraMobs;
	public static ArrayList<MobInfo> MinibossMobs;
	public static ArrayList<MobInfo> BossMobs;
	
	public static MobInfo getMob(){
		if(Math.random()<Type.Miniboss.occuranceRate && MinibossMobs!=null && MinibossMobs.size()>0)
			return MinibossMobs.get((int)(Math.random()*MinibossMobs.size()));
		
		if(Math.random()<Type.Ultra.occuranceRate && UltraMobs!=null && UltraMobs.size()>0)
			return UltraMobs.get((int)(Math.random()*UltraMobs.size()));
		
		if(Math.random()<Type.Rare.occuranceRate && RareMobs!=null && RareMobs.size()>0)
			return RareMobs.get((int)(Math.random()*RareMobs.size()));
		
		if(Math.random()<Type.Uncommon.occuranceRate && UncommonMobs!=null && UncommonMobs.size()>0)
			return UncommonMobs.get((int)(Math.random()*UncommonMobs.size()));
		
		return CommonMobs.get((int)(Math.random()*CommonMobs.size()));
	}
	
	public static MobInfo getMobOfType(Type t){
		switch(t){
		case Common:
			return CommonMobs.get((int)(Math.random()*CommonMobs.size())); 
		case Uncommon:
			return UncommonMobs.get((int)(Math.random()*UncommonMobs.size()));
		case Rare:
			return RareMobs.get((int)(Math.random()*RareMobs.size()));
		case Ultra:
			return UltraMobs.get((int)(Math.random()*UltraMobs.size()));
		case Miniboss:
			return MinibossMobs.get((int)(Math.random()*MinibossMobs.size()));
		case Boss:
			return BossMobs.get((int)(Math.random()*BossMobs.size()));
		}
		return null;
	}
}
