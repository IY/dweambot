package slayerMonsters;

import java.util.ArrayList;

import org.dreambot.api.methods.map.Area;

public abstract class SlayerTask {
	
	private String name = "";
	protected Area location = new Area();
	protected boolean isKourend = false;

	private ArrayList<Loot> loots = new ArrayList<Loot>();
	
	public SlayerTask() {
		
	}
	
	public SlayerTask(String name) {
		this.setName(name);
	}
	
	public SlayerTask(String name, ArrayList<Loot> loots) {
		this.setName(name);
		this.setLoots(loots);
	}
	
	public SlayerTask(String name, ArrayList<Loot> loots, Area area) {
		this.setName(name);
		this.setLoots(loots);
		this.location = area;
	}
	
	public SlayerTask(String name, ArrayList<Loot> loots, Area area, boolean isKourend) {
		this.setName(name);
		this.setLoots(loots);
		this.location = area;
		this.isKourend = isKourend;
	}
	
	public abstract boolean getTo();



	public String getName() {
		return this.name;
	}



	public void setName(String name) {
		this.name = name;
	}


	public ArrayList<Loot> getLoots() {
		return this.loots;
	}

	public void setLoots(ArrayList<Loot> loots) {
		this.loots = loots;
	}
	
	public void setArea(Area area) {
		this.location = area;
	}
	
	
	public Area getArea() {
		return location;
	}
	
	public void setKourend() {
		this.loots.add(new Loot("Dark"));
		this.loots.add(new Loot("Shard", true));
	}
}
