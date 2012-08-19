package abilitys;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import com.mike.rogue.AttackSquare;
import com.mike.rogue.Main;
import com.mike.rogue.R;
import com.mike.rogue.Main.OurView;
import com.mike.rogue.R.drawable;

public class Lightning extends AttackType{


	int[][] basic = {
			{ 0, 1, 0},
			{ 0, 1, 0},
			{ 0, 1, 0},
			{ 0, 1, 0},
	};

	public Lightning(OurView ourView, Resources r) {
		ov = ourView;
		attackGrid = basic;
		damage = 70;
		effect = BitmapFactory.decodeResource(r, R.drawable.se_lightning);	
		icon = BitmapFactory.decodeResource(r, R.drawable.si_lightning);
		attacked = new ArrayList<Posn>();
		frames = 7;
		height = effect.getHeight() / 1; //5 rows
		width = effect.getWidth() / 7; //5 columns
		square = new AttackSquare(ov, r);

		xCenter = 20;
		yCenter = 0;
		
		name = "Lightning";
	}

}
