package util;

public class Node {
	
	public Vector2i vector;
	public Node parent;
	public double fCost, gCost, hCost;
	
	public Node(Vector2i position, Node parent, double gCost, double hCost){
		vector = position;
		this.parent = parent;
		this.gCost = gCost;
		this.hCost = hCost;
		fCost = gCost + hCost;
	}
	
}
