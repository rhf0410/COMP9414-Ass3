package com.example.ass;

/*********************************************
 *  Agent.java 
 *  Sample Agent for Text-Based Adventure Game
 *  COMP3411/9414/9814 Artificial Intelligence
 *  UNSW Session 1, 2018
*/

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.example.event.EventCheck;
import com.example.event.event;
import com.example.event.eventFactory;
import com.example.event.event_exploreMap;
import com.example.map.Action;
import com.example.map.Events;
import com.example.map.ToolsPosition;
import com.example.map.ToolsSet;

public class Agent {
	private Set<Character>tools;
	private HashMap<Character, ArrayList<Position>> tools_position;
	private Deque<Character>action;
	private Map<String, Position>rotation_corr;
	private Queue<Event>events;

	private event event;
	private EventCheck event_check;
	
	/**
	 * Constructing function.
	 */
	public Agent() {
		this.tools = ToolsSet.getInstance();
		this.tools_position = ToolsPosition.getInstance();
		this.action = Action.getInstance();
		
		//Used for calculating cos & sin.
		this.rotation_corr = new HashMap<String, Position>();
		this.rotation_corr.put("North", new Position(1, 0));
		this.rotation_corr.put("East", new Position(0, 1));
		this.rotation_corr.put("South", new Position(-1, 0));
		this.rotation_corr.put("West", new Position(0, -1));
		
		Variable.rotation = new String("North");
		Variable.current_pos = new Position(0, 0);
		this.events = Events.getInstance();
		
		Variable.map_width_floor = -1;
		Variable.map_width_ceil = 1;
		Variable.map_height_floor = -1;
		Variable.map_height_ceil = 1;
		
		Variable.inSea = false;
		this.event_check = new EventCheck();
	}

	public char get_action( char view[][] ) {
		// REPLACE THIS CODE WITH AI TO CHOOSE ACTION
		if(this.action.size() == 0){
			//---------------------------------------------------------------------------------------------------
			System.out.print("We have tools: ");
			Iterator<Character>ite_tools = this.tools.iterator();
			while(ite_tools.hasNext()){
				System.out.print(ite_tools.next() + " ");
			}
			System.out.println();
			Iterator<Character>ite = this.tools_position.keySet().iterator();
			while(ite.hasNext()){
				char tool = ite.next();
				ArrayList<Position>pos = this.tools_position.get(tool);
				for(int i=0;i<pos.size();i++){
					System.out.print(tool + " at position " + "(" + pos.get(i).x + "," + pos.get(i).y +") ");
				}
			}
			System.out.println();
			//---------------------------------------------------------------------------------------------------
			
			this.event = new event_exploreMap(view);
			boolean exp_flag = this.event.operation();
			this.event_check.event_check(exp_flag);
			boolean event_flag = false;
			
			while(!this.events.isEmpty()){
				Event next_event = this.events.poll();
				System.out.println("Event name: " + next_event.name);
				event_flag = eventFactory.eventProduct(next_event);
				if(event_flag){
					break;
				}else{
					System.out.println(next_event.name + " failed.");
				}
			}
			
			if(this.action.size() > 0){
				char action = this.action.pollFirst();
				return action;
			}else{
				int ch=0;
				System.out.print("Enter Action(s): ");
				try {
				    while ( ch != -1 ) {
						// read character from keyboard
						ch  = System.in.read();
						switch( ch ) { 
							// if character is a valid action, return it
							case 'F': case 'L': case 'R': case 'C': case 'U':
							case 'f': case 'l': case 'r': case 'c': case 'u':
							break;
						}
				    }
				}catch (IOException e) {
					System.out.println ("IO error:" + e );
				}
				return((char) ch );
			}
		}else{
			char action = this.action.pollFirst();
			return action;
		}
	}

	void print_view( char view[][] ){
		int i,j;

		System.out.println("\n+-----+");
		for( i=0; i < 5; i++ ) {
			System.out.print("|");
			for( j=0; j < 5; j++ ) {
				if(( i == 2 )&&( j == 2 )) {
					System.out.print('^');
				}
				else {
					System.out.print( view[i][j] );
				}
			}
			System.out.println("|");
		}
		System.out.println("+-----+");
	}
	
	public static void main( String[] args ){
		  InputStream in  = null;
	      OutputStream out= null;
	      Socket socket   = null;
	      Agent  agent    = new Agent();
	      char   view[][] = new char[5][5];
	      char   action   = 'F';
	      int port;
	      int ch;
	      int i,j;

	      /*if( args.length < 2 ) {
	         System.out.println("Usage: java Agent -p <port>\n");
	         System.exit(-1);
	      }*/

	      port = 10000;

	      try { // open socket to Game Engine
	         socket = new Socket( "localhost", port );
	         in  = socket.getInputStream();
	         out = socket.getOutputStream();
	      }catch( IOException e ) {
	         System.out.println("Could not bind to port: "+port);
	         System.exit(-1);
	      }

	      try { // scan 5-by-5 window around current location
	         while( true ) {
	            for( i=0; i < 5; i++ ) {
	               for( j=0; j < 5; j++ ) {
	                  if( !(( i == 2 )&&( j == 2 ))) {
	                     ch = in.read();
	                     if( ch == -1 ) {
	                        System.exit(-1);
	                     }
	                     view[i][j] = (char) ch;
	                  }
	               }
	            }
	            agent.print_view( view ); // COMMENT THIS OUT BEFORE SUBMISSION
	            action = agent.get_action( view );
	            try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            out.write( action );
	         }
	      }
	      catch( IOException e ) {
	         System.out.println("Lost connection to port: "+ port );
	         System.exit(-1);
	      }
	      finally {
	         try {
	            socket.close();
	         }
	         catch( IOException e ) {}
	      }
	}
}
