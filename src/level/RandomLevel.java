package level;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomLevel extends Level {

	private static final Random rng = new Random();
	private int[][] colorBlemishes = new int[5][];
	
	private RTile[][] roomTiles;
	
	public RandomLevel(int width, int height) {
		super(width,height);
		generateLevel();
	}
	
	public void generateLevel() {
		int[][] colorBlemishes = LevelLight.getPossibleBlemishes(100);
		
		//Generation happens here
		roomTiles=new RTile[width][height];
		generate(10,10,width/2-5,height/2-5,RTile.Dirt);
		
		//Find place for exit
		//
		//EMPTY CODE
		//
		
		String world="";
		String solids="";//0 or 1
		Color[] colors = null;
		
		colors=new Color[width*height];
		
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
					}else if(!Character.isWhitespace(world.charAt(x+y*width - whiteSpace)))//if not space
						tiles[x+y*width] = new Tile(world.charAt(x+y*width - whiteSpace),solids.charAt(x+y*width-whiteSpace)=='1',colors[x+y*width].getRGB(),colorBlemishes[rng.nextInt(colorBlemishes.length)]);
				}else break;
			}
		}
		levelLight.generateLevel(colors);
		levelLight.setTilesLight(tiles);
	}
	
	//Includes hallways
	int roomCount=0, maxRooms=50;
	boolean hallway=false;
	private void generate(int w,int h,int xStart,int yStart, RTile type){
		roomCount++;
		if(roomCount>maxRooms)
			return;
		
		for(int x=xStart;x<w+xStart;x++){
			for(int y=yStart;y<h+yStart;y++){
				if(x>=0 && y>=0 && x<roomTiles.length && y<roomTiles[x].length)
					roomTiles[x][y]=type;
			}
		}
		
		hallway=!hallway;
		int nW, nH;
		int nX, nY;
		int xOff=0,yOff=0;
		int attempts=0;
		boolean skip=false;
		while(true){
			if(Math.random()<0.5){
				//Top/Bottom expansion
				if(hallway){
					nW=2;
					nH=rng.nextInt(5)+10;
				}else{
					nW=rng.nextInt(25)+5;
					nH=rng.nextInt(20)+5;
					xOff=rng.nextInt(nW-1);
					yOff=0;
				}
				nX=rng.nextInt(w-1);
				nY=0;
				if(Math.random()<0.5){//Top
					nY=-nH;
				}else{//Bottom
					nY=h;
				}
			}else{
				//L/R expansion
				if(hallway){
					nH=2;
					nW=rng.nextInt(5)+10;
				}else{
					nH=rng.nextInt(25)+5;
					nW=rng.nextInt(20)+5;
					xOff=0;
					yOff=rng.nextInt(nH-1);
				}
				nX=0;
				nY=rng.nextInt(h-1);
				if(Math.random()<0.5){//L
					nX=-nW;
				}else{//R
					nX=w;
				}
			}
			if(canFit(nW,nH,nX+xStart,nY+yStart)){
				break;
			}else{
				attempts++;
				if(attempts>9999){
					skip=true;
					break;
				}
			}
		}
		if(!skip)
			generate(nW,nH,nX+xStart,nY+yStart,RTile.Stone);//RTile.tiles()[rng.nextInt(RTile.tiles().length)]);
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
	
	public static enum RTile{
			Wall_Stone(new char[]{'H','M','#'},new Color[]{Color.darkGray},true),
			Wall_Grass(new char[]{'W','w','v','V'},new Color[]{new Color(0x8ae75e)},true),
			
		Stone(new char[]{',','.','`',' ','\'',':',' ',' '},new Color[]{Color.gray},Wall_Stone),
		Dirt(new char[]{'\\',';',':','=','!','#',']','['},new Color[]{new Color(0x8f6b38)},Wall_Grass),
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
		Color[] colors;
		RTile border;
		boolean isWall;
		RTile(char[] characters, Color[] colors, RTile border){
			this.characters=characters;
			this.colors=colors;
			this.border=border;
		}
		RTile(char[] characters, Color[] colors, boolean isWall){
			this.characters=characters;
			this.colors=colors;
			isWall=true;
			border=null;
		}
	}
	
}
