package slayerMonsters;

import java.util.ArrayList;

import org.dreambot.api.methods.map.Area;

public class Mutated_Bloodveld extends SlayerTask {

	private static final String NAME = "Mutated Bloodveld";
	private static final Area AREA = new Area();
	private static final boolean ISKOUREND = true;

	@SuppressWarnings("serial")
	private static final ArrayList<Loot> LOOTS = new ArrayList<Loot>() {{
			add(new Loot("lood",true));
			add(new Loot("Rune"));
	}};
			
	public Mutated_Bloodveld() {
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
