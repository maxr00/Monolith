package input;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import game.Game;
import net.packet.Packet11Disconnect;

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
		if(Game.game.socketServer==null){
			Packet11Disconnect packet = new Packet11Disconnect(this.game.player.getUsername());
			packet.writeData(this.game.socketClient);
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
