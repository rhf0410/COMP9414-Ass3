package com.example.map;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

import com.example.ass.Position;

public class Action {
	private static volatile Deque<Character>action;
	
	private Action(){
		
	}
	
	/**
	 * Action queue.
	 * @return
	 */
	public static synchronized Deque<Character>getInstance(){
		if(action == null){
			synchronized(Action.class){
				if(action == null){
					action = new LinkedList<Character>();
				}
			}
		}
		return action;
	}
}
