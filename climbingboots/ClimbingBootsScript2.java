package examples;

import java.util.function.Consumer;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;

import base.AbstractFeaturedScript;
import base.Task;
import base.Task.TaskBody;
import climbingboots.ClimbingBootsGui;

@ScriptManifest(author = "skengrat", name = "Climbing Boots 2", version = 1.0, description = "Buys climbing boots from tenzing and banks them at castle wars bank, or trade with given acc", category = Category.MONEYMAKING)
public class ClimbingBootsScript2 extends AbstractFeaturedScript{

	private final int NECKLACE_ID_0 = 3853;
	private final int RING_ID_0 = 2552;
	private final Tile TENZING_SHED = new Tile(2825, 3555, 0);
	private final Tile TENZING_SHED_HW = new Tile(2855, 3571, 0);
	private final Tile BURTHORPE = new Tile(2900, 3555, 0);
	private final Tile TENZING = new Tile(2820, 3555, 0);
	private final Tile CW_BANK = new Tile(2439, 3092, 0);
	private final Tile CW_CHEST_TILE = new Tile(2443, 3083, 0);

	private ClimbingBootsGui gui;

	private Task init = new Task("Initialize", new TaskBody() {
		@Override
		public int execute(){
			addToSmallTasks(100, ()->(getWalking().isRunEnabled() ? false : getWalking().toggleRun()) ? 200 : 200);
			if(!getWalking().isRunEnabled())
				getWalking().toggleRun();
			conditionalSleep(() -> getWalking().isRunEnabled(), 1000, 1200);
			getWidgets().getWidget(548).getChild(9).interact("Look North");
			sleep(200);
			if(getClientSettings().roofsEnabled())
				roofsOff();

			if(!checkTeleports(NECKLACE_ID_0, null) || !checkTeleports(RING_ID_0, null) || getInventory().count("Coins") < getInventory().emptySlotCount() * 12 || getInventory().isFull())
				setNextTask(bank);
			else if(getLocalPlayer().distance(TENZING) < 2)
				setNextTask(tenzingTalk);
			else setNextTask(shedWalk);

			return Calculations.random(200, 500);
		}
	});

	private Task shedWalk = new Task("Shed Walk", new TaskBody() {
		@Override
		public int execute(){
			if(getLocalPlayer().distance(BURTHORPE) > 100){
				if(checkTeleports(NECKLACE_ID_0, i -> i.interact("Burthorpe"))){
					conditionalSleep(() -> getLocalPlayer().distance(BURTHORPE) < 15, 4000, 4500);
					getWidgets().getWidget(548).getChild(9).interact("Look North");
					conditionalSleep(() -> getCamera().getYaw() == 0, 200, 500);
				}else{
					log("No Necklace");
					stop();
					return 1;
				}
			}

			if(!getWalking().isRunEnabled() && getWalking().getRunEnergy() > 15)
				getWalking().toggleRun();
			getWalking().walk(getLocalPlayer().distance(BURTHORPE) < getLocalPlayer().distance(TENZING_SHED) ? TENZING_SHED_HW : TENZING_SHED);
			conditionalSleep(() -> getLocalPlayer().distance(TENZING_SHED_HW) < 2, 2000, 2500);
			if(getLocalPlayer().distance(TENZING_SHED) < 2){
				openObstacle("Gate");
				openObstacle("Door");
				getWalking().walk(TENZING);
				walkingSleep();
			}
			if(getLocalPlayer().distance(TENZING) < 2){
				setNextTask(tenzingTalk);
			}
			return 1;
		}
	});

	private Task tenzingTalk = new Task("Tenzing Talk", new TaskBody() {
		@Override
		public int execute(){
			if(getLocalPlayer().distance(TENZING) > 2)
				setNextTask(shedWalk);
			if((getInventory().isFull() && getInventory().count("Coins") != 12)){
				if(doTrade())
					setNextTask(trade);
				else setNextTask(bank);
			}else if(getInventory().count("Coins") < getInventory().emptySlotCount() * 12)
				setNextTask(bank);
			
			
			if(getDialogues().inDialogue()){
				if(getDialogues().getOptions() != null)
					getDialogues().typeOption(1);
				else getDialogues().spaceToContinue();
				return Calculations.random(200, 300);
			}
			NPC tenzing = getNpcs().closest("Tenzing");
			if(tenzing != null){
				if(tenzing.isOnScreen() && tenzing.interact()){
					walkingSleep();
					conditionalSleep(() -> getDialogues().canContinue(), 1200, 1600);
				}else{
					getWalking().walk(tenzing);
					walkingSleep();
				}
			}

			return 1;
		}
	});

	private Task bank = new Task("Banking", new TaskBody() {
		@Override
		public int execute(){
			if(getLocalPlayer().distance(CW_BANK) < 10){
				getWalking().walk(CW_CHEST_TILE);
				walkingSleep();
				GameObject g = getGameObjects().closest("Bank Chest");
				if(g != null)
					if(g.interact("Use")){
						conditionalSleep(() -> getBank().isOpen(), 1000, 2500);
						getBank().depositAllItems();
						conditionalSleep(() -> getInventory().isEmpty(), 800, 1200);
						getBank().withdraw("Coins", 336);
						conditionalSleep(() -> !getInventory().isEmpty(), 800, 1200);
						if(!checkTeleports(NECKLACE_ID_0, null) && getBank().contains(NECKLACE_ID_0)){
							getBank().withdraw(NECKLACE_ID_0);
							conditionalSleep(() -> getInventory().contains(NECKLACE_ID_0), 800, 1200);
						}
						if(!checkTeleports(RING_ID_0, null) && getBank().contains(RING_ID_0)){
							getBank().withdraw(RING_ID_0);
							conditionalSleep(() -> getInventory().contains(RING_ID_0), 800, 1200);
						}
						getBank().close();
						if(!checkTeleports(NECKLACE_ID_0, null) || !checkTeleports(RING_ID_0, null)){
							log("No teleports in bank");
							stop();
						}
						if(getInventory().count("Coins") == 336 && checkTeleports(NECKLACE_ID_0, null) && checkTeleports(RING_ID_0, null))
							setNextTask(shedWalk);
					}
				return Calculations.random(200, 300);
			}else if(checkTeleports(RING_ID_0, i -> i.interact("Castle Wars"))){
				conditionalSleep(() -> getLocalPlayer().distance(CW_BANK) < 4, 4000, 4500);
				getWalking().walk(CW_CHEST_TILE);
				walkingSleep();
				return Calculations.random(500, 800);
			}

			log("No Ring");
			stop();
			return 1;
		}
	});

	private Task trade = new Task("Trading", new TaskBody() {
		@Override
		public int execute(){
			if(!doTrade())
				setNextTask(bank);
			if(getLocalPlayer().distance(CW_BANK) > 10){
				if(checkTeleports(RING_ID_0, i -> i.interact("Castle Wars"))){
					conditionalSleep(() -> getLocalPlayer().distance(CW_BANK) < 4, 4000, 4500);
					Player p = getPlayers().closest(gui.getTradeAccounts().get(0));
					if(p != null){
						getWalking().walk(p);
						walkingSleep();	
						return Calculations.random(500, 800);
					}else{
						conditionalSleep(()->getPlayers().closest(gui.getTradeAccounts().get(0)) != null, 10000, 12000);
						if(getPlayers().closest(gui.getTradeAccounts().get(0)) == null){
							log("no trade account");
							setNextTask(bank);
							gui.getTradeAccounts().clear();
						}
					}
				}else{
					log("No ring");
					stop();
				}
			}else{
				if(getTrade().isOpen()){
					if(getInventory().contains("Climbing boots")){
						getTrade().addItem("Climbing boots", 28);
						conditionalSleep(() -> !getInventory().contains("Climbing boots"), 1000, 2000);
					}
					getTrade().acceptTrade();
				}else{
					if(getInventory().contains("Climbing boots")){
						getTrade().tradeWithPlayer(gui.getTradeAccounts().get(0));
						log("Trading with " + gui.getTradeAccounts().get(0));
						conditionalSleep(() -> getTrade().isOpen(), 5000, 6000);
					}else setNextTask(bank);
				}
			}

			return Calculations.random(200, 400);
		}
	});

	public void onStart(){
		super.onStart();
		setNextTask(init);
		gui = new ClimbingBootsGui("Enter trade account:", getFriends().getFriends(), false);
	}

	public void onExit(){
		gui.dispose();
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
	
	private boolean doTrade(){
		return !gui.getTradeAccounts().isEmpty();
	}
}
