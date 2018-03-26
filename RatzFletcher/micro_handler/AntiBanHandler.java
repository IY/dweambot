package micro_handler;

import node_structure.AntiBanNode;
import node_structure.antiban.RightClickPlayers;
import node_structure.antiban.RotateCamera;
import org.dreambot.api.methods.MethodContext;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

class AntiBanHandler {
    private final List<AntiBanNode> antiBanNodes;
    private final Random random;

    AntiBanHandler(final MethodContext context) {
        antiBanNodes = Arrays.asList(new RightClickPlayers(context), new RotateCamera(context));
        random = new Random();
    }

    void verify() {
        if (random.nextInt(10) == 0) {
            final AntiBanNode current = antiBanNodes.get(random.nextInt(antiBanNodes.size()));
            current.execute();
        }
    }
}
