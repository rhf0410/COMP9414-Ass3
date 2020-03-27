package com.example.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.Astar.Astar;
import com.example.ass.Block;
import com.example.ass.Position;
import com.example.ass.Variable;
import com.example.map.ObstaclePosition;
import com.example.map.ToolsSet;
import com.example.map.WorldMap;
import com.example.order.OrderGenerating;

/**
 * Class for opening lock.
 * If we have got a key, open a lock we found.
 * @author lenovo
 *
 */
public class event_openLock extends event {
	private HashMap<Character, ArrayList<Position>> obstacle_position;
	private Map<Position, Block>world_map;
	private Set<Character>tools;
	private OrderGenerating order;
	
	public event_openLock(){
		this.obstacle_position = ObstaclePosition.getInstance();
		this.world_map = WorldMap.getInstance();
		this.tools = ToolsSet.getInstance();
		this.order = new OrderGenerating();
	}
	
	@Override
	public boolean operation() {
		if(this.obstacle_position.containsKey('-')){
			List<Position>path = null;
			for(Position lock_pos: this.obstacle_position.get('-')){
				Astar astar = new Astar(lock_pos, this.world_map, Variable.current_pos);
				path = astar.go_to_block(lock_pos);
				if(path != null){
					break;
				}
			}
			if(path != null){
				if(!Variable.inSea){
					for(Position step : path){
						if(this.world_map.get(step).type == '~'){
							if(this.tools.contains('w')){
								this.tools.remove('w');
							}
						}
					}
				}
				this.order = new OrderGenerating(path, "unlock");
				this.order.order_generate();
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

}
