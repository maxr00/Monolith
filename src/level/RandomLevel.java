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
		
		//																								   Scale,Smoothness
		perlinNoise=this.GeneratePerlinNoise(this.GenerateSmoothNoise(this.GenerateWhiteNoise(width, height), 0),4);
		
		generateLevel();
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
			//System.out.println(tile);
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
	
}
