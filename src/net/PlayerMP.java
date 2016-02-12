package net;

import java.awt.Color;
import java.net.InetAddress;

import entity.Particle;
import game.Game;
import graphics.Screen;
import graphics.UI;
import input.Keyboard;
import input.MouseHandler;
import level.Level;
import player.Player;

public class PlayerMP extends Player{//Change to Mob

	public InetAddress ipAddress;
	public int port;
	
	public PlayerMP(Keyboard in, MouseHandler mouse, Screen screen, Level lvl, int spawnX, int spawnY, String username, int color,InetAddress ipAddress, int port) {
		super(in,mouse,screen, lvl, username, spawnX, spawnY,color);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	//Local Connection
	public PlayerMP(Level lvl, int spawnX, int spawnY, String username,int color, InetAddress ipAddress, int port) {
		super(null,null,null, lvl, username, spawnX, spawnY,color);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public void update(){
		if(this.input!=null) //Update local player
			super.update();
	}
	
}
