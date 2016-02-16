package entity.mob;

import java.awt.Color;

public class MobInfo {

	
	public static MobInfo Slippery_Snake=new MobInfo("Slippery Snake", Type.Common, new char[][]{{'~'}}   );
	public static MobInfo Salty_Sailor=new MobInfo("Salty Sailor", Type.Common, new char[][]{{'T'}}   );
	public static MobInfo Avergae_Monster=new MobInfo("Average Monster", Type.Common, new char[][]{{'M'}}   );
	public static MobInfo Living_Bucket=new MobInfo("Living Bucket", Type.Common, new char[][]{{'u'}}   );
	public static MobInfo Yellow_Belly=new MobInfo("Yellow Belly", Type.Common, new char[][]{{'B'}}   );
	public static MobInfo Jack_OLantern=new MobInfo("Jack O'Lantern", Type.Common, new char[][]{{'P'}}   );
	public static MobInfo Royal_Pain=new MobInfo("Royal Pain", Type.Common, new char[][]{{'R'}}   );
	public static MobInfo Quack=new MobInfo("Quack", Type.Common, new char[][]{{'Q'}}   );
	public static MobInfo George=new MobInfo("George", Type.Common, new char[][]{{'G'}});
	
	
	public static MobInfo Lost_Traveller=new MobInfo("Lost Traveller", Type.Uncommon, new char[][]{{'/','#'}});

	
	public static MobInfo Walrus=new MobInfo("Walrus", Type.Rare, new char[][]{{'^','W','w'}});
	
	
	
	
	
	private MobInfo(String name, Type type, Color col, char[][] chars){
		
	}
	
	//Uses default color:   new Color(112,39,195)
	private MobInfo(String name, Type type, char[][] chars){
		this(name,type,new Color(112,39,195),chars);
	}
	
	private static enum Type{
		Common(1,10),
		Uncommon(0.40f,15),
		Rare(0.1f,30),
		Miniboss(0.05f,50),
		Boss(0,100)
		;
		
		public float occuranceRate;
		public int expDrop;
		private Type(float occurance, int exp){
			occuranceRate=occurance;
			expDrop=exp;
		}
	}
}
