package com.mike.rogue;

import com.mike.rogue.Main.OurView;
import com.mike.rogue.Main.button;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
	Bitmap walkImg,attackImg;
	OurView ov;
	int x, y;
	int height, width;
	int currentFrame = 0;
	int direction = 0;

	public Sprite(OurView ourView, Resources r) {
		// TODO Auto-generated constructor stub
		walkImg = BitmapFactory.decodeResource(r, R.drawable.cs_spritesheet);
		attackImg = BitmapFactory.decodeResource(r, R.drawable.cs_attacksheet);
		ov = ourView;
		height = walkImg.getHeight() / 4; //5 rows
		width = walkImg.getWidth() / 5; //5 columns
		x = 365 + width/2;
		y = 152 + height;

	}

	public void walk(Canvas c, button pressed, int frames) {

		if(frames%2 == 1){
			update(pressed);
		}
		int srcY = direction * height;
		int srcX = currentFrame * width;

		if(direction == 4){
			srcY = 1 * height;
			srcX = 3 * width;


		}
		Rect src = new Rect (srcX ,srcY, srcX + width, srcY + height);
		Rect dst = new Rect (x, y, x + width, y + height);
		c.drawBitmap(walkImg, src, dst, null);

	}

	public int getDirection(){
		return direction;

	}

	private void update(button pressed) {
		// TODO Auto-generated method stub
		switch(pressed){
		case UP:
			direction = 0;
			break;

		case DOWN:
			direction = 3;
			break;

		case LEFT:
			direction = 1;
			break;

		case RIGHT:
			direction = 2;
			break;

		case NONE:
			direction = 4;
			break;
		}

		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		currentFrame = ++currentFrame % 4;
	}


	public void face(Canvas c, button pressed) {
		if(pressed != button.NONE){
			update(pressed);
		}
		int srcY = direction * height;
		int srcX = 2 * width;
		Rect src = new Rect (srcX ,srcY, srcX + width, srcY + height);
		Rect dst = new Rect (x, y, x + width, y + height);
		c.drawBitmap(walkImg, src, dst, null);
	}

	public void resetFrame() {
		currentFrame = 0;
	}

	public void attack(Canvas c, button lastDirection) {
		update(lastDirection);
		int srcY = direction * height;
		int srcX = currentFrame * width;


		Rect src = new Rect (srcX ,srcY, srcX + width, srcY + height);
		Rect dst = new Rect (x, y, x + width, y + height);
		c.drawBitmap(attackImg, src, dst, null);
	}
}
