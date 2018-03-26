package node_structure.branch_nodes;

import node_structure.BranchNode;
import org.dreambot.api.methods.MethodContext;

public class BankOpen extends BranchNode {
    public BankOpen(final MethodContext context) {
        super(context);
    }

    @Override
    public boolean isValid() {
        return getContext().getBank().isOpen();
    }
}
