package com.example.ass;

public class Position {
	public int x;
	public int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.x * this.y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Position)){
			return false;
		}
		Position pos = (Position)obj;
		if(this.x == pos.x && this.y == pos.y){
			return true;
		}
		return false;
	}
}
