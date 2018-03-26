package node_structure.branch_nodes;

import node_structure.BranchNode;
import org.dreambot.api.methods.MethodContext;

public class BusyFletching extends BranchNode {
    public BusyFletching(final MethodContext context) {
        super(context);
    }

    @Override
    public boolean isValid() {
        return getContext().getLocalPlayer().isAnimating();
    }
}
