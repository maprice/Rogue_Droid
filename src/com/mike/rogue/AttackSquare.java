package com.mike.rogue;


import com.mike.rogue.Main.OurView;
import com.mike.rogue.Main.button;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class AttackSquare {
	Bitmap k;
	OurView ov;
	int x, y;
	int height, width;
	int currentFrame = 0;
	int direction = 0;

	public AttackSquare(OurView ourView, Resources r) {
		k = BitmapFactory.decodeResource(r, R.drawable.se_attackbox);
		ov = ourView;
		height =  k.getHeight() / 1; //5 rows
		width = k.getWidth() / 5; //5 columns
		x = -100;
		y = -100;	
	}


	final int tileWidth = 108;
	final int tileHeight = 54;




	public void animate(Canvas c, int i, int j) {
		currentFrame = ++currentFrame % 5;
		int srcY = direction * height;
		int srcX = currentFrame * width;


		//	y = 210;// + tileHeight;//+100;
		//	x = 346;

		x = 346 + tileWidth/2*((j) - (i));
		y = 210 + tileHeight/2*((j) + (i));
		
		Paint p = new Paint();
		p.setAlpha(70);
		Rect src = new Rect (srcX ,srcY, srcX + width, srcY + height);
		Rect dst = new Rect (x, y, x + width, y + height);
		
		c.drawBitmap(k, src, dst, p);

	}

}
