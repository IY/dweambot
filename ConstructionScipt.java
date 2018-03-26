package construciton;

import java.awt.Graphics;
import java.util.function.Consumer;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;

import base.AbstractFeaturedScript;
import base.Task;
import base.Task.TaskBody;

@ScriptManifest(author = "skengrat", name = "Construction", version = 1.0, description = "", category = Category.CONSTRUCTION)
public class ConstructionScipt extends AbstractFeaturedScript{

	private boolean building = false;
	private boolean gotButler = false;
	private final int RING_ID_0 = 2552;
	private final Tile RIMMINGTON = new Tile(2953, 3224, 0);

	private Task init = new Task("Init", new TaskBody() {
		@Override
		public int execute(){

			if(getLocalPlayer().distance(RIMMINGTON) < 10){
				GameObject g = getGameObjects().closest(15478);
				if(g != null){
					g.interact();
					conditionalSleep(() -> getDialogues().inDialogue() && getDialogues().getOptions() != null, 2000, 3000);
					Tile lastTile = getLocalPlayer().getTile();
					getDialogues().chooseOption(2);
					
					conditionalSleep(()->getLocalPlayer().distance(lastTile) > 20, 3000, 4500);
					if(getLocalPlayer().distance(lastTile) > 20){
						GameObject g3 = getGameObjects().closest(15403);
						GameObject g2 = getGameObjects().closest(13566);
						if(g2 != null || g3 != null){
							
							getWalking().walk(g2 == null ? g3 : g2);
							walkingSleep();
							sleep(2000);
							setNextTask(build);
						}
					}else
						stop();
				}
			}else
				setNextTask(build);
			return Calculations.random(500, 1500);
		}
	});

	private Task build = new Task("Build", new TaskBody() {
		@Override
		public int execute(){


			if(!gotButler){
				NPC b = getNpcs().closest("Butler");
				if(b != null){
					b.interact();
					conditionalSleep(() -> getDialogues().inDialogue(), 5000, 6000);
					if(getDialogues().inDialogue())
						gotButler = true;	
						
				}else{
					getWalking().clickTileOnMinimap(getLocalPlayer().getTile().translate(Calculations.random(0, 10) - 5, Calculations.random(0, 10) - 5));
					walkingSleep();
				}
				return Calculations.random(500, 1500);
			}
			int plankCount = getInventory().count(8778);
			if(plankCount < 20){
				if(plankCount < 8){
					conditionalSleep(()->getNpcs().closest("Butler") != null, 2000, 2500);
				}
				NPC b = getNpcs().closest("Butler");
				if(b != null){
//					int pCount = getInventory().count(8778);
					b.interact();
					conditionalSleep(() -> getDialogues().inDialogue(), 5000, 6000);
					String dial = getDialogues().getNPCDialogue();
					log(dial);
					if(dial.equals("Your goods, sir.")){
						conditionalSleep(()->getDialogues().canContinue(), 1000, 1500);
						getDialogues().spaceToContinue();
						b.interact();
						conditionalSleep(() -> getDialogues().inDialogue(), 5000, 6000);
						getDialogues().chooseOption(1);
						conditionalSleep(()->getDialogues().canContinue(), 1000, 1500);
						getDialogues().spaceToContinue();
						sleep(Calculations.random(500, 800));
						getDialogues().spaceToContinue();
					}
					else if(dial.contains("pay me")){
						conditionalSleep(()->getDialogues().canContinue(), 1000, 1500);
						getDialogues().spaceToContinue();
						conditionalSleep(() -> getDialogues().getOptions() != null, 500, 1000);
						getDialogues().typeOption(1);
						conditionalSleep(()->getDialogues().canContinue(), 1000, 1500);
						getDialogues().spaceToContinue();
						b.interact();
						conditionalSleep(() -> getDialogues().inDialogue(), 5000, 6000);
						getDialogues().chooseOption(1);
						conditionalSleep(()->getDialogues().canContinue(), 1000, 1500);
						getDialogues().spaceToContinue();
						sleep(Calculations.random(500, 800));
						
					}else if (getDialogues().getOptions() != null){
						getDialogues().chooseOption(1);
						conditionalSleep(()->getDialogues().canContinue(), 1000, 1500);
						getDialogues().spaceToContinue();
						sleep(Calculations.random(500, 800));
					}
//					if(getInventory().count(8778) != pCount){
//						conditionalSleep(()->getDialogues().canContinue(), 1000, 1500);
//						getDialogues().spaceToContinue();
//						b.interact();
//						conditionalSleep(() -> getDialogues().inDialogue(), 5000, 6000);
//						getDialogues().chooseOption(1);
//						conditionalSleep(()->getDialogues().canContinue(), 1000, 1500);
//						getDialogues().spaceToContinue();
//					}else{
//						conditionalSleep(()->getDialogues().canContinue(), 1000, 1500);
//						getDialogues().spaceToContinue();
//						sleep(500);
//						if(getDialogues().getOptionIndexContaining("5000") != -1){
//							getDialogues().chooseOption(getDialogues().getOptionIndexContaining("5000"));
//						}else{
//							getDialogues().chooseOption(1);
//							conditionalSleep(()->getDialogues().canContinue(), 1000, 1500);
//							getDialogues().spaceToContinue();
//						}
//					}
				}
			}
			GameObject g = getGameObjects().closest(15403);
			GameObject g2 = getGameObjects().closest(13566);
			Widget w = getWidgets().getWidget(458);
			if(w != null){
				getWidgets().getWidget(458).getChild(5).getChild(6).interact();
				building = true;
			}else if(getDialogues().getOptions() != null){
				getDialogues().typeOption(1);
				conditionalSleep(() -> getDialogues().getOptions() == null, 500, 1000);
				building = false;
			}else if(g != null && !building){
				g.interact("Build");
				conditionalSleep(() -> getWidgets().getWidget(458) != null, 500, 1000);
			}else if(g2 != null){
				g2.interact("Remove");
				conditionalSleep(() -> getDialogues().getOptions() != null, 500, 1000);
			}
			return Calculations.random(100, 800);
		}
	});

	private final Tile CW_BANK = new Tile(2439, 3092, 0);
	private final Tile CW_CHEST_TILE = new Tile(2443, 3083, 0);

//	private Task bank = new Task("Banking", new TaskBody() {
//		@Override
//		public int execute(){
//			if(getLocalPlayer().distance(CW_BANK) < 10){
//				getWalking().walk(CW_CHEST_TILE);
//				walkingSleep();
//				GameObject g = getGameObjects().closest("Bank Chest");
//				if(g != null)
//					if(g.interact("Use")){
//						conditionalSleep(() -> getBank().isOpen(), 1000, 2500);
//
//						if(!checkTeleports(RING_ID_0, null) && getBank().contains(RING_ID_0)){
//							getBank().withdraw(RING_ID_0);
//							conditionalSleep(() -> getInventory().contains(RING_ID_0), 800, 1200);
//						}
//						getBank().withdraw(8013);
//						conditionalSleep(() -> (getInventory().get(8013) != null), 800, 1200);
//						getBank().withdraw(8778, 24);
//						conditionalSleep(() -> (getInventory().get(960) != null), 800, 1200);
//
//						getBank().close();
//						if(!checkTeleports(RING_ID_0, null)){
//							log("No teleports in bank");
//							stop();
//						}
//						if(getInventory().count(8778) >= 22 && getInventory().count(8013) >= 1 && getInventory().count(4820) > 10 && checkTeleports(RING_ID_0, null)){
//							getInventory().get(8013).interact("Break");
//							conditionalSleep(() -> TILE.distance(getLocalPlayer()) < 100, 3000, 4000);
//							turnOnBuilding();
//							setNextTask(build);
//							getWalking().clickTileOnMinimap(TILE);
//							walkingSleep();
//							conditionalSleep(() -> TILE.distance(getLocalPlayer()) < 1, 2000, 5000);
//						}
//					}
//				return Calculations.random(200, 300);
//			}else if(checkTeleports(RING_ID_0, i -> i.interact("Castle Wars"))){
//				conditionalSleep(() -> getLocalPlayer().distance(CW_BANK) < 4, 4000, 4500);
//				getWalking().walk(CW_CHEST_TILE);
//				walkingSleep();
//				return Calculations.random(500, 800);
//			}
//
//			log("No Ring");
//			stop();
//			return 1;
//		}
//	});
	
	private void turnOnBuilding(){
		getWidgets().getWidget(548).getChild(41).interact();
		sleep(Calculations.random(400, 985));
		getWidgets().getWidget(261).getChild(75).interact();
		sleep(Calculations.random(800, 985));
		getWidgets().getWidget(370).getChild(5).interact();
		sleep(Calculations.random(1002, 2000));
		getWidgets().getWidget(548).getChild(57);
		sleep(Calculations.random(400, 985));
	}

	private boolean checkTeleports(int itemID, Consumer<Item> c){
		for(int i = 0; i < 16; i += 2){
			if(getInventory().contains(itemID + i) || getEquipment().contains(itemID + i)){
				if(getInventory().contains(itemID + i)){
					getInventory().get(itemID + i).interact("Wear");
					final int id = itemID + i;
					conditionalSleep(() -> getEquipment().contains(id), 500, 1000);
				}
				if(c != null){
					getWidgets().getWidget(548).getChild(58).interact();
					sleep(300);
					c.accept(getEquipment().get(itemID + i));
					getWidgets().getWidget(548).getChild(57).interact();
					sleep(300);
				}
				return true;
			}
		}
		log("Has no " + itemID);
		return false;
	}

	@Override
	public void onStart(){
		super.onStart();
		setNextTask(init);
	}
	
	@Override
	public void onPaint(Graphics g){
		super.onPaint(g);
		long time = getSkills().getExperienceToLevel(Skill.CONSTRUCTION);
		g.drawString("exp to lvl: " + time, 10, 80);
	}
}
