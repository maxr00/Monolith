package net.packet;

import net.GameClient;
import net.GameServer;

public class Packet18LevelColors extends Packet {

	private int[] colors;
	
	//Recieved a packet
	public Packet18LevelColors(byte[] data) {
		super(18);
		String[] dataArray = readData(data).split(",");
		try{
			this.colors=new int[dataArray[0].split("/").length];
			String[] c=dataArray[0].split("/");
			for(int i=0;i<colors.length;i++){
				colors[i]=Integer.parseInt(c[i]);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("LEVEL COLOR PACKET OUT OF BOUNDS");
		}
	}
	
	//Creating a packet
	public Packet18LevelColors(int[] colors){
		super(18);
		this.colors=colors;
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
		for(int i=0;i<colors.length;i++){
			s+=colors[i]+"/";
		}
		return ("18" +s).getBytes();
	}

	public int[] getColors(){
		return colors;
	}
}