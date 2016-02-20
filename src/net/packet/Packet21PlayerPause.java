package net.packet;

import net.GameClient;
import net.GameServer;

public class Packet21PlayerPause extends Packet{
	
	private String id;
	private boolean paused;

	public Packet21PlayerPause(byte[] data) {
		super(21);
		String[] dataArray = readData(data).split(",");
		this.id = dataArray[0];
		this.paused=dataArray[1].equals("true");
	}
	
	public Packet21PlayerPause(String id,boolean paused){
		super(21);
		this.id = id;
		this.paused=paused;
	}
	
	public void writeData(GameClient client){
		client.sendData(getData());
	}
	
	public void writeData(GameServer server){
		server.sendDataToAllClients(getData());
	}	
	
	public byte[] getData(){
		return ("21" + this.id+","+this.paused).getBytes();
	}

	public String getID() {
		return id;
	}
	
	public boolean getPaused(){
		return paused;
	}

}
