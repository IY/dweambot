package slayerMonsters;

import java.util.HashMap;
import org.dreambot.api.methods.map.Area;

public final class SlayerLocations {

	public static final HashMap<String, Area> SLAYERLOCATIONS = new  HashMap<String, Area>();
	
	public static Area getArea(String slayerMonster) {
		return SLAYERLOCATIONS.get(slayerMonster);
	}
}
