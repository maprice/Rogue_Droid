package com.mike.rogue;


import com.mike.rogue.Main.OurView;
import com.mike.rogue.Main.button;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class AttackSwipe {
	Bitmap m;
	OurView ov;
	int x, y;
	int height, width;
	int currentFrame = 0;
	int direction = 0;

	public AttackSwipe(OurView ourView, Resources r) {
		m = BitmapFactory.decodeResource(r, R.drawable.se_attackswipe);
		ov = ourView;
		height = m.getHeight() / 1; //5 rows
		width = m.getWidth() / 5; //5 columns
		x = 365 ;
		y = 185;
	} 


	final int tileWidth = 108;
	final int tileHeight = 54;



	public void reset() {
		currentFrame = 0;

	}

	public void animate(Canvas c) {
		currentFrame++;// = ++currentFrame;

		if(currentFrame <= 5){
			int srcY = direction * height;
			int srcX = currentFrame * width;




			Rect src = new Rect (srcX ,srcY, srcX + width, srcY + height);
			Rect dst = new Rect (x, y, x + width, y + height);
			c.drawBitmap(m, src, dst, null);
		}
	}

}
