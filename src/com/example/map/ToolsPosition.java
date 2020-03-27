package com.example.map;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.ass.Position;

public class ToolsPosition {
	private static volatile HashMap<Character, ArrayList<Position>> tools_position;
	
	private ToolsPosition(){
		
	}
	
	/**
	 * List of tools' positions.
	 * @return
	 */
	public static synchronized HashMap<Character, ArrayList<Position>> getInstance(){
		if(tools_position == null){
			synchronized(ToolsPosition.class){
				if(tools_position == null){
					tools_position = new HashMap<Character, ArrayList<Position>>();
				}
			}
		}
		return tools_position;
	}
}
