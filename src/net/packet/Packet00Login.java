package net.packet;

import net.GameClient;
import net.GameServer;

public class Packet00Login extends Packet {
	
	private String username;
	private int x,y,color;
	
	//Recieved a packet
	public Packet00Login(byte[] data) {
		super(00);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x=Integer.parseInt(dataArray[1]);
		this.y=Integer.parseInt(dataArray[2]);
		this.color=Integer.parseInt(dataArray[3]);
	}
	
	//Creating a packet
	public Packet00Login(String username, int x, int y, int color){
		super(00);
		this.username = username;
		this.x = x;
		this.y = y;
		this.color=color;
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
		return ("00" + this.username+","+x+","+y+","+color).getBytes();
	}

	public String getUsername() {
		return username;
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getColor(){
		return color;
	}
}
