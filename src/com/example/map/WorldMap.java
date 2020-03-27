package com.example.map;

import java.util.HashMap;
import java.util.Map;

import com.example.ass.Block;
import com.example.ass.Position;

public class WorldMap {
	private static volatile Map<Position, Block> world_map;
	
	private WorldMap(){
		
	}
	
	/**
	 * Get world map.
	 * @return
	 */
	public static synchronized Map<Position, Block> getInstance(){
		if(world_map == null){
			synchronized(WorldMap.class){
				if(world_map == null){
					world_map = new HashMap<Position, Block>();
				}
			}
		}
		return world_map;
	}
}
