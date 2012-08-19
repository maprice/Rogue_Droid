package items;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.mike.rogue.R;
import com.mike.rogue.Main.OurView;

import dungeonGen.NRandom;

public class Item {

	private String[] weaponSyn = {"Battle Staff","Elemental Staff", "Quarter Staff"};
	private String[] chestSyn = {"Chest","Hauberk", "Armor", "Breast Plate"};
	private String[] headSyn = {"Helm","Mask", "Helmet", "Circlet"};
	private String[] ringSyn = {"Ring","Finger", "Band", "Hoop", "Eye"};
	private String[] suffix = {"Eagle","Lizard", "Bear" , "Turtle"};

	public enum ItemType {WEAPON, HEAD, CHEST, RING, LEG}
	public enum Stat {ATTACK(1), DEFENCE(2), HEALTH(4), MANA(8);
	private Stat (int value)
	{
		this.value = value;
	}
	private int value;}

	public ItemType type;
	static int x;
	public String name;
	public int quality;
	double qualityLevel;
	public int levelReq;
	public Map<Stat, Integer> stats;
	//public String stats;
	//public int statsAmount
	public int sellPrice;
	int numStats;
	int suffixNum = 0;
	//public String type;
	Bitmap icon;
	NRandom rand;



	public Item(OurView ov, Resources re, int level) {
		rand = new NRandom();
		int randInt = rand.nextInt(4);

		rand = new NRandom();
		setQuality(level);
		stats = new HashMap<Stat, Integer>();

		switch(randInt){
		case 0:
			createWeapon(ov, re, level);
			break;
		case 1:
			createChest(ov, re, level);
			break;
		case 2:
			createHead(ov, re, level);
			break;
		case 3:
			createRing(ov, re, level);
			break;
		}

		addStats(level);

		randInt = new Random().nextInt(suffix.length);
		name = name+" of the "+ suffix[randInt];

		randInt = new Random().nextInt((int) (level+level*.2)) + 1;
		levelReq = randInt;


		sellPrice = (int) (qualityLevel*level+1);

		//	icon = BitmapFactory.decodeResource(re, R.drawable.i_+"type"+""+2);
		/*	name = "Sword of Cool";
			stats = "+8 Attack";
			levelReq = 3;
			qualityLevel = 0;
			quality = 	Color.BLUE;
			sellPrice = 46;
			type = "Weapon";
		 */

	}

	private void addStats(int level){
		int randInt;
		for (int i = 1; i < numStats; i++){
			Stat s;
			do{
				randInt = rand.nextInt(4);
				s = Stat.values()[randInt];
			}while (stats.containsKey(s));
			randInt = rand.nextInt(11)+20;
			double multiplier = randInt/10.0;
			suffixNum += s.value;
			stats.put(s, (int)((qualityLevel*level*multiplier)+1));
		}
	}

	private void createHead(OurView ov, Resources re, int level){
		int randInt = rand.nextInt(headSyn.length);
		//type = "Head";
		type = ItemType.HEAD;
		name = headSyn[randInt];
		stats.put(Stat.HEALTH, (int)((qualityLevel*level*.3)+1));
		//stats = "+"+(int)((qualityLevel*level*.3)+1)+" Health";
		randInt = new Random().nextInt(3);
		switch(randInt){
		case 0:
			icon = BitmapFactory.decodeResource(re, R.drawable.i_head1);
			break;
		case 1:
			icon = BitmapFactory.decodeResource(re, R.drawable.i_head2);
			break;

		case 2:
			icon = BitmapFactory.decodeResource(re, R.drawable.i_head3);	
			break;
		}
	}
	private void createChest(OurView ov, Resources re, int level){
		int randInt = rand.nextInt(chestSyn.length);
		//type = "Chest";
		type = ItemType.CHEST;
		name = chestSyn[randInt];
		stats.put(Stat.DEFENCE, (int)((qualityLevel*level*.3)+1));
		//stats = "+"+(int)((qualityLevel*level*.3)+1)+" Defence";
		randInt = new Random().nextInt(3);
		switch(randInt){
		case 0:
			icon = BitmapFactory.decodeResource(re, R.drawable.i_chest1);
			break;
		case 1:
			icon = BitmapFactory.decodeResource(re, R.drawable.i_chest2);
			break;

		case 2:
			icon = BitmapFactory.decodeResource(re, R.drawable.i_chest3);	
			break;
		}
	}
	private void createRing(OurView ov, Resources re, int level){
		int randInt = rand.nextInt(ringSyn.length);
		//type = "Ring";
		type = ItemType.RING;
		name = ringSyn[randInt];
		stats.put(Stat.MANA, (int)((qualityLevel*level*.3)+1));
		//stats = "+"+(int)((qualityLevel*level*.3)+1)+" Mana";
		randInt = new Random().nextInt(3);
		switch(randInt){
		case 0:
			icon = BitmapFactory.decodeResource(re, R.drawable.i_ring1);
			break;
		case 1:
			icon = BitmapFactory.decodeResource(re, R.drawable.i_ring2);
			break;

		case 2:
			icon = BitmapFactory.decodeResource(re, R.drawable.i_ring3);	
			break;
		}
	}
	private void createWeapon(OurView ov, Resources re, int level){
		int randInt = rand.nextInt(weaponSyn.length);
		type = ItemType.WEAPON;
		//type = "Weapon";
		name = weaponSyn[randInt];
		stats.put(Stat.ATTACK, (int) ((qualityLevel*level*.3)+1));

		randInt = rand.nextInt(3);
		switch(randInt){
		case 0:
			icon = BitmapFactory.decodeResource(re, R.drawable.i_staff1);
			break;
		case 1:
			icon = BitmapFactory.decodeResource(re, R.drawable.i_staff2);
			break;

		case 2:
			icon = BitmapFactory.decodeResource(re, R.drawable.i_staff3);	
			break;
		}
	}

	private void setQuality(int level){
		int percent = level;
		qualityLevel = rand.nextInt(100);
		if(qualityLevel > 90){
			quality = Color.rgb(165,56,198);//3
			numStats = 2;
		}else if(qualityLevel > 70){
			quality = 	Color.BLUE;//2
			numStats = 2;
		}
		else if(qualityLevel >40){
			quality = 	Color.GREEN;//2
			numStats = 1;
		}
		else if(qualityLevel > 20){
			quality = 	Color.WHITE;//1
			numStats = 1;
		}

		else {
			quality = 	Color.rgb(154,154,154);
			numStats = 1;
		}
		qualityLevel /= 10;
	}

	public String statLexeme(Stat s){
		String out = "";
		if (stats.containsKey(s)){
			//stats = "+"+(int)((qualityLevel*level*.3)+1)+" Health";
			out += "+" + stats.get(s) + " " + s.toString().charAt(0)+s.toString().toLowerCase().substring(1);
		}
		return out;
	}

	public Bitmap getIcon(){
		return icon;
	}


}
