package com.example.order;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.ass.Block;
import com.example.ass.MakeTurn;
import com.example.ass.Position;
import com.example.ass.Variable;
import com.example.map.Action;
import com.example.map.ObstaclePosition;
import com.example.map.ToolsPosition;
import com.example.map.ToolsSet;
import com.example.map.WorldMap;

/**
 * Class for generating order.
 * @author lenovo
 *
 */
public class OrderGenerating {
	private Set<Character>tools;
	private HashMap<Character, ArrayList<Position>> tools_position;
	private HashMap<Character, ArrayList<Position>> obstacle_position;
	private Deque<Character>action;
	private Map<Position, Block>world_map;
	private List<Position>path;
	private String event;
	
	/**
	 * Constructing function.
	 */
	public OrderGenerating(){
		
	}

	public OrderGenerating(List<Position>path, String event){
		this.tools = ToolsSet.getInstance();
		this.tools_position = ToolsPosition.getInstance();
		this.obstacle_position = ObstaclePosition.getInstance();
		this.action = Action.getInstance();
		this.world_map = WorldMap.getInstance();
		this.path = path;
		this.event = event;
	}
	
	/**
	 * Key function to generate order to game server.
	 */
	public void order_generate(){
		if(path == null || path.size() == 0){
			return;
		}
		String order_string = new String("");
		for(Position pos:path){
			System.out.print(" (" + pos.x + "," + pos.y + ") ");
		}
		System.out.println();
		
		for(Position pos:path){
			//Check the path is end with a tool.
			char type = this.world_map.get(pos).type;
			if(type == 'k' || type == 'd' || type == 'a' || type == '$'){
				char this_tool = type;
				this.tools.add(this_tool);
				//Delete the tool from map and tools dict.
				this.tools_position.get(this_tool).remove(pos);
				this.world_map.get(pos).type = ' ';
			}
			Position dif_pos = new Position(pos.x - Variable.current_pos.x, pos.y - Variable.current_pos.y);
			String [] turn = MakeTurn.make_turn(dif_pos);
			order_string += turn[0];
			
			Position last_pos = path.get(path.size() - 1);
			if(pos.x == last_pos.x && pos.y == last_pos.y){
				if(event.equals("unlock")){
					order_string += "u";
					this.world_map.get(pos).type = ' ';
					this.world_map.get(pos).isOpen = true;
					this.obstacle_position.get('-').remove(pos);
				}else if(event.equals("cut")){
					order_string += "c";
					this.tools.add('w');
					this.world_map.get(pos).type = ' ';
					this.world_map.get(pos).isOpen = true;
					this.obstacle_position.get('T').remove(pos);
				}
			}
			char ch = this.world_map.get(pos).type;
			if(ch == '*' || ch == 'T'){
				if(event.equals("boom")){
					order_string += "b";
					this.obstacle_position.get(ch).remove(pos);
					this.world_map.get(pos).type = ' ';
					this.world_map.get(pos).isOpen = true;
					this.tools.remove('d');
					order_string += "f";
					Variable.rotation = turn[1];
					Variable.current_pos = pos;
					break;
				}
			}
			order_string += "f";
			Variable.rotation = turn[1];
			Variable.current_pos = pos;
			this.world_map.get(pos).isFrontier = false;
		}
		char [] orders = order_string.toCharArray();
		for(char order:orders){
			this.action.add(order);
		}
	}
	
	public List<Position> getPath() {
		return path;
	}

	public void setPath(List<Position> path) {
		this.path = path;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
}
