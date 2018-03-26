package slayerMonsters;

import java.util.ArrayList;

import org.dreambot.api.methods.map.Area;

public class Turoth extends SlayerTask {

	private static final String NAME = "Turoth";
	private static final Area AREA = new Area();
	private static final boolean ISKOUREND = false;

	@SuppressWarnings("serial")
	private static final ArrayList<Loot> LOOTS = new ArrayList<Loot>() {{
		add(new Loot("Rune"));
		add(new Loot("Nature", true));
		add(new Loot("ranarr"));
		add(new Loot("snap"));
		add(new Loot("hard"));
		add(new Loot("Mystic"));
		add(new Loot("uarm"));
		add(new Loot("Leaf"));
	}};
			
	public Turoth() {
		super();
		this.setName(NAME);
		this.setArea(AREA);
		if(ISKOUREND) { setKourend(); };
		this.setLoots(LOOTS);
	}

	@Override
	public boolean getTo() {
		// TODO Auto-generated method stub
		return false;
	}
}
