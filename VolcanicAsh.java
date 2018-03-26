package mining;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

@ScriptManifest(category = Category.MINING, name = "Ratz Ash", author = "skengrat", 
version = 1.26)
public class VolcanicAsh extends AbstractScript {

	public int onLoop() {
		while(getLocalPlayer().isAnimating()) {
			getGameObjects().closest("Ash pile").interact("Mine");
		}
		return Calculations.random(800,1300);
	}
}
