package abilitys;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import com.mike.rogue.AttackSquare;
import com.mike.rogue.Main;
import com.mike.rogue.R;
import com.mike.rogue.Main.OurView;
import com.mike.rogue.R.drawable;

public class FireStorm extends AttackType{


	int[][] basic = {
			{ 0, 0, 0},
			{ 0, 1, 0},
			{ 1, 1, 1},
			{ 0, 1, 0},
	};

	public FireStorm(OurView ourView, Resources r) {
		ov = ourView;
		attackGrid = basic;
		damage = 78;
		effect = BitmapFactory.decodeResource(r, R.drawable.se_basic);	
		icon = BitmapFactory.decodeResource(r, R.drawable.si_firestorm);
		attacked = new ArrayList<Posn>();
		frames = 4;
		height = effect.getHeight() / 1; //5 rows
		width = effect.getWidth() / 4; //5 columns
		square = new AttackSquare(ov, r);

		xCenter = 10;
		yCenter = -10;
		
		
		name = "Fire Storm";
	}

}
