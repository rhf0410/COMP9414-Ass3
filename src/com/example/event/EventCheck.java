package com.example.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.example.ass.Block;
import com.example.ass.Event;
import com.example.ass.EventName;
import com.example.ass.Position;
import com.example.ass.Variable;
import com.example.map.Events;
import com.example.map.ObstaclePosition;
import com.example.map.ToolsPosition;
import com.example.map.ToolsSet;
import com.example.map.WorldMap;

public class EventCheck {
	private Set<Character>tools;
	private HashMap<Character, ArrayList<Position>> tools_position;
	private HashMap<Character, ArrayList<Position>> obstacle_position;
	private Map<Position, Block>world_map;
	private Queue<Event>events;
	
	public EventCheck(){
		this.tools = ToolsSet.getInstance();
		this.tools_position = ToolsPosition.getInstance();
		this.obstacle_position = ObstaclePosition.getInstance();
		this.world_map = WorldMap.getInstance();
		this.events = Events.getInstance();
	}
	
	/**
	 * Key function to arrange all events logically.
	 * @param exp_flag
	 */
	public void event_check(boolean exp_flag){
		//If agent does not have wood, the sea is not accessible.
		if(!this.tools.contains('w')){
			if(this.obstacle_position.containsKey('~')){
				for(Position sea_pos : this.obstacle_position.get('~')){
					this.world_map.get(sea_pos).isOpen = false;
				}
			}
		}
		if(this.tools.contains('$')){
			this.events.add(new Event(0, EventName.Return_Home, null));
		}
		if(!exp_flag){
			if(this.tools.contains('k')){
				if(this.obstacle_position.containsKey('-')){
					if(this.obstacle_position.get('-').size() > 0){
						this.events.add(new Event(1, EventName.Open_Lock, null));
					}
				}
			}
			if(this.tools.contains('d')){
				if(this.obstacle_position.containsKey('*')){
					if(this.obstacle_position.get('*').size() > 0){
						this.events.add(new Event(5, EventName.Boom_Way, null));
					}
				}
			}
			if(this.tools_position.size() > 0){
				Iterator<Character>tools_ite = this.tools_position.keySet().iterator();
				while(tools_ite.hasNext()){
					char tool = tools_ite.next();
					List<Position>tool_array = this.tools_position.get(tool);
					if(tool == 'k'){
						if(tool_array.size() > 0){
							this.events.add(new Event(2, EventName.Get_Tool_K, null));
						}
					}
					if(tool == 'd'){
						if(tool_array.size() > 0){
							this.events.add(new Event(2, EventName.Get_Tool_D, null));
						}
					}
					if(tool == 'a'){
						if(tool_array.size() > 0){
							this.events.add(new Event(2, EventName.Get_Tool_A, null));
						}
					}
					if(tool == '$'){
						if(tool_array.size() > 0){
							this.events.add(new Event(2, EventName.Get_Tool_T, null));
						}
					}
				}
			}
			if(this.tools.contains('$')){
				this.events.add(new Event(0, EventName.Return_Home, null));
			}
			if(this.tools.contains('a')){
				if(this.obstacle_position.containsKey('T')){
					if(this.obstacle_position.get('T').size() > 0 && !this.tools.contains('w')){
						this.events.add(new Event(3, EventName.Cut_Tree, null));
					}
				}
			}
			if(this.tools.contains('w') && !Variable.inSea){
				if(this.obstacle_position.containsKey('~')){
					for(Position sea_pos : this.obstacle_position.get('~')){
						this.world_map.get(sea_pos).isOpen = true;
					}
					this.events.add(new Event(4, EventName.Switch_Explore, null));
				}
			}
			if(Variable.inSea){
				this.events.add(new Event(4, EventName.Switch_Explore, null));
			}
		}
	}
}
