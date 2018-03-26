package node_structure;

import org.dreambot.api.methods.MethodContext;

public class BranchNode implements TaskCondition {
    private final MethodContext context;
    private BranchNode success, failure;

    protected BranchNode(final MethodContext context) {
        this.context = context;
    }

    public BranchNode getSuccess() {
        return success;
    }

    public BranchNode getFailure() {
        return failure;
    }


    /**
     * Might have to revisit logic
     * @return
     */
    public boolean isChildLeaf(){
        return success == null && failure == null;
    }

    public BranchNode setSuccess(final BranchNode success) {
        return this.success = success;
    }

    public BranchNode setFailure(final BranchNode failure) {
        return this.failure = failure;
    }

    protected MethodContext getContext() {
        return context;
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
