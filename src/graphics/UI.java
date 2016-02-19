package graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import game.Game;

public class UI {
	
	public static UI combatUI = new UI("/ui/combat",Align.Bottom_Right,Type.Combat, true);
	public static UI combatUIDir = new UI("/ui/combatDirection",Align.Bottom_Right,Type.Combat, true);
	public static UI healthUI = new UI("/ui/health",Align.Bottom_Left,Type.Health, true);
	public static UI statusUI = new UI(Align.Top,Type.Status, true);
	
	public static UI waitingForServerLevel = new UI("/ui/waiting",Align.Center,Type.Standby,true);
	//public static UI pauseMenu = new UI("/ui/pause",Align.Center, Type.Pause, true);
	
	public boolean active;
	public Align alignment;
	public Type type;
	public Sprite[][] sprites;
	public final Sprite[][] startSprites;
	public Color[][] colors;
	public Color defaultColor=Color.white;
	public final Color[][] startColors;
	private int width, height;
	
	public static enum Type{
		Combat,Pause,Health,Status,Standby
	}
	
	private final static int pixelOffset=7;
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
		startSprites=null;
		startColors=null;
	}
	
	public UI(String path, Align alignment,Type type, boolean active) {
		this.type=type;
		this.alignment=alignment;
		this.active = active;
		
		String uiPath = path + ".txt";
		int w = 0, h = 0;

		ArrayList<String> lines = new ArrayList<String>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(UI.class.getResourceAsStream(uiPath)));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		/*List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(uiPath));
		} catch (Exception e) {
			e.printStackTrace();
		}*/

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
		startSprites=sprites;
		
		String colPath = path + ".png";
		int[] pixels = null;
		if(this.getClass().getResource(colPath)!=null){//new File(colPath).exists()){
			BufferedImage fileImg = null;
			try{fileImg =ImageIO.read(this.getClass().getResource(colPath));}catch (IOException e) {e.printStackTrace();}
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
	
	public void setStatus(String status, String observation){
		status=status.toUpperCase();
		observation=observation.toUpperCase();
		
		width=Math.max(status.length(), observation.length());
		char[][] ui = new char[width][3];
		for (int x = 0; x < width; x++) {
			if(x<status.length())
				ui[x][0] = status.charAt(x);
		}
		for (int x = 0; x < width; x++) {
			if(x<observation.length())
				ui[x][2] = observation.charAt(x);
		}

		sprites=new Sprite[width][3];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < ui[x].length; y++) {
				if(!Character.isWhitespace(ui[x][y])){
					sprites[x][y] = Sprite.getSprite(ui[x][y]);
				}
			}
		}
		
		colors=new Color[width][3];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < colors[x].length; y++) {
				colors[x][y]=defaultColor;//Color.white;
			}
		}
		//startColors=colors;
	}
	public void clearStatus(){sprites=null;width=0;colors=null;}

	//
	//Combat UI functions
	//

}
