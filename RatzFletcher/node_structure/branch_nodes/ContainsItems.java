package node_structure.branch_nodes;

import items.FletchingProducts;
import node_structure.BranchNode;
import org.dreambot.api.methods.MethodContext;

public class ContainsItems extends BranchNode {
    public ContainsItems(final MethodContext context) {
        super(context);
    }

    @Override
    public boolean isValid() {
        return getContext().getInventory().containsAll("Knife", FletchingProducts.MAPLE_LOGS.getName());
    }
}
