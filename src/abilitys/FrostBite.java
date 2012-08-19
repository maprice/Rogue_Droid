package abilitys;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import com.mike.rogue.AttackSquare;
import com.mike.rogue.Main;
import com.mike.rogue.R;
import com.mike.rogue.Main.OurView;
import com.mike.rogue.R.drawable;

public class FrostBite extends AttackType{


	int[][] basic = {
			{ 0, 0, 0},
			{ 0, 1, 0},
			{ 0, 1, 0},
			{ 1, 1, 1},
	};

	public FrostBite(OurView ourView, Resources r) {
		ov = ourView;
		attackGrid = basic;
		damage = 60;
		effect = BitmapFactory.decodeResource(r, R.drawable.se_iceeffect);	
		icon = BitmapFactory.decodeResource(r, R.drawable.si_frostbite);
		attacked = new ArrayList<Posn>();
		frames = 7;
		height = effect.getHeight() / 1; //5 rows
		width = effect.getWidth() / 7; //5 columns
		square = new AttackSquare(ov, r);

		xCenter = 10;
		yCenter = -55;

		name = "Frost Bite";
	}

}
