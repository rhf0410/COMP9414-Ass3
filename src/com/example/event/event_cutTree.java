package com.example.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.Astar.Astar;
import com.example.Astar.Astar_position;
import com.example.ass.Block;
import com.example.ass.Position;
import com.example.ass.Variable;
import com.example.map.Distance;
import com.example.map.ObstaclePosition;
import com.example.map.ToolsSet;
import com.example.map.WorldMap;
import com.example.order.OrderGenerating;

/**
 * Class for cutting trees.
 * Cut a tree if needed.
 * @author lenovo
 *
 */
public class event_cutTree extends event {
	private HashMap<Character, ArrayList<Position>> obstacle_position;
	private Set<Character>tools;
	private Map<Position, Block>world_map;
	private OrderGenerating order;
	
	public event_cutTree(){
		this.obstacle_position = ObstaclePosition.getInstance();
		this.tools = ToolsSet.getInstance();
		this.world_map = WorldMap.getInstance();
		this.order = new OrderGenerating();
	}
	
	@Override
	public boolean operation() {
		// TODO Auto-generated method stub
		List<Position>path = new ArrayList<Position>();
		List<Astar_position>tree_list = new ArrayList<Astar_position>();
		for(Position tree_pos : this.obstacle_position.get('T')){
			tree_list.add(new Astar_position(tree_pos, Distance.distance(tree_pos, Variable.current_pos)));
		}
		
		//Sort process.
		Collections.sort(tree_list, new Comparator<Astar_position>() {

			@Override
			public int compare(Astar_position o1, Astar_position o2) {
				if(o1.distance > o2.distance){
					return 1;
				}else{
					return -1;
				}
			}
		});
		
		for(Astar_position tree_pos_d : tree_list){
			Astar astar = new Astar(tree_pos_d.pos, this.world_map, Variable.current_pos);
			path = astar.go_to_block(tree_pos_d.pos);
			if(path != null){
				break;
			}
		}
		
		if(path != null){
			if(!Variable.inSea){
				for(Position step:path){
					if(this.world_map.get(step).type == '~' && this.tools.contains('w')){
						this.tools.remove('w');
					}
				}
			}
			this.order = new OrderGenerating(path, "cut");
			this.order.order_generate();
			return true;
		}else{
			return false;
		}
	}

}
