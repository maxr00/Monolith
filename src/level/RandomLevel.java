package level;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entity.mob.BasicEnemy;
import entity.mob.Mob;
import entity.mob.MobInfo;
import game.Game;

public class RandomLevel extends Level {

	private static final Random rng = new Random();
	private int[][] colorBlemishes = new int[5][];
	
	private RTile[][] roomTiles;
	
	public RandomLevel(int width, int height) {
		super(width,height);
	}
	
	public static enum RTile{
			Wall_OUTOFBOUNDS(new char[]{'#','%','$'},new Color[]{Color.black},true),
			Wall_Stone(new char[]{'H','M','#'},new Color[]{Color.darkGray},true),
			Wall_Grass(new char[]{'W','w','v','V'},new Color[]{new Color(0x8ae75e)},true),
			
		Stone(new char[]{',','.','`',' ','\'',':',' ',' '},new Color[]{Color.gray},Wall_Stone),
		Dirt(new char[]{'\\',';',':','=','!','#',']','['},new Color[]{new Color(0x8f6b38)},Wall_Grass),
		Water(new char[]{' ',' ',' ',' ',' ','.'},new Color[]{Color.white},new Color[]{Color.blue},Wall_Stone),
		;
		public static RTile[] tiles(){
			List<RTile> tileList = new ArrayList<RTile>();
			for(RTile t : RTile.values())
				if(t.border!=null)
					tileList.add(t);
			RTile[] tiles = tileList.toArray(new RTile[tileList.size()]);
			return tiles;
		}
		char[] characters;
		Color[] colors, bg;
		RTile border;
		boolean isWall;
		RTile(char[] characters, Color[] colors, RTile border){
			this.characters=characters;
			this.colors=colors;
			this.border=border;
			this.bg=new Color[]{Color.black};
		}
		RTile(char[] characters, Color[] colors, Color[] bg, RTile border){
			this.characters=characters;
			this.colors=colors;
			this.bg=bg;
			this.border=border;
		}
		
		RTile(char[] characters, Color[] colors, boolean isWall){
			this.characters=characters;
			this.colors=colors;
			isWall=true;
			border=null;
		}
		
		static int indexOf(RTile tile){
			int i=0;
			for(RTile t : RTile.values()){
				if(t==tile)
					return i;
				i++;
			}
			return -1;
		}
	}
	
 	public void generateLevel() {
		int[][] colorBlemishes = LevelLight.getPossibleBlemishes(100);
		
		//Generation happens here
		while(roomCount<minRooms || endH==0 || endW==0){
			roomCount=0;
			endH=0;
			endW=0;
			endX=0;
			endY=0;
			mobs=new ArrayList<Mob>();
			roomTiles=new RTile[width][height];
			generate(10,10,width/2-5,height/2-5,RTile.Dirt);
		}
		
		//Find place for exit
		//
		//EMPTY CODE
		//
		
		world="";
		solids="";//0 or 1
		colors = null;
		
		colors=new Color[width*height];
		background = new Color[width*height];
		
		boolean border;
		for(int y=0;y<height;y++){
			for(int x=0;x<width;x++){
				if(roomTiles[x][y]!=null){
					world+=roomTiles[x][y].characters[rng.nextInt(roomTiles[x][y].characters.length)];
					if(x==0 || y==0 || x==width-1 || y==height-1)
						solids+="1";
					else
						solids+="0";
					
					colors[x+y*width]=roomTiles[x][y].colors[rng.nextInt(roomTiles[x][y].colors.length)];
					if(roomTiles[x][y].bg!=null)
						background[x+y*width]=roomTiles[x][y].bg[rng.nextInt(roomTiles[x][y].colors.length)];
				}else{
					border=false;
					for(int xx=-1;xx<2;xx++){
						if(border)break;
						for(int yy=-1;yy<2;yy++){
							if(border)break;
							if(x+xx<width && x+xx>=0 && y+yy<height && y+yy>=0 && (xx!=0 || yy!=0)){
								if(roomTiles[x+xx][y+yy]!=null){
									world+=roomTiles[x+xx][y+yy].border.characters[rng.nextInt(roomTiles[x+xx][y+yy].border.characters.length)];
									solids+="1";
									colors[x+y*width]=roomTiles[x+xx][y+yy].border.colors[rng.nextInt(roomTiles[x+xx][y+yy].border.colors.length)];
									border=true;
								}
							}
						}
					}
					if(!border){
						//world+=RTile.Wall_OUTOFBOUNDS.characters[rng.nextInt(RTile.Wall_OUTOFBOUNDS.characters.length)];						
						//colors[x+y*width]=RTile.Wall_OUTOFBOUNDS.colors[rng.nextInt(RTile.Wall_OUTOFBOUNDS.colors.length)];
						world+=" ";
						solids+="1";
						colors[x+y*width]=Color.white;
					}
				}
			}
		}
		
		int whiteSpace = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if(world.length()>x+y*width-whiteSpace && solids.length()>x+y*width-whiteSpace){
					if(world.charAt(x+y*width - whiteSpace)=='\n'){//if is next line
						whiteSpace += width - x - 1;
						break;
					}else //if(!Character.isWhitespace(world.charAt(x+y*width - whiteSpace)))//if not space
						tiles[x+y*width] = new Tile(RTile.indexOf(roomTiles[x][y]),world.charAt(x+y*width - whiteSpace),solids.charAt(x+y*width-whiteSpace)=='1',colors[x+y*width].getRGB(),background[x+y*width]!=null ? background[x+y*width].getRGB() : -1,colorBlemishes[rng.nextInt(colorBlemishes.length)]);
				}else break;
			}
		}
		
		levelLight.generateLevel(colors);
		levelLight.setTilesLight(tiles);
	}
	
	//Includes hallways
	int roomCount=0, maxRooms=50, minRooms=40;
	float enemyTileRatio=1/75f, mobChance=1/10f;
	int DistBetweenMobs=3;
	boolean hallway=false;
	
	int endW,endH,endX,endY;
	
	private void generate(int w,int h,int xStart,int yStart, RTile type){
		roomCount++;
		if(roomCount>maxRooms){
			for(int x=xStart;x<w+xStart;x++){
				for(int y=yStart;y<h+yStart;y++){
					if(x>=0 && y>=0 && x<roomTiles.length && y<roomTiles[x].length)
						roomTiles[x][y]=type;
				}
			}
			return;
		}
		
		for(int x=xStart;x<w+xStart;x++){
			for(int y=yStart;y<h+yStart;y++){
				if(x>=0 && y>=0 && x<roomTiles.length && y<roomTiles[x].length)
					roomTiles[x][y]=type;
			}
		}
		
		int numEnemies=roomCount!=1 ? (int)(enemyTileRatio*(w*h)) : 0;
		if(numEnemies>0)
			for(int x=xStart;x<w+xStart;x++){
				for(int y=yStart;y<h+yStart;y++){
					if(x>=0 && y>=0 && x<roomTiles.length && y<roomTiles[x].length && roomTiles[x][y]!=null)
						if(numEnemies>0 && Math.random()<mobChance){//(x!=width/2 || y!=height/2) &&
							numEnemies--;
							addEnemy(x,y);
						}
				}
			}
		
		hallway=!hallway;
		int nW, nH;
		int nX, nY;
		int xOff=0,yOff=0;
		int attempts=0;
		boolean skip=false;
		
		int doorSizeX=0, doorSizeY=0; 
		int doorX=0, doorY=0;
		
		while(true){
			if(Math.random()<0.5){
				//Top/Bottom expansion
				if(hallway){
					nW=2;
					nH=rng.nextInt(5)+10;
					if(roomCount==maxRooms-1){
						nW+=10;
						nH+=10;
					}
				}else{
					nW=rng.nextInt(25)+5;
					nH=rng.nextInt(20)+5;
					if(roomCount==maxRooms-1){
						nW+=10;
						nH+=10;
					}
					xOff=rng.nextInt(nW-1);
					yOff=0;
				}
				nX=rng.nextInt(w-1);
				nY=0;
				if(Math.random()<0.5){//Top
					nY=-nH;
					if( canFit(2,1,nX+xStart,yStart-1)){
						doorSizeX=2;
						doorSizeY=1;
						doorX=nX+xStart;
						doorY=yStart-1;
						nY--;
					}else{
						attempts++;
						if(attempts>9999){
							skip=true;
							break;
						}
						continue;
					}
				}else{//Bottom
					nY=h;
					if( canFit(2,1,nX+xStart,nY+yStart)){
						doorSizeX=2;
						doorSizeY=1;
						doorX=nX+xStart;
						doorY=nY+yStart;
						nY++;
					}else{
						attempts++;
						if(attempts>9999){
							skip=true;
							break;
						}
						continue;
					}
				}
			}else{
				//L/R expansion
				if(hallway){
					nH=2;
					nW=rng.nextInt(5)+10;
					if(roomCount==maxRooms-1){
						nW+=10;
						nH+=10;
					}
				}else{
					nH=rng.nextInt(25)+5;
					nW=rng.nextInt(20)+5;
					if(roomCount==maxRooms-1){
						nW+=10;
						nH+=10;
					}
					xOff=0;
					yOff=rng.nextInt(nH-1);
				}
				nX=0;
				nY=rng.nextInt(h-1);
				if(Math.random()<0.5){//L
					nX=-nW;
					if( canFit(1,2,xStart-1,nY+yStart)){
						doorSizeX=1;
						doorSizeY=2;
						doorX=xStart-1;
						doorY=nY+yStart;
						nX--;
					}else{
						attempts++;
						if(attempts>9999){
							skip=true;
							break;
						}
						continue;
					}
				}else{//R
					nX=w;
					if(canFit(1,2,nX+xStart,nY+yStart)){
						doorSizeX=1;
						doorSizeY=2;
						doorX=nX+xStart;//-xOff;
						doorY=nY+yStart;//-yOff;
						nX++;
					}else{
						attempts++;
						if(attempts>9999){
							skip=true;
							break;
						}
						continue;
					}
				}
			}
			if(canFit(nW+2,nH+2,nX+xStart-xOff-1,nY+yStart-yOff-1)){
				if(roomCount==maxRooms-1){
					endW=nW;
					endH=nH;
					endX=nX;
					endY=nY;
				}
				break;
			}else{
				attempts++;
				if(attempts>9999){
					skip=true;
					break;
				}
			}
		}
		if(!skip){
			addDoor(doorSizeX,doorSizeY,doorX,doorY,RTile.Dirt);
			generate(nW,nH,nX+xStart-xOff,nY+yStart-yOff,Math.random()<0.5f ? RTile.Stone : RTile.Water);//RTile.tiles()[rng.nextInt(RTile.tiles().length)]);
		}
	}
	
	private void addDoor(int w,int h,int xStart,int yStart,RTile type){
		roomCount++;
		if(roomCount>maxRooms)
			return;
		
		for(int x=xStart;x<w+xStart;x++){
			for(int y=yStart;y<h+yStart;y++){
				if(x>=0 && y>=0 && x<roomTiles.length && y<roomTiles[x].length)
					roomTiles[x][y]=type;
			}
		}
	}
	
	private boolean canFit(int w, int h, int xStart, int yStart) {
		for(int x=xStart;x<w+xStart;x++){
			for(int y=yStart;y<h+yStart;y++){
				if(x>=0 && y>=0 && x<roomTiles.length && y<roomTiles[x].length){
					if(roomTiles[x][y]!=null){
						return false;
					}
				}else
					return false;
			}
		}
		return true;
	}
	
	private void addEnemy(int x,int y){
		int character=rng.nextInt(94)+33;
		//No commas or |s (causes problems sometimes)!
		//while((char)character==',' || (char)character=='|' || (char)character=='/'){character=rng.nextInt(94)+33;}
		
		MobInfo mobInfo=null;
		while(mobInfo==null)
			mobInfo = MobInfo.getMob();
		
		Mob mob=new BasicEnemy(Game.game.level,x,y,mobInfo.NAME,mobInfo.CHARACTERS,mobInfo.PATHFINDING,mobInfo.SPELLS,mobInfo.HEALTH,mobInfo.SPEED,mobInfo.SHOT_SPEED,mobInfo.DAMAGE_RATIO, mobInfo.COLOR,mobInfo.STATUSES);//(char)(random.nextInt(94)+33));
		
		//Packet14AddMob mobPacket = new Packet14AddMob(mob.x/Game.TILE_SIZE,mob.y/Game.TILE_SIZE,mob.Health,mob.name,mob.characters,mob.spells,mob.identifier,mobInfo.STATUSES);
		//mobPacket.writeData(Game.game.socketServer);
		
		x+=DistBetweenMobs;
	}

	
}
