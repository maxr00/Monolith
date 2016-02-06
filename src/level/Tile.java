package level;

import graphics.Screen;
import graphics.Sprite;

public class Tile {

	public int tint;
	private Sprite sprite;
	private char character;
	private boolean solid, render=true;
	public int[] colorBlemishes;
	public boolean hasBeenSeen;
	//private final Random random = new Random();
	
	public Tile(char character, boolean solid, int defaultTint, int[] blemishes){//float blemishRate) {
		this.character=character;
		this.solid = solid;
		this.tint = defaultTint;
		
		colorBlemishes = blemishes;
		sprite = Sprite.getSprite(character);
		if(sprite==null)
			System.out.println(character);
	}
	
	public void render(int x, int y, Screen screen) {
		if(render){
			screen.renderSprite(x, y, sprite);
		}
	}

	public void renderLight(int x, int y, Screen screen) {
		if(render){
			screen.renderLight(x, y, sprite.WIDTH, sprite.HEIGHT, tint, colorBlemishes);
		}
	}
	
	public void renderLight(int x, int y, Screen screen, int manualColor) {
		if(render){
			screen.renderLight(x, y, sprite.WIDTH, sprite.HEIGHT, manualColor, colorBlemishes);
		}
	}
	
	public void update(){

	}
	
	public boolean isSolid() {
		return solid;
	}

	public void doRender(boolean r){ render=r; }
	
	public Sprite getSprite() {
		return sprite;
	}

	public void setTint(int tint){
		this.tint = tint;
	}
	
	public String toString(){return ""+character;}
	
}
