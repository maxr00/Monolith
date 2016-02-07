package net.packet;

import net.GameClient;
import net.GameServer;

public class Packet03Projectile extends Packet {
	
	private String spell, mobID;
	private int x,y;
	private float xDir, yDir;
	private float damagePercent;
	
	//Recieved a packet
	public Packet03Projectile(byte[] data) {
		super(03);
		String[] dataArray = readData(data).split(",");
		this.x=Integer.parseInt(dataArray[0]);
		this.y=Integer.parseInt(dataArray[1]);
		this.xDir=Float.parseFloat(dataArray[2]);
		this.yDir=Float.parseFloat(dataArray[3]);
		this.spell=dataArray[4];
		this.damagePercent=Float.parseFloat(dataArray[5]);
		this.mobID=dataArray[6];
	}
	
	//Creating a packet
	public Packet03Projectile(int x,int y, float xDir, float yDir, String spell, float damagePercent, String mobID){
		super(03);
		this.x = x;
		this.y = y;
		this.xDir=xDir;
		this.yDir=yDir;
		this.spell=spell;
		this.damagePercent=damagePercent;
		this.mobID=mobID;
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
		return ("03" + x+","+y +"," +xDir +"," +yDir +"," +spell +"," +damagePercent+","+mobID).getBytes();
	}

	public int getX(){return x;}
	public int getY(){return y;}
	public float getxDir(){return xDir;}
	public float getyDir(){return yDir;}
	public String getSpell() {return spell;}
	public float getDamagePercent(){return damagePercent;}
	public String getMobID(){return mobID;}

}