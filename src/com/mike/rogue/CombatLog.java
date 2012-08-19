package com.mike.rogue;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.mike.rogue.Main.OurView;

public class CombatLog {
int messageType;
	
List<String> log;
List<Integer> messageColor;
	
public enum messageType {
	ENEMY(0), LOOT(1), DEATH(2);
	int value;
	messageType(int v){
		value = v;
	}
}
	
	
	public CombatLog() {
		 log = new ArrayList<String>();

		 messageColor = new ArrayList<Integer>();
	}




	
	public void add(String s, int c){
		log.add(s);
		if(log.size()>4){
			log.remove(0);
		}
		
		messageColor.add(c);
		if(messageColor.size()>4){
			messageColor.remove(0);
		}
	}

	
	public String get(int element) {
		return log.get(element);
		
	}
	
//	public int getColor(int element) {
//		return messageColor.get(element);
//	}
	
	public int size() {
		return log.size();
		
	}
	
	public void clear() {
		log.clear();
		messageColor.clear();
	}





	public int getType(int i) {
		return messageColor.get(i);
	}






	

	
}
