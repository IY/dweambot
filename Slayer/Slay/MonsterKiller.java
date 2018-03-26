package Slay;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.widgets.message.Message;

import slayerMonsters.SlayerTask;
import Slay.MonsterGUI;
import Slay.MonsterVars;

@ScriptManifest(category = Category.COMBAT, name = "Ratz Slayer", author = "skengrat", version = 16.12)
public class MonsterKiller extends AbstractScript {
	
	List<Integer>order = new ArrayList<Integer>() {{
		add(0);
		add(1);
		add(2);
		add(3);
	}};
	
	String line = "";
	
	MonsterGUI gui;
	
	private int i = 0;
	
	public int eat = 0;
	public int pray = 0;
	
	
	private final MonsterVars vars = new MonsterVars();

	public void onStart() {
		setNextpray();
		setNextEat();
		gui = new MonsterGUI(vars);
		gui.setVisible(true);
		while(vars.currentTask == null) {
		 sleep(1000);
		}
		System.out.println(vars.currentTask.getName());
	}
	
	public void onPaint(Graphics g) {
		g.drawString("Pray next: " + pray + "", 10, 90);
		g.drawString("Eat next: " + eat + "", 10, 100);
		g.drawString(decode(order.get(0)), 10, 110);
		g.drawString(decode(order.get(1)), 10, 120);
		g.drawString(decode(order.get(2)), 10, 130);
		g.drawString(decode(order.get(3)), 10, 140);
	}
	
	public String decode(int x) {
		String s = "";
		switch(x) {
		case 0: s = "handleMessage;"; return s;
		case 1: s = "loot" ;return s;
		case 2: s = "neccesities;" ;return s;
		case 3: s = "fight;" ;return s;
		}
		return null;
	}
	public void onMessage(Message message) {
		line = message.getMessage();
	}
	
	public void handleMessage(String x) {
		if(x.contains("space")) {
			if(getInventory().get(item -> item != null && item.hasAction("Eat")).interact("Eat")) {
				int foodCount = getInventory().count(getInventory().get(item -> item != null && item.hasAction("Eat")).getName());
				sleepUntil(() -> getInventory().count(getInventory().get(item -> item != null && item.hasAction("Eat")).getName()) > foodCount, 2000);
			}
		}
		if(x.contains("row")) {
			stop();
		}
		if(x.contains("ammo")) {
			if(getInventory().contains("Cannonball")) {
				if(getGameObjects().closest(cannon -> cannon != null && cannon.hasAction("Fire")).interact("Fire")) {
					sleepUntil(()->x.contains("load"), Calculations.random(1344,2839));
				}
			}
		}
		if(x.contains("broken")) {
			getGameObjects().closest(cannon -> cannon != null && cannon.hasAction("Repair")).interact("Repair");
		}
		if(x.contains("run out")) {
			getInventory().interact(potion -> potion != null && potion.getName().contains("fire"), "Drink");
		}
	}
	
	public void onExit() {
		gui.setVisible(false);
	}
	
	public void setNextEat() {
		eat = (getSkills().getRealLevel(Skill.HITPOINTS)/5) + (int)(Math.random() * (getSkills().getRealLevel(Skill.HITPOINTS)/2));
	}
	
	public boolean shouldEat() {
		if(getSkills().getBoostedLevels(Skill.HITPOINTS) <= eat) {
			return true;
		} else {
			return false;
		}
	}
	

	public void setNextpray() {
		pray = 4 + (int)(Math.random() * 24);
	}
	
	public boolean shouldpray() {
		if(getSkills().getBoostedLevels(Skill.PRAYER) <= pray) {
			return true;
		} else {
			return false;
		}
	}
	
	public void neccesities() {
		if(getDialogues().inDialogue()) {
			getDialogues().continueDialogue();
		}
		if(getInventory().contains("Vial")) {
			if(getInventory().interact("Vial", "Drop")) {
				int count = getInventory().count("Vial");
				sleepUntil(() ->getInventory().count("Vial") < count, 3321);
			}
		}
		
		if(getInventory().contains(item -> item.hasAction("Bury"))) {
			if(getInventory().get(item -> item != null && item.hasAction("Bury")).interact("Bury")) {
				int count = getInventory().count(item -> item.hasAction("Bury"));
				sleepUntil(() ->getInventory().count(item -> item.hasAction("Bury")) < count, 3321);
			}
		}
		
		if(shouldEat()) {
			if(getInventory().get(item -> item != null && item.hasAction("Eat")).interact("Eat")) {
				int count = getSkills().getBoostedLevels(Skill.HITPOINTS);
				sleepUntil(() -> getSkills().getBoostedLevels(Skill.HITPOINTS) > count, 3829);
				sleepUntil(() -> getLocalPlayer().isInCombat(),Calculations.random(3129,5339));
			}
			setNextEat();
		}
		if(shouldpray() && vars.usePrayer) {
			if(getInventory().get(item -> item != null && (item.getName().contains("Pray") || item.getName().contains("restore") ) ).interact("Drink")) {
				int count = getSkills().getBoostedLevels(Skill.PRAYER);
				sleepUntil(() -> getSkills().getBoostedLevels(Skill.PRAYER) > count, 3829);
			}
			setNextpray();
		}
	}
	
	@Override
	public int onLoop() {
		Collections.shuffle(order);
		for(int i = 0; i < order.size(); i++) {
		switch(order.get(i)) {
			case 0: handleMessage(line); break;
			case 1: sleepUntil(() -> loot(), Calculations.random(7218,9123)); break;
			case 2: neccesities(); break;
			case 3: fight(); break;
			}
		}
		
		return Calculations.random(231,871);
	}
	
	public int fight() {
		final NPC monster = getNpcs().closest(npc -> npc != null && (npc.getName().equals(vars.currentTask.getName()) && !npc.isInCombat()));
		if(!getLocalPlayer().isInCombat() && !monster.isInCombat() && !getLocalPlayer().isHealthBarVisible() && monster.distance(getLocalPlayer()) < 6) {
			if(monster.interact("Attack")) {
				sleepUntil(() -> getLocalPlayer().distance(monster) == 1, 10000);
				if(!getLocalPlayer().isInCombat()) {
						return Calculations.random(50, 150);
					}
				}
			}
		return Calculations.random(231,871);
	}
	
	public boolean loot() {
		for(i = 0; i < vars.currentTask.getLoots().size(); i++) {
			final GroundItem loot = getGroundItems().closest(loot_ -> loot_ != null && loot_.getName().contains(vars.currentTask.getLoots().get(i).getName()));
				if(loot != null && loot.distance(getLocalPlayer()) < 8) {
					if(getInventory().getEmptySlots() == 0) {
						//if it is food do not pick it up
						if(loot.getName().contains("kebab")) {
							return true;
						}
						if(!(loot.getName().contains("arrow") || loot.getName().contains(" rune") || loot.getName().contains("Coins"))) {
							if(getInventory().get(item -> item != null && item.hasAction("Eat")).interact("Eat")) {
								int foodCount = getInventory().count(getInventory().get(item -> item != null && item.hasAction("Eat")).getName());
								sleepUntil(() -> getInventory().count(getInventory().get(item -> item != null && item.hasAction("Eat")).getName()) > foodCount, 2000);
							}
						}
					}
					if(loot.interact("Take")) {
						int lootsCount = getInventory().count(loot.getName());
						sleepUntil(() -> getInventory().count(loot.getName()) > lootsCount, 4000);
						i = 0;
					} 
				}
			}
		Collections.shuffle(vars.currentTask.getLoots());
		return true;
	}
}