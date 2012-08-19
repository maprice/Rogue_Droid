package dungeonGen;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dungeonGen.Dungeon.Direction;






public class Room {
	int id;
	public int x, y;
	public int h, w;
	int p;
	public int area;
	boolean hasDoor = false;
	List <Posn> doors;
	private static int idct = 0;
	public Room(int x, int y, int w, int h){
		id = idct++;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		area = (h-1)*(w-1);
		p = 2*h+2*w;
		doors = new ArrayList<Posn>();
	}

	public boolean doorOk(Posn p){
		if (!validDoor(p.x, p.y)){
			return false;
		}
		Iterator<Posn> it = doors.iterator();
		while (it.hasNext()){
			Posn d = it.next();
			for (int i = 0; i < 5; i++){
				for (int j=0; j<5; j++){
					if (p.x == d.x + i-2  && p.y == d.y + j-2 ){
						return false;
					}
				}
			}
		}
		return true;
	}

	public void deleteRoom(){
		idct--;
	}

	public boolean inRange(int x, int y){
		return (this.x - 1 <= x && this.x + w + 1 > x)&&(this.y - 1 <= y && this.y + h + 1> y);
	}

	public boolean validDoor(int x, int y){
		return !((x == this.x-1 && y == this.y-1)
				||(x == this.x+w && y == this.y-1)
				||(x == this.x-1 && y == this.y+h)
				||(x == this.x+w && y == this.y+h));
	}

	public boolean doorExists(int x, int y){
		Iterator<Posn> it = doors.iterator();
		while (it.hasNext()){
			Posn d = it.next();
			if (x == d.x && y == d.y){
				return true;
			}
		}
		return false;
	}


}
