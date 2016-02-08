package net.packet;

import net.GameClient;
import net.GameServer;

public class Packet11Disconnect extends Packet {
	
	private String username;

	public Packet11Disconnect(byte[] data) {
		super(11);
		this.username = readData(data);
	}
	
	public Packet11Disconnect(String username){
		super(11);
		this.username = username;
	}
	
	public void writeData(GameClient client){
		client.sendData(getData());
	}
	
	public void writeData(GameServer server){
		server.sendDataToAllClients(getData());
	}	
	
	public byte[] getData(){
		return ("11" + this.username).getBytes();
	}

	public String getUsername() {
		return username;
	}

}
