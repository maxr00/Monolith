package net.packet;

import net.GameClient;
import net.GameServer;

public class Packet19RequestLevel extends Packet {
	
	//Recieved a packet
	public Packet19RequestLevel(byte[] data) {
		super(19);
	}
	
	//Creating a packet
	public Packet19RequestLevel(){
		super(19);
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
		return ("19").getBytes();
	}
}
