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
 * Class for returning home.
 * @author lenovo
 *
 */
public class event_return extends event {
	private Set<Character>tools;
	private HashMap<Character, ArrayList<Position>> obstacle_position;
	private Map<Position, Block>world_map;
	private OrderGenerating order;
	
	public event_return(){
		this.tools = ToolsSet.getInstance();
		this.obstacle_position = ObstaclePosition.getInstance();
		this.world_map = WorldMap.getInstance();
		this.order = new OrderGenerating();
	}
	
	@Override
	public boolean operation() {
		// TODO Auto-generated method stub
		List<Position>path = new ArrayList<Position>();
		if(this.tools.contains('w') && !Variable.inSea){
			if(this.obstacle_position.containsKey('~')){
				for(Position sea_pos: this.obstacle_position.get('~')){
					this.world_map.get(sea_pos).isOpen = true;
				}
			}
		}
		if(this.tools.contains('$')){
			Position start = new Position(0, 0);
			Astar astar = new Astar(start, this.world_map, Variable.current_pos);
			path = astar.go_to_block(start);
			if(path != null){
				for(Position step_pos:path){
					if(this.obstacle_position.containsKey('~')){
						if(this.obstacle_position.get('~').contains(step_pos)){
							if(!this.tools.contains('w')){
								return false;
							}else{
								this.order = new OrderGenerating(path, "");
								this.order.order_generate();
								System.out.println("Congratulations");
								return true;
							}
						}
					}
				}
				this.order = new OrderGenerating(path, "");
				this.order.order_generate();
				System.out.println("Congratulations");
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

}
