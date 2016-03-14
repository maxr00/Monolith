package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

	public boolean[] keys = new boolean[600];
	public boolean[] onKeys = new boolean[600];
	public boolean[] offKeys = new boolean[600];
	public KeyEvent typedKey = null;
	private boolean[] bk = new boolean[600];
	
	public static Key[] runes=new Key[]{Key.rune1,Key.rune2,Key.rune3,Key.rune4,Key.rune5,Key.rune6,Key.rune7,Key.rune8,Key.rune9};
		
	public static Keyboard input;
	
	public static enum Key{
		up			(new int[]{ KeyEvent.VK_UP, 	KeyEvent.VK_W}),
		down		(new int[]{	KeyEvent.VK_DOWN,	KeyEvent.VK_S}),
		left		(new int[]{ KeyEvent.VK_LEFT,	KeyEvent.VK_A}),
		right		(new int[]{ KeyEvent.VK_RIGHT,	KeyEvent.VK_D}),
		refresh		(new int[]{ KeyEvent.VK_SPACE}),
		zoomIn		(new int[]{ KeyEvent.VK_EQUALS,	KeyEvent.VK_ADD}),
		zoomOut		(new int[]{ KeyEvent.VK_MINUS,	KeyEvent.VK_SUBTRACT}),
		pause		(new int[]{ KeyEvent.VK_ESCAPE, KeyEvent.VK_P}),
		select		(new int[]{ KeyEvent.VK_ENTER}),
		back		(new int[]{ KeyEvent.VK_BACK_SPACE}),
		levelUp		(new int[]{ KeyEvent.VK_L}),
		
		rune1		(new int[]{ KeyEvent.VK_NUMPAD7}),
		rune2		(new int[]{ KeyEvent.VK_NUMPAD8}),
		rune3		(new int[]{ KeyEvent.VK_NUMPAD9}),
		rune4		(new int[]{ KeyEvent.VK_NUMPAD4}),
		rune5		(new int[]{ KeyEvent.VK_NUMPAD5}),
		rune6		(new int[]{ KeyEvent.VK_NUMPAD6}),
		rune7		(new int[]{ KeyEvent.VK_NUMPAD1}),
		rune8		(new int[]{ KeyEvent.VK_NUMPAD2}),
		rune9		(new int[]{ KeyEvent.VK_NUMPAD3}),
		
		castSpell	(new int[]{ KeyEvent.VK_NUMPAD0}),
		clearSpell	(new int[]{ KeyEvent.VK_DECIMAL}),
		
		toggleLock	(new int[]{ KeyEvent.VK_E}),
		
		fullscreen  (new int[]{ KeyEvent.VK_F11}),
		;
		public boolean pressed;
		public boolean onPress;
		public boolean offPress;
		
		private int[] keyIndexes;
		Key(int[] indexes){
			keyIndexes=indexes;
		}
		
		public void update(){
			boolean p=false,o=false,f=false;
			for(int key : keyIndexes){
				if(!p)
					p = input.keys[key];
				if(!o)
					o = input.onKeys[key];
				if(!f)
					f = input.offKeys[key];
			}
			pressed = p;
			onPress = o;
			offPress= f;
		}
	}
	
	public void update() {
		for(int i=0;i<keys.length;i++){
			if(Keyboard.input.keys[i] && !bk[i])
				onKeys[i]=true;
			else
				onKeys[i]=false;
			
			if(input.bk[i] && !keys[i])
				offKeys[i]=true;
			else
				offKeys[i]=false;
			
			bk[i]=keys[i];
		}
		
		for(Key k : Key.values())
			k.update();
		
	}

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public boolean got;
	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar()!=KeyEvent.CHAR_UNDEFINED){
			typedKey = e;
			got=false;
		}else
			typedKey=null;
	}

}
