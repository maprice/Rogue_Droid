package com.mike.rogue;

import items.Item;
import items.Item.Stat;



import abilitys.AttackType;
import abilitys.Basic;
import abilitys.Empty;
import abilitys.FireStorm;
import abilitys.FrostBite;
import abilitys.Lightning;
import abilitys.WoodSpike;
import android.content.res.Resources;

import com.mike.rogue.Main.OurView;

public class Stats {
	public Stats(OurView ov, Resources re) {
	this.re = re;
	this.ov = ov;
	
	gold = 0;
	
		allSkills = new AttackType[5];
		allSkills[0] = new FrostBite(ov, re);
		allSkills[1] = new FireStorm(ov, re);
		allSkills[2] = new Lightning(ov, re);
		allSkills[3] = new Basic(ov, re);
		allSkills[4] = new WoodSpike(ov, re);

		
		currentSkills = new AttackType[4];
		currentSkills[0] = new Empty(ov, re);
		currentSkills[1] = new Empty(ov, re);
		currentSkills[2] = new Empty(ov, re);
		currentSkills[3] = new Empty(ov, re);
		
		
	    inventory = new Item[5][6];
	//	inventory[0][0] = new Item(ov, re);
	//	inventory[1][5] = new Item(ov, re);

	    equipped = new Item[5];
	    
	//	addItem(1, 5);
	//	addItem(2, 5);
	//addItem(3, 5);
	//addItem(4, 5);
	//	addItem(5, 5);
	//	equipped[0] = new Item(ov, re);
	//	equipped[1] = new Item(ov, re);
	}

	int level = 1;
	float health = 100;
	int totalHealth = 100;
	float mana = 100;
	int totalMana = 100;
	float xp = 0;
	float totalXp = 100;

	int attack = 1;
	int defence = 1;
	int stamina = 1;
	int intellect =1;
	
	
	AttackType []currentSkills;
	AttackType []allSkills;

	Item[][] inventory;
	Item[] equipped;
	int gold;
	Resources re;
	OurView ov;
	
	
	public boolean addItem(Item newItem) {
		for(int j = 0; j < 5;j++){
			for(int i = 0; i < 6;i++){
				if(inventory[j][i] == null){
					inventory[j][i] = newItem;
					return true;
				}
			}
		}
		return false;
		
	}

	public void levelUp() {
		level++;
		xp = xp - totalXp;
		totalXp = (float)(totalXp*(1.3));
		totalHealth = (int)(totalHealth*(1.1));
		totalMana = (int)(totalMana*(1.1));
		health = totalHealth;
		mana = totalMana;
	}
	public void updateStats() {
		
		attack = 1;
		defence = 1;
		stamina = 1;
		intellect =1;
		
for(int i = 0; i < equipped.length; i++){
	
	if(equipped[i] != null){
	if(equipped[i].stats.containsKey(Stat.ATTACK)){
		attack += equipped[i].stats.get(Stat.ATTACK);
	}
	
	if(equipped[i].stats.containsKey(Stat.DEFENCE)){
		defence += equipped[i].stats.get(Stat.DEFENCE);
	}
	
	if(equipped[i].stats.containsKey(Stat.HEALTH)){
		stamina += equipped[i].stats.get(Stat.HEALTH);
	}
	
	if(equipped[i].stats.containsKey(Stat.MANA)){
		intellect += equipped[i].stats.get(Stat.MANA);
	}
	}
	
}
	}
}


















