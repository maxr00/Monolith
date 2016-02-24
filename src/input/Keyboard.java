package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

	private boolean[] keys = new boolean[600];
	public boolean up, down, left, right, refresh, zoomIn, zoomOut, pause, select, back, levelUp;
	public boolean onUp, onDown, onLeft, onRight, onRefresh, onZoomIn, onZoomOut, onPause,onSelect,onBack, onLevelUp; //True on update it is pressed

	public boolean[] runeKey = new boolean[9], runeKeyOn = new boolean[9], runeKeyOff = new boolean[9];
	public boolean castSpell, onCastSpell, offCastSpell, clearSpell;
	
	public boolean onToggleLock, toggleLock;
	
	private boolean u,d,l,r,rf,cs,tl,zi,zo,p,s,b,lu;
	private boolean[] rk = new boolean[9];
	
	public void update() {
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		refresh = keys[KeyEvent.VK_SPACE];

		pause = keys[KeyEvent.VK_ESCAPE] || keys[KeyEvent.VK_P];
		select = keys[KeyEvent.VK_ENTER];
		back = keys[KeyEvent.VK_BACK_SPACE];
		
		zoomIn = keys[KeyEvent.VK_EQUALS] || keys[KeyEvent.VK_ADD];
		zoomOut = keys[KeyEvent.VK_MINUS] || keys[KeyEvent.VK_SUBTRACT];
		
		levelUp = keys[KeyEvent.VK_L];
		
		if (up && !u) onUp = true;
		else onUp = false;
		if (down && !d) onDown = true;
		else onDown = false;
		if (left && !l) onLeft = true;
		else onLeft = false;
		if (right && !r) onRight = true;
		else onRight = false;
		if (refresh && !rf) onRefresh = true;
		else onRefresh = false;
		if(zoomIn && !zi) onZoomIn=true;
		else onZoomIn=false;
		if(zoomOut && !zo) onZoomOut=true;
		else onZoomOut=false;
		if(pause && !p) onPause=true;
		else onPause=false;
		if(select && !s) onSelect=true;
		else onSelect=false;
		if(back && !b) onBack=true;
		else onBack=false;
		if(levelUp && !lu) onLevelUp=true;
		else onLevelUp=false;

		runeKey[0] = keys[KeyEvent.VK_NUMPAD7];
		runeKey[1] = keys[KeyEvent.VK_NUMPAD8];
		runeKey[2] = keys[KeyEvent.VK_NUMPAD9];
		runeKey[3] = keys[KeyEvent.VK_NUMPAD4];
		runeKey[4] = keys[KeyEvent.VK_NUMPAD5];
		runeKey[5] = keys[KeyEvent.VK_NUMPAD6];
		runeKey[6] = keys[KeyEvent.VK_NUMPAD1];
		runeKey[7] = keys[KeyEvent.VK_NUMPAD2];
		runeKey[8] = keys[KeyEvent.VK_NUMPAD3];
		
		for(int i=0;i<runeKey.length;i++){
			if(rk[i] && !runeKey[i])
				runeKeyOff[i]=true;
			else runeKeyOff[i]=false;
			
			if(!rk[i] && runeKey[i])
				runeKeyOn[i]=true;
			else runeKeyOn[i]=false;
		}
		
		castSpell = keys[KeyEvent.VK_NUMPAD0];
		clearSpell = keys[KeyEvent.VK_DECIMAL];
		
		if (!castSpell && cs) offCastSpell = true;
		else offCastSpell = false;
		if (castSpell && !cs) onCastSpell = true;
		else onCastSpell = false;
		
		toggleLock=keys[KeyEvent.VK_E];
		
		if(toggleLock && !tl)
			onToggleLock=true;
		else
			onToggleLock=false;
		
		u=up; d=down; l=left; r=right; rf=refresh; cs=castSpell;
		for(int i=0;i<runeKey.length;i++){
			rk[i]=runeKey[i];
		}
		tl=toggleLock;zi=zoomIn;zo=zoomOut;
		p=pause; s=select; b=back;
		lu=levelUp;
	}

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) {

	}

}
