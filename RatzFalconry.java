package hunter;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.awt.Robot;



import java.awt.event.KeyEvent;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;

@ScriptManifest(category = Category.MINING, name = "Ratz Falconry", author = "skengrat", 
version = 1.26)
public class RatzFalconry extends AbstractScript {

	private final static String KEBBIT_43 = "Spotted kebbit";
	private final static String KEBBIT_57 = "Dark kebbit";
	public static final int[] dropOrder = {0,1,2,3,7,6,5,4,8,9,10,11,15,14,13,12,16,17,18,19,23,22,21,20,24,25,26,27};
	
	public Filter<NPC>kebbit = new Filter<NPC>() {
		public boolean match(NPC k) {
			if(k == null || !k.exists()) {
				return false;
			}
			return k.getName().equals(KEBBIT_43);
		}
	};
	
	public int onLoop() {
		if(getNpcs().closest(kebbit).interact("Catch")) {
			int bones = getInventory().count("Bones");
			if(sleepUntil(() -> getInventory().count("Bones") > bones, 29999)) {
				drop();
			}
		}
		return Calculations.random(1130, 1900);
	}

	public boolean drop() {
		for(int i:dropOrder) {
			if(getInventory().slotContains(i, fur -> fur != null && fur.getName().contains("fur"))) {
				getInventory().drop(i);
			} else if(getInventory().slotContains(i, "Bones")) {
				getInventory().interact(i, "Bury");
			}
			int random = (int)(Math.random() * 4);
			switch(random) {
			case 0:
				sleep(Calculations.random(44,115));
				break;
				
			case 1:
				sleep(Calculations.random(44,115));
				break;
			
			case 2:
				sleep(Calculations.random(114,270));
				break;
				
			default:
				break;
			}
		}
		return true;
	}
}
