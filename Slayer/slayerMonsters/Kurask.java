package slayerMonsters;

import java.util.ArrayList;

import org.dreambot.api.methods.map.Area;

public class Kurask extends SlayerTask {

	private static final String NAME = "Kurask";
	private static final Area AREA = new Area();
	private static final boolean ISKOUREND= false;

	@SuppressWarnings("serial")
	private static final ArrayList<Loot> LOOTS = new ArrayList<Loot>() {{
		add(new Loot("napDrag"));
		add(new Loot("orstol"));
		add(new Loot("Rune"));
		add(new Loot("Ada"));
		add(new Loot("Leaf"));
		add(new Loot("Mystic"));
		add(new Loot("Nature",true));
		add(new Loot("anarr"));
		add(new Loot("Coins",true));
		add(new Loot("head"));
		add(new Loot("Papaya",true));
		add(new Loot("Coconut",true));
		add(new Loot("Big",true));
		add(new Loot("root",true));
		add(new Loot("berries",true));
		add(new Loot("hard"));
		add(new Loot("cadantine"));
		add(new Loot("avantoe"));
		add(new Loot("kwuarm"));
		add(new Loot("key"));
	}};
			
	public Kurask() {
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
