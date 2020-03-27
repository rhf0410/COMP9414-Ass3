package com.example.map;

import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import com.example.ass.Event;
import com.example.ass.Position;

public class Frontier {
	private static volatile Deque<Position>frontier;
	
	public Frontier(){
		
	}
	
	/**
	 * Frontier queue.
	 * @return
	 */
	public static synchronized Deque<Position>getInstance(){
		if(frontier == null){
			synchronized(Frontier.class){
				if(frontier == null){
					frontier = new LinkedList<Position>();
				}
			}
		}
		return frontier;
	}
}
