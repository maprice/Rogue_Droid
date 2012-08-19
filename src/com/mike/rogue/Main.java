package com.mike.rogue;

import items.Item;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import dungeonGen.Dungeon.BlockType;
import abilitys.AttackType;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;



public class Main extends Activity implements OnTouchListener {
	/** Called when the activity is first created. */
	OurView v;
	Bitmap joyPad, buttonA, buttonB, buttonC,buttonD,buttonS, grad, statusBar, skillMenu, emptySkill;
	Bitmap skillMenuA, skillMenuB, skillMenuC, skillMenuD, skillToolTip, floorBar, miniMap, miniMapHidden, logHidden;
	float x , y;
	float uX, uY;
 
	int screenHeight;
	int screenWidth;
	boolean hold = false;
	boolean init = false;
	boolean showMap = true;
	boolean showLog = true;
 Map map;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //Locks orientation to landscape
		

	        v = new OurView(this);//();
	        v.setOnTouchListener(this);
	     //   v.setOrientation
	   
		requestWindowFeature(Window.FEATURE_NO_TITLE); //hides the title
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //hides the status bar

		//v = new OurView(this);
		

		


	

		setContentView(v);

		Display display = getWindowManager().getDefaultDisplay(); 
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();

		System.out.println(screenWidth);
		System.out.println(screenHeight);
		
		  final Object xyz = getLastNonConfigurationInstance();
		    
		    // The activity is starting for the first time, load the photos from Flickr
		    if (xyz == null) {
		        map	= new Map(v, getResources(), 1);	
		    } else {
		        // The activity was destroyed/created automatically, populate the grid
		        // of photos with the images loaded by the previous activity
		     this.map = (Map)xyz;
		    }

	}

	@Override
	public void onBackPressed() {
	// do something on back.
		super.onBackPressed();
		System.out.println("Back button pressed");
		 this.finish();
		 System.exit(0);
		// this.onDestroy();
	return;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		v.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		v.resume();
	}


	
	@Override
	public Object onRetainNonConfigurationInstance() {
	    final Map[] list = new Map[1];
	list[0] = map;
	    return map;
	}
	


	@Override
	public boolean onTouch(View v, MotionEvent me) {
		switch (me.getAction()){
		case MotionEvent.ACTION_DOWN:
			x = me.getX();
			y = me.getY();
		break;
		case MotionEvent.ACTION_MOVE:
			x = me.getX();
			y = me.getY();
			hold = true;
			break;
		case MotionEvent.ACTION_UP: 
			uX = x;
			uY = y;
			x = 0;
			y = 0;
			hold = false;
		}
		return true;
	}
	

	public int getScreenOrientation()
	{
	    Display getOrient = getWindowManager().getDefaultDisplay();
	    int orientation = Configuration.ORIENTATION_UNDEFINED;
	    if(getOrient.getWidth()==getOrient.getHeight()){
	        orientation = Configuration.ORIENTATION_SQUARE;
	    } else{ 
	        if(getOrient.getWidth() < getOrient.getHeight()){
	            orientation = Configuration.ORIENTATION_PORTRAIT;
	        }else { 
	             orientation = Configuration.ORIENTATION_LANDSCAPE;
	        }
	    }
	    return orientation;
	}
	
	public enum button {
		UP (0), DOWN (1) , LEFT (2), RIGHT (3), NONE (4) , A (5),B (6),C (7),D (8) , S(9), MAP(10), LOG(11);  
		int value;
		button(int v){
			value = v;
		}
	}
	

	int FRAMES_PER_SECOND;
	int SKIP_TICKS;

	long next_game_tick;
	
	public class OurView extends SurfaceView implements Runnable{
		int setOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		Thread t = null;
		SurfaceHolder holder;
		boolean ok = false;

	//	Map map ;
		Menu inventory; 
		button pressed = button.NONE;
		button lastDirection = button.NONE;

		public OurView(Context context) {
			super(context);
			holder = getHolder();
		}



		



boolean loaded = false;
		boolean	menu = false;
	
		public void run() {
			Canvas c = null;

		//	if(!init){
			//	System.out.println("WTF");
			
			 inventory = new Menu(OurView.this, getResources(), map.character);
		//	 init = true;
		//	}
			//map.spawn(3);


			
			FRAMES_PER_SECOND = 25;
			SKIP_TICKS = 1000 / FRAMES_PER_SECOND;

			next_game_tick = SystemClock.uptimeMillis();
			// GetTickCount() returns the current number of milliseconds
			// that have elapsed since the system was started

			int sleep_time = 0;
			int idleClock = 0;
			if(  getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT){
				inventory.loadBitmaps();
			}
			else{
				loadBitmaps();
				changeSkills(button.NONE, 0);
			}

			
			while(ok == true){
			//	System.out.println(getScreenOrientation2());
				if(!holder.getSurface().isValid()){
					continue;	
				}
				//if(true){
		
				if(  getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT){
					
					
					
					
					if(loaded){
						System.out.println("LoadedBit");
					recycleBitmaps();
					inventory.loadBitmaps();
					loaded = false;
					} 
				//	inventory.loadBitmaps();
					c = holder.lockCanvas();
					inventory.display(c, x , y, uX, uY, hold);
					holder.unlockCanvasAndPost(c);
					
					
					
					 if ( idleClock == 10){
						 c = holder.lockCanvas();
		
						 inventory.animate(c);	
							
							holder.unlockCanvasAndPost(c);
						//	System.out.println("IDLEING");
						//System.out.println("Idling");
						idleClock = 0;

					}
					else{
						//	System.out.println("Ticking");
						idleClock++;
						next_game_tick += SKIP_TICKS;
						sleep_time = (int) (next_game_tick - SystemClock.uptimeMillis());
						if( sleep_time >= 0 ) {
							try {
								Thread.sleep(sleep_time);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						//idleClock++;
					}
					
				}
				else if(!loaded){
					inventory.recycleBitmaps();
					loadBitmaps();
					
					
				}
				
				else if(menu){
					//displayMenu(c);
					c = holder.lockCanvas();
					openSkillMenu(c);
					holder.unlockCanvasAndPost(c);
					c = holder.lockCanvas();
					openSkillMenu(c);
					holder.unlockCanvasAndPost(c);

					next_game_tick = SystemClock.uptimeMillis();

					while(menu){
						//openSkillMenu(c);
						drawToolTip(c);
					}
					//System.out.println("MENNUS");
				}

				else if(getInput(c)){
					execute(c);

				}
				else if ( idleClock == 10){

					drawIdle(c);	
					//	System.out.println("IDLEING");
					//System.out.println("Idling");
					idleClock = 0;

				}
				else{
					//	System.out.println("Ticking");
					idleClock++;
					next_game_tick += SKIP_TICKS;
					sleep_time = (int) (next_game_tick - SystemClock.uptimeMillis());
					if( sleep_time >= 0 ) {
						try {
							Thread.sleep(sleep_time);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					//idleClock++;
				}
			}
		}






public void resetTimer(){
	next_game_tick = SystemClock.uptimeMillis();
}












		private void drawIdle(Canvas c) {

			c = holder.lockCanvas();
			//	c.drawColor(Color.BLACK);

			map.drawIdle(c);


			drawControls(c);
			holder.unlockCanvasAndPost(c);

		}




		private void execute(Canvas c) {

			//if(holdTimer )
			switch(pressed){
			case UP: case DOWN: case LEFT: case RIGHT:
				moveChar(c);
				break;
			case A: 
				//	System.out.println("Execute Attack");


				map.attackType = map.character.currentSkills[0];
				attack(c);

				break;	

			case B: 

				map.attackType = map.character.currentSkills[1];
				attack(c);

				break;	

			case C: 

				map.attackType = map.character.currentSkills[2];
				attack(c);

				break;	

			case D: 

				map.attackType = map.character.currentSkills[3];
				attack(c);

				break;	
			case S: 
				loot(c);

				break;	
			case MAP: 
			 if(showMap){
				 showMap = false;
			 }
			 else{
				 showMap = true;
			 }
drawIdle(c);
				break;	
			case LOG: 
				 if(showLog){
						map.log.add("Combat Log Hidden[0]", 8);
					 showLog = false;
				 }
				 else{
					 map.log.add("Combat Log Shown[0]", 8);
					 showLog = true;
				 }
	drawIdle(c);
					break;	
			}
		}



		private void loot(Canvas c) {
			
			for (int i =0; i < map.loots.size(); i++){

				if(map.loots.get(i).xCoord == map.currentX && map.loots.get(i).yCoord == map.currentY && map.loots.get(i).type){
					map.loots.remove(i);
					Item newItem = new Item(v, getResources(), map.dunFloor);
					map.character.addItem(newItem); 
					map.log.add("You pick up a :"+newItem.name, newItem.quality);
					updateButton(c);
					drawIdle(c);
					return;
				}
				else if(map.loots.get(i).xCoord == map.currentX && map.loots.get(i).yCoord == map.currentY && (!map.loots.get(i).type)){
					map.loots.remove(i);
					int randInt = new Random().nextInt((int)(map.dunFloor*1.2))+map.dunFloor;
					map.character.gold += randInt;
					map.log.add("You pick up [0]"+randInt+" gold[1]", 3);
					updateButton(c);
					drawIdle(c);
					return;
					
				}
			}
		
		}



















		//FUGLY function
		private void attack(Canvas c) {
			int idleClock = 0;
			//	button attackSlot = pressed;

			buttonS.recycle();
			buttonS = BitmapFactory.decodeResource(getResources(), R.drawable.ui_buttona_spell);

			//pressed = button.NONE;
			button tempButton =   button.NONE;

			while(true){
				if(!holder.getSurface().isValid()){
					continue;	
				}
				getInput(c);

				if(map.attackType.empty && !hold){
					return;
				}
				if(pressed == button.S){
					if(map.attackType.mana > map.character.mana){
						map.log.add("You dont have enough[0] MP[1] to cast that[2]", 6);
						
						return;
					}
					map.character.mana = map.character.mana - map.attackType.mana;
					System.out.println(map.character.mana);
					map.sprite.resetFrame();
					for(int i = 0; i < 5; i++){


						c = holder.lockCanvas();

						map.attackAnimate(c, lastDirection);
						//	map.updateHealth(c, lastDirection);
						drawControls(c);
						holder.unlockCanvasAndPost(c);

						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					buttonS.recycle();
					buttonS = BitmapFactory.decodeResource(getResources(), R.drawable.ui_buttona);
					map.calcHit();
					
					for (int i =0; i < map.enemies.size(); i++){
						if(map.enemies.get(i).hit){
							 map.log.add("Your [0]"+  map.attackType.name +"[1] hits [2]Slime "+  map.enemies.get(i).id + "[3] for [4]" + (int) (map.attackType.damage +map.character.attack) +"[5]", 0);
						}
					}
					//woo
					for(int j = 0; j < 5; j++){
						c = holder.lockCanvas();

						//	map.attackAnimate(c, lastDirection);
						map.updateHealth(c, lastDirection);
						drawControls(c);
						holder.unlockCanvasAndPost(c);


						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}

					map.calcStats();
					for(int j = 0; j < 9; j++){
						c = holder.lockCanvas();

						map.moveEnemys(c, lastDirection, j);
						drawControls(c);
						holder.unlockCanvasAndPost(c);


						try {
							Thread.sleep(30);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
					//map.drawIdle(c);
					resetTimer();
					return;
				}

				if(pressed != tempButton){
					tempButton = pressed;

					c = holder.lockCanvas();
					map.drawAttackPreview(c, lastDirection);

					drawControls(c);
					drawManaPreview(c, map.attackType.mana);
					holder.unlockCanvasAndPost(c);
				}


				if (idleClock == 100){
					c = holder.lockCanvas();
					map.drawAttackPreview(c, lastDirection);
					drawControls(c);
					drawManaPreview(c, map.attackType.mana);
					holder.unlockCanvasAndPost(c);
					idleClock = 0;
				}

				if (cancelAttack == true){
					buttonS.recycle();
					buttonS = BitmapFactory.decodeResource(getResources(), R.drawable.ui_buttona);
					cancelAttack = false;
					resetTimer();
					return;
				}


				else{
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					idleClock++;
				}
			}
			
	
			
		} 


		Paint p = new Paint();
		public void drawControls(Canvas c){ 

			p.setAlpha(200);
			c.drawBitmap(grad, 0, 0, p);
			p.setAlpha(80);
			c.drawBitmap(joyPad, 10, 260, p);
			c.drawBitmap(buttonS, screenWidth-buttonS.getWidth(), screenHeight-buttonS.getHeight(), p);
		//	c.drawBitmap(buttonS, screenWidth-buttonS.getWidth(), screenHeight-buttonS.getHeight(), p);

			Paint pc = new Paint();


			pc.setAntiAlias(true);
			pc.setColor(Color.YELLOW);
			pc.setStyle(Paint.Style.STROKE); 
			pc.setStrokeWidth(7);

			
			RectF rectF = new RectF(26, 24+9, 25+73, 25+9+73);
			c.drawOval(rectF, pc);
			pc.setColor(Color.BLACK);
			c.drawArc (rectF, 90, 180*(map.character.xp/map.character.totalXp)-360, false, pc);


			p.setAlpha(250);
			c.drawBitmap(statusBar, 10, 5, p);
		//	c.drawBitmap(floorBar, (screenWidth/2)-(floorBar.getWidth()/2), screenHeight-floorBar.getHeight(), p);
			
			p.setAlpha(170);
			c.drawBitmap(buttonD, screenWidth-211, screenHeight-buttonA.getHeight()-2, p);
			c.drawBitmap(buttonA, screenWidth-buttonB.getWidth()-2, screenHeight-211, p);
			c.drawBitmap(buttonB, screenWidth-buttonC.getWidth()-71, screenHeight-buttonC.getHeight()-122, p);
			c.drawBitmap(buttonC, screenWidth-buttonD.getWidth()-122, screenHeight-buttonD.getHeight()-71, p);

			
			//	float totalHP = 100;
			//	float currentHP = 75;
			
			Paint pb = new Paint();
			pb.setColor(Color.BLACK);
			c.drawRect(116 , 34, 259, 43, pb); 
			pb.setColor(Color.GREEN);
			if(map.character.health < 0){
				map.character.health = 0; 
			}
			c.drawRect(116 , 34, ((259) - (116))*(map.character.health/map.character.totalHealth)+116,43, pb); 

			pb.setColor(Color.BLACK);
			c.drawRect(116 , 50, 259, 59, pb); 
			pb.setColor(Color.BLUE);
			if(map.character.mana < 0){
				map.character.mana = 0; 
			}
			c.drawRect(116 , 50, ((259) - (116))*(map.character.mana/map.character.totalMana)+116,59, pb); 

			Paint pt = new Paint();
			pt.setAntiAlias(true);
			pt.setTextSize(30);
			pt.setColor(Color.WHITE);
			c.drawText(Integer.toString(map.character.level), 55, 80, pt);

			
			pt.setTextSize(20);
			
			
			//  pc.setColor(Color.YELLOW);
			p.setAlpha(70);
			if(showLog){
			drawCombatLog(c);
			}
			else{
				c.drawBitmap(logHidden, (screenWidth-logHidden.getWidth())/2, screenHeight-logHidden.getHeight(), p);
			}

			//c.drawArc (rectF, 90, -270, false, pc);
			//c.drawArc(rectF, startAngle, sweepAngle, useCenter, paint)
			if(showMap){
			drawMiniMap(c);
			}
			else{
				c.drawBitmap(miniMapHidden, screenWidth-miniMap.getWidth(), 0, p);
			}
			c.drawText(Integer.toString(map.dunFloor), screenWidth-26, 25, pt);

		}




	
	
		// String a = "hey";
		

		private void drawCombatLog(Canvas c) {
			
		//	map.log.add("thing 1");
		//	map.log.add("thing 2");
		//	map.log.add("thing 3");
		//	map.log.add("thing 4");
			
			Paint pb = new Paint();
			pb.setColor(Color.BLACK);
			pb.setAlpha(100);
		//	c.drawRect(220 , screenHeight-92 - map.log.size()*20, screenWidth - 220, screenHeight, pb); 
			c.drawRect(220 , screenHeight - map.log.size()*23, screenWidth - 220, screenHeight, pb); 
			Paint pt = new Paint();
			pt.setAntiAlias(true);
			pt.setTextSize(16);
			pt.setColor(Color.WHITE);
			
			
			 
			//  c.drawString(s, rightEdge - fm.stringWidth(s), y);
			int colors[] = new int[6];
			for(int i = 0; i < map.log.size(); i++){
				//0 = attack
				//1 = attacked
				//2 = item loot
				//3 = gold loots
				String output = map.log.get(i);

				
			
			//	System.out.println(output);
			//	System.out.println(map.log.getType(i));
				switch(map.log.getType(i)){
				
				case 0: 
					
					// map.log.add("Your [0]"+  map.attackType.name +"[1] hits [2]Slime "+  map.enemies.get(i).id + "[3] for [4]" + (int) map.attackType.damage +"[5]", 0);
					colors[0] = Color.WHITE;
					colors[1] = Color.GREEN;
					colors[2] = Color.WHITE;
					colors[3] = Color.RED;
					colors[4] = Color.WHITE;
					colors[5] = Color.YELLOW;
				
					outputString(c, pt, output, colors, i);
				break;
				case 1: 
					colors[0] = Color.RED;
					colors[1] = Color.WHITE;
					colors[2] = Color.YELLOW;
					colors[3] = Color.WHITE;
				
					outputString(c, pt, output, colors, i);
				break;
				
				case 2:
					colors[0] = Color.RED;
					colors[1] = Color.WHITE;
					colors[2] =  Color.rgb(165,56,198);
				//	colors[3] = Color.WHITE;
					//log.add("Slime "+enemies.get(i).id+" dies, you gain 25xp", 2);
					outputString(c, pt, output, colors, i);
					break;
				
				case 3:
					colors[0] = Color.WHITE;
					colors[1] = Color.YELLOW;
				//	colors[2] =  Color.rgb(165,56,198);
				//	colors[3] = Color.WHITE;
					//log.add("Slime "+enemies.get(i).id+" dies, you gain 25xp", 2);
					outputString(c, pt, output, colors, i);
					break;
					
				case 4:
					colors[0] = Color.WHITE;
					colors[1] = Color.YELLOW;
					colors[2] = Color.WHITE;
				//	colors[3] = Color.WHITE;
					//log.add("Slime "+enemies.get(i).id+" dies, you gain 25xp", 2);
					outputString(c, pt, output, colors, i);
					break;
					
					 
				 case 5:
					pt.setColor(Color.WHITE);
					c.drawText(map.log.get(i), 225, screenHeight+ 10 +20*i - 20*map.log.size(), pt);
					break;
					
				 case 6:
						colors[0] = Color.WHITE;
						colors[1] = Color.BLUE;
						colors[2] = Color.WHITE;
						//log.add("Slime "+enemies.get(i).id+" dies, you gain 25xp", 2);
						outputString(c, pt, output, colors, i);
						break;
				 case 8:
						colors[0] = Color.GRAY;
						//log.add("Slime "+enemies.get(i).id+" dies, you gain 25xp", 2);
						outputString(c, pt, output, colors, i);
						break;
					default:
						
					//	System.out.println
						
						pt.setColor(Color.WHITE);
						pt.getFontSpacing();
						c.drawText(output.substring(0, output.indexOf(':')), 225, screenHeight+ 10 +20*i - 20*map.log.size(), pt);
						pt.setColor(map.log.getType(i));
						c.drawText(output.substring(output.indexOf(':')+1, output.length()), 225 +pt.measureText(output.substring(0, output.indexOf(':'))) , screenHeight+ 10 +20*i - 20*map.log.size(), pt);						 
				}
				
		
		}
		}




		//		maper.log.add("Slime "+ id + "[0] hits your for [1]"+damage+"[2] damage[3]", 1);
		private void outputString(Canvas c, Paint pt, String output,int[] colors, int index) {

			float start = 0;
			String s = output;

			for(int i = 0; output.indexOf("["+i+"]") != -1; i++){
			//	System.out.println(output.indexOf("["+i+"]"));
			//	System.out.println("OH HAI");
				// pt.measureText(output.substring(0, output.indexOf("["+i+"]"))
				pt.setColor(colors[i]);
			//	pt.setColor(Color.WHITE);
				c.drawText(s.substring(0, s.indexOf("["+i+"]")), 225+start, screenHeight+ 10 +20*index - 20*map.log.size(), pt);
				start += pt.measureText(s.substring(0, s.indexOf("["+i+"]")));
				s = s.substring(s.indexOf("["+i+"]")+3, s.length());
			}

		}






		private void drawMiniMap(Canvas c) {
			Paint pm = new Paint();
			
			pm.setColor(Color.BLACK);
			pm.setAlpha(255);
			   c.drawCircle(screenWidth-(miniMap.getWidth()/2)+20, 100, 90, p);
		
		
		
			pm.setColor(Color.BLACK);
			pm.setAlpha(160);
			//c.drawRect(screenWidth-175,0, screenWidth, 175, pm);
			
			pm.setColor(Color.DKGRAY);
			
			for(int j =  map.currentX-15; j<  map.currentX+15; j++){
			for(int i = map.currentY-15; i<  map.currentY+15; i++){
				if(i >= 0 && j >= 0 && i < map.d.visibility.length &&  j < map.d.visibility.length){
					if (map.d.visibility[j][i] == BlockType.FLOOR || map.d.visibility[j][i] == BlockType.CORRIDOOR){
						pm.setColor(Color.DKGRAY);
					c.drawRect(screenWidth-87 + ((i-map.currentY)*5),100 + ((j-map.currentX)*5), screenWidth - 87 + ((i-map.currentY)*5 + 5), 100 + ((j-map.currentX)*5 + 5), pm);
					}
					else if (map.d.visibility[j][i] == BlockType.STAIRDD 
							|| map.d.visibility[j][i] == BlockType.STAIRDL
							|| map.d.visibility[j][i] == BlockType.STAIRDR
							|| map.d.visibility[j][i] == BlockType.STAIRDT
							|| map.d.visibility[j][i] == BlockType.STAIRUD 
							|| map.d.visibility[j][i] == BlockType.STAIRUL
							|| map.d.visibility[j][i] == BlockType.STAIRUR
							|| map.d.visibility[j][i] == BlockType.STAIRUT){
						pm.setColor(Color.GREEN);
						c.drawRect(screenWidth-87 + ((i-map.currentY)*5),100 + ((j-map.currentX)*5), screenWidth - 87 + ((i-map.currentY)*5 + 5), 100 + ((j-map.currentX)*5 + 5), pm);
						}
				}
			}
					//c.drawRect(0 , 0, 800, 480, p); 
		}
			
			c.drawBitmap(miniMap, screenWidth-miniMap.getWidth(), 0, null);
			pm.setColor(Color.RED);
			c.drawRect(screenWidth-(87) ,100 , screenWidth-(87)+5 ,100 +5 , pm);
		}






		private void moveChar(Canvas c) {


			int FRAMES_PER_SECOND = 30;
			int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;

			long next_game_tick = SystemClock.uptimeMillis();
			// GetTickCount() returns the current number of milliseconds
			// that have elapsed since the system was started

			int sleep_time = 0;

			map.calcMovement(pressed); //think
			for(int i = 0; i < 9; i++){


				c = holder.lockCanvas();

				//	c.drawColor(Color.BLUE);


				map.animateMovement(c, pressed, i);
				drawControls(c);

				holder.unlockCanvasAndPost(c);

				next_game_tick += SKIP_TICKS;
				sleep_time = (int) (next_game_tick - SystemClock.uptimeMillis());
				if( sleep_time >= 0 ) {
					try {
						Thread.sleep(sleep_time);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}


			}

			if(map.checkSpace()==0||map.checkSpace()==1){
				for(int i = 0; i < 10; i++){


					c = holder.lockCanvas();

					//	c.drawColor(Color.BLUE);


					map.drawIdle(c);
					fadeOut(c, i);
					drawControls(c);

					holder.unlockCanvasAndPost(c);

					next_game_tick += SKIP_TICKS;
					sleep_time = (int) (next_game_tick - SystemClock.uptimeMillis());
					if( sleep_time >= 0 ) {
						try {
							Thread.sleep(sleep_time);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}


				}
				if(map.checkSpace()==0){
					map.levelUp(c);
					map.center(map.currentX, map.currentY, true);
				}else if (map.checkSpace()==1){
					map.levelDown(c);
					map.center(map.currentX, map.currentY, true);
				}

			} 

			
			updateButton(c);
			map.center(map.currentX, map.currentY, false);
			resetTimer();

		}

		
		private void updateButton(Canvas c){
			for (int i =0; i < map.loots.size(); i++){

				if(map.loots.get(i).xCoord == map.currentX && map.loots.get(i).yCoord == map.currentY){
					buttonS.recycle();
					buttonS = BitmapFactory.decodeResource(getResources(), R.drawable.ui_buttona_loot);
				drawIdle(c);
					return;
				}
			}
			buttonS.recycle();
			buttonS = BitmapFactory.decodeResource(getResources(), R.drawable.ui_buttona);
			
		}


		private void fadeOut(Canvas c, int i) {



			Paint p = new Paint();
			p.setColor(Color.BLACK);
			if((i+1)*26 > 255){
				p.setAlpha(255);
			}
			else{
				p.setAlpha((i+1)*26);
				//c.drawRect(0 , 0, 800, 480, p); 
			}
			c.drawRect(0 , 0, 800, 480, p); 




		}










		private boolean within(int bX1, int bY1, int bX2, int bY2,  boolean down) {
			int height = bY2 - bY1;
			int width = bX2 - bX1;
			if(down){
				return (y> bY1 && y < bY1+height && x > bX1 && x < bX1 + width );
			}
			else{
				return (uY> bY1 && uY < bY1+height && uX > bX1 && uX < bX1 + width );
			}
		}





		public boolean within(Bitmap b, int bX, int bY, boolean down){
			int height = b.getHeight();
			int width = b.getWidth();
			if(down){
				return (y> bY && y < bY+height && x > bX && x < bX + width );
			}
			else{
				return (uY> bY && uY < bY+height && uX > bX && uX < bX + width );
			}

		}


		int holdTimer = 0;
		boolean cancelAttack = false;
		//Deciphers which button the user is touching, sets the button field and returns true, false otherwise
		private boolean getInput(Canvas c) {

			if(!hold){
				holdTimer = 0;
				if(within(buttonA, screenWidth-buttonB.getWidth()-2,screenHeight-211, true)){

					pressed = button.A;	 
					//	lastDirection = button.A;
					System.out.println("A");
					hold=true;
					return true;
				}
				else if(within(buttonB,screenWidth-buttonC.getWidth()-71, screenHeight-buttonC.getHeight()-122, true)){

					pressed = button.B;	 
					//	lastDirection = button.B;
					System.out.println("B");
					hold=true;
					return true;
				}
				else if(within(buttonC, screenWidth-buttonD.getWidth()-122,  screenHeight-buttonD.getHeight()-71, true)){

					pressed = button.C;	 
					//	lastDirection = button.C;
					System.out.println("C");
					hold=true;
					return true;
				}
				else if(within(buttonC,screenWidth-211,  screenHeight-buttonA.getHeight()-2, true)){

					pressed = button.D;	 
					//	lastDirection = button.D;
					System.out.println("D");
					hold=true;
					return true;
				}
				else if(within(buttonS,screenWidth-buttonS.getWidth()+50, screenHeight-buttonS.getHeight()+50, true)){
					pressed = button.S;	 
					System.out.println("S");
					hold=true;
					return true;
				}
				else if(within(miniMap, screenWidth-miniMap.getWidth(), 0, true)){
					pressed = button.MAP;	 
					System.out.println("MAP");
					hold=true;
					return true;
				}
				else if(within(220, screenHeight - 4*23, screenWidth - 220 ,screenHeight, true)){
					
				//	c.drawRect(220 , screenHeight - map.log.size()*23, screenWidth - 220, screenHeight, pb); 
					//c.drawRect(220 , screenHeight - map.log.size()*23, screenWidth - 220, screenHeight, pb); 
					
					pressed = button.LOG;	 
					System.out.println("LOG");
					hold=true;
					return true;
				}

			}
			else{
				holdTimer++;
			}
			if(holdTimer == 500){
				//pressed = button.NONE;	 
				cancelAttack = true;
				menu = true;
				return false;
				//	System.out.println("Open Menu");
			}



			if(x > 10 + 60 && x < joyPad.getWidth() - 60){
				if(y > 260 && y < 260 + 60 ){
					pressed = button.UP;
					lastDirection = button.UP;
					return true;
				}
				else if(y > 260 + joyPad.getHeight() - 60){
					pressed =  button.DOWN;
					lastDirection = button.DOWN;
					return true;
				}
			}
			else if(y < 260 + joyPad.getHeight() - 60 && y > 260 + 60){
				if(x < 10 + 60 && x > 10){
					pressed =  button.LEFT;
					lastDirection = button.LEFT;
					return true;

				}
				else if(x > 10 + joyPad.getWidth() - 60 && x < 10 + joyPad.getWidth()){
					pressed =  button.RIGHT;
					lastDirection = button.RIGHT;
					return true;

				}	
			}



			else{
				//	pressed = button.NONE;	
			}
			return false;
		}









		boolean drawn = false;

		int current = -1;

		boolean update = true;

		int skill = -1;
		private void openSkillMenu(Canvas ca) {







			//	holder = getHolder();
			//	ca.restore();

			//	if( ! drawn){
			//	ca = holder.lockCanvas();
			//Paint p = new Paint();

			//	c.drawBitmap(skillMenu, -20, 70, p);
			//System.out.println(pressed);
			skillToolTip = BitmapFactory.decodeResource(getResources(), R.drawable.ui_skilltooltipp);


			switch(pressed){
			case A: 
				skillMenu = BitmapFactory.decodeResource(getResources(), R.drawable.ui_skillmenua);
				break;	
			case B: 
				skillMenu = BitmapFactory.decodeResource(getResources(), R.drawable.ui_skillmenub);
				break;
			case C: 
				skillMenu = BitmapFactory.decodeResource(getResources(), R.drawable.ui_skillmenuc);
				break;
			case D: 
				skillMenu = BitmapFactory.decodeResource(getResources(), R.drawable.ui_skillmenud);
				break;	
			}

			p.setAlpha(100);
			ca.drawBitmap(skillMenu, screenWidth-skillMenu.getWidth(), screenHeight-skillMenu.getHeight(), p);


			ca.drawBitmap(map.character.allSkills[0].getIcon(), screenWidth-buttonA.getHeight()-2, screenHeight-285,  null);
			ca.drawBitmap(map.character.allSkills[1].getIcon(), screenWidth-buttonD.getWidth()-80, screenHeight-buttonD.getHeight()-200, null);
			ca.drawBitmap(map.character.allSkills[2].getIcon(), screenWidth-buttonD.getWidth()-150, screenHeight-buttonD.getHeight()-150, null);
			ca.drawBitmap(map.character.allSkills[3].getIcon(), screenWidth-buttonD.getWidth()-200, screenHeight-buttonD.getHeight()-80, null);
			ca.drawBitmap(map.character.allSkills[4].getIcon(), screenWidth-285, screenHeight-buttonA.getHeight()-2, null);





			p.setAlpha(170);
			ca.drawBitmap(buttonD, screenWidth-211, screenHeight-buttonA.getHeight()-2, p);
			ca.drawBitmap(buttonA, screenWidth-buttonB.getWidth()-2, screenHeight-211, p);
			ca.drawBitmap(buttonB, screenWidth-buttonC.getWidth()-71, screenHeight-buttonC.getHeight()-122, p);
			ca.drawBitmap(buttonC, screenWidth-buttonD.getWidth()-122, screenHeight-buttonD.getHeight()-71, p);
			//holder.unlockCanvasAndPost(ca);
			//drawn = true;
			//}




			//drawn = true;




			/*


			 */



		}



		private void drawToolTip(Canvas ca) {
			if(skill != current && ! map.character.allSkills[skill].empty){

				ca = holder.lockCanvas();

				//c.drawColor(Color.BLACK);
				// c = holder.lockCanvas();
				Paint pt = new Paint();
				pt.setAntiAlias(true);
				pt.setTextSize(30);
				pt.setColor(Color.BLACK);



				//
				p.setAlpha(160);
				ca.drawBitmap(skillToolTip, screenWidth-skillToolTip.getWidth()-234, screenHeight-skillToolTip.getHeight(), null);

				//	  pt.setTextAlign(Paint.Align.CENTER);

				ca.drawText(map.character.allSkills[skill].name, 230, 350, pt);
				//	
				current = skill;




				holder.unlockCanvasAndPost(ca);


			}





			//	drawn = false;


			if(within(emptySkill, screenWidth-285,screenHeight-buttonA.getHeight()-2, true)){
				skill = 4;
				update = true;
				//	updateToolTip(c, 4);

			}
			else if(within(emptySkill, screenWidth-buttonD.getWidth()-200,screenHeight-buttonD.getHeight()-80, true)){
				skill = 3;
				update = true;
				//updateToolTip(c, 3);
			}
			else if(within(emptySkill, screenWidth-buttonA.getHeight()-2, screenHeight-buttonD.getHeight()-200, true)){
				skill = 0;
				update = true;
				//updateToolTip(c, 2);
			}
			else if(within(emptySkill, screenWidth-buttonD.getWidth()-80, screenHeight-buttonD.getHeight()-200, true)){
				skill = 1;
				update = true;
				//updateToolTip(c, 1);
			}
			else if(within(emptySkill, screenWidth-buttonD.getWidth()-150, screenHeight-buttonD.getHeight()-150, true)){
				skill = 2;
				update = true;
				//	updateToolTip(c, 0);
			}


			if(!hold){
				skillMenu.recycle();
				skillToolTip.recycle();
				//System.out.println("pop");

				if(within(emptySkill, screenWidth-285,screenHeight-buttonA.getHeight()-2, false)){
					System.out.println("5");
					changeSkills(pressed, 4);
				}
				else if(within(emptySkill, screenWidth-buttonD.getWidth()-200,screenHeight-buttonD.getHeight()-80, false)){
					System.out.println("4");
					changeSkills(pressed, 3);
				}
				else if(within(emptySkill, screenWidth-buttonA.getHeight()-2, screenHeight-buttonD.getHeight()-200, false)){
					System.out.println("1");
					changeSkills(pressed, 0);
				}
				else if(within(emptySkill, screenWidth-buttonD.getWidth()-80, screenHeight-buttonD.getHeight()-200, false)){
					System.out.println("2");
					changeSkills(pressed, 1);
				}
				else if(within(emptySkill, screenWidth-buttonD.getWidth()-150, screenHeight-buttonD.getHeight()-150, false)){
					System.out.println("3");
					changeSkills(pressed, 2);
				}

				drawn = false;
				menu = false;
			}
		}




		private void changeSkills(button pressed, int i) {

			AttackType temp;
			switch(pressed){
			case A: 
				temp = map.character.allSkills[i];
				map.character.allSkills[i] = map.character.currentSkills[0] ;
				map.character.currentSkills[0] = temp;
				break;	
			case B: 
				temp = map.character.allSkills[i];
				map.character.allSkills[i] = map.character.currentSkills[1] ;
				map.character.currentSkills[1] = temp;
				break;
			case C: 
				temp = map.character.allSkills[i];
				map.character.allSkills[i] = map.character.currentSkills[2] ;
				map.character.currentSkills[2] = temp;
				break;
			case D: 
				temp = map.character.allSkills[i];
				map.character.allSkills[i] = map.character.currentSkills[3] ;
				map.character.currentSkills[3] = temp;
				break;	
			}

			buttonA = map.character.currentSkills[0].getIcon();
			buttonB = map.character.currentSkills[1].getIcon();
			buttonC = map.character.currentSkills[2].getIcon();
			buttonD = map.character.currentSkills[3].getIcon();

		}



		public void pause(){
			ok = false;
			while(true){
				try{
					t.join();
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
				break;
			}
			t = null;
		}

		public void resume(){
			ok = true;
			t = new Thread(this);
			t.start();
		}



		public void drawManaPreview(Canvas c, float mana) {

			if(mana > map.character.mana){
				return;
			}
			Paint pc = new Paint();
			pc.setAlpha(255);
			//  pc.setColor(Color.BLACK);
			//	c.drawRect(116 , 50, 259, 59, pc); 
			pc.setColor(Color.YELLOW);
			if(map.character.mana < 0){
				map.character.mana = 0; 
			}
			c.drawRect(((259) - (116))*(map.character.mana/map.character.totalMana)+116 - (((259) - (116))*(mana/map.character.totalMana)) , 50, ((259) - (116))*(map.character.mana/map.character.totalMana)+116,59, pc); 

		}
		
		private void loadBitmaps() {
			loaded = true;
			joyPad = BitmapFactory.decodeResource(getResources(), R.drawable.ui_joypad);
			statusBar = BitmapFactory.decodeResource(getResources(), R.drawable.ui_statusbar);
			grad = BitmapFactory.decodeResource(getResources(), R.drawable.ui_gradient);
			buttonS = BitmapFactory.decodeResource(getResources(), R.drawable.ui_buttona);
		//	floorBar = BitmapFactory.decodeResource(getResources(), R.drawable.ui_floorbar);
			miniMap = BitmapFactory.decodeResource(getResources(), R.drawable.ui_minimap);
			miniMapHidden = BitmapFactory.decodeResource(getResources(), R.drawable.ui_minimap_hidden);
			logHidden = BitmapFactory.decodeResource(getResources(), R.drawable.ui_log_hidden);
			map.loadBitmaps();
			
			

		//	buttonA = BitmapFactory.decodeResource(getResources(), R.drawable.si_frostbite);
		//	buttonB = BitmapFactory.decodeResource(getResources(), R.drawable.si_firestorm);
		//	buttonC = BitmapFactory.decodeResource(getResources(), R.drawable.si_drainlife);
		//	buttonD = BitmapFactory.decodeResource(getResources(), R.drawable.si_lightning);
			emptySkill = BitmapFactory.decodeResource(getResources(), R.drawable.si_emptyskill);






			
			
		}
		
		public void recycleBitmaps() {
			loaded = false;
			joyPad.recycle();
			statusBar.recycle();
			grad.recycle();
			buttonS.recycle();
		//	floorBar.recycle();
			miniMap.recycle();
			miniMapHidden.recycle();
			logHidden.recycle();
			map.recycleBitmaps();
			

		//	buttonA.recycle();
			//buttonB.recycle();
		//buttonC.recycle();
			//buttonD.recycle();
			emptySkill.recycle();
loaded = false;





			
		}

	}

	}







