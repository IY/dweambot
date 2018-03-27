package MonkSkenger;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
@ScriptManifest(category = Category.COMBAT, name = "MonkSkenger", author = "skengrat", version = 1.5)
public class MonkSkenger  extends AbstractScript {
  
    public boolean isruning;
    public boolean inCombatBool = false;
    private int beginningXP;
    private int currentXp;
    private int xpGained;
    private static final String EVENT_RPG = "Event rpg";
    private Timer timer = new Timer();
    private Image mainPaint = getImage("https://i.imgur.com/JY38qJe.png");
    // ONSTART() METHOD
    public void onStart() {

    	getRandomManager().disableSolver(RandomEvent.DISMISS);
    	beginningXP = getSkills().getExperience(Skill.DEFENCE);
    	hasDefaultZoom();
    	getClient().getInstance().setDrawMouse(false);
    	isruning = true;
    	
    	Thread t = new Thread(() -> {
            int x = 0;
            while (isruning) {
                if ((getLocalPlayer().isInCombat() && getLocalPlayer().getInteractingCharacter() != null) || getLocalPlayer().getAnimation() != -1) {
                    inCombatBool = true;
                    x=0;
                } else if (x > 8) {
                    inCombatBool = false;
                    x = 0;
                }
                x++;
               // log(String.valueOf(inCombatBool));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start(); 
        
        //antiafk thread
    	Thread k = new Thread(() -> {
            int x = 0;
            while (isruning) {
                if ((getLocalPlayer().isInCombat() && getLocalPlayer().getInteractingCharacter() != null) || getLocalPlayer().getAnimation() != -1) {
                   //anti afk here
                	 log((String)"antiafk");
                	 getMouse().getMouseSettings().setWordsPerMinute(Calculations.random(22, 122));
                     getKeyboard().type("l", false);
                	 sleep(Calculations.random(22042, 522073));
                	
                	
                    x=0;
                } else if (x > 8) {
                    //nothn
                    x = 0;
                }
                x++;
               // log(String.valueOf(inCombatBool));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    	k.start(); 
    }
    
    // ONLOOP() METHOD
    @Override
    public int onLoop() {
   	 //if (getEquipment().isSlotEmpty(EquipmentSlot.WEAPON.getSlot())){
     //    if(getInventory().contains(EVENT_RPG)) {
      //       getInventory().interact(EVENT_RPG, "Wield");
     //    }else{

        if (!inCombatBool) {
        	alltaken();
          
            sleep(Calculations.random(3000, 8000));
        
//    	}
//        currentNpc = getNpcs().closest(npc -> npc != null && npc.getName() != null && npc.getName().equals(npcName) && !npc.isInCombat() && npc.getInteractingCharacter() == null);
//        if(currentNpc != null) { //does the npc exist?
//            if(!getLocalPlayer().isInCombat() && getLocalPlayer().getInteractingCharacter() == null) { //Make sure we aren't in combat or interacting with something
//                if(currentNpc.interact("Attack")) { //currentNpc will return true if we succesfully attacked the rat, if that happens we want to wait a bit to make sure we are in combat
//                    sleepUntil(() -> getLocalPlayer().isInCombat() || getLocalPlayer().getInteractingCharacter() != null, 2000); //Wait a max of 2 seconds or until we are in combat
//                }
//                return 100;
//            } else {
//                return 100;
//            }
//        }
//        return 100;
        }//}
      //  }
		return 0;
        
    }
    public void attack() {
    	//NPC slayerTask = getNpcs().closest(npc = npc.getName("Monk") && !npc.isInCombat());
    	
    	
    	  currentNpc = getNpcs().closest(npc -> npc != null && npc.getName() != null && npc.getName().equals(npcName) && !npc.isInCombat() && npc.getInteractingCharacter() == null);
        if(currentNpc != null) { //does the npc exist?

            if(!getLocalPlayer().isInCombat() && getLocalPlayer().getInteractingCharacter() == null) { //Make sure we aren't in combat or interacting with something
            	if (currentNpc.interact("Attack")) {

                changeStatus("Attacking");
                sleepUntil(() -> getLocalPlayer().isInCombat() || getLocalPlayer().getInteractingCharacter() != null, Calculations.random(5000, 7000));
            	}
    }
        }
    }
    
    public void alltaken() {
    	  currentNpc = getNpcs().closest(npc -> npc != null && npc.getName() != null && npc.getName().equals(npcName) && !npc.isInCombat() && npc.getInteractingCharacter() == null);
          if(currentNpc != null) { 
        	 //if no monk hop world
        	  log((String)"monk here");
        	  attack();
          }else {
        	  
        	  log((String)"all taken, hopping");
              this.hopworlds();
          }
    }
    
    private void hopworlds()
    {
        if (this.getWorldHopper().hopWorld(this.f2pworld())) {
            sleep((int)Calculations.random((int)100, (int)300));
            sleepUntil(() -> this.getLocalPlayer().exists() && this.getClient().isLoggedIn(), (long)Calculations.random((int)300, (int)500));
        }
    }
   
    private int f2pworld()
    {
        return this.getWorlds().getRandomWorld(w -> w.isF2P() && !w.isDeadmanMode() && !w.isPVP() && w.getMinimumLevel() < 500 && !w.isHighRisk() && w.getID() != this.getClient().getCurrentWorld()).getID();
    }
        public void onExit() {
            super.onExit();
            isruning = false;
        }
        

        private final Color[] COLORS = {Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA};

        @Override
        public void onPaint(Graphics g) {
        	((Graphics2D) g).setRenderingHint(
        		    RenderingHints.KEY_ANTIALIASING,
        		    RenderingHints.VALUE_ANTIALIAS_ON);
        	g.drawImage(mainPaint, 0, 339, null);
                currentXp = getSkills().getExperience(Skill.DEFENCE);
                xpGained = currentXp - beginningXP;
               // g.drawString("XP Gained: " + xpGained, 1, 1);
         
                g.setFont(new Font("Arial", Font.BOLD, 12));
                g.setColor(Color.BLACK);
                g.drawString("World: " + getClient().getCurrentWorld(), 20, 392);
                g.drawString("Runtime: " + timer.formatTime(), 20, 365);
                //g.drawString("State: " + status.name(), 25, 55);
                g.drawString("XP Gained: " + xpGained, 20, 410);
                
                g.drawString("Defence LVL: "+ getSkills().getRealLevel(Skill.DEFENCE), 20, 435);
                //g.drawString("XP/h: " + getSkillTracker().getGainedExperiencePerHour(Skill.DEFENCE)  , 25, 395);
                
                
                
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setStroke(new BasicStroke(3));

            final Point location = getMouse().getPosition();
            for (int i = COLORS.length - 1; i >= 0; i--) {
                graphics2D.setColor(COLORS[i]);
                graphics2D.drawLine(location.x - i, location.y + i, location.x + i, location.y - i);
                graphics2D.drawLine(location.x + i, location.y + i, location.x - i, location.y - i);
            }
        }
        private void changeStatus(String attacking) {

        }
        private Image getImage(String url){
    		try {
    			return ImageIO.read(new URL(url));
    		}catch (IOException e){
    			return null;
    		}
    	}
        public boolean hasDefaultZoom() {
        	  WidgetChild zoomBtn = getWidgets().getWidgetChild(261,13);
        	  return zoomBtn.getX() == 626 && zoomBtn.getY() == 265;
        	}
        private String ft(long duration)
        {
                String res = "";
                long days = TimeUnit.MILLISECONDS.toDays(duration);
                long hours = TimeUnit.MILLISECONDS.toHours(duration)
                - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
                long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                .toHours(duration));
                long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                .toMinutes(duration));
                if (days == 0) {
                res = (hours + ":" + minutes + ":" + seconds);
                } else {
                res = (days + ":" + hours + ":" + minutes + ":" + seconds);
                }
				return res;
        }
    private NPC currentNpc; //Let's make this a global variable so we dont have to create a new one each loop :)
    public String npcName = "Monk"; //This will be changed with our GUI later on
}
