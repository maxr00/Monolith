package net.packet;

import net.GameClient;
import net.GameServer;

public class Packet17LoadLevel extends Packet {
	
	private int width, height;
	private String world, solids;
	
	//Recieved a packet
	public Packet17LoadLevel(byte[] data) {
		super(17);
		String[] dataArray = readData(data).split(",");
		try{
			this.width=Integer.parseInt(dataArray[0]);
			this.height=Integer.parseInt(dataArray[1]);
			this.solids = dataArray[2];
			this.world = dataArray[3];
			for(int i=4;i<dataArray.length;i++)//Incase world contains any ,s
				this.world += ","+dataArray[i];
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("LOAD LEVEL PACKET OUT OF BOUNDS");
		}
	}
	
	//Creating a packet
	public Packet17LoadLevel(int width, int height, String world, String solids){//, int[] colors){
		super(17);
		this.width=width;
		this.height=height;
		this.world=world;
		this.solids=solids;
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
		return ("17" + width+","+height+","+solids+","+world).getBytes();
	}

	public String getWorld() {
		return world;
	}
	
	public String getSolids() {
		return solids;
	}
	
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
}