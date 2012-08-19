package com.mike.rogue;


import com.mike.rogue.Main.OurView;
import com.mike.rogue.Main.button;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class GroundLoot {
	Bitmap loot,gold;
	OurView ov;

	final int xSpeed = 6;
	final int ySpeed = 3;
	int x, y;
	int xCoord, yCoord;
	int height, width;
	int xPlayer, yPlayer;
	boolean type; //true == item, false == gold

	public GroundLoot(OurView ourView, Resources r, int xCoord, int yCoord, int xPlayer, int yPlayer, boolean type) {
		this.type = type;
		if(type){
			loot = BitmapFactory.decodeResource(r, R.drawable.g_lootbag);
		}
		else{
			loot = BitmapFactory.decodeResource(r, R.drawable.g_goldbag);	
		}
		ov = ourView;
		//x = -100;
		//y = -100;	

		this.xCoord = xCoord;
		this.yCoord = yCoord;

		System.out.println("loot at x= "+xCoord+" y= "+yCoord);
		this.xPlayer = xPlayer;
		this.yPlayer = yPlayer;
		//	xCoord = startX; 
		//	yCoord = starty;
		x = 365 + width/2 + 22 + tileWidth/2*((yCoord-yPlayer) - (xCoord - xPlayer));
		y = 152 + height + 65 + tileHeight/2*((yCoord-yPlayer) + (xCoord - xPlayer));
	}


	final int tileWidth = 108;
	final int tileHeight = 54;




	public void animate(Canvas c, button pressed, boolean movement) {

		if(movement){
			switch(pressed){
			case UP:
				x += xSpeed;
				y += ySpeed;
				break;

			case DOWN:
				x -= xSpeed;
				y -= ySpeed;
				break;

			case LEFT:
				x += xSpeed;
				y -= ySpeed;
				break;


			case RIGHT:
				x -= xSpeed;
				y += ySpeed;
				break;
			}
		}

		c.drawBitmap(loot, x, y, null);

	}

}
