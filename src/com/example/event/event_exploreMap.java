package com.example.event;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.Astar.Astar;
import com.example.ass.Block;
import com.example.ass.Position;
import com.example.ass.Variable;
import com.example.map.Frontier;
import com.example.map.ObstaclePosition;
import com.example.map.SpotPosition;
import com.example.map.ToolsPosition;
import com.example.map.WorldMap;
import com.example.order.OrderGenerating;

/**
 * Exploring with DFS and A*
 * Keep exploring if there is a block to go.
 * @author lenovo
 *
 */
public class event_exploreMap extends event {
	private char view[][];
	private Map<String, Position>rotation_corr;
	private Map<Position, Block>world_map;
	private HashMap<Character, ArrayList<Position>> tools_position;
	private HashMap<Character, ArrayList<Position>> spot_position;
	private HashMap<Character, ArrayList<Position>> obstacle_position;
	private Deque<Position>frontier;
	private OrderGenerating order;
	
	public event_exploreMap(char view[][]){
		this.view = view;
		//Used for calculating cos & sin.
		this.rotation_corr = new HashMap<String, Position>();
		this.rotation_corr.put("North", new Position(1, 0));
		this.rotation_corr.put("East", new Position(0, 1));
		this.rotation_corr.put("South", new Position(-1, 0));
		this.rotation_corr.put("West", new Position(0, -1));
		this.world_map = WorldMap.getInstance();
		this.tools_position = ToolsPosition.getInstance();
		this.spot_position = SpotPosition.getInstance();
		this.obstacle_position = ObstaclePosition.getInstance();
		this.frontier = Frontier.getInstance();
		this.order = new OrderGenerating();
	}
	
	@Override
	public boolean operation() {
		// TODO Auto-generated method stub
		this.explore(view);
		//If agent is in sea, always set all sea block free to go.
		if(Variable.inSea){
			for(Position pos: this.obstacle_position.get('~')){
				this.world_map.get(pos).isOpen = true;
			}
		}
		int [] xs = {-1,0,1,0};
		int [] ys = {0,-1,0,1};
		for(int i=0;i<xs.length;i++){
			Position frontier_pos = new Position(Variable.current_pos.x + xs[i], Variable.current_pos.y + ys[i]);
			if(this.world_map.containsKey(frontier_pos)){
				char type = this.world_map.get(frontier_pos).type;
				if(!Variable.inSea){
					if(this.world_map.get(frontier_pos).isOpen &&
					   this.world_map.get(frontier_pos).isFrontier && 
					   type != '-' && type != '~' && type != 'T'){
					   this.frontier.addFirst(frontier_pos);
					}
				}else{
					if(type == '~' && this.world_map.get(frontier_pos).isFrontier){
						this.frontier.addFirst(frontier_pos);
					}
				}
			}
		}
		//If nowhere to go, just return.
		if(this.frontier.size() == 0){
			System.out.println("Exploration finished.");
			return false;
		}
		List<Position>path = null;
		while(this.frontier.size() > 0){
			Position next_frontier = this.frontier.pollFirst();
			if(this.world_map.get(next_frontier).isFrontier){
				System.out.println("Go to frontier " + "(" + next_frontier.x + " , " + next_frontier.y + ") Type: " + this.world_map.get(next_frontier).type);
				Astar astar = new Astar(next_frontier, this.world_map, Variable.current_pos);
				astar.setThrSea(Variable.inSea);
				path = astar.go_to_block(next_frontier);
				break;
			}
		}
		if(path == null){
			System.out.println("Exploration finished.");
			return false;
		}else{
			this.order = new OrderGenerating(path, "");
			this.order.order_generate();
			return true;
		}
	}
	
	/**
	 * Key function to explore the world in every step.
	 * This function is mainly called in event_exploreMap.
	 * @param view
	 */
	private void explore(char view[][]){
		//Explore the world if possible.
		for(int j=-2;j<3;j++){
			for(int i=2;i>-3;i--){
				int local_x = j;
				int local_y = i;
				
				char sign = view[2-i][j+2] == '^' ? ' ' : view[2-i][j+2];
				//Check the edge of map.
				if(sign == '.'){
					continue;
				}
				
				Position global_pos_shift = this.rotation_correction(new Position(local_x, local_y));
				Position global_pos = new Position(global_pos_shift.x + Variable.current_pos.x, global_pos_shift.y + Variable.current_pos.y);
				
				if(!this.world_map.containsKey(global_pos)){
					if(global_pos.x > Variable.map_width_ceil){
						Variable.map_width_ceil = global_pos.x;
					}
					if(global_pos.x < Variable.map_width_floor){
						Variable.map_width_floor = global_pos.x;
					}
					if(global_pos.y > Variable.map_height_ceil){
						Variable.map_height_ceil = global_pos.y;
					}
					if(global_pos.y < Variable.map_height_floor){
						Variable.map_height_floor = global_pos.y;
					}
					
					this.world_map.put(global_pos, new Block(global_pos, sign));
					if(sign == 'a' || sign == 'k' || sign == 'd' || sign == '$'){
						if(this.tools_position.containsKey(sign)){
							this.tools_position.get(sign).add(global_pos);
						}else{
							ArrayList<Position>list = new ArrayList<Position>();
							list.add(global_pos);
							this.tools_position.put(sign, list);
						}
						if(this.spot_position.containsKey(sign)){
							this.spot_position.get(sign).add(Variable.current_pos);
						}else{
							ArrayList<Position>list = new ArrayList<Position>();
							list.add(Variable.current_pos);
							this.spot_position.put(sign, list);
						}
					}else if(sign == '-' || sign == 'T' || sign == '*' || sign == '~'){
						if(this.obstacle_position.containsKey(sign)){
							this.obstacle_position.get(sign).add(global_pos);
						}else{
							ArrayList<Position>list = new ArrayList<Position>();
							list.add(global_pos);
							this.obstacle_position.put(sign, list);
						}
					}
					//Current position.
					if(i == 0 && j == 0){
						this.world_map.get(global_pos).isOpen = true;
					}
				}
			}
		}
		//Connecting neighbors.
		this.find_neighbors(Variable.current_pos);
	}
	
	/**
	 * Correcting coordinate from view.
	 * @param pos
	 * @return
	 */
	private Position rotation_correction(Position pos){
		Position angle = this.rotation_corr.get(Variable.rotation);
		int x = pos.x * angle.x + pos.y * angle.y;
		int y = pos.y * angle.x - pos.x * angle.y;
		return new Position(x, y);
	}

	/**
	 * Connecting neighbors so that we know which block is free to go.
	 * @param pos
	 */
	private void find_neighbors(Position pos){
		if(this.world_map.get(pos).isOpen){
			int [] xs = {0,1,0,-1};
			int [] ys = {1,0,-1,0};
			//Make all its neighbour possible.
			for(int i=0;i<xs.length;i++){
				Position this_pos = new Position(pos.x + xs[i], pos.y + ys[i]);
				if(this.world_map.containsKey(this_pos)){
					if(!this.world_map.get(this_pos).isOpen){
						char ch = this.world_map.get(this_pos).type;
						if(ch != '*' && ch != '~'){
							this.world_map.get(this_pos).isOpen = true;
							if(ch != 'T' && ch != '-'){
								this.find_neighbors(this_pos);
							}
						}
					}
				}
			}
		}
	}
}
