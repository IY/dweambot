import micro_handler.FletchingHandler;
import node_structure.branch_nodes.ContainsItems;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

import java.awt.*;

@ScriptManifest(author = "skengrat", version = 1.09, name = "RatzFletcher", category = Category.FLETCHING)
public class BotHandler extends AbstractScript{
    private FletchingHandler fletchingHandler;
    /**
     * The initialization of objects is done here
     */
    @Override
    public void onStart() {
        fletchingHandler = new FletchingHandler(this);
        super.onStart();
    }

    /**
     * Runs the handler, following its logic
     * @return
     */
    @Override
    public int onLoop() {
        return fletchingHandler.verify();
    }

    @Override
    public void onPaint(final Graphics graphics) {
        graphics.setColor(Color.CYAN);
        graphics.drawString("DFletcher", 20, 20);
        graphics.drawString("Current task: " + fletchingHandler.getTaskDescription(), 20, 40);
        super.onPaint(graphics);
    }
}
