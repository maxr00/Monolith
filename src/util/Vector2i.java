package util;

public class Vector2i {

	private int x, y;

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2i(Vector2i v) {
		this.x = v.x;
		this.y = v.y;
	}

	public void setX(int x) {this.x = x;}

	public void setY(int y) {this.y = y;}

	public int getX() {return x;}

	public int getY() {return y;}

	public boolean equals(Object obj) {
		if(!(obj instanceof Vector2i)) return false;
		Vector2i v = (Vector2i) obj;
		if (v.getX() == getX() && v.getY() == getY()) return true;
		return false;
	}
	
	public String toString(){
		return (x+","+y);
	}
}
