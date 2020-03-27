package com.example.ass;

public class Event implements Comparable<Event> {
	public int prior;
	public EventName name;
	public Position para;
	
	public Event(int prior, EventName name, Position para) {
		this.prior = prior;
		this.name = name;
		this.para = para;
	}

	@Override
	public int compareTo(Event o) {
		// TODO Auto-generated method stub
		if(this.prior > o.prior){
			return 1;
		}else{
			return -1;
		}
	}
}
