package level;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import game.Game;
import graphics.Screen;

public class LevelLight {

	private int width, height;
	public int[] tiles,background;
	private final Random random = new Random();
	
	public LevelLight(int w, int h){
		width = w;
		height = h;
		tiles = new int[w*h];
		background = new int[w*h];
		//generateLevel(path);
		//generateRandomLevel();
	}
	
	public void generateLevel(String path){
		//String path = //lvlpath.substring(0,lvlpath.indexOf(".txt")-1) +".png";
		path+=".png";
		 
		BufferedImage fileImg = null;
				//new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		try{   fileImg = ImageIO.read(this.getClass().getResource(path));   }catch (IOException e) {e.printStackTrace();} //new File(path)
		
		BufferedImage bufImg = fileImg;
		BufferedImage img = new BufferedImage(bufImg.getWidth(), bufImg.getHeight(), BufferedImage.TYPE_INT_RGB);
		img.getGraphics().drawImage(bufImg, 0, 0, null);
		
		int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
				
		int whiteSpace = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if(pixels.length>x+y*width-whiteSpace){
					if(x >= img.getWidth()){
						whiteSpace += width - x;
						break;
					}
					if(pixels[x+y*width - whiteSpace]!=0){//0x00ffffff){
						tiles[x+y*width] = pixels[x+y*width - whiteSpace];
					}
				}else break;
			}
		}
	}
	
	public void generateLevel(Color[] cols){
		for(int i=0; i<tiles.length;i++){
			tiles[i]=cols[i].getRGB();
		}
	}
	
	public void generateLevel(Color[] cols,Color[] bg){
		for(int i=0; i<tiles.length;i++){
			tiles[i]=cols[i].getRGB();
			background[i]=bg[i].getRGB();
		}
	}
	
	public void generateLevel(int[] cols){
		for(int i=0; i<tiles.length;i++){
			tiles[i]=cols[i];
		}
	}
	
	public void generateLevel(int[] cols,int[] bgs){
		for(int i=0; i<tiles.length;i++){
			tiles[i]=cols[i];
			background[i]=bgs[i];
		}
	}
	
	public void generateRandomLevel(){
		for(int i=0; i<tiles.length;i++){
			Color col = new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256));
			tiles[i] = col.getRGB();
		}
	}
	
	public void setTilesLight(Tile[] tiles){
		for(int i=0;i<tiles.length;i++){
			if(tiles[i]!=null && this.tiles[i]!=0){
				tiles[i].setTint(this.tiles[i]);
			}
		}
	}
	
	public void update(){
	}
	
	public void render(Screen screen){
	}
	
	public static int[][] getPossibleBlemishes(int amt, long seed){
		Random r = new Random(seed);
		int[][] colorBlemishes = new int[amt][];
		for(int i=0;i<amt;i++){
			colorBlemishes[i]=new int[Game.TILE_SIZE*Game.TILE_SIZE];
			int pixel = 0;
			while(pixel < Game.TILE_SIZE*Game.TILE_SIZE){
				pixel += r.nextInt(Game.TILE_SIZE/2);
				if(pixel<colorBlemishes[i].length){ // && Math.random()<blemishRate
					int c=r.nextInt(100)+50;
					colorBlemishes[i][pixel] = new Color(c,c,c).getRGB();
				}
			}
		}
		return colorBlemishes;
	}
	
}
