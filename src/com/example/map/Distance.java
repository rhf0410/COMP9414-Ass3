package com.example.map;

import com.example.ass.Position;

public class Distance {
	/**
	 * Measure distance with Manhattan distance.
	 * @param pos1
	 * @param pos2
	 * @return
	 */
	public static int distance(Position pos1, Position pos2){
		return Math.abs(pos1.x - pos2.x) + Math.abs(pos1.y - pos2.y);
	}
}
