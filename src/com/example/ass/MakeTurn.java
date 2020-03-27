package com.example.ass;

/**
 * Change direction.
 * @author lenovo
 *
 */
public class MakeTurn {
	/**
	 * Make turn (rotate agent)
	 * @param dis_pos
	 * @return
	 */
	public static String [] make_turn(Position dis_pos){
		String [] dirc = new String[2];
		if(Variable.rotation.equals("North")){
			if(dis_pos.x == 0 && dis_pos.y == 1){
				dirc[0] = "";
				dirc[1] = "North";
			}else if(dis_pos.x == 1 && dis_pos.y == 0){
				dirc[0] = "r";
				dirc[1] = "East";
			}else if(dis_pos.x == 0 && dis_pos.y == -1){
				dirc[0] = "rr";
				dirc[1] = "South";
			}else if(dis_pos.x == -1 && dis_pos.y == 0){
				dirc[0] = "l";
				dirc[1] = "West";
			}
		}else if(Variable.rotation.equals("East")){
			if(dis_pos.x == 0 && dis_pos.y == 1){
				dirc[0] = "l";
				dirc[1] = "North";
			}else if(dis_pos.x == 1 && dis_pos.y == 0){
				dirc[0] = "";
				dirc[1] = "East";
			}else if(dis_pos.x == 0 && dis_pos.y == -1){
				dirc[0] = "r";
				dirc[1] = "South";
			}else if(dis_pos.x == -1 && dis_pos.y == 0){
				dirc[0] = "ll";
				dirc[1] = "West";
			}
		}else if(Variable.rotation.equals("South")){
			if(dis_pos.x == 0 && dis_pos.y == 1){
				dirc[0] = "ll";
				dirc[1] = "North";
			}else if(dis_pos.x == 1 && dis_pos.y == 0){
				dirc[0] = "l";
				dirc[1] = "East";
			}else if(dis_pos.x == 0 && dis_pos.y == -1){
				dirc[0] = "";
				dirc[1] = "South";
			}else if(dis_pos.x == -1 && dis_pos.y == 0){
				dirc[0] = "r";
				dirc[1] = "West";
			}
		}else if(Variable.rotation.equals("West")){
			if(dis_pos.x == 0 && dis_pos.y == 1){
				dirc[0] = "r";
				dirc[1] = "North";
			}else if(dis_pos.x == 1 && dis_pos.y == 0){
				dirc[0] = "rr";
				dirc[1] = "East";
			}else if(dis_pos.x == 0 && dis_pos.y == -1){
				dirc[0] = "l";
				dirc[1] = "South";
			}else if(dis_pos.x == -1 && dis_pos.y == 0){
				dirc[0] = "";
				dirc[1] = "West";
			}
		}
		return dirc;
	}
}
