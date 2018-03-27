package sandCrabs;

import java.awt.Point;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.Item;


@ScriptManifest(author = "skengrat", name = "SandCrabs", version = 1.0, description = "killsCrabs, "
		+ "stand at the crab killing spot you want to kill the crabs in before starting", category = Category.COMBAT)
public class Main extends AbstractScript {

	Item food;
	Area bankArea = new Area(1722, 3463, 1719, 3466);
	Area resetArea = new Area(1785, 3500, 1763, 3501);
	Tile crabTile;
	Timer foodTimer;
	Timer resetTimer;
	GameObject bankChest;
	int resetTime;
	int eatTime;
	int mouseDestX;
	int mouseDestY;
	//hpDiff is the difference in hp required for us to eat
	int hpDiff = 6;
	boolean isReset = false;
	boolean hasBanked = false;
	
	
	public void onStart(){
		food = getInventory().get(f -> f!= null && (f.getName().equals("Trout") || f.getName().equals("Salmon")));
		foodTimer = new Timer();
		resetTimer = new Timer();
		crabTile = getLocalPlayer().getTile();
		
		resetTime = Calculations.random(600000, 700000);
		eatTime = Calculations.random(100000, 300000);
	}
	
	
	@Override
	public int onLoop() {
		// if no food in inventory, bank
		if (!getInventory().contains(food)){
			bank();
		} else {
			//check if we're low life and need to eat immediately
			if (getSkills().getBoostedLevels(Skill.HITPOINTS) < 10){
				food.interact();
			} else {
				//if we need to reset (time is up, and crabs aren't attacking us anymore), reset
				if ((resetTimer.elapsed() > resetTime) && !(getLocalPlayer().isInCombat())){
					reset();
				} else {
				//if we don't need to reset, check if we need to eat
					if ((foodTimer.elapsed() > eatTime) && 
							getSkills().getBoostedLevels(Skill.HITPOINTS) < (getSkills().getRealLevel(Skill.HITPOINTS) - hpDiff)){
						eat();
					} else {
						//if we don't need to eat, everything is fine, sleep
						getMouse().moveMouseOutsideScreen();
						sleep(500);
					}
				}
			}
		}
		
		return 600;
	}
	
	private void withdrawFood() {
		//329 = salmon, 333 = trout
		int foodid = 333;
		
		//if the bank is open
		if (getBank().isOpen()){

			if (getInventory().contains(foodid)){
				//if we actually withdrew that shit
				getBank().close();
				hasBanked = true;
			} else {
				//if we didn't withdraw, do the banking again
				getBank().withdraw(foodid, 28);
				sleep(300);
			}
		} else {
			//if bank not open, open it
			bankChest = getGameObjects().closest(chest -> chest != null && chest.hasAction("Use") && chest.getName().equals("Bank chest"));
			bankChest.interact();
			//some area for the mouse to move to; anti-ban
			getMouse().move(new Point(mouseDestX, mouseDestY));
			sleep(100);
		}
		return;
	}
	
	private void reset(){
		//if reset hasn't happened yet,
		if (!isReset){
			if (resetArea.contains(getLocalPlayer())){
				isReset = true;
			} else {
				getWalking().walk(resetArea.getRandomTile());
			}
		} else {
			//if reset has happened, go walk to the starting tile
			if (getLocalPlayer().getTile().equals(crabTile)){
				//if he walked back to the starting tile, reset the timer and boolean
				resetTimer.reset();
				isReset = false;
				resetTime = Calculations.random(600000, 700000);
			} else {
				getWalking().walk(crabTile);
			}
		}
		return;
	}
	
	private void eat(){
		while(getSkills().getBoostedLevels(Skill.HITPOINTS) < (getSkills().getRealLevel(Skill.HITPOINTS) - hpDiff)){
		food.interact();
		sleep(400);
		}
	}
	
	private void bank(){
		//if player has banked, go back to crab tile
		if (hasBanked){
			if (getLocalPlayer().getTile().equals(crabTile)){
				//if on the crab tile, do nothing
				getMouse().moveMouseOutsideScreen();
				sleep(300);
			} else {
				//if not on crab tile, go back
				getWalking().walk(crabTile);
				sleep(600);
			}
		} else {
			//if not banked
			if (!bankArea.contains(getLocalPlayer())){
				//if player not in banking area, walk there
				getWalking().walk(bankArea.getRandomTile());	
			}
			else {
				// change mouseX and Y, withdrawFood
				mouseDestX = Calculations.random(200, 266);
				mouseDestY = Calculations.random(130, 190);
				//if player in banking area, bank
				withdrawFood();
			}
		}
	}

}
