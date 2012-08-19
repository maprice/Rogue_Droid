package abilitys;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import com.mike.rogue.AttackSquare;
import com.mike.rogue.Main;
import com.mike.rogue.R;
import com.mike.rogue.Main.OurView;
import com.mike.rogue.R.drawable;

public class Empty extends AttackType{


	int[][] basic = {
			{ 0, 0, 0},
			{ 0, 0, 0},
			{ 0, 0, 0},
			{ 0, 0, 0},
	};

	public Empty(OurView ourView, Resources r) {

		empty = true;

		ov = ourView;
		attackGrid = basic;
		damage = 0;
		mana = 0;
		effect = BitmapFactory.decodeResource(r, R.drawable.se_attackswipe);	
		icon = BitmapFactory.decodeResource(r, R.drawable.si_emptyskill);
		attacked = new ArrayList<Posn>();
		frames = 5;

		height = effect.getHeight() / 1; //5 rows
		width = effect.getWidth() / 5; //5 columns
		square = new AttackSquare(ov, r);

		xCenter = 0;
		yCenter = 0;

		name = "";
	}

}
