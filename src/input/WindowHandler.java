package input;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import game.Game;
import net.packet.Packet01Disconnect;

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
		Packet01Disconnect packet = new Packet01Disconnect(this.game.player.getUsername());
		packet.writeData(this.game.socketClient);
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
