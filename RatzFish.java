package fishing;


import java.awt.Color;
import java.awt.Graphics;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;

import antiBan.Anti;
import antiBan.Anti.Statex;


@ScriptManifest(category = Category.FISHING, name = "Ratz Fisher", author = "skengrat", 
version = 3)
public class RatzFish extends AbstractScript{

	public static final int[] dropOrder = {2,3,7,6,5,4,8,9,10,11,15,14,13,12,16,17,18,19,23,22,21,20,24,25,26,27,28};
	private final Area rosaldo = new Area(2807, 3439, 2811, 3441);
	private final Area fish = new Area(3099,3422,3110,3436);
	
	private State state;
	
	Anti x;
	
	boolean droppedOnce = false;

	public void onPaint(Graphics g) {
		g.setColor(new Color(247, 148, 230));
		g.drawString(state + "", 10, 90);
		g.drawString("State: " + x.state + "", 10, 100);
		g.drawString("Seed: " + x.seed + "", 10, 120);
		g.drawString("smallDeviation: " +x.smallDeviation + "", 10, 140);
		g.drawString("medDeviation: " +x.medDeviation + "", 10, 160);
		g.drawString("waitTime: " +x.waitTime + "", 10 , 180);
		g.drawString("Anti time: " + x.time + "", 10, 200);
	}
	
	public void onStart() {
		x = new Anti(getLocalPlayer().getName());
		x.setState();
		state = state.DROP;
		
	}
	
	public enum State {
		DROP, FISH
	}
	
	public State setState() {
		if(getInventory().isFull() || state == state.DROP) {
			if(getInventory().count(fish -> fish != null && fish.getName().contains("Raw") || fish.getName().contains("Clue")) < 3) {
				return state.FISH;
			}
			return state.DROP;
		} else {
			return state.FISH;
		}
	}
	
	private boolean drop() {
		getKeyboard().pressShift();
		if(!droppedOnce) {
			droppedOnce = true;
		}
		for(int position: dropOrder) {
			if(position == 28) {
				continue;
			}
			if((Math.random() * 69) < 1) {
				if((position >= 24 && position <= 27) && Math.random() * 7 < 4) {
					getMouse().move(getInventory().slotBounds(position));
					continue;
				} else {
					getMouse().move(getInventory().slotBounds(position));
					continue;
				}
			}
			getMouse().click(getInventory().slotBounds(position));
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


		//break here to see move in advance
		for(int i = 2; i <= 24; i++) {
			if(getInventory().slotContains(i, (item -> item != null && item.getName().contains("Raw") || item.getName().contains("Clue")))) {
				getMouse().click(getInventory().slotBounds(i));
			}
		
		}
		getKeyboard().releaseShift();
		state = state.FISH;
		return true;
	}
	
	public int bank() {
		if(rosaldo.contains(getLocalPlayer())) {
			if(getBank().isOpen()) {
				if(getInventory().getEmptySlots() != 27) {
					getBank().depositAllExcept("Lobster pot");
				} 
			} else {
				GameObject bank = getGameObjects().closest(bank_ -> bank_ != null && bank_.hasAction("Bank"));
				bank.interact("Bank");
				sleepUntil(() -> getBank().isOpen(), Calculations.random(715, 1325));
			}
		} else {
			getWalking().walk(rosaldo.getRandomTile());
			return Calculations.random(1121,1955);
		}
		return Calculations.random(721,955);
	}
	
	public int fish() {
		if(droppedOnce) {
			for(int i = 25; i <= 27; i++) {
				if(getInventory().slotContains(i, (item -> item != null && item.getName().contains("Raw") || item.getName().contains("Clue")))) {
					//getMouse().click(getInventory().slotBounds(i));
				}
			
			}
		}
		if(fish.contains(getLocalPlayer())) {
			if(getLocalPlayer().getAnimation() == -1) {
				if(getNpcs().closest(spot -> spot != null && spot.hasAction("Lure")).interact("Lure")) {
					if(sleepUntil(() -> getLocalPlayer().getAnimation() != -1, Calculations.random(4198))) {
						droppedOnce = false;
						sleepUntil(() -> getLocalPlayer().getAnimation() == -1, Calculations.random(31777, 55555));
					}
				}
			} else {
				
			}
		} else {
			getWalking().walk(fish.getRandomTile());
		}
		return Calculations.random(4412,9374);
	}

	@Override
	public int onLoop() {
		x.switchState();
		state = setState();
		switch(state) {
		case DROP:
			sleepUntil(() -> drop() == true, Calculations.random(11000,12330));
			return 0;
		case FISH:
			fish();
			break;
		}


		return x.stateWaitTime();
	}
}
