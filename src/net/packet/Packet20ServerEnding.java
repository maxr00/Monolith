package net.packet;

import net.GameClient;
import net.GameServer;

public class Packet20ServerEnding extends Packet{

	//Recieved a packet
	public Packet20ServerEnding(byte[] data) {
		super(20);
	}
	
	//Creating a packet
	public Packet20ServerEnding(){
		super(20);
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
		return ("20").getBytes();
	}
}
