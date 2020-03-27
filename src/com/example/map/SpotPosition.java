package com.example.map;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.ass.Position;

public class SpotPosition {
	private static volatile HashMap<Character, ArrayList<Position>> spot_position;
	
	public SpotPosition(){
		
	}
	
	/**
	 * Spot positions.
	 * @return
	 */
	public static synchronized HashMap<Character, ArrayList<Position>>getInstance(){
		if(spot_position == null){
			synchronized(SpotPosition.class){
				if(spot_position == null){
					spot_position = new HashMap<Character, ArrayList<Position>>();
				}
			}
		}
		return spot_position;
	}
}
