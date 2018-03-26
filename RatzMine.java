package mining;

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
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.Item;

@ScriptManifest(category = Category.MINING, name = "Ratz Mine", author = "skengrat", 
version = 1.26)
public class RatzMine extends AbstractScript {
	
	//0123
	//4567
	//8901
	//2345
	//6789
	//0123
	//4567
	
	public static final int[] DROP_ORDER = {0,1,2,3,7,6,5,4,8,9,10,11,15,14,13,12,16,17,18,19,23,22,21,20,24,25,26,27};
	private final static Area MINING_AREA = new Area(3032, 9825,3033,9826);

	private boolean minedOnce = false;
	public boolean POWER = true;
	public State state;
	
	private static final int MINING_ID = 7488;
	private static final int MINING_ID2 = 7455;
	
	private ArrayList <Integer>MiningRocks = new ArrayList<Integer>();
	
	//fally mine north
	//final Tile rockSouth = new Tile(3033,9825);
	//final Tile rockWest = new Tile(3032,9826);
	
	//zammy mage
	//final Tile rockSouth = new Tile(3105,3569);
	//final Tile rockWest = new Tile(3104,3570);
	
	//deep wild
	final Tile rockSouth = new Tile(3089,3768);
	final Tile rockWest = new Tile(3088,3769);
	final Tile rockThree = new Tile(3090, 3768);
	final Tile rockFour = new Tile(3091, 3768);
	List<GameObject> allRocks;
	List<Tile> tileRocks;
	
	GameObject currentRock;
	Tile currentTile;
	boolean hasClicked = false;
	
	int playerCount = 0;
	public void onStart() {
		state = State.MINE;
		MiningRocks.add(MINING_ID);
		allRocks = getGameObjects().all(u -> u != null && u.getID() == MINING_ID || u.getID() == MINING_ID2);
		tileRocks = new ArrayList<Tile>();
		tileRocks.add(rockSouth);
		tileRocks.add(rockWest);
		tileRocks.add(rockThree);
		//tileRocks.add(rockFour);
	}

	int orientation = 0;
	
	public enum State {
		DROP, MINE, HOPWORLDS
	}
	
	public State setState() {
		if((getPlayers().all(player -> player != null && MINING_AREA.contains(player)).size() > 1)) {
			return State.HOPWORLDS;
		}
		else if(getInventory().isFull() || state == state.DROP) {
			if(getInventory().count(ore -> ore != null && ore.getName().contains("ore") || ore.getName().contains("cut")) < 4) {
				return state.MINE;
			}
			return state.DROP;
		} else {
			return state.MINE;
		}
	}
	
	public boolean hopWorlds() {
		Random r = new Random();
		return sleepUntil(() -> getWorldHopper().quickHop(getWorlds().f2p().get(r.nextInt()).getID()), 20000);
	}
	@Override
	public int onLoop() {
		state = setState();
		switch(state) {
		case HOPWORLDS:
			//hopWorlds();
			break;
		case DROP:
			//getInventory().dropAll((ore -> ore != null  && (ore.getName().contains("Tin") || ore.getName().contains("ore") || ore.getName().contains("cut"))));
			drop();
			return 0;
		case MINE:
			//hopWorlds();
			playerCount = getPlayers().all(player -> player != null && MINING_AREA.contains(player)).size();
			
			if(getLocalPlayer().isInCombat()) {
				break;
			}
			int count = getInventory().count("Iron ore");
			if(getGameObjects().all(u -> u != null && u.getID() == MINING_ID || u.getID() == MINING_ID2).isEmpty()) {
				//sleepUntil(() -> drop(), 12345);
				getInventory().dropAll((ore -> ore != null  &&  ore.getName().contains("ore") || ore.getName().contains("cut")));
					//sleepUntil(() -> getInventory().count(ore -> ore != null  &&  (ore.getName().contains("ore") || ore.getName().contains("cut"))) == 0, 13333);
				//}
				//drop();
				hoverNext();
			}
			if(!minedOnce) {
				sleepUntil(() -> mine(), 12345);
			} else {
				sleepUntil(() -> mineAfter(), 12345);
			}
			/*
			getMouse().move(tileRocks.get(0));
			sleep(Calculations.random(3000));
			getMouse().move(tileRocks.get(1));
			*/
			if( getInventory().count("Iron ore") > count) {
				hasClicked = false;
			}
			break;
		}
		return Calculations.random(778,1337);
	}

	private boolean drop() {
		getKeyboard().pressShift();
		for(int position: DROP_ORDER) {
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
			if(getInventory().slotContains(i, (item -> item != null && item.getName().contains("ore") || item.getName().contains("cut")))) {
				getMouse().click(getInventory().slotBounds(i));
			}
		
		}
		getKeyboard().releaseShift();
		state = State.MINE;
		return true;
	}
	
	private boolean mine() {
		if(getLocalPlayer().getAnimation() == -1) {
			currentRock = getGameObjects().closest(u -> u != null && u.getID() == MINING_ID || u.getID() == MINING_ID2);
			currentTile = currentRock.getTile();
			if(!hasClicked) {
			if(currentRock.interact("Mine")) {
				//hasClicked = true;
				//sleep(Calculations.random(5, 444));
				if(sleepUntil(() -> getLocalPlayer().getAnimation() > 600, 4444)) {
					sleepUntil(() -> hoverNext(), 3000);
				}
					if(sleepUntil(() -> getLocalPlayer().getAnimation() != -1, Calculations.random(6198))) {
						hasClicked = false;
						if(sleepUntil(() -> getLocalPlayer().getAnimation() == -1, Calculations.random(31777, 55555))) {
						minedOnce = true;
						return true;
					}
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		}
		return false;
	}
	
	private boolean mineAfter() {
		
		if(getLocalPlayer().getAnimation() == -1) {
			if(!getMouse().getEntitiesOnCursor().isEmpty()) {
			if(getMouse().getEntitiesOnCursor().get(0).getID() == MINING_ID || getMouse().getEntitiesOnCursor().get(0).getID() == MINING_ID2) {
				if(!hasClicked) {
				if(getMouse().click()) {
					//hasClicked = true;
					if(currentTile.getX() == tileRocks.get(0).getX()) {
						//currentTile = tileRocks.get(1);
					} else {
						//currentTile = tileRocks.get(0);
					}

					//sleep(Calculations.random(5, 444));
					if(sleepUntil(() -> getLocalPlayer().getAnimation() > 600, 4444)) {
						sleepUntil(() -> hoverNext(), 3000);
					}
					if(sleepUntil(() -> getLocalPlayer().getAnimation() != -1, Calculations.random(6198))) {
						hasClicked = false;
						if(sleepUntil(() -> getLocalPlayer().getAnimation() == -1, Calculations.random(31777, 55555))) {
							return true;
						}
					}
				} else {
					return false;
				}
				}
			} else {
				if(!hasClicked) {
				return sleepUntil(() -> mine(), 7000);
				}
			}
		} else {
			if(!hasClicked) {
				return sleepUntil(() -> mine(), 7000);
				}
		}
		} 
		return false;
	}

	public boolean hoverNext() {
		for(int i = 0; i < tileRocks.size(); i++) {
			if(currentTile.getX() == tileRocks.get(i).getX() && currentTile.getY() == tileRocks.get(i).getY()) {
				currentTile = tileRocks.get((i+1)%(tileRocks.size() ));
				getMouse().move(tileRocks.get((i+1)%(tileRocks.size())));
				return true;
			}
		}
		return false;
	}
	
	public void onPaint(Graphics graphics) {
		graphics.drawString("playerCount" + playerCount + "", 10, 60);
		graphics.drawString("hasClicked" + hasClicked  + "", 10, 70);
		graphics.drawString(allRocks.toString() + "", 10, 80);
		graphics.drawString(currentRock.getTile() + "", 10, 90);
		graphics.drawString(tileRocks.toString() + "", 10, 110);
		graphics.drawString(state + "", 10, 100);
	}
}
