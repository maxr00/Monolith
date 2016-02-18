package net.packet;

import entity.Projectile.Spell;
import net.GameClient;
import net.GameServer;


public class Packet14AddMob extends Packet {
	
	private int x,y, health;
	private char[][] characters;
	private String identifier, name;
	private Spell[] spells;
	private String[] statuses;
	private int color;
	
	//Recieved a packet
	public Packet14AddMob(byte[] data) {
		super(14);
		String[] dataArray = readData(data).split(",");
		try{
			this.x=Integer.parseInt(dataArray[0]);
			this.y=Integer.parseInt(dataArray[1]);
			this.health=Integer.parseInt(dataArray[2]);
			this.name=dataArray[3];
			this.identifier=dataArray[4];
			
			//int w=Integer.parseInt(dataArray[5]);
			//int h=Integer.parseInt(dataArray[6]);
			for(String s : dataArray[5].split("\\."))
				System.out.println(s);
			characters=new char[dataArray[5].split("\\.")[0].length()][dataArray[7].split("\\.").length];
			
			for(int y=0;y<dataArray[5].split("\\.").length;y++){
				for(int x=0;x<dataArray[5].split("\\.")[y].length();x++){
					characters[x][y] = dataArray[5].split("\\.")[y].toCharArray()[x];
				}
			}
			spells=new Spell[dataArray[6].split("/").length];
			for(int i=0;i<spells.length;i++){
				spells[i]=Spell.getSpell(dataArray[6].split("/")[i]);
			}
			
			color=Integer.parseInt(dataArray[7]);
			
			statuses=new String[dataArray[8].split("/").length];
			for(int i=0;i<statuses.length;i++){
				statuses[i]=dataArray[8].split("/")[i];
			}
			
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			System.out.println("ADD MOB PACKET OUT OF BOUNDS");
		}
	}
	
	//Creating a packet
	public Packet14AddMob(int x, int y, int health,int col,String name, char[][] characters, Spell[] spells, String identifier, String[] statuses){
		super(14);
		this.x = x;
		this.y = y;
		this.health=health;
		this.name=name;
		this.characters=characters;
		this.spells=spells;
		this.identifier=identifier;
		this.statuses=statuses;
		this.color=col;
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
		String s="", s1="", st="";
		for(int x=0;x<characters.length;x++){
			for(int y=0;y<characters[x].length;y++){
				s+= ""+characters[x][y];//;+"/";
			}
			s+=".";
		}
		for(Spell spell : spells){
			s1+=spell.name()+"/";
		}
		for(String status : statuses){
			st+=status+"/";
		}
		return ("14"+x+","+y+","+health+","+name+","+identifier +","+s+","+s1+","+color+","+st).getBytes();
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
	public String getName(){
		return name;
	}
	public String getID(){
		return identifier;
	}
	public Spell[] getSpells(){
		return spells;
	}

	public String[] getStatuses() {
		return statuses;
	}

	public int getColor(){
		return color;
	}
}
