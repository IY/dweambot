package node_structure.antiban;

import node_structure.AntiBanNode;
import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.wrappers.interactive.Player;

import java.util.List;
import java.util.Random;

public class RightClickPlayers extends AntiBanNode {
    private final Random random;
    public RightClickPlayers(final MethodContext context) {
        super(context);
        random = new Random();
    }

    @Override
    public boolean execute() {
        final MethodContext context = getContext();
        final List<Player> players = context.getPlayers().all(player -> player != null && player.isOnScreen());
        if(players != null) context.getMouse().click(players.get(random.nextInt(players.size())), true);
        return super.execute();
    }
}
