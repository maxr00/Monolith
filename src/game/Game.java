//Created on 1/1/2016
package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import graphics.Screen;
import graphics.UI;
import input.Keyboard;
import input.MouseHandler;
import input.WindowHandler;
import input.Keyboard.Key;
import level.Level;
import level.RandomLevel;
import net.GameClient;
import net.GameServer;
import net.PlayerMP;
import net.packet.Packet10Login;
import net.packet.Packet11Disconnect;
import net.packet.Packet19RequestLevel;
import player.Player;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L; //Default

	public static int scale = 2; //Pixel density
	
	public static int width = 1200/scale;
	public static int height = width / 16 * 9;

	public static int TILE_SIZE=7;
	
	private static String title = "Project Monolith vDB_8";
	
	private static final int minScale=1, maxScale=4;
	
	public static Game game;
	
	private Thread gameThread;
	public JFrame frame;
	private boolean isRunning = false;

	public Screen screen;
	public Keyboard keyboard;
	public WindowHandler windowHandler;
	public MouseHandler mouse;
	
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); //Rendered image
	private int[] renderPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData(); //Every pixel on the screen that will be rendered

	public Player player;
	public Level level;
	
	private final Random random = new Random();
	
	public GameClient socketClient;
	public GameServer socketServer;	
	
	private boolean inGame;
	public Menu startMenu;
	
	private int startScale;
	
	public Game() {
		game=this;
		//Set up window and canvas
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);

		screen = new Screen(width, height);
		frame = new JFrame();
		keyboard = new Keyboard();
		windowHandler = new WindowHandler(this);
		mouse = new MouseHandler();

		Keyboard.input=keyboard;
		
		/*if(JOptionPane.showConfirmDialog(this, "Do you want to run the server")==0){
			startServer();
		}else{
			startClient();
		}*/
		startMenu=Menu.START_MENU;
		inGame=false;
		startMenu.active=true;
		
		//set Start Menu scale
		startScale=scale;
		scale=4;
		resetZoom();
		
		addKeyListener(keyboard);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}
	int playerStartX=50;
	int playerStartY=50;
	String username="Player";
	char playerCharacter='@';
	Color playerCol=Color.yellow;
	
	public void exitToMenu(){
		player.menu=null;
		player.inMenu=false;
		inGame=false;
		scale=4;
		resetZoom();
		startMenu = Menu.START_MENU.load();

		try{
			Packet11Disconnect packet = new Packet11Disconnect(player.getUsername());
			packet.writeData(socketClient);
		}catch(Exception e){}
		
		player=null;
	}
	
	public void closeGame(){
		//Acts as if window was manually closed
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
	
	
	public void startServer(){
		inGame=true;
		
		startMenu.active=false;
		startMenu=null;
		
		scale=1;//startScale;
		resetZoom();
		
		socketServer = new GameServer(this);
		socketServer.start();
		
		level = new RandomLevel(100,100);
		((RandomLevel)level).generateLevel();
		
		socketClient = new GameClient(this, "localhost");
		socketClient.start();
	}
	
	public void startClient(String ip){
		inGame=true;
		startMenu.active=false;
		startMenu=null;
		
		scale=startScale;
		resetZoom();
		
		socketClient = new GameClient(this, ip);
		socketClient.start();
		
		//username=JOptionPane.showInputDialog(frame,"Please enter a username");
		//playerCol=new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256));
		
		sendLogin();
	}
	
	public void sendLogin(){
		Packet10Login loginPacket = new Packet10Login(username,playerStartX*TILE_SIZE,playerStartY*TILE_SIZE,playerCol.getRGB());
		loginPacket.writeData(socketClient);
	}
	
	public void joinLevel(){
		player = new PlayerMP(keyboard,mouse,screen, level,playerStartX,playerStartY,username,playerCol.getRGB(), null, -1);
		screen.snapOffsetTo(player.x - screen.width/2,player.y - screen.height/2);
	}
	
	public void leaveLevel(){
		player=null;
		level=null;
	}

	
	public synchronized void start() {
		BufferedImage fileImg = null;
		try{fileImg =ImageIO.read(this.getClass().getResource("/textures/Monolith-Icon.png"));}catch (IOException e) {e.printStackTrace();}
		game.frame.setIconImage(fileImg);
		
		isRunning = true;
		//Begin thread
		gameThread = new Thread(this, "Monolith_Game");
		gameThread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		//End thread
		try {
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() { //Called at application open, after main
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();//Used for FPS counter
		final double ns = 1000000000.0 / 60.0;//Nanosecond conversion constant divided by update lock (60)
		double delta = 0;
		int frames = 0, updates = 0; //Used for FPS and update counter

		requestFocus();
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			while (delta >= 1) {//Will only happen 60 times per second
				updates++;
				delta--;
				update();
			}
			lastTime = now;
			render(); //Render as fast as possible
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {//Every second
				timer += 1000;
				frame.setTitle(title + "  |   UPS: " + updates + ", FPS: " + frames);
				updates = 0;
				frames = 0;
			}
		}
		stop();//if program gets out of loop, it will terminate
	}

	public void update() {
		Keyboard.input.update();
		mouse.update();
		Menu.update();
		
		if(Key.zoomIn.onPress && scale<maxScale){
			scale++;
			resetZoom();
			if(player!=null)
				screen.snapOffsetTo(player.x - screen.width/2,player.y - screen.height/2);
		}
		if(Key.zoomOut.onPress && scale>minScale){
			scale--;
			resetZoom();
			if(player!=null)
				screen.snapOffsetTo(player.x - screen.width/2,player.y - screen.height/2);
		}
		
		if(inGame){
			//if(keyboard.onSelect)
			//if(!screen.isShaking)
			if(player!=null && !screen.isShaking){
				if(player.getUsername().equals("shakeit"))
					screen.setShakeEffect(5f, 3, 3, 10);
				if(player.getUsername().equals("icantakeit")){
					screen.setShakeEffect(4f, random.nextInt(10), random.nextInt(10), random.nextInt(50));
					if(Math.random()<0.5f)
						screen.activateRainbowEffect();
				}
				if(player.getUsername().equals("*o*") && !screen.isRainbow()){
					screen.activateRainbowEffect();
				}
			}
			
			if(level!=null){
				level.update();
				level.renderUpdate(xScroll, yScroll, screen);
			}else{
				if(UI.waitingForServerLevel.standByUpdate()){
					Packet19RequestLevel request=new Packet19RequestLevel();
					request.writeData(socketClient);
				}
			}
			if(player!=null)
				player.handleStatus(screen);
			screen.update();
		}else{
			//Start Menu controls
			if (Key.up.onPress)
				startMenu.selectPrevious();
			if (Key.down.onPress)
				startMenu.selectNext();
			if(Key.select.onPress)
				startMenu.select();
			if(Key.back.onPress && !startMenu.listening)
				if(startMenu.back()!=null)
					startMenu=startMenu.back();
		}
	}

	private void resetZoom(){
		width = 1200/scale;
		height = width / 16 * 9;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); //Rendered image
		renderPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		screen = new Screen(width, height);
	}
	
	int xScroll, yScroll;
	public void render() {
		//if(socketServer!=null) return;
		
		BufferStrategy bufferStrategy = getBufferStrategy(); //Get buffer strategy from canvas
		if (bufferStrategy == null) { //Called first time through
			createBufferStrategy(3);
			return;
		}

		Graphics graphics = bufferStrategy.getDrawGraphics();
		//Enter all graphics changes in here, before dispose
		graphics.setColor(Color.blue);
		graphics.fillRect(0, 0, getWidth(), getHeight());
		screen.clear();
		
		
		if(inGame){
			if(screen.getAdditive()==null)screen.newAdditive();
			if(screen.getParticles()==null){screen.newParticles();}
			screen.resetAdditive();
			screen.resetParticles();
			
			if(player!=null){
				xScroll = player.x - screen.width/2;
				yScroll = player.y - screen.height/2;
			}else{
				UI.combatUI.active=false;
				UI.healthUI.active=false;
				UI.levelReadyUI.active=false;
			}
				
			screen.setOffset(xScroll, yScroll);
			if(level!=null){
				level.render(xScroll, yScroll, screen);
				UI.waitingForServerLevel.active=false;
			}else
				UI.waitingForServerLevel.active=true;//render(screen);
			
			screen.displayAdditive();
			screen.displayParticles();
			
			for(UI ui : UI.UIElements)
				ui.render(screen);
			for(Menu m : Menu.Menus)
				m.render(screen);
			
			
		}else{
			for(Menu m : Menu.Menus)
				m.render(screen);
		}
		
		if(screen.isRainbow()) screen.renderRainbowEffect();
		
		if(player!=null){
			if(player.getUsername().equals("glitch"))
				screen.renderGlitchEffect(1,5);
			if(player.getUsername().equals("3D"))
				screen.splitRGB_UL(0,0,width, height, 0,true, 2, true, false, false);
			if(player.getUsername().equals("icantakeit")){
				screen.renderGlitchEffect(5,10);
				screen.splitRGB_UL(0,0,width, height, 0,true, 2, false, true, false);
				if(random.nextBoolean()){
					screen.splitRGB_DR(0,		0,		 width/2, height/2, 0,true,		random.nextInt(10), random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
					screen.splitRGB_DR(width/2,	height/2,width/2, height/2, 0,true, 	random.nextInt(10), random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
					screen.splitRGB_DR(width/2,	0,		 width/2, height/2, 0,true,		random.nextInt(10), random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
					screen.splitRGB_DR(0,		height/2,width/2, height/2, 0,true,		random.nextInt(10), random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
				}else{
					screen.splitRGB_UL(0,		0,		 width/2, height/2, 0,true,		random.nextInt(10), random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
					screen.splitRGB_UL(width/2,	height/2,width/2, height/2, 0,true, 	random.nextInt(10), random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
					screen.splitRGB_UL(width/2,	0,		 width/2, height/2, 0,true,		random.nextInt(10), random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
					screen.splitRGB_UL(0,		height/2,width/2, height/2, 0,true,		random.nextInt(10), random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
				}
			}
		}
		
		
		
		if(renderPixels.length==screen.pixels.length)
		for (int i = 0; i < renderPixels.length; i++) {
			if(screen.pixels[i]!=Screen.defaultBackground)
				renderPixels[i] = screen.pixels[i]; //Copy raw pixels to screen pixels
			else{
				renderPixels[i] = screen.background[i];
			}
		}
		graphics.drawImage(image, 0, 0, getWidth(), getHeight(), null); //Draw pixels
		//End change in graphics
		graphics.dispose(); //Remove past graphics
		bufferStrategy.show();
	}

	public static void main(String[] args) { //Called at start
		Game game = new Game(); //Create game
		//Setting frame settings
		game.frame.setResizable(false);
		game.frame.setTitle(title); //Title of window
		game.frame.add(game);
		game.frame.pack(); //Pack frame to fit width and height
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Terminate on application close
		game.frame.setLocationRelativeTo(null); //Creates window in center of screen
		game.frame.setVisible(true);
		
		game.start();
	}

}
