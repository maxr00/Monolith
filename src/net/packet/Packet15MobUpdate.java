package net.packet;

import net.GameClient;
import net.GameServer;

public class Packet15MobUpdate extends Packet {
	
	private int x,y, health;
	private String identifier;
	
	//Recieved a packet
	public Packet15MobUpdate(byte[] data) {
		super(15);
		String[] dataArray = readData(data).split(",");
		try{
			this.x=Integer.parseInt(dataArray[0]);
			this.y=Integer.parseInt(dataArray[1]);
			this.health=Integer.parseInt(dataArray[2]);
			this.identifier=dataArray[3];
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("PACKET OUT OF BOUNDS");
		}
	}
	
	//Creating a packet
	public Packet15MobUpdate(int x, int y, int health, String identifier){
		super(15);
		this.x = x;
		this.y = y;
		this.health=health;
		this.identifier=identifier;
	}
	
	//Send to server from client
	public void writeData(GameClient client){
		client.sendData(getData());
	}
	
	//Send to all clients from server
	public void writeData(GameServer server){
		server.sendDataToAllClients(getData());
	}	
	
	public byte[] getData(){
		return ("15"+x+","+y+","+health+","+identifier).getBytes();
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getHealth(){
		return health;
	}
	public String getID(){
		return identifier;
	}

}
