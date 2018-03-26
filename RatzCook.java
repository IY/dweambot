package cooking;

import java.awt.Color;
import java.awt.Graphics;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;
import org.dreambot.api.methods.Calculations;

import antiBan.*;
import antiBan.Anti.Statex;

@ScriptManifest(category = Category.COOKING, name  = "Ratz Cook", author = "skengrat",
version = 1.0)
public class RatzCook extends AbstractScript {

	int cooked = 0;
	int burned = 0;
	
	String food = "Sweetcorn";
    //String cookingSource = "Clay oven";
	String cookingSource = "Fire";
    
    int COOKING_WIDGET_P = 307;
    int COOKING_WIDGET_C = 2;
    final int COOKING_WIDGET_K_P = 303;
    final int COOKING_WIDGET_K_C = 3;
    
    int inventoriesFinished = 0;
    Area cookingArea = new Area(1652, 3610, 1657, 3613);
    private State state;
    
    Anti x;

    boolean cooking;
    
    boolean karambwan = true;
    GameObject activeCookingSource;
    
	public void onPaint(Graphics g) {
		g.setColor(new Color(247, 148, 230));
		g.drawString("inventoriesFinished: " + inventoriesFinished, 10, 60);
		g.drawString(state + "", 10, 90);
		g.drawString("State: " + x.state + "", 10, 100);
		g.drawString("Seed: " + x.seed + "", 10, 120);
		g.drawString("smallDeviation: " +x.smallDeviation + "", 10, 140);
		g.drawString("medDeviation: " +x.medDeviation + "", 10, 160);
		g.drawString("waitTime: " +x.waitTime + "", 10 , 180);
		g.drawString("time: " + x.time + "", 10, 200);
		g.drawString("cooking: " + cooking + "", 10, 210);
		g.drawString("cooked: " + cooked + "", 10, 220);
		g.drawString("burned: " + burned + "", 10, 230);
	}
	
	
	public void onStart() {
		x = new Anti(getLocalPlayer().getName());
		//x.setState();
		state = state.COOK;
		x.forceStateActive(true);
		if(karambwan) {
			food = "Raw karambwan";
			cookingSource = "Clay oven";
			COOKING_WIDGET_P = COOKING_WIDGET_K_P;
			COOKING_WIDGET_C = COOKING_WIDGET_K_C;
		}
	}
	
    public enum State {
    	COOK, BANK;
    }
    
    private State getState() {
    	if(getInventory().contains(food)) {
    		return State.COOK;
    	} else {
    		return State.BANK;
    	}
    }
    
	@Override
	public int onLoop() {
		//x.switchState();
		state = getState();
		switch(state) {
		case COOK:
			if(!getLocalPlayer().isAnimating()) {
				cook();
			}
			break;
		case BANK:
			bank();
			break;
		}
		return x.stateWaitTime();
	}

	public void cook() {
		if(!cooking) {
			Item raw = getInventory().get(x -> x != null && x.getName().equals(food));
			GameObject fire = getGameObjects().closest(x -> x != null && x.getName().equals(cookingSource));
			activeCookingSource = fire;
			if(!getLocalPlayer().isAnimating()) {
				if(raw.useOn(fire)) {
					sleep(Calculations.random(2200,4000));
					if(sleepUntil(()-> getDialogues().inDialogue(), 5779)) {
						final Widget par = getWidgets().getWidget(COOKING_WIDGET_P);
						WidgetChild child = null;
						if (par != null) {
							child = par.getChild(COOKING_WIDGET_C);
						} else {
							return;
						}
						sleepUntil(() -> getDialogues().inDialogue(), Calculations.random(3000));
						if (child != null && child.isVisible()) {
							if(karambwan) {
								if(child.interact("Make X")) {
								sleepUntil(
										() -> getDialogues().inDialogue() == true,
										2000);
								sleep(Calculations.random(300, 600));
								int typ = Calculations.random(3, 9);
								getKeyboard()
										.type((Integer.toString(typ)
												+ Integer.toString(typ) + Integer
												.toString(typ)),
												true);
								cooking = true;
								sleepUntil(() -> !getDialogues().inDialogue(), 50000);
								return;
								
								} else {
									cooking = false;
								}
							}
							if(child.interact("Cook All")) {
								cooking = true;
							} else {
								cooking = true;
							}
							sleepUntil(() -> !getDialogues().inDialogue(), 50000);
						}
					}
				}
			}
		} else {
			} if(getDialogues().inDialogue() || !activeCookingSource.exists()) {
				cooking = false;
			}
	}
	
	public boolean bank() {
		cooking = false;
		GameObject bank = getGameObjects().closest(Bank -> Bank != null && Bank.hasAction("Use") && Bank.getName().contains("Bank"));
		//NPC bank = getNpcs().closest(x -> x != null && x.hasAction("Bank"));
		if (!getBank().isOpen()) {
			if(sleepUntil(() -> bank.interact("Use"), 3000)) {
			//if(sleepUntil(() -> bank.interact("Bank"), 3000)) {
				sleepUntil(() -> getBank().isOpen(), Calculations.random(6113,8244));
				bank();
				}
			} else {
				if(getBank().count(food) < 10) {
					sleepUntil(() ->getBank().close(),1323);
					getTabs().logout();
					stop();
				}
				if(getInventory().isFull()) {
					sleepUntil(() -> getBank().depositAllItems(), 3000);  
				}
				sleepUntil(() -> !getInventory().isEmpty(), Calculations.random(1200,1500));
				if (sleepUntil(() -> getBank().withdrawAll(food),7000)) {
						sleepUntil(() -> getInventory().contains(food), Calculations.random(1200,1500));
						getWalking().walk(cookingArea.getRandomTile());

						//getBank().close();
						inventoriesFinished++;
						} else {
							getBank().close();
							getTabs().logout();
							stop();
				}
			}
		return false;
	}
}