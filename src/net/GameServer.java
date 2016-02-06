package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import entity.mob.Mob;
import game.Game;
import net.packet.Packet;
import net.packet.Packet.PacketType;
import net.packet.Packet00Login;
import net.packet.Packet01Disconnect;
import net.packet.Packet02Move;
import net.packet.Packet03Projectile;
import net.packet.Packet04AddMob;
import net.packet.Packet05MobUpdate;
import net.packet.Packet06RemoveMob;

public class GameServer extends Thread{

	private DatagramSocket socket;
	private Game game;
	private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();
	
	public GameServer(Game game){
		this.game = game;
		try {
			this.socket = new DatagramSocket(1331); //Listens on port 1331, change if changed in GameClient
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
//				String message = new String(packet.getData());
//				System.out.println("CLIENT SAYS > " +message.trim()); //["+packet.getAddress().getHostAddress()+":" +packet.getPort() +"]
//				if(message.trim().equalsIgnoreCase("Ping")){
//					sendData("Pong".getBytes(), packet.getAddress(), packet.getPort());
//				}
		}
	}
	
    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        PacketType type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;
        switch (type) {
        default:
        case INVALID:
            break;
        case LOGIN:
            packet = new Packet00Login(data);
            handleLogin((Packet00Login)packet,address,port);
            break;
        case DISCONNECT:
        	packet = new Packet01Disconnect(data);
            System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                    + ((Packet01Disconnect) packet).getUsername() + " has left...");
            this.removeConnection((Packet01Disconnect) packet);
        	break;
        case MOVE:
        	packet=new Packet02Move(data);
        	//System.out.println( ((Packet02Move)packet).getUsername()+" has moved to " +((Packet02Move)packet).getX() +"," +((Packet02Move)packet).getY() );
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
    
    private void handleRemoveMob(Packet06RemoveMob packet) {
		packet.writeData(this);
	}
    
    private void handleMobUpdate(Packet05MobUpdate packet){
    	packet.writeData(this);
    }
    
    private void handleAddMob(Packet04AddMob packet){
    	packet.writeData(this);
    }
    
    private void handleProjectile(Packet03Projectile packet){
    	//Sends info to all clients (including the server's player)
    	packet.writeData(this);
    }
    
    private void handleLogin(Packet00Login packet, InetAddress address, int port){
    	System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                + packet.getUsername() + " has connected...");
        PlayerMP player = new PlayerMP(game.level, packet.getX()/Game.TILE_SIZE, packet.getY()/Game.TILE_SIZE, packet.getUsername(),packet.getColor(), address, port);
        this.addConnection(player, packet);
	}
    
    private void handleMove(Packet02Move packet) {
		if(getPlayerMP(packet.getUsername())!=null){
			int index = getPlayerMPIndex(packet.getUsername());
			this.connectedPlayers.get(index).x = packet.getX(); 
			this.connectedPlayers.get(index).y = packet.getY();
			packet.writeData(this);
		}
	}

	public void removeConnection(Packet01Disconnect packet) {
    	//PlayerMP player = getPlayerMP(packet.getUsername());
    	this.connectedPlayers.remove(getPlayerMPIndex(packet.getUsername()));
    	packet.writeData(this);
	}
    
    public PlayerMP getPlayerMP(String username){    	
    	for(PlayerMP player : this.connectedPlayers){
    		if(player.getUsername().equals(username))
    			return player;
    	}
    	return null;
    }

    public int getPlayerMPIndex(String username){    	
    	int index=0;
    	for(PlayerMP player : this.connectedPlayers){
    		if(player.getUsername().equals(username))
    			break;
    		index++;
    	}
    	return index;
    }
    
	public void addConnection(PlayerMP player, Packet00Login packet) {
        boolean alreadyConnected = false;
        for (PlayerMP p : this.connectedPlayers) {
            if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
                if (p.ipAddress == null) {
                    p.ipAddress = player.ipAddress;
                }
                if (p.port == -1) {
                    p.port = player.port;
                }
                alreadyConnected = true;
            } else {
                // relay to the current connected player that there is a new player
                sendData(packet.getData(), p.ipAddress, p.port);

                // relay to the new player that the currently connect player exists
                packet = new Packet00Login(p.getUsername(),p.x,p.y,p.color);
                sendData(packet.getData(), player.ipAddress, player.port);
                
                Packet04AddMob mobPacket;
                for(Mob mob : game.level.mobs){
                	mobPacket = new Packet04AddMob(mob.x/Game.TILE_SIZE,mob.y/Game.TILE_SIZE,mob.Health,mob.characters,mob.spells,mob.identifier);
                	sendData(mobPacket.getData(),player.ipAddress,player.port);
                }
                
            }
        }
        if (!alreadyConnected) {
            this.connectedPlayers.add(player);
        }
    }

	public void sendData(byte[] data, InetAddress ipAddress, int port){
		DatagramPacket packet = new DatagramPacket(data,data.length, ipAddress, port);
		try {
			this.socket.send(packet);
		} catch (IOException e) {e.printStackTrace();}
	}

	public void sendDataToAllClients(byte[] data) {
		for(PlayerMP p : connectedPlayers){
			sendData(data, p.ipAddress, p.port);
		}
	}
	
}
