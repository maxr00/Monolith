package net.packet;

import entity.Projectile.Spell;
import net.GameClient;
import net.GameServer;

public class Packet14AddMob extends Packet {
	
	private int x,y, health;
	private char[][] characters;
	private String identifier;
	private Spell[] spells;
	
	//Recieved a packet
	public Packet14AddMob(byte[] data) {
		super(14);
		String[] dataArray = readData(data).split(",");
		try{
			this.x=Integer.parseInt(dataArray[0]);
			this.y=Integer.parseInt(dataArray[1]);
			this.health=Integer.parseInt(dataArray[2]);
			this.identifier=dataArray[3];
			
			int w=Integer.parseInt(dataArray[4]);
			int h=Integer.parseInt(dataArray[5]);
			characters=new char[w][h];
			for(int y=0;y<h;y++){
				for(int x=0;x<w;x++){
					characters[x][y] = dataArray[6+y].split("|")[x].charAt(0);
				}
			}
			spells=new Spell[dataArray[7].split("|").length];
			for(int i=0;i<spells.length;i++){
				spells[i]=Spell.getSpell(dataArray[7].split("|")[i]);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("PACKET OUT OF BOUNDS");
		}
	}
	
	//Creating a packet
	public Packet14AddMob(int x, int y, int health, char[][] characters, Spell[] spells, String identifier){
		super(14);
		this.x = x;
		this.y = y;
		this.health=health;
		this.characters=characters;
		this.spells=spells;
		this.identifier=identifier;
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
		String s="", s1="";
		for(int x=0;x<characters.length;x++){
			for(int y=0;y<characters[x].length;y++){
				s+= ""+characters[x][y]+"|";
			}
			s+=",";
		}
		for(Spell spell : spells){
			s1+=spell.name()+"|";
		}
		return ("14"+x+","+y+","+health+","+identifier +"," +characters.length +"," +characters[0].length +","+s+","+s1).getBytes();
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getHealth(){
		return health;
	}
	public char[][] getCharacters(){
		return characters;
	}
	public String getID(){
		return identifier;
	}
	public Spell[] getSpells(){
		return spells;
	}

}
