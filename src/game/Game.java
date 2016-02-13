//Created on 1/1/2016
package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import entity.mob.Mob;
import graphics.Screen;
import input.Keyboard;
import input.MouseHandler;
import input.WindowHandler;
import level.Level;
import level.RandomLevel;
import net.GameClient;
import net.GameServer;
import net.PlayerMP;
import net.packet.Packet10Login;
import player.Player;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L; //Default

	public static int scale = 2; //Pixel density
	
	public static int width = 1200/scale;
	public static int height = width / 16 * 9;

	public static int TILE_SIZE=7;
	
	private static final int minScale=1, maxScale=4;
	
	public static Game game;
	
	private static String title = "Project Monolith v0.05";

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

		if(JOptionPane.showConfirmDialog(this, "Do you want to run the server")==0){
			//Server Starts
			socketServer = new GameServer(this);
			socketServer.start();
			
			level = new RandomLevel(100,100);
			((RandomLevel)level).generateLevel();
			
			socketClient = new GameClient(this, "localhost");
			socketClient.start();
		}else{
			//Client joins
			socketClient = new GameClient(this, "localhost");
			socketClient.start();
			
			username=JOptionPane.showInputDialog(frame,"Please enter a username");
			playerCol=new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256));
			
			Packet10Login loginPacket = new Packet10Login(username,playerStartX*TILE_SIZE,playerStartY*TILE_SIZE,playerCol.getRGB());
			loginPacket.writeData(socketClient);
		}
		addKeyListener(keyboard);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}
	int playerStartX=50;
	int playerStartY=50;
	String username="";
	Color playerCol;
	
	public void joinLevel(){
		player = new PlayerMP(keyboard,mouse,screen, level,playerStartX,playerStartY,username,playerCol.getRGB(), null, -1);
		screen.snapOffsetTo(player.x - screen.width/2,player.y - screen.height/2);
	}

	
	public synchronized void start() {
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

	Mob lockedOn;
	public void update() {
		keyboard.update();
		mouse.update();
		if(keyboard.onRefresh){
			screen.activateRainbowEffect();
		}
		if(keyboard.onZoomIn && scale<maxScale){
			scale++;
			resetZoom();
			if(player!=null)
				screen.snapOffsetTo(player.x - screen.width/2,player.y - screen.height/2);
		}
		if(keyboard.onZoomOut && scale>minScale){
			scale--;
			resetZoom();
			if(player!=null)
				screen.snapOffsetTo(player.x - screen.width/2,player.y - screen.height/2);
		}
		
		if(level!=null)
			level.update();
		if(player!=null)
			player.handleStatus(screen);
		screen.update();
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
		
		if(player!=null){
			//xScroll=0;
			//yScroll=0;
			xScroll = player.x - screen.width/2;
			yScroll = player.y - screen.height/2;
		}
		if(level!=null){
			level.render(xScroll, yScroll, screen);
		}
		if(player!=null)
			player.render(screen);
		
		if(screen.isRainbow()) screen.renderRainbowEffect();
		
		//UI.combatUI.render(screen);
		
		if(renderPixels.length==screen.pixels.length)
		for (int i = 0; i < renderPixels.length; i++) {
			if(screen.pixels[i]!=0)
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
