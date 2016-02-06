package net.packet;

import net.GameClient;
import net.GameServer;

public class Packet01Disconnect extends Packet {
	
	private String username;

	public Packet01Disconnect(byte[] data) {
		super(01);
		this.username = readData(data);
	}
	
	public Packet01Disconnect(String username){
		super(01);
		this.username = username;
	}
	
	public void writeData(GameClient client){
		client.sendData(getData());
	}
	
	public void writeData(GameServer server){
		server.sendDataToAllClients(getData());
	}	
	
	public byte[] getData(){
		return ("01" + this.username).getBytes();
	}

	public String getUsername() {
		return username;
	}

}
