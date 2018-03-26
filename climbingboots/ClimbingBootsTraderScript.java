package climbingboots;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.message.Message;

import base.AbstractFeaturedScript;
import base.Task;
import base.Task.TaskBody;

@ScriptManifest(author = "Rob", name = "Climbing Boots Trader", version = 1.2, description = "Use in combination with ClimbingBootsScript, receives trades from other acc", category = Category.MONEYMAKING)
public class ClimbingBootsTraderScript extends AbstractFeaturedScript{

	private final int NOTED_BOOTS_ID = 3106;
	private final int RING_ID_0 = 2552;
	private final Tile CW_BANK = new Tile(2439, 3092, 0);
	private final Tile CW_CHEST_TILE = new Tile(2443, 3083, 0);

	private ClimbingBootsGui gui;

	private Queue<String> traderQueue;
	private boolean trading;
	private boolean logout;

	private Task init = new Task("Initialize", new TaskBody() {
		@Override
		public int execute(){
			getWidgets().getWidget(548).getChild(9).interact("Look North");
			sleep(200);

			if(getLocalPlayer().distance(CW_BANK) > 10){
				if(checkTeleports(RING_ID_0, i -> i.interact("Castle Wars")))
					conditionalSleep(() -> getLocalPlayer().distance(CW_BANK) < 4, 4000, 4500);
				else{
					log("Not at castle wars and no teleport ring");
					stop();
				}
			}else setNextTask(trade);

			return Calculations.random(200, 500);
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
						getBank().close();
						conditionalSleep(() -> !getBank().isOpen(), 1000, 2500);
						getTabs().logout();
						getRandomManager().disableSolver(RandomEvent.LOGIN);
						setNextTask(trade);
						logout = false;
					}
				return Calculations.random(200, 300);
			}
			log("Not at castle wars, mid script");
			stop();

			return 1;
		}
	});

	private Task trade = new Task("Trading", new TaskBody() {

		private boolean contactedTrader = false;

		@Override
		public int execute(){
			if(trading){
				String traderName = traderQueue.peek().trim();
				if(getPlayers().closest(traderName) == null){
					log("Boike is erni");
					traderQueue.remove();
					contactedTrader = false;
				}else if(getTrade().isOpen()){
					log("Trading with " + traderName);
					contactedTrader = true;
					if(getTrade().getTheirItems() == null ||
						getTrade().getTheirItems()[0].getID() == NOTED_BOOTS_ID)
						getTrade().acceptTrade();
					else
						getTrade().declineTrade();
					
					conditionalSleep(() -> getTrade().getTheirItems() != null || !getTrade().isOpen(), 2000, 3000);
				}else if(contactedTrader){
					traderQueue.remove();
					contactedTrader = false;
				}else{
					getTrade().tradeWithPlayer(traderName);
					conditionalSleep(() -> getTrade().isOpen(), 1000, 2000);
				}
				if(traderQueue.isEmpty())
					trading = false;
			}else{
				
			}
			if(logout && traderQueue.isEmpty())
				setNextTask(bank);
			
			return Calculations.random(200, 400);
		}
	});

	@Override
	public int onLoop(){
		if(getClient().isLoggedIn())
			return super.onLoop();
		return 1000;
	}

	public void onStart(){
		super.onStart();
		setNextTask(init);
		trading = false;
		traderQueue = new LinkedList<String>();
		getRandomManager().registerSolver(new TradeSolver(RandomEvent.BREAK, this));
		gui = new ClimbingBootsGui("Enter mule accounts: ", getFriends().getFriends(), true);
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

	@Override
	public void onTradeMessage(Message m){
		super.onTradeMessage(m);
		if(gui.getTradeAccounts().contains(m.getUsername()) && !logout){
			if(!traderQueue.contains(m.getUsername()))
				traderQueue.add(m.getUsername());
			trading = true;
		}
	}
	
	public void startLogout(){
		logout = true;
	}
	
	public float getOnlineTime() {
		return gui.getOnlineTime();
	}
	
	public float getOfflineTime() {
		return gui.getOfflineTime();
	}
}
