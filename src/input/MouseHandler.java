package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener, MouseMotionListener{

	public boolean isPressed=false, onPress=false;
	public int button;
	public int x,y;
	private boolean clicked=false;
	
	
	//mouseClicked sometimes misses clicks if setting variable and resetting it each update
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		isPressed=true;
		button=e.getButton();
	}

	public void mouseReleased(MouseEvent e) {
		isPressed=false;
		clicked=false;
		button=-1;
	}
	
	public void update(){
		if(isPressed && !onPress && !clicked){
			onPress=true;
			clicked=true;
		}else
			onPress=false;
	}
	
	
	public void mouseDragged(MouseEvent e) {
		x=e.getX();
		y=e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		x=e.getX();
		y=e.getY();
	}

}
