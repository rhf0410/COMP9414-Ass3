package com.example.map;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.ass.Position;

public class ObstaclePosition {
	private static volatile HashMap<Character, ArrayList<Position>> obstacle_position;
	
	private ObstaclePosition(){
		
	}
	
	/**
	 * Obstacle's positions.
	 * @return
	 */
	public static synchronized HashMap<Character, ArrayList<Position>>getInstance(){
		if(obstacle_position == null){
			synchronized(ToolsPosition.class){
				if(obstacle_position == null){
					obstacle_position = new HashMap<Character, ArrayList<Position>>();
				}
			}
		}
		return obstacle_position;
	}
}
