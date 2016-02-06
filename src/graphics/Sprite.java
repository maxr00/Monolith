package graphics;

import java.awt.Color;

import game.Game;

public class Sprite {

	public final int WIDTH, HEIGHT;
	private int startX, startY;
	public int[] pixels;

	private SpriteSheet sheet;

	// Start x and y are the top left pixels of the sprite
	public Sprite(SpriteSheet s, int w, int h, int startX, int startY) {
		sheet = s;
		WIDTH = w;
		HEIGHT = h;
		this.startX = startX;
		this.startY = startY;
		pixels = new int[WIDTH * HEIGHT];
		load();
	}

	public Sprite(int w, int h, Color col) {
		WIDTH = w;
		HEIGHT = h;
		pixels = new int[WIDTH * HEIGHT];
		for(int i=0;i<pixels.length;i++){
			pixels[i]=col.getRGB();
		}
	}
	
	private void load() {
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				pixels[x + y * WIDTH] = sheet.pixels[(x + startX) + (y + startY) * sheet.WIDTH];
			}
		}
	}

	//88? Sprites I thought there was 90-something
	public static Sprite a = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 0, 0);
	public static Sprite b = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 7, 0);
	public static Sprite c = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 14, 0);
	public static Sprite d = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 21, 0);
	public static Sprite e = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 28, 0);
	public static Sprite f = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 35, 0);
	public static Sprite g = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 42, 0);
	public static Sprite h = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 49, 0);
	public static Sprite i = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 56, 0);
	public static Sprite j = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 63, 0);
	public static Sprite k = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 70, 0);
	public static Sprite l = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 77, 0);
	public static Sprite m = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 84, 0);
	public static Sprite n = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 91, 0);
	public static Sprite o = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 98, 0);
	public static Sprite p = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 105, 0);
	public static Sprite q = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 112, 0);
	public static Sprite r = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 119, 0);
	public static Sprite s = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 126, 0);
	public static Sprite t = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 133, 0);
	public static Sprite u = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 140, 0);
	public static Sprite v = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 147, 0);
	public static Sprite w = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 154, 0);
	public static Sprite x = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 161, 0);
	public static Sprite y = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 168, 0);
	public static Sprite z = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 175, 0);

	public static Sprite A = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 0, Game.TILE_SIZE);
	public static Sprite B = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 7, 7);
	public static Sprite C = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 14, 7);
	public static Sprite D = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 21, 7);
	public static Sprite E = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 28, 7);
	public static Sprite F = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 35, 7);
	public static Sprite G = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 42, 7);
	public static Sprite H = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 49, 7);
	public static Sprite I = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 56, 7);
	public static Sprite J = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 63, 7);
	public static Sprite K = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 70, 7);
	public static Sprite L = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 77, 7);
	public static Sprite M = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 84, 7);
	public static Sprite N = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 91, 7);
	public static Sprite O = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 98, 7);
	public static Sprite P = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 105, 7);
	public static Sprite Q = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 112, 7);
	public static Sprite R = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 119, 7);
	public static Sprite S = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 126, 7);
	public static Sprite T = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 133, 7);
	public static Sprite U = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 140, 7);
	public static Sprite V = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 147, 7);
	public static Sprite W = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 154, 7);
	public static Sprite X = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 161, 7);
	public static Sprite Y = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 168, 7);
	public static Sprite Z = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 175, 7);

	public static Sprite _0 = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 0, 14);
	public static Sprite _1 = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 7, 14);
	public static Sprite _2 = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 14, 14);
	public static Sprite _3 = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 21, 14);
	public static Sprite _4 = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 28, 14);
	public static Sprite _5 = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 35, 14);
	public static Sprite _6 = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 42, 14);
	public static Sprite _7 = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 49, 14);
	public static Sprite _8 = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 56, 14);
	public static Sprite _9 = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 63, 14);

	public static Sprite exclamation = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 70, 14);
	public static Sprite _at = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 77, 14);
	public static Sprite pound = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 84, 14);
	public static Sprite dollar = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 91, 14);
	public static Sprite percent = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 98, 14);
	public static Sprite carrot = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 105, 14);
	public static Sprite and = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 112, 14);
	public static Sprite star = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 119, 14);
	public static Sprite openParen = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 126, 14);
	public static Sprite closeParen = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 133, 14);

	public static Sprite dash = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 0, 21);
	public static Sprite underscore = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 7, 21);
	public static Sprite equals = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 14, 21);
	public static Sprite plus = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 21, 21);
	public static Sprite openBracket = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 28, 21);
	public static Sprite closeBracket = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 35, 21);
	public static Sprite openCurl = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 42, 21);
	public static Sprite closeCurl = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 49, 21);
	public static Sprite backSlash = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 56, 21);
	public static Sprite vertLine = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 63, 21);
	public static Sprite comma = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 70, 21);
	public static Sprite period = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 77, 21);
	public static Sprite lessThan = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 84, 21);
	public static Sprite greaterThan = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 91, 21);
	public static Sprite forwardSlash = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 98, 21);
	public static Sprite question = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 105, 21);
	public static Sprite tilde = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 112, 21);
	public static Sprite colon = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 119, 21);
	public static Sprite semicolon = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 126, 21);
	public static Sprite quote = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 133, 21);
	public static Sprite singleQuote = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 140, 21);
	public static Sprite backtick = new Sprite(SpriteSheet.font, Game.TILE_SIZE, Game.TILE_SIZE, 147, 21);
	
	public static Sprite getSprite(char character){
		Sprite sprite = null;
		switch(character){
			case 'a': sprite = Sprite.a; break;
			case 'b': sprite = Sprite.b; break;
			case 'c': sprite = Sprite.c; break;
			case 'd': sprite = Sprite.d; break;
			case 'e': sprite = Sprite.e; break;
			case 'f': sprite = Sprite.f; break;
			case 'g': sprite = Sprite.g; break;
			case 'h': sprite = Sprite.h; break;
			case 'i': sprite = Sprite.i; break;
			case 'j': sprite = Sprite.j; break;
			case 'k': sprite = Sprite.k; break;
			case 'l': sprite = Sprite.l; break;
			case 'm': sprite = Sprite.m; break;
			case 'n': sprite = Sprite.n; break;
			case 'o': sprite = Sprite.o; break;
			case 'p': sprite = Sprite.p; break;
			case 'q': sprite = Sprite.q; break;
			case 'r': sprite = Sprite.r; break;
			case 's': sprite = Sprite.s; break;
			case 't': sprite = Sprite.t; break;
			case 'u': sprite = Sprite.u; break;
			case 'v': sprite = Sprite.v; break;
			case 'w': sprite = Sprite.w; break;
			case 'x': sprite = Sprite.x; break;
			case 'y': sprite = Sprite.y; break;
			case 'z': sprite = Sprite.z; break;
			
			case 'A': sprite = Sprite.A; break;
			case 'B': sprite = Sprite.B; break;
			case 'C': sprite = Sprite.C; break;
			case 'D': sprite = Sprite.D; break;
			case 'E': sprite = Sprite.E; break;
			case 'F': sprite = Sprite.F; break;
			case 'G': sprite = Sprite.G; break;
			case 'H': sprite = Sprite.H; break;
			case 'I': sprite = Sprite.I; break;
			case 'J': sprite = Sprite.J; break;
			case 'K': sprite = Sprite.K; break;
			case 'L': sprite = Sprite.L; break;
			case 'M': sprite = Sprite.M; break;
			case 'N': sprite = Sprite.N; break;
			case 'O': sprite = Sprite.O; break;
			case 'P': sprite = Sprite.P; break;
			case 'Q': sprite = Sprite.Q; break;
			case 'R': sprite = Sprite.R; break;
			case 'S': sprite = Sprite.S; break;
			case 'T': sprite = Sprite.T; break;
			case 'U': sprite = Sprite.U; break;
			case 'V': sprite = Sprite.V; break;
			case 'W': sprite = Sprite.W; break;
			case 'X': sprite = Sprite.X; break;
			case 'Y': sprite = Sprite.Y; break;
			case 'Z': sprite = Sprite.Z; break;
			
			case '0': sprite = Sprite._0; break;
			case '1': sprite = Sprite._1; break;
			case '2': sprite = Sprite._2; break;
			case '3': sprite = Sprite._3; break;
			case '4': sprite = Sprite._4; break;
			case '5': sprite = Sprite._5; break;
			case '6': sprite = Sprite._6; break;
			case '7': sprite = Sprite._7; break;
			case '8': sprite = Sprite._8; break;
			case '9': sprite = Sprite._9; break;
			
			case '!': sprite = Sprite.exclamation; break;
			case '@': sprite = Sprite._at; break;
			case '#': sprite = Sprite.pound; break;
			case '$': sprite = Sprite.dollar; break;
			case '%': sprite = Sprite.percent; break;
			case '^': sprite = Sprite.carrot; break;
			case '&': sprite = Sprite.and; break;
			case '*': sprite = Sprite.star; break;
			case '(': sprite = Sprite.openParen; break;
			case ')': sprite = Sprite.closeParen; break;
			case '-': sprite = Sprite.dash; break;
			case '_': sprite = Sprite.underscore; break;
			case '=': sprite = Sprite.equals; break;
			case '+': sprite = Sprite.plus; break;
			case '[': sprite = Sprite.openBracket; break;
			case ']': sprite = Sprite.closeBracket; break;
			case '{': sprite = Sprite.openCurl; break;
			case '}': sprite = Sprite.closeCurl; break;
			case '\\':sprite = Sprite.backSlash; break;
			case '|': sprite = Sprite.vertLine; break;
			case ',': sprite = Sprite.comma; break;
			case '.': sprite = Sprite.period; break;
			case '<': sprite = Sprite.lessThan; break;
			case '>': sprite = Sprite.greaterThan; break;
			case '/': sprite = Sprite.forwardSlash; break;
			case '?': sprite = Sprite.question; break;
			case '~': sprite = Sprite.tilde; break;
			case ':': sprite = Sprite.colon; break;
			case ';': sprite = Sprite.semicolon; break;
			case '"': sprite = Sprite.quote; break;
			case '\'': sprite = Sprite.singleQuote; break;
			case '`': sprite = Sprite.backtick; break;
		}
		return sprite;
	}
}
