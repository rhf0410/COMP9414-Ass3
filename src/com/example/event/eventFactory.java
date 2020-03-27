package com.example.event;

import com.example.ass.Event;
import com.example.ass.EventName;

/**
 * Event Factory for producing.
 * @author lenovo
 *
 */
public class eventFactory {
	public static boolean eventProduct(Event next_event){
		event event;
		boolean event_flag = false;
		switch(next_event.name){
			case Return_Home:
				event = new event_return();
				event_flag = event.operation();
				if(event_flag){
					System.out.println(next_event.name + " succeeded.");
				}
				break;
			case Open_Lock:
				event = new event_openLock();
				event_flag = event.operation();
				if(event_flag){
					System.out.println(next_event.name + " succeeded.");
				}
				break;
			case Get_Tool_K:
				event = new event_getTool('k');
				event_flag = event.operation();
				if(event_flag){
					System.out.println(next_event.name + " succeeded.");
				}
				break;
			case Get_Tool_D:
				event = new event_getTool('d');
				event_flag = event.operation();
				if(event_flag){
					System.out.println(next_event.name + " succeeded.");
				}
				break;
			case Get_Tool_T:
				event = new event_getTool('$');
				event_flag = event.operation();
				if(event_flag){
					System.out.println(next_event.name + " succeeded.");
				}
				break;
			case Get_Tool_A:
				event = new event_getTool('a');
				event_flag = event.operation();
				if(event_flag){
					System.out.println(next_event.name + " succeeded.");
				}
				break;
			case Cut_Tree:
				event = new event_cutTree();
				event_flag = event.operation();
				if(event_flag){
					System.out.println(next_event.name + " succeeded.");
				}
				break;
			case Switch_Explore:
				event = new event_switchExp(next_event.para);
				event_flag = event.operation();
				if(event_flag){
					System.out.println(next_event.name + " succeeded.");
				}
				break;
			case Boom_Way:
				event = new event_boomWay();
				event_flag = event.operation();
				if(event_flag){
					System.out.println(next_event.name + " succeeded.");
				}
				break;
			default:
				break;
		}
		return event_flag;
	}
}
