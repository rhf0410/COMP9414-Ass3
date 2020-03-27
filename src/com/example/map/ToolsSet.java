package com.example.map;

import java.util.HashSet;
import java.util.Set;

public class ToolsSet {
	private static volatile Set<Character> tools;
	
	private ToolsSet(){
		
	}
	
	/**
	 * Tools'box.
	 * @return
	 */
	public static synchronized Set<Character> getInstance(){
		if(tools == null){
			synchronized(ToolsSet.class){
				if(tools == null){
					tools = new HashSet<Character>();
				}
			}
		}
		return tools;
	}
}
