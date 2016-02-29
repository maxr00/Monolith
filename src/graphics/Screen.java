package graphics;

import java.awt.Color;
import java.util.Random;

import game.Game;

public class Screen {

	public int width;
	public int height;
	public int[] pixels, background; // Must be 1D array because Game.java's renderPixels must be 1D
	public float xOffset;
	public float yOffset;
	private float cameraSpeed=0.5f;
	Random random = new Random();
	
	public static int defaultBackground=0x000000;
	public static Color defaultBackgroundColor=Color.black;
	
	// Takes raw width and height (not multiplied by pixel scale)
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		background = new int[width * height];
		
		defaultBackgroundColor=new Color(30,30,30);//=new Color(random.nextInt());
		defaultBackground=defaultBackgroundColor.getRGB();
	}
	
	public void renderSprite(int xPos, int yPos, Sprite sprite){
		xPos -= xOffset + shake_xOffset; //Minus because otherwise movement would be reversed
		yPos -= yOffset + shake_yOffset;
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
					pixels [xa + ya * width] = defaultBackground;
				
			}
		}
	}
	
	public void renderLight(int xPos, int yPos, int w, int h, int color, int[] colBlemishes){
		//if(rainbow) {color= Color.black.getRGB(); colBlemishes=null;}
		
		xPos -= xOffset + shake_xOffset; //Minus because otherwise movement would be reversed
		yPos -= yOffset + shake_yOffset;
		for (int y = 0; y < h; y++) {
			int ya = yPos + y;
			if(ya < -h || ya >= height) break; if(ya<0) ya=0; 
			for (int x = 0; x < w; x++) {
				int xa = xPos + x;
				if(xa < -w || xa >= width) break; if(xa<0)xa=0;
				if(pixels [xa + ya * width] != defaultBackground){
					if(colBlemishes != null && colBlemishes[x+y]!=0){
						pixels [xa + ya * width] = blend(color, colBlemishes[x+y]);
					}else
						pixels [xa + ya * width] = color;
				}
			}
		}
	}
	
	public void renderBackground(int xPos, int yPos, int w, int h, int color){
		xPos -= xOffset + shake_xOffset;
		yPos -= yOffset + shake_yOffset;
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
		xPos -= xOffset + shake_xOffset; //Minus because otherwise movement would be reversed
		yPos -= yOffset + shake_yOffset;
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
					particles [xa + ya * width] = defaultBackground;
					
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
		xPos -= xOffset + shake_xOffset; //Minus because otherwise movement would be reversed
		yPos -= yOffset + shake_yOffset;
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
				if(additive[i]!=null && pixels[i]!=defaultBackground){
					c=new Color(pixels[i]);
					pixels[i]=blendColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),255-additive[i].getAlpha()),additive[i]);				
				}
			resetAdditive();
		}
	}
	
	public void renderUI(int xPos, int yPos, Sprite sprite, int color, int background){
		if(sprite==null){
			for (int y = 0; y < Game.TILE_SIZE; y++) {
				int ya = yPos + y;
				if(ya < -Game.TILE_SIZE || ya >= height) break;
				if(ya<0) ya=0;
				for (int x = 0; x < Game.TILE_SIZE; x++) {
					int xa = xPos + x;
					if(xa < -Game.TILE_SIZE || xa >= width) break;
					if(xa<0)xa=0;
					pixels [xa + ya * width] = defaultBackground;
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
					pixels [xa + ya * width] = defaultBackground;
			}
		}
		//Background
		for (int y = 0; y < Game.TILE_SIZE; y++) {
			int ya = yPos + y;
			if(ya < -Game.TILE_SIZE || ya >= height) break; if(ya<0) ya=0;
			for (int x = 0; x < Game.TILE_SIZE; x++) {
				int xa = xPos + x;
				if(xa < -Game.TILE_SIZE || xa >= width) break; if(xa<0)xa=0;
				this.background[xa + ya * width]=background;
			}
		}
	}
	
	public void renderProjectile(int xPos, int yPos, Sprite sprite, int color){
		xPos -= xOffset + shake_xOffset; //Minus because otherwise movement would be reversed
		yPos -= yOffset + shake_yOffset;
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
					pixels [xa + ya * width] = defaultBackground;
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
			pixels[i] = defaultBackground;
			background[i] = defaultBackground;
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
		
		if(isShaking)updateShake();
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
	
	
	//Default amt = 2
	public void renderGlitchEffect(int amt){
		int a=random.nextInt(width), b=random.nextInt(height);
		splitRGB_UL(a+amt, b+amt, random.nextInt(width-a)-amt, random.nextInt(height-b)-amt, 0,true, amt, random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
		int a1=random.nextInt(width), b1=random.nextInt(height);
		splitRGB_DR(a1+amt, b1+amt, random.nextInt(width-a1)-amt, random.nextInt(height-b1)-amt, 0,true, amt, random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
	}
	
	public void renderGlitchEffect(int min,int max){
		int a=random.nextInt(width), b=random.nextInt(height);
		splitRGB_UL(a+max, b+max, random.nextInt(width-a)-max, random.nextInt(height-b)-max, 0,true, random.nextInt(max-min)+min, random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
		int a1=random.nextInt(width), b1=random.nextInt(height);
		splitRGB_DR(a1+max, b1+max, random.nextInt(width-a1)-max, random.nextInt(height-b1)-max, 0,true, random.nextInt(max-min)+min, random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
	}
	
	public void splitRGB_UL(int x,int y,int w,int h,int range, boolean isSquare,int amt,boolean distRed,boolean distGreen,boolean distBlue){
		splitRGB_UL(x,y,w,h,range,0,isSquare,amt,distRed,distGreen,distBlue);
	}
	
	public void splitRGB_UL(int x,int y,int w,int h,int range,int start, boolean isSquare,int amt,boolean distRed,boolean distGreen,boolean distBlue){
		int xStart = x-range;
		int xEnd   = x+w+range;
		int yStart = y-range;
		int yEnd   = y+h+range;
		
		Color color, lost, distorted;
		for(int dx=xStart;dx<xEnd;dx++){
			for(int dy=yStart;dy<yEnd;dy++){
				if((isSquare && (dx-w/2 < start && dy-h/2 < start)) || (!isSquare && (dx-x-w/2)*(dx-x-w/2) + (dy-y-h/2)*(dy-y-h/2) > (start)*(start)))
					if((isSquare || (dx-x-w/2)*(dx-x-w/2) + (dy-y-h/2)*(dy-y-h/2) < (range)*(range)) && dx+dy*this.width<pixels.length && dx+dy*this.width>=0 && pixels[dx+dy*this.width]!=0){
						color=new Color(pixels[dx+dy*this.width]);
						distorted=new Color(distRed ? color.getRed() : defaultBackgroundColor.getRed(), distGreen ? color.getGreen() : defaultBackgroundColor.getGreen(), distBlue ? color.getBlue() : defaultBackgroundColor.getBlue());
						//Inverse of distorted
						lost = new Color(distRed ? defaultBackgroundColor.getRed() : color.getRed(), distGreen ? defaultBackgroundColor.getGreen() : color.getGreen(), distBlue ? defaultBackgroundColor.getBlue() : color.getBlue());
						
						
						pixels[dx+dy*width]=lost.getRGB();
						if(dx-amt>=0 && dy-amt>=0 && dx-amt<this.width && dy-amt<this.height && pixels[dx-amt + (dy-amt)*this.width]==0){
							pixels[dx-amt + (dy-amt)*this.width]=distorted.getRGB();
						}
					}
				
			}
		}
	}
	
	public void splitRGB_DR(int x,int y,int w,int h,int range, boolean isSquare,int amt,boolean distRed,boolean distGreen,boolean distBlue){
		splitRGB_DR(x,y,w,h,range,0,isSquare,amt,distRed,distGreen,distBlue);
	}
	public void splitRGB_DR(int x,int y,int w,int h,int range,int start, boolean isSquare,int amt,boolean distRed,boolean distGreen,boolean distBlue){
		int xStart = x-range;
		int xEnd   = x+w+range;
		int yStart = y-range;
		int yEnd   = y+h+range;
		
		Color color, lost, distorted;
		for(int dx=xEnd-1;dx>=xStart;dx--){
			for(int dy=yEnd-1;dy>=yStart;dy--){
				if((isSquare && (dx-w/2 < start && dy-h/2 < start)) || (!isSquare && (dx-x-w/2)*(dx-x-w/2) + (dy-y-h/2)*(dy-y-h/2) > (start)*(start)))
					if((isSquare || (dx-x-w/2)*(dx-x-w/2) + (dy-y-h/2)*(dy-y-h/2) < (range)*(range)) &&  pixels[dx+dy*this.width]!=0){
						color=new Color(pixels[dx+dy*this.width]);
						distorted=new Color(distRed ? color.getRed() : defaultBackgroundColor.getRed(), distGreen ? color.getGreen() : defaultBackgroundColor.getGreen(), distBlue ? color.getBlue() : defaultBackgroundColor.getBlue());
						//Inverse of distorted
						lost = new Color(distRed ? defaultBackgroundColor.getRed() : color.getRed(), distGreen ? defaultBackgroundColor.getGreen() : color.getGreen(), distBlue ? defaultBackgroundColor.getBlue() : color.getBlue());
						
						
						pixels[dx+dy*width]=lost.getRGB();
						if(dx+amt>=0 && dy+amt>=0 && dx+amt<this.width && dy+amt<this.height && pixels[dx+amt + (dy+amt)*this.width]==0){
							pixels[dx+amt + (dy+amt)*this.width]=distorted.getRGB();
						}
					}
				
			}
		}
	}
	
	public float shake_xOffset, shake_yOffset;
	public void setShakeOffset(float x,float y){
		shake_xOffset=x;
		shake_yOffset=y;
	}
	
	public boolean isShaking=false;
	private int shake_speed, shake_count=0, shake_xAmt, shake_yAmt;
	private float shake_duration;
	public void setShakeEffect(float duration, int xAmt, int yAmt, int speed){
		shake_speed=speed;
		shake_xAmt=xAmt;
		shake_yAmt=yAmt;
		shake_duration=duration;
		isShaking=true;
		shake_count=0;
	}
	private void updateShake(){
		shake_count++;
		if(shake_count>shake_duration*60){ shake_xOffset=0; shake_yOffset=0; isShaking=false; return;}

		shake_xOffset=(float)((shake_xAmt)*Math.sin(2*Math.PI*(shake_count/(double)shake_speed)));
		shake_yOffset=(float)((shake_yAmt)*Math.sin(2*Math.PI*(shake_count/(double)shake_speed)));
	}
	
	public void renderGlow(int xPos, int yPos, int w, int h, Color color,int range, int alpha){
		xPos -= xOffset + shake_xOffset; //Minus because otherwise movement would be reversed
		yPos -= yOffset + shake_yOffset;
		color=new Color(color.getRed(),color.getGreen(),color.getBlue(),alpha);
		for (int y = -range; y < h+range; y++) {
			int ya = yPos + y;
			if(ya < 0 || ya >= height) break; if(ya<0) ya=0; 
			for (int x = -range; x < w+range; x++) {
				int xa = xPos + x;
				if(xa < 0 || xa >= width) break; if(xa<0)xa=0;
				
				//If in circle
				if( (xa-xPos-w/2)*(xa-xPos-w/2) + (ya-yPos-h/2)*(ya-yPos-h/2) < (range)*(range) )
					//if(pixels [xa + ya * width] != 0){
						additive [xa + ya * width] = color;//blend(color, pixels[xa+ya*width]);
					//}
				
			}
		}
	}
}
/*

Circle formula:

(dx-x-w/2)*(dx-x-w/2) + (dy-y-h/2)*(dy-y-h/2) < (range)*(range)

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
