package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import entity.Projectile;
import entity.mob.Mob;
import game.Game;
import level.Level;
import level.Tile;
import net.packet.Packet;
import net.packet.Packet.PacketType;
import net.packet.Packet10Login;
import net.packet.Packet11Disconnect;
import net.packet.Packet12Move;
import net.packet.Packet13Projectile;
import net.packet.Packet14AddMob;
import net.packet.Packet15MobUpdate;
import net.packet.Packet16RemoveMob;
import net.packet.Packet17LoadLevel;
import net.packet.Packet19RequestLevel;
import net.packet.Packet21PlayerPause;
import player.Spell;

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
			byte[] data = new byte[8192*10]; //Original size:1024   //Increase size if needed?
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
            packet = new Packet10Login(data);
            handleLogin((Packet10Login)packet,address,port);
            break;
        case DISCONNECT:
        	packet = new Packet11Disconnect(data);
            System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                    + ((Packet11Disconnect) packet).getUsername() + " has left...");
            this.removeConnection((Packet11Disconnect) packet);
        	break;
        case MOVE:
        	packet=new Packet12Move(data);
        	//System.out.println( ((Packet02Move)packet).getUsername()+" has moved to " +((Packet02Move)packet).getX() +"," +((Packet02Move)packet).getY() );
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
        case REQUESTLEVEL:
        	if(Game.game.level!=null && Game.game.level.colors!=null){
        		packet = new Packet19RequestLevel(data);
        		sendData(((Packet19RequestLevel)packet).getData(), address, port);
        	}
			break;
        case PAUSE:
        	packet = new Packet21PlayerPause(data);
        	handlePause((Packet21PlayerPause)packet);
        	break;
        }
    }
    
    
    private void handlePause(Packet21PlayerPause packet) {
		packet.writeData(this);
		((PlayerMP)game.level.getMob(packet.getID())).paused=packet.getPaused();
	}
    
    private void handleLoadLevel(Packet17LoadLevel packet) {
		packet.writeData(this);
	}

	private void handleRemoveMob(Packet16RemoveMob packet) {
		packet.writeData(this);
	}
    
    private void handleMobUpdate(Packet15MobUpdate packet){
    	packet.writeData(this);
    }
    
    private void handleAddMob(Packet14AddMob packet){
    	packet.writeData(this);
    }
    
    private void handleProjectile(Packet13Projectile packet){
    	new Projectile(packet.getX(),packet.getY(),packet.getxDir(),packet.getyDir(),Spell.getSpell(packet.getSpell()),packet.getDamagePercent(),game.level.getMob(packet.getMobID()),game.level,packet.getSourceID());
    	packet.writeData(this);
    }
    
    private void handleLogin(Packet10Login packet, InetAddress address, int port){
    	System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername() + " has connected...");

    	Level lvl=Game.game.level;
    	int[] tiles = new int[lvl.width*lvl.height];
    	for(int i=0;i<lvl.tiles.length;i++){
    		tiles[i]=lvl.tiles[i].type;
    	}
    	
    	Packet17LoadLevel levelPacket=new Packet17LoadLevel(lvl.width,lvl.height,lvl.seed);//cols
    	sendData(levelPacket.getData(),address,port);
    	
    	//Send existing mobs to connecting player
    	Packet14AddMob mobPacket;
    	for(Mob mob : game.level.mobs){
    		mobPacket = new Packet14AddMob(mob.x/Game.TILE_SIZE,mob.y/Game.TILE_SIZE,mob.Health,mob.color,mob.name,mob.characters,mob.spells,mob.identifier, mob.statuses);
    		sendData(mobPacket.getData(),address,port);
    	}
        
    	//Create player to handle on server side
        PlayerMP player = new PlayerMP(game.level, packet.getX()/Game.TILE_SIZE, packet.getY()/Game.TILE_SIZE, packet.getUsername(),packet.getColor(), address, port);
        this.addConnection(player, packet);
	}
    
    private void handleMove(Packet12Move packet) {
		if(getPlayerMP(packet.getUsername())!=null){
			int index = getPlayerMPIndex(packet.getUsername());
			//this.connectedPlayers.get(index).x = packet.getX(); 
			//aaathis.connectedPlayers.get(index).y = packet.getY();
			this.connectedPlayers.get(index).moveTo(packet.getX(), packet.getY());
			packet.writeData(this);
		}
	}

	public void removeConnection(Packet11Disconnect packet) {
    	//PlayerMP player = getPlayerMP(packet.getUsername());
		try{
			this.connectedPlayers.remove(getPlayerMPIndex(packet.getUsername()));
			game.level.removePlayerMP(packet.getUsername());
			packet.writeData(this);
		}catch(Exception e){
			System.out.println("PROBLEM WITH PLAYER " +packet.getUsername() +" DISCONNECTING FROM SERVER");
		}
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
    
	public void addConnection(PlayerMP player, Packet10Login packet) {
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
                //Send new player to all current players
                sendData(packet.getData(), p.ipAddress, p.port);

                // Send new player all players already connected
                packet = new Packet10Login(p.getUsername(),p.x,p.y,p.color);
                sendData(packet.getData(), player.ipAddress, player.port);
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
	
	/*Request Level:
	
	Packet17LoadLevel levelPacket = new Packet17LoadLevel(Game.game.level.width, Game.game.level.height, Game.game.level.world, Game.game.level.solids);
	sendData(levelPacket.getData(), address, port);
	
	int[] cols=new int[Game.game.level.colors.length];
	for(int i=0;i<Game.game.level.colors.length;i++){
		cols[i]=Game.game.level.colors[i].getRGB();
	}
	Packet18LevelColors colorPacket = new Packet18LevelColors(cols);
	sendData(colorPacket.getData(), address, port);
	*/
	
}
