package com.mike.rogue;



import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;


import com.mike.rogue.Main.OurView;
import com.mike.rogue.Main.button;

import dungeonGen.Dungeon.BlockType;

public class MapChunks{


	final int tileWidth = 108;
	final int tileHeight = 54;
	final int xScale = tileWidth/2;
	final int YScale = tileHeight/2;

	private BlockType[][] g;

	private int centerX;
	private int centerY; 

	final int buffer = 30;
	final int pixelBufferX = 1800;
	final int pixelBufferY = 960;

	int x = 0;
	int y = 0;

	private Bitmap NU, FL, EN, EX, NE, NW, SE, SW, TC, LC, RC, BC;
	private Bitmap NED, NWD, SED, SWD;
	private Bitmap NEDF, NWDF, SEDF, SWDF;
	private Bitmap NEF, NWF, SEF, SWF, TCF, LCF, RCF, BCF;

	private Bitmap mCBitmap;
	private Bitmap floorChunk, wallChunk;
	private Bitmap nextBlock;
	Paint p;


	public MapChunks(OurView ourView, Resources r, BlockType[][] grid, int startX, int startY) {

		NU = BitmapFactory.decodeResource(r, R.drawable.t00null);
		FL = BitmapFactory.decodeResource(r, R.drawable.t11empty);

		EX = BitmapFactory.decodeResource(r, R.drawable.t03stairdown);
		EN = BitmapFactory.decodeResource(r, R.drawable.t02stairup);
		NE = BitmapFactory.decodeResource(r, R.drawable.t01newall);
		NW = BitmapFactory.decodeResource(r, R.drawable.t01nwwall);
		SE = BitmapFactory.decodeResource(r, R.drawable.t01sewall);
		SW = BitmapFactory.decodeResource(r, R.drawable.t01swwall);

		TC = BitmapFactory.decodeResource(r, R.drawable.t01tcwall);
		LC = BitmapFactory.decodeResource(r, R.drawable.t01lcwall);
		RC = BitmapFactory.decodeResource(r, R.drawable.t01rcwall);
		BC = BitmapFactory.decodeResource(r, R.drawable.t01bcwall);

		NEF = BitmapFactory.decodeResource(r, R.drawable.t01newallf);
		NWF= BitmapFactory.decodeResource(r, R.drawable.t01nwwallf);
		SEF = BitmapFactory.decodeResource(r, R.drawable.t01sewallf);
		SWF = BitmapFactory.decodeResource(r, R.drawable.t01swwallf);
		TCF = BitmapFactory.decodeResource(r, R.drawable.t01tcwallf);
		LCF = BitmapFactory.decodeResource(r, R.drawable.t01lcwallf);
		RCF = BitmapFactory.decodeResource(r, R.drawable.t01rcwallf);
		BCF = BitmapFactory.decodeResource(r, R.drawable.t01bcwallf);

		SED = BitmapFactory.decodeResource(r, R.drawable.t01sedoor);
		SWD = BitmapFactory.decodeResource(r, R.drawable.t01sedoor);

		SEDF = BitmapFactory.decodeResource(r, R.drawable.t01sedoorf);
		SWDF = BitmapFactory.decodeResource(r, R.drawable.t01swdoorf);

		p = new Paint();
		p.setAlpha(200);

		centerX = startX;
		centerY = startY;
		floorChunk = saveMap(grid, startX, startY);
		wallChunk = saveWalls(grid, startX, startY);
		g = grid;


	}


	private boolean exists(int x, int y, int row, int col) {
		return(x>=0 && x <row && y >=0 && y < col);
	}

	public Bitmap saveMap(BlockType[][] currentMap, int xCoord, int yCoord){

		mCBitmap = Bitmap.createBitmap(pixelBufferX, pixelBufferY, Bitmap.Config.RGB_565);
		Canvas tCanvas = new Canvas(mCBitmap);

		for(int j = 0; j < buffer; j++){
			for(int i = 0; i < buffer; i++){
				if(exists(j+xCoord-(buffer/2),i+yCoord - (buffer/2),25,25)){
					switch(currentMap[j+xCoord-(buffer/2)][i+yCoord - (buffer/2)]){
					case NOTHING:
						nextBlock = null;
						break;
					case FLOOR: 		case CORRIDOOR:
						nextBlock = FL;
						break;
					case NWWALL: 
						nextBlock = NW;
						break;
					case UDDOOR:
						nextBlock = SED;
						break;	
					case LRDOOR:
						nextBlock = SWD;
						break;	
					case NEWALL:
						nextBlock = NE;
						break;
					case SEWALL:
						nextBlock = SE;
						break;
					case SWWALL:
						nextBlock = SW;
						break;					
					case TOPCORNER:
						nextBlock = TC;
						break; 
					case LEFTCORNER:
						nextBlock = LC;
						break; 
					case RIGHTCORNER:
						nextBlock = RC;
						break; 
					case BOTTOMCORNER:
						nextBlock = BC;
						break; 
					case STAIRUT: case STAIRUD: case STAIRUL: case STAIRUR: 
						nextBlock = EN;
						break;
					case STAIRDT: case STAIRDD: case STAIRDL: case STAIRDR: 
						nextBlock = EX;
						break;
					default:
						nextBlock = NU;
					} 
					/*if(i == buffer/2  && j == buffer/2){
						nextBlock = EN;
					}
					 */
					if(nextBlock != null){
						tCanvas.drawBitmap(nextBlock, xScale * (i - j) + xScale*((buffer/2)-1), YScale * (i + j)- YScale*((buffer/2)-1), null);
					}
				}
			}
		}
		return mCBitmap;
	}


	public void drawMap(Canvas c, button pressed, boolean movement){
		final int xSpeed = 6;
		final int ySpeed = 3;

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
	c.drawBitmap(floorChunk, x-(pixelBufferX/4 - 40 ), y-(pixelBufferY/4+40), null);
	}

	public void drawWalls(Canvas c){
		c.drawBitmap(wallChunk, x-(pixelBufferX/4 -40), y-(pixelBufferY/4+40), p);
	}

	public Bitmap saveWalls(BlockType[][] currentMap, int xCoord, int yCoord){

		mCBitmap = Bitmap.createBitmap(pixelBufferX, pixelBufferY, Bitmap.Config.ARGB_8888);
		Canvas tCanvas = new Canvas(mCBitmap);

		for(int j = 0; j < buffer; j++){
			for(int i = 0; i < buffer; i++){
				if(exists(j+xCoord-(buffer/2),i+yCoord - (buffer/2),25,25)){
					switch(currentMap[j+xCoord-(buffer/2)][i+yCoord - (buffer/2)]){

					case NEWALL:
						nextBlock = NEF;
						break;
					case NWWALL:
						nextBlock = NWF;
						break;	
					case SEWALL:
						nextBlock = SEF;
						break;
					case SWWALL:
						nextBlock = SWF;
						break;					
					case TOPCORNER:
						nextBlock = TCF;
						break; 
					case LEFTCORNER:
						nextBlock = LCF;
						break; 
					case RIGHTCORNER:
						nextBlock = RCF;
						break; 
					case BOTTOMCORNER:
						nextBlock = BCF;
						break; 
				case UDDOOR:
						nextBlock = SEDF;
						break; 
					case LRDOOR:
						nextBlock = SWDF;
						break;	
						
				/*		
					case UDDOOR:
						nextBlock = NU;
						break;	
					case LRDOOR:
						nextBlock = NU;
						break;	
					*/	
					default:
						nextBlock = NU;
					}

					if(nextBlock != null){
						tCanvas.drawBitmap(nextBlock, xScale * (i - j) + xScale*((buffer/2)-1), YScale * (i + j)- YScale*((buffer/2)-1), null);
					}
				}
			}
		}
		return mCBitmap;
	}


	private int abs(int x) {
		if(x < 0){
			return x*(-1);
		}
		return x;
	}
 




	public void center( BlockType[][] visibility, int xCoord, int yCoord, boolean door) {
		if(abs(xCoord-centerX) > 5 || abs(yCoord-centerY) > 5 || door){
			
			
			x = 0;
			y = 0;
			centerX = xCoord;
			centerY = yCoord;

			floorChunk.recycle();
			wallChunk.recycle();
			mCBitmap.recycle();
			floorChunk = saveMap(visibility, xCoord, yCoord);
			wallChunk = saveWalls(visibility, xCoord, yCoord);
			System.gc();
		}
	//	if(door){
	//		g = visibility;
//
		
	//	}
	}
}