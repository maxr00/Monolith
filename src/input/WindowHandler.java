package input;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import game.Game;
import net.packet.Packet11Disconnect;
import net.packet.Packet20ServerEnding;

public class WindowHandler implements WindowListener{

	private final Game game;
	
	public WindowHandler(Game game){
		this.game = game;
		this.game.frame.addWindowListener(this);
	}
	
	public void windowActivated(WindowEvent event) {
	}

	public void windowClosed(WindowEvent event) {
	}

	public void windowClosing(WindowEvent event) {
		if(Game.game.socketServer==null && Game.game.socketClient!=null && Game.game.player!=null){
			try{
				Packet11Disconnect packet = new Packet11Disconnect(this.game.player.getUsername());
				packet.writeData(this.game.socketClient);
			}catch(Exception e){e.printStackTrace();}
		}else if(Game.game.socketServer!=null){
			try{
				Packet20ServerEnding packet = new Packet20ServerEnding();
				packet.writeData(this.game.socketServer);
			}catch(Exception e){e.printStackTrace();}
		}
	}

	public void windowDeactivated(WindowEvent event) {
	}

	public void windowDeiconified(WindowEvent event) {
	}

	//Minimized
	public void windowIconified(WindowEvent event) {
	}

	public void windowOpened(WindowEvent event) {
	}

}
