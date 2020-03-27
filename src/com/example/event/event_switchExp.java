package com.example.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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
import com.example.map.ToolsPosition;
import com.example.map.ToolsSet;
import com.example.map.WorldMap;
import com.example.order.OrderGenerating;

/**
 * Switch from land to water or from water to land.
 * @author lenovo
 *
 */
public class event_switchExp extends event {
	private Position target_pos;
	private HashMap<Character, ArrayList<Position>> obstacle_position;
	private HashMap<Character, ArrayList<Position>> tools_position;
	private Set<Character>tools;
	private Map<Position, Block>world_map;
	private OrderGenerating order;
	
	public event_switchExp(Position target_pos){
		this.target_pos = target_pos;
		this.obstacle_position = ObstaclePosition.getInstance();
		this.tools_position = ToolsPosition.getInstance();
		this.tools = ToolsSet.getInstance();
		this.world_map = WorldMap.getInstance();
		this.order = new OrderGenerating();
	}
	
	@Override
	public boolean operation() {
		// TODO Auto-generated method stub
		List<Position>path = null;
		if(!Variable.inSea){
			List<Astar_position>sea_entry = new ArrayList<Astar_position>();
			for(Position sea_pos : this.obstacle_position.get('~')){
				sea_entry.add(new Astar_position(sea_pos, Distance.distance(sea_pos, Variable.current_pos)));
			}
			if(sea_entry.size() > 0){
				//Sort process.
				Collections.sort(sea_entry, new Comparator<Astar_position>() {

					@Override
					public int compare(Astar_position o1, Astar_position o2) {
						if(o1.distance > o2.distance){
							return 1;
						}else{
							return -1;
						}
					}
				});
				
				for(Astar_position sea_pos_d : sea_entry){
					Astar astar = new Astar(sea_pos_d.pos, this.world_map, Variable.current_pos);
					path = astar.go_to_block(sea_pos_d.pos);
					if(path != null){
						break;
					}
				}
			}
			if(path != null){
				this.order = new OrderGenerating(path, "explore_switch");
				this.order.order_generate();
				Variable.inSea = true;
				return true;
			}else{
				return false;
			}
		}else{
			//Got to land to explore.
			if(target_pos == null){
				Iterator<Character>tool_ite = this.tools_position.keySet().iterator();
				while(tool_ite.hasNext()){
					char tool = tool_ite.next();
					List<Position>tool_pos_list = this.tools_position.get(tool);
					if(tool == 'k' && tool_pos_list.size() > 0){
						target_pos = tool_pos_list.get(0);
						break;
					}else if(tool == 'd' && tool_pos_list.size() > 0){
						target_pos = tool_pos_list.get(0);
						break;
					}else if(tool == '$' && tool_pos_list.size() > 0){
						target_pos = tool_pos_list.get(0);
						break;
					}
				}
			}
			List<Astar_position>sea_target_dis = new ArrayList<Astar_position>();
			for(Position sea_pos : this.obstacle_position.get('~')){
				sea_target_dis.add(new Astar_position(sea_pos, Distance.distance(target_pos, sea_pos)));
			}
			//Sort process.
			Collections.sort(sea_target_dis, new Comparator<Astar_position>() {

				@Override
				public int compare(Astar_position o1, Astar_position o2) {
					if(o1.distance > o2.distance){
						return 1;
					}else{
						return -1;
					}
				}
			});
			
			for(Astar_position sea_pos_d : sea_target_dis){
				Astar astar = new Astar(sea_pos_d.pos, this.world_map, Variable.current_pos);
				astar.setThrSea(true);
				path = astar.go_to_block(sea_pos_d.pos);
				if(path != null){
					break;
				}
			}
			
			if(path != null){
				this.order = new OrderGenerating(path, "explore_switch");
				this.order.order_generate();
				List<Astar_position>land_tar_list = new ArrayList<Astar_position>();
				Iterator<Position>land_pos = this.world_map.keySet().iterator();
				while(land_pos.hasNext()){
					Position land = land_pos.next();
					Block block = this.world_map.get(land);
					if(block.type == ' '){
						land_tar_list.add(new Astar_position(land, Distance.distance(land, target_pos)));
					}
				}
				//Sort process.
				Collections.sort(land_tar_list, new Comparator<Astar_position>() {

					@Override
					public int compare(Astar_position o1, Astar_position o2) {
						if(o1.distance > o2.distance){
							return 1;
						}else{
							return -1;
						}
					}
				});
				for(Astar_position land_pos_d : land_tar_list){
					Astar astar = new Astar(land_pos_d.pos, this.world_map, Variable.current_pos);
					List<Position>path_con = astar.go_to_block(land_pos_d.pos);
					if(path_con != null){
						this.order = new OrderGenerating(path_con, "");
						this.order.order_generate();
						Variable.inSea = false;
						this.tools.remove('w');
						System.out.println("Remove tree.");
						return true;
					}
				}
			}else{
				return false;
			}
		}
		return false;
	}

}
