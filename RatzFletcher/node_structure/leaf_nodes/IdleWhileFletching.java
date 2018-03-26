package node_structure.leaf_nodes;

import node_structure.BranchNode;
import node_structure.LeafNode;
import org.dreambot.api.methods.MethodContext;

public class IdleWhileFletching extends LeafNode {
    public IdleWhileFletching(final MethodContext context, final BranchNode parent, final boolean antiBanActive) {
        super(context, parent, antiBanActive);
    }

    @Override
    public boolean execute() {
        MethodContext.sleepUntil(() -> !isParentConditionValid(), 2000);
        return super.execute();
    }

    @Override
    public String getTaskDescription() {
        return "Idling while fletching";
    }
}
