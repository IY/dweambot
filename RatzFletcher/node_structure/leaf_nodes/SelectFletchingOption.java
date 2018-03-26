package node_structure.leaf_nodes;

import node_structure.BranchNode;
import node_structure.LeafNode;
import org.dreambot.api.methods.MethodContext;

public class SelectFletchingOption extends LeafNode {
    /**
     * selectedProduct details:
     * 14 - arrow shaft
     * 15 - short bow
     * 16 - long bow
     * 17 - crossbow stock
     * 18 - shield
     */
    final int selectedProduct;

    public SelectFletchingOption(final MethodContext context, final BranchNode parent) {
        super(context, parent);
        selectedProduct = 16;
    }

    @Override
    public boolean execute() {
        getContext().getWidgets().getWidgetChild(270, selectedProduct).interact();
        MethodContext.sleepUntil(() -> !isParentConditionValid(), 1000);
        return TASK_SUCCESS;
    }

    @Override
    public String getTaskDescription() {
        return "Selecting crafting product";
    }
}
