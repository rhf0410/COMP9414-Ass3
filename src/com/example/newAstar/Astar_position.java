package com.example.newAstar;

import com.example.ass.Position;

public class Astar_position implements Comparable<Astar_position>{
	public Position pos;
	public Astar_position parent;
	public int G;
	public int H;

	public Astar_position(int x, int y){
		this.pos = new Position(x, y);
	}
	
	public Astar_position(Position pos){
		this.pos = pos;
	}
	
	public Astar_position(Position pos, Astar_position parent, int G, int H) {
		this.pos = pos;
		this.parent = parent;
		this.G = G;
		this.H = H;
	}

	@Override
	public int compareTo(Astar_position o) {
		// TODO Auto-generated method stub
		if(o == null){
			return -1;
		}
		if(G + H > o.G + o.H){
			return 1;
		}else if(G + H < o.G + o.H){
			return -1;
		}
		return 0;
	}
}
