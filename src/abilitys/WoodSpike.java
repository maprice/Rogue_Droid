package abilitys;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import com.mike.rogue.AttackSquare;
import com.mike.rogue.Main;
import com.mike.rogue.R;
import com.mike.rogue.Main.OurView;
import com.mike.rogue.R.drawable;

public class WoodSpike extends AttackType{


	int[][] basic = {
			{ 1, 1, 1},
			{ 0, 1, 0},
			{ 0, 0, 0},
			{ 0, 0, 0},
	};

	public WoodSpike(OurView ourView, Resources r) {
		ov = ourView;
		attackGrid = basic;
		damage = 90;
		effect = BitmapFactory.decodeResource(r, R.drawable.se_woodspike);	
		icon = BitmapFactory.decodeResource(r, R.drawable.si_woodspikecion);
		attacked = new ArrayList<Posn>();
		frames = 7;
		height = effect.getHeight() / 1; //5 rows
		width = effect.getWidth() / 7; //5 columns
		square = new AttackSquare(ov, r);

		xCenter = 20;
		yCenter = -30;

		name = "Wood Spike";
	}

}
