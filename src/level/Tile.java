package level;

import java.awt.Color;

import entity.mob.Mob;
import game.Game;
import graphics.Screen;
import graphics.Sprite;
import level.RandomLevel.RTile;

public class Tile {

	public int tint,background;
	private Sprite sprite;
	private char character;
	private boolean solid;
	public boolean render=false, renderLight=false, renderGray;
	public int[] colorBlemishes;
	public boolean hasBeenSeen;
	public int type;
	//private final Random random = new Random();
	
	public boolean reflectMobs=false;
	private int x,y;
	
	public Tile(int type, int x,int y, char character, boolean solid, int defaultTint, int defaultBackground, int[] blemishes){//float blemishRate) {
		this.x=x;
		this.y=y;
		this.type=type;
		this.character=character;
		this.solid = solid;
		this.tint = defaultTint;
		this.background=defaultBackground;
		
		colorBlemishes = blemishes;
		sprite = Sprite.getSprite(character);
		
		if(type!=-1)
			reflectMobs = RTile.values()[type] == RTile.Water;
		
	}
	
	public void update() {
	}
	
	public void render(int x, int y, Screen screen) {
		if(render){
			screen.renderSprite(x, y, sprite);
		}
	}

	public void renderLight(int x, int y, Screen screen) {
		if(renderLight && sprite!=null){
			if(renderGray)											//Color.black.getRGB()
				screen.renderLight(x, y, sprite.WIDTH, sprite.HEIGHT, Screen.defaultBackground, colorBlemishes);
			else{
				screen.renderLight(x, y, sprite.WIDTH, sprite.HEIGHT, tint, colorBlemishes);
			}
		}
		if(renderLight && !renderGray)
			if(background!=-1){
				if(background!=Color.black.getRGB())
					screen.renderBackground(x, y, Game.TILE_SIZE, Game.TILE_SIZE, background);
				else
					screen.renderBackground(x, y, Game.TILE_SIZE, Game.TILE_SIZE, Screen.defaultBackground);
			}
/*		if(background!=-1)
			if(background!=Color.black.getRGB())
				screen.renderBackground(x, y, Game.TILE_SIZE, Game.TILE_SIZE, background);
*/
	}
	
	public void renderLight(int x, int y, Screen screen, int manualColor) {
		if(renderLight && sprite!=null){
			screen.renderLight(x, y, sprite.WIDTH, sprite.HEIGHT, manualColor, colorBlemishes);
		}
	}
	
	public boolean isSolid() {
		return solid;
	}

	public void doRender(boolean r){ render=r; }
	
	public void doRenderLight(boolean r){ renderLight=r; }
	
	public Sprite getSprite() {
		return sprite;
	}

	public void setTint(int tint){
		this.tint = tint;
	}
	
	public String toString(){return ""+character;}

	
}
