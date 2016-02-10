package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import entity.Projectile;
import entity.mob.Mob;
import game.Game;
import level.Level;
import net.packet.Packet;
import net.packet.Packet10Login;
import net.packet.Packet11Disconnect;
import net.packet.Packet12Move;
import net.packet.Packet13Projectile;
import net.packet.Packet14AddMob;
import net.packet.Packet15MobUpdate;
import net.packet.Packet16RemoveMob;
import net.packet.Packet17LoadLevel;
import net.packet.Packet18LevelColors;
import net.packet.Packet19RequestLevel;

public class GameClient extends Thread{
	
	private InetAddress ipAddress;
	private DatagramSocket socket;
	private Game game;
	
	public GameClient(Game game, String ipAddress){
		this.game = game;
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public void run() {
		while(true){
			byte[] data = new byte[8192*10]; //Increase size if needed?
				DatagramPacket packet = new DatagramPacket(data, data.length);
				try {
					socket.receive(packet);
				} catch (IOException e) {e.printStackTrace();}
				
				this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
				//System.out.println("SERVER SAYS > " +new String(packet.getData()).trim() );
		}
	}
	
	public void sendData(byte[] data){
		DatagramPacket packet = new DatagramPacket(data,data.length, ipAddress, 1331);//Port 1331, pick above 1024(?)
		try {
			socket.send(packet);
		} catch (IOException e) {e.printStackTrace();}
	}
	
	 private void parsePacket(byte[] data, InetAddress address, int port) {
		 String message = new String(data).trim();
		 Packet.PacketType type = Packet.lookupPacket(message.substring(0, 2));
		 Packet packet = null;
		 switch (type) {
		 default:
		 case INVALID:
			 break;
		 case LOGIN:
			 packet = new Packet10Login(data);
			 handleLogin((Packet10Login)packet, address, port);
			 break;
		 case DISCONNECT:
			 packet = new Packet11Disconnect(data);
			 System.out.println("["+address.getHostAddress()+":"+port+"] "+((Packet11Disconnect) packet).getUsername()+" has left the world...");
			 game.level.removePlayerMP(((Packet11Disconnect)packet).getUsername());
			 break;
		 case MOVE:
			 packet=new Packet12Move(data);
			 this.handleMove((Packet12Move)packet);
			 break;
		 case PROJECTILE:
			 packet = new Packet13Projectile(data);
			 handleProjectile((Packet13Projectile)packet);
			 break;
		 case ADDMOB:
			 packet = new Packet14AddMob(data);
			 handleAddMob((Packet14AddMob)packet);
			 break;
		 case MOBUPDATE:
			 packet = new Packet15MobUpdate(data);
			 handleMobUpdate((Packet15MobUpdate)packet);
			 break;
		 case REMOVEMOB:
			 packet = new Packet16RemoveMob(data);
			 handleRemoveMob((Packet16RemoveMob)packet);
			 break;
		 case LOADLEVEL:
			 packet = new Packet17LoadLevel(data);
			 handleLoadLevel((Packet17LoadLevel)packet);
			 break;
		 case LEVELCOLORS:
			 packet = new Packet18LevelColors(data);
			 handleLoadLevelColors((Packet18LevelColors)packet);
			 break;
		 case REQUESTLEVEL:
			 System.out.println("CLIENT RECIEVED REQUEST PACKET. IMPOSSIBLE RESULT");
			 break;
		 }
	 }
	 
	 private void handleLoadLevelColors(Packet18LevelColors packet) {
		 Game.game.level.setColor(packet.getColors());
	}

	private void handleLoadLevel(Packet17LoadLevel packet) {
		Game.game.level=new Level(packet.getWidth(),packet.getHeight(),packet.getWorld(),packet.getSolids());//,packet.getColors());
		//Game.game.player.level=Game.game.level;
		Game.game.level.addPlayer((PlayerMP)Game.game.player);
	}

	private void handleRemoveMob(Packet16RemoveMob packet){
		 for(Mob mob : game.level.mobs){
			 if(mob.identifier.equals(packet.getID())){
				 mob.remove();
				 break;
			 }
		 }
	 }
	 
	 private void handleMobUpdate(Packet15MobUpdate packet){
		 for(Mob mob : game.level.mobs){
			 if(mob.identifier.equals(packet.getID())){
				 mob.moveTo(packet.getX(), packet.getY());
				 mob.Health=packet.getHealth();
				 break;
			 }
		 }
	 }
	 
	 private void handleAddMob(Packet14AddMob packet){
		 new MobMP(game.level,packet.getX(),packet.getY(),packet.getCharacters(),packet.getHealth(),packet.getID());
	 }
	 
	 private void handleProjectile(Packet13Projectile packet){
		 new Projectile(packet.getX(),packet.getY(),packet.getxDir(),packet.getyDir(),Projectile.Spell.getSpell(packet.getSpell()),packet.getDamagePercent(),game.level.getMob(packet.getMobID()),game.level);
	 }
	 
	private void handleLogin(Packet10Login packet, InetAddress address, int port){
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername() + " has joined the game...");
		
		if(game.level==null){
			System.out.println("WAIT, NO LEVEL");
			new Packet19RequestLevel().writeData(this);
			while(game.level==null){
			}
		}
		
        PlayerMP player = new PlayerMP(game.level, packet.getX()/Game.TILE_SIZE, packet.getY()/Game.TILE_SIZE, packet.getUsername(),packet.getColor(), address, port);
       	game.level.addPlayer(player);
	}
	
	private void handleMove(Packet12Move packet) {
		game.level.movePlayer(packet.getUsername(), packet.getX(), packet.getY());
	}
	
}
