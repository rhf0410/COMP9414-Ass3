package com.example.Astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.ass.Block;
import com.example.ass.Position;
import com.example.map.Distance;
import com.example.newAstar.NewAstar;
import com.example.newAstar.ObsAstar;

/**
 * A* algorithms class.
 * @author Administrator
 *
 */
public class Astar {
	private Position pos;
	private boolean thrSea;
	private boolean thrObs;
	private int obsNum;
	private Set<Character>obsType;
	private Set<Character>obsExc;
	private Map<Position, Block>world_map;
	private Position current_pos;
	private NewAstar astar;
	private ObsAstar ostar;
	
	/**
	 * Constructing function.
	 * @param pos
	 * @param thrSea
	 */
	public Astar(Position pos, Map<Position, Block>world_map, Position current_pos) {
		this.pos = pos;
		this.thrSea = false;
		this.thrObs = false;
		this.obsNum = 0;
		this.obsType = new HashSet<Character>();
		this.obsType.add('*');
		this.obsType.add('T');
		this.obsExc = new HashSet<Character>();
		this.obsExc.add('~');
		this.world_map = world_map;
		this.current_pos = current_pos;
		this.astar = new NewAstar(this.thrSea);
		this.ostar = new ObsAstar(this.obsNum, this.obsType, this.obsExc);
	}
	
	/**
	 * Go to a specific block.
	 * @param pos
	 * @return
	 */
	public List<Position> go_to_block(Position pos){
		List<Position>path = new ArrayList<Position>();
		//Check if this block is accessible.
		if(!this.thrObs){
			if(!this.world_map.get(pos).isOpen){
				return null;
			}
		}
		//Check if the goal is current position.
		if(this.current_pos.x == pos.x && this.current_pos.y == pos.y){
			System.out.println("You are at goal now");
			return null;
		}
		
		if(!this.thrObs){
			this.astar.setThrSea(this.thrSea);
			path = this.astar.start(world_map, this.current_pos, pos);
		}else{
			//Initialize all path costs to infinity.
			Iterator<Position>ites = this.world_map.keySet().iterator();
			while(ites.hasNext()){
				Position tmp_pos = ites.next();
				this.world_map.get(tmp_pos).cost = Float.MAX_VALUE;
			}
			List<Position>step_list = new ArrayList<Position>();
			/*this.ostar.setObsNum(this.obsNum);
			path = this.ostar.start(world_map, this.current_pos, pos);
			this.obsNum = this.ostar.getObsNum();*/
			path = search_next_obs(this.current_pos, pos, step_list, 0, this.obsNum, this.obsType, this.obsExc);
		}
		if(path != null){
			System.out.println("Path found.");
			System.out.println("Goal (" + this.pos.x + "," + this.pos.y + ") " + this.world_map.get(pos).isOpen);
		}else{
			System.out.println("No path.");
		}
		return path;
	}

	/**
	 * Search for a path through obstacle.
	 * @param cur_pos
	 * @param goal_pos
	 * @param step_list
	 * @param cost
	 * @param obsNum
	 * @return
	 */
	public List<Position> search_next_obs(Position cur_pos, Position goal_pos, List<Position>step_list, float cost, int obsNum,
				Set<Character>obsType, Set<Character>obsExc){
		List<Astar_position>path = new ArrayList<Astar_position>();
		int [] xs = {0,1,0,-1};
		int [] ys = {1,0,-1,0};
		for(int i=0;i<xs.length;i++){
			Position next_pos = new Position(cur_pos.x + xs[i], cur_pos.y + ys[i]);
			if(this.world_map.containsKey(next_pos)){
				if(!obsExc.contains(this.world_map.get(next_pos).type)){
					Astar_position a_pos = new Astar_position(next_pos, Distance.distance(goal_pos, next_pos));
					path.add(a_pos);
				}
			}
		}
		
		//If nowhere to go, return.
		if(path.size() == 0){
			return null;
		}
		
		//Sort process.
		Collections.sort(path, new Comparator<Astar_position>() {

			@Override
			public int compare(Astar_position o1, Astar_position o2) {
				if(o1.distance > o2.distance){
					return 1;
				}else{
					return -1;
				}
			}
		});
		
		for(Astar_position a_pos:path){
			Position pos = a_pos.pos;
			if(!step_list.contains(pos)){
				if(pos.x == goal_pos.x && pos.y == goal_pos.y){
					step_list.add(pos);
					return step_list;
				}else if(this.world_map.get(pos).cost > cost){
					this.world_map.get(pos).cost = cost;
					List<Position>new_step_list = new ArrayList<Position>();
					new_step_list.addAll(step_list);
					new_step_list.add(pos);
					List<Position>res_path = null;
					char type = this.world_map.get(pos).type;
					if(obsType.contains(type)){
						if(obsNum - 1 >= 0){
							res_path = search_next_obs(pos, goal_pos, new_step_list, cost + 1, obsNum - 1, obsType, obsExc);
						}
					}else{
						res_path = search_next_obs(pos, goal_pos, new_step_list, cost, obsNum, obsType, obsExc);
					}
					if(res_path != null){
						return res_path;
					}
				}
			}
		}
		return null;
	}

	public boolean isThrObs() {
		return thrObs;
	}

	public void setThrObs(boolean thrObs) {
		this.thrObs = thrObs;
	}

	public int getObsNum() {
		return obsNum;
	}

	public void setObsNum(int obsNum) {
		this.obsNum = obsNum;
	}

	public boolean isThrSea() {
		return thrSea;
	}

	public void setThrSea(boolean thrSea) {
		this.thrSea = thrSea;
	}
}
