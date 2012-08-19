package dungeonGen;




import java.util.ArrayList;

import java.util.List;
import java.util.Random;

public class Dungeon {
	public enum BlockType {
		NOTHING(0), FLOOR(1), NEWALL(2), NWWALL(2), SEWALL(2), SWWALL(2), TOPCORNER(2), LEFTCORNER(2), RIGHTCORNER(2), BOTTOMCORNER(2), WALL (2), UDDOOR(3), LRDOOR(3), CORRIDOOR(4), STAIRUT(5), STAIRUD(5), STAIRUL(5), STAIRUR(5), STAIRDT(5), STAIRDD(5), STAIRDL(5), STAIRDR(5);
		private BlockType (int value)
		{
			this.value = value;
		}
		private int value; 
	}
	public enum Features {
		CORRIDOR, ROOM;
	}
	public enum Direction{
		LEFT, RIGHT, UP, DOWN;
	}

	public BlockType grid [][]; 
	public BlockType visibility [][];

	private List<Room> roomList = new ArrayList<Room>();
	public int numRow = 25;
	public int numColumn = 25;
	private int level = 1;
	public NRandom rand;
	private int numRooms = 3;
	private int maxRoomHeight = 8;
	private int maxRoomWidth = 8;
	private int minRoomHeight = 4;
	private int minRoomWidth = 4;
	private int maxCorridoorHeight = 4;
	private int maxCorridoorWidth = 4;
	private int minCorridoorHeight = 1;
	private int minCorridoorWidth = 1;
	public int startX = 0, endX = 0;
	public int startY = 0, endY = 0;
	//Constructors
	public Dungeon (){
		rand = new NRandom();
		generateDungeon();
		this.increaseVisibilityInitial(startY, startX);
	}
	public Dungeon (int r, int c){
		rand = new NRandom();
		numRow = r;
		numColumn = c;
		generateDungeon();
		this.increaseVisibilityInitial(startY, startX);
	}
	public Dungeon (int l){
		rand = new NRandom();
		level = l;
		generateDungeon();
		this.increaseVisibilityInitial(startY, startX);
	}
	public Dungeon (int r, int c, int l){
		rand = new NRandom();
		numRow = r;
		numColumn = c;
		level = l;
		generateDungeon();
		this.increaseVisibilityInitial(startY, startX);
	}
	public Dungeon(int l, long seed){
		rand = new NRandom(seed);
		numRow = (int)(l*1.2) + 25;
		numColumn = (int)(l*1.2) + 25;
		level = l;
		generateDungeon();
		this.increaseVisibilityInitial(startY, startX);
	}


	public Dungeon(long temp) {
		rand = new NRandom(temp);
		generateDungeon();
		increaseVisibilityInitial(endY, endX);
	}


	int spaceUsed =0;
	private void generateDungeon() {
		initialCondition();
		while ((spaceUsed*1.0)/(numRow*numColumn*1.0)*100 < 50){
			Posn newPlace = pickPlace();//check door ok
			Features type = chooseFeature();
			Direction dir = getDirection(newPlace);
			BlockType doorType = null;
			int h ;
			int w ;
			int x=0, y=0;
			int rx=0, ry=0;
			switch (type){
			case CORRIDOR://Corridoor
				h = rand.nextInt(maxCorridoorHeight-minCorridoorHeight+1)+minCorridoorHeight;
				w = rand.nextInt(maxCorridoorWidth-minCorridoorWidth+1)+minCorridoorWidth;
				switch(dir){
				case LEFT:
					x = newPlace.x - w+1;
					y = newPlace.y;
					h = 1;
					rx = newPlace.x + 1;
					ry = newPlace.y;
					doorType = BlockType.LRDOOR;
					break;
				case RIGHT:
					x = newPlace.x;
					y = newPlace.y;
					h = 1;
					rx = newPlace.x - 1;
					ry = newPlace.y;
					doorType = BlockType.LRDOOR;
					break;
				case UP:
					x = newPlace.x;
					y = newPlace.y-h+1;
					w = 1;
					rx = newPlace.x;
					ry = newPlace.y+1;
					doorType = BlockType.UDDOOR;
					break;
				case DOWN:
					x = newPlace.x;
					y = newPlace.y;
					w = 1;
					rx = newPlace.x;
					ry = newPlace.y-1;
					doorType = BlockType.UDDOOR;
					break;
				}
				if (placeCorridor(x, y, h, w, dir)){
					Room r = findRoom(rx, ry);
					if (r != null){
						r.doors.add(new Posn(rx, ry));
						grid[ry][rx] = doorType;
					}
				}

				break;
			case ROOM://Room
				h = rand.nextInt(maxRoomHeight-minRoomHeight+1) + minRoomHeight;
				w = rand.nextInt(maxRoomWidth - minRoomWidth+1) + minRoomWidth;
				switch(dir){
				case LEFT:
					x = newPlace.x - w;
					y = newPlace.y - rand.nextInt(h);
					rx = newPlace.x + 1;
					ry = newPlace.y;
					doorType = BlockType.LRDOOR;
					break;
				case RIGHT:
					x = newPlace.x+1;
					y = newPlace.y - rand.nextInt(h);
					rx = newPlace.x - 1;
					ry = newPlace.y;
					doorType = BlockType.LRDOOR;
					break;
				case UP:
					x = newPlace.x - rand.nextInt(w);
					y = newPlace.y-h;
					rx = newPlace.x;
					ry = newPlace.y+1;
					doorType = BlockType.UDDOOR;
					break;
				case DOWN:
					x = newPlace.x - rand.nextInt(w);
					y = newPlace.y+1; 
					rx = newPlace.x;
					ry = newPlace.y-1;
					doorType = BlockType.UDDOOR;
					break;
				}
				if (placeRoom(x, y, h, w)){
					Room r = findRoom(x, y);
					r.doors.add(new Posn(newPlace.x, newPlace.y));
					grid[newPlace.y][newPlace.x] = doorType;
					r = findRoom(rx, ry);
					if (r != null){
						r.doors.add(new Posn(rx, ry));
						grid[ry][rx] = doorType;
					}
				}
				break;
			}
			//spaceUsed = 50;
		}
		setStairs();
	}
	public void increaseVisibilityInitial(int startY, int startX){
		increaseVisibility(startY, startX);
		increaseVisibility(startY+1, startX);
		increaseVisibility(startY-1, startX);
		increaseVisibility(startY, startX+1);
		increaseVisibility(startY, startX-1);
	}
	/*
 public void increaseVisibility(int x, int y){
 // System.out.println("Hey");

	 if(!inRange(x, y)){
		 return;
	 }
  if ((grid[y][x] != BlockType.FLOOR && grid[y][x] != BlockType.CORRIDOOR)||visibility [y][x] != BlockType.NOTHING){
   visibility [y][x] = grid [y][x];
   return;
  }
  visibility [y][x] = grid [y][x];
  increaseVisibility(x+1, y);
  increaseVisibility(x-1, y);
  increaseVisibility(x, y+1);
  increaseVisibility(x, y-1);
  increaseVisibility(x+1, y+1);
  increaseVisibility(x+1, y-1);
  increaseVisibility(x-1, y+1);
  increaseVisibility(x-1, y-1);
 }
	 */ 
	public void increaseVisibility(int x, int y){
		// System.out.println("Hey");

		if(!inRange(x, y)){
			return;
		}
		if ((grid[x][y] != BlockType.FLOOR && grid[x][y] != BlockType.CORRIDOOR && grid[x][y].value != 5)||visibility [x][y] != BlockType.NOTHING){
			visibility [x][y] = grid [x][y];
			return;
		}
		visibility [x][y] = grid [x][y];
		increaseVisibility(x+1, y);
		increaseVisibility(x-1, y);
		increaseVisibility(x, y+1);
		increaseVisibility(x, y-1);
		if (visibility[x][y] == BlockType.FLOOR){
			increaseVisibility(x+1, y+1);
			increaseVisibility(x+1, y-1);
			increaseVisibility(x-1, y+1);
			increaseVisibility(x-1, y-1);
		}
	}

	private boolean placeCorridor(int x, int y, int h, int w, Direction dir) {
		if (!corridorOk(x, y, h, w, dir)){
			return false;
		}
		for (int i = y; i < y+h; i++){
			for (int j = x; j < x + w; j++){
				grid[i][j]= BlockType.CORRIDOOR;
			}
		}
		spaceUsed += h*w;
		return true;
	}

	private boolean corridorOk (int x, int y, int h, int w, Direction dir){
		for (int i = y; i < y+h; i++){
			for (int j = x; j < x + w; j++){
				if(!(inRange(j, i)&&grid[i][j] == BlockType.NOTHING&&corridorOk(j, i, dir))){
					return false;
				}
			}
		}
		return true;
	}

	private boolean corridorOk(int x, int y, Direction dir) {
		if (dir == Direction.LEFT || dir == Direction.RIGHT){
			if ((inRange(y-1, x) && grid[y-1][x] == BlockType.CORRIDOOR)&&(
					(inRange(y-1, x-1) && grid[y-1][x-1] == BlockType.CORRIDOOR)
					||(inRange(y-1, x+1) && grid[y-1][x+1] == BlockType.CORRIDOOR))){
				return false;
			}
			if ((inRange(y+1, x) && grid[y+1][x] == BlockType.CORRIDOOR)&&(
					(inRange(y+1, x-1) && grid[y+1][x-1] == BlockType.CORRIDOOR)
					||(inRange(y+1, x+1) && grid[y+1][x+1] == BlockType.CORRIDOOR))){
				return false;
			}
		}
		else{
			if ((inRange(y, x-1) && grid[y][x-1] == BlockType.CORRIDOOR)&&(
					(inRange(y-1, x-1) && grid[y-1][x-1] == BlockType.CORRIDOOR)
					||(inRange(y+1, x-1) && grid[y+1][x-1] == BlockType.CORRIDOOR))){
				return false;
			}
			if ((inRange(y, x+1) && grid[y][x+1] == BlockType.CORRIDOOR)&&(
					(inRange(y-1, x+1) && grid[y-1][x+1] == BlockType.CORRIDOOR)
					||(inRange(y+1, x+1) && grid[y+1][x+1] == BlockType.CORRIDOOR))){
				return false;
			}
		}
		return true;
	}
	private Direction getDirection(Posn p) {
		if (inRange (p.x-1, p.y)&& grid[p.y][p.x-1] != BlockType.NOTHING){
			return Direction.RIGHT;
		}
		if (inRange (p.x+1, p.y)&& grid[p.y][p.x+1] != BlockType.NOTHING){
			return Direction.LEFT;
		}
		if (inRange (p.x, p.y-1)&& grid[p.y-1][p.x] != BlockType.NOTHING){
			return Direction.DOWN;
		}
		if (inRange (p.x, p.y+1)&& grid[p.y+1][p.x] != BlockType.NOTHING){
			return Direction.UP;
		}

		return null;
	}
	private Features chooseFeature() {
		int num = rand.nextInt(100);
		if (num < 50){
			return Features.CORRIDOR;
		}else{
			return Features.ROOM;
		}
	}
	private Posn pickPlace(){
		Posn p;
		do{
			int x = rand.nextInt(numRow);
			int y = rand.nextInt(numColumn);
			p = new Posn (x, y);
		}while (!pointGood(p));
		return p;
	}

	private boolean pointGood(Posn p) {
		if (grid[p.y][p.x] != BlockType.NOTHING ){
			return false;
		}
		Room r = null;
		if (inRange (p.x-1, p.y)&& grid[p.y][p.x-1] != BlockType.NOTHING){
			r = findRoom(p.x-1, p.y);
			if (r != null && !r.doorOk(new Posn(p.x-1, p.y))){
				return false;
			}
			return true;
		}
		if (inRange (p.x+1, p.y)&& grid[p.y][p.x+1] != BlockType.NOTHING){
			r = findRoom(p.x+1, p.y);
			if (r != null && !r.doorOk(new Posn(p.x+1, p.y))){
				return false;
			}
			return true;
		}
		if (inRange (p.x, p.y-1)&& grid[p.y-1][p.x] != BlockType.NOTHING){
			r = findRoom(p.x, p.y-1);
			if (r != null && !r.doorOk(new Posn(p.x, p.y-1))){
				return false;
			}
			return true;
		}
		if (inRange (p.x, p.y+1)&& grid[p.y+1][p.x] != BlockType.NOTHING){
			r = findRoom(p.x, p.y+1);
			if (r != null && !r.doorOk(new Posn(p.x, p.y+1))){
				return false;
			}
			return true;
		}

		return false;
	}
	private void initialCondition(){
		grid = new BlockType [numRow][numColumn];
		visibility = new BlockType [numRow][numColumn];
		for (int i = 0; i < numRow; i++){
			for (int j =0; j < numColumn; j++){
				grid[i][j]=BlockType.NOTHING;
				visibility[i][j]=BlockType.NOTHING;
			}
		}
		int h = rand.nextInt(maxRoomHeight-minRoomHeight+1) + minRoomHeight;
		int w = rand.nextInt(maxRoomWidth - minRoomWidth+1) + minRoomWidth;

		int x = numRow/2 - w/2;
		int y = numColumn/2 - h/2;
		placeRoom(x, y, h, w);

	}
	private boolean placeRoom(int x, int y, int h, int w){
		Room r = new Room(x, y, w, h);
		if (!roomFits(r)){
			return false;
		}

		for (int i = y; i < y + h; i++){
			for (int j = x; j < x + w; j++){
				grid[i][j] = BlockType.FLOOR;
			}
		}
		placeWalls(r);
		roomList.add(r);
		spaceUsed += (h+1)*(w+1);
		return true;
	}

	private boolean roomFits(Room r)
	{
		for (int i = r.y-1; i < r.y+r.h+1; i++){
			for (int j = r.x-1; j < r.x + r.w+1;  j++){
				if(!(inRange(j, i) && grid[i][j] == BlockType.NOTHING)){
					r.deleteRoom();
					return false;
				}
			}
		}
		return true;
	}




	public Room findRoom(int x, int y){
		Room r;
		for (int i = 0; i < roomList.size(); i++){
			r = roomList.get(i);
			if (r.inRange(x, y)){
				return r;
			}
		}
		return null;
	}


	private void placeWalls(Room r){

		//Corners
		if (inRange(r.x-1, r.y-1)){//Top left corner
			grid[r.y-1][r.x-1] = BlockType.TOPCORNER;
		}
		if (inRange(r.x+r.w, r.y-1)){//Top right corner
			grid[r.y-1][r.x+r.w] = BlockType.RIGHTCORNER;
		}
		if (inRange(r.x-1, r.y+r.h)){//Bottom left corner
			grid[r.y+r.h][r.x-1] = BlockType.LEFTCORNER;
		}
		if (inRange(r.x+r.w, r.y+r.h)){//Bottom right corner
			grid[r.y+r.h][r.x+r.w] = BlockType.BOTTOMCORNER;
		}

		//Verticals
		if (inRange(r.x-1, 0)){//left wall
			for (int k = r.y; k < r.y+r.h; k++){
				grid[k][r.x-1] = BlockType.NWWALL;
			}
		}
		if (inRange(r.x+r.w, 0)){//right wall
			for (int k = r.y; k < r.y+r.h; k++){
				grid[k][r.x+r.w] = BlockType.SWWALL;
			}
		} 

		//Horizontals
		if (inRange(0, r.y-1)){//top wall
			for (int j = r.x; j < r.x + r.w;  j++){
				grid [r.y-1][j] = BlockType.NEWALL;
			}
		}
		if (inRange(0, r.y + r.h)){//bottom wall
			for (int j = r.x; j < r.x + r.w;  j++){
				grid [r.y + r.h][j] = BlockType.SEWALL;
			}
		}
	}

	private boolean inRange(int x, int y){
		return (x >= 0 && x < numColumn)&&(y >= 0 && y < numRow);
	}


	public void printGrid (){
		for (int i = 0; i < numRow; i++){
			for (int j = 0; j < numColumn; j++){
				System.out.print(grid[i][j].value + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	public void printVisibility (){
		for (int i = 0; i < numRow; i++){
			for (int j = 0; j < numColumn; j++){
				System.out.print(visibility[i][j].value + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public void openDoor(int x, int y) {

		grid[x][y] = BlockType.FLOOR;
		visibility[x][y] = BlockType.FLOOR;
	}
	public boolean roomVisited(int x, int y){
		return visibility[x][y] != BlockType.NOTHING;
	}

	private Room randomRoom(){
		int num = rand.nextInt(roomList.size());
		return roomList.get(num);
	}
	
	private Direction randomDirection(){
		int num = rand.nextInt(4);
		switch(num){
		case 0:
			return Direction.DOWN;
		case 1:
			return Direction.UP;
		case 2:
			return Direction.RIGHT;
		default:
			return Direction.LEFT;
		}
	}
	private void setStairs(){
		Room firstRoom = randomRoom();
		Room secondRoom;
		do{
			secondRoom = randomRoom();
		}while(firstRoom.equals(secondRoom));
		Direction d = randomDirection();
		int num;
		switch (d){
		case UP:
			do{ 
				num = rand.nextInt(firstRoom.w-2)+firstRoom.x+1;
			}while(firstRoom.doorExists(num, firstRoom.y-1));
			grid[firstRoom.y][num] = BlockType.STAIRUT;
			startX = num;
			startY = firstRoom.y;
			break;
		case DOWN:
			do{
				num = rand.nextInt(firstRoom.w-2)+firstRoom.x+1;
			}while(firstRoom.doorExists(num, firstRoom.y+firstRoom.h));
			grid[firstRoom.y+firstRoom.h-1][num] = BlockType.STAIRUD;
			startX = num;
			startY = firstRoom.y+firstRoom.h-1;
			break;
		case LEFT:
			do{
				num = rand.nextInt(firstRoom.h-2)+firstRoom.y+1;
			}while(firstRoom.doorExists(firstRoom.x-1, num));
			grid[num][firstRoom.x] = BlockType.STAIRUL;
			startX = firstRoom.x;
			startY = num;
			break;
		case RIGHT:
			do{
				num = rand.nextInt(firstRoom.h-2)+firstRoom.y+1;
			}while(firstRoom.doorExists(firstRoom.x+firstRoom.w, num));
			grid[num][firstRoom.x+firstRoom.w-1] = BlockType.STAIRUR;
			startX = firstRoom.x+firstRoom.w-1;
			startY = num;
			break;
		}
		d = randomDirection();
		switch (d){
		case UP:
			do{
				num = rand.nextInt(secondRoom.w-2)+secondRoom.x+1;
			}while(secondRoom.doorExists(num, secondRoom.y-1));
			grid[secondRoom.y][num] = BlockType.STAIRDT;
			endX =num;
			endY = secondRoom.y;
			break;
		case DOWN:
			do{
				num = rand.nextInt(secondRoom.w-2)+secondRoom.x+1;
			}while(secondRoom.doorExists(num, secondRoom.y+secondRoom.h));
			grid[secondRoom.y+secondRoom.h-1][num] = BlockType.STAIRDD;
			endX =num;
			endY = secondRoom.y+secondRoom.h-1;
			break;
		case LEFT:
			do{
				num = rand.nextInt(secondRoom.h-2)+secondRoom.y+1;
			}while(secondRoom.doorExists(secondRoom.x-1, num));
			grid[num][secondRoom.x] = BlockType.STAIRDL;
			endX =secondRoom.x;
			endY = num;
			break;
		case RIGHT:
			do{
				num = rand.nextInt(secondRoom.h-2)+secondRoom.y+1;
			}while(secondRoom.doorExists(secondRoom.x+secondRoom.w, num));
			grid[num][secondRoom.x+secondRoom.w-1] = BlockType.STAIRDR;
			endX =secondRoom.x+secondRoom.w-1;
			endY = num;
			break;
		}
		System.out.println("X: " + startY + ", Y: "+ startX);
		System.out.println("X: " + endY + ", Y: "+ endX);
	}
}

