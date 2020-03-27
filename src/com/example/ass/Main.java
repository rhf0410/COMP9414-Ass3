package com.example.ass;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Queue<Event>q = new PriorityQueue<Event>(10, new Comparator<Event>() {

			@Override
			public int compare(Event o1, Event o2) {
				if(o1.prior > o2.prior){
					return -1;
				}else{
					return 1;
				}
			}
		});
		Event e1 = new Event(1, null, null);
		Event e2 = new Event(3, null, null);
		Event e3 = new Event(2, null, null);
		q.add(e1);
		q.add(e2);
		q.add(e3);
		Iterator<Event>ite = q.iterator();
		while(ite.hasNext()){
			System.out.println(ite.next().prior);
		}
	}

}
