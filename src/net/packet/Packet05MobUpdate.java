package net.packet;

import net.GameClient;
import net.GameServer;

public class Packet05MobUpdate extends Packet {
	
	private int x,y, health;
	private String identifier;
	
	//Recieved a packet
	public Packet05MobUpdate(byte[] data) {
		super(05);
		String[] dataArray = readData(data).split(",");
		this.x=Integer.parseInt(dataArray[0]);
		this.y=Integer.parseInt(dataArray[1]);
		this.health=Integer.parseInt(dataArray[2]);
		this.identifier=dataArray[3];
	}
	
	//Creating a packet
	public Packet05MobUpdate(int x, int y, int health, String identifier){
		super(05);
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
		return ("05"+x+","+y+","+health+","+identifier).getBytes();
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
