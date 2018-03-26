package node_structure.leaf_nodes;

import items.FletchingProducts;
import node_structure.BranchNode;
import node_structure.LeafNode;
import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.methods.MethodProvider;

public class Combination extends LeafNode {
    public Combination(final MethodContext context, final BranchNode parent) {
        super(context, parent);
    }

    @Override
    public boolean execute() {
        getContext().getInventory().get("Knife").useOn(FletchingProducts.MAPLE_LOGS.getName());
        MethodProvider.sleepUntil(this::isParentConditionValid, 1000);
        return TASK_SUCCESS;
    }

    @Override
    public String getTaskDescription() {
        return "Combining Knife & Maple logs";
    }
}
