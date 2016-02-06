package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import entity.Projectile;
import entity.mob.Mob;
import game.Game;
import net.packet.Packet;
import net.packet.Packet00Login;
import net.packet.Packet01Disconnect;
import net.packet.Packet02Move;
import net.packet.Packet03Projectile;
import net.packet.Packet04AddMob;
import net.packet.Packet05MobUpdate;
import net.packet.Packet06RemoveMob;

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
			byte[] data = new byte[1024]; //Increase size if needed?
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
			 packet = new Packet00Login(data);
			 handleLogin((Packet00Login)packet, address, port);
			 break;
		 case DISCONNECT:
			 packet = new Packet01Disconnect(data);
			 System.out.println("["+address.getHostAddress()+":"+port+"] "+((Packet01Disconnect) packet).getUsername()+" has left the world...");
			 game.level.removePlayerMP(((Packet01Disconnect)packet).getUsername());
			 break;
		 case MOVE:
			 packet=new Packet02Move(data);
			 this.handleMove((Packet02Move)packet);
			 break;
		 case PROJECTILE:
			 packet = new Packet03Projectile(data);
			 handleProjectile((Packet03Projectile)packet);
			 break;
		 case ADDMOB:
			 packet = new Packet04AddMob(data);
			 handleAddMob((Packet04AddMob)packet);
			 break;
		 case MOBUPDATE:
			 packet = new Packet05MobUpdate(data);
			 handleMobUpdate((Packet05MobUpdate)packet);
			 break;
		 case REMOVEMOB:
			 packet = new Packet06RemoveMob(data);
			 handleRemoveMob((Packet06RemoveMob)packet);
			 break;
		 }
	 }
	 
	 private void handleRemoveMob(Packet06RemoveMob packet){
		 for(Mob mob : game.level.mobs){
			 if(mob.identifier.equals(packet.getID())){
				 mob.remove();
				 break;
			 }
		 }
	 }
	 
	 private void handleMobUpdate(Packet05MobUpdate packet){
		 for(Mob mob : game.level.mobs){
			 if(mob.identifier.equals(packet.getID())){
				 mob.moveTo(packet.getX(), packet.getY());
				 mob.Health=packet.getHealth();
				 break;
			 }
		 }
	 }
	 
	 private void handleAddMob(Packet04AddMob packet){
		 System.out.println("GAMECLIENT.JAVA: Mob: " +packet.getCharacters()[0][0]);
		 new MobMP(game.level,packet.getX(),packet.getY(),packet.getCharacters(),packet.getHealth(),packet.getID());
	 }
	 
	 private void handleProjectile(Packet03Projectile packet){
		 new Projectile(packet.getX(),packet.getY(),packet.getxDir(),packet.getyDir(),Projectile.Spell.getSpell(packet.getSpell()),packet.getDamagePercent(),game.level.getMob(packet.getMobID()),game.level);
	 }
	 
	private void handleLogin(Packet00Login packet, InetAddress address, int port){
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername() + " has joined the game...");
		
        PlayerMP player = new PlayerMP(game.level, packet.getX()/Game.TILE_SIZE, packet.getY()/Game.TILE_SIZE, packet.getUsername(),packet.getColor(), address, port);
        game.level.addPlayer(player);
	}
	
	private void handleMove(Packet02Move packet) {
		game.level.movePlayer(packet.getUsername(), packet.getX(), packet.getY());
	}
	
}
