package hunter;

import java.awt.AWTException;
import java.awt.Color;
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
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

@ScriptManifest(category = Category.MINING, name = "Ratz Netting", author = "skengrat", 
version = 1.26)
public class RatzNetting extends AbstractScript {

	private final static String LIZARD_29 = "Swamp lizard";
	private final static String KEBBIT_57 = "Dark kebbit";
	public static final int[] dropOrder = {0,1,2,3,7,6,5,4,8,9,10,11,15,14,13,12,16,17,18,19,23,22,21,20,24,25,26,27};
	
	private State state;
	
	private static final Area LIZARD_AREA = new Area();
	private final static Filter<Item>LIZARD = new Filter<Item>() {
		public boolean match(Item k) {
			if(k == null) {
				return false;
			}
			return k.getName().equals(LIZARD_29);
		}
	};
	
	private final static Filter<Item>MATERIAL = new Filter<Item>() {
		public boolean match(Item k) {
			if(k == null) {
				return false;
			}
			return k.getName().equals("Rope") || k.getName().equals("Small fishing net");
		}
	};
	
	private final static Filter<GroundItem>G_MATERIAL = new Filter<GroundItem>() {
		public boolean match(GroundItem k) {
			if(k == null) {
				return false;
			}
			return k.getName().equals("Rope") || k.getName().equals("Small fishing net");
		}
	};
	
	public void onPaint(Graphics g) {
		g.setColor(new Color(247, 148, 230));
		g.drawString(state + "", 10, 90);
	}
	
	private enum State {
		HOP, SET, CHECK, 
	}
	
	public State getState() {
		if(getInventory().count("Rope") >= 1 && getInventory().count("Small fishing net") >= 1) {
			return State.SET;
		} else {
			return State.CHECK;
		}
	}
	
	public int onLoop() {
		state = getState();
		switch(state) {
		case SET:
			sleepUntil(() -> set(), 9999);
			break;
		case CHECK:
			sleepUntil(() -> check(), 9999);
			sleepUntil(() -> pickUp(), 9999);
			sleepUntil(() -> drop(), 9999);
			break;
		}
		//return Calculations.random(630, 910);
		return 0;
	}

	public boolean set() {
		GameObject tree = getGameObjects().closest(treex -> treex != null && treex.hasAction("Set-trap"));
		if(tree.interact("Set-trap")) {
			int material = getInventory().count(MATERIAL);
			if(sleepUntil(() -> getInventory().count(MATERIAL) < material,9999)) {
				if(sleepUntil(() -> getLocalPlayer().distance(tree) == 1, 9999)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean pickUp() {
		List<GroundItem>pickUp = getGroundItems().all(G_MATERIAL);
		if(!pickUp.isEmpty()) {
			for(GroundItem material : pickUp) {
				if(material.interact("Take")) {
					int emptySlots = getInventory().getEmptySlots();
					sleepUntil(() -> getInventory().getEmptySlots() < emptySlots, 9999);
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean check() {
		if(!getGameObjects().all(trap -> trap != null && trap.hasAction("Check")).isEmpty()) {
			if(getGameObjects().closest(trap -> trap != null && trap.hasAction("Check")).interact("Check")) {
				int count = getInventory().count(MATERIAL);
				if(sleepUntil(() -> getInventory().count(MATERIAL) > count, 9999)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean drop() {
		for(int i:dropOrder) {
			if(getInventory().slotContains(i, LIZARD_29)) {
				getInventory().drop(i);
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
