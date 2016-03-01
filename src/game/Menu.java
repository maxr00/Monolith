package game;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import game.Menu.Option;
import graphics.Screen;
import graphics.Sprite;
import input.Keyboard;
import player.Spell;
import player.Spell.Rune;

public class Menu {
	
	//Make sure first line is the longest
	public final static Menu PAUSE=new Menu(new Line[]{
			new Line("--------------------",Color.white), 
			new Line("       PAUSE        ",Color.white),
			new Line("--------------------",Color.white),
			new Line("                    ",Color.white),
			new Line("                    ",Color.white),
			new Line("       RESUME       ",Color.white,Color.yellow,Option.Resume),
			new Line("                    ",Color.white),
			new Line("                    ",Color.white),
			new Line("     SPELL BOOK     ",Color.white,Color.yellow,Option.Open_Book),
			new Line("                    ",Color.white),
			new Line("                    ",Color.white),
			new Line("        EXIT        ",Color.white,Color.orange,Option.Exit_To_Menu),
			new Line("                    ",Color.white),
			new Line("                    ",Color.white),
			new Line("--------------------",Color.white)
			},
			null
	);
	
	public final static Menu START_MENU=new Menu(new Line[]{
			new Line("-=----=----=-----=----=----=-",Color.white),
			new Line("                             ",Color.white),
			new Line("          Monolith           ",Color.white),
			new Line("                             ",Color.white),
			new Line("-=----=----=--@--=----=----=-",Color.white),
			new Line("                             ",Color.white),
			new Line("                             ",Color.white),
			new Line("        START SERVER         ",Color.white,Color.yellow,Option.Start_Server),
			new Line("                             ",Color.white),
			new Line("                             ",Color.white),
			new Line("         JOIN SERVER         ",Color.white,Color.yellow,Option.Customize_Player),
			new Line("                             ",Color.white),
			new Line("                             ",Color.white),
			new Line("            EXIT             ",Color.white,Color.yellow,Option.Close_Application),
			},
			null
	);
	
	public final static Menu CUSTOMIZE_PLAYER=new Menu(new Line[]{
			new Line("         CUSTOMIZE PLAYER         ",Color.white),
			new Line("----------------------------------",Color.white),
			new Line("                                  ",Color.white),
			new Line("NAME:                             ",Color.white,Color.cyan,Option.Enter_Name_Below),
			new Line("Player                            ",Color.white),
			new Line("                                  ",Color.white),
			new Line("COLOR:                            ",Color.white,Color.cyan,Option.Enter_Color_Below),
			new Line("255,255,0                         ",Color.white),
			new Line("< ############################## >",Color.yellow),
			new Line("                                  ",Color.white),
			new Line("                 @                ",Color.yellow),
			new Line("                                  ",Color.white),
			new Line("               ENTER              ",Color.white,Color.cyan,Option.Join_Server),
			new Line("                                  ",Color.white),
			new Line("               BACK               ",Color.white,Color.cyan,Option.Back),
			},
			START_MENU
	);
	
	public final static Menu JOIN_SERVER=new Menu(new Line[]{
			new Line("         JOIN SERVER         ",Color.white),
			new Line("-----------------------------",Color.white),
			new Line("                             ",Color.white),
			new Line("                             ",Color.white),
			new Line("      JOIN LOCAL SERVER      ",Color.white,Color.yellow,Option.Join_Local_Server),
			new Line("                             ",Color.white),
			new Line("                             ",Color.white),
			new Line("     JOIN EXTERNAL SERVER    ",Color.white,Color.yellow,Option.Join_External_Server),
			new Line("                             ",Color.white),
			new Line("                             ",Color.white),
			new Line("                             ",Color.white),
			new Line("            BACK             ",Color.white,Color.yellow,Option.Back),
			},
			CUSTOMIZE_PLAYER
	);
	
	public final static Menu LEVEL_UP_SPELL=new Menu(new Line[]{
			new Line("----------------------------------------------",Color.white),
			new Line("                L E V E L  U P                ",Color.yellow),
			new Line("----------------------------------------------",Color.white),
			new Line("                                              ",Color.white),
			new Line("upgrade1                                      ",Color.white,Color.green,Option.Level_Up),
			new Line("upgrade2                                      ",Color.white,Color.green,Option.Level_Up),
			new Line("upgrade3                                      ",Color.white,Color.green,Option.Level_Up),
			new Line("upgrade4                                      ",Color.white,Color.green,Option.Level_Up),
			new Line("upgrade5                                      ",Color.white,Color.green,Option.Level_Up),
			new Line("upgrade6                                      ",Color.white,Color.green,Option.Level_Up),
			new Line("upgrade7                                      ",Color.white,Color.green,Option.Level_Up),
			new Line("upgrade8                                      ",Color.white,Color.green,Option.Level_Up),
			new Line("upgrade9                                      ",Color.white,Color.green,Option.Level_Up),
			new Line("upgrade10                                     ",Color.white,Color.green,Option.Level_Up),
			new Line("upgrade11                                     ",Color.white,Color.green,Option.Level_Up),
			new Line("upgrade12                                     ",Color.white,Color.green,Option.Level_Up),
			new Line("upgrade13                                     ",Color.white,Color.green,Option.Level_Up),
			new Line("upgrade14                                     ",Color.white,Color.green,Option.Level_Up),
			new Line("upgrade15                                     ",Color.white,Color.green,Option.Level_Up),
			new Line("                                              ",Color.white),
			new Line("                 C A N C E L                  ",Color.white,Color.green,Option.Cancel_Level_Up),
			new Line("----------------------------------------------",Color.white),
			},
			null
	);
	
	private static Rune newRune;
	public final static Menu LEVEL_UP_RUNE=new Menu(new Line[]{
			new Line("----------------------------------------------",Color.white),
			new Line("      N E W  R U N E S  A V A I L A B L E     ",Color.yellow),
			new Line("----------------------------------------------",Color.white),
			new Line("              CHOOSE A  NEW RUNE              ",Color.yellow),
			new Line("----------------------------------------------",Color.white),
			new Line("                                              ",Color.white),
			new Line("upgrade1                                      ",Color.white,Color.green,Option.Level_Up_New_Rune),
			new Line("upgrade2                                      ",Color.white,Color.green,Option.Level_Up_New_Rune),
			new Line("upgrade3                                      ",Color.white,Color.green,Option.Level_Up_New_Rune),
			new Line("upgrade4                                      ",Color.white,Color.green,Option.Level_Up_New_Rune),
			new Line("upgrade5                                      ",Color.white,Color.green,Option.Level_Up_New_Rune),
			new Line("upgrade6                                      ",Color.white,Color.green,Option.Level_Up_New_Rune),
			new Line("upgrade7                                      ",Color.white,Color.green,Option.Level_Up_New_Rune),
			new Line("upgrade8                                      ",Color.white,Color.green,Option.Level_Up_New_Rune),
			new Line("upgrade9                                      ",Color.white,Color.green,Option.Level_Up_New_Rune),
			new Line("                                              ",Color.white),
			new Line("      KEEP CURRENT RUNES  (SPENDS LEVEL)      ",Color.white,Color.green,Option.Level_Up_Keep_Runes),
			new Line("                                              ",Color.white),
			new Line("                  C A N C E L                 ",Color.white,Color.green,Option.Cancel_Level_Up),
			new Line("----------------------------------------------",Color.white),
			},
			null
	);
	
	public final static Menu LEVEL_UP_REPLACE_RUNE=new Menu(new Line[]{
			new Line("----------------------------------------------",Color.white),
			new Line("      N E W  R U N E S  A V A I L A B L E     ",Color.yellow),
			new Line("----------------------------------------------",Color.white),
			new Line("            CHOOSE RUNE TO REPLACE            ",Color.yellow),
			new Line("----------------------------------------------",Color.white),
			new Line("                                              ",Color.white),
			new Line("upgrade1                                      ",Color.white,Color.green,Option.Level_Up_Replace_Rune),
			new Line("upgrade2                                      ",Color.white,Color.green,Option.Level_Up_Replace_Rune),
			new Line("upgrade3                                      ",Color.white,Color.green,Option.Level_Up_Replace_Rune),
			new Line("upgrade4                                      ",Color.white,Color.green,Option.Level_Up_Replace_Rune),
			new Line("upgrade5                                      ",Color.white,Color.green,Option.Level_Up_Replace_Rune),
			new Line("upgrade6                                      ",Color.white,Color.green,Option.Level_Up_Replace_Rune),
			new Line("upgrade7                                      ",Color.white,Color.green,Option.Level_Up_Replace_Rune),
			new Line("upgrade8                                      ",Color.white,Color.green,Option.Level_Up_Replace_Rune),
			new Line("upgrade9                                      ",Color.white,Color.green,Option.Level_Up_Replace_Rune),
			new Line("                                              ",Color.white),
			new Line("                  C A N C E L                 ",Color.white,Color.green,Option.Cancel_Level_Up),
			new Line("----------------------------------------------",Color.white),
			},
			LEVEL_UP_RUNE
	);
	
	private int bookPage=0;
	public final static Menu SPELL_BOOK=new Menu(new Line[]{
			new Line("----------------------------------------------",Color.white),
			new Line("                  SPELL BOOK                  ",Color.cyan),
			new Line("----------------------------------------------",Color.white),
			new Line("                                              ",Color.white),
			new Line("SPELL NAME                                    ",Color.white),
			new Line("                                              ",Color.white),
			new Line("TYPE: THE SPELL TYPE                          ",Color.white),
			new Line("                                              ",Color.white),
			new Line("DAMAGE: X                                     ",Color.white),
			new Line("                                              ",Color.white),
			new Line("SPEED: X                                      ",Color.white),
			new Line("                                              ",Color.white),
			new Line("                                              ",Color.white),
			new Line("RUNES:                                        ",Color.white),
			new Line("                                              ",Color.white),
			new Line("rune1-3                                       ",Color.white),
			new Line("                                              ",Color.white),
			new Line("rune4-6                                       ",Color.white),
			new Line("                                              ",Color.white),
			new Line("rune7-9                                       ",Color.white),
			new Line("                                              ",Color.white),
			new Line("                                              ",Color.white),
			new Line("                PREVIOUS PAGE                 ",Color.white,Color.yellow,Option.Previous_Page),
			new Line("                                              ",Color.white),
			new Line("                  NEXT PAGE                   ",Color.white,Color.yellow,Option.Next_Page),
			new Line("                                              ",Color.white),
			new Line("                    CANCEL                    ",Color.white,Color.yellow,Option.Leave_Book),
			new Line("---------------------------------------------/",Color.white),
			},
			PAUSE
	);
	
	
	
	//Menu for joining a local or external server
	// if choose external, for now pull up j-option-panel and get ip from that
	
	public boolean listening;
	private int listeningLine;
	
	public enum Option{
		NONE, 
		//Start Menu
		Start_Server,
		Customize_Player,
		Join_Server,
		Close_Application,
		Back, 
		
		Join_Local_Server,
		Join_External_Server,
		//Pause Menu
		Resume,
		Open_Book,
		Exit_To_Menu,
		//Level Up
		Level_Up,
		Cancel_Level_Up,
		Level_Up_New_Rune,
		Level_Up_Replace_Rune,
		Level_Up_Keep_Runes,
		//Spell Book
		Next_Page,
		Previous_Page,
		Leave_Book,
		//Customize Player
		Enter_Name_Below,
		Enter_Char_Below,
		Enter_Color_Below,
		
	}
		
	public void select(){
		switch(selectedOptions[selected]){
		default:break;
		case Back:
			if(!listening)
				Game.game.startMenu=back();
			break;
		case Start_Server:
			Game.game.startServer();
			break;
		case Customize_Player:
			active=false;
			Game.game.startMenu=CUSTOMIZE_PLAYER.load();
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
		case Open_Book:
			Game.game.player.menu=SPELL_BOOK.load();
			Spell.setSpellBookPage(Spell.spells.get(bookPage));
			break;
		case Exit_To_Menu:
			active=false;
			Game.game.exitToMenu();
			break;
		case Level_Up:
			Game.game.player.unPause();
			Game.game.player.leveledUp();
			Spell.unlockSpell(selected-4);
			break;
		case Cancel_Level_Up:
			Game.game.player.unPause();
			break;
		case Level_Up_New_Rune:
			newRune = Rune.getAvailableRunes().get(selected-6);
			Game.game.player.swapRuneMenu(newRune.preReq);//Rune.getAvailableRunes().get(selected-6).preReq);
			break;
		case Level_Up_Replace_Rune:
			Game.game.player.unPause();
			Game.game.player.leveledUp();
			Rune.replaceRune(Rune.getRunesForSwap(newRune.preReq).get(selected-6),newRune);//Rune.getAvailableRunes().get(selected-6)
			break;
		case Level_Up_Keep_Runes:
			Game.game.player.unPause();
			Game.game.player.leveledUp();
			break;
		case Previous_Page:
			bookPage--;
			if(bookPage<0) bookPage=Spell.spells.size()-1;
			boolean good=true;
			for(Rune r : Spell.spells.get(bookPage).runesNeeded)
				if(!r.active) good=false;
			
			while(!Spell.spells.get(bookPage).unlocked || !good){
				bookPage--; 
				if(bookPage<0) 
					bookPage=Spell.spells.size()-1;
				good=true;
				for(Rune r : Spell.spells.get(bookPage).runesNeeded)
					if(!r.active) good=false;
			}
			Spell.setSpellBookPage(Spell.spells.get(bookPage));
			break;
		case Next_Page:
			bookPage++;
			if(bookPage>=Spell.spells.size())bookPage=0;
			good=true;
			for(Rune r : Spell.spells.get(bookPage).runesNeeded)
				if(!r.active) good=false;
			while(!Spell.spells.get(bookPage).unlocked || !good){
				bookPage++; 
				if(bookPage>=Spell.spells.size())
					bookPage=0;
				good=true;
				for(Rune r : Spell.spells.get(bookPage).runesNeeded)
					if(!r.active) good=false;
			}
			Spell.setSpellBookPage(Spell.spells.get(bookPage));
			break;
		case Leave_Book:
			Game.game.player.menu=back();
			break;
			
		case Enter_Name_Below:
			listening=!listening;
			listeningForChar=false;
			if(listening){
				listeningLine=selected+1;
				enteredText="";
				defaultText=lines[selected+1];
				setLine(enteredText,selected+1);
				setLine("NAME:       PRESS ENTER TO CONFIRM",selected);
			}else{
				if(enteredText.trim().equals(""))
					setLine(defaultText,selected+1);
				else
					setLine(enteredText.trim(),selected+1);
				Game.game.username=enteredText.trim();
				setLine("NAME:                             ",selected);
			}
			break;
		case Enter_Char_Below:
			listening=!listening;
			listeningForChar=true;
			if(listening){
				listeningLine=selected+1;
				enteredText="";
				defaultText=lines[selected+1];
				setLine(""+enteredText,selected+1);
				setLine("CHARACTER:  PRESS ENTER TO CONFIRM",selected);
			}else{
				setLine("CHARACTER:                        ",selected);
				if(enteredText.trim().equals("")){
					setLine(""+defaultText,selected+1);
					Game.game.playerCharacter=defaultText.charAt(0);
				}else{
					setLine(""+enteredText,selected+1);
					Game.game.playerCharacter=enteredText.charAt(0);
				}
			}
			break;
		case Enter_Color_Below:
			listening=!listening;
			listeningForChar=false;
			if(listening){
				listeningLine=selected+1;
				enteredText="".trim();
				defaultText=lines[selected+1];
				setLine(enteredText,selected+1);
				setLine("COLOR:      SEPARATE RGB BY COMMAS",selected);
				colors[selected+2]=Color.white;
			}else{
				
				if(enteredText.trim().equals(""))
					setLine(defaultText,selected+1);
				else
					setLine(enteredText,selected+1);
				setLine("COLOR:",selected);
				
				enteredText=enteredText.trim();
				
				Color c;
				try{
					if(enteredText.trim().equalsIgnoreCase("random")){
						c=new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));
						enteredText = c.getRed() +"," +c.getGreen() +"," +c.getBlue();
						setLine(enteredText,selected+1);
					}else 
						c = new Color(Integer.parseInt(enteredText.split(",")[0].trim()),Integer.parseInt(enteredText.split(",")[1].trim()),Integer.parseInt(enteredText.split(",")[2].trim()));
				}catch(Exception e){
					c=Color.white;
					enteredText = "255,255,255";
					setLine(enteredText,selected+1);
				}
				colors[selected+2]=c;
				colors[selected+4]=c;
				Game.game.playerCol=c;
			}
			break;
		}
	}
	
	private void hover(){
		switch(hoverOptions[selected]){
		default:break;
		
		}
	}
	
	private static boolean usingMouse;
	private static int lastY=-1;
	public static void update(){
		for(Menu m : Menus){
			if(!m.active)
				continue;
			
			m.enterText();
			
			if(!m.listening){
				if(Game.game.mouse.inScreen){
					int y=(int)( ((Game.game.mouse.y/(float)Game.scale) /Game.TILE_SIZE) -(Game.game.screen.height/2f-m.sprites[0].length*Game.TILE_SIZE/2f)/Game.TILE_SIZE);
					
					if(lastY!=Game.game.mouse.y)
						usingMouse=true;
					
					if(usingMouse && y<m.selectedOptions.length && y>=0 && m.selectedOptions[y]!=null){
						m.selected=y;
						if(Game.game.mouse.onPress){
							m.select();
							Game.game.mouse.onPress=false;
						}
					}
					lastY = Game.game.mouse.y;
				}
			}
		}
		
	}
	
	public String enteredText="", defaultText;
	public boolean listeningForChar;
	public void enterText(){
		if(listening){
			if(Keyboard.input.typedKey!=null && !Keyboard.input.got){
				Keyboard.input.got=true;
				enteredText+=Keyboard.input.typedKey.getKeyChar();
				if(Keyboard.input.keys[KeyEvent.VK_BACK_SPACE] && enteredText.length()>1)
					enteredText=enteredText.substring(0, enteredText.length()-2);
			}
			if(listeningForChar){
				enteredText=""+enteredText.charAt(enteredText.length()-1);
			}
			setLine(enteredText,listeningLine);
		}
	}
	
	private String[] lines;
	private Option[] hoverOptions, selectedOptions;
	private Color[] colors, selectedColors;
	private Sprite[][] sprites;
	private int selected;
	private Menu previousMenu;
	
	public boolean active=false;
	public static ArrayList<Menu> Menus;
	
	private Menu(Line[] lines, Menu previous){
	//private Menu(String[] lines, Option[] options, Color color, Color selectColor, Menu previousMenu){
		this.lines=new String[lines.length];
		this.hoverOptions=new Option[lines.length];
		this.selectedOptions=new Option[lines.length];
		this.colors=new Color[lines.length];
		this.selectedColors=new Color[lines.length];
		this.previousMenu = previous;
		
		for(int i=0;i<lines.length;i++){
			this.lines[i]=lines[i].line;
			selectedOptions[i]=lines[i].selected;
			hoverOptions[i]=lines[i].hover;
			colors[i]=lines[i].color;
			selectedColors[i]=lines[i].selectedColor;
		}
		
		char[][] ui = new char[this.lines[0].length()][lines.length];
		for (int x = 0; x < ui.length; x++) {
			for (int y = 0; y < ui[x].length; y++) {
					ui[x][y] = this.lines[y].charAt(x);
			}
		}
		
		sprites=new Sprite[this.lines[0].length()][lines.length];
		for (int x = 0; x < sprites.length; x++) {
			for (int y = 0; y < sprites[x].length; y++) {
				if(!Character.isWhitespace(ui[x][y])){
					sprites[x][y] = Sprite.getSprite(ui[x][y]);
				}
			}
		}
		
		for(int i=0;i<lines.length;i++)
			if(selectedOptions[i]!=null && this.lines[i].trim()!=""){
				selected=i;
				break;
			}
		
		if(Menus==null)
			Menus=new ArrayList<Menu>();
		Menus.add(this);
	}
	
	public Menu load(){
		listening=false;
		enteredText="";
		
		active=true;
		for(int i=0;i<lines.length;i++)
			if(selectedOptions[i]!=null && this.lines[i].trim()!=""){
				selected=i;
				break;
			}
		return this;
	}
	
	public void selectNext(){
		if(listening) return;
		usingMouse=false;
		
		for(int i=selected+1;i<lines.length;i++)
			if(selectedOptions[i]!=null && lines[i].trim()!=""){
				selected=i;
				return;
			}
		//Select first if there is no next option
		for(int i=0;i<lines.length;i++)
			if(selectedOptions[i]!=null && lines[i].trim()!=""){
				selected=i;
				return;
			}
	}
	
	public void selectPrevious(){
		if(listening) return;
		usingMouse=false;
		
		for(int i=selected-1;i>0;i--)
			if(selectedOptions[i]!=null && lines[i].trim()!=""){
				selected=i;
				return;
			}
		//Select last if there is no previous option
		for(int i=lines.length-1;i>=0;i--)
			if(selectedOptions[i]!=null && lines[i].trim()!=""){
				selected=i;
				return;
			}
	}
	
	
	public Menu back(){
		if(listening) return null;
		
		if(previousMenu!=null){
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
					screen.renderUI(x*Game.TILE_SIZE+screen.width/2-sprites.length*Game.TILE_SIZE/2, y*Game.TILE_SIZE+screen.height/2-sprites[x].length*Game.TILE_SIZE/2, sprites[x][y], colors[y].getRGB(),Screen.defaultBackground);
				else
					screen.renderUI(x*Game.TILE_SIZE+screen.width/2-sprites.length*Game.TILE_SIZE/2, y*Game.TILE_SIZE+screen.height/2-sprites[x].length*Game.TILE_SIZE/2, sprites[x][y], selectedColors[y].getRGB(),Screen.defaultBackground);
			}
		}
	}

	public void setLines(String[] l, int startLine) {
		for(int i=0;i<l.length;i++){
			if(i+startLine<lines.length)
				lines[startLine+i]=l[i];
		}
		
		char[][] ui = new char[lines[0].length()][lines.length];
		for (int x = 0; x < ui.length; x++) {
			for (int y = 0; y < ui[x].length; y++) {
				if(y<lines.length && lines[y]!=null && x<lines[y].length())
					ui[x][y] = lines[y].charAt(x);
				else
					ui[x][y] = ' ';
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
	}
	
	public void setLine(String l,int line){
		setLines(new String[]{l},line);
	}
	
	public void setOptions(String[] l, int startLine) {
		for(int i=0;i<l.length;i++){
			if(i+startLine<lines.length)
				lines[startLine+i]=l[i];
		}
	}

	
	
}

class Line{
	public String line;
	public Color color;
	public Color selectedColor;
	public Option selected;
	public Option hover;
	
	//Lines that can be selected
	public Line(String l,Color c,Color sc,Option s){
		this(l,c,sc,s,null);
	}
	public Line(String l,Color c,Color sc,Option s,Option h){
		line=l;
		color=c;
		selectedColor=sc;
		selected=s;
		hover=h;
	}
	//Lines that cannot be selected
	public Line(String l,Color c){
		line=l;
		color=c;
		selectedColor=null;
		selected=null;
		hover=null;
	}
}
