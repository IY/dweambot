package slayerMonsters;

import java.util.ArrayList;

import org.dreambot.api.methods.map.Area;

public class Dust_devil extends SlayerTask {

	private static final String NAME = "Dust devil";
	private static final Area AREA = new Area();
	private static final boolean ISKOUREND = true;

	@SuppressWarnings("serial")
	private static final ArrayList<Loot> LOOTS = new ArrayList<Loot>() {{
		add(new Loot("Rune"));
		add(new Loot("vamb"));
		add(new Loot("staff"));
		add(new Loot("Dragon"));
		add(new Loot("Soul", true));
		add(new Loot("Chaos",true));
		add(new Loot("ranarr"));
		add(new Loot("kwuarm"));
		add(new Loot("Coins", true));
		add(new Loot("kebab"));
		add(new Loot("bar"));
		add(new Loot("key"));
		add(new Loot("ruby"));
		add(new Loot("mond"));
	}};
			
	public Dust_devil() {
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
