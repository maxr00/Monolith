package net;

import java.awt.Color;

import entity.Particle;
import entity.Particle_Exp;
import entity.mob.Mob;
import game.Game;
import graphics.Screen;
import graphics.Sprite;
import level.Level;
import net.packet.Packet16RemoveMob;

public class MobMP extends Mob{
	
	private int[][][] colorBlemishes;
	private boolean hasBlemishes = false;
	
	public MobMP(Level lvl, int spawnX, int spawnY, char[][] characters,int col, int health, String name, String id, String[] statuses) { //Random Personality
		level = lvl;
		level.addMob(this);
		Health = health;
		identifier=id;
		color = col;
		this.statuses=statuses;
		this.name=name;
		
		this.characters=characters;
		sprites = new Sprite[characters.length][characters[0].length];
		takenPos = new boolean[characters.length][characters[0].length];
		colorBlemishes = new int[characters.length][characters[0].length][];

		for (int x = 0; x < sprites.length; x++) {
			for (int y = 0; y < sprites[x].length; y++) {
				sprites[x][y] = Sprite.getSprite(characters[x][y]);
				if (sprites[x][y] != null) {
					takenPos[x][y] = true;
				}
			}
		}

		if (hasBlemishes) {
			int pixel = 0;
			for (int x = 0; x < colorBlemishes.length; x++) {
				for (int y = 0; y < colorBlemishes[x].length; y++) {
					pixel = 0;
					while (pixel < Game.TILE_SIZE * Game.TILE_SIZE) {
						pixel += random.nextInt(Game.TILE_SIZE * Game.TILE_SIZE);
						int c = random.nextInt(20) + 10;
						if (pixel < colorBlemishes.length) colorBlemishes[x][y][pixel] = new Color(c, c, c).getRGB();
					}
				}
			}
		}
		move(spawnX * Game.TILE_SIZE, spawnY * Game.TILE_SIZE);
	}
	
	int particlesPerDamage=400;
	public void setHealth(int h){
		if(h<=0){
			Packet16RemoveMob packet=new Packet16RemoveMob(identifier);
			packet.writeData(Game.game.socketClient);
			new Particle(x + Game.TILE_SIZE/2,y + Game.TILE_SIZE/2,1,-600,0.3f,particlesPerDamage*(h-Health)*2,level,new Color[]{Color.red,new Color(150,0,0)},Particle.RenderType.Additive,150);
			new Particle(x + Game.TILE_SIZE/2,y + Game.TILE_SIZE/2,2,-1200,0.1f,5*((takenPos.length+takenPos[0].length)/2),level,new Color[]{Color.lightGray},Particle.RenderType.Sprite);
			
			new Particle_Exp(x + Game.TILE_SIZE/2,y + Game.TILE_SIZE/2,1,1200,0.1f,10,level,new Color[]{Color.yellow},1);
		}
		Health=h;
	}
	
	public void render(Screen screen) {
		if(render){
			if(lockedOnto){
				screen.renderBackground(this.x-1, this.y-1, sprites.length*Game.TILE_SIZE+2, sprites[0].length*Game.TILE_SIZE+2, new Color(201,175,40).getRGB());
				screen.renderBackground(this.x, this.y, sprites.length*Game.TILE_SIZE, sprites[0].length*Game.TILE_SIZE, Screen.defaultBackground);
			}
			for (int x = 0; x < sprites.length; x++) {
				for (int y = 0; y < sprites[x].length; y++) {
					if (sprites[x][y] != null) {
						
						if(reflected){
							screen.renderSpriteReflected(this.x, this.y+Game.TILE_SIZE-1, sprites[x][y]);
							screen.renderLight(this.x, this.y + Game.TILE_SIZE-1, sprites[x][y].WIDTH, sprites[x][y].HEIGHT, color, Color.blue.getRGB(), colorBlemishes[x][y]);
						}
						
						screen.renderSprite(this.x + x * Game.TILE_SIZE, this.y + y * Game.TILE_SIZE, sprites[x][y]);
						//new Color(112,39,195)
						if (colorBlemishes != null && colorBlemishes[x][y] != null)
							screen.renderLight(this.x + x * Game.TILE_SIZE, this.y + y * Game.TILE_SIZE, sprites[x][y].WIDTH, sprites[x][y].HEIGHT, color, colorBlemishes[x][y]);
						else{
							screen.renderLight(this.x + x * Game.TILE_SIZE, this.y + y * Game.TILE_SIZE, sprites[x][y].WIDTH, sprites[x][y].HEIGHT, color, null);
						}
					}
				}
			}
		}
	}
}
