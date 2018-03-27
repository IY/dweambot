import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.walking.path.impl.LocalPath;
import org.dreambot.api.methods.walking.pathfinding.impl.obstacle.impl.DestructableObstacle;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;


@ScriptManifest(author = "skengrat", name = "LavaDragon killer", version = 1.1, description = "kills lava dragons", category = Category.COMBAT)
public class main extends AbstractScript{
	
	private final String[] loot = new String[]{"Draconic visage", "Onyx bolt tips", "Shield left half", "Dragon med helm", "Dragon spear", "Rune 2h sword", "Rune kiteshield", "Rune battleaxe", "Runite bar", "Nature rune", "Rune sq shield", "Tooth half of key", "Loop half of key", "Fire orb", "Rune full helm", "Rune longsword", "Adamant platebody", "Rune med helm", "Rune dart", "Dragon javelin heads", "Lava dragon bones", "Black dragonhide", "Lava scale", "Rune javelin", "Runite bolts", "Blood rune", "Death rune", "Rune knife", "Law rune", "Rune axe", "Grimy ranarr weed", "Rune battleaxe", "Uncut diamond", "Silver ore", "Rune arrow", "Dragonstone", "Law rune", "Death rune", "Steel arrow", "Rune spear"};
	
	public Area Bank = new Area(2531, 4711, 2564, 4722);
	
	public Area SafeSpot = new Area(3216, 3825, 3215, 3826);
	public Area DragonZone = new Area(3210, 3817, 3225, 3834);
	
	public Area died = new Area(3225, 3227, 3219, 3204);
	
	public Boolean dead = false;
	
    private Timer t;
	
	
	public boolean looting = false;
	public boolean shouldHop = false; 
    public boolean full = false;
    
    public int hopped = 0;
    
    public ArrayList<String> wlist = new ArrayList<String>();
    
    Area teleArea = new Area(new Tile(3153, 3925), new Tile(3156, 3925), new Tile(3156, 3922), new Tile(3154, 3922), new Tile(3153, 3923));
    Area doorArea = new Area(new Tile(3222,3903), new Tile(3227,3903));
    Area mageBankTele = new Area(new Tile(3090, 3955), new Tile(3090, 3958), new Tile(3092, 3956));
    
    Tile dragIsleTile = new Tile(3200,3816);
    Tile wildGate = new Tile(3224,3906);
    Tile corner = new Tile(3135, 3913);
    
    LocalPath<Tile> pathToBank = new LocalPath<Tile>(this);
    Tile[] tiles = new Tile[] {dragIsleTile, wildGate, corner, mageBankTele.getRandomTile()};
    
    LocalPath<Tile> pathToDrags = new LocalPath<Tile>(this);
    Tile[] dTiles = new Tile[]{mageBankTele.getCenter(), corner, wildGate, dragIsleTile};
    
    
    
    public String[] items = {"Air rune", "Mind rune", "Amulet of glory(4)", "Mystic robe top", "Mystic robe bottom", "Stamina potion(4)", 
        "Staff of fire", "Anti-dragon shield", "Lobster", "Burning amulet(4)"
    };
    
    public int[] amount = {1500, 1000, 1, 1, 1, 1, 1, 1, 8, 1}; // null = idk
    
    public int rState = 0;
    public boolean  pulled= false;
    
	public String BurnAmulet;
	public String Glory;
	public String Stamina;
	public String rune;
	
	
	// BANKERS ONDEATH BANK METHOD
	
	 public void onStart() {
		new Frame(this);
		t = new Timer();
        pathToBank.addAll(Arrays.asList(tiles));
        pathToDrags.addAll(Arrays.asList(dTiles));
        getWalking().getAStarPathFinder().addObstacle(new DestructableObstacle("Web", "Slash", null, null, null));
    }
    
    public void onDeath() {
        log("Bot Has Died. Returning to Mage Bank.");
        
        if(!getBank().getClosestBankLocation().EDGEVILLE.getArea(3).contains(getLocalPlayer()) && rState == 0) {
            getWalking().walk(getBank().getClosestBankLocation().EDGEVILLE.getCenter());
            sleepUntil(() -> !getLocalPlayer().isMoving() || getLocalPlayer().distance(getClient().getDestination()) < 3, Calculations.random(1500,2500));
            if(getBank().getClosestBankLocation().EDGEVILLE.getArea(3).contains(getLocalPlayer())) {
                rState = 1;
                log("Stage 1: Bot has reached Edgeville Bank.");
            }
        }
        
        if(!getInventory().contains("Knife") && rState == 1) {
            if(!getBank().isOpen()) {
                getBank().openClosest();
                sleepUntil(() -> getBank().isOpen(), 5000);
            } else {
                if(!getInventory().isEmpty()) {
                    getBank().depositAllItems();
                    sleepUntil(() -> getInventory().isEmpty(), 3000);
                } else {
                    getBank().withdraw("Knife", 1);
                    sleepUntil(() -> getInventory().contains("Knife"), 2500);
                    getBank().close();
                }
                if(getInventory().contains("Knife")) {
                    rState = 2;
                    log("Stage 2: Knife Withdrawn - Going to Mage-Bank.");
                }
            }
            
        if(getInventory().contains("Knife") && rState == 2) {
            
            GameObject lever = getGameObjects().closest("Lever");
            
            if(!pulled) {
                lever.interact("Pull");
                sleepUntil(() -> getDialogues().canContinue(), 3000);
                
                if(getDialogues().canContinue()) {
                    getDialogues().continueDialogue();
                    sleepUntil(() -> getDialogues().getOptions() != null, 2500);
                }
                
                if(getDialogues().getOptions() != null) {
                    getDialogues().chooseOption(1);
                    sleepUntil(() -> !getDialogues().inDialogue(), 3250);
                    pulled = true;
                }
            }
            
            if(pulled && !teleArea.contains(getLocalPlayer())) {
                sleepUntil(() -> teleArea.contains(getLocalPlayer()), 10000);
            }else if (pulled && teleArea.contains(getLocalPlayer())) {
                log("Stage 3: Arrived In Wilderness.");
                rState = 3;
                pulled = false;
            }
            
        }
        
        if(getInventory().contains("Knife") && rState == 3) {
            
            if(!mageBankTele.contains(getLocalPlayer()) && !pulled) {
                getWalking().walk(mageBankTele.getRandomTile());
                sleepUntil(() -> !getLocalPlayer().isMoving() || getLocalPlayer().distance(getClient().getDestination())<3, Calculations.random(1000,2400));
            } else {
                GameObject lever = getGameObjects().closest("Lever");
                
                if(!pulled) {
                    lever.interact("Pull");
                    sleepUntil(() -> mageBankTele.contains(getLocalPlayer()), 10000);
                    if(!mageBankTele.contains(getLocalPlayer()))
                    {
                        pulled = false;
                        rState = 4;
                        log("Stage 4: Arrived In Mage-Bank.");
                        log("Defaulting to Bank Method.");
                    }
            	}
        	} 
        }
    }
}
    
    public void walkToBank() {
        
        if (pathToBank.isEmpty()) {
            pathToBank.addAll(Arrays.asList(tiles));
        }
        if(!mageBankTele.contains(getLocalPlayer())) {
            log("following the path");

            pathToBank.walk();
            sleep(1000, 1400);
        }
        
        if(mageBankTele.contains(getLocalPlayer())) {
                GameObject lever = getGameObjects().closest("Lever");
                
                if(!pulled) {
                    lever.interact("Pull");
                    sleepUntil(() -> mageBankTele.contains(getLocalPlayer()), 10000);
                    if(Bank.contains(getLocalPlayer())) {
                        pulled = false;
                    }
                }
            }
        
    }    

    public void walkToDragons() {
        
        if (pathToDrags.isEmpty()) {
            pathToDrags.addAll(Arrays.asList(tiles));
        }
        if(!getLocalPlayer().equals(dragIsleTile)) {
            log("Following path to dragons.");
        
            pathToDrags.walk();
            sleep(1000, 1400);
        }
    }
    
    public void banking() {
        
        if(!Bank.contains(getLocalPlayer())) {
            walkToBank();
        } else {
            NPC gundai = getNpcs().closest("Gundai");
            if(!getBank().isOpen()) {
                gundai.interact("Bank");
                sleepUntil(() -> getBank().isOpen(), 3500);
            } else {
                if(!getInventory().isEmpty()) {
                    getBank().depositAllItems();
                    getBank().depositAllEquipment();
                    sleep(Calculations.random(890, 1350));
                } else {
                    
                    for(int x = 0; x < items.length; x++) {
                        if(!getInventory().contains(items[x])) {
                            getBank().withdraw(items[x], amount[x]);
                            sleep(500, 1000);
                        }
                    }
                    
                    getBank().close();
                    GameObject Lever = getGameObjects().closest("Lever");
                    Lever.interact("Pull");
                    sleep(3000, 4000);
                } 
            }
    }
        
        if(!getBank().getClosestBankLocation().EDGEVILLE.getArea(3).contains(getLocalPlayer())) {
            getWalking().walk(getBank().getClosestBankLocation().EDGEVILLE.getCenter());
            sleepUntil((() -> !getLocalPlayer().isMoving() || getLocalPlayer().distance(getClient().getDestination()) < 30), Calculations.random(2000,3000));
        } else {
            if(!getBank().isOpen()) {
                getBank().openClosest();
                sleepUntil(() -> getBank().isOpen(), 2100);
            } else {
                getBank().depositAllItems();
                getBank().withdraw("Knife", 1);
                sleepUntil(() -> getInventory().contains("Knife"), 2000);
            }
        }
    }
    
	
	//BEGIN GET METHODS
	
	public void getUseable()
	{
		if(getBank().contains("Burning amulet(1)")) {BurnAmulet = "Burning amulet(1)";}
		else if(getBank().contains("Burning amulet(2)")) {BurnAmulet = "Burning amulet(2)";}
		else if(getBank().contains("Burning amulet(3)")) {BurnAmulet = "Burning amulet(3)";}
		else if(getBank().contains("Burning amulet(4)")) {BurnAmulet = "Burning amulet(4)";}
		else if(getBank().contains("Burning amulet(5)")) {BurnAmulet = "Burning amulet(5)";}
		else{log("Bot has ran out of Burning amulets");}
		
		if(getBank().contains("Amulet of glory(1)")) {Glory = "Amulet of glory(1)";}
		else if(getBank().contains("Amulet of glory(2)")) {Glory = "Amulet of glory(2)";}
		else if(getBank().contains("Amulet of glory(3)")) {Glory = "Amulet of glory(3)";}
		else if(getBank().contains("Amulet of glory(4)")) {Glory = "Amulet of glory(4)";}
		else if(getBank().contains("Amulet of glory(5)")) {Glory = "Amulet of glory(5)";}
		else if(getBank().contains("Amulet of glory(6)")) {Glory = "Amulet of glory(6)";}
		else{log("Bot has ran out of Amulet of glories");}
		
		if(getBank().contains("Stamina potion(1)")) {Stamina = "Stamina potion(1)";}
		else if(getBank().contains("Stamina potion(2)")) {Stamina = "Stamina potion(2)";}
		else if(getBank().contains("Stamina potion(3)")) {Stamina = "Stamina potion(3)";}
		else if(getBank().contains("Stamina potion(4)")) {Stamina = "Stamina potion(4)";}
		else{log("Bot has ran out of Stamina potions");}
			
	}
	
	public void getDragon()
	{
	    if(!looting)
	    { 
	    	NPC Dragon = getNpcs().closest("Lava dragon");
	    	if(DragonZone.contains(Dragon))
	    	{
	    		if(getLocalPlayer().isInCombat())
	    		{
	    			sleepUntil(() -> !getLocalPlayer().isInCombat(), 300);
	    		}
	    		
			else
			{
			sleepWhile(() -> Dragon.interact("Attack"), 300);
			}
		  }
	    }
 	  }
	
	public void getLoot()
	{
		for(int y = 0; y < 2; y++)
        {
            hopifnotalone();
            for(GroundItem i : getGroundItems().all()) {
                for(int x = 0; x < loot.length; x++) {
                    if(i.getName().equalsIgnoreCase(loot[x]) && i.distance(SafeSpot.getCenter()) < 10) {
                        looting = true;
                        i.interact("Take");
                        sleep(300);
                    }
                }
            }
            looting = false;
        }
	}
	
	public Boolean getCast()
	{
		if(this.getSkills().getRealLevel(Skill.MAGIC)> 50)
		{
		    rune = "Chaos rune";
			return  true;
		}
		else
		{
		    rune = "Mind rune";
			return false;
		}
	}
	
	public Boolean getmystic()
	{
		if(this.getSkills().getRealLevel(Skill.MAGIC) > 40 && this.getSkills().getRealLevel(Skill.DEFENCE) > 20)
		{
			log("Bot is able to use mystic");
			return true;
		}
		else
		{
			log("Bot is not able to use mystic");
			return false;
		}
		
	}
	
	//END GET METHODS
	
	
	//BEGIN HOPPING STUFF
	
    private void hopifnotalone() 
    {
    	
    	for(Player x : getPlayers().all()) {
    		if(!wlist.contains(x.getName())) {
    			shouldHop = true;
    		}
    	}
    	
    		if(getLocalPlayer().isHealthBarVisible())
    		{
    			//this way it won't log out when its bad to try logout
    		}
    		else
    		{
                Player otherPlayer = (Player)this.getPlayers().closest(p -> !p.equals((Object)this.getLocalPlayer()) && p != null && Math.abs(p.getLevel() - this.getLocalPlayer().getLevel()) <= (this.getCombat().getWildernessLevel() + 1));
                if (otherPlayer != null && otherPlayer.distance(getLocalPlayer()) < 25) {
                    log((String)"hopping");
                    this.hopworlds();
                    hopped++;
                    shouldHop = false; // this should only hop if a player thats not in the whitelist appears
    		}
    	}
    }
    
    private void hopworlds() 
    {
        if (this.getWorldHopper().quickHop(this.p2pworld())) {
            sleep((int)Calculations.random((int)100, (int)300));
            sleepUntil(() -> this.getLocalPlayer().exists() && this.getClient().isLoggedIn(), (long)Calculations.random((int)300, (int)500));
        }
    }
    
    private int p2pworld() 
    {
        return this.getWorlds().getRandomWorld(w -> w.isMembers() && !w.isDeadmanMode() && !w.isPVP() && w.getMinimumLevel() < 500 && !w.isHighRisk() && w.getID() != this.getClient().getCurrentWorld()).getID();
    }
    
    //END HOPPING STUFF
    
    
    //BEGIN WALKING
    public void WalkToDrag()
	{
		//new walking method still have to find the coordinates of the Area's
	}
	
	public void WalkToBank()
    {

    }
    
    //END WALKING
	
	//BEGIN STATES
	
	public enum State
	{
		BANK, WALKTODRAG, WALKTOBANK, DIED, KILLING, LOOTING, WAITING
	}
	
	public State getState()
	{
		hopifnotalone();
		if(died.contains(getLocalPlayer()))
		{
			dead = true;
		}
		if(Bank.contains(getLocalPlayer()))
		{
			log("State bank");
			return State.BANK;
		}
		else if(dead == true)
		{
			return State.DIED;
		}
		else if(full == true)
		{
			return State.WALKTOBANK;
		}
		else if(SafeSpot.contains(getLocalPlayer()))
		{
			log("is in the safespot area and will start killing");
			return State.KILLING;
		}
		else
		{
			log("State WAITING");
			return State.WALKTODRAG;
		}
	}
	
	//END STATES
	
	
	@Override
	public int onLoop() 
	{
		//checks incase it leveld up
		if(getDialogues().inDialogue()) {
			getDialogues().continueDialogue();
		}
		
		//checks if inventory == full or doesn't have runes left
		if(getInventory().isFull() || !getInventory().contains(rune)) {
			full = true;
		} else if(Bank.contains(getLocalPlayer())) {
			full = false;
		}
		
		hopifnotalone();
		if (!this.getWalking().isRunEnabled() && this.getWalking().getRunEnergy() > 5) {
            getWalking().toggleRun();
        }
        if (this.getCombat().getHealthPercent() < 75 && this.getClient().isLoggedIn() && this.getInventory().contains("Lobster")) {
            this.getInventory().interact("Lobster", "Eat");
        }
        antiban();
		switch(getState())
		{
		case BANK:
			dead = false;
			banking();
			break;
		case WALKTODRAG:
			WalkToDrag();
			break;
		case KILLING:
			if(getDialogues().inDialogue())
			{
				getDialogues().chooseOption(1);
			}
			getLoot();
			getDragon();
			break;
		case LOOTING:
			break;
		case DIED:
			onDeath();
			break;
		case WALKTOBANK:
			WalkToBank();
			break;
		default:
			break;
		}
		return 100;
	}
	
	private void antiban()
	{
        int random = Calculations.random((int)1, (int)5000);
        if (random <= 2) {
            if (!this.getTabs().isOpen(Tab.STATS)) {
                this.getTabs().open(Tab.STATS);
                this.getSkills().hoverSkill(Skill.MAGIC);
                sleep((int)Calculations.random((int)1000, (int)2000));
                this.getTabs().open(Tab.INVENTORY);
            }
        } else if (random <= 10) {
            if (!this.getTabs().isOpen(Tab.INVENTORY)) {
                this.getTabs().open(Tab.INVENTORY);
            }
        } else if (random <= 15) {
            this.getCamera().rotateToTile(this.SafeSpot.getRandomTile());
        } else if (random <= 20) {
            this.getCamera().rotateToEntity((Entity)this.getLocalPlayer());
        } else if (random <= 88 && this.getMouse().isMouseInScreen() && this.getMouse().moveMouseOutsideScreen()) {
            sleep((int)Calculations.random((int)1500, (int)3000));
        }
    }
	
    public void onPaint(Graphics g) 
    {
        g.setColor(new Color(0, 200, 0, 150));
        g.drawString("Ratz Lava ting: ", 15, 60);
        g.drawString("Runtime: " + this.t.formatTime(), 15, 80);
        g.drawString("hopped:" + hopped + "  Hopped/hr:" + String.valueOf(t.getHourlyRate(hopped)), 15, 100);
    }

}
