package node_structure.branch_nodes;

import node_structure.BranchNode;
import org.dreambot.api.methods.MethodContext;

public class InventoryFull extends BranchNode{
    public InventoryFull(final MethodContext context) {
        super(context);
    }

    @Override
    public boolean isValid() {
        return getContext().getInventory().getEmptySlots() < 2;
    }
}
