package node_structure.leaf_nodes;

import items.FletchingProducts;
import node_structure.BranchNode;
import node_structure.LeafNode;
import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.methods.container.impl.bank.Bank;

public class OutOfItems extends LeafNode {
    public OutOfItems(final MethodContext context, final BranchNode parent) {
        super(context, parent);
    }

    @Override
    public boolean execute() {
        final Bank bank = getContext().getBank();
        return (!bank.contains("Knife") || !bank.contains(FletchingProducts.MAPLE_LOGS.getName()))? TASK_FAILURE: TASK_SUCCESS;
    }

    @Override
    public String getTaskDescription() {
        return "We don't have resources";
    }
}
