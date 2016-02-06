package net.packet;

import net.GameClient;
import net.GameServer;

public class Packet06RemoveMob extends Packet {
	
	private String identifier;
	
	//Recieved a packet
	public Packet06RemoveMob(byte[] data) {
		super(06);
		String[] dataArray = readData(data).split(",");
		this.identifier=dataArray[0];
	}
	
	//Creating a packet
	public Packet06RemoveMob(String identifier){
		super(06);
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
		return ("06"+identifier).getBytes();
	}
	
	public String getID(){
		return identifier;
	}

}
