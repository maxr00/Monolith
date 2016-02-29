package graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import game.Game;
import player.Spell;

public class UI {
	
	public static UI combatUI = new UI("/ui/combat",Align.Bottom_Right,Type.Combat, false);
	public static UI combatUIDir = new UI("/ui/combatDirection",Align.Bottom_Right,Type.Combat, false);
	public static UI healthUI = new UI("/ui/health",Align.Bottom_Left,Type.Health, false);
	public static UI statusUI = new UI(Align.Top,Type.Status, true);
	public static UI levelReadyUI = new UI(Align.Top,Type.LevelUpReady,new String[]{"PRESS L TO LEVEL UP"},Color.green, false);
	public static UI popupUI = new UI(Align.Center_Left,Type.Popup, true);
	
	public static UI waitingForServerLevel = new UI("/ui/waiting",Align.Center,Type.Standby,false);
	
	public boolean active;
	public Align alignment;
	public Type type;
	public Sprite[][] sprites;
	public final Sprite[][] startSprites;
	public Color[][] colors, backgroundColors;
	public Color defaultColor=Color.white;
	public final Color[][] startColors;
	private int width, height;
	
	public static enum Type{
		Combat,Health,Status,Standby,LevelUpReady,Popup
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

	public static ArrayList<UI> UIElements;
	
	public UI(Align alignment,Type type,boolean active){
		this.type=type;
		this.alignment=alignment;
		this.active=active;
		startSprites=null;
		startColors=null;
		
		if(UIElements==null)
			UIElements=new ArrayList<UI>();
		UIElements.add(this);
	}
	
	public UI(Align alignment,Type type,String[] text,Color def,boolean active){
		this.type=type;
		this.alignment=alignment;
		this.active=active;
		startSprites=null;
		startColors=null;
		defaultColor=def;
		
		sprites=new Sprite[text[0].length()][text.length];
		for (int x = 0; x < sprites.length; x++) {
			for (int y = 0; y < sprites[x].length; y++) {
				if(x<text[y].length()){
					if(!Character.isWhitespace(text[y].charAt(x))){
						sprites[x][y] = Sprite.getSprite(text[y].charAt(x));
					}
				}
			}
		}
		colors=new Color[text[0].length()][text.length];
		for(int x=0;x<colors.length;x++)
			for(int y=0;y<colors[y].length;y++)
				colors[x][y]=defaultColor;
		
		if(UIElements==null)
			UIElements=new ArrayList<UI>();
		UIElements.add(this);
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
			backgroundColors=new Color[width][height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					colors[x][y]=new Color(pixels[x+y*width]);
					backgroundColors[x][y]=Color.black;
				}
			}
		}else{
			colors=new Color[width][height];
			backgroundColors=new Color[width][height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					colors[x][y]=defaultColor;//Color.white;
					backgroundColors[x][y]=Color.black;
				}
			}
		}
		startColors=colors;
		
		if(UIElements==null)
			UIElements=new ArrayList<UI>();
		UIElements.add(this);
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
		if(sprites==null || sprites.length==0 || !active)
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
				screen.renderUI(x*Game.TILE_SIZE+xOffset, y*Game.TILE_SIZE+yOffset, sprites[x][y], colors[x][y].getRGB(), backgroundColors!=null && backgroundColors[x][y]!=null ? backgroundColors[x][y]==Color.black ? Screen.defaultBackground : backgroundColors[x][y].getRGB() : Screen.defaultBackground);
			}
		}
	}
	
	public void setRunes() {
		sprites[1][2]=Sprite.getSprite(Spell.runes[0].character);
		sprites[3][2]=Sprite.getSprite(Spell.runes[1].character);
		sprites[5][2]=Sprite.getSprite(Spell.runes[2].character);
		sprites[1][4]=Sprite.getSprite(Spell.runes[3].character);
		sprites[3][4]=Sprite.getSprite(Spell.runes[4].character);
		sprites[5][4]=Sprite.getSprite(Spell.runes[5].character);
		sprites[1][6]=Sprite.getSprite(Spell.runes[6].character);
		sprites[3][6]=Sprite.getSprite(Spell.runes[7].character);
		sprites[5][6]=Sprite.getSprite(Spell.runes[8].character);
		colors[1][2]=Spell.runes[0].color;
		colors[3][2]=Spell.runes[1].color;
		colors[5][2]=Spell.runes[2].color;
		colors[1][4]=Spell.runes[3].color;
		colors[3][4]=Spell.runes[4].color;
		colors[5][4]=Spell.runes[5].color;
		colors[1][6]=Spell.runes[6].color;
		colors[3][6]=Spell.runes[7].color;
		colors[5][6]=Spell.runes[8].color;
		for(int x=0;x<backgroundColors.length;x++)
			for(int y=0;y<backgroundColors[x].length;y++)
				backgroundColors[x][y]=Color.black;
	}
	
	public void setStatus(String status, String observation){
		status=status.toUpperCase();
		observation=observation.toUpperCase();
		
		width=Math.max(status.length(), observation.length());
		char[][] ui;
		if(observation.length()>0){
			ui = new char[width][3];
		}else
			ui = new char[width][1];
		
		for (int x = 0; x < width; x++) {
			if(x<status.length())
				ui[x][0] = status.charAt(x);
		}
		if(ui[0].length>1)
			for (int x = 0; x < width; x++) {
				if(x<observation.length())
					ui[x][2] = observation.charAt(x);
			}

		if(observation.length()>0)
			sprites=new Sprite[width][3];
		else
			sprites=new Sprite[width][1];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < ui[x].length; y++) {
				if(!Character.isWhitespace(ui[x][y])){
					sprites[x][y] = Sprite.getSprite(ui[x][y]);
				}
			}
		}
		
		if(observation.length()>0)
			colors=new Color[width][3];
		else
			colors=new Color[width][1];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < colors[x].length; y++) {
				colors[x][y]=defaultColor;//Color.white;
			}
		}
		//startColors=colors;
	}
	public void clearStatus(){sprites=null;width=0;colors=null;}

	int amt=100,currSpot=(int)(-amt*1.1f), speed=4, count=0;
	int waitTime=300;
	public boolean standByUpdate(){
		count++;
		if(count%speed==0){
			currSpot++;
			if(currSpot>sprites.length) currSpot=-amt;
			
			this.colors=new Color[sprites.length][sprites[0].length];
			for(int x=0;x<colors.length;x++){
				for(int y=0;y<colors[x].length;y++){
					colors[x][y]=defaultColor;
				}
			}
			int extra=0;
			for(int i=0;i<amt;i++){
				int spot=currSpot+i+extra;
				for(int x=0;x<colors.length;x++){
					for(int y=0;y<colors[x].length;y++){
						if(spot>=0){
							if(x==spot){
								if(sprites[x][y]==null){
									extra++;
									spot++;
								}else
									colors[x][y]=new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));//new Color((int)(Math.random()*106)+150,100,100);//Color.blue;
							}
						}
					}
				}
			}
		}
		if(count%waitTime==0){
			return true;
		}
		return false;
	}
	
	public void setPopups(String[] popups,Color[] popupColors){
		int width=0;
		for(String s : popups){
			if(s.length()>width)
				width=s.length();
		}
		
		sprites=new Sprite[width][popups.length];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < sprites[x].length; y++) {
				if(x<popups[y].length()){
					if(!Character.isWhitespace(popups[y].charAt(x))){
						sprites[x][y] = Sprite.getSprite(popups[y].charAt(x));
					}
				}
			}
		}
		colors=new Color[width][popupColors.length];
		for(int x=0;x<colors.length;x++)
			for(int y=0;y<colors[x].length;y++)
				colors[x][y]=popupColors[y];
	}

}
