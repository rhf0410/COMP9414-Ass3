package com.example.map;

import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import com.example.ass.Event;

public class Events {
	private static volatile Queue<Event>events;
	
	public Events(){
		
	}
	
	/**
	 * Events queue.
	 * @return
	 */
	public static synchronized Queue<Event>getInstance(){
		if(events == null){
			synchronized(Action.class){
				if(events == null){
					events = new PriorityQueue<Event>();
				}
			}
		}
		return events;
	}
}
