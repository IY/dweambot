package slayerMonsters;

public class Loot {

	protected boolean isStackable = false;
	private String name = "";
	
	public Loot(String name) {
		this.setName(name);
	}
	
	public Loot(String name, boolean isStackable) {
		this.setName(name);
		this.isStackable = isStackable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
