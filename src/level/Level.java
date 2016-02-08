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
import entity.mob.Mob;
import game.Game;
import graphics.Screen;
import net.PlayerMP;
import util.Node;
import util.Vector2i;

public class Level {

	public int width, height;
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
	public Color[] colors;
	
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
	
	public Level(int width, int height, String world, String solids){
		this.width = width;
		this.height = height;
		this.world=world;
		this.solids=solids;
		tiles = new Tile[width * height];
		levelLight = new LevelLight(width,height);
	}
	
	public void setColor(int[] colors){
		int[][] colorBlemishes = LevelLight.getPossibleBlemishes(100);
		
		int whiteSpace = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if(world.length()>x+y*width-whiteSpace && solids.length()>x+y*width-whiteSpace){
					if(world.charAt(x+y*width - whiteSpace)=='\n'){//if is next line
						whiteSpace += width - x - 1;
						break;
					}else //if(!Character.isWhitespace(world.charAt(x+y*width - whiteSpace)))//if not space
						tiles[x+y*width] = new Tile(world.charAt(x+y*width - whiteSpace),solids.charAt(x+y*width-whiteSpace)=='1',colors[x+y*width],colorBlemishes[random.nextInt(colorBlemishes.length)]);
				}else break;
			}
		}
		
		levelLight.generateLevel(colors);
		levelLight.setTilesLight(tiles);
	}
	
	public void generateLevel(String path) {
		String worldPath=path+".txt";
		String solidPath=path+"-solid.txt";
		
		int[][] colorBlemishes = LevelLight.getPossibleBlemishes(100);
		
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
					}else if(!Character.isWhitespace(world.charAt(x+y*width - whiteSpace)))//if not space 						  new Color(150,150,150).getRGB()
						tiles[x+y*width] = new Tile(world.charAt(x+y*width - whiteSpace),solids.charAt(x+y*width-whiteSpace)=='1',new Color(150,150,150).getRGB(),colorBlemishes[random.nextInt(colorBlemishes.length)]);
				}else break;
			}
		}
		levelLight.generateLevel(path);
		levelLight.setTilesLight(tiles);
	}

	int time=0;
	boolean doMobUpdate;
	public void update() {
		time++;
		//Update mobs and tiles
		for(int i = 0;i < tiles.length; i++){
			if(tiles[i]!=null)
				tiles[i].update();
		}
		
		if(time%15==0 && getPlayers().size()>0){
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
		}
		for(int i = getPlayers().size()-1;i >= 0; i--){
			if(getPlayers().get(i)!=null && !getPlayers().get(i).isRemoved()){
				getPlayers().get(i).update();
			}else
				getPlayers().remove(i);
		}
		for(int i = mobs.size()-1;i >= 0; i--){
			if(mobs.get(i)!=null && !mobs.get(i).isRemoved()){
				doMobUpdate=true;
				for(PlayerMP p : getPlayers()){					
					if(getDistance(mobs.get(i).vector,p.vector)>mobs.get(i).updateRange){
						doMobUpdate=false;
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
	public void render(int xScroll, int yScroll, Screen screen) {
		
		screen.setOffset(xScroll, yScroll);
		//Corner Pins (x0=Top Left Tile)
		int x0 = xScroll / TILE_SIZE -1;
		int x1 = (xScroll + screen.width) / TILE_SIZE +1; //+1 to make it render everything on screen if tile is slightly off screen
		int y0 = yScroll / TILE_SIZE -1;
		int y1 = (yScroll + screen.height) / TILE_SIZE +1;

		//Vector2i pv = new Vector2i(player.x/Game.TILE_SIZE,player.y/Game.TILE_SIZE);
		if(shadowMap!=null)
		synchronized(shadowMap){
			for (int y = y0; y < y1; y++) {
				for (int x = x0; x < x1; x++) {
					Tile tile = getTile(x, y);
					if (tile != null){
						for(int i=0;i<getPlayers().size();i++){
							if(tile.hasBeenSeen || (i<shadowMap.length && shadowMap[i]!=null && shadowMap[i][x][y]==1))
								tile.render(x * TILE_SIZE, y * TILE_SIZE, screen);
							if(i<shadowMap.length && shadowMap[i]!=null && shadowMap[i][x][y]==1){
								//tile.renderLight(x * TILE_SIZE, y * TILE_SIZE, screen);
								tile.hasBeenSeen=true;
							}
						}
					}
				}
			}
			for (int y = y0; y < y1; y++) {
				for (int x = x0; x < x1; x++) {
					Tile tile = getTile(x, y);
					if (tile != null){
						boolean renderGray = true;
						for(int i=0;i<getPlayers().size();i++){
							if(i<shadowMap.length && shadowMap[i]!=null && shadowMap[i][x][y]==1){
								tile.renderLight(x * TILE_SIZE, y * TILE_SIZE, screen);
								tile.hasBeenSeen=true;
								renderGray=false;
							}
						}
						if(tile.hasBeenSeen && renderGray){
							tile.doRender(true);
							tile.renderLight(x * TILE_SIZE, y * TILE_SIZE, screen, new Color(0,0,0).getRGB());
						}
					}
				}
			}
			for(int i = getPlayers().size()-1;i >= 0; i--){
				if(getPlayers().get(i)!=null && !getPlayers().get(i).isRemoved()){
					for(int s=0;s<getPlayers().size();s++){
						if(i<shadowMap.length && i>0 && getPlayers().get(i)!=null && shadowMap[i]!=null && shadowMap[s][getPlayers().get(i).x/Game.TILE_SIZE][getPlayers().get(i).y/Game.TILE_SIZE]==1)
							getPlayers().get(i).render(screen);
					}
				}else
					getPlayers().remove(i);
			}
			for(int i = mobs.size()-1;i >= 0; i--){
				if(mobs.get(i)!=null && !mobs.get(i).isRemoved()){
					for(int s=0;s<getPlayers().size();s++){
						if(s<shadowMap.length && shadowMap[s]!=null && i<mobs.size() && shadowMap[s][mobs.get(i).x/Game.TILE_SIZE][mobs.get(i).y/Game.TILE_SIZE]==1){
							mobs.get(i).render(screen);
							mobs.get(i).hasBeenSeen=true;
						}
						else if(i<mobs.size() && players.get(s).lockedOn==mobs.get(i)){
								players.get(s).lockedOn.lockedOnto=false;
								players.get(s).lockedOn=null;
						}
					}
				}else
					mobs.remove(i);
			}
		}
		for(int i = entities.size()-1;i >= 0; i--){
			if(entities.get(i)!=null && !entities.get(i).isRemoved()){
				for(int s=0;s<getPlayers().size();s++){
					if(s<shadowMap.length && shadowMap[s]!=null && shadowMap[s][entities.get(i).x/Game.TILE_SIZE][entities.get(i).y/Game.TILE_SIZE]==1)
						entities.get(i).render(screen);
				}
			}else
				entities.remove(i);
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
