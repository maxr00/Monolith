package level;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entity.mob.BasicEnemy;
import entity.mob.Mob;
import entity.mob.MobInfo;
import game.Game;
import graphics.Screen;

public class RandomLevel extends Level {

	private static final Random rng=new Random();
	private int[][] colorBlemishes = new int[5][];
	
	private RTile[][] roomTiles;
	
	private float[][] perlinNoise;

	private int maxRooms=200, minRooms=180;
	
	public RandomLevel(int width, int height, long seed) {
		super(width,height);
		this.seed=seed;
		rng.setSeed(seed);
		
		maxRooms=50*(int)(width/100f);
		minRooms=maxRooms-10;
		
		//																								   Scale,Smoothness
		perlinNoise=this.GeneratePerlinNoise(this.GenerateSmoothNoise(this.GenerateWhiteNoise(width, height), 0),4);
		
		//generateLevel();
		generateFloor();
	}
	
	public static enum RTile{
			Wall_OUTOFBOUNDS(new char[]{'#','%','$'},new Color[]{Color.black},true),
			Wall_Stone(new char[]{'H','M','#'},new Color[]{Color.darkGray},true),
			Wall_Grass(new char[]{'W','w','v','V'},new Color[]{new Color(0x8ae75e)},true),
			
		Stone(new char[]{',','.','`',' ','\'',':',' ',' '},new Color[]{Color.gray},Wall_Stone),
		Dirt(new char[]{'\\',';',':','=','!','#',']','['},new Color[]{new Color(0x8f6b38)},Wall_Grass),
		Door(new char[]{'[',']',')','(','\\','/','_','='},new Color[]{new Color(0x8f6b38)},Wall_Stone),
		Water(new char[]{' ',' ',' ',' ',' ','.'},new Color[]{Color.white},new Color[]{new Color(47,71,225)},Wall_Stone),
		;
		public static RTile[] tiles(){
			if(tiles==null){
				List<RTile> tileList = new ArrayList<RTile>();
				for(RTile t : RTile.values())
					if(t.border!=null)
						tileList.add(t);
				tiles = tileList.toArray(new RTile[tileList.size()]);
			}
			return tiles;
		}
		private static RTile[] tiles;
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
		static int tileIndex(RTile tile){
			int i=0;
			for(RTile t : RTile.tiles()){
				if(t==tile)
					return i;
				i++;
			}
			return -1;
		}
	}
	
 	public void generateLevel() {
		int[][] colorBlemishes = LevelLight.getPossibleBlemishes(100,seed);
		
		//Generation happens here
		while(roomCount<minRooms || endH==0 || endW==0){
			roomCount=0;
			endH=0;
			endW=0;
			endX=0;
			endY=0;
			mobs.clear();
			mobs=new ArrayList<Mob>();
			roomTiles=new RTile[width][height];
			System.out.println("Generating...");
			generate(10,10,width/2-5,height/2-5,RTile.Dirt,false);
		}
		System.out.println("Done!");
		
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
		
		/*for(int x=0;x<width;x++)
			for(int y=0;y<height;y++){
				background[x+y*width]=new Color(perlinNoise[x][y],0,0);
			}*/
		
		int whiteSpace = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if(world.length()>x+y*width-whiteSpace && solids.length()>x+y*width-whiteSpace){
					if(world.charAt(x+y*width - whiteSpace)=='\n'){//if is next line
						whiteSpace += width - x - 1;
						break;
					}else //if(!Character.isWhitespace(world.charAt(x+y*width - whiteSpace)))//if not space
						tiles[x+y*width] = new Tile(RTile.indexOf(roomTiles[x][y]),x,y,world.charAt(x+y*width - whiteSpace),solids.charAt(x+y*width-whiteSpace)=='1',colors[x+y*width].getRGB(),background[x+y*width]!=null ? background[x+y*width].getRGB() : -1,colorBlemishes[rng.nextInt(colorBlemishes.length)]);
				}else break;
			}
		}
		
		levelLight.generateLevel(colors);
		levelLight.setTilesLight(tiles);
		
		System.out.println("Level Created");
	}
	
	//Includes hallways
	int roomCount=0;
	float enemyTileRatio=1/75f, mobChance=1/10f;
	int DistBetweenMobs=3;
	boolean hallway=false;
	
	int endW,endH,endX,endY;
	
	private void generate(int w,int h,int xStart,int yStart, RTile type, boolean spawnEnemies){
		roomCount++;
		if(roomCount>maxRooms){
			for(int x=xStart;x<w+xStart;x++){
				for(int y=yStart;y<h+yStart;y++){
					if(x>=0 && y>=0 && x<width && y<height)
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
		
		/*if(spawnEnemies){
			int numEnemies=(int)(enemyTileRatio*(w*h));
			if(numEnemies>0)
				for(int x=xStart;x<w+xStart;x++){
					for(int y=yStart;y<h+yStart;y++){
						if(x>=0 && y>=0 && x<roomTiles.length && y<roomTiles[x].length && roomTiles[x][y]!=null)
							if(numEnemies>0 && rng.nextFloat()<mobChance){//(x!=width/2 || y!=height/2) &&
								numEnemies--;
								addEnemy(x,y);
							}
					}
				}
		}*/
		
		hallway=!hallway;
		int nW, nH;
		int nX, nY;
		int xOff=0,yOff=0;
		int attempts=0;
		boolean skip=false;
		
		int doorSizeX=0, doorSizeY=0; 
		int doorX=0, doorY=0;
		
		while(true){
			if(rng.nextFloat()<0.5){
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
				if(rng.nextFloat()<0.5){//Top
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
				if(rng.nextFloat()<0.5){//L
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
			addDoor(doorSizeX,doorSizeY,doorX,doorY,RTile.Door);
			generate(nW,nH,nX+xStart-xOff,nY+yStart-yOff,rng.nextFloat()<0.5f ? RTile.Stone : RTile.Water,true);//RTile.tiles()[rng.nextInt(RTile.tiles().length)]);
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
	
	private int tilesPerEnemy = 200, distBetweenEnemies = 5;
	public void generateEnemies(){
		int numEnemies = (width*height) / tilesPerEnemy;
		int attemptCount=0, attemptsToLower=100;
		
		int origDist=distBetweenEnemies;
		
		boolean canPlace;
		for(int enemyCount=0;enemyCount<numEnemies;attemptCount++){
			
			if(attemptCount>=attemptsToLower){
				if(distBetweenEnemies>0)
					distBetweenEnemies--;
				else
					numEnemies--;
				attemptCount=0;
			}
			
			int x=rng.nextInt(width), y=rng.nextInt(height);
			canPlace=true;
			
			if(this.getTile(x, y)==null || this.getTile(x, y).isSolid())
				canPlace=false;

			if(canPlace)
				if(x<width/2+10 && x>width/2-10 && y<height/2+10 && y>height/2-10) //Starting area
					canPlace=false;
			
			if(canPlace)
				for(int i=-distBetweenEnemies;i<distBetweenEnemies;i++){
					for(int j=-distBetweenEnemies;j<distBetweenEnemies;j++){
						if(x+i>=0 && x+i<width && j+y>=0 && j+y<height)
							if(this.getMobOn(x+i, y+j)!=null) canPlace=false;
					}
				}
			if(canPlace){
				addEnemy(x,y);
				enemyCount++;
			}
		}
		distBetweenEnemies=origDist;
	}
	
	private void addEnemy(int x,int y){
		//int character=rng.nextInt(94)+33;
		//No commas or |s (causes problems sometimes)!
		//while((char)character==',' || (char)character=='|' || (char)character=='/'){character=rng.nextInt(94)+33;}
		
		MobInfo mobInfo=null;
		while(mobInfo==null)
			mobInfo = MobInfo.getMob();
		
		//Mob mob=
		new BasicEnemy(Game.game.level,x,y,mobInfo.NAME,mobInfo.CHARACTERS,mobInfo.PATHFINDING,mobInfo.SPELLS,mobInfo.HEALTH,mobInfo.SPEED,mobInfo.SHOT_SPEED,mobInfo.DAMAGE_RATIO, mobInfo.COLOR,mobInfo.STATUSES);//(char)(random.nextInt(94)+33));
		
		//Packet14AddMob mobPacket = new Packet14AddMob(mob.x/Game.TILE_SIZE,mob.y/Game.TILE_SIZE,mob.Health,mob.name,mob.characters,mob.spells,mob.identifier,mobInfo.STATUSES);
		//mobPacket.writeData(Game.game.socketServer);
		
		//x+=DistBetweenMobs;
	}

	
	
	//White, smooth, and perlin noise code source: http://devmag.org.za/2009/04/25/perlin-noise/
	float[][] GenerateWhiteNoise(int width, int height) {
	    Random random = new Random(0); //Seed to 0 for testing
	    float[][] noise = new float[width][height];//GetEmptyArray(width, height);
	 
	    for (int i = 0; i < width; i++){
	        for (int j = 0; j < height; j++){
	            noise[i][j] = (float)random.nextDouble() % 1;
	        }
	    }
	 
	    return noise;
	}
	
	float[][] GenerateSmoothNoise(float[][] baseNoise, int octave){
	   int width = baseNoise.length;
	   int height = baseNoise[0].length;
	 
	   float[][] smoothNoise = new float[width][height];//GetEmptyArray(width, height);
	 
	   int samplePeriod = 1 << octave; // calculates 2 ^ k
	   float sampleFrequency = 1.0f / samplePeriod;
	 
	   for (int i = 0; i < width; i++){
	      //calculate the horizontal sampling indices
	      int sample_i0 = (i / samplePeriod) * samplePeriod;
	      int sample_i1 = (sample_i0 + samplePeriod) % width; //wrap around
	      float horizontal_blend = (i - sample_i0) * sampleFrequency;
	 
	      for (int j = 0; j < height; j++) {
	         //calculate the vertical sampling indices
	         int sample_j0 = (j / samplePeriod) * samplePeriod;
	         int sample_j1 = (sample_j0 + samplePeriod) % height; //wrap around
	         float vertical_blend = (j - sample_j0) * sampleFrequency;
	 
	         //blend the top two corners
	         float top = Interpolate(baseNoise[sample_i0][sample_j0],
	            baseNoise[sample_i1][sample_j0], horizontal_blend);
	 
	         //blend the bottom two corners
	         float bottom = Interpolate(baseNoise[sample_i0][sample_j1],
	            baseNoise[sample_i1][sample_j1], horizontal_blend);
	 
	         //final blend
	         smoothNoise[i][j] = Interpolate(top, bottom, vertical_blend);
	      }
	   }
	 
	   return smoothNoise;
	}
	
	float Interpolate(float x0, float x1, float alpha){return x0 * (1 - alpha) + alpha * x1;}
	
	float[][] GeneratePerlinNoise(float[][] baseNoise, int octaveCount){
	   int width = baseNoise.length;
	   int height = baseNoise[0].length;
	 
	   float[][][] smoothNoise = new float[octaveCount][][]; //an array of 2D arrays containing
	 
	   float persistance = 0.5f;
	 
	   //generate smooth noise
	   for (int i = 0; i < octaveCount; i++){
	       smoothNoise[i] = GenerateSmoothNoise(baseNoise, i);
	   }
	 
	    float[][] perlinNoise = new float[width][height];
	    float amplitude = 1.0f;
	    float totalAmplitude = 0.0f;
	 
	    //blend noise together
	    for (int octave = octaveCount - 1; octave >= 0; octave--){
	       amplitude *= persistance;
	       totalAmplitude += amplitude;
	 
	       for (int i = 0; i < width; i++){
	          for (int j = 0; j < height; j++){
	             perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
	          }
	       }
	    }
	 
	   //normalisation
	   for (int i = 0; i < width; i++){
	      for (int j = 0; j < height; j++){
	         perlinNoise[i][j] /= totalAmplitude;
	      }
	   }
	 
	   return perlinNoise;
	}
	
	
	
	
	
	
	
	
	
	
	
	enum LevelType{
		Normal		(0.6f),
		Flooded		(0.1f),
		Overgrown	(0.2f),
		Found		(0.05f),
		Abandoned	(0.05f),
		
		Overworld	(0.5f),
		;
		public float chance;
		LevelType(float chance){this.chance=chance;}
	}
	enum RoomType{
		Normal,
		Double,
		Circle,
		Flooded,
		Overgrown,
		Natural,
		Collapsed,
		Abandoned,
		
		Boss
		;
		static int getIndexOf(RoomType t){for(int i=0;i<RoomType.values().length;i++)if(t==RoomType.values()[i])return i;return -1;}
	}
	enum HallwayType{
		Normal,
	}
	
	enum Dir{up,down,left,right}
	
	float[][] natural;
	private int Floor=0;
	void generateFloor(){
		Floor++;
		LevelType LType=getLevelType(Floor);
		float[] possibleRoomsChances=getRoomChances(LType);
		int[][] colorBlemishes = LevelLight.getPossibleBlemishes(100,seed);
		
		roomTiles=new RTile[width][height];
		
		//make rooms
		RTile[][] startRoom=new RTile[10][10];
		for(int i=0;i<startRoom.length;i++)
			for(int j=0;j<startRoom.length;j++)
				//Just has to be not null
				startRoom[i][j]=RTile.Stone;
		
		natural=this.GeneratePerlinNoise(this.GenerateSmoothNoise(this.GenerateWhiteNoise(width, height), 3),4);
		
		while(generateRoom(width/2-5,height/2-5,10,10,getRandomRoom(possibleRoomsChances),startRoom,possibleRoomsChances,maxRooms) > maxRooms-minRooms){
			roomTiles=new RTile[width][height];
		}
		
		System.out.println("Done!");
		
		//Place exit
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
		
		/*for(int x=0;x<width;x++)
			for(int y=0;y<height;y++){
				background[x+y*width]=new Color(perlinNoise[x][y],0,0);
			}*/
		
		int whiteSpace = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if(world.length()>x+y*width-whiteSpace && solids.length()>x+y*width-whiteSpace){
					if(world.charAt(x+y*width - whiteSpace)=='\n'){//if is next line
						whiteSpace += width - x - 1;
						break;
					}else //if(!Character.isWhitespace(world.charAt(x+y*width - whiteSpace)))//if not space
						tiles[x+y*width] = new Tile(RTile.indexOf(roomTiles[x][y]),x,y,world.charAt(x+y*width - whiteSpace),solids.charAt(x+y*width-whiteSpace)=='1',colors[x+y*width].getRGB(),background[x+y*width]!=null ? background[x+y*width].getRGB() : -1,colorBlemishes[rng.nextInt(colorBlemishes.length)]);
				}else break;
			}
		}
		
		levelLight.generateLevel(colors);
		levelLight.setTilesLight(tiles);
		
		System.out.println("Level Created");
	}
	
	//Number of rooms left to generate before generation ended is returned
	//553 Lines of code for room generation
	int generateRoom(int x,int y,int w,int h,RoomType type,RTile[][] rtiles,float[] roomChances,int numRoomsToGen){
		
		if(numRoomsToGen==0) return 0;
		
		//Place room tiles (only if tile is null, do this because branching paths need to place a room twice)
		for(int i=0;i<w;i++)
			for(int j=0;j<h;j++)
				if(roomTiles[i+x][j+y]==null && rtiles[i][j]!=null)
					roomTiles[i+x][j+y]=rtiles[i][j];
		
		//Take in array to place,dont generate it here.
		
		
		int hX=0,hY=0,hW=0,hH=0;
		int hsX=0,hsY=0;
		int attempts=0;
		RTile[][] hTiles = null;
		RTile[][] ehTiles = null;
		
		HallwayType hallwayType;
		Dir hallwayDir;
		Dir doorDir;
		int hWidth = 2;
		
		int nX=0,nY=0,nW=0,nH=0;
		int hXe=0,hYe=0,hWe=0,hHe=0;
		RoomType roomType=null;
		RTile[][] rTiles = null;
		while(true){
			attempts++;
			if(attempts > 9999){return numRoomsToGen;}
			
			//Generate Hallway and Door to next room
			hallwayType = HallwayType.values()[rng.nextInt(HallwayType.values().length)];
			//Dir hallway opens into
			hallwayDir = Dir.values()[rng.nextInt(Dir.values().length)];
			//Dir door opens into next room
			doorDir = hallwayDir;
			//NOTE: Should get 2 doors if not short hallway, one opening into the hallway, one from hallway to new room. if curved, the doorDir will change.
			//NOTE: If short hallway, it is just a door into the next room
			
			int maxLength = 10;
			float[] tileChances;
			tileChances = getTileChance(RoomType.Normal);
			//Don't need to fit, added on after
			hXe=0;hYe=0;hWe=0;hHe=0;
			switch(hallwayType){
			case Normal:
				maxLength = 10;
				hWidth = 2;
				if(hallwayDir==Dir.up || hallwayDir==Dir.down){
					hW=hWidth;
					hH=rng.nextInt(maxLength)+3;
					//Calculate where the hallway will be placed in level.
					hX=x+rng.nextInt(w-hW);
					if(hallwayDir==Dir.up)
						hY=y-hH;
					else
						hY=y+h;
					
					if(type==RoomType.Circle) hX= x+w/2;
				}else{
					hW=rng.nextInt(maxLength)+1;
					hH=hWidth;
					//Calculate where the hallway will be placed in level.
					hY=y+rng.nextInt(h-hH);
					if(hallwayDir==Dir.left)
						hX=x-hW;
					else
						hX=x+w;
					
					if(type==RoomType.Circle) hY= y+h/2;
				}
				
				//get RTile[] for hallway
				hTiles=new RTile[hW][hH];
				for(int i=0;i<hW;i++)
					for(int j=0;j<hH;j++){
						//Place doors in hallway
						if((j==0 || j==hH-1) && (doorDir==Dir.up || doorDir==Dir.down))
							hTiles[i][j]=RTile.Door;
						else if((i==0 || i==hW-1) && (doorDir==Dir.left || doorDir==Dir.right))
							hTiles[i][j]=RTile.Door;
						else
							hTiles[i][j]=getRandomTile(tileChances);
					}
				hsX=hX;
				hsY=hY;
				break;
			}
			if(!canFit(hsX,hsY,hW,hH,hTiles)) continue;
		
			
			if(type==RoomType.Abandoned || type==RoomType.Collapsed || type==RoomType.Overgrown || type==RoomType.Double || type==RoomType.Natural){
				if(hallwayDir==Dir.left || hallwayDir==Dir.right){
					hWe = hW+w+nW;//w;//hWe+w/2;
					hHe = hH;
					hXe = hX-nW;
					hYe = hY;
					if(hallwayDir==Dir.right)
						hXe = hX-w;//-hW;//-w-w;
				}else{
					hHe = hH+h+nH ;
					hWe = hW;
					hYe = hY-nH;
					hXe = hX;
					if(hallwayDir==Dir.down)
						hYe=hY-h;
				}
				
				ehTiles=new RTile[hWe][hHe];
				for(int i=0;i<hWe;i++)
					for(int j=0;j<hHe;j++){
						if(hXe+i<width && hYe+j<height && hYe>=0 && hXe>=0)
							ehTiles[i][j]=getRandomTile(tileChances);
					}
			}

			
			//
			//
			//Now room generation:::::::::::::::::::::::
			//
			
		//Generate Next room
		roomType = getRandomRoom(roomChances); //get room type from possible rooms;
		
		if(numRoomsToGen==0){
			//Generate Boss Room
			roomType=RoomType.Boss;
		}

		//DEBUG
		//roomType = RoomType.Double;
		
		tileChances = getTileChance(roomType);
			
			switch(roomType){
			case Normal:
				//get random w and h
				nW=rng.nextInt(20)+5;
				nH=rng.nextInt(20)+5;
				rTiles=new RTile[nW][nH];
				//calculate where new X and Y are depending on doorDir
				switch(doorDir){
					case up:
						nY=hsY-nH;
						nX=hsX-rng.nextInt(nW-hWidth);
						break;
					case down:
						nY=hsY+hH;
						nX=hsX-rng.nextInt(nW-hWidth);
						break;
					case left:
						nX=hsX-nW;
						nY=hsY-rng.nextInt(nH-hWidth);
						break;
					case right:
						nX=hsX+hW;
						nY=hsY-rng.nextInt(nH-hWidth);
						break;
				}
				//Get RTile[][]
				for(int i=0;i<nW;i++)
					for(int j=0;j<nH;j++){
						rTiles[i][j]=getRandomTile(tileChances);
					}
				break;
			case Circle:
				//get random w and h
				nW=nH=rng.nextInt(20)+5;
				rTiles=new RTile[nW][nH];
				//calculate where new X and Y are depending on doorDir
				switch(doorDir){
					case up:
						nY=hsY-nH;
						nX=hsX-(nW/2);//-rng.nextInt(nW-hWidth);
						break;
					case down:
						nY=hsY+hH;
						nX=hsX-(nW/2);//-rng.nextInt(nW-hWidth);
						break;
					case left:
						nX=hsX-nW;
						nY=hsY-(nH/2);//-rng.nextInt(nH-hWidth);
						break;
					case right:
						nX=hsX+hW;
						nY=hsY-(nH/2);//rng.nextInt(nH-hWidth);
						break;
				}
				//Get RTile[][]
				for(int i=0;i<nW;i++)
					for(int j=0;j<nH;j++){
						if( (i-nW/2f)*(i-nW/2f) + (j-nH/2f)*(j-nH/2f) < (nW/2f)*(nW/2f)+2)
							rTiles[i][j]=getRandomTile(tileChances);
						else{
							rTiles[i][j]=null;
						}
					}
				break;
			case Double:
				// separate one big room by making 1 or 2 dividers randomly placed in the room(make sure the dividers are the size of the room)
				
				//get random w and h
				nW=rng.nextInt(20)+5;
				nH=rng.nextInt(20)+5;
				rTiles=new RTile[nW][nH];
				//calculate where new X and Y are depending on doorDir
				switch(doorDir){
					case up:
						nY=hsY-nH;
						nX=hsX-rng.nextInt(nW-hWidth);
						break;
					case down:
						nY=hsY+hH;
						nX=hsX-rng.nextInt(nW-hWidth);
						break;
					case left:
						nX=hsX-nW;
						nY=hsY-rng.nextInt(nH-hWidth);
						break;
					case right:
						nX=hsX+hW;
						nY=hsY-rng.nextInt(nH-hWidth);
						break;
				}
				
				
				//Get RTile[][]
				for(int i=0;i<nW;i++)
					for(int j=0;j<nH;j++){
						rTiles[i][j]=getRandomTile(tileChances);
					}
				
				
				int dnW=rng.nextInt(nW-4)+3;
				int dnH=rng.nextInt(nH-4)+3;
				int dnX=rng.nextInt(nW-dnW-1)+1;
				int dnY=rng.nextInt(nH-dnH-1)+1;
				for(int i=0;i<dnW;i++)
					for(int j=0;j<dnH;j++){
						rTiles[i+dnX][j+dnY]=null;
					}
				dnW=rng.nextInt(nW-4)+3;
				dnH=rng.nextInt(nH-4)+3;
				dnX=rng.nextInt(nW-dnW-1)+1;
				dnY=rng.nextInt(nH-dnH-1)+1;
				for(int i=0;i<dnW;i++)
					for(int j=0;j<dnH;j++){
						rTiles[i+dnX][j+dnY]=null;
					}
				if(hallwayDir==Dir.left || hallwayDir==Dir.right){
					hWe = hW+w+nW;//w;//hWe+w/2;
					hHe = hH;
					hXe = hX-nW;
					hYe = hY;
					if(hallwayDir==Dir.right)
						hXe = hX-w;//-hW;//-w-w;
				}else{
					hHe = hH+h+nH ;
					hWe = hW;
					hYe = hY-nH;
					hXe = hX;
					if(hallwayDir==Dir.down)
						hYe=hY-h;
				}
				
				ehTiles=new RTile[hWe][hHe];
				for(int i=0;i<hWe;i++)
					for(int j=0;j<hHe;j++){
						if(hXe+i<width && hYe+j<height && hYe>=0 && hXe>=0)
							ehTiles[i][j]=getRandomTile(tileChances);
					}
				
				break;
			case Flooded:
				//get random w and h
				nW=rng.nextInt(20)+5;
				nH=rng.nextInt(20)+5;
				rTiles=new RTile[nW][nH];
				//calculate where new X and Y are depending on doorDir
				switch(doorDir){
					case up:
						nY=hsY-nH;
						nX=hsX-rng.nextInt(nW-hWidth);
						break;
					case down:
						nY=hsY+hH;
						nX=hsX-rng.nextInt(nW-hWidth);
						break;
					case left:
						nX=hsX-nW;
						nY=hsY-rng.nextInt(nH-hWidth);
						break;
					case right:
						nX=hsX+hW;
						nY=hsY-rng.nextInt(nH-hWidth);
						break;
				}
				//Get RTile[][]
				for(int i=0;i<nW;i++)
					for(int j=0;j<nH;j++){
						if(nX+i>=0 && nY+j>=0 && nX+i<this.width && nY+j<this.height && perlinNoise[nX+i][nY+j]<0.75f){
							rTiles[i][j]=RTile.Water;
						}else rTiles[i][j]=getRandomTile(tileChances);
					}
				break;
			case Overgrown:
				//get random w and h
				nW=rng.nextInt(20)+5;
				nH=rng.nextInt(20)+5;
				rTiles=new RTile[nW][nH];
				//calculate where new X and Y are depending on doorDir
				switch(doorDir){
					case up:
						nY=hsY-nH;
						nX=hsX-rng.nextInt(nW-hWidth);
						break;
					case down:
						nY=hsY+hH;
						nX=hsX-rng.nextInt(nW-hWidth);
						break;
					case left:
						nX=hsX-nW;
						nY=hsY-rng.nextInt(nH-hWidth);
						break;
					case right:
						nX=hsX+hW;
						nY=hsY-rng.nextInt(nH-hWidth);
						break;
				}
				//Get RTile[][]
				for(int i=0;i<nW;i++)
					for(int j=0;j<nH;j++){
						if(nX+i>=0 && nY+j>=0 && nX+i<this.width && nY+j<this.height && perlinNoise[nX+i][nY+j]>0.6f){
							rTiles[i][j]=RTile.Dirt;
						}else rTiles[i][j]=getRandomTile(tileChances);
					}
				break;
			case Natural:
				//get random w and h
				nW=rng.nextInt(10)+10;
				nH=rng.nextInt(10)+10;
				rTiles=new RTile[nW][nH];
				//calculate where new X and Y are depending on doorDir
				switch(doorDir){
					case up:
						nY=hsY-nH;
						nX=hsX-rng.nextInt(nW-hWidth);
						break;
					case down:
						nY=hsY+hH;
						nX=hsX-rng.nextInt(nW-hWidth);
						break;
					case left:
						nX=hsX-nW;
						nY=hsY-rng.nextInt(nH-hWidth);
						break;
					case right:
						nX=hsX+hW;
						nY=hsY-rng.nextInt(nH-hWidth);
						break;
				}
				
				if(hallwayDir==Dir.left || hallwayDir==Dir.right){
					hWe = hW+w+nW;//w;//hWe+w/2;
					hHe = hH;
					hXe = hX-nW;
					hYe = hY;
					if(hallwayDir==Dir.right)
						hXe = hX-w;//-hW;//-w-w;
				}else{
					hHe = hH+h+nH ;
					hWe = hW;
					hYe = hY-nH;
					hXe = hX;
					if(hallwayDir==Dir.down)
						hYe=hY-h;
				}
				ehTiles=new RTile[hWe][hHe];
				for(int i=0;i<hWe;i++)
					for(int j=0;j<hHe;j++){
						if(hXe+i<width && hYe+j<height && hYe>=0 && hXe>=0 && natural[hXe+i][hYe+j]>0.55f)
							ehTiles[i][j]=getRandomTile(tileChances);
						else
							ehTiles[i][j]=RTile.Dirt;
					}
				
				for(int i=0;i<nW;i++)
					for(int j=0;j<nH;j++){
						if(nX+i>=0 && nY+j>=0 && nX+i<this.width && nY+j<this.height && natural[nX+i][nY+j]>0.5f){
							if(natural[nX+i][nY+j]>0.55f)
								rTiles[i][j]=getRandomTile(tileChances);
							else
								rTiles[i][j]=RTile.Dirt;
						}
					}
				break;
			case Collapsed:
				//get random w and h
				nW=rng.nextInt(20)+5;
				nH=rng.nextInt(20)+5;
				rTiles=new RTile[nW][nH];
				//calculate where new X and Y are depending on doorDir
				switch(doorDir){
					case up:
						nY=hsY-nH;
						nX=hsX-rng.nextInt(nW-hWidth);
						break;
					case down:
						nY=hsY+hH;
						nX=hsX-rng.nextInt(nW-hWidth);
						break;
					case left:
						nX=hsX-nW;
						nY=hsY-rng.nextInt(nH-hWidth);
						break;
					case right:
						nX=hsX+hW;
						nY=hsY-rng.nextInt(nH-hWidth);
						break;
				}
				
				if(hallwayDir==Dir.left || hallwayDir==Dir.right){//type==RoomType.Natural || type==RoomType.Collapsed){
					hWe = hW+w+nW;//w;//hWe+w/2;
					hHe = hH;
					hXe = hX-nW;
					hYe = hY;
					if(hallwayDir==Dir.right)
						hXe = hX-w;//-hW;//-w-w;
				}else{
					hHe = hH+h+nH ;
					hWe = hW;
					hYe = hY-nH;
					hXe = hX;
					if(hallwayDir==Dir.down)
						hYe=hY-h;
				}
				ehTiles=new RTile[hWe][hHe];
				for(int i=0;i<hWe;i++)
					for(int j=0;j<hHe;j++){
						if(hXe+i<width && hYe+j<height && hYe>=0 && hXe>=0)
							if(perlinNoise[hXe+i][hYe+j]<0.35f && perlinNoise[hXe+i][hYe+j]>0.3f)
								ehTiles[i][j]=RTile.Dirt;
							else
								ehTiles[i][j]=getRandomTile(tileChances);
					}
				
				
				//Get RTile[][]
				for(int i=0;i<nW;i++)
					for(int j=0;j<nH;j++){
						if(nX+i>=0 && nY+j>=0 && nX+i<this.width && nY+j<this.height && perlinNoise[nX+i][nY+j]>0.3f){
							if(perlinNoise[nX+i][nY+j]<0.35f)
								rTiles[i][j]=RTile.Dirt;
							else
								rTiles[i][j]=getRandomTile(tileChances);
						}
					}
				break;
			case Abandoned:
				//Collapsed + overgrown
				
				//get random w and h
				nW=rng.nextInt(20)+5;
				nH=rng.nextInt(20)+5;
				rTiles=new RTile[nW][nH];
				//calculate where new X and Y are depending on doorDir
				switch(doorDir){
					case up:
						nY=hsY-nH;
						nX=hsX-rng.nextInt(nW-hWidth);
						break;
					case down:
						nY=hsY+hH;
						nX=hsX-rng.nextInt(nW-hWidth);
						break;
					case left:
						nX=hsX-nW;
						nY=hsY-rng.nextInt(nH-hWidth);
						break;
					case right:
						nX=hsX+hW;
						nY=hsY-rng.nextInt(nH-hWidth);
						break;
				}
				
				if(hallwayDir==Dir.left || hallwayDir==Dir.right){//type==RoomType.Natural || type==RoomType.Collapsed){
					hWe = hW+w+nW;//w;//hWe+w/2;
					hHe = hH;
					hXe = hX-nW;
					hYe = hY;
					if(hallwayDir==Dir.right)
						hXe = hX-w;//-hW;//-w-w;
				}else{
					hHe = hH+h+nH ;
					hWe = hW;
					hYe = hY-nH;
					hXe = hX;
					if(hallwayDir==Dir.down)
						hYe=hY-h;
				}
				ehTiles=new RTile[hWe][hHe];
				for(int i=0;i<hWe;i++)
					for(int j=0;j<hHe;j++){
						if(hXe+i<width && hYe+j<height && hYe>=0 && hXe>=0)
							if(perlinNoise[hXe+i][hYe+j]>0.7f)
								ehTiles[i][j]=RTile.Dirt;
							else
								ehTiles[i][j]=getRandomTile(tileChances);
					}
				
				
				//Get RTile[][]
				for(int i=0;i<nW;i++)
					for(int j=0;j<nH;j++){
						if(nX+i>=0 && nY+j>=0 && nX+i<this.width && nY+j<this.height && perlinNoise[nX+i][nY+j]>0.3f){
							if(perlinNoise[nX+i][nY+j]>0.7f)
								rTiles[i][j]=RTile.Dirt;
							else
								rTiles[i][j]=getRandomTile(tileChances);
						}
					}
				break;
				
			case Boss:
				break;
			}
			if(canFit(nX-1,nY-1,nW+2,nH+2,rTiles))break;
		}
		//Place Hallway
		if(ehTiles!=null)
			if(hXe+ehTiles.length<roomTiles.length && hXe>0 && hYe>0 && ehTiles.length>0&& hYe+ehTiles[0].length<roomTiles[0].length)
				for(int i=0;i<ehTiles.length;i++)
					for(int j=0;j<ehTiles[i].length;j++)
						roomTiles[hXe+i][hYe+j]=ehTiles[i][j];
		
		for(int i=0;i<hTiles.length;i++)
			for(int j=0;j<hTiles[i].length;j++)
				roomTiles[hsX+i][hsY+j]=hTiles[i][j];
		
		//Decide if next room branches out to multiple rooms
		if(rng.nextFloat() < 0.03) {//Branching chance
			int numBranch=rng.nextInt(15);//Random number for branch
			generateRoom(nX,nY,nW,nH,roomType,rTiles,roomChances, numBranch);
			numRoomsToGen-=numBranch;
		}
		//get number of rooms generated
		int numGen=generateRoom(nX,nY,nW,nH,roomType,rTiles,roomChances, numRoomsToGen-1);
		
		return numGen;
	}
	
	boolean canFit(int xStart,int yStart,int w,int h,RTile[][] tiles){
		for(int x=xStart;x<w+xStart;x++){
			for(int y=yStart;y<h+yStart;y++){
				if(x>=0 && y>=0 && x<roomTiles.length && y<roomTiles[x].length){
					if(roomTiles[x][y]!=null ){//&& tiles[x-xStart][y-yStart]!=null ){
						return false;
					}
				}else
					return false;
			}
		}
		return true;
	}
	
	RoomType getRandomRoom(float[] chance){
		RoomType type=null;
		int start;
		while(type==null){
			start=rng.nextInt(chance.length);
			for(int i=0;i<chance.length;i++)
				if(chance[(start+i)%chance.length]<rng.nextFloat()){
					type = RoomType.values()[(start+i)%chance.length];
					break;
				}
		}
		return type;
	}
	
	RTile getRandomTile(float[] chance){
		//RTile type=null;
		while(true)//type==null)
			for(int i=0;i<chance.length;i++){
				if(rng.nextFloat()<chance[i]){
					return RTile.tiles()[i];
				}
			}
		//return type;
	}
	
	LevelType getLevelType(int floor){
		if (floor==1){
			if(rng.nextFloat()<LevelType.Overworld.chance)
				return LevelType.Overworld;
		}
		LevelType type=null;
		while(type==null){
			for(LevelType t : LevelType.values())
				if(rng.nextFloat()<t.chance) {type=t;break;}
		}
		
		System.out.println("Level Type: "+type);
		
		return type;
	}
	
	float[] getRoomChances(LevelType type){
		float[] chances = new float[RoomType.values().length];
		
		switch(type){
		case Normal:
			chances[RoomType.getIndexOf(RoomType.Normal)]	= 80   ;
			chances[RoomType.getIndexOf(RoomType.Double)]	= 10   ;
			chances[RoomType.getIndexOf(RoomType.Circle)]	=  6.3f;
			chances[RoomType.getIndexOf(RoomType.Flooded)]	=  2   ;
			chances[RoomType.getIndexOf(RoomType.Overgrown)]=  1   ;
			chances[RoomType.getIndexOf(RoomType.Natural)]	=  0.5f;
			chances[RoomType.getIndexOf(RoomType.Collapsed)]=  0.1f;
			chances[RoomType.getIndexOf(RoomType.Abandoned)]=  0.1f;
			break;
		case Abandoned:
			chances[RoomType.getIndexOf(RoomType.Normal)]	=  5   ;
			chances[RoomType.getIndexOf(RoomType.Double)]	=  5   ;
			chances[RoomType.getIndexOf(RoomType.Circle)]	=  6.5f;
			chances[RoomType.getIndexOf(RoomType.Flooded)]	=  2   ;
			chances[RoomType.getIndexOf(RoomType.Overgrown)]=  1   ;
			chances[RoomType.getIndexOf(RoomType.Natural)]	=  0.5f;
			chances[RoomType.getIndexOf(RoomType.Collapsed)]= 10   ;
			chances[RoomType.getIndexOf(RoomType.Abandoned)]= 70   ;
			break;
		case Flooded:
			chances[RoomType.getIndexOf(RoomType.Normal)]	= 5    ;
			chances[RoomType.getIndexOf(RoomType.Double)]	= 5    ;
			chances[RoomType.getIndexOf(RoomType.Circle)]	= 5    ;
			chances[RoomType.getIndexOf(RoomType.Flooded)]	= 85   ;
			break;
		case Found:
			chances[RoomType.getIndexOf(RoomType.Normal)]	=  5   ;
			chances[RoomType.getIndexOf(RoomType.Double)]	=  1   ;
			chances[RoomType.getIndexOf(RoomType.Circle)]	=  1   ;
			chances[RoomType.getIndexOf(RoomType.Flooded)]	=  3   ;
			chances[RoomType.getIndexOf(RoomType.Overgrown)]= 10   ;
			chances[RoomType.getIndexOf(RoomType.Natural)]	= 70   ;
			chances[RoomType.getIndexOf(RoomType.Collapsed)]=  5   ;
			chances[RoomType.getIndexOf(RoomType.Abandoned)]=  5   ;
			break;
		case Overgrown:
			chances[RoomType.getIndexOf(RoomType.Normal)]	=  3   ;
			chances[RoomType.getIndexOf(RoomType.Double)]	=  1   ;
			chances[RoomType.getIndexOf(RoomType.Circle)]	=  1   ;
			chances[RoomType.getIndexOf(RoomType.Flooded)]	=  5   ;
			chances[RoomType.getIndexOf(RoomType.Overgrown)]=  70  ;
			chances[RoomType.getIndexOf(RoomType.Natural)]	=  5   ;
			chances[RoomType.getIndexOf(RoomType.Collapsed)]=  15  ;
			chances[RoomType.getIndexOf(RoomType.Abandoned)]=  10  ;
			break;
		case Overworld:
			chances[RoomType.getIndexOf(RoomType.Natural)]	=  90  ;
			chances[RoomType.getIndexOf(RoomType.Overgrown)]=  10  ;
			break;
		default:
			break;
		}
		
		//Convert to percent
		for(int i=0;i<chances.length;i++)
			chances[i]/=100f;
		
		return chances;
	}
	
	float[] getTileChance(RoomType room){
		float[] chances=new float[RTile.tiles().length];
		switch(room){
		case Normal:
			chances[RTile.tileIndex(RTile.Stone)]=100;
			break;
		case Double:
			chances[RTile.tileIndex(RTile.Stone)]=100;
			break;
		case Circle:
			chances[RTile.tileIndex(RTile.Stone)]=100;
			break;
		case Flooded:
			chances[RTile.tileIndex(RTile.Stone)]=100;
			//Water is placed by perlin noise
			//chances[RTile.tileIndex(RTile.Water)]=100;
			break;
		case Overgrown:
			chances[RTile.tileIndex(RTile.Stone)]=60;
			break;
		case Natural:
			chances[RTile.tileIndex(RTile.Stone)]=100;
			break;
		case Collapsed:
			chances[RTile.tileIndex(RTile.Stone)]=90;
			chances[RTile.tileIndex(RTile.Dirt)] =20;
			break;
		case Abandoned:
			chances[RTile.tileIndex(RTile.Stone)]=70;
			chances[RTile.tileIndex(RTile.Dirt)]=30;
			break;
		case Boss:
			chances[RTile.tileIndex(RTile.Stone)]=100;
			break;
		}
		
		//Convert to percent
		for(int i=0;i<chances.length;i++)
			chances[i]/=100f;
		
		return chances;
	}
}
/*
Level types:
	Normal		(rectangle rooms, chance for circle rooms, very small chance for natural/callapsed/abandoned/flooded rooms)
	Flooded		(normal, circle, and flooded rooms)
	Overgrown	(normal + small abandoned rooms chance)
	Overworld	(dirt/grassy, only possible on the first and second levels (only possible for flr 1 was also overworld) )
	Found 		(mostly natural rooms)
	Abandoned	(abandoned rooms,

Room types:
	Normal
	Double		(2 normal rooms that overlap must be same RoomType)
	Circle
	Flooded
	Overgrown (use perlin noise to make some tiles dirt and grass (esp the walls))
	Natural   (use perlin noise, may be stone, dirt, or a small water area) "naturally generated caves"
	Collapsed (normal + perlin noise)
	Abandoned (overgrown + slightly collapsed, maybe some water)

	Boss	  (large room that contains the next floor stairs. Maybe have a Boss Rooms Type with 1+ rooms for each boss)

Hallway types:
	Normal
	Curving
	Short


::PLAN::::::::::

Generate Floor Type

Gather possible room types

Generate room type

Generate room

Generate door and hallway.



*/







/*

LEVEL GENERATION PSEUDO CODE:

--------
Start
--------


Hallway made after placing room, not alongside? Maybe... 


Make starting room. 

Determine floor type.

loop{ (until floor requirements are filled)
generate new hallway

place door to next room

generate next room
}







Things I want to be able to customize:

Level theme / color scheme
Room types able to be placed
Requirements in a floor
Enemies able to spawn


 */ 
