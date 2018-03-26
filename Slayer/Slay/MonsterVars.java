package Slay;
import java.util.ArrayList;

import slayerMonsters.*;

public class MonsterVars {

	public String monsterName;
	public boolean usePrayer;
	public SlayerTask currentTask;
	public ArrayList<Loot> loots = new ArrayList<Loot>();
	public ArrayList<String> potions =  new ArrayList<String>();
	public SlayerTask[] slayerTasks = new SlayerTask[] {
			new Dust_devil(), 
			new Kurask(), 
			new Mutated_Bloodveld(), 
			new Turoth()	
			};

}
