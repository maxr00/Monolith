package graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

import game.Game;

public class UI {
	
	public static UI combatUI = new UI("res/ui/combat",Align.Bottom_Right,Type.Combat, true);
	public static UI combatUIDir = new UI("res/ui/combatDirection",Align.Bottom_Right,Type.Combat, true);
	public static UI healthUI = new UI("res/ui/health",Align.Bottom_Left,Type.Health, true);
	public static UI statusUI = new UI(Align.Top,Type.Status, true);
	
	public boolean active;
	public Align alignment;
	public Type type;
	public Sprite[][] sprites;
	public Color[][] colors;
	public Color defaultColor=Color.white;
	public Color[][] startColors;
	private int width, height;
	private final static int pixelOffset=7;
	
	public static enum Type{
		Combat,Pause,Health,Status
	}
	
	public static enum Align {
		Top_Left	(pixelOffset,pixelOffset),//(0.1f,0.1f),
		Top			(0,pixelOffset),//(0.5f,0.1f),
		Top_Right	(-pixelOffset,pixelOffset),//(0.9f,0.1f),
		Center_Left	(pixelOffset,0),//(0.1f,0.5f),
		Center		(0,0),//(0.5f,0.5f),
		Center_Right(pixelOffset,0),//(0.9f,0.5f),
		Bottom_Left	(pixelOffset,-pixelOffset),//(0.1f,0.9f),
		Bottom		(0,-pixelOffset),//(0.5f,0.9f),
		Bottom_Right(-pixelOffset,-pixelOffset);//(0.9f,0.9f);
		
		public float xRelativeOffset=-1,yRelativeOffset=-1;
		public int xPixelOffset, yPixelOffset;
		private Align(float x,float y){
			xRelativeOffset = x;
			yRelativeOffset = y;
		}
		private Align(int x,int y){
			xPixelOffset = x;
			yPixelOffset = y;
		}
	}

	public UI(Align alignment,Type type,boolean active){
		this.type=type;
		this.alignment=alignment;
		this.active=active;
	}
	
	public UI(String path, Align alignment,Type type, boolean active) {
		this.type=type;
		this.alignment=alignment;
		this.active = active;
		
		String uiPath = path + ".txt";
		int w = 0, h = 0;

		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(uiPath));
		} catch (Exception e) {
			e.printStackTrace();
		}

		h = lines.size();
		for (String l : lines) {
			if (l.length() > w) w = l.length();
		}

		width = w;
		height = h;

		char[][] ui = new char[w][h];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				ui[x][y] = lines.get(y).charAt(x);
			}
		}

		sprites=new Sprite[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if(!Character.isWhitespace(ui[x][y])){
					sprites[x][y] = Sprite.getSprite(ui[x][y]);
				}
			}
		}
		
		String colPath = path + ".png";
		int[] pixels = null;
		if(new File(colPath).exists()){
			BufferedImage fileImg = null;
			try{fileImg = ImageIO.read(new File(colPath));}catch (IOException e) {e.printStackTrace();}
			BufferedImage bufImg = fileImg;
			BufferedImage img = new BufferedImage(bufImg.getWidth(), bufImg.getHeight(), BufferedImage.TYPE_INT_RGB);
			img.getGraphics().drawImage(bufImg, 0, 0, null);
			pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
			colors=new Color[width][height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					colors[x][y]=new Color(pixels[x+y*width]);
				}
			}
		}else{
			colors=new Color[width][height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					colors[x][y]=defaultColor;//Color.white;
				}
			}
		}
		startColors=colors;
	}
	
	public void setDefaultColor(Color c){
		defaultColor=c;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				colors[x][y]=defaultColor;//Color.white;
			}
		}
	}
	
	private int xOffset,yOffset;
	public void render(Screen screen){
		if(sprites==null)
			return;
		
		if(alignment.xRelativeOffset>=0)
			xOffset=(int)(alignment.xRelativeOffset*screen.width);
		else{
			if(alignment.xPixelOffset<0){
				xOffset = screen.width + alignment.xPixelOffset - (sprites.length*Game.TILE_SIZE);
			}else if(alignment.xPixelOffset==0){
				xOffset = screen.width/2 - (sprites.length*Game.TILE_SIZE)/2;
			}else if(alignment.xPixelOffset>0){
				xOffset = alignment.xPixelOffset;
			}
		}

		if(alignment.yRelativeOffset>=0)
			yOffset=(int)(alignment.yRelativeOffset*screen.height);
		else{
			if(alignment.yPixelOffset<0)
				yOffset = screen.height + alignment.yPixelOffset - sprites[0].length*Game.TILE_SIZE;
			else if(alignment.yPixelOffset==0)
				yOffset = screen.height/2 - (sprites[0].length*Game.TILE_SIZE)/2;
			else if(alignment.yPixelOffset>0)
				yOffset = alignment.yPixelOffset;
		}
		
		for (int x = 0; x < sprites.length; x++) {
			for (int y = 0; y < sprites[x].length; y++) {
				screen.renderUI(x*Game.TILE_SIZE+xOffset, y*Game.TILE_SIZE+yOffset, sprites[x][y], colors[x][y].getRGB());
			}
		}
	}
	
	public void setStatus(String status){
		status=status.toUpperCase();
		
		width=status.length();
		char[] ui = new char[width];
		for (int x = 0; x < width; x++) {
			ui[x] = status.charAt(x);
		}

		sprites=new Sprite[width][1];
		for (int x = 0; x < width; x++) {
			if(!Character.isWhitespace(ui[x])){
				sprites[x][0] = Sprite.getSprite(ui[x]);
			}
		}
		
		colors=new Color[width][1];
		for (int x = 0; x < width; x++) {
			colors[x][0]=defaultColor;//Color.white;
		}
		startColors=colors;
	}
	public void clearStatus(){sprites=null;width=0;colors=null;}

	//
	//Combat UI functions
	//

}
