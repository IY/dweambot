package node_structure.antiban;

import node_structure.AntiBanNode;
import org.dreambot.api.methods.MethodContext;

import java.util.Random;

public class RotateCamera extends AntiBanNode {
    private final Random random;
    public RotateCamera(final MethodContext context) {
        super(context);
        random = new Random();
    }

    @Override
    public boolean execute() {
        return getContext().getCamera().mouseRotateTo(random.nextInt(2047), 128 + random.nextInt(255));
    }
}
