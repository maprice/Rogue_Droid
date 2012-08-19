package dungeonGen;
import java.sql.Date;
import java.util.Random;


public class NRandom extends Random{
	/**
	 * 
	 */


	private long newSeed;

	public NRandom(){	
		newSeed = System.currentTimeMillis();
		this.setSeed(newSeed);
	}

	public NRandom(long newSeed){
		this.newSeed = newSeed;
		this.setSeed(newSeed);
	}
	public long getSeed(){
		return newSeed;
	}
}
