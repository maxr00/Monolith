package level;

import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import entity.Entity;
import entity.Particle_Exp;
import entity.mob.Mob;
import game.Game;
import graphics.Screen;
import level.RandomLevel.RTile;
import net.PlayerMP;
import player.Player;
import util.Node;
import util.Vector2i;

public class Level {

	public int width, height;
	public long seed;
	public Tile[] tiles;
	//private LevelLight levelLight;
	private final int TILE_SIZE = Game.TILE_SIZE; //Space between tile, (size of tiles will have no space between tiles, less will overlap tiles)
	private final static Random random = new Random();
	public LevelLight levelLight;
	//public Player player;
	public ArrayList<PlayerMP> players = new ArrayList<PlayerMP>();
	public ArrayList<Mob> mobs = new ArrayList<Mob>();
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public String path;
	
	public String world, solids;
	public Color[] colors, background;
	
	private float[][] lightMap;
	private int[][][] shadowMap = null;
	
	public Level(int width, int height, String path) {// Randomly Generated
		this.width = width;
		this.height = height;
		this.path = path;
		tiles = new Tile[width * height];
		levelLight = new LevelLight(width,height);
		generateLevel(path);
	}
	
	public Level(int width,int height){
		this.width = width;
		this.height = height;
		tiles = new Tile[width * height];
		levelLight = new LevelLight(width,height);
	}
	
	public Level(int width, int height, int[] rtiles){
		this.width = width;
		this.height = height;
		tiles = new Tile[width * height];
		levelLight = new LevelLight(width,height);
		
		world="";
		solids="";//0 or 1
		colors = null;
		
		colors=new Color[width*height];
		background = new Color[width*height];
		
		int[][] colorBlemishes = LevelLight.getPossibleBlemishes(100,seed);
		//int[] colors=new int[width*height],bgs=new int[width*height];
		
		RTile[][] roomTiles=new RTile[width][height];
		for(int x=0;x<width;x++)
			for(int y=0;y<height;y++){
				if(rtiles[x+y*width]!=-1)
					roomTiles[x][y]=RTile.values()[rtiles[x+y*width]];
			}
		
		boolean border;
		for(int y=0;y<height;y++){
			for(int x=0;x<width;x++){
				if(roomTiles[x][y]!=null){
					world+=roomTiles[x][y].characters[random.nextInt(roomTiles[x][y].characters.length)];
					if(x==0 || y==0 || x==width-1 || y==height-1)
						solids+="1";
					else
						solids+="0";
					
					colors[x+y*width]=roomTiles[x][y].colors[random.nextInt(roomTiles[x][y].colors.length)];
					if(roomTiles[x][y].bg!=null)
						background[x+y*width]=roomTiles[x][y].bg[random.nextInt(roomTiles[x][y].colors.length)];
				}else{
					border=false;
					for(int xx=-1;xx<2;xx++){
						if(border)break;
						for(int yy=-1;yy<2;yy++){
							if(border)break;
							if(x+xx<width && x+xx>=0 && y+yy<height && y+yy>=0 && (xx!=0 || yy!=0)){
								if(roomTiles[x+xx][y+yy]!=null){
									world+=roomTiles[x+xx][y+yy].border.characters[random.nextInt(roomTiles[x+xx][y+yy].border.characters.length)];
									solids+="1";
									colors[x+y*width]=roomTiles[x+xx][y+yy].border.colors[random.nextInt(roomTiles[x+xx][y+yy].border.colors.length)];
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
						tiles[x+y*width] = new Tile(RTile.indexOf(roomTiles[x][y]),x,y,world.charAt(x+y*width - whiteSpace),solids.charAt(x+y*width-whiteSpace)=='1',colors[x+y*width].getRGB(),background[x+y*width]!=null ? background[x+y*width].getRGB() : -1,colorBlemishes[random.nextInt(colorBlemishes.length)]);
				}else break;
			}
		}
		
		levelLight.generateLevel(colors);
		levelLight.setTilesLight(tiles);
		
		/*
		for(int i=0;i<width*height;i++){
			if(rtiles[i]!=-1){
				RTile t = RTile.values()[rtiles[i]];
				char c=t.characters[random.nextInt(t.characters.length)];
				int col = t.colors[random.nextInt(t.colors.length)].getRGB();
				int bg = t.bg[random.nextInt(t.bg.length)].getRGB();
				world+=c;
				solids+=t.isWall ? "1" : "0";
				colors[i]=col;
				bgs[i]=bg;
				tiles[i]=new Tile(rtiles[i], c, t.isWall, col, bg, colorBlemishes[random.nextInt(colorBlemishes.length)]);
			}
		}
		
		levelLight.generateLevel(colors,bgs);
		levelLight.setTilesLight(tiles);
		shadowMap = new int[getPlayers().size()][][];
		*/
	}
	
	public void generateLevel(String path) {
		String worldPath=path+".txt";
		String solidPath=path+"-solid.txt";
		
		int[][] colorBlemishes = LevelLight.getPossibleBlemishes(100,seed);
		
		List<String> lines = null, sLines = null;
		try{
			lines = Files.readAllLines(Paths.get(worldPath));
			sLines = Files.readAllLines(Paths.get(solidPath));
		}catch(Exception e){e.printStackTrace();}
		
		String world = "";
		for(String l : lines){
			world += l +"\n";
		}
		String solids = "";
		for(String s : sLines){
			solids += s +"\n";
		}
		
		int whiteSpace = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if(world.length()>x+y*width-whiteSpace && solids.length()>x+y*width-whiteSpace){
					if(world.charAt(x+y*width - whiteSpace)=='\n'){//if is next line
						whiteSpace += width - x - 1;
						break;
					}else{ //if(!Character.isWhitespace(world.charAt(x+y*width - whiteSpace)))//if not space 						  new Color(150,150,150).getRGB()
						tiles[x+y*width] = new Tile(-1,x,y,world.charAt(x+y*width - whiteSpace),solids.charAt(x+y*width-whiteSpace)=='1',new Color(150,150,150).getRGB(),-1,colorBlemishes[random.nextInt(colorBlemishes.length)]);
					}
				}else break;
			}
		}
		levelLight.generateLevel(path);
		levelLight.setTilesLight(tiles);
		shadowMap = new int[getPlayers().size()][][];
	}

	int time=0;
	boolean doMobUpdate;
	boolean paused=true;
	public void update() {
		if(getPlayers().size()==0) paused=false;
		else paused=true;
		for(Player p : getPlayers())
			if(!p.paused){
				paused=false;
				break;
			}
		
		if(paused) {
			for(int i = getPlayers().size()-1;i >= 0; i--){
				if(getPlayers().get(i)!=null && !getPlayers().get(i).isRemoved()){
					getPlayers().get(i).update();
				}else
					getPlayers().remove(i);
			}
			return;
		}
		
		time++;
		//Update mobs and tiles
		for(int i = 0;i < tiles.length; i++){
			if(tiles[i]!=null)
				tiles[i].update();
		}
		if(time%15==0){
			if(getPlayers().size()>0){
				for(int i=0;i<getPlayers().size();i++){
					float[][] lm = calculateFOV(getPlayers().get(i).x/Game.TILE_SIZE,getPlayers().get(i).y/Game.TILE_SIZE,1000);
					
					synchronized(shadowMap){
						if(shadowMap[i]==null){
							shadowMap[i]=new int[width][height];
						}
						for(int y=0;y<height;y++){
							for(int x=0;x<width;x++){
								if(shadowMap!=null && shadowMap[i]!=null)
									shadowMap[i][x][y]=(int)lm[x][y];
							}
						}
					}
					
				}
			}else{
				if(shadowMap==null || shadowMap.length<=0)
					shadowMap=new int[1][][];
				if(shadowMap[0]==null){
					shadowMap[0]=new int[width][height];
				}
				for(int y=0;y<height;y++){
					for(int x=0;x<width;x++){
						shadowMap[0][x][y]=1;
					}
				}
			}
		}
		for(int i = getPlayers().size()-1;i >= 0; i--){
			if(getPlayers().get(i)!=null && !getPlayers().get(i).isRemoved()){
				getPlayers().get(i).update();
			}else
				getPlayers().remove(i);
		}
		for(int i = mobs.size()-1;i >= 0; i--){
			if(mobs.get(i)!=null && !mobs.get(i).isRemoved()){
				doMobUpdate=false;
				for(PlayerMP p : getPlayers()){
					if(getDistance(mobs.get(i).vector,p.vector)<mobs.get(i).updateRange){
						doMobUpdate=true;
						break;
					}
				}
				if(doMobUpdate)
					mobs.get(i).update();
			}else
				mobs.remove(i);
		}
		for(int i = entities.size()-1;i >= 0; i--){
			if(entities.get(i)!=null && !entities.get(i).isRemoved()){
				entities.get(i).update();
			}else
				entities.remove(i);
		}
		//mobUpdate();
	}
	
	public void addMob(Mob mob){mobs.add(mob);}
	
	public void addPlayer(PlayerMP p){
		getPlayers().add(p);
		shadowMap = new int[getPlayers().size()][][];
	}
	
	//xScroll, yScroll is top left corner
	public void renderUpdate(int xScroll, int yScroll, Screen screen) {
		
		int x0 = xScroll / TILE_SIZE -1;
		int x1 = (xScroll + screen.width) / TILE_SIZE +1;
		int y0 = yScroll / TILE_SIZE -1;
		int y1 = (yScroll + screen.height) / TILE_SIZE +1;

		if(shadowMap!=null)
		synchronized(shadowMap){
			
			for (int y = y0; y < y1; y++) {
				for (int x = x0; x < x1; x++) {
					Tile tile = getTile(x, y);
					if (tile != null){
						if(getPlayers().size()>0){
							boolean seen=false;
							for(int i=0;i<getPlayers().size();i++){
								if(i<shadowMap.length && shadowMap[i]!=null && shadowMap[i][x][y]==1){
									seen=true;
								}
							}
							if(seen){
								tile.doRender(true);
								tile.doRenderLight(true);
								tile.hasBeenSeen=true;
								tile.renderGray=false;
							}else if(tile.hasBeenSeen){
								tile.renderGray=true;
								tile.doRender(true);
								tile.doRenderLight(true);
							}else{
								tile.renderGray=false;
								tile.doRender(false);
								tile.doRenderLight(false);
							}
						}else{
							tile.doRender(true);
						}
					}
				}
			}
			
			for(int i = getPlayers().size()-1;i >= 0; i--){
				getPlayers().get(i).doRender(true);
			}
			
			for(int i = mobs.size()-1;i >= 0; i--){
				if(mobs.get(i)!=null && !mobs.get(i).isRemoved()){
					boolean wasSeen=false;
					if(getPlayers().size()>0){
						for(int s=0;s<getPlayers().size();s++){
							if(s<shadowMap.length && shadowMap[s]!=null && i<mobs.size() && shadowMap[s][mobs.get(i).x/Game.TILE_SIZE][mobs.get(i).y/Game.TILE_SIZE]==1){
								wasSeen=true;
								mobs.get(i).hasBeenSeen=true;
							}else{
								if(i<mobs.size() && getPlayers().get(s).lockedOn==mobs.get(i)){
									getPlayers().get(s).lockedOn.lockedOnto=false;
									getPlayers().get(s).lockedOn=null;
								}
							}
						}
						mobs.get(i).isSeen=wasSeen;
						mobs.get(i).doRender(wasSeen);
					}else{
						mobs.get(i).doRender(true);
					}
				}else
					mobs.remove(i);
			}
			
			for(int i = entities.size()-1;i >= 0; i--){
				if(entities.get(i)!=null && !entities.get(i).isRemoved()){
					if(getPlayers().size()>0){
						boolean seen=false;
						for(int s=0;s<getPlayers().size();s++){
							if((s<shadowMap.length && shadowMap[s]!=null && shadowMap[s][entities.get(i).x/Game.TILE_SIZE][entities.get(i).y/Game.TILE_SIZE]==1) || entities.get(i) instanceof Particle_Exp)
								seen=true;
						}
						entities.get(i).doRender(seen);
					}else{
						entities.get(i).doRender(true);
					}
				}else
					entities.remove(i);
			}
			
		}
	}
	
	public void render(int xScroll, int yScroll, Screen screen) {
		int x0 = xScroll / TILE_SIZE -1;
		int x1 = (xScroll + screen.width) / TILE_SIZE +1;
		int y0 = yScroll / TILE_SIZE -1;
		int y1 = (yScroll + screen.height) / TILE_SIZE +1;
		System.out.println(x0 +"-"+x1 +"," +y0 +"-" +y1);
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				Tile tile = getTile(x, y);
				if (tile != null){
					tile.render(x * TILE_SIZE, y * TILE_SIZE, screen);
					tile.renderLight(x * TILE_SIZE, y * TILE_SIZE, screen);
				}
			}
		}
		for(int i = entities.size()-1;i >= 0; i--){
			entities.get(i).render(screen);
		}
		screen.displayAdditive();
		screen.displayParticles();
		screen.resetAdditive();
		screen.resetParticles();
		for(int i = mobs.size()-1;i >= 0; i--){
			mobs.get(i).render(screen);
		}
		for(int i = getPlayers().size()-1;i >= 0; i--){
			getPlayers().get(i).render(screen);
		}
	}
	
	public Tile getTile(int x, int y) {
		if (y < 0 || y >= height) return null;
		if (x < 0 || x >= width) return null;
		return tiles[x + y * width];
	}

	public boolean canMoveOn(int x, int y) {
		for(int i = 0;i < getPlayers().size(); i++){
			if(getPlayers().get(i)!=null && !getPlayers().get(i).isRemoved() && (getPlayers().get(i).isOnTile(x,y))){
				return false;
			}
		}
		for(int i = 0;i < mobs.size(); i++){
			if(mobs.get(i)!=null && !mobs.get(i).isRemoved() && (mobs.get(i).isOnTile(x,y))){
				return false;
			}
		}
		Tile t=getTile(x, y);
		if(t==null) return true;
		return !t.isSolid();
	}
	
	public Entity getEntityOn(int x,int y){
		//if(player!=null && player.isOnTile(x, y))return player;
		for(int i = 0;i < entities.size(); i++){
			if(entities.get(i)!=null && !entities.get(i).isRemoved() && (entities.get(i).isOnTile(x,y))){
				return entities.get(i);
			}
		}
		return null;
	}
	
	public Mob getMobOn(int x,int y){
		//if(player!=null && player.isOnTile(x, y))return player;
		for(int i = 0;i < getPlayers().size(); i++){
			if(getPlayers().get(i)!=null && !getPlayers().get(i).isRemoved() && (getPlayers().get(i).isOnTile(x,y))){
				return getPlayers().get(i);
			}
		}
		for(int i = 0;i < mobs.size(); i++){
			if(mobs.get(i)!=null && !mobs.get(i).isRemoved() && (mobs.get(i).isOnTile(x,y))){
				return mobs.get(i);
			}
		}
		return null;
	}
	
	public Mob getMob(String id){
		for(int i = 0;i < mobs.size(); i++){
			if(mobs.get(i)!=null && !mobs.get(i).isRemoved()){
				if(mobs.get(i).identifier.equals(id)){
					return mobs.get(i);
				}
			}
		}
		for(int i = 0;i < getPlayers().size(); i++){
			if(getPlayers().get(i)!=null && !getPlayers().get(i).isRemoved()){
				if(getPlayers().get(i).identifier.equals(id)){
					return getPlayers().get(i);
				}
			}
		}
		return null;
	}
	
	private static Comparator<Node> nodeSorter = new Comparator<Node>() {
		public int compare(Node n0, Node n1){
			if(n1.fCost<n0.fCost) return 1;
			if(n1.fCost>n0.fCost) return -1;
			return 0;
		}
	};
	
	int tries;
	public ArrayList<Node> getPath(Vector2i start, Vector2i goal, int maxTries){
		tries = 0;
		ArrayList<Node> openList = new ArrayList<Node>();
		ArrayList<Node> closedList = new ArrayList<Node>();
		Node current = new Node(start,null,0,getDistance(start,goal));
		openList.add(current);
		
		while(openList.size()>0){
			tries++;
			if(tries>maxTries) return null;
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			if(current.vector.equals(goal)){
				ArrayList<Node> path = new ArrayList<Node>();
				while(current.parent!=null){
					path.add(current);
					current = current.parent;
				}
				openList.clear();
				closedList.clear();
				
//System.out.println(tries);
				
				return path;
			}
			openList.remove(current);
			closedList.add(current);
			for(int i=0;i<9;i++){
				if(i==4)continue;
				int x=current.vector.getX();
				int y=current.vector.getY();
				int xi = (i%3)-1;
				int yi = (i/3)-1;
				Tile at = getTile(x+xi,y+yi);
				if(at==null)continue;
				if(at.isSolid())continue;
				Vector2i a = new Vector2i(x+xi,y+yi);
				double gCost = current.gCost + getDistance(current.vector,a);
				double hCost = getDistance(a,goal);
				Node node = new Node(a,current,gCost,hCost);
				if(vectorInList(closedList,a) && gCost >= node.gCost) continue;
				if(!vectorInList(closedList,a) || gCost < node.gCost) openList.add(node);
			}
		}
		
		
		return null;
	}
	
	public double getDistance(Vector2i start, Vector2i end){
		double dx = start.getX() - end.getX();
		double dy = start.getY() - end.getY();
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	private boolean vectorInList(List<Node> list, Vector2i vector){
		for(Node n : list){
			if(n.vector.equals(vector)) return true;
		}
		return false;
	}
	
	
	enum Direction{
		UL(-1,-1), U(0,-1), UR(1,-1),
		L (-1,0), 			R (1,0),
		DL(-1,1),  D(0,1),  DR(1,1);
		
		public int deltaX,deltaY;
		
		Direction(int x,int y){
			deltaX=x;
			deltaY=y;
		}
		
		static Direction[] DIAGONALS = {UL,U,UR,L,R,DL,D,DR};
	}
	
	int startx, starty; float radius; float[][] resistanceMap;
	public float[][] calculateFOV(int startx, int starty, float radius) {//float[][] resistanceMap,   RadiusStrategy rStrat) { 
	    this.startx = startx;
	    this.starty = starty;
	    this.radius = radius;
	    //this.rStrat = rStrat;
	    this.resistanceMap = new float[width][height];//resistanceMap;
	 
	    for(int y=0;y<height;y++){
	    	for(int x=0;x<width;x++){
	    		if(getTile(x,y)!=null){
	    			resistanceMap[x][y]=getTile(x,y).isSolid() ? 1 : 0;
	    		}
	    	}
	    }
	    
	    width = resistanceMap.length;
	    height = resistanceMap[0].length;
	    lightMap = new float[width][height];
	    
	    if(startx>0 && starty>0)
	    	lightMap[startx][starty] = 1;//force;//light the starting cell
	    for (Direction d : Direction.DIAGONALS) {
	        castLight(1, 1.0f, 0.0f, 0, d.deltaX, d.deltaY, 0);
	        castLight(1, 1.0f, 0.0f, d.deltaX, 0, 0, d.deltaY);
	    }
	 
	    return lightMap;
	}
	 
	private void castLight(int row, float start, float end, int xx, int xy, int yx, int yy) {
	    float newStart = 0.0f;
	    if (start < end) {
	        return;
	    }
	    boolean blocked = false;
	    for (int distance = row; distance <= radius && !blocked; distance++) {
	        int deltaY = -distance;
	        for (int deltaX = -distance; deltaX <= 0; deltaX++) {
	            int currentX = startx + deltaX * xx + deltaY * xy;
	            int currentY = starty + deltaX * yx + deltaY * yy;
	            float leftSlope = (deltaX - 0.5f) / (deltaY + 0.5f);
	            float rightSlope = (deltaX + 0.5f) / (deltaY - 0.5f);
	 
	            if (!(currentX >= 0 && currentY >= 0 && currentX < this.width && currentY < this.height) || start < rightSlope) {
	                continue;
	            } else if (end > leftSlope) {
	                break;
	            }
	 
	            //check if it's within the lightable area and light if needed
	            //if (rStrat.radius(deltaX, deltaY) <= radius) {
	            //    float bright = (float) (1 - (rStrat.radius(deltaX, deltaY) / radius));
	            lightMap[currentX][currentY] = 1;// = bright;
	            //}
	            
	            if (blocked) { //previous cell was a blocking one
	                if (resistanceMap[currentX][currentY] >= 1) {//hit a wall
	                    newStart = rightSlope;
	                    continue;
	                } else {
	                    blocked = false;
	                    start = newStart;
	                }
	            } else {
	                if (resistanceMap[currentX][currentY] >= 1 && distance < radius) {//hit a wall within sight line
	                    blocked = true;
	                    castLight(distance + 1, start, leftSlope, xx, xy, yx, yy);
	                    newStart = rightSlope;
	                }
	            }
	        }
	    }
	}

	public void removePlayerMP(String username) {
		int index=0;
		for(PlayerMP player : getPlayers()){
			if(player.getUsername().equals(username))
				break;
			index++;
		}
		if(getPlayers().size()>index)
			this.getPlayers().remove(index);
	}
	
	private int getPlayerMPIndex(String username){
		int index=0;
		for(PlayerMP player : getPlayers()){
			if(player.getUsername().equals(username))
				break;
			index++;
		}
		return index;
	}
	
	public void movePlayer(String username,int x,int y){
		int index = getPlayerMPIndex(username);
		this.getPlayers().get(index).moveTo(x, y);
	}
	
	public synchronized List<PlayerMP> getPlayers(){return players;}
	
}
