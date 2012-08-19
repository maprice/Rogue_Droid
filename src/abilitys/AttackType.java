package abilitys;

import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.mike.rogue.AttackSquare;
import com.mike.rogue.Main;
import com.mike.rogue.Map;
import com.mike.rogue.Main.OurView;
import com.mike.rogue.Main.button;

public class AttackType {


	public float damage;
	public float mana = 10;
	
	public int[][] attackGrid;
	char slot;
	Bitmap effect;
	Bitmap icon;
	public String name;
	public String description = "<Insert Description>";
	
	public Boolean empty = false;
	
	int frames;
	
	OurView ov;
	int x, y;
	public int height, width;
	int currentFrame = 0;
	int direction = 0;
	int xCenter, yCenter;
	public AttackSquare square;

	List<Posn> attacked;


	public int[][] getAttackGrid(){
		return attackGrid;
	}


	final int tileWidth = 108;
	final int tileHeight = 54;



	public void reset() {
		currentFrame = 0;
	}


	int [][] attackArray = new int[12][2];


	public void select(Canvas c, button pressed, Map map) {

		//currentFrame++;// = ++currentFrame;
		attacked.clear();
		currentFrame = 0;

		int row = attackGrid[0].length;
		int col = attackGrid.length;

		int playerX = map.getCurrentX();
		int playerY = map.getCurrentY();

		int xMod = 1;
		int yMod = 1;
		boolean ns = true;
		switch(pressed){
		case UP:
			xMod = -1;
			yMod = -1;
			ns = true;
			break;

		case DOWN:
			xMod = 1;
			yMod = 1;
			ns = true;
			break;

		case LEFT: 
			xMod = 1;
			yMod = -1;
			ns = false;
			break;

		case RIGHT:
			xMod = -1;
			yMod = 1;
			ns = false;
			break;
		}		  

		/*	if(map.validAttack(playerX -1*xMod   +i*xMod, playerY+1*yMod + j*yMod)){
		//	System.out.println(i + ", " + j);
			square.animate(c,i-1 * xMod, j+1 * yMod);
		}
	//square.animate(c,1,1);
		 */
		/*	

		 */

		if(ns){	
			for(int j = 0; j < attackGrid.length; j++){

				for(int i = 0; i < attackGrid[0].length; i++){

					if(map.validAttack(playerX + xMod*i+yMod*(-1), playerY+yMod*j+1*xMod) && attackGrid[j][i] == 1){

						square.animate(c, xMod*i+yMod*(-1), yMod*j+1*xMod);


						Posn delta = new Posn(xMod*i+yMod*(-1),yMod*j+1*xMod);
						attacked.add(delta);
					} 


				}
			}
		}

		else{
			for(int j = 0; j < attackGrid[0].length; j++){
				for(int i = 0; i < attackGrid.length; i++){

					if(map.validAttack(playerX + xMod*i+yMod*(-1), playerY+yMod*j+1*xMod) && attackGrid[i][j] == 1){

						square.animate(c, xMod*i+yMod*(-1), yMod*j+1*xMod);
						Posn delta = new Posn(xMod*i+yMod*(-1),yMod*j+1*xMod);
						attacked.add(delta);
					} 

				}
			}

		}




	}


public Bitmap getIcon(){
	return icon;
	
}

	public void animate(Canvas c) {
		currentFrame++;

		if(currentFrame <= frames){
			int srcY = direction * height;
			int srcX = currentFrame * width;


			for (int p =0; p < attacked.size(); p++){
				int i = attacked.get(p).x;
				int	j = attacked.get(p).y;

				x = 350 + tileWidth/2*((j) - (i)) + xCenter;
				y = 200 + tileHeight/2*((j) + (i)) + yCenter;

				Rect src = new Rect (srcX ,srcY, srcX + width, srcY + height);
				Rect dst = new Rect (x, y, x + width, y + height);
				c.drawBitmap(effect, src, dst, null);
			}
		}
	}

	public List<Posn> getHit(){


		return attacked;
	}


}




