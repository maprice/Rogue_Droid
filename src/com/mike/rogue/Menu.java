package com.mike.rogue;


import com.mike.rogue.Main.OurView;
//import com.mike.rogue.Main.button;

//import abilitys.Posn;
import abilitys.AttackType;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import items.Item;
import items.Item.ItemType;
import items.Item.Stat;

import java.lang.Math;
import java.util.Map.Entry;

import android.graphics.Point;


public class Menu {
	Bitmap bg, iconItem,iconSkill, k;
	Bitmap sword1, chest1, head1;  
	OurView ov;
	float x, y;
	float uX, uY;
	float drx, dry;
	int pressedX = -1;
	int pressedY = -1;

	int equipX = -1;
	int equipY = -1;

	int selectedX = 0;
	int selectedY = 0;
	Point item = new Point(-1,-1);
	int height, width;
	int currentFrame = 0;
	boolean hold;
	boolean dragging = false;
	Item dragNum;
	Stats character;
	//	int direction = 0;
	Resources re;
	int equipSlot;

	public Menu(OurView ourView, Resources r, Stats character) {
		ov = ourView;

		x = -100;
		y = -100;
		this.character = character;
		re = r;
	}


	final int tileWidth = 108;
	final int tileHeight = 54;


	Point selected = new Point(0,0); 
	Point slot;
	Item current;
	boolean pickUp = false;
	int view = 1;

	public void display(Canvas c, float mX, float mY, float uX, float uY, boolean hold) {
		checkTab(mX, mY);



		switch(view){
		case 0:
			skills(c, mX, mY, uX, uY, hold);
			break;

		case 1:
			inventory(c, mX, mY, uX, uY, hold);
			break;

		case 2:
			system(c, mX, mY, uX, uY, hold);
			break;
		}

		
		Paint pt = new Paint();
		pt.setAntiAlias(true);
		pt.setColor(Color.WHITE);
		pt.setTextSize(25);
		c.drawText("Skills", 64, 800-30, pt);
		c.drawText("Equip", 64+140, 800-30, pt);
		c.drawText("System", 60+282, 800-30, pt);
		
	}

	public boolean checkTab(float mX, float mY){
		int buttonWidth = 160;
		int newView = view;

		if(mY>700){
			if(mX > buttonWidth*2){

				view = 2;
			}
			else if(mX < buttonWidth){
				view = 0;
			}
			else {
				view = 1;
			}
		}
		if(newView != view){
			selected.x = 0;
			selected.y = 0;
			
			System.out.println(view);
			bg.recycle();
			switch(view){
			case 0:
				bg = BitmapFactory.decodeResource(re, R.drawable.m_skills);
				break;

			case 1:
				bg = BitmapFactory.decodeResource(re, R.drawable.m_inventory);
				break;

			case 2:
				bg = BitmapFactory.decodeResource(re, R.drawable.m_system);
				break;
			}
			return true;
		}
		else{
			return false;
		}


	}

	public void skills(Canvas c, float mX, float mY, float uX, float uY, boolean hold) {
		//c.drawBitmap(bg, 0, 0, null);
		
		
		

		
		
		
		x = mX;
		y = mY;
		this.uX = uX;
		this.uY = uY;
		this.hold = hold;

		
		//drawEquip(c);
		drawItems(c);
//drawStats(c);
		
		
		
		
		for(int i = 0; i < character.currentSkills.length; i++){
			c.drawBitmap(character.currentSkills[i].getIcon(), 70+i*85, 50, null);
			
		}
		
		for(int i = 0; i < character.allSkills.length; i++){
			c.drawBitmap(character.allSkills[i].getIcon(), 30+i*85, 130, null);
			
		}
		
		slot = getInputSkill(x,y);

	//	x = skill
		if(!pickUp){
		//if(true){
			if(selected.y == -1){
				/*
				switch(selected.x){
				case 0:
					c.drawBitmap(icon2, headSlot.x, headSlot.y , null);
					//c.drawBitmap(icon2, 95 + (0*218), 23 + 0*icon2.getHeight() , null);
					break;

				case 1:
					c.drawBitmap(icon2, wepSlot.x, wepSlot.y , null);
					//	c.drawBitmap(icon2, 95 + (1*217), 23 + 0*icon2.getHeight() , null);
					break;

				case 2:
					c.drawBitmap(icon2, chestSlot.x, chestSlot.y , null);
				//	c.drawBitmap(icon2, 95 + (0*218), 23 + 1*icon2.getHeight() , null);
					break;

				case 3:
					c.drawBitmap(icon2, ringSlot.x, ringSlot.y , null);
					//	c.drawBitmap(icon2, 95 + (1*217), 23 + 1*icon2.getHeight() , null);
					break;

				case 4:
					c.drawBitmap(icon2, legSlot.x, legSlot.y , null);
					//	c.drawBitmap(icon2, 95 + (1*217), 23 + 1*icon2.getHeight() , null);
					break;
				}
				*/
				//System.out.println("neg");
				
				if(selected.x < 4){
					System.out.println("het");
					c.drawBitmap(iconSkill,70-5+selected.x*85, 50-5 , null);
				}
				else{
					c.drawBitmap(iconSkill, 30-5+(selected.x-4)*85, 130-5, null);
				}
				
			}
			else{
				c.drawBitmap(iconItem, 23 + selected.x*iconItem.getWidth(), 366+4 + selected.y*iconItem.getHeight() , null);
			}
		}
		//	drawSprite(c);

		Item temp;
		if(!hold){
		
			
			slot = getInputSkill(uX,uY);
			if( getInputSkill(uX,uY) != null && pickUp && valid(slot, dragNum)){

				System.out.println("Brabra");
				if(slot.y == selected.y && slot.x == selected.x){
					if(slot.y == -1){
						character.equipped[selected.x] = dragNum;
					}
					else{
						character.inventory[selected.y][selected.x] = dragNum;
					}
				}
				if(slot.y == -1){
					temp = character.equipped[slot.x];
					character.equipped[slot.x] = dragNum;

					if(selected.y == -1){
						character.equipped[selected.x] = temp;
					}
					else{
						character.inventory[selected.y][selected.x] = temp;
					}
				}
				else{
					temp = character.inventory[slot.y][slot.x] ;
					character.inventory[slot.y][slot.x] = dragNum;

					if(selected.y == -1){
						character.equipped[selected.x] = temp;
					}
					else{
						character.inventory[selected.y][selected.x] = temp;
					}
				}
				pickUp = false;
			}
			else if(pickUp){
				if(selected.y == -1){
					character.equipped[selected.x] = dragNum;
				}
				else{
					character.inventory[selected.y][selected.x] = dragNum;
				}
				pickUp = false;
			}

			dragging = false;

		}


		if(slot!=null && !pickUp && !dragging){
			selected = slot;

		}

		//	System.out.println(slot);
	/*	if(!dragging && hold && slot!= null){ 
			System.out.println("Selected");
			selected = slot;
			drx = x;
			dry = y;
			//dragging = true;
		}
		*/
		if(!dragging && hold && slot!= null){ 
			System.out.println("Selected");
			selected = slot;
			drx = x;
			dry = y;
			//dragging = true;
		}




		if(dragging && (Math.abs(x - drx) > 10 || Math.abs(y - dry) > 10) && !pickUp){
			System.out.println("Dragging");


			if(selected.y == -1){
				dragNum = character.equipped[selected.x];
				character.equipped[selected.x] = null;
			}
			else{
				dragNum = character.inventory[selected.y][selected.x];
				character.inventory[selected.y][selected.x] = null;
			}
			pickUp = true;
			System.out.println(dragNum);

		}


		if(pickUp){
			if(slot != null){
				if(slot.y == -1){
					if(slot.x<4){
					drawToolTipSkill(c, character.currentSkills[slot.x]);
					}
					else{
						drawToolTipSkill(c, character.allSkills[slot.x]);	
					}
					//character.equipped[slot.x] = null;
				}
				else{
					drawToolTip(c, character.inventory[slot.y][slot.x]);
				} 
			}
		}
		else{
			if(selected.y == -1){
				
				
				if(selected.x < 4){
				drawToolTipSkill(c, character.currentSkills[selected.x]);
				}
				else{
					drawToolTipSkill(c, character.allSkills[selected.x-4]);
				}
			//	c.drawText(character.currentSkills[selected.x].name, 0, 0, null);
			//	System.out.println("Skill ="+character.allSkills[selected.x].name);
				//character.equipped[slot.x] = null;
			}
			else{
			//	System.out.println("Skill ="+character.allSkills[selected.x].name);
				drawToolTip(c, character.inventory[selected.y][selected.x]);
			}
		}

		if(pickUp){
			c.drawBitmap(dragNum.getIcon(), x-(iconItem.getWidth()/2)-20, y-(iconItem.getHeight()/2)-20 , null);
		}


		
	}



	private void drawEquip(Canvas c) {
		if(character.equipped[0] != null){
			c.drawBitmap(character.equipped[0].getIcon(), headSlot.x, headSlot.y , null);
		}
		if(character.equipped[1] != null){
			c.drawBitmap(character.equipped[1].getIcon(),  wepSlot.x, wepSlot.y , null);
		}
		if(character.equipped[2] != null){
			c.drawBitmap(character.equipped[2].getIcon(),  chestSlot.x, chestSlot.y  , null);
		}
		if(character.equipped[3] != null){
			c.drawBitmap(character.equipped[3].getIcon(),  ringSlot.x, ringSlot.y  , null);
		}
		
		Paint pt = new Paint();
		pt.setAntiAlias(true);
		pt.setTextSize(22);
		pt.setColor(Color.WHITE);
		c.drawText(character.gold+"g", 480-50, 225, pt);
		
	}

	public void system(Canvas c, float mX, float mY, float uX, float uY, boolean hold) {
		c.drawBitmap(bg, 0, 0, null);
	}

	public void inventory(Canvas c, float mX, float mY, float uX, float uY, boolean hold) {
		x = mX;
		y = mY;
		this.uX = uX;
		this.uY = uY;
		this.hold = hold;
		drawItems(c);
		drawEquip(c);
	
drawStats(c);
		slot = getInput(x,y);

		if(!pickUp){
			if(selected.y == -1){
				switch(selected.x){
				case 0:
					c.drawBitmap(iconItem, headSlot.x, headSlot.y , null);
					//c.drawBitmap(icon2, 95 + (0*218), 23 + 0*icon2.getHeight() , null);
					break;

				case 1:
					c.drawBitmap(iconItem, wepSlot.x, wepSlot.y , null);
					//	c.drawBitmap(icon2, 95 + (1*217), 23 + 0*icon2.getHeight() , null);
					break;

				case 2:
					c.drawBitmap(iconItem, chestSlot.x, chestSlot.y , null);
				//	c.drawBitmap(icon2, 95 + (0*218), 23 + 1*icon2.getHeight() , null);
					break;

				case 3:
					c.drawBitmap(iconItem, ringSlot.x, ringSlot.y , null);
					//	c.drawBitmap(icon2, 95 + (1*217), 23 + 1*icon2.getHeight() , null);
					break;

				case 4:
					c.drawBitmap(iconItem, legSlot.x, legSlot.y , null);
					//	c.drawBitmap(icon2, 95 + (1*217), 23 + 1*icon2.getHeight() , null);
					break;
				}
			}
			else{
				c.drawBitmap(iconItem, 23 + selected.x*iconItem.getWidth(), 366+4 + selected.y*iconItem.getHeight() , null);
			}
		}
		//	drawSprite(c);

		Item temp;
		if(!hold){
			
			slot = getInput(uX,uY);
			if( getInput(uX,uY) != null && pickUp && valid(slot, dragNum)){

				System.out.println("Brabra");
				if(slot.y == selected.y && slot.x == selected.x){
					if(slot.y == -1){
						character.equipped[selected.x] = dragNum;
					}
					else{
						character.inventory[selected.y][selected.x] = dragNum;
					}
				}
				if(slot.y == -1){
					temp = character.equipped[slot.x];
					character.equipped[slot.x] = dragNum;

					if(selected.y == -1){
						character.equipped[selected.x] = temp;
					}
					else{
						character.inventory[selected.y][selected.x] = temp;
					}
				}
				else{
					temp = character.inventory[slot.y][slot.x] ;
					character.inventory[slot.y][slot.x] = dragNum;

					if(selected.y == -1){
						character.equipped[selected.x] = temp;
					}
					else{
						character.inventory[selected.y][selected.x] = temp;
					}
				}
				pickUp = false;
			}
			else if(pickUp){
				if(selected.y == -1){
					character.equipped[selected.x] = dragNum;
				}
				else{
					character.inventory[selected.y][selected.x] = dragNum;
				}
				pickUp = false;
			}

			dragging = false;
			character.updateStats();
		}


		if(slot!=null && !pickUp && !dragging){
			selected = slot;

		}

		//	System.out.println(slot);
		if(!dragging && hold && getItem(slot)!= null && slot!= null){ 
			System.out.println("Selected");
			selected = slot;
			drx = x;
			dry = y;
			dragging = true;
		}




		if(dragging && (Math.abs(x - drx) > 10 || Math.abs(y - dry) > 10) && !pickUp){
			System.out.println("Dragging");


			if(selected.y == -1){
				dragNum = character.equipped[selected.x];
				character.equipped[selected.x] = null;
			}
			else{
				dragNum = character.inventory[selected.y][selected.x];
				character.inventory[selected.y][selected.x] = null;
			}
			pickUp = true;
			System.out.println(dragNum);

		}


		if(pickUp){
			if(slot != null){
				if(slot.y == -1){
					drawToolTip(c, character.equipped[slot.x]);
					//character.equipped[slot.x] = null;
				}
				else{
					drawToolTip(c, character.inventory[slot.y][slot.x]);
				} 
			}
		}
		else{
			if(selected.y == -1){
				drawToolTip(c, character.equipped[selected.x]);
				//character.equipped[slot.x] = null;
			}
			else{
				drawToolTip(c, character.inventory[selected.y][selected.x]);
			}
		}

		if(pickUp){
			c.drawBitmap(dragNum.getIcon(), x-(iconItem.getWidth()/2)-20, y-(iconItem.getHeight()/2)-20 , null);
		}


	}

	private void drawStats(Canvas c) {

		
		Paint pb = new Paint();
		pb.setColor(Color.RED);
		
	//	c.drawRect(116 , 34, ((259) - (116))*(character.health/character.totalHealth)+116,43, pb); 
		c.drawRect(97 , 10, ((262) - (97))*(character.health/character.totalHealth)+97,31, pb); 

		pb.setColor(Color.BLUE);
		c.drawRect(97 , 35, ((262) - (97))*(character.mana/character.totalMana)+97,54, pb); 
		
			Paint pt = new Paint();
			pt.setAntiAlias(true);
			pt.setColor(Color.WHITE);
			pt.setTextSize(25);
			c.drawText(""+character.level, 40, 34, pt);
			
			
			pt.setTextSize(13);
			c.drawText((int)character.xp+"/"+(int)character.totalXp, 37, 52, pt);
			
			c.drawText((int)character.health+"/"+(int)character.totalHealth, 158, 27, pt);
			c.drawText((int)character.mana+"/"+(int)character.totalMana, 158, 51, pt);
			
			
			pt.setTextSize(17);

			c.drawText("Attack", 26, 90, pt);
			c.drawText("Defence", 26, 90+43, pt);
			c.drawText("Stamina", 26, 90+43+43, pt);
			c.drawText("Intellect", 26, 90+43+43+43, pt);
			
			c.drawText(""+character.attack, 125, 90, pt);
			c.drawText(""+character.defence, 125, 90+43, pt);
			c.drawText(""+character.stamina, 125, 90+43+43, pt);
			c.drawText(""+character.intellect, 125, 90+43+43+43, pt);
			
			


		
	}

	private boolean valid(Point slot, Item drop) {
		if(slot.y != -1){
			return true;
		}
		else{
			switch(slot.x){
			case 0:
				return(drop.type == ItemType.HEAD);

			case 1:
				return(drop.type == ItemType.WEAPON);

			case 2:
				return(drop.type == ItemType.CHEST);

			case 3:
				return(drop.type == ItemType.RING);
		case 4:
			return(drop.type == ItemType.LEG);
		}
		}
		return false;
	}

	private Item getItem(Point slot){
		if(slot == null){
			return null;
		}
		else if(slot.y == -1){
			return character.equipped[slot.x];

		}
		else{
			return character.inventory[slot.y][slot.x];

		}

	}

	private void drawSprite(Canvas c){
		int srcY = 0 * height;
		int srcX = currentFrame * width;


		//	y = 210;// + tileHeight;//+100;
		//	x = 346;

		int dx = 210;
		int dy = 35;/// + tileHeight/2*((j) + (i));

		Paint p = new Paint();
		p.setAlpha(70);
		Rect src = new Rect (srcX ,srcY, srcX + width, srcY + height);
		Rect dst = new Rect (dx, dy, dx + width, dy + height);

		c.drawBitmap(k, src, dst, null);
	}

	private Point getInput(float x, float y){
		Point slot = new Point();
		slot.x = (int)(x-23)/(iconItem.getWidth());
		slot.y = (int)(y-366+4)/(iconItem.getHeight());
		equipSlot = getSlot(x, y);


		if(slot.x >= 0 && slot.x <= 5 && slot.y >= 0 && slot.y <= 4){
			//	System.out.println(slot.x+" "+slot.y);
			return slot;	
		}
		else if(equipSlot >= 0){
			slot.x = equipSlot;
			slot.y = -1;
			return slot;
		}
		else{
			return null;
		}
	}
	
	private Point getInputSkill(float x, float y){
		Point slot = new Point();
		slot.x = (int)(x-23)/(iconItem.getWidth());
		slot.y = (int)(y-366+4)/(iconItem.getHeight());
		equipSlot = getSlotSkills(x, y);


		if(slot.x >= 0 && slot.x <= 5 && slot.y >= 0 && slot.y <= 4){
			//	System.out.println(slot.x+" "+slot.y);
			return slot;	
		}
		else if(equipSlot >= 0){
			//System.out.println("NEGEGEG");
			slot.x = equipSlot;
			slot.y = -1;
			return slot;
		}
		else{
			return null;
		}
	}

	private void drawItems(Canvas c){
		c.drawBitmap(bg, 0, 0, null);




		for(int j = 0; j < 5;j++){
			for(int i = 0; i < 6;i++){
				if(character.inventory[j][i] != null){
					if(character.inventory[j][i].getIcon() == null){
						System.out.println(character.inventory[j][i].name);
						System.out.println("Icon is null");

					}
					else if(iconItem == null){
						System.out.println("Icon2 is null");


					}
					//	c.drawBitmap(character.inventory[j][i].getIcon(), 23 + i*icon2.getWidth(), 366 + j*icon2.getHeight() , null);
					c.drawBitmap(character.inventory[j][i].getIcon(), 23 + i*iconItem.getWidth(), 370 + j*iconItem.getHeight() , null);
				}
			}
		}



		//if(character.equipped[4] != null){
		//	c.drawBitmap(character.equipped[3].getIcon(),  legSlot.x, legSlot.y  , null);
		//}
	}

	/*
case 0:
	return(drop.type == ItemType.HEAD);

case 1:
	return(drop.type == ItemType.WEAPON);

case 2:
	return(drop.type == ItemType.CHEST);

case 3:
	return(drop.type == ItemType.RING);

	private int getSlot(float x, float y) {
		int width = icon2.getWidth();
		int height = icon2.getHeight();
		if (y> 23 && y < 23+height && x > 95 && x < 95 + width ){
			return 0;
		}
		else if(y> 23 && y < 23+height && x > 95 + (1*218) && x < 95 + (1*218) + width ){
			return 1;
		}
		else if(y> 23 + 1*icon2.getHeight()  && y < 23 + 1*icon2.getHeight() +height && x > 95 && x < 95 + width ){
			return 2;
		}
		else if(y> 23 + 1*icon2.getHeight()  && y < 23 + 1*icon2.getHeight() +height && x > 95 + (1*218) && x < 95 + (1*218) + width ){
			return 3;
		}
		else{
			return -1;
		}


	}

	 */

	Point headSlot = new Point(302,6);
	Point wepSlot = new Point(217,86);
	Point chestSlot = new Point(302,86);
	Point ringSlot = new Point(386,42);
	Point legSlot = new Point(302,166);

	private int getSlot(float x, float y) {
		int width = iconItem.getWidth();
		int height = iconItem.getHeight();
		if (y> headSlot.y && y < headSlot.y+height && x > headSlot.x && x < headSlot.x + width ){
			return 0;
		}
		else if(y> wepSlot.y && y < wepSlot.y+height && x > wepSlot.x && x < wepSlot.x + width ){
			return 1;
		}
		else if(y> chestSlot.y  && y < chestSlot.y +height && x > chestSlot.x && x < chestSlot.x + width ){
			return 2;
		}
		else if(y> ringSlot.y  && y < ringSlot.y +height && x > ringSlot.x && x < ringSlot.x + width ){
			return 3;
		}
		else if(y> legSlot.y  && y < legSlot.y +height && x > legSlot.x && x < legSlot.x + width ){
			return 4;
		}
		else{
			return -1;
		}


	}
	
	
	
	private int getSlotSkills(float x, float y) {
		int topIconY = 50;
		int botIconY = 130;
		
	
		
		int width = character.currentSkills[0].width;
		int height = character.currentSkills[0].height;
		
		if (y> topIconY && y < topIconY+height){
			//System.out.println("Your presssssed top "+(int)(x-70)/(width));
			
			
			if((int)(x-70)/(width)< 4){
			return (int)(x-70)/(width);
			}
			else{
				return 3;
			}
		}
		else if (y> botIconY && y < botIconY+height){
			//System.out.println("Your presssssed bot"+(int)(x)/(width));
			return (int)(x)/(width)+4;
		}
		else{
			return -1;
		}


	}


	private void drawToolTip(Canvas c, Item i){
		if(i != null){
			Paint pt = new Paint();
			pt.setAntiAlias(true);
			pt.setTextSize(22);
			pt.setColor(i.quality);


			c.drawText(i.name, 29, 270, pt);
			pt.setColor(Color.WHITE);
			pt.setTextSize(17);

			//c.drawText("                 ".substring(0,i.type.length())+i.type, 325, 215, pt);
			c.drawText("Requires Level "+i.levelReq, 32, 289, pt);

			c.drawText("Sell Price: "+i.sellPrice+"g", 32, 348, pt);
			pt.setColor(Color.GREEN);
			int j = 0;
			for (Entry<Stat, Integer> entry : i.stats.entrySet()) {
				
				c.drawText(i.statLexeme(entry.getKey()), 38 , 308+ 18*j, pt);
				j++;
				//c.drawText(i.stats, 50, 262, pt);
			}
			//	

		}
	}
	
	private void drawToolTipSkill(Canvas c, AttackType x){
		if(!x.empty){
			Paint pt = new Paint();
			pt.setAntiAlias(true);
			pt.setTextSize(22);
			pt.setColor(Color.WHITE);


			c.drawText(x.name, 29, 270, pt);
			pt.setColor(Color.WHITE);
			pt.setTextSize(17);

			//c.drawText("                 ".substring(0,i.type.length())+i.type, 325, 215, pt);
			c.drawText((int)x.damage+" damage", 32, 289, pt);
			pt.setColor(Color.GREEN);
			c.drawText(x.description, 38, 308, pt);

			int rX = 380;
			int rY = 274;
			int size = 20;
		
			
			pt.setColor(Color.BLUE);
			//c.drawRect(rX+1*size, rY+-1*size, rX+1*size+18, rY+-1*size+18, pt);
			c.drawCircle (rX+1*size+9, rY+-1*size+9, 10, pt);
			for(int j = x.attackGrid.length-1; j >= 0; j--){
			for(int i = 0; i < x.attackGrid[0].length; i++){
				if(x.attackGrid[j][i]==1){
					pt.setColor(Color.DKGRAY);
				c.drawRect(rX+i*size, rY+j*size, rX+i*size+18, rY+j*size+18, pt);
				}
				else{
					pt.setColor(Color.WHITE);
				//	c.drawRect(rX+i*size, rY+j*size, rX+i*size+18, rY+j*size+18, pt);
				}
			}
			}
			//	

		}
	}

	public boolean itemSlot(){
		return true;
	}

	public boolean within(Bitmap b, int bX, int bY){
		int height = b.getHeight();
		int width = b.getWidth();
		return (y> bY && y < bY+height && x > bX && x < bX + width );
	}


	public void temp(){
		System.out.println("X= "+(int)(x-25)/(iconItem.getWidth())+" Y= "+(int)(y-367)/(iconItem.getHeight()));

	}


	public void animate(Canvas c) {
		currentFrame = ++currentFrame % 5;


	}

	public void recycleBitmaps() {
		//if(k!=null){
		k.recycle();
		bg.recycle();
		iconItem.recycle();
		iconSkill.recycle();
		//sword1.recycle();
		//chest1.recycle();
		//head1.recycle();
		//	}

	}

	public void loadBitmaps() {
		//	if(k == null){
		k = BitmapFactory.decodeResource(re, R.drawable.cs_bigsprite);
		bg = BitmapFactory.decodeResource(re, R.drawable.m_inventory);
		iconItem = BitmapFactory.decodeResource(re, R.drawable.m_itemicon);
		iconSkill = BitmapFactory.decodeResource(re, R.drawable.m_skillicon);
		//sword1 = BitmapFactory.decodeResource(re, R.drawable.i_sword1);
		//chest1 = BitmapFactory.decodeResource(re, R.drawable.i_chest1);
		//head1 = BitmapFactory.decodeResource(re, R.drawable.i_head1);

		height =  k.getHeight() / 1; //5 rows
		width = k.getWidth() / 5; //5 columns
		//}

	}
}





