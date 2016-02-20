package game;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import graphics.Screen;
import graphics.Sprite;

public class Menu {
	
	//Make sure first line is the longest
	public final static Menu PAUSE=new Menu(new String[]{
			"  P A U S E  ",
			"             ",
			"-------------",
			"             ",
			"             ",
			" R E S U M E ",
			"             ",
			"             ",
			"   E X I T   ",
			"             ",
			"             ",
			"-------------",
			},
			new Option[]{	null, null, null, null,	 null, Option.Resume,null, null, Option.Exit_To_Menu,null,null,null},
			Color.white, Color.yellow,null
	);
	
	public final static Menu START_MENU=new Menu(new String[]{
			"-=----=----=-----=----=----=-",
			"                             ",
			"          Monolith           ",
			"                             ",
			"-=----=----=--@--=----=----=-",
			"                             ",
			"                             ",
			"        START SERVER         ",
			"                             ",
			"                             ",
			"         JOIN SERVER         ",
			"                             ",
			"                             ",
			"            EXIT             ",
			},
			new Option[]{	
					null, null, null, null, null, null, null,
					Option.Start_Server, 
					null, null,
					Option.Join_Server,
					null,null,
					Option.Close_Application},
			Color.white, Color.yellow,null
	);
	
	public final static Menu JOIN_SERVER=new Menu(new String[]{
			"         JOIN SERVER         ",
			"-----------------------------",
			"                             ",
			"                             ",
			"      JOIN LOCAL SERVER      ",
			"                             ",
			"                             ",
			"     JOIN EXTERNAL SERVER    ",
			"                             ",
			"                             ",
			"                             ",
			"            BACK             ",
			},
			new Option[]{	
					null, null, null, null, 
					Option.Join_Local_Server,
					null, null,
					Option.Join_External_Server,
					null,null,null,
					Option.BACK},
			Color.white, Color.yellow,START_MENU
	);
	
	
	//Menu for joining a local or external server
	// if choose external, for now pull up j-option-panel and get ip from that
	
	public enum Option{
		NONE, BACK,
		//Start Menu
		Start_Server,
		Join_Server,
		Close_Application,
		
		Join_Local_Server,
		Join_External_Server,
		//Pause Menu
		Resume,
		Exit_To_Menu,
	}
	
	public void select(){
		switch(options[selected]){
		default:break;
		case BACK:
			Game.game.startMenu=back();
			break;
		case Start_Server:
			Game.game.startServer();
			break;
		case Join_Server:
			active=false;
			Game.game.startMenu=JOIN_SERVER.load();
			break;
		case Join_Local_Server:
			Game.game.startClient("localhost");
			break;
		case Join_External_Server:
			Game.game.startClient(JOptionPane.showInputDialog(Game.game.frame,"Please enter the host's IP"));
			break;
		case Close_Application:
			Game.game.closeGame();
			break;
		case Resume:
			Game.game.player.unPause();
			break;
		case Exit_To_Menu:
			Game.game.exitToMenu();
			break;
		}
	}
	
	private String[] lines;
	private Option[] options;
	private Color color, selectedColor;
	private Sprite[][] sprites;
	private int selected;
	private Menu previousMenu;
	
	public boolean active=false;
	public static ArrayList<Menu> Menus;
	
	private Menu(String[] lines, Option[] options, Color color, Color selectColor, Menu previousMenu){
		this.lines=lines;
		this.options=options;
		this.color=color;
		this.selectedColor=selectColor;
		this.previousMenu=previousMenu;
		
		char[][] ui = new char[lines[0].length()][lines.length];
		for (int x = 0; x < ui.length; x++) {
			for (int y = 0; y < ui[x].length; y++) {
					ui[x][y] = lines[y].charAt(x);
			}
		}
		
		sprites=new Sprite[lines[0].length()][lines.length];
		for (int x = 0; x < sprites.length; x++) {
			for (int y = 0; y < sprites[x].length; y++) {
				if(!Character.isWhitespace(ui[x][y])){
					sprites[x][y] = Sprite.getSprite(ui[x][y]);
				}
			}
		}
		
		for(int i=0;i<lines.length;i++)
			if(options[i]!=null){
				selected=i;
				break;
			}
		
		if(Menus==null)
			Menus=new ArrayList<Menu>();
		Menus.add(this);
	}
	
	public Menu load(){
		active=true;
		for(int i=0;i<lines.length;i++)
			if(options[i]!=null){
				selected=i;
				break;
			}
		return this;
	}
	
	public void selectNext(){
		for(int i=selected+1;i<lines.length;i++)
			if(options[i]!=null){
				selected=i;
				return;
			}
		//Select first if there is no next option
		for(int i=0;i<lines.length;i++)
			if(options[i]!=null){
				selected=i;
				return;
			}
	}
	
	public void selectPrevious(){
		for(int i=selected-1;i>0;i--)
			if(options[i]!=null){
				selected=i;
				return;
			}
		//Select last if there is no previous option
		for(int i=lines.length-1;i>=0;i--)
			if(options[i]!=null){
				selected=i;
				return;
			}
	}
	
	
	public Menu back(){
		if(back()!=null){
			active=false;
			return previousMenu.load();
		}
		return null;
	}
	
	public void render(Screen screen){
		if(sprites==null || !active)
			return;
		
		for (int x = 0; x < sprites.length; x++) {
			for (int y = 0; y < sprites[x].length; y++) {
				if(y!=selected)
					screen.renderUI(x*Game.TILE_SIZE+screen.width/2-sprites.length*Game.TILE_SIZE/2, y*Game.TILE_SIZE+screen.height/2-sprites[x].length*Game.TILE_SIZE/2, sprites[x][y], color.getRGB());
				else
					screen.renderUI(x*Game.TILE_SIZE+screen.width/2-sprites.length*Game.TILE_SIZE/2, y*Game.TILE_SIZE+screen.height/2-sprites[x].length*Game.TILE_SIZE/2, sprites[x][y], selectedColor.getRGB());
			}
		}
	}

}