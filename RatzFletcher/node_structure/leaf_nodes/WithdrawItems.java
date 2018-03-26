package node_structure.leaf_nodes;

import items.FletchingProducts;
import node_structure.BranchNode;
import node_structure.LeafNode;
import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.methods.container.impl.bank.Bank;

public class WithdrawItems extends LeafNode {
    public WithdrawItems(final MethodContext context, final BranchNode parent) {
        super(context, parent);
    }

    @Override
    public boolean execute() {
        final MethodContext context = getContext();
        final boolean hasKnife = context.getInventory().contains("Knife");
        final Bank bank = context.getBank();
        if((!hasKnife && !bank.contains("Knife")) || !bank.contains(FletchingProducts.MAPLE_LOGS.getName())) return TASK_FAILURE;
        context.getBank().withdraw(!hasKnife ? "Knife" : FletchingProducts.MAPLE_LOGS.getName(), !hasKnife ? 1 : 27);
        MethodContext.sleepUntil(this::isParentConditionValid, 2000);
        return TASK_SUCCESS;
    }

    @Override
    public String getTaskDescription() {
        return "Withdrawing resources";
    }
}
