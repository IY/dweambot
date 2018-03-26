package crafting;

import org.dreambot.api.input.event.impl.camera.CameraEvent;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.widgets.WidgetChild;
import org.dreambot.api.methods.map.Area;

import antiBan.Anti;
import antiBan.Anti.Statex;

import java.awt.*;
import java.util.List;

@ScriptManifest(category = Category.CRAFTING, name = "Bracelet Crafter", author = "skengrat", version = 4.03)
public class GoldBracelets extends AbstractScript {

	public int inventoriesFinished = 0;
	
	private final int SMELTING_PARENT = 446;
	private final int SMELTING_PAENT_GOLD_Bracelet = 44;
	
	
	private final Area bankArea = new Area(3094, 3494, 3099, 3497);
	private final Area craftArea = new Area(3106, 3498, 3109, 3501);
	
	
	private State state;

	long start = 0;
	long time = 0;
	Anti x;
	int invs = 0;
	int animating = 0;
	private boolean smelting = false;
	
	private enum State {
		CRAFT, BANK, END
	}
	
	private State getState() {
		if (getInventory().contains("Gold bar")) {
			return State.CRAFT;
		} else if(getBank().isOpen() && !getBank().contains("Gold bar")) {
			return State.END;
		} else {
			return State.BANK;
		}
	}

	public void onStart() {
		x = new Anti(getLocalPlayer().getName());
		x.setState();
	}
	
	@Override
	public int onLoop() {
		if(!hasMaterials()) {
			return -1;
		}

		random();
		
		time = System.currentTimeMillis() - start;
		if(x.state == Statex.ACTIVE) {
			//switch states after 51 secs + (0-4) mins
			if((time > (51000 + (int)(Math.random()) * 237135))) {
				switchState();
			}
		} else {
			//switches states after 8 minutes + (0-23) mins
			if((time > (471321 + (int)(Math.random()) * 1413789))) {
				switchState();
			}
		}
		
		state = getState();
		switch (state) {

		case CRAFT:
			crafting();
			break;

		case BANK:
			banking();
			break;

		case END:
			stop();
			break;
			
		default:
			return -1;
		}

		if((((int)Math.random()) * 2) == 0) {
			return x.waitTime + Calculations.random(x.seed);
		} else {
			return x.waitTime - Calculations.random(x.seed);
		}
	}

	public boolean hasMaterials() {
		if (!(getInventory().contains("Bracelet mould") || !getBank().contains("Gold bar"))) {
			return false;
		}
		return true;
	}

	public boolean banking() {
		if (bankArea.contains(getLocalPlayer())) {
			GameObject bank = getGameObjects().closest(
					Bank -> Bank != null && Bank.hasAction("Bank"));
			if (!getBank().isOpen()) {
				if (sleepUntil(() -> bank.interact("Bank"), 3000)) {
					sleep(Calculations.random(200,1000));
					}
				} else {
					sleepUntil(() -> getBank().depositAllExcept("Bracelet mould"), 3000); 
						sleepUntil(() -> !getInventory().contains("Gold Bracelet"), Calculations.random(1200,1500));
						if (sleepUntil(
								() -> getBank().withdrawAll("Gold bar"),
								7000)) {
							sleepUntil(() -> getInventory().contains("Gold bar"), Calculations.random(1200,1500));
								inventoriesFinished++;
						} else {
							getBank().close();
							getTabs().logout();
							stop();
						}
					}
				} else {
					smelting = false;
					getWalking().walk(bankArea.getRandomTile());
					sleepUntil(() -> bankArea.contains(getLocalPlayer()), 5127);
			}
		return false;
	}

	public void crafting() {
		if (craftArea.contains(getLocalPlayer())) {
			if (!getLocalPlayer().isAnimating() && !smelting) {
				if (getInventory().interact("Gold bar", "Use")) {
					sleepUntil(()->getInventory().isItemSelected(), Calculations.random(800,1200));
					GameObject furnace = getGameObjects().closest(
							furnace_ -> furnace_ != null
									&& furnace_.hasAction("Smelt"));
					sleepUntil(() -> furnace != null, 3000);
					if (getMouse().click(randomizePoint(furnace.getClickablePoint()))) {
						sleep(Calculations.random(2200,4000));
						final Widget par = getWidgets().getWidget(SMELTING_PARENT);
						WidgetChild chil = null;
						if (par != null) {
							chil = par.getChild(SMELTING_PAENT_GOLD_Bracelet);
						} else {
							return;
						}
						if (chil != null && chil.isVisible()) {
							chil.interact("Make-X");
							sleepUntil(
									() -> getDialogues().inDialogue() == false,
									2000);
							sleep(Calculations.random(300, 600));
							int typ = Calculations.random(3, 9);
							getKeyboard()
									.type((Integer.toString(typ)
											+ Integer.toString(typ) + Integer
											.toString(typ)),
											true);
							smelting = true;
						}
						if (!chil.isVisible()) {
							getMouse().click();

						}
					} else {
						return;
					}
				} else {
					getMouse().click();
				}
			} else {
				if(getLocalPlayer().isAnimating()) {
					animating = (int)System.currentTimeMillis();
				}
				//if(getDialogues().inDialogue()) {
				//	smelting = false;
				//}
				if(!getLocalPlayer().isAnimating()) {
					if((System.currentTimeMillis() - animating) > ((x.waitTime/2) + 894)) {
						animating = 0;
						smelting = false;
					}
				}
				
				if(Statex.ACTIVE == x.state) {
					switch((int)(Math.random() * 5)) {
					case 0:
						random();
						break;
					
					case 1:
						random();
						break;
					case 2:
						random();
						break;
					}
				} else {
					switch((int)(Math.random() * 5)) {
					case 0:
						random();
						break;
					}
				}
				
			}
		} else {
			smelting = false;
			getWalking().walk(craftArea.getRandomTile());
			sleepUntil(() -> craftArea.contains(getLocalPlayer()), 5127);
		}
	}

	
	
	public void onPaint(Graphics g) {
		g.setColor(new Color(247, 148, 230));
		g.drawString("Inventories Finished: " + inventoriesFinished, 10, 60);
		g.drawString(smelting + "", 10, 80);
		g.drawString(x.state + "", 10, 100);
		g.drawString(x.seed + "", 10, 120);
		g.drawString(x.smallDeviation + "", 10, 140);
		g.drawString(x.medDeviation + "", 10, 160);
		g.drawString(x.waitTime + "", 10 , 180);
		g.drawString(time + "", 10, 200);
		g.drawString(invs + "", 10, 220);
	}

	public void camera() {
		//if(((int)(Math.random() * 12)) == 1) {
			//yaw, pitch
			//0-2047, 128 - 383
			new CameraEvent(getClient(), (int)(Math.random() * 2047), (int)(Math.random() * 255) + 138).start();
		//}
	}

	public void players() {
		List<Player> players = getPlayers().all(player -> player != null && player.getTile().distance(getLocalPlayer()) < x.smallDeviation);
		if(players.size() > 1) {
			getMouse().click(players.get((int)Math.random() * players.size()), true);
			sleep(x.waitTime);
			getMouse().move(new Point(getMouse().getX() - (int)(Math.random() * 100), getMouse().getY() -(int)(Math.random() * 100)));
		}
	}

	public void things() {
		List<GameObject> objects = getGameObjects().all(game -> !game.getName().equals(null) && game.isOnScreen());
		if(objects.size() > 1) {
			getMouse().click(objects.get((int)(Math.random() * objects.size())), true);
			sleep(x.waitTime);
			getMouse().move(new Point(getMouse().getX() - (int)(Math.random() * 100), getMouse().getY() -(int)(Math.random() * 100)));
		}
	}
	
	public void tabs() {
		//if((int)(Math.random() * 1823) == 0) {
			if(getTabs().isOpen(Tab.STATS)) {
				sleepUntil(() -> getSkills().hoverSkill(Skill.CRAFTING), 3000);
				if(getTabs().isOpen(Tab.INVENTORY)) {
					return;
				} else {
					getTabs().open(Tab.INVENTORY);
				}
			} else {
				getTabs().open(Tab.STATS);
			}
		}
	//}
	
	public void switchState() {
		System.out.println("Switch");
		time = 0;
		start = System.currentTimeMillis();
		x.setState();
	}
	
	public void random() {
		for(int i = 0; i < 3; i++) {
			if(x.state == Statex.ACTIVE) {
				if(((int)Math.random() * x.medDeviation) == 2 || (((int)Math.random()) * x.medDeviation) == 3 || (((int)Math.random()) * x.medDeviation) == 4) {
					switch(i) {
					case 0:
						camera();
						break;
					case 1:
						players();
						break;
					case 2:
						tabs();
						break;
					
					case 3: 
						things();
						break;
					}
				}
			} else {
				if(((int)Math.random() * x.medDeviation) == 2) {
					switch(i) {
					case 0:
						camera();
						break;
					case 1:
						players();
						break;
					case 2:
						tabs();
						break;
					case 3: 
						things();
						break;
					}
				}
			}
		}
	}
	
	private Point randomizePoint(Point p){
		return new Point(p.x + Calculations.random(-2,2), p.y + Calculations.random(-2,2));
	}
}
