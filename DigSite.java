package digsite;


import java.awt.Color;
import java.awt.Graphics;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.message.Message;

import antiBan.Anti;
import antiBan.Anti.Statex;


@ScriptManifest(category = Category.FISHING, name = "DigSite", author = "skengrat", 
version = 3)
public class DigSite extends AbstractScript{

	public static final int[] dropOrder = {8,9,10,11,15,14,13,12,16,17,18,19,23,22,21,20,24,25,26,27,28};
	private final Area rosaldo = new Area(2807, 3439, 2811, 3441);
	private final Area fish = new Area(3099,3422,3110,3436);
	
	private final String TOOL = "Rock pick";
	private State state;
	
	Anti x;
	
	private String line = "";
	
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
	
	public void onMessage(Message message) {
		line = message.toString();
	}
	
	
	public void onStart() {
		x = new Anti(getLocalPlayer().getName());
		x.setState();
		x.forceStateActive(true);
		state = state.FISH;
		
	}
	
	public enum State {
		DROP, FISH
	}
	
	public State setState() {
		if(getInventory().isFull() || state == state.DROP) {
			if(getInventory().getEmptySlots() > 16) {
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
			if(getInventory().slotContains(position, "Uncleaned find")) {
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
		if(!getLocalPlayer().isAnimating()) {
			if(getInventory().interact(TOOL, "Use")) {
				if(getMouse().click(getGameObjects().closest(x -> x != null && x.getName().equals("Soil")).getBoundingBox())) {
					sleepUntil(() -> !getLocalPlayer().isAnimating(), 4444);
				}
			}
		}
		return 0;
	}

	@Override
	public int onLoop() {
		//x.switchState();
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
