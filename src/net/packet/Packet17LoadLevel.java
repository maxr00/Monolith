package net.packet;

import net.GameClient;
import net.GameServer;

public class Packet17LoadLevel extends Packet {
	
	private int width, height;
	private int[] tiles;
	
	//Recieved a packet
	public Packet17LoadLevel(byte[] data) {
		super(17);
		System.out.println("HEY");
		String[] dataArray = readData(data).split(",");
		this.width=Integer.parseInt(dataArray[0]);
		this.height=Integer.parseInt(dataArray[1]);
		tiles = new int[width*height];
		String[] t = dataArray[2].split("/");
		for(int i=0;i<t.length;i++){//Incase world contains any ,s
			this.tiles[i] = Integer.parseInt(t[i]);
		}
		try{
		}catch(Exception e){
			System.out.println("LOAD LEVEL PACKET OUT OF BOUNDS");
		}
	}
	
	//Creating a packet
	public Packet17LoadLevel(int width, int height, int[] tiles){//, int[] colors){
		super(17);
		this.width=width;
		this.height=height;
		this.tiles=tiles;
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
		String s="";
		for(int t : tiles){	s+=t+"/"; }
		return ("17" + width+","+height+","+s).getBytes();
	}
	
	public int[] getTiles(){
		return tiles;
	}
	
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
}