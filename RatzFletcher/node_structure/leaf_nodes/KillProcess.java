package node_structure.leaf_nodes;

import node_structure.BranchNode;
import node_structure.LeafNode;
import org.dreambot.api.methods.MethodContext;

public class KillProcess extends LeafNode {
    public KillProcess(final MethodContext context, final BranchNode parent) {
        super(context, parent);
    }

    /**
     * To be used from Branch nodes where no items are found
     * eg. No maple logs found in bank would lead to this node being referenced
     * @return
     */
    @Override
    public boolean execute() {
        return TASK_FAILURE;
    }

    @Override
    public String getTaskDescription() {
        return "Ending program";
    }
}
