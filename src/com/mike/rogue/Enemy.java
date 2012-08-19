package com.mike.rogue;

import com.mike.rogue.Main.OurView;
import com.mike.rogue.Main.button;

import dungeonGen.Dungeon.BlockType;

import android.R.color;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Enemy {
	Bitmap m;
	OurView ov;

	int x, y; //current pixel value of x y 
	int height, width; //height and width of sprite
	int currentFrame = 0; //frame of the sprite sheet
	int frame = 0;//frame of animation
	int xCoord , yCoord; //coordinates with respect to the map
	int xPlayer, yPlayer; //players x and y coordinates
	int id;



	//Prolly should import this from somewhere...
	final int xSpeed = 6;
	final int ySpeed = 3;
	final int tileWidth = 108;
	final int tileHeight = 54;
	final int xScale = tileWidth/2;
	final int YScale = tileHeight/2;

	static int idct = 1;

	direction d = direction.DOWN;
	Map maper;	
	boolean movement;
	boolean attack = false;

	//Stats s;  //to be implemented

	AttackSwipe sq;


	float totalHP = 100;
	float currentHP = 100;
	int damage = 5;

	boolean hit = false;

	public enum direction {
		UP (0), DOWN (1) , LEFT (2), RIGHT (3), NONE (4);  
		private int value;
		direction(int v){
			value = v;
		}
	}

	public Enemy(OurView ourView, Resources r, Map map, int startX, int starty) {
		id = idct++; 
		m = BitmapFactory.decodeResource(r, R.drawable.e_slime);
		ov = ourView;
		height = m.getHeight() / 4; //4 rows
		width = m.getWidth() / 3; //3 columns

		maper = map;

		xPlayer = map.getCurrentX();
		yPlayer = map.getCurrentY();
		sq = new AttackSwipe(ov,r); 
		xCoord = startX; 
		yCoord = starty;
		x = 365 + width/2 + 12 + tileWidth/2*((yCoord-yPlayer) - (xCoord - xPlayer));
		y = 152 + height + 35 + tileHeight/2*((yCoord-yPlayer) + (xCoord - xPlayer));
	}


	public void setCoord() {
		switch(d){
		case UP:
			yCoord--;
			break;

		case DOWN:
			yCoord++;
			break;

		case LEFT:
			xCoord++;
			break;


		case RIGHT:
			xCoord--;
			break;
		}
	}



	public void calc() {
		if(currentHP > 0){
			hit = false;
			xPlayer = maper.getCurrentX();
			yPlayer = maper.getCurrentY();
			if(yCoord > yPlayer && (maper.validMove(direction.UP.value,xCoord, yCoord) == BlockType.FLOOR||maper.validMove(direction.UP.value,xCoord, yCoord) == BlockType.CORRIDOOR)){
				//System.out.println("Word son, ima go UP");
				d = direction.UP;
			}
			else if(yCoord < yPlayer&& (maper.validMove(direction.DOWN.value,xCoord, yCoord) == BlockType.FLOOR||maper.validMove(direction.DOWN.value,xCoord, yCoord) == BlockType.CORRIDOOR)){
				d = direction.DOWN;
				//	System.out.println("Nigga i sure wanna move DOWN");
			}
			else if(xCoord > xPlayer){
				//	System.out.println("AYYYY Yo i wanna go RIGHT");
				d = direction.RIGHT;
			} 
			else if(xCoord < xPlayer){ 
				//System.out.println("Yo bitch please i wanna go LEFT");
				d = direction.LEFT;
			}

			if(abs(yCoord - yPlayer)==1 && xCoord == xPlayer || abs(xCoord - xPlayer)==1 && yCoord == yPlayer){
				System.out.println("Slime:" + id +" is attacking");
				attack = true;
				maper.character.health -= damage; 
				maper.log.add("Slime "+ id + "[0] hits your for [1]"+damage+"[2] damage[3]", 1);
				sq.reset();
				movement = false;
			}

			else if(!(maper.validMove(d.value,xCoord, yCoord) == BlockType.FLOOR||maper.validMove(d.value,xCoord, yCoord) == BlockType.CORRIDOOR)){
				movement = false;
				System.out.println("SHEEEET i can go thurr");
			}
			else{
				movement = true;
				setCoord();	
			}
		}	
		else{
			attack = false;
			movement = false;
			d = direction.NONE;
		}
	}

	private int abs(int x) {
		if(x < 0){
			return x*(-1);
		}
		return x;
	}

	//AttackSquare sq = new AttackSquare(ov,r); 
	public void animate(Canvas c, button pressed, int frames) {
		if(frames%4 == 1){

			update(); //updates the direction being faced.
		}
		//compensates for the movement of the map
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


		if(movement == true){
			switch(d){
			case UP:
				x -= xSpeed;
				y -= ySpeed;
				break;

			case DOWN:
				x += xSpeed;
				y += ySpeed;
				break;

			case LEFT:
				x -= xSpeed;
				y += ySpeed;
				break;


			case RIGHT:
				x += xSpeed;
				y -= ySpeed;
				break;
			}
		} 


		int srcY = frame * height;
		int srcX = currentFrame * width;

		Rect src = new Rect (srcX ,srcY, srcX + width, srcY + height);
		Rect dst = new Rect (x, y, x + width, y + height);
		c.drawBitmap(m, src, dst, null);

		if(attack == true){


			sq.animate(c);
			//			attack = false;
		}
	}

	private void update() {
		switch(d){
		case UP:
			frame = 0;
			break;

		case DOWN:
			frame = 1;
			break;

		case LEFT:
			frame = 3;
			break;

		case RIGHT:
			frame = 0;
			break;
		}

		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		currentFrame = ++currentFrame % 3; //
	}

	public int getCurrentX() {
		return xCoord;

	}

	public int getCurrentY() {
		return yCoord;
	}


	public boolean conflict(int x2, int y2) {
		return (xCoord == x2 && yCoord == y2);
	}

	public void idle(Canvas c) {
		int srcY = frame * height;
		int srcX = currentFrame * width;

		Rect src = new Rect (srcX ,srcY, srcX + width, srcY + height);
		Rect dst = new Rect (x, y, x + width, y + height);
		c.drawBitmap(m, src, dst, null);
		currentFrame = ++currentFrame % 3;
	}


	public void drawHealth(Canvas c) {
		Paint pc = new Paint();
		//	float totalHP = 100;
		//	float currentHP = 75;
		pc.setColor(Color.RED);
		c.drawRect(x-15 , y-20, x+35, y-10, pc); 
		pc.setColor(Color.GREEN);
		if(currentHP < 0){
			currentHP = 0; 
		}
		c.drawRect(x-15 , y-20, ((x+35) - (x-15))*(currentHP/totalHP)+x-15, y-10, pc); 
		//	c.drawRect(1)
		//c.drawBitmap(m, src, dst, null);
		//currentFrame = ++currentFrame % 3;

	}




}
