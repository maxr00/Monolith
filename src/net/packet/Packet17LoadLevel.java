package net.packet;

import net.GameClient;
import net.GameServer;

public class Packet17LoadLevel extends Packet {
	
	private int width, height;
	private long seed;
	
	//Recieved a packet
	public Packet17LoadLevel(byte[] data) {
		super(17);
		String[] dataArray = readData(data).split(",");
		try{
			this.width=Integer.parseInt(dataArray[0]);
			this.height=Integer.parseInt(dataArray[1]);
			this.seed=Long.parseLong(dataArray[2]);
		}catch(Exception e){
			System.out.println("LOAD LEVEL PACKET ERROR");
			e.printStackTrace();
		}
	}
	
	//Creating a packet
	public Packet17LoadLevel(int width, int height, long seed){//, int[] colors){
		super(17);
		this.width=width;
		this.height=height;
		this.seed=seed;
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
		return ("17" + width+","+height+","+seed).getBytes();
	}
	
	public long getSeed(){
		return seed;
	}
	
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
}