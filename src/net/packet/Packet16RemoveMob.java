package net.packet;

import net.GameClient;
import net.GameServer;

public class Packet16RemoveMob extends Packet {
	
	private String identifier;
	
	//Recieved a packet
	public Packet16RemoveMob(byte[] data) {
		super(16);
		String[] dataArray = readData(data).split(",");
		try{
			this.identifier=dataArray[0];
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("REMOVE MOB PACKET OUT OF BOUNDS");
		}
	}
	
	//Creating a packet
	public Packet16RemoveMob(String identifier){
		super(16);
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
		return ("16"+identifier).getBytes();
	}
	
	public String getID(){
		return identifier;
	}

}
