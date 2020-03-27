package com.example.newAstar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import com.example.ass.Block;
import com.example.ass.Position;
import com.example.map.Distance;

public class NewAstar {
	private Queue<Astar_position>openList;
	private List<Astar_position>closeList;
	private boolean thrSea;
	
	public NewAstar(boolean thrSea){
		this.openList = new PriorityQueue<Astar_position>();
		this.closeList = new ArrayList<Astar_position>();
		this.thrSea = thrSea;
	}
	
	private boolean isEndNode(Position end, Position pos){
		return pos != null && end.equals(pos);
	}
	
	private boolean canAddNodetoMap(Map<Position, Block>world_map, Astar_position end, int x, int y){
		Position detection = new Position(x, y);
		if(!world_map.containsKey(detection)){
			return false;
		}
		if(x == end.pos.x && y == end.pos.y){
			return true;
		}
		Block block = world_map.get(detection);
		char type = block.type;
		if(!block.isOpen){
			return false;
		}
		if(isPositioninClose(detection)){
			return false;
		}
		if(this.thrSea){
			if(type == '~'){
				return true;
			}
		}else{
			if(type != 'T' && type != '-'){
				return true;
			}
		}
		return false;
	}

	private boolean isPositioninClose(Position pos){
		return pos != null && isPositioninClose(pos.x, pos.y);
	}
	
	private boolean isPositioninClose(int x, int y){
		if(this.closeList.isEmpty()){
			return false;
		}
		for(Astar_position node: this.closeList){
			if(node.pos.x == x && node.pos.y == y){
				return true;
			}
		}
		return false;
	}
	
	private Astar_position findNodeinOpenList(Position pos){
		if(pos == null || this.openList.isEmpty()){
			return null;
		}
		for(Astar_position node: this.openList){
			if(node.pos.equals(pos)){
				return node;
			}
		}
		return null;
	}
	
	private void addNeighborNodeInOpen(Map<Position, Block>world_map, Astar_position current, Astar_position end){
		int x = current.pos.x;
		int y = current.pos.y;
		
		addNeighborNodeInOpen(world_map, current, end, x, y+1, Constant.DIRECT_VALUE);
		addNeighborNodeInOpen(world_map, current, end, x+1, y, Constant.DIRECT_VALUE);
		addNeighborNodeInOpen(world_map, current, end, x, y-1, Constant.DIRECT_VALUE);
		addNeighborNodeInOpen(world_map, current, end, x-1, y, Constant.DIRECT_VALUE);
	}
	
	private void addNeighborNodeInOpen(Map<Position, Block>world_map, Astar_position current, Astar_position end, int x, int y, int value){
		if(canAddNodetoMap(world_map, end, x, y)){
			Position pos = new Position(x, y);
			int G = current.G + value;
			Astar_position child = findNodeinOpenList(pos);
			if(child == null){
				int H = Distance.distance(end.pos, pos);
				if(this.isEndNode(end.pos, pos)){
					child = end;
					child.parent = current;
					child.G = G;
					child.H = H;
				}else{
					child = new Astar_position(pos, current, G, H);
				}
				this.openList.add(child);
			}else if(child.G > G){
				child.G = G;
				child.parent = current;
				this.openList.add(child);
			}
		}
	}
	
	public List<Position>draw_path(Map<Position, Block>world_map, Astar_position end){
		if(world_map == null || end == null){
			return null;
		}
		List<Position>path = new ArrayList<Position>();
		while(end != null){
			Position pos = end.pos;
			path.add(pos);
			end = end.parent;
		}
		return path;
	}
	
	public List<Position> start(Map<Position, Block>world_map, Position start, Position end){
		if(world_map == null){
			return null;
		}
		Astar_position start_point = new Astar_position(start);
		Astar_position end_point = new Astar_position(end);
		this.openList.clear();
		this.closeList.clear();
		this.openList.add(start_point);
		return this.moveNodes(world_map, end_point);
	}
	
	private List<Position> moveNodes(Map<Position, Block>world_map, Astar_position end_point){
		List<Position>path=null;
		while(!this.openList.isEmpty()){
			if(isPositioninClose(end_point.pos)){
				path = this.draw_path(world_map, end_point);
				Collections.reverse(path);
				path.remove(0);
				break;
			}
			Astar_position current = this.openList.poll();
			this.closeList.add(current);
			this.addNeighborNodeInOpen(world_map, current, end_point);
		}
		return path;
	}

	public void setThrSea(boolean thrSea) {
		this.thrSea = thrSea;
	}
}
