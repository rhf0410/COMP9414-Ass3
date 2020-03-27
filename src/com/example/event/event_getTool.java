package com.example.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.example.Astar.Astar;
import com.example.ass.Block;
import com.example.ass.Event;
import com.example.ass.EventName;
import com.example.ass.Position;
import com.example.ass.Variable;
import com.example.map.Events;
import com.example.map.SpotPosition;
import com.example.map.ToolsPosition;
import com.example.map.ToolsSet;
import com.example.map.WorldMap;
import com.example.order.OrderGenerating;

public class event_getTool extends event {
	private char tool;
	private Set<Character>tools;
	private HashMap<Character, ArrayList<Position>> tools_position;
	private HashMap<Character, ArrayList<Position>> spot_position;
	private Map<Position, Block>world_map;
	private Queue<Event>events;
	private OrderGenerating order;
	
	public event_getTool(char tool){
		this.tool = tool;
		this.tools = ToolsSet.getInstance();
		this.tools_position = ToolsPosition.getInstance();
		this.spot_position = SpotPosition.getInstance();
		this.world_map = WorldMap.getInstance();
		this.events = Events.getInstance();
		this.order = new OrderGenerating();
	}
	
	@Override
	public boolean operation() {
		// TODO Auto-generated method stub
		if(this.tool == ' '){
			return false;
		}
		List<Position>path = null;
		int num_bomb = 0;
		Iterator<Character>tool_ite = this.tools.iterator();
		while(tool_ite.hasNext()){
			if(tool_ite.next() == 'd'){
				num_bomb++;
			}
		}
		
		System.out.println("Go to tool " + tool);
		List<Position>tool_positions = this.tools_position.get(tool);
		List<Position>spot_positions = this.spot_position.get(tool);
		for(int i=0;i<tool_positions.size();i++){
			Position tool_position = tool_positions.get(i);
			Position sspot_position = spot_positions.get(i);
			
			this.world_map.get(tool_position).isOpen = true;
			Astar astar = new Astar(sspot_position, this.world_map, Variable.current_pos);
			path = astar.go_to_block(sspot_position);
			if(path != null){
				for(Position step : path){
					if(this.world_map.get(step).type == '~'){
						this.events.add(new Event(1, EventName.Switch_Explore, tool_position));
						return false;
					}
				}
				this.order = new OrderGenerating(path, "");
				this.order.order_generate();
				astar = new Astar(sspot_position, this.world_map, Variable.current_pos);
				astar.setThrObs(true);
				astar.setObsNum(num_bomb);
				List<Position>path_obs = astar.go_to_block(tool_position);
				if(path_obs != null){
					this.order = new OrderGenerating(path_obs, "boom");
					this.order.order_generate();
					return true;
				}else{
					return false;
				}
			}else{
				astar = new Astar(sspot_position, this.world_map, Variable.current_pos);
				astar.setThrObs(true);
				astar.setObsNum(num_bomb);
				List<Position>path_obs = astar.go_to_block(tool_position);
				if(path_obs != null){
					this.order = new OrderGenerating(path_obs, "boom");
					this.order.order_generate();
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}
}
