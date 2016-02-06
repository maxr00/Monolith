package net.packet;

import net.GameClient;
import net.GameServer;

public abstract class Packet {
	
	public static enum PacketType{
		INVALID(-1),
		LOGIN(00),
		DISCONNECT(01),
		MOVE(02),
		PROJECTILE(03),
		ADDMOB(04),
		MOBUPDATE(05),
		REMOVEMOB(06),
		;
		private int packetId;
		private PacketType(int packetId){
			this.packetId=packetId;
		}		
		public int getId(){ return packetId; }
	}
	
	public byte packetId;
	
	public Packet(int packetId){
		this.packetId=(byte)packetId;
	}

	public abstract void writeData(GameClient client); //Send data to server from client
	public abstract void writeData(GameServer server); //Send from server to all cients in server
	
	public String readData(byte[] data){
		String message = new String(data).trim();
		return message.substring(2);
	}
	
	public abstract byte[] getData();
	
	public static PacketType lookupPacket(String packetId){
		try{
			return lookupPacket(Integer.parseInt(packetId));
		}catch(Exception e){
			return PacketType.INVALID;
		}
	}
	
	public static PacketType lookupPacket(int id){
		for(PacketType p : PacketType.values()){
			if(p.getId()==id)
				return p;
		}
		return PacketType.INVALID;
	}
}
