package com.example.event;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.Astar.Astar;
import com.example.ass.Block;
import com.example.ass.Position;
import com.example.ass.Variable;
import com.example.map.ToolsSet;
import com.example.map.WorldMap;
import com.example.order.OrderGenerating;

/**
 * Class for booming ways.
 * Use dynamites, may need when go home or get tools.
 * @author lenovo
 *
 */
public class event_boomWay extends event {
	private Set<Character>tools;
	private Map<Position, Block>world_map;
	private OrderGenerating order;
	
	public event_boomWay(){
		this.tools = ToolsSet.getInstance();
		this.world_map = WorldMap.getInstance();
		this.order = new OrderGenerating();
	}
	
	@Override
	public boolean operation() {
		// TODO Auto-generated method stub
		int num_bomb = 0;
		Iterator<Character>tool_ite = this.tools.iterator();
		while(tool_ite.hasNext()){
			if(tool_ite.next() == 'd'){
				num_bomb++;
			}
		}
		System.out.println("Number of bombs " + num_bomb);
		Astar astar = new Astar(new Position(0, 0), this.world_map, Variable.current_pos);
		astar.setThrObs(true);
		astar.setObsNum(num_bomb);
		List<Position>path = astar.go_to_block(new Position(0, 0));
		if(path != null){
			this.order = new OrderGenerating(path, "boom");
			this.order.order_generate();
			return true;
		}else{
			return false;
		}
	}

}
