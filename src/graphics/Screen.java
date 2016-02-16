package graphics;

import java.awt.Color;
import java.util.Random;

import game.Game;

public class Screen {

	public int width;
	public int height;
	public int[] pixels, background; // Must be 1D array because Game.java's renderPixels must be 1D
	private float xOffset, yOffset;
	private float cameraSpeed=0.5f;
	Random random = new Random();
	
	// Takes raw width and height (not multiplied by pixel scale)
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		background = new int[width * height];
	}
	
	public void renderSprite(int xPos, int yPos, Sprite sprite){
		xPos -= xOffset; //Minus because otherwise movement would be reversed
		yPos -= yOffset;
		if(sprite==null) return;
		for (int y = 0; y < sprite.HEIGHT; y++) {
			int ya = yPos + y;
			if(ya < -sprite.HEIGHT || ya >= height) break;
			if(ya<0) ya=0;
			for (int x = 0; x < sprite.WIDTH; x++) {
				int xa = xPos + x;
				if(xa < -sprite.WIDTH || xa >= width) break;
				if(xa<0)xa=0;
				if(sprite.pixels[x+y*sprite.WIDTH]!=0x00ffffff){ //Don't render transparent pixels
					pixels [xa + ya * width] = sprite.pixels[x + y * sprite.WIDTH];
				}else
					pixels [xa + ya * width] = 0;
					
			}
		}
	}
	
	public void renderLight(int xPos, int yPos, int w, int h, int color, int[] colBlemishes){
		//if(rainbow) {color= Color.black.getRGB(); colBlemishes=null;}
		
		xPos -= xOffset; //Minus because otherwise movement would be reversed
		yPos -= yOffset;
		for (int y = 0; y < h; y++) {
			int ya = yPos + y;
			if(ya < -h || ya >= height) break; if(ya<0) ya=0; 
			for (int x = 0; x < w; x++) {
				int xa = xPos + x;
				if(xa < -w || xa >= width) break; if(xa<0)xa=0;
				if(pixels [xa + ya * width] != 0){
					if(colBlemishes != null && colBlemishes[x+y]!=0){
						pixels [xa + ya * width] = blend(color, colBlemishes[x+y]);
					}else
						pixels [xa + ya * width] = color;
				}
			}
		}
	}
	
	public void renderBackground(int xPos, int yPos, int w, int h, int color){
		xPos -= xOffset;
		yPos -= yOffset;
		for (int y = 0; y < h; y++) {
			int ya = yPos + y;
			if(ya < -h || ya >= height) break; if(ya<0) ya=0;
			for (int x = 0; x < w; x++) {
				int xa = xPos + x;
				if(xa < -w || xa >= width) break; if(xa<0)xa=0;
				background[xa + ya * width]=color;
			}
		}
	}
	
	int[] particles;
	public void newParticles(){
		particles=new int[pixels.length];
	}
	public void resetParticles(){
		for(int i=0;i<pixels.length;i++)
			particles[i]=0;
	}
	public int[] getParticles(){return particles;}
	public void renderParticle(int xPos, int yPos, Sprite sprite){
		xPos -= xOffset; //Minus because otherwise movement would be reversed
		yPos -= yOffset;
		if(sprite==null) return;
		for (int y = 0; y < sprite.HEIGHT; y++) {
			int ya = yPos + y;
			if(ya < -sprite.HEIGHT || ya >= height) break;
			if(ya<0) ya=0;
			for (int x = 0; x < sprite.WIDTH; x++) {
				int xa = xPos + x;
				if(xa < -sprite.WIDTH || xa >= width) break;
				if(xa<0)xa=0;
				if(sprite.pixels[x+y*sprite.WIDTH]!=0x00ffffff){ //Don't render transparent pixels
					particles [xa + ya * width] = sprite.pixels[x + y * sprite.WIDTH];
				}else
					particles [xa + ya * width] = 0;
					
			}
		}
	}
	public void displayParticles(){
		if(particles!=null){
			for(int i=0;i<pixels.length;i++)
				if(particles[i]!=0){
					pixels[i]=particles[i];
				}
			resetParticles();
		}
	}
	
	Color[] additive;
	public void newAdditive(){
		additive=new Color[pixels.length];
	}
	public void resetAdditive(){
		//additive=null;
		for(int i=0;i<pixels.length;i++)
			additive[i]=null;
	}
	public Color[] getAdditive(){return additive;}
	public void renderAdditiveLight(int xPos, int yPos, int w, int h, Color color, int alpha){
		xPos -= xOffset; //Minus because otherwise movement would be reversed
		yPos -= yOffset;
		color=new Color(color.getRed(),color.getGreen(),color.getBlue(),alpha);
		for (int y = 0; y < h; y++) {
			int ya = yPos + y;
			if(ya < -h || ya >= height) break; if(ya<0) ya=0; 
			for (int x = 0; x < w; x++) {
				int xa = xPos + x;
				if(xa < -w || xa >= width) break; if(xa<0)xa=0;
				if(pixels [xa + ya * width] != 0){
					additive [xa + ya * width] = color;//blend(color, pixels[xa+ya*width]);
				}
			}
		}
	}
	public void displayAdditive(){
		if(additive!=null){
			Color c;
			for(int i=0;i<pixels.length;i++)
				if(additive[i]!=null){
					c=new Color(pixels[i]);
					pixels[i]=blendColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),255-additive[i].getAlpha()),additive[i]);				
				}
			resetAdditive();
		}
	}
	
	public void renderUI(int xPos, int yPos, Sprite sprite, int color){
		if(sprite==null){
			for (int y = 0; y < Game.TILE_SIZE; y++) {
				int ya = yPos + y;
				if(ya < -Game.TILE_SIZE || ya >= height) break;
				if(ya<0) ya=0;
				for (int x = 0; x < Game.TILE_SIZE; x++) {
					int xa = xPos + x;
					if(xa < -Game.TILE_SIZE || xa >= width) break;
					if(xa<0)xa=0;
					pixels [xa + ya * width] = 0x000000;
				}
			}
			return;
		}
		for (int y = 0; y < sprite.HEIGHT; y++) {
			int ya = yPos + y;
			if(ya < -sprite.HEIGHT || ya >= height) break;
			if(ya<0) ya=0;
			for (int x = 0; x < sprite.WIDTH; x++) {
				int xa = xPos + x;
				if(xa < -sprite.WIDTH || xa >= width) break;
				if(xa<0)xa=0;
				if(sprite.pixels[x+y*sprite.WIDTH]!=0x00ffffff){ //Don't render transparent pixels
					pixels [xa + ya * width] = color;//sprite.pixels[x + y * sprite.WIDTH];
				}else 
					pixels [xa + ya * width] = 0x000000;
			}
		}
	}
	
	public void renderProjectile(int xPos, int yPos, Sprite sprite, int color){
		xPos -= xOffset; //Minus because otherwise movement would be reversed
		yPos -= yOffset;
		for (int y = 0; y < sprite.HEIGHT; y++) {
			int ya = yPos + y;
			if(ya < -sprite.HEIGHT || ya >= height) break;
			if(ya<0) ya=0;
			for (int x = 0; x < sprite.WIDTH; x++) {
				int xa = xPos + x;
				if(xa < -sprite.WIDTH || xa >= width) break;
				if(xa<0)xa=0;
				if(sprite.pixels[x+y*sprite.WIDTH]!=0x00ffffff){ //Don't render transparent pixels
					pixels [xa + ya * width] = color;//sprite.pixels[x + y * sprite.WIDTH];
				}else 
					pixels [xa + ya * width] = 0x000000;
			}
		}
	}
	
	private int targetXOffset, targetYOffset;
	public void setOffset(int x,int y){
		targetXOffset = x;
		targetYOffset = y;
	}
	
	public void snapOffsetTo(int x,int y){
		xOffset = x;
		yOffset = y;
	}

	//Clears, replacing all pixels to background color (black)
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
			background[i] = 0;
		}
	}
	
	public void update(){
		if(isRainbow()){updateRainbowEffect();}
		
		//Camera smoothing
		if(targetXOffset!=xOffset){
			if(xOffset<targetXOffset) xOffset+=cameraSpeed;
			if(xOffset>targetXOffset) xOffset-=cameraSpeed;
		}
		if(targetYOffset!=yOffset){
			if(yOffset<targetYOffset) yOffset+=cameraSpeed;
			if(yOffset>targetYOffset) yOffset-=cameraSpeed;
		}
	}
	
	public static int blend(int colorA, int colorB) {
		Color c0 = new Color(colorA);
		Color c1 = new Color(colorB);
		
	    double totalAlpha = c0.getAlpha() + c1.getAlpha();
	    double weight0 = c0.getAlpha() / totalAlpha;
	    double weight1 = c1.getAlpha() / totalAlpha;

	    double r = weight0 * c0.getRed() + weight1 * c1.getRed();
	    double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
	    double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
	    double a = Math.max(c0.getAlpha(), c1.getAlpha());
	    
	    return new Color((int) r, (int) g, (int) b, (int) a).getRGB();
	}

	public static int blendColor(Color colorA, Color colorB) {
		Color c0 = colorA;
		Color c1 = colorB;
		
	    double totalAlpha = c0.getAlpha() + c1.getAlpha();
	    double weight0 = c0.getAlpha() / totalAlpha;
	    double weight1 = c1.getAlpha() / totalAlpha;

	    double r = weight0 * c0.getRed() + weight1 * c1.getRed();
	    double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
	    double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
	    double a = Math.max(c0.getAlpha(), c1.getAlpha());
	    
	    return new Color((int) r, (int) g, (int) b, (int) a).getRGB();
	}
	
	public void activateRainbowEffect(){
		int index=-1;
		for(int i=0;i<rainbowsActive.length;i++){
			if(rainbowsActive[i]==false){
				rainbowsActive[i]=true;
				index=i;
				break;
			}
		}
		if(index!=-1)
			renderCount[index]=0;
	}
	
	public boolean isRainbow(){
		for(int i=0;i<rainbowsActive.length;i++){
			if(rainbowsActive[i]==true){
				return true;
			}
		}
		return false;
	}
	
	
	private final Color[] rainbowCols = {Color.red, Color.orange, Color.green, Color.cyan, Color.blue, Color.magenta};
	//private int startX = width;
	private final int buffer = 20;
	private final float loops = .5f, speed = 5f, colorSpeed=0.1f, angle = 4;
	private int[] renderCount = new int[100];
	private boolean[] rainbowsActive = new boolean[100];
	
	public void renderRainbowEffect(){
		//if(!rainbow) return;
		for(int i=0;i<renderCount.length;i++){
			if(rainbowsActive[i]==false) continue;
						
			int colorMovement = (int)(renderCount[i]*colorSpeed);
			
			int xStart = 0;
			int xPos = renderCount[i];
			
			
			if(renderCount[i]/loops >= width)
				xStart = (int)(renderCount[i]-width*loops);
			if(renderCount[i]/width>loops){
				if(xStart>=width+height){
					renderCount[i]=0;
					rainbowsActive[i]=false;
					return;
				}
			}
			
			for (int y = 0; y < height; y++) {
				int sub =(int)(y/angle);
				for (int x = xStart; x < xPos; x++) {
					if(x-sub>=width)break;
					if(x-sub>0 && x+colorMovement>0 && pixels [x-sub + y * width] != 0){
						pixels [x-sub + y * width] = rainbowCols[((x+colorMovement)/buffer)%rainbowCols.length].getRGB();
					}
				}
			}
		}
	}
	
	public void updateRainbowEffect(){
		for(int i=0;i<renderCount.length;i++){
			renderCount[i]+=speed;
		}
	}
	
}

/*
 *  Render base and other stuff commented here for future use
 * 
 * 
 * 
	private final int mapSize = 100;
	public int[] tiles = new int[mapSize * mapSize];
	private Random random = new Random();
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];

		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = random.nextInt(0xffffff);
		}
	}
	
	
	 	public void render(int xOffset, int yOffset) {//Take in x and y offsets
		for (int y = 0; y < height; y++) {
			int yPixel = y + yOffset;
			if (yPixel < 0 || yPixel >= height) continue;
			for (int x = 0; x < width; x++) { // Nest x in y, and multiple y by width
				int xPixel = x + xOffset;
				if (xPixel < 0 || xPixel >= width) continue;
				pixels[xPixel + yPixel * width] = Sprite.a.pixels[(x % 6) + (y % 6) * 7];//tiles[tileIndex]; 364,56
			}
		}
	}
 */ 
