package com.mike.rogue;


import items.Item;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import com.mike.rogue.Main.OurView;
import com.mike.rogue.Main.button;

import dungeonGen.Dungeon;
import dungeonGen.Dungeon.BlockType;
import dungeonGen.Room;

import abilitys.AttackType;
import abilitys.Basic;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

final public class Map {

	int x, y;

	Bitmap NU, FL, EN, EX, NE, NW, SE, SW, TC, LC, RC, BC;
	Bitmap NED, NWD, SED, SWD;
	Bitmap NEDF, NWDF, SEDF, SWDF; 
	Bitmap NEF, NWF, SEF, SWF, TCF, LCF, RCF, BCF;
	OurView ov;

	Resources re;
	final int tileWidth = 108;
	final int tileHeight = 54;
	final int xScale = tileWidth/2;
	final int YScale = tileHeight/2;
	Bitmap nextBlock = NU;

	Bitmap mCBitmap;
	Bitmap map, walls;
//	Bitmap HIGH;

	int startRow;
	int startCol;

	int currentX = startRow;
	int currentY = startCol;

	Sprite sprite; 
	Stats character; 
	CombatLog log;

	List<Enemy> enemies;
	List<GroundLoot> loots;

	Stack<Long> pastSeed = new Stack<Long>();

	//List<Bitmap> mapSquares;

	boolean movement = false;

	int dunFloor = 1;
	
	
	
	
	Dungeon.BlockType[][] tileMap;
	//AttackSquare square;
	/*
	int[][] tileMap = {
			{ 6, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0},
			{ 4, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0},
			{ 4, 1, 1, 1, 1, 1, 0, 0, 6, 5, 5, 5},
			{ 4, 1, 1, 1, 1, 1, 0, 0, 4, 1, 1, 1},
			{ 4, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1},
			{ 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{ 4, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1},
			{ 4, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1},
			{ 4, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1},
			{ 4, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0},
			{ 4, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0},
			{ 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{ 0, 0, 1, 0, 0, 0, 0, 0, 6, 5, 5, 0},
			{ 0, 0, 1, 0, 0, 0, 0, 0, 4, 1, 1, 0},
			{ 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
			{ 0, 0, 0, 0, 0, 0, 0, 0, 4, 1, 1, 0},
			{ 0, 0, 0, 0, 0, 0, 0, 0, 4, 1, 1, 0},


	};
	 */




	Dungeon d;
	/*
	int[][] tileMap2 = {
			{ 6, 5, 5, 3, 5},
			{ 4, 1, 1, 1, 1},
			{ 4, 1, 1, 1, 1},
			{ 4, 1, 1, 1, 1}
	};
	 */
	Dungeon.BlockType[][] currentMap;


	AttackType attackType;


	MapChunks m;

	 public Map(OurView ourView, Resources r,int level) {
		// TODO Auto-generated constructor stub
		ov = ourView;

	
		mCBitmap = null;//BitmapFactory.decodeResource(r, R.drawable.tile4);

		enemies = new ArrayList<Enemy>();
		loots = new ArrayList<GroundLoot>();
		//	mapSquares = new ArrayList<Bitmap>();

		x = y = 0;
		re = r;

		d = new Dungeon();
		d.printGrid();

		System.out.println("CONSTRUCTYEFDDD");

		currentMap = d.grid;
	//	currentMap = tileMap;

		startRow = d.startY;
		startCol = d.startX;
		// 21 5
		// startRow = 5;
		//	 startCol = 20;

		currentX = startRow;
		currentY = startCol;

	//	System.out.println(startRow+"     "+ startCol);
		sprite = new Sprite(ov,re);	


		character = new Stats(ov, re);

		attackType = new Basic(ov, re);

		m = new MapChunks(ourView, r, d.visibility, startRow, startCol);
		log = new CombatLog();
		
		
		 Item newItem = new Item(ourView, r, 4);
		character.addItem(newItem); 
		
	}
	



	public BlockType[][] getTiles(){
		return currentMap;
	}

	public int getCurrentY(){
		return currentY;
	}

	public int getCurrentX(){
		return currentX;
	}



	public void setCoord(button pressed) {
		//addLog(currentX+" 3 ");
		System.out.println("X=" + currentX + "Y=" + currentY);
		switch(pressed){
		case UP:
			currentY--;
			break;

		case DOWN:
			currentY++;
			break;

		case LEFT:
			currentX++;
			break;


		case RIGHT:
			currentX--;
			break;
		}
	}


	public BlockType validMove(int d, int x, int y) {


		int row = this.d.numRow;
		int col = this.d.numRow;

		switch(d){
		case 0:  
			if(exists(x, y-1, row, col) && !enemyCoord(x, y-1)){
				return currentMap[x][y-1];
			}
			break;
		case 1: 
			if(exists(x, y+1, row, col) && !enemyCoord(x, y+1)){
				return currentMap[x][y+1];
			}
			break;
		case 2: 
			if(exists(x+1, y, row, col) && !enemyCoord(x+1, y)){
				return currentMap[x+1][y];
			}
			break;
		case 3:
			if(exists(x-1, y, row, col) && !enemyCoord(x-1, y)){
				return currentMap[x-1][y];
			}
			break;
	case 4:
			return currentMap[x][y];
	}
		return BlockType.NOTHING;

	}

	public boolean validAttack(int x, int y) {
		int row =  this.d.numRow;
		int col =  this.d.numRow;

		if(exists(x, y, row, col)&&(d.visibility[x][y] == BlockType.FLOOR||d.visibility[x][y] == BlockType.CORRIDOOR)){
			return true;
		}
		return false;
	}


	private boolean enemyCoord(int x, int y){
		for (int i =0; i < enemies.size(); i++){
			if(enemies.get(i).conflict(x, y)){
				return true;
			}
		}
		return false;
	}

	private boolean exists(int x, int y, int row, int col) {
		return(x>=0 && x <row && y >=0 && y < col);
	}

	public void calcMovement(button pressed) {
		if(validMove(pressed.value, getCurrentX(), getCurrentY())== BlockType.FLOOR || validMove(pressed.value, getCurrentX(), getCurrentY())== BlockType.CORRIDOOR  ){
			movement = true;
			setCoord(pressed);	
		}
		else if(validMove(pressed.value, getCurrentX(), getCurrentY())== BlockType.UDDOOR || validMove(pressed.value, getCurrentX(), getCurrentY())== BlockType.LRDOOR){
			movement = true;


			openDoor(getCurrentX(), getCurrentY(), pressed);
			setCoord(pressed);	

			//openDoor(getCurrentX(), getCurrentY());


		}
		else if(validMove(pressed.value, getCurrentX(), getCurrentY())== BlockType.STAIRDD
				|| validMove(pressed.value, getCurrentX(), getCurrentY())== BlockType.STAIRDL
				|| validMove(pressed.value, getCurrentX(), getCurrentY())== BlockType.STAIRDR
				|| validMove(pressed.value, getCurrentX(), getCurrentY())== BlockType.STAIRDT){
			movement = true;
			setCoord(pressed);	

			System.out.println("Stairs Down");
			//	openDoor(getCurrentX(), getCurrentY(), pressed);
			//setCoord(pressed);	

			//openDoor(getCurrentX(), getCurrentY());
		//	levelUp();

		}
		else if(validMove(pressed.value, getCurrentX(), getCurrentY())== BlockType.STAIRUD
				|| validMove(pressed.value, getCurrentX(), getCurrentY())== BlockType.STAIRUL
				|| validMove(pressed.value, getCurrentX(), getCurrentY())== BlockType.STAIRUR
				|| validMove(pressed.value, getCurrentX(), getCurrentY())== BlockType.STAIRUT){
			movement = true;
			setCoord(pressed);	

			System.out.println("Stairs Up");
			//	openDoor(getCurrentX(), getCurrentY(), pressed);
			//	setCoord(pressed);	

			//openDoor(getCurrentX(), getCurrentY());

		//	levelUp();
		}
		else{
			movement = false;
		}
		for (int i =0; i < enemies.size(); i++){
			enemies.get(i).calc();
		}
	}

	private void openDoor(int x, int y, button pressed) {
		System.out.println(pressed);
		switch(pressed){
		case UP:
			System.out.println("UP");
			spawnEnemies(x, y-2);
			d.increaseVisibility(x, y-2);
			d.openDoor(x, y-1);
			//	openDoor(getCurrentX(), getCurrentY()-1);
			break;

		case DOWN:
			System.out.println("DOWN");
			spawnEnemies(x, y+2);
			d.increaseVisibility(x, y+2);
			d.openDoor(x, y+1);
			//	openDoor(getCurrentX(), getCurrentY()+1);
			break;

		case LEFT:
			System.out.println("LEFT");
			spawnEnemies(x+2, y);
			d.increaseVisibility(x+2, y);
			d.openDoor(x+1, y);
			//	openDoor(getCurrentX()+1, getCurrentY());
			break;


		case RIGHT:
			System.out.println("RIGHT");
			spawnEnemies(x-2, y);
			d.increaseVisibility(x-2, y);
			d.openDoor(x-1, y);
			//	openDoor(getCurrentX()-1, getCurrentY());
			break;
		} 

		//d.openDoor(x, y);
		//m.openDoor(x, y);
		m.center(d.visibility, x, y, true);
		d.printVisibility();
		//	System.out.println(x+" "+ y) ;
	}

	public void animateMovement(Canvas c, button pressed, int frames) {

		m.drawMap(c, pressed, movement);
		
		for (int i =0; i < loots.size(); i++){

			loots.get(i).animate(c, pressed, movement);
		}
		//}
		sprite.walk(c, pressed, frames);



		if(movement == true){
			for (int i =0; i < enemies.size(); i++){
				enemies.get(i).animate(c, pressed, frames);
			}
		}
		else{
			for (int i =0; i < enemies.size(); i++){
				enemies.get(i).animate(c, button.NONE , frames);
			}
		}
		m.drawWalls(c);


	}

	public void drawIdle(Canvas c) {

		m.drawMap(c, button.NONE, false);
		
		for (int i =0; i < loots.size(); i++){

			loots.get(i).animate(c, button.NONE, false);
		}
		
		
		sprite.face(c, button.NONE);

		for (int i =0; i < enemies.size(); i++){
			enemies.get(i).idle(c);
		}

		Paint p = new Paint();
		p.setAlpha(220);
		m.drawWalls(c);
		//	c.drawBitmap(walls, x, y, p);

	}

	/*public void levelDown() {
		currentMap = tileMap2; 
		map = saveMap(tileMap2);	
		startRow = 0;
		startCol = 3;
		currentX = startRow;
		currentY = startCol;
		x = y = 0; 
		x = x - xScale*(currentMap.length)+398 + xScale*(startRow-startCol);//+344; 
		y = y - YScale*(startCol+startRow) + 156; 

		//spawn(3);
	}
	 */
	public void spawnEnemies (int x, int y){
		if (d.roomVisited(x, y)){
			return;
		}
		Room currentRoom = d.findRoom(y, x);
		if (currentRoom == null||currentRoom.doorExists(y, x)){
			return;
		}
		int maxEnemies = (int)Math.floor(currentRoom.area/2.0);
		double percentChange = 100.0/Math.pow((1-maxEnemies), 2);
		int percent = d.rand.nextInt(100);
		int numEnemies = (int)Math.round(maxEnemies - Math.sqrt(percent/percentChange));
		spawnEnemies(numEnemies, currentRoom);
	}
	private void spawnEnemies (int num, Room r){
		boolean spaceFree[][] = new boolean [r.w][r.h];
		for (int i = 0; i < r.w; i++){
			for (int j = 0; j < r.h; j++){
				spaceFree[i][j] = true;
			}
		}
		while (num > 0){
			int x = d.rand.nextInt(r.w);
			int y = d.rand.nextInt(r.h);
			if(spaceFree[x][y]&&d.grid[y+r.y][x+r.x] == BlockType.FLOOR){
				spaceFree[x][y] = false; 
				num--;
				spawn(y+r.y, x+r.x);   
			}
		}
	}
	public void spawn(int x, int y) {
		//	for (int i = 0; i < numEnemies; i++){
		//		Enemy enemyA;
		//		enemyA = new Enemy(ov,re, this, 3+i, 10);
		//		enemies.add(enemyA);
		//	}

		Enemy enemyA;
		enemyA = new Enemy(ov,re, this, x, y);
		enemies.add(enemyA);
		/*
		//Enemy enemyA;
		enemyA = new Enemy(ov,re, this, 5, 10);
		enemies.add(enemyA);

		//Enemy enemyA;
		enemyA = new Enemy(ov,re, this, 6, 8);
		enemies.add(enemyA);

		enemyA = new Enemy(ov,re, this, 14, 10);
		enemies.add(enemyA);
		 */
	}
	public void levelDown(Canvas c) {
		dunFloor--;
			enemies.clear();
			loots.clear();
		//	m = null;
			//	mapSquares = new ArrayList<Bitmap>();

			//	x = y = 0;
			//	re = r;
			
		
			if (!pastSeed.isEmpty()){
				long temp = pastSeed.pop();
				d = null;
				d = new Dungeon(temp);
			}else{
				long temp = d.rand.getSeed();
				d = null;
				d = new Dungeon(temp);
			}
			//d.printGrid();

			//System.out.println("CONSTRUCTYEFDDD");

			currentMap = d.grid;;

			startRow = d.endY;
			startCol = d.endX;
			currentX = startRow;
			currentY = startCol;
		//	center(currentX,currentY ,d.visibility) ;
			// System.out.println(startRow+"     "+ startCol);
			//		sprite = new Sprite(ov,re);	


			//	character = new Stats(ov, re);

			//		attackType = new Basic(ov, re);
		//	d.increaseVisibility(currentY, currentX);
		//	d.increaseVisibility(currentY+1, currentX);
		//	d.increaseVisibility(currentY-1, currentX);
		//	d.increaseVisibility(currentY, currentX+1);
		//	d.increaseVisibility(currentY, currentX-1);
			//center(currentX,currentY ) ;
		//	drawIdle(c);
			log.clear();
			log.add("You go up the stairs", 5);
	}
	public void levelUp(Canvas c) {
		dunFloor++;
		enemies.clear();
		loots.clear();

	//	m = null;
		//	mapSquares = new ArrayList<Bitmap>();

		//	x = y = 0;
		//	re = r;
		long temp = d.rand.getSeed();
		pastSeed.push(temp);
	//	d = null;
		d = new Dungeon();
		//d.printGrid();

		//System.out.println("CONSTRUCTYEFDDD");

	//	tileMap = 
		currentMap = d.grid;;

		startRow = d.startY;
		startCol = d.startX;
		currentX = startRow;
		currentY = startCol;

		//center(currentX,currentY ,d.visibility) ;
		// System.out.println(startRow+"     "+ startCol);
		//		sprite = new Sprite(ov,re);	


		//	character = new Stats(ov, re);

		//		attackType = new Basic(ov, re);
	//	d.increaseVisibility(currentY, currentX);
	//	d.increaseVisibility(currentY+1, currentX);
	//	d.increaseVisibility(currentY-1, currentX);
	//	d.increaseVisibility(currentY, currentX+1);
	//	d.increaseVisibility(currentY, currentX-1);
	//	center(currentX,currentY ) ;
	//	m = new MapChunks(ov, re, d.visibility, startRow, startCol);
	//	currentMap = tileMap;
		//	map = saveMap(tileMap);	
		//	startRow = 2;
		//	startCol = 2;
		///	currentX = startRow;
		//		currentY = startCol;
		//	x = y = 0;
		//	x = x - xScale*(currentMap.length)+398 + xScale*(startRow-startCol);//+344; 
		//	y = y - YScale*(startCol+startRow) + 156; 
		//
	//	drawIdle(c);
		log.clear();
		log.add("You go down the stairs", 5);
	}




	public void drawAttackPreview(Canvas c, button pressed) {

		c.drawColor(Color.BLACK);
		m.drawMap(c, button.NONE,false);

		attackType.select(c, pressed, this);
		
		for (int i =0; i < loots.size(); i++){

			loots.get(i).animate(c, button.NONE, false);
		}
		
		sprite.face(c, pressed);

		for (int i =0; i < enemies.size(); i++){
			//   enemies.get(i).drawHealth(c);
			enemies.get(i).idle(c);
		}
		Paint p = new Paint();
		p.setAlpha(220);
		m.drawWalls(c);
		for (int i =0; i < enemies.size(); i++){
			enemies.get(i).drawHealth(c);
		}
		//c.drawBitmap(walls, x, y, p);
	}  



	public void attackAnimate(Canvas c, button lastDirection) {

		c.drawColor(Color.BLACK);
		m.drawMap(c, button.NONE,false);
		
		for (int i =0; i < loots.size(); i++){

			loots.get(i).animate(c, button.NONE, false);
		}
		
		sprite.attack(c, lastDirection);
		
		for (int i =0; i < enemies.size(); i++){
			enemies.get(i).idle(c);
		}
		
		attackType.animate(c);

		Paint p = new Paint();
		p.setAlpha(220);
		m.drawWalls(c);
		//	c.drawBitmap(walls, x, y, p);
	}

	public void moveEnemys(Canvas c, button direction, int frames) {


		c.drawColor(Color.BLACK);
		m.drawMap(c, button.NONE,false);
		
		for (int i =0; i < loots.size(); i++){

			loots.get(i).animate(c, button.NONE, false);
		}
		
		sprite.face(c, direction);
		for (int i =0; i < enemies.size(); i++){
			if(enemies.get(i).currentHP <= 0){
				character.xp += 25;
				dropLoots(enemies.get(i).xCoord, enemies.get(i).yCoord);
				log.add("Slime "+enemies.get(i).id+"[0] dies, you gain[1] 25xp[2]", 2);
				enemies.remove(i);
			}
		}
		for (int i =0; i < enemies.size(); i++){

			enemies.get(i).animate(c, button.NONE, frames);
		}
		Paint p = new Paint();
		p.setAlpha(220);
		m.drawWalls(c);
		//c.drawBitmap(walls, x, y, p);
		
		if(character.xp >= character.totalXp){
			character.levelUp();
			log.add("Congratulations your now level [0]"+ character.level+"[1]![2]", 4);

		}
	}





	private void dropLoots(int xCoord, int yCoord) {
		// TODO Auto-generated method stub
		int randInt = new Random().nextInt(10);
		
	//	if(randInt < 1){
	//	loots.add(new GroundLoot(ov, re, xCoord, yCoord, currentX, currentY, true));
	//	}
	//	else if(randInt < 3){
	//		loots.add(new GroundLoot(ov, re, xCoord, yCoord, currentX, currentY, false));
	//	}
		
		
		if(randInt < 2){
			loots.add(new GroundLoot(ov, re, xCoord, yCoord, currentX, currentY, true)); //loot
			}
			else if(randInt < 6){
				loots.add(new GroundLoot(ov, re, xCoord, yCoord, currentX, currentY, false)); //moneys
			}
	}




	public void center(int currentX2, int currentY2, boolean b) {
		// TODO Auto-generated method stub
		//	x = 0;
		//	y = 0;
		m.center(d.visibility, currentX2, currentY2, b);
	}



	
	
	public void updateHealth(Canvas c, button lastDirection) {


		
		c.drawColor(Color.BLACK);
		m.drawMap(c, button.NONE,false);
		for (int i =0; i < loots.size(); i++){

			loots.get(i).animate(c, button.NONE, false);
		}
		sprite.face(c, button.NONE);
		//attackType.animate(c);

		for (int i =0; i < enemies.size(); i++){
			if(enemies.get(i).hit){
				enemies.get(i).currentHP = enemies.get(i).currentHP - attackType.damage/5 - character.attack/5;

			}
			enemies.get(i).idle(c);
			// enemies.get(i).drawHealth(c);

		}

		// Paint p = new Paint();
		// p.setAlpha(220);
		m.drawWalls(c);

		for (int i =0; i < enemies.size(); i++){
			enemies.get(i).drawHealth(c);
		}


	}


	public void calcStats(){


		for (int i =0; i < enemies.size(); i++){
			enemies.get(i).calc();
		}
	}

	public void calcHit() {
		for (int p =0; p < attackType.getHit().size(); p++){
			int i = attackType.getHit().get(p).x +currentX;
			int j = attackType.getHit().get(p).y + currentY;
			System.out.println(i+" "+ j);

			for (int q =0; q < enemies.size(); q++){
				if(enemies.get(q).conflict(i, j)){
					System.out.println("OW");
					enemies.get(q).hit = true;
					// enemies.get(q).currentHP -= 30;

				}
			}

		}
	}
     



	public int checkSpace() {
    if(validMove(button.NONE.value, getCurrentX(), getCurrentY())== BlockType.STAIRDD
			|| validMove(button.NONE.value, getCurrentX(), getCurrentY())== BlockType.STAIRDL
			|| validMove(button.NONE.value, getCurrentX(), getCurrentY())== BlockType.STAIRDR
			|| validMove(button.NONE.value, getCurrentX(), getCurrentY())== BlockType.STAIRDT){
	
return 0;
//	levelUp();

	}
	else if(validMove(button.NONE.value, getCurrentX(), getCurrentY())== BlockType.STAIRUD
			|| validMove(button.NONE.value, getCurrentX(), getCurrentY())== BlockType.STAIRUL
			|| validMove(button.NONE.value, getCurrentX(), getCurrentY())== BlockType.STAIRUR
			|| validMove(button.NONE.value, getCurrentX(), getCurrentY())== BlockType.STAIRUT){

		return 1;
		//levelUp();
	}
	return -1;


}




	public void loadBitmaps() {
	//	Bitmap mCBitmap;
	//	Bitmap map, walls;
		
	}
	
	public void recycleBitmaps() {
		//mCBitmap.recycle();
		if(map != null){
		map.recycle();
		}
		if(walls != null){
		walls.recycle();
		}
		}
}
