package com.example.ass;

public class Block {
	public Position pos;
	public char type;
	public boolean isOpen;
	public boolean isGoal;
	public boolean isFrontier;
	public float cost;
	
	public Block(Position pos, char type) {
		this.pos = pos;
		this.type = type;
		this.isOpen = false;
		this.isGoal = false;
		this.isFrontier = true;
		this.cost = 0.0f;
	}
}
