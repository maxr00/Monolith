package net.packet;

import net.GameClient;
import net.GameServer;

public class Packet12Move extends Packet {
	
	
	private String username;
	private int x,y;

	public Packet12Move(byte[] data) {
		super(12);
		String[] dataArray = readData(data).split(",");
		try{
			this.username = dataArray[0];
			this.x=Integer.parseInt(dataArray[1]);
			this.y=Integer.parseInt(dataArray[2]);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("MOVE PACKET OUT OF BOUNDS");
		}
	}
	
	public Packet12Move(String username,int x,int y){
		super(12);
		this.username = username;
		this.x=x;
		this.y=y;
	}
	
	public void writeData(GameClient client){
		client.sendData(getData());
	}
	
	public void writeData(GameServer server){
		server.sendDataToAllClients(getData());
	}	
	
	public byte[] getData(){
		return ("12" + this.username +"," +this.x +"," +this.y).getBytes();
	}
	
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}

	public String getUsername() {
		return username;
	}

}
