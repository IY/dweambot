package node_structure.leaf_nodes;

import node_structure.BranchNode;
import node_structure.LeafNode;
import org.dreambot.api.methods.MethodContext;

public class CloseBank extends LeafNode {
    public CloseBank(final MethodContext context, final BranchNode parent) {
        super(context, parent);
    }

    @Override
    public boolean execute() {
        getContext().getBank().close();
        MethodContext.sleepUntil(() -> !isParentConditionValid(), 1000);
        return TASK_SUCCESS;
    }

    @Override
    public String getTaskDescription() {
        return "Closing bank";
    }
}
